/*
 * $Source: /home/cvsroot/manywhere/simplepool/src/net/java/dev/simplepool/SimplePoolDataSource.java,v $
 * $Revision: 1.4 $
 * $Date: 2004/03/17 23:19:02 $
 *
 * Copyright (c) 2004, Russell Beattie (http://www.russellbeattie.com/)
 * All rights reserved.
 *
 * Copyright (c) 2004, Erik C. Thauvin (http://www.thauvin.net/erik/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the authors nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.java.dev.simplepool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Basic implementation of {@link javax.sql.DataSource}.
 *
 * @author <a href="http://www.russellbeattie.com/">Russell Beattie</a>
 * @author <a href="http://www.thauvin.net/erik/">Erik C. Thauvin</a>
 * @version $Revision: 1.4 $, $Date: 2004/03/17 23:19:02 $
 * @since 1.0
 */
public class SimplePoolDataSource implements DataSource {

    private PrintWriter logWriter = new PrintWriter(System.out);
    private SimplePool pool = null;

    private String driver = "";
    private String jdbcUrl = "";
    private String user = "";
    private String password = "";
    private String minConns = "";
    private String maxConns = "";
    private String maxConnTime = "";
    private String maxCheckoutSeconds = "";

    /**
     * Initializes the connection pool.
     *
     * @throws Exception if the pool could not be intialized.
     */
    protected void init() throws Exception {

        pool = new SimplePool(driver, jdbcUrl, user, password,
                Integer.parseInt(minConns), Integer.parseInt(maxConns),
                Double.parseDouble(maxConnTime), Integer.parseInt(maxCheckoutSeconds));

    }

    /**
     * See {@link javax.sql.DataSource#getConnection()}.
     */
    public Connection getConnection() throws SQLException {

        if (pool == null) {
            try {
                init();
            } catch (Exception e) {
                throw new SQLException("Error initializing Connection Broker.");
            }
        }

        return new SimplePoolConnection(pool);

    }

    /**
     * See {@link javax.sql.DataSource#getConnection(String, String)}.
     */
    public Connection getConnection(String username, String password)
            throws SQLException {

        throw new SQLException("Not supported in this DataSource.");

    }

    /**
     * See {@link javax.sql.DataSource#setLogWriter(PrintWriter)}.
     */
    public void setLogWriter(PrintWriter out) throws SQLException {
        logWriter = out;
    }

    /**
     * See {@link javax.sql.DataSource#getLogWriter}.
     */
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    /**
     * See {@link javax.sql.DataSource#setLoginTimeout(int)}.
     */
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    /**
     * See {@link javax.sql.DataSource#getLoginTimeout}.
     */
    public int getLoginTimeout() {
        return 0;
    }

    /**
     * Closes the connection pool.
     */
    public void close() {
        pool.destroy();
        pool = null;
    }

    /**
     * Sets the JDBC driver.
     *
     * @param driver The JDBC driver. e.g. 'com.mysql.jdbc.Driver'
     *
     * @see #getDriver()
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * Gets the JDBC driver.
     *
     * @return The JDBC driver string.
     *
     * @see #setDriver(String)
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Sets the JDBC connect string.
     *
     * @param jdbcUrl The JDBC connection URL string. e.g. 'jdbc:mysql://localhost:3306/dbname'
     *
     * @see #getJdbcUrl()
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * Gets the JDBC connect string.
     *
     * @return The JDBC connection URL string.
     *
     * @see #setJdbcUrl(String)
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    /**
     * Sets the database login name.
     *
     * @param user The database login name. e.g. 'Scott'
     *
     * @see #getUser()
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the database login name.
     *
     * @return The login name string.
     *
     * @see #setUser(String)
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the database password.
     *
     * @param password The database password. e.g. 'Tiger'
     *
     * @see #getPassword()
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the database password.
     *
     * @return The password string.
     *
     * @see #setPassword(String)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the minimum number of connections to start with.
     *
     * @param minConns The minimum number of connections.
     *
     * @see #getMinConns()
     */
    public void setMinConns(String minConns) {
        this.minConns = minConns;
    }

    /**
     * Gets the minimum number of connections to start with.
     *
     * @return The minimum number of connections string.
     *
     * @see #setMinConns(String)
     */
    public String getMinConns() {
        return minConns;
    }

    /**
     * Sets the maximum number of connections in dynamic pool.
     *
     * @param maxConns The maximum number of connections.
     *
     * @see #getMaxConns()
     */
    public void setMaxConns(String maxConns) {
        this.maxConns = maxConns;
    }

    /**
     * Gets the maximum number of connections in dynamic pool.
     *
     * @return The maximum number of connection string.
     *
     * @see #setMaxConns(String)
     */
    public String getMaxConns() {
        return maxConns;
    }

    /**
     * Sets the time in days between connection resets.
     *
     * @param maxConnTime The maximum connection time.
     *
     * @see #getMaxConnTime()
     */
    public void setMaxConnTime(String maxConnTime) {
        this.maxConnTime = maxConnTime;
    }

    /**
     * Gets the time in days between connection resets.
     *
     * @return The maximum connection time string.
     *
     * @see #setMaxConnTime(String)
     */
    public String getMaxConnTime() {
        return maxConnTime;
    }

    /**
     * Sets the max time a connection can be checked out before being recycled.
     *
     * @param maxCheckoutSeconds The maximum number of seconds to wait before recycling a connection.
     *
     * @see #getMaxCheckoutSeconds()
     */
    public void setMaxCheckoutSeconds(String maxCheckoutSeconds) {
        this.maxCheckoutSeconds = maxCheckoutSeconds;
    }

    /**
     * Gets the max time a connection can be checked out before being recycled.
     *
     * @return The maximum checkout time string.
     *
     * @see #setMaxCheckoutSeconds(String)
     */
    public String getMaxCheckoutSeconds() {
        return maxCheckoutSeconds;
    }

}