<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html>
<%
if(session != null && session.getAttribute("id") != null){
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html" charset="ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body>
	
		<h3>File Upload</h3>
		Carica Proposta:<br>
	
		<form action="ContentExtractionServlet" method="post" enctype="multipart/form-data">
			<input type="file" name="file"/>
			<br>
			<input type="submit" value="Carica File"/>
		</form>
		 	<a href="proposeView.jsp">Visualizze proposte caricate</a>
		 	 <%}else{
 				response.sendRedirect("login.jsp");
 			} %>
	</body>
	
</html>