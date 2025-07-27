<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Register</title>
		<link href="<c:url value="/css/register-style.css" />" type="text/css" rel="stylesheet">
	</head>
<body>
	<c:if test="${not registered}">
		<a href="/RecordStore/pages/login.jsp" id="back">Back</a>
		<div class="register-form">
	    	<h1>Create new account</h1>
	    	<form action="${pageContext.request.contextPath}/register"
							method="post">
	    		<table>
	    			<tr>
	    				<td>Name: </td>
	    				<td><input type="text" name="ime"/></td>
	    			</tr>
	    			<tr>
	    				<td>Surname: </td>
	    				<td><input type="text" name="prezime"/></td>
	    			</tr>
	    			<tr>
	    				<td>Username: </td>
	    				<td><input type="text" name="username"/></td>
	    			</tr>
	    			<tr>
	    				<td>Password: </td>
	    				<td><input type="password" name="password"/></td>
	    			</tr>
	    			<tr>
	    				<td>Repeat password: </td>
	    				<td><input type="password" name="repeatedPassword"/></td>
	    			</tr>
	    			<tr>
	    				<td>Address: </td>
	    				<td><input type="text" name="adresa"/></td>
	    			</tr>
	    			<tr>
	    				<td>Role: </td>
	    				<td>
	    					<select name="role">
	    						<option value="1">KUPAC</option>
	    						<option value="2">PRODAVAC</option>
	    					</select>
	    				</td>
	    			</tr>
	    		</table>
	    		<br>
	    		<button id="register-button">Register</button>
    		</form>
    	</div>
	</c:if>
	
    <c:if test="${validData}">
    	<div class="success">
    		${message} <br>
    		<a href="${pageContext.request.contextPath}/pages/login.jsp">Go back to login page</a>
    	</div>
    </c:if>
    
    <c:if test="${not validData}">
    	<div class="error">
    		${message}
    	</div>
    </c:if>
</body>
</html>