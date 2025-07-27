<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Record store - login</title>
    <link href="<c:url value="/css/index-style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
    <div class="login-form">
    	<h1>Welcome to record store!</h1>
    	<form action="${pageContext.request.contextPath}/login"
						method="post">
    		<table>
    			<tr>
    				<td>Username: </td>
    				<td><input type="text" name="username"/></td>
    			</tr>
    			<tr>
    				<td>Password: </td>
    				<td><input type="password" name="password"/></td>
    			</tr>
    		</table>
    		<br>
    		
    		<button id="login-button">Login</button>
    	</form>
    	<br>
    	<a href="${pageContext.request.contextPath}/getRegisterPage">Register</a> if you don't have an account <br>
    </div>
    <c:if test="${not empty paramValues['error']}">
    	<div class="error">
    		Login error! <br>
    		Check username / password
    	</div>
    </c:if>
</body>
</html>