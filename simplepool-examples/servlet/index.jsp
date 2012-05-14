<!DOCTYPE html PUBLIC "-//Tigris//DTD XHTML 1.0 Transitional//EN"
  "http://style.tigris.org/tigris_transitional.dtd">
<%@page import="javax.naming.*" %>
<html>
<head>
<style type="text/css"> 
  /* <![CDATA[ */
  @import "../includes/tigris.css";
  @import "../includes/inst.css";
  /* ]]> */
</style>
<title>simplepool: Servlet DataSource Examples</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<script language="javascript" type="text/javascript">
	<% boolean enabled = (pageContext.findAttribute("ServletPoolDS") != null); %>
	function RTFM()
	{
		alert("Please read the instructions to enable this example.");
	}
</script>
</head>
<body marginwidth="0" marginheight="0" class="composite">
<table border="0" cellspacing="0" cellpadding="4" width="100%" id="main">
  <tr valign="top">
    <td>
	    <div id="bodycol">
        <div id="apphead">
          <h2><small>simplepool</small><br />
            Servlet DataSource Examples</h2>
        </div>
        <p class="tasknav"><a href="../index.html">Examples Home</a></p>
        <p><strong>Please read the instructions before trying to run the examples.</strong></p>
        <div id="examples" class="app">
          <div class="axial">
            <table border="0" cellspacing="2" cellpadding="3">
              <tr>
                <th>Summary</th>
                <td>A simple database connection pool example using the SimplePool Servlet.</td>
              </tr>
              <tr>
                <th>Example 1</th>
                <td><a href="<% if (enabled) { %>scriptlet.jsp<% } else { %>javascript:RTFM()<% } %>">Scriptlet Example</a> [<a href="../source.jsp?file=servlet/scriptlet.jsp">source</a>]</td>
              </tr>
              <tr>
                <th>Example 2</th>
                <td><a href="<% if (enabled) { %>jstl.jsp<% } else { %>javascript:RTFM()<% } %>">JSTL Example</a> [<a href="../source.jsp?file=servlet/jstl.jsp">source</a>]</td>
              </tr>
            </table>
          </div>
          <div class="h3" id="instructions">
            <h3>Instructions</h3>
            <p>The SimplePoolServlet is used to create, access and manage the connection pool.</p>
            <p class="alert">First, make sure that your JDBC driver is available, by placing its components in this webapp's WEB-INF/lib/ directory.</p>
            <p>The SimplePoolServlet is configured using a set of required initialization parameters, as show in this webapp's <code><a href="../source.jsp?file=WEB-INF/web.xml">WEB-INF/web.xml</a></code> under the <code>&lt;servlet/&gt;</code> section:</p>
            <ul>
              <li>The <code class="info">varName</code> initialization parameter is used to specify the name of the variable which will hold a reference to the DataSource object created by the Servlet.</li>
              <li>The <code class="info">driver</code> initialization parameter is used to specify the name of the JDBC driver class to be registered. For <code>example: com.mysql.jdbc.Driver</code>.</li>
              <li>The <span class="info">user</span> initialization parameter is used to specify the database username, if any.</li>
              <li>The <code class="info">password</code> initialization parameter is used to specify the database password, if any.</li>
              <li>The <code class="info">jdbcUrl</code> initialization parameter is used to specify the JDBC URL associated with the database. For example: <code>jdbc:mysql://localhost:3306/dbname</code>.</li>
              <li>The <code class="info">minConns</code> initialization parameter is used to specify the minimum number of connections to start the pool with.</li>
              <li>The <code class="info">maxConns</code> initialization parameter is used to specify the maximum number of connections to be dynamically created in the pool.</li>
              <li>The <code class="info">maxConnTime</code> initialization parameter is used to specify the time (in days) between connection resets. The pool manager will perform a basic cleanup at the specified interval.</li>
              <li>The <code class="info">maxCheckoutSeconds</code> initialization parameter is used to specify the maximum time a connection can be checked out before being recycled. A zero value turns this option off.</li>
            </ul>
            <p>Please note that <em>all parameters are required</em>.</p>
            <p class="alert">Please take a minute to uncomment the <code>&lt;servlet/&gt;</code> declaration and configure the Servlet's initialization parameters in this webapp's <code><a href="../source.jsp?file=WEB-INF/web.xml">WEB-INF/web.xml</a></code>. Once done, please restart the webapp and/or Tomcat. The examples will be functional upon restart.</p>
            <p>Please look at the [source] of the provided examples to understand how to access the connection pool from a scriptlet or using the JSTL SQL tags. The examples demonstrates how to list all of the tables contained in the specified database.</p>
          </div>
        </div>
      </div>
	</td>
  </tr>
</table>
</body>
</html>
