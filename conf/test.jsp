<% out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); %>
<%@ page contentType="text/xml;charset=UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<sql:setDataSource dataSource="jdbc/PooledDS" />

<sql:query var="page" maxRows="30">
	select * from entry
</sql:query>

<document>
	<c:forEach var="row" items="${page.rows}" varStatus="counter">
		<entry>
			<count><c:out value="${counter.count}"/></count>
			<id><c:out value="${row.id}"/></id>
			<title><c:out value="${row.name}"/></title>
		</entry>
	</c:forEach>
</document>
