/**
 * $Source$ $Revision$
 * $Date$ Copyright (c) 2004, Russell Beattie (http://www.russellbeattie.com/) All rights
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * Creates a pool of database connection based on the specific intialization parameters.
 *
 * @author <a href="http://www.russellbeattie.com/">Russell Beattie</a>
 * @author <a href="http://www.thauvin.net/erik/">Erik C. Thauvin</a>
 * @version $Revision$, $Date$
 * @since 1.0
 */
public class SimplePoolServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(SimplePoolServlet.class);
    private static SimplePoolDataSource dataSource;

    /**
     * Closes the connection pool.
     *
     * @see javax.servlet.http.HttpServlet#destroy
     */
    public void destroy() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    /**
     * Initializes the connection pool.
     *
     * @see javax.servlet.http.HttpServlet#init(ServletConfig)
     */
    public void init(ServletConfig servletConfig)
            throws ServletException {
        log.info("Initializing " + servletConfig.getServletName() + " Servlet");

        String varName = servletConfig.getInitParameter("varName");
        String driver = servletConfig.getInitParameter("driver");
        String jdbcUrl = servletConfig.getInitParameter("jdbcUrl");
        String user = servletConfig.getInitParameter("user");
        String password = servletConfig.getInitParameter("password");
        String minConns = servletConfig.getInitParameter("minConns");
        String maxConns = servletConfig.getInitParameter("maxConns");
        String maxConnTime = servletConfig.getInitParameter("maxConnTime");
        String maxCheckoutSeconds = servletConfig.getInitParameter("maxCheckoutSeconds");

        if (isValid(varName) && isValid(driver) && isValid(jdbcUrl) && (user != null) && (password != null) &&
                isValid(minConns) && isValid(maxConns) && isValid(maxConnTime) && isValid(maxCheckoutSeconds)) {
            dataSource = new SimplePoolDataSource();

            dataSource.setDriver(driver);
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setMinConns(minConns);
            dataSource.setMaxConns(maxConns);
            dataSource.setMaxConnTime(maxConnTime);
            dataSource.setMaxCheckoutSeconds(maxCheckoutSeconds);

            try {
                dataSource.init();

                servletConfig.getServletContext().setAttribute(varName, dataSource);
            } catch (Exception e) {
                log.error("An error occurred while starting the connection pool.", e);
                throw new ServletException("Error starting the connection pool.");
            }
        } else {
            log.error("One or more intialization parameter is invalid or missing.");
            throw new ServletException("Invalid or missing initialization parameter.");
        }
    }

    private boolean isValid(String s) {
        if ((s != null) && (s.length() > 0)) {
            return true;
        }

        return false;
    }
}
