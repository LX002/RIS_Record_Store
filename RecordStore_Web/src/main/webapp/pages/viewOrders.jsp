<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>View orders</title>
<link href="<c:url value="/css/viewOrders-style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
	<a href="${pageContext.request.contextPath}/users/store" class="navigation">Go back</a>
	<sec:authorize access="hasRole('PRODAVAC')">
		<a href="${pageContext.request.contextPath}/admin/getProcessedOrdersReport/${idKorisnik}" class="navigation">Today's orders report</a>
		<c:if test="${not empty orders}">
			<div class="unprocessed-orders">
				<h3>Unprocessed orders</h3>
				<table>
					<tr>
						<th>ID</th>
						<th>Customer</th>
						<th>Order price</th>
						<th></th>
					</tr>
					<c:forEach var="order" items="${orders}">
						<tr>
							<td>${order.idNarudzbina}</td>
							<td>${order.korisnik2.ime} ${order.korisnik2.prezime}</td>
							<td>${order.iznos}</td>
							<td><a href="${pageContext.request.contextPath}/admin/viewOrderDetails/${order.idNarudzbina}">Process order</a></td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</c:if>
		<br>
		<c:if test="${empty orders}">
			<div class="error">
				There are no orders for processing!
			</div>
		</c:if>
		<c:if test="${process}">
			<div class="order-details">
				<h4>Processing order with ID: ${o.idNarudzbina}</h4>
				<c:if test="${not empty orderItems}">
					<table>
						<tr>
							<th>Album art</th>
							<th>Album / artist</th>
							<th>Quantity</th>
							<th></th>
						</tr>
						<c:forEach var="item" items="${orderItems}" varStatus="loopStatus">
							<tr>
								<td><img width="50px" height="50px" src="data:image/jpeg;base64,${albumArts[loopStatus.index]}" alt="albumArt" /></td>
								<td>${item.album.naziv} by ${item.album.izvodjac}</td>
								<td>${item.kolicina}</td>
							</tr>
						</c:forEach>
					</table>
					<div class="process-form">
						<form method="post" action="${pageContext.request.contextPath}/admin/processOrder/${o.idNarudzbina}">
							<input type="submit" value="Process this order" />
						</form>
					</div>
				</c:if>
				<c:if test="${empty orderItems}">
					This order has no items!
				</c:if>
			</div>
		</c:if>
		<br>
		<c:if test="${not success and not empty message}">
			<div class="error">
				${message}
			</div>	
		</c:if>
		<c:if test="${success}">
			<div class="success">
				The order with ID: ${idN} is ready for delivery!
			</div>
		</c:if>
	</sec:authorize>
</body>
</html>