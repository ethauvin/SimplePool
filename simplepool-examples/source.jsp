<%@page import="java.io.*"%>
<html>
<body>
<pre>
<%
	String file = request.getParameter("file"); 
	if ((file != null) && (file.trim().length() > 0) && (file.indexOf( ".." ) == -1)) { 
		InputStream is = pageContext.getServletContext().getResourceAsStream(file); 
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			for (int ch = isr.read(); ch != -1; ch = isr.read()) {
				if (ch == '<') {
					out.print("&lt;");
				} else if (ch == '\t') {
					out.print("  ");
				} else {
					out.print((char) ch);
				}
			}
		}       
	}
%>
</pre>
</body> 
</html> 
