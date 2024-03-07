<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Pagina di registrazione</title>
</head>
<body>
<% 
if(session.getAttribute("id") == null){
%>
	<form method="post" action="RegistrationServlet" enctype="multipart/form-data">
		<table>
			<tr>
				<td>User name</td>
				<td><input type="text" name="username"></td>
			</tr>
			
			<tr>
				<td>Password</td>
				<td><input type="password" name="pass"></td>
			</tr>
			
			<tr>
				<td>Conferma password</td>
				<td><input type="password" name="pass_c"></td>
			</tr>
			
			<tr>
				<td>Carica immagine profilo</td>
				<td><input type="file" name="profile_img"></td>
			</tr>
			
			<tr>
				<td><input type="submit" name="register" value="registrati"></td>
			</tr>	
			
		</table>
	</form>
	<a href="login.jsp">Torna alla fase di login</a>
	
		<%}else{
 				response.sendRedirect("proposeView.jsp");
 			} %>
</body>
</html>