/**
 * $Source$ $Revision: 1.1
 * $ $Date$ Copyright (c) 2004, Russell Beattie (http://www.russellbeattie.com/) All rights
 * reserved. Copyright (c) 2004, Erik C. Thauvin (http://www.thauvin.net/erik/) All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met: Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the authors nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE
 * COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.java.dev.simplepool;

import java.sql.*;
import java.util.Map;


/**
 * Basic implementation of <code>javax.sql.Connnection</code>.
 *
 * @author <a href="http://www.russellbeattie.com/">Russell Beattie</a>
 * @author <a href="http://www.thauvin.net/erik/">Erik C. Thauvin</a>
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class SimplePoolConnection implements Connection {

    private Connection conn;
    private SimplePool broker;

    /**
     * Creates a new SimplePoolConnection object.
     *
     * @param broker The connection pool.
     */
    public SimplePoolConnection(SimplePool broker) {
        this.broker = broker;
        conn = broker.getConnection();
    }

    /**
     * See {@link java.sql.Connection#setAutoCommit(boolean)}.
     *
     * @see java.sql.Connection#setAutoCommit(boolean)
     */
    public void setAutoCommit(boolean autoCommit)
            throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    /**
     * See {@link java.sql.Connection#getAutoCommit}.
     *
     * @see java.sql.Connection#getAutoCommit
     */
    public boolean getAutoCommit()
            throws SQLException {
        return conn.getAutoCommit();
    }

    /**
     * See {@link java.sql.Connection#setCatalog(String)}.
     *
     * @see java.sql.Connection#setCatalog(String)
     */
    public void setCatalog(String catalog)
            throws SQLException {
        conn.setCatalog(catalog);
    }

    /**
     * See {@link java.sql.Connection#getCatalog()}.
     *
     * @see java.sql.Connection#getCatalog()
     */
    public String getCatalog()
            throws SQLException {
        return conn.getCatalog();
    }

    /**
     * See {@link java.sql.Connection#isClosed}.
     *
     * @see java.sql.Connection#isClosed
     */
    public boolean isClosed()
            throws SQLException {
        return conn.isClosed();
    }

    /**
     * See {@link java.sql.Connection#setHoldability(int)}.
     *
     * @see java.sql.Connection#setHoldability(int)
     */
    public void setHoldability(int holdability)
            throws SQLException {
        conn.setHoldability(holdability);
    }

    /**
     * See {@link java.sql.Connection#getHoldability}.
     *
     * @see java.sql.Connection#getHoldability
     */
    public int getHoldability()
            throws SQLException {
        return conn.getHoldability();
    }

    /**
     * See {@link java.sql.Connection#getMetaData}.
     *
     * @see java.sql.Connection#getMetaData
     */
    public DatabaseMetaData getMetaData()
            throws SQLException {
        return conn.getMetaData();
    }

    /**
     * See {@link java.sql.Connection#setReadOnly(boolean)}.
     *
     * @see java.sql.Connection#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly)
            throws SQLException {
        conn.setReadOnly(readOnly);
    }

    /**
     * See {@link java.sql.Connection#isReadOnly}.
     *
     * @see java.sql.Connection#isReadOnly
     */
    public boolean isReadOnly()
            throws SQLException {
        return conn.isReadOnly();
    }

    /**
     * See {@link java.sql.Connection#setSavepoint}.
     *
     * @see java.sql.Connection#setSavepoint
     */
    public Savepoint setSavepoint()
            throws SQLException {
        return conn.setSavepoint();
    }

    /**
     * See {@link java.sql.Connection#setSavepoint(String)}.
     *
     * @see java.sql.Connection#setSavepoint(String)
     */
    public Savepoint setSavepoint(String savepoint)
            throws SQLException {
        return conn.setSavepoint(savepoint);
    }

    /**
     * See {@link java.sql.Connection#setTransactionIsolation(int)}.
     *
     * @see java.sql.Connection#setTransactionIsolation(int)
     */
    public void setTransactionIsolation(int level)
            throws SQLException {
        conn.setTransactionIsolation(level);
    }

    /**
     * See {@link java.sql.Connection#getTransactionIsolation}.
     *
     * @see java.sql.Connection#getTransactionIsolation
     */
    public int getTransactionIsolation()
            throws SQLException {
        return conn.getTransactionIsolation();
    }

    /**
     * See {@link java.sql.Connection#setTypeMap(Map)}.
     *
     * @see java.sql.Connection#setTypeMap(Map)
     */
    public void setTypeMap(Map map)
            throws SQLException {
        conn.setTypeMap(map);
    }

    /**
     * See {@link java.sql.Connection#getTypeMap}.
     *
     * @see java.sql.Connection#getTypeMap
     */
    public Map getTypeMap()
            throws SQLException {
        return conn.getTypeMap();
    }

    /**
     * See {@link java.sql.Connection#getWarnings}.
     *
     * @see java.sql.Connection#getWarnings
     */
    public SQLWarning getWarnings()
            throws SQLException {
        return conn.getWarnings();
    }

    /**
     * See {@link java.sql.Connection#clearWarnings}.
     *
     * @see java.sql.Connection#clearWarnings
     */
    public void clearWarnings()
            throws SQLException {
        conn.clearWarnings();
    }

    /**
     * See {@link java.sql.Connection#close}.
     *
     * @see java.sql.Connection#close
     */
    public void close()
            throws SQLException {
        broker.freeConnection(conn);
    }

    /**
     * See {@link java.sql.Connection#commit}.
     *
     * @see java.sql.Connection#commit
     */
    public void commit()
            throws SQLException {
        conn.commit();
    }

    /**
     * See {@link java.sql.Connection#createStatement}.
     *
     * @see java.sql.Connection#createStatement
     */
    public Statement createStatement()
            throws SQLException {
        return conn.createStatement();
    }

    /**
     * See {@link java.sql.Connection#createStatement(int, int)}.
     *
     * @see java.sql.Connection#createStatement(int, int)
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * See {@link java.sql.Connection#createStatement(int, int, int)}.
     *
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * See {@link java.sql.Connection#nativeSQL(String)}.
     *
     * @see java.sql.Connection#nativeSQL(String)
     */
    public String nativeSQL(String sql)
            throws SQLException {
        return conn.nativeSQL(sql);
    }

    /**
     * See {@link java.sql.Connection#prepareCall(String)}.
     *
     * @see java.sql.Connection#prepareCall(String)
     */
    public CallableStatement prepareCall(String sql)
            throws SQLException {
        return conn.prepareCall(sql);
    }

    /**
     * See {@link java.sql.Connection#prepareCall(String, int, int)}.
     *
     * @see java.sql.Connection#prepareCall(String, int, int)
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * See {@link java.sql.Connection#prepareCall(String, int, int, int)}.
     *
     * @see java.sql.Connection#prepareCall(String, int, int, int)
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * See {@link java.sql.Connection#prepareStatement(String)}.
     *
     * @see java.sql.Connection#prepareStatement(String)
     */
    public PreparedStatement prepareStatement(String sql)
            throws SQLException {
        return conn.prepareStatement(sql);
    }

    /**
     * See {@link java.sql.Connection#prepareStatement(String)}.
     *
     * @see java.sql.Connection#prepareStatement(String)
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return conn.prepareStatement(sql);
    }

    /**
     * See {@link java.sql.Connection#prepareStatement(String, int, int)}.
     *
     * @see java.sql.Connection#prepareStatement(String, int, int)
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * See {@link java.sql.Connection#prepareStatement(String, int, int, int)}.
     *
     * @see java.sql.Connection#prepareStatement(String, int, int, int)
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * See {@link java.sql.Connection#prepareStatement(String, int[])}.
     *
     * @see java.sql.Connection#prepareStatement(String, int[])
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        return conn.prepareStatement(sql, columnIndexes);
    }

    /**
     * See {@link java.sql.Connection#prepareStatement(String, String[])}.
     *
     * @see java.sql.Connection#prepareStatement(String, String[])
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return conn.prepareStatement(sql, columnNames);
    }

    /**
     * See {@link java.sql.Connection#releaseSavepoint(Savepoint)}.
     *
     * @see java.sql.Connection#releaseSavepoint(Savepoint)
     */
    public void releaseSavepoint(Savepoint savepoint)
            throws SQLException {
        conn.releaseSavepoint(savepoint);
    }

    /**
     * See {@link java.sql.Connection#rollback}.
     *
     * @see java.sql.Connection#rollback
     */
    public void rollback()
            throws SQLException {
        conn.rollback();
    }

    /**
     * See {@link java.sql.Connection#rollback(Savepoint)}.
     *
     * @see java.sql.Connection#rollback(Savepoint)
     */
    public void rollback(Savepoint savepoint)
            throws SQLException {
        conn.rollback(savepoint);
    }
}
