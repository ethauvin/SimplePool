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


import java.sql.*;
import java.util.Map;

/**
 * SimplePoolConnection
 *
 * @author Russell Beattie
 * @author Erik C. Thauvin
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class SimplePoolConnection implements Connection {

    protected Connection conn;
    protected SimplePool broker;

    public SimplePoolConnection(SimplePool broker) {
        this.broker = broker;
        conn = broker.getConnection();
    }

    public void setAutoCommit(boolean arg0)
            throws SQLException {
        conn.setAutoCommit(arg0);
    }

    public boolean getAutoCommit()
            throws SQLException {
        return conn.getAutoCommit();
    }


    public void setCatalog(String arg0)
            throws SQLException {
        conn.setCatalog(arg0);
    }


    public String getCatalog()
            throws SQLException {
        return conn.getCatalog();
    }


    public boolean isClosed()
            throws SQLException {
        return conn.isClosed();
    }


    public void setHoldability(int arg0)
            throws SQLException {
        conn.setHoldability(arg0);
    }


    public int getHoldability()
            throws SQLException {
        return conn.getHoldability();
    }


    public DatabaseMetaData getMetaData()
            throws SQLException {
        return conn.getMetaData();
    }


    public void setReadOnly(boolean arg0)
            throws SQLException {
        conn.setReadOnly(arg0);
    }


    public boolean isReadOnly()
            throws SQLException {
        return conn.isReadOnly();
    }


    public Savepoint setSavepoint()
            throws SQLException {
        return conn.setSavepoint();
    }


    public Savepoint setSavepoint(String arg0)
            throws SQLException {
        return conn.setSavepoint(arg0);
    }


    public void setTransactionIsolation(int arg0)
            throws SQLException {
        conn.setTransactionIsolation(arg0);
    }


    public int getTransactionIsolation()
            throws SQLException {
        return conn.getTransactionIsolation();
    }


    public void setTypeMap(Map arg0)
            throws SQLException {
        conn.setTypeMap(arg0);
    }


    public Map getTypeMap()
            throws SQLException {
        return conn.getTypeMap();
    }


    public SQLWarning getWarnings()
            throws SQLException {
        return conn.getWarnings();
    }


    public void clearWarnings()
            throws SQLException {
        conn.clearWarnings();
    }


    public void close()
            throws SQLException {
        broker.freeConnection(conn);
    }


    public void commit()
            throws SQLException {
        conn.commit();
    }


    public Statement createStatement()
            throws SQLException {
        return conn.createStatement();
    }


    public Statement createStatement(int arg0, int arg1)
            throws SQLException {
        return conn.createStatement(arg0, arg1);
    }


    public Statement createStatement(int arg0, int arg1, int arg2)
            throws SQLException {
        return conn.createStatement(arg0, arg1, arg2);
    }


    public String nativeSQL(String arg0)
            throws SQLException {
        return conn.nativeSQL(arg0);
    }


    public CallableStatement prepareCall(String arg0)
            throws SQLException {
        return conn.prepareCall(arg0);
    }


    public CallableStatement prepareCall(String arg0, int arg1, int arg2)
            throws SQLException {
        return conn.prepareCall(arg0, arg1, arg2);
    }


    public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3)
            throws SQLException {
        return conn.prepareCall(arg0, arg1, arg2, arg3);
    }


    public PreparedStatement prepareStatement(String arg0)
            throws SQLException {
        return conn.prepareStatement(arg0);
    }


    public PreparedStatement prepareStatement(String arg0, int arg1)
            throws SQLException {
        return conn.prepareStatement(arg0);
    }


    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
            throws SQLException {
        return conn.prepareStatement(arg0, arg1, arg2);
    }


    public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3)
            throws SQLException {
        return conn.prepareStatement(arg0, arg1, arg2, arg3);
    }


    public PreparedStatement prepareStatement(String arg0, int[] arg1)
            throws SQLException {
        return conn.prepareStatement(arg0, arg1);
    }


    public PreparedStatement prepareStatement(String arg0, String[] arg1)
            throws SQLException {
        return conn.prepareStatement(arg0, arg1);
    }


    public void releaseSavepoint(Savepoint arg0)
            throws SQLException {
        conn.releaseSavepoint(arg0);
    }


    public void rollback()
            throws SQLException {
        conn.rollback();
    }


    public void rollback(Savepoint arg0)
            throws SQLException {
        conn.rollback(arg0);
    }
}
