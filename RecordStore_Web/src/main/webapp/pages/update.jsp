<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Update album: ${album.naziv} by ${album.izvodjac}</title>
<link href="<c:url value="/css/update-style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
	<sec:authorize access="hasRole('PRODAVAC')">
			<a href="${pageContext.request.contextPath}/users/store" id="back">Back</a> <br>
			<div class="update-form">
				<img src="data:image/jpeg;base64,${albumArt}" alt="albumArt" /> <br>
				<form action="${pageContext.request.contextPath}/admin/saveChanges/${album.idAlbum}" method="post">
					<table>
						<tr>
							<td>Name: </td>
							<td><input type="text" name="naziv" value="${album.naziv}" required/></td>
						</tr>
						<tr>
							<td>Artist: </td>
							<td><input type="text" name="izvodjac" required value="${album.izvodjac}"/></td>
						</tr>
						<tr>
							<td>Year of release: </td>
							<td><input type="number" name="godIzdanja" required value="${album.godIzdanja}"/></td>
						</tr>
						<tr>
							<td>Genre: </td>
							<td><input type="text" name="zanr" required value="${album.zanr}"/></td>
						</tr>
						<tr>
							<td>Units in stock: </td>
							<td><input type="number" name="brKomNaStanju" required value="${album.brKomNaStanju}"/></td>
						</tr>
						<tr>
							<td>Price: </td>
							<td><input type="number" name="cena" required value="${album.cena}"/></td>
						</tr>
					</table>
					<br>
					<input value="Update details" type="submit"/>
				</form>
			</div>
		<c:if test="${not success}">
			<div class="error">
            	${message}
            </div>
		</c:if>
		<c:if test="${success}">
			<div class="success">
				<p>Update of album details was successful!</p>
				<a href="${pageContext.request.contextPath}/users/store">Go back to store</a>
			</div>
		</c:if>
	</sec:authorize>
	<sec:authorize access="!hasRole('PRODAVAC')">
        <jsp:include page="/pages/deniedAccess.jsp" />
    </sec:authorize>
</body>
</html>