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
<title>simplepool: JNDI DataSource Examples</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<script language="javascript" type="text/javascript">
	<%
		boolean enabled = false;
			
		try {
			enabled = (new InitialContext().lookup("java:comp/env/jdbc/JNDIPoolDS") != null);
		} catch (Exception ignore) { }
	%>
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
            JNDI DataSource Examples</h2>
        </div>
        <p class="tasknav"><a href="../index.html">Examples Home</a></p>
        <p><strong>Please read the instructions before trying to run the examples.</strong></p>
        <div id="examples" class="app">
          <div class="axial">
            <table border="0" cellspacing="2" cellpadding="3">
              <tr>
                <th>Summary</th>
                <td>A simple database connection pool example using JDNI.</td>
              </tr>
              <tr>
                <th>Example 1</th>
                <td><a href="<% if (enabled) { %>scriptlet.jsp<% } else { %>javascript:RTFM()<% } %>">Scriptlet Example</a> [<a href="../source.jsp?file=jndi/scriptlet.jsp">source</a>]</td>
              </tr>
              <tr>
                <th>Example 2</th>
                <td><a href="<% if (enabled) { %>jstl.jsp<% } else { %>javascript:RTFM()<% } %>">JSTL Example</a> [<a href="../source.jsp?file=jndi/jstl.jsp">source</a>]</td>
              </tr>
            </table>
          </div>
          <div class="h3" id="instructions">
            <h3>Instructions</h3>
            <p>JNDI is used to create, access and manage the connection pool.</p>
          <p class="alert">First, make sure that your JDBC driver is available, by placing its components in this webapp's WEB-INF/lib/ directory.</p>
          <p class="alert">The JNDI DataSource is defined as naming resource in the current webapp context, as shown in this minimal <a href="../source.jsp?file=conf/server.xml">server configuration file</a>. </p>
          <p>The JNDI DataSource is configured using the following parameters:</p>
            <ul>
              <li>The <span class="info">varName</span> parameter is used to specify the name of the variable which will hold a reference to the DataSource object created by the Servlet.</li>
              <li>The <span class="info">driver</span> parameter is used to specify the name of the JDBC driver class to be registered. For <code>example: com.mysql.jdbc.Driver</code>.</li>
              <li>The <span class="info">user</span> parameter is used to specify the database username, if any.</li>
              <li>The <span class="info">password</span> parameter is used to specify the database password, if any.</li>
              <li>The <span class="info">jdbcUrl</span> parameter is used to specify the JDBC URL associated with the database. For example: <code>jdbc:mysql://localhost:3306/dbname</code>.</li>
              <li>The <span class="info">minConns</span> parameter is used to specify the minimum number of connections to start the pool with.</li>
              <li>The <span class="info">maxConns</span> parameter is used to specify the maximum number of connections to be dynamically created in the pool.</li>
              <li>The <span class="info">maxConnTime</span> parameter is used to specify the time (in days) between connection resets. The pool manager will perform a basic cleanup at the specified interval.</li>
              <li>The <span class="info">maxCheckoutSeconds</span> parameter is used to specify the maximum time a connection can be checked out before being recycled. A zero value turns this option off.</li>
            </ul>
            <p>Please note that <em>all parameters are required</em>.</p>
            <p class="alert">Please take a minute to incorporate the <a href="../source.jsp?file=conf/server.xml">JDNI DataSource example</a> in your <code>$CATALINE_HOME/conf/server.xml</code>. Once done, please restart Tomcat. The examples will be functional upon restart.</p>
            <p>Please look at the [source] of the provided examples to understand how to access the connection pool from a scriptlet or using the JSTL SQL tags. The examples demonstrates how to list all of the tables contained in the specified database.</p>
          </div>
        </div>
      </div>
	</td>
  </tr>
</table>
</body>
</html>
