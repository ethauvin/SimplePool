/*
 * $Source$
 * $Revision$
 * $Date$
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
 * @version $Revision$, $Date$
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
     *
     * @see javax.sql.DataSource#getConnection()
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
     *
     * @see javax.sql.DataSource#getConnection(String, String)
     */
    public Connection getConnection(String username, String password)
            throws SQLException {

        throw new SQLException("Not supported in this DataSource.");

    }

    /**
     * See {@link javax.sql.DataSource#setLogWriter(PrintWriter)}.
     *
     * @see javax.sql.DataSource#setLogWriter(PrintWriter)
     */
    public void setLogWriter(PrintWriter out) throws SQLException {
        logWriter = out;
    }

    /**
     * See {@link javax.sql.DataSource#getLogWriter}.
     *
     * @see javax.sql.DataSource#getLogWriter
     */
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    /**
     * See {@link javax.sql.DataSource#setLoginTimeout(int)}.
     *
     * @see javax.sql.DataSource#setLoginTimeout(int)
     */
    public void setLoginTimeout(int seconds) throws SQLException {
    }

    /**
     * See {@link javax.sql.DataSource#getLoginTimeout}.
     *
     * @see javax.sql.DataSource#getLoginTimeout
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
     */
    public void setDriver(String val) {
        driver = val;
    }

    /**
     * Gets the JDBC driver.
     */
    public String getDriver() {
        return driver;
    }

    /**
     * Sets the JDBC connect string.
     */
    public void setJdbcUrl(String val) {
        jdbcUrl = val;
    }

    /**
     * Sets the JDBC connect string.
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    /**
     * Sets the database login name.
     */
    public void setUser(String val) {
        user = val;
    }

    /**
     * Gets the database login name.
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the database password.
     */
    public void setPassword(String val) {
        password = val;
    }

    /**
     * Gets the database password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the minimum number of connections to start with.
     */
    public void setMinConns(String val) {
        minConns = val;
    }

    /**
     * Gets the minimum number of connections to start with.
     */
    public String getMinConns() {
        return minConns;
    }

    /**
     * Sets the maximum number of connections in dynamic pool.
     */
    public void setMaxConns(String val) {
        maxConns = val;
    }

    /**
     * Gets the maximum number of connections in dynamic pool.
     */
    public String getMaxConns() {
        return maxConns;
    }

    /**
     * Sets the time in days between connection resets.
     */
    public void setMaxConnTime(String val) {
        maxConnTime = val;
    }

    /**
     * Gets the time in days between connection resets.
     */
    public String getMaxConnTime() {
        return maxConnTime;
    }

    /**
     * Sets the max time a connection can be checked out before being recycled.
     */
    public void setMaxCheckoutSeconds(String val) {
        maxCheckoutSeconds = val;
    }

    /**
     * Sets the max time a connection can be checked out before being recycled.
     */
    public String getMaxCheckoutSeconds() {
        return maxCheckoutSeconds;
    }

}