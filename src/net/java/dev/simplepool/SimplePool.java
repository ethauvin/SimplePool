/*
 * $Source: /zpool01/javanet/scm/svn/tmp/cvs2svn/simplepool/src/net/java/dev/simplepool/SimplePool.java,v $
 * $Revision: 1.1 $
 * $Date: 2004-03-28 02:04:57 $
 *
 * Copyright (c) 2002, Marc A. Mnich (http://www.javaexchange.com/)
 * All rights reserved.
 *
 * Copyright (c) 2004, Russell Beattie (http://www.russellbeattie.com/)
 * All rights reserved.
 *
 * Copyright (c) 2004, Erik C. Thauvin (http://www.thauvin.net/erik/)
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License, please see:
 *
 *     http://www.javaexchange.com/GPL.html
 */
package net.java.dev.simplepool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Creates and manages a pool of database connections.
 *
 * @author <a href="http://www.javaexchange.com/">Marc A. Mnich</a>
 * @author <a href="http://www.russellbeattie.com/">Russell Beattie</a>
 * @author <a href="http://www.thauvin.net/erik/">Erik C. Thauvin</a>
 * @version $Revision: 1.1 $, $Date: 2004-03-28 02:04:57 $
 * @since 1.0
 */
public class SimplePool implements Runnable {

    private static final Log log = LogFactory.getLog(SimplePool.class);

    private Thread runner;

    private Connection[] connPool;
    private int[] connStatus;

    private long[] connLockTime;
    private long[] connCreateDate;
    private String[] connID;
    private String driver;
    private String jdbcUrl;
    private String user;
    private String password;
    private int currConnections;
    private int connLast;
    private int maxConns;
    private int maxConnMSec;
    private int maxCheckoutSeconds = 60;
    //available: set to false on destroy, checked by getConnection()
    private boolean available = true;

    private SQLWarning currSQLWarning;


    /**
     * Creates a new Connection Pool.
     *
     * @param driver             JDBC driver. e.g. 'oracle.jdbc.driver.OracleDriver'
     * @param jdbcUrl            JDBC connect string. e.g. 'jdbc:oracle:thin:@203.92.21.109:1526:orcl'
     * @param user               Database login name. e.g. 'Scott'
     * @param password           Database password.  e.g. 'Tiger'
     * @param minConns           Minimum number of connections to start with.
     * @param maxConns           Maximum number of connections in dynamic pool.
     * @param maxConnTime        Time in days between connection resets. (Reset does a basic cleanup)
     * @param maxCheckoutSeconds Max time a connection can be checked out before being recycled. Zero value turns option
     *                           off, default is 60 seconds.
     *
     * @throws IOException If the pool cannot be created.
     */
    public SimplePool(String driver, String jdbcUrl, String user, String password, int minConns, int maxConns,
                      double maxConnTime, int maxCheckoutSeconds)
            throws IOException {

        this.connPool = new Connection[maxConns];
        this.connStatus = new int[maxConns];
        this.connLockTime = new long[maxConns];
        this.connCreateDate = new long[maxConns];
        this.connID = new String[maxConns];
        this.currConnections = minConns;
        this.maxConns = maxConns;
        this.driver = driver;
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.maxCheckoutSeconds = maxCheckoutSeconds;
        this.maxConnMSec = (int) (maxConnTime * 86400000.0);  //86400 sec/day
        if (this.maxConnMSec < 30000) {  // Recycle no less than 30 seconds.
            this.maxConnMSec = 30000;
        }

        log.info("-----------------------------------------");
        log.info("Starting Connection Pool:");
        log.info("driver = " + driver);
        log.info("jdbcUrl = " + jdbcUrl);
        log.info("user = " + user);
        log.info("minconnections = " + minConns);
        log.info("maxconnections = " + maxConns);
        log.info("Total refresh interval = " + maxConnTime + " days");
        log.info("maxCheckoutSeconds = " + maxCheckoutSeconds);
        log.info("-----------------------------------------");
        
        
        // Initialize the pool of connections with the mininum connections:
        // Problems creating connections may be caused during reboot when the
        // servlet is started before the database is ready.  Handle this
        // by waiting and trying again.  The loop allows 5 minutes for
        // db reboot.
        boolean connectionsSucceeded = false;
        int dbLoop = 20;

        try {
            for (int i = 1; i < dbLoop; i++) {
                try {
                    for (int j = 0; j < currConnections; j++) {
                        createConn(j);
                    }
                    connectionsSucceeded = true;
                    break;
                } catch (SQLException e) {

                    log.error("Attempt (" + String.valueOf(i) + " of " + String.valueOf(dbLoop) +
                            ") failed to create new connections set at startup.", e);
                    log.warn("Will try again in 15 seconds...");

                    try {
                        Thread.sleep(15000L);
                    } catch (InterruptedException ignore) {
                        ;
                    }
                }
            }
            if (!connectionsSucceeded) { // All attempts at connecting to db exhausted
                throw new IOException("All attempts at connecting to Database exhausted.");
            }

        } catch (Exception e) {
            log.fatal(e.getMessage(), e);
            throw new IOException(e.getMessage());
        }
        
        // Fire up the background housekeeping thread
        runner = new Thread(this);
        runner.start();

    }


