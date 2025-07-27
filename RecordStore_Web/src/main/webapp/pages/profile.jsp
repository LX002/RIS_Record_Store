<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>
	<sec:authentication property="principal.k.ime" />'s profile
</title>
</head>
<link href="<c:url value="/css/profile-style.css" />" type="text/css" rel="stylesheet">
<body>
	<c:if test="${not success}">
		<a href="${pageContext.request.contextPath}/users/store" id="back">Back</a>
	</c:if>
	<sec:authorize access="hasAnyRole('KUPAC', 'PRODAVAC')">
		<c:if test="${not success}">
			<div class="profile-form">
				<h3>${user.ime} ${user.prezime}</h3>
				<form action="${pageContext.request.contextPath}/users/updateProfile/${idKorisnik}" method="post">
					<table>
						<tr>
							<td>Role: </td>
							<td>${user.role.naziv}</td>
						</tr>
						<tr>
							<td>Username: </td>
							<td><input value="${user.username}" type="text" name="username" required /></td>
						<tr>
						<tr>
							<td>Address: </td>
							<td><input value="${user.adresa}" type="text" name="address" required /></td>
						</tr>
						<tr>
							<td>Old password: </td>
							<td><input type="password" name="oldPassword"/></td>
						</tr>	
						<tr>
							<td>New password: </td>
							<td><input type="password" name="newPassword"/></td>
						</tr>
					</table>
					<input type="submit" value="Save changes" />
				</form>
			</div>
		</c:if>
		<c:if test="${not success and not empty message}">
			<div class="error">
				${message}
			</div>
		</c:if>
		<c:if test="${success}">
			<div class="success">
				Your profile was updated successfully!<br>
				<a href="${pageContext.request.contextPath}/logout" id="logout">Log out</a> for the changes to take effect
			</div>
		</c:if>
	</sec:authorize>
</body>
</html>