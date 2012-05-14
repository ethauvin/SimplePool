<%@page import="javax.naming.*, javax.sql.*, java.sql.*;" %>
<html>
	<head>
		<title>SimplePool JNDI Example</title>
	</head> 
	<body>

		<%
			// Get a new initial JNDI context
			Context ctx = new InitialContext();
			
			// Get a reference to our JNDI DataSource 
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/JNDIPoolDS");
			
			// Get a connection from the pool
			Connection conn = ds.getConnection();
			
			// Create a new statement
			Statement stmt = conn.createStatement();
			
			// Execute the query
			ResultSet rst = stmt.executeQuery("SHOW TABLES");
		%>

		<h3>Tables</h3> 
		<ul>
		<%
			// Loop through the result set and
			// display the current column value
			while (rst.next()) {
		%>
			<li><%= rst.getString(1) %></li>
		<%  
			}
		%>
		<ul>
		
		<%
			// Close the result set, statement and connection
			rst.close();
			stmt.close();
			conn.close();
		%>
	</body> 
</html>
