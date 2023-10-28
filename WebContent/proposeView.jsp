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
      <sql:setDataSource var = "snapshot" driver = "com.mysql.cj.jdbc.Driver"
         url = "jdbc:mysql://127.0.0.1:3306/test"
         user = "root"  password = "root"/>
 
      <sql:query dataSource = "${snapshot}" var = "result">
         SELECT proposals.idProposal, client.name, proposals.propose_name  from proposals inner join client on proposals.idClient=client.indexClient;
      </sql:query>


      
      <table border = "1" width = "100%">
         <tr>
            <th>Nome creatore</th>
            <th>Proposta</th>
         </tr>
         
         <c:forEach var = "row" items = "${result.rows}">
            <tr>
               <td>
               		<c:out value = "${row.name}"/>
               </td>
               
               <td>
               		<a href = "DownloadServlet?id=${row.idProposal}" target="_blank"><c:out value = "${row.propose_name}"/></a>
               </td>
            </tr>
         </c:forEach>
      </table>
 			<td><a href="uploadFile.jsp">Carica nuova proposta</a></td>

   </body>
</html>