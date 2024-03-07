<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Pagina d'accesso</title>
</head>
<body>
<% 
/*Cookie[] cookies = request.getCookies();
if (cookies != null) {
   for (Cookie cookie : cookies) {
      if (cookie.getName().equals("remember")) {
     	out.print(cookie.getName());
     	session.invalidate();
     	response.sendRedirect("/LoginServlet");
        break;
      }
   }
}
*/
if(session.getAttribute("id") == null){
%>
	<form method="post" action="LoginServlet">
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
				<td>Remember me <input type="checkbox" name="remember" value="bocchin"></td>
			</tr>
			
			<tr>
				<td></td>
				<td><input type="submit" name="login" value="login"></td>	
			</tr>	
		</table>
	</form>
	<a href="registration.jsp"> Registrati</a>
	
		<%}else{
 				response.sendRedirect("proposeView.jsp");
 			} %>


</body>
</html>