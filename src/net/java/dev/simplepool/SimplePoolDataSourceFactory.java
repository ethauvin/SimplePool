/*
 * $Source: /home/cvsroot/manywhere/simplepool/src/net/java/dev/simplepool/SimplePoolDataSourceFactory.java,v $
 * $Revision: 1.5 $
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

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Hashtable;

/**
 * JNDI object factory that creates an instance of {@link SimplePoolDataSource}.
 *
 * @author <a href="http://www.russellbeattie.com/">Russell Beattie</a>
 * @author <a href="http://www.thauvin.net/erik/">Erik C. Thauvin</a>
 * @version $Revision: 1.5 $, $Date: 2004/03/17 23:19:02 $
 * @since 1.0
 */
public class SimplePoolDataSourceFactory implements ObjectFactory {
    /**
     * Creates a {@link SimplePoolDataSource} instance.
     *
     * @param obj  The object containing location or reference information that is used in creating the
     *             <code>DataSource</code>.
     * @param name The name of this object relative to <code>ctx</code>, or null if no name is specified.
     * @param ctx  The context relative to which the <code>name</code> parameter is specified, or null if
     *             <code>name</code> is relative to the default initial context.
     * @param env  The possibly null environment that is used in creating the <code>DataSource</code>.
     *
     * @return The <code>DataSource</code>; null if it cannot be created.
     *
     * @throws Exception if the factory encountered an exception while attempting to create the <code>DataSource</code>,
     *                   and no other object factories are to be tried.
     */
    public Object getObjectInstance(Object obj, Name name, Context ctx, Hashtable env)
            throws Exception {

        Reference ref = (Reference) obj;
        SimplePoolDataSource dataSource = new SimplePoolDataSource();

        /*
             driver:             JDBC driver. e.g. 'oracle.jdbc.driver.OracleDriver'<br>
             jdbcUrl:            JDBC connect string. e.g. 'jdbc:oracle:thin:@203.92.21.109:1526:orcl'<br>
             user:               Database login name. e.g. 'Scott'<br>
             password:           Database password. e.g. 'Tiger'<br>
             minConns:           Minimum number of connections to start with.<br>
             maxConns:           Maximum number of connections in dynamic pool.<br>
             maxConnTime:        Time in days between connection resets. (Reset does a basic cleanup)<br>
             maxCheckoutSeconds: Max time a connection can be checked out before being recycled. Zero value turns option
                                 off, default is 60 seconds.
        */

        String driver = (String) ref.get("driver").getContent();

        if (driver != null) {
            dataSource.setDriver(driver);
        }

        String user = (String) ref.get("user").getContent();

        if (user != null) {
            dataSource.setUser(user);
        }

        String password = (String) ref.get("password").getContent();

        if (password != null) {
            dataSource.setPassword(password);
        }

        String jdbcUrl = (String) ref.get("jdbcUrl").getContent();

        if (jdbcUrl != null) {
            dataSource.setJdbcUrl(jdbcUrl);
        }

        String minConns = (String) ref.get("minConns").getContent();

        if (minConns != null) {
            dataSource.setMinConns(minConns);
        }

        String maxConns = (String) ref.get("maxConns").getContent();

        if (maxConns != null) {
            dataSource.setMaxConns(maxConns);
        }

        String maxConnTime = (String) ref.get("maxConnTime").getContent();

        if (maxConnTime != null) {
            dataSource.setMaxConnTime(maxConnTime);
        }

        String maxCheckoutSeconds = (String) ref.get("maxCheckoutSeconds").getContent();

        if (maxCheckoutSeconds != null) {
            dataSource.setMaxCheckoutSeconds(maxCheckoutSeconds);
        }

        dataSource.init();

        return dataSource;
    }
}