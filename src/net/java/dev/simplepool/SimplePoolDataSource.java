/**
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
 * SimplePoolDataSource
 *
 * @author Russell Beattie
 * @author Erik C. Thauvin
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class SimplePoolDataSource implements DataSource {

    protected PrintWriter logWriter = new PrintWriter(System.out);

    protected SimplePool broker = null;

    private String driver = "";
    private String jdbcUrl = "";
    private String user = "";
    private String password = "";
    private String minConns = "";
    private String maxConns = "";
    private String maxConnTime = "";
    private String maxCheckoutSeconds = "";

    public SimplePoolDataSource() {
        ;
    }

    public void init() throws Exception {

        broker = new SimplePool(driver, jdbcUrl, user, password,
                Integer.parseInt(minConns), Integer.parseInt(maxConns),
                Double.parseDouble(maxConnTime), Integer.parseInt(maxCheckoutSeconds));

    }

    public Connection getConnection() throws SQLException {

        if (broker == null) {
            try {
                init();
            } catch (Exception e) {
                throw new SQLException("Error initializing Connection Broker.");
            }
        }

        return new SimplePoolConnection(broker);

    }

    public Connection getConnection(String user, String password)
            throws SQLException {

        throw new SQLException("Not supported in this DataSource.");

    }

    public void setLogWriter(PrintWriter output) throws SQLException {
        logWriter = output;
    }

    public PrintWriter getLogWriter() {
        return logWriter;
    }

    public void setLoginTimeout(int seconds) throws SQLException {
    }

    public int getLoginTimeout() {
        return 0;
    }

    public void close() {
        broker.destroy();
        broker = null;
    }


// GET/SETs

    public void setDriver(String val) {
        driver = val;
    }

    public String getDriver() {
        return driver;
    }

    public void setJdbcUrl(String val) {
        jdbcUrl = val;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setUser(String val) {
        user = val;
    }

    public String getUser() {
        return user;
    }

    public void setPassword(String val) {
        password = val;
    }

    public String getPassword() {
        return password;
    }

    public void setMinConns(String val) {
        minConns = val;
    }

    public String getMinConns() {
        return minConns;
    }

    public void setMaxConns(String val) {
        maxConns = val;
    }

    public String getMaxConns() {
        return maxConns;
    }

    public void setMaxConnTime(String val) {
        maxConnTime = val;
    }

    public String getMaxConnTime() {
        return maxConnTime;
    }

    public void setMaxCheckoutSeconds(String val) {
        maxCheckoutSeconds = val;
    }

    public String getMaxCheckoutSeconds() {
        return maxCheckoutSeconds;
    }

}