    /**
     * Housekeeping thread.  Runs in the background with low CPU overhead. Connections are checked for warnings and
     * closure and are periodically restarted.
     * <p/>
     * This thread is a catchall for corrupted connections and prevents the buildup of open cursors. (Open cursors
     * result when the application fails to close a Statement).
     * <p/>
     * This method acts as fault tolerance for bad connection/statement programming.
     */
    public void run() {
        boolean forever = true;
        Statement stmt = null;
        long maxCheckoutMillis = (long) (maxCheckoutSeconds * 1000);

        while (forever) {

            // Get any Warnings on connections and print to event file
            for (int i = 0; i < currConnections; i++) {
                try {
                    currSQLWarning = connPool[i].getWarnings();
                    if (currSQLWarning != null) {

                        log.debug("Warnings on connection [" + String.valueOf(i) + "]: " + currSQLWarning);

                        connPool[i].clearWarnings();
                    }
                } catch (SQLException e) {
                    log.debug("Cannot access connection [" + String.valueOf(i) + "] warnings.", e);
                }
            }

            for (int i = 0; i < currConnections; i++) { // Do for each connection

                long age = System.currentTimeMillis() - connCreateDate[i];

                try {  // Test the connection with createStatement call
                    synchronized (connStatus) {
                        if (connStatus[i] > 0) { // In use, catch it next time!

                            // Check the time it's been checked out and recycle
                            long timeInUse = System.currentTimeMillis() - connLockTime[i];
                            log.warn("Connection [" + i + "] in use for " + timeInUse + " ms.");
                            if (maxCheckoutMillis != 0) {
                                if (timeInUse > maxCheckoutMillis) {
                                    log.warn("Connection [" + i + "] failed to be returned in time. Recycling...");
                                    throw new SQLException();
                                }
                            }

                            continue;
                        }
                        connStatus[i] = 2; // Take offline (2 indicates housekeeping lock)
                    }


                    if (age > maxConnMSec) {  // Force a reset at the max conn time
                        throw new SQLException();
                    }

                    stmt = connPool[i].createStatement();
                    connStatus[i] = 0;  // Connection is O.K.
                    log.trace("Connection [" + String.valueOf(i) + "] confirmed.");

                    // Some DBs return an object even if DB is shut down
                    if (connPool[i].isClosed()) {
                        throw new SQLException();
                    }


                    // Connection has a problem, restart it
                } catch (SQLException e) {

                    log.debug("Recycling connection  [" + String.valueOf(i) + ']');
                    try {
                        connPool[i].close();
                    } catch (SQLException e0) {
                        log.warn("Can't close connection [" + String.valueOf(i) +
                                "]. Might have been closed already. Trying to recycle anyway...", e);
                    }

                    try {
                        createConn(i);
                    } catch (SQLException e1) {
                        log.warn("Failed to create connection [" + String.valueOf(i) + ']', e1);

                        connStatus[i] = 0;  // Can't open, try again next time
                    }
                } finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                        }
                    } catch (SQLException ignore) {
                        ;
                    }
                }

            }

            try {
                Thread.sleep(20000L);
            }  // Wait 20 seconds for next cycle
            catch (InterruptedException e) {
                // Returning from the run method sets the internal
                // flag referenced by Thread.isAlive() to false.
                // This is required because we don't use stop() to
                // shutdown this thread.
                return;
            }

        }

    } // End run

    /**
     * This method hands out the connections in round-robin order. This prevents a faulty connection from locking up an
     * application entirely. A browser 'refresh' will get the next connection while the faulty connection is cleaned up
     * by the housekeeping thread.
     * <p/>
     * If the min number of threads are ever exhausted, new threads are added up the the max thread count. Finally, if
     * all threads are in use, this method waits 2 seconds and tries again, up to ten times. After that, it returns a
     * null.
     *
     * @return A connection from the pool.
     */
    public Connection getConnection() {

        Connection conn = null;

        if (available) {
            boolean gotOne = false;

            for (int outerloop = 1; outerloop <= 10; outerloop++) {

                try {
                    int loop = 0;
                    int roundRobin = connLast + 1;
                    if (roundRobin >= currConnections) {
                        roundRobin = 0;
                    }

                    do {
                        synchronized (connStatus) {
                            if ((connStatus[roundRobin] < 1) && (!connPool[roundRobin].isClosed())) {
                                conn = connPool[roundRobin];
                                connStatus[roundRobin] = 1;
                                connLockTime[roundRobin] = System.currentTimeMillis();
                                connLast = roundRobin;
                                gotOne = true;
                                break;
                            } else {
                                loop++;
                                roundRobin++;
                                if (roundRobin >= currConnections) {
                                    roundRobin = 0;
                                }
                            }
                        }
                    } while ((!gotOne) && (loop < currConnections));

                } catch (SQLException e1) {
                    log.debug(e1.getMessage(), e1);
                }

                if (gotOne) {
                    break;
                } else {
                    synchronized (this) {  // Add new connections to the pool
                        if (currConnections < maxConns) {
                            try {
                                createConn(currConnections);
                                currConnections++;
                            } catch (SQLException e) {
                                log.error("Unable to create new connection.", e);
                            }
                        }
                    }

                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException ignore) {
                        ;
                    }

                    log.debug("Connections Exhausted. Will wait and try again in loop " + String.valueOf(outerloop));
                }

            } // End of try 10 times loop

        } else {
            log.debug("Unsuccessful getConnection() request during destroy()");
        } // End if(available)

        log.debug("Handing out connection [" + idOfConnection(conn) + "]: " +
                (new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a")).format(new Date()));

        return conn;

    }

    /**
     * Returns the local JDBC ID for a connection.
     *
     * @param conn The connection object.
     *
     * @return The local JDBC ID for the connection.
     */
    public int idOfConnection(Connection conn) {
        int match = -1;
        String tag;

        try {
            tag = conn.toString();
        } catch (NullPointerException e1) {
            tag = "none";
        }

        for (int i = 0; i < currConnections; i++) {
            if (connID[i].equals(tag)) {
                match = i;
                break;
            }
        }
        return match;
    }

    /**
     * Frees a connection. Replaces connection back into the main pool for reuse.
     *
     * @param conn The connection object.
     *
     * @return  A status or empty string.
     */
    public String freeConnection(Connection conn) {
        String res = "";

        int thisconn = idOfConnection(conn);
        if (thisconn >= 0) {
            connStatus[thisconn] = 0;
            res = "freed " + conn.toString();
            log.debug("Freed connection [" + String.valueOf(thisconn) + ']');
        } else {
            log.error("Could not free connection [" + String.valueOf(thisconn) + ']');
        }

        return res;

    }

    /**
     * Returns the age of a connection -- the time since it was handed out to an application.
     *
     * @param conn The connection object.
     *
     * @return The age of the connection.
     */
    public long getAge(Connection conn) { // Returns the age of the connection in millisec.
        int thisconn = idOfConnection(conn);
        return System.currentTimeMillis() - connLockTime[thisconn];
    }

    private void createConn(int i)
            throws SQLException {

        Date now = new Date();

        try {
            Class.forName(driver);

            connPool[i] = DriverManager.getConnection
                    (jdbcUrl, user, password);

            connStatus[i] = 0;
            connID[i] = connPool[i].toString();
            connLockTime[i] = 0L;
            connCreateDate[i] = now.getTime();
        } catch (ClassNotFoundException e2) {
            log.debug("Error creating connection. The driver could not be loaded.", e2);
        }

        log.debug("Opening connection [" + String.valueOf(i) + "]: " + connPool[i].toString());
    }

    /**
     * Shuts down the housekeeping thread and closes all connections in the pool. Call this method from the destroy()
     * method of the servlet.
     * <p/>
     * Multi-phase shutdown having following sequence:
     * </p>
     * <ol>
     * <li><code>getConnection()</code> will refuse to return connections.</li>
     * <li>The housekeeping thread is shut down.<br>
     * Up to the time of <code>millis</code> milliseconds after shutdown of the housekeeping thread,
     * <code>freeConnection()</code> can still be called to return used connections.<br>
     * After <code>millis</code> milliseconds after the shutdown of the housekeeping thread, all connections in the pool
     * are closed.</li>
     * <li>If any connections were in use while being closed then a <code>SQLException</code> is thrown.</li>
     * <li>The log is closed.</li>
     * </ol>
     * <p/>
     * Call this method from a servlet destroy() method.
     *
     * @param millis the time to wait in milliseconds.
     *
     * @throws SQLException if connections were in use after <code>millis</code>.
     */
    public void destroy(int millis) throws SQLException {

        log.info("Shutting down SimplePool.");
    	
        // Checking for invalid negative arguments is not necessary,
        // Thread.join() does this already in runner.join().

        // Stop issuing connections
        available = false;

        // Shut down the background housekeeping thread
        runner.interrupt();

        // Wait until the housekeeping thread has died.
        try {
            runner.join((long) millis);
        } catch (InterruptedException ignore) {
            ;
        }
        
        // The housekeeping thread could still be running
        // (e.g. if millis is too small). This case is ignored.
        // At worst, this method will throw an exception with the
        // clear indication that the timeout was too short.

        long startTime = System.currentTimeMillis();

        // Wait for freeConnection() to return any connections
        // that are still used at this time.
        int useCount;
        while ((useCount = getUseCount()) > 0 && System.currentTimeMillis() - startTime <= millis) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException ignore) {
                ;
            }
        }

        // Close all connections, whether safe or not
        for (int i = 0; i < currConnections; i++) {
            try {
                connPool[i].close();
            } catch (SQLException e1) {
                log.debug("Cannot close connections on Destroy.");
            }
        }

        if (useCount > 0) {
            //bt-test successful
            String msg = "Unsafe shutdown: Had to close " + useCount + " active DB connections after " + millis +
                    "ms.";
            log.error(msg);
            // Throwing following Exception is essential because servlet authors
            // are likely to have their own error logging requirements.
            throw new SQLException(msg);
        }
    }//End destroy()


    /**
     * Less safe shutdown. Uses default timeout value.
     * <p/>
     * This method simply calls the <code>destroy()</code> method with a <code>millis</code> value of 10000 (10 seconds)
     * and ignores <code>SQLException</code> thrown by that method.
     *
     * @see #destroy(int)
     */
    public void destroy() {
        try {
            destroy(10000);
        } catch (SQLException e) {
            ;
        }
    }


    /**
     * Returns the number of connections in use.
     * <p/>
     * This method could be reduced to return a counter that is maintained by all methods that update connStatus.
     * However, it is more efficient to do it this way because: Updating the counter would put an additional burden on
     * the most frequently used methods; in comparison, this method is rarely used (although essential).
     *
     * @return The number of connections in use.
     */
    public int getUseCount() {
        int useCount = 0;
        synchronized (connStatus) {
            for (int i = 0; i < currConnections; i++) {
                if (connStatus[i] > 0) { // In use
                    useCount++;
                }
            }
        }
        return useCount;
    }//End getUseCount()

    /**
     * Returns the number of connections in the dynamic pool.
     *
     * @return The number of connections in the pool.
     */
    public int getSize() {
        return currConnections;
    }//End getSize()

}// End class