<%@page import="javax.naming.*, javax.sql.*, java.sql.*;" %>
<html>
	<head>
		<title>SimplePool Servlet Example</title>
	</head> 
	<body>

		<%			
			// Get a reference to our DataSource
			DataSource ds = (DataSource) pageContext.findAttribute("ServletPoolDS");
			
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
			// and display the current column value
			rst.close();
			stmt.close();
			conn.close();
		%>
	</body> 
</html> 

