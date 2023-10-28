<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>

<html>
   <head>
      <title>Visualizza proposte</title>
   </head>

   <body>
   Visualizzazione proposte:
      
      <table border = "1" width = "100%">
         <tr>
            <th>Nome creatore</th>
            <th>Proposta</th>
         </tr>
         <% ResultSet rs = (ResultSet)request.getAttribute("rs");
         	ResultSetMetaData rsmd = (ResultSetMetaData)request.getAttribute("rsmd");%>
         <c:forEach var = "row" items = "${rs.rows}">
            <tr>
               <td><c:out value = "${row.name}"/></td>
               <td><c:out value = "${row.propose_name}"/></td>
            </tr>
         </c:forEach>         
      </table>
 			<td><a href="uploadFile.jsp">Carica nuova proposta</td>
 
   </body>
</html>