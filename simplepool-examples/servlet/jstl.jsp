<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<html>
	<head>
		<title>SimplePool Servlet/JSTL Example</title>
	</head> 
	<body>				
		
		<%-- Execute the query using our DataSource --%>
		<sql:query var="rs" dataSource="${ServletPoolDS}">
			SHOW TABLES 
		</sql:query>
		
		<h3>Tables</h3> 
		<ul>

		<%--
			Loop through the result set and
			display the current column value
		--%>			
		<c:forEach var="row" items="${rs.rowsByIndex}"> 
			<li><c:out value="${row[0]}"/></li> 
		</c:forEach>

		<ul>
	</body> 
</html>