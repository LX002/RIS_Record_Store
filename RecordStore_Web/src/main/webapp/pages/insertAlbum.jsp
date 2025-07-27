<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert album</title>
<link href="<c:url value="/css/insertAlbum-style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
	<a href="${pageContext.request.contextPath}/users/store" id="back">Go back</a>
	<sec:authorize access="hasRole('PRODAVAC')">
		<div class="insertion-form">
			<h2>New album</h2>
			<form enctype="multipart/form-data" method="post" action="${pageContext.request.contextPath}/admin/saveAlbum">
				<table>
					<tr>
						<td>Select album art: </td>
						<td><input class="input-field" type="file" name="albumArt" accept="image/jpeg" required /></td>
					</tr>
					<tr>
						<td>Name:</td>
						<td><input class="input-field" type="text" name="naziv" required /></td>
					</tr>
					<tr>
						<td>Artist:</td>
						<td><input class="input-field" type="text" name="izvodjac" required/></td>
					</tr>
					<tr>
						<td>Year of release:</td>
						<td><input class="input-field" type="number" name="godIzdanja" required /></td>
					</tr>
					<tr>
						<td>Genre:</td>
						<td><input class="input-field" type="text" name="zanr" required /></td>
					</tr>
					<tr>
						<td>Units in stock:</td>
						<td><input class="input-field" type="number" name="brKomNaStanju" required /></td>
					</tr>
					<tr>
						<td>Price:</td>
						<td><input class="input-field" type="number" name="cena" required /></td>
					</tr>
				</table>
				<input type="submit" value="Insert album" />
			</form>
		</div>
		<c:if test="${not success}">
			<div class="error">
				${message}
			</div>
		</c:if>
		<c:if test="${success}">
			<div class="success">
				Album is saved succesfully!
			</div>
		</c:if>
	</sec:authorize>
</body>
</html>