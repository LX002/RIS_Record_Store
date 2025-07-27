<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Save order</title>
<link href="<c:url value="/css/order-style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
	<sec:authorize access="hasRole('KUPAC')">
			<a href="${pageContext.request.contextPath}/users/store" id="back">Go back</a>
            
            <c:if test="${not empty cart}">
	            <div class="cart">
	            	<h3> Your cart: </h3>
	            	<table>
		            	<c:forEach var="albumInCart" items="${cart}">
			    			<tr>	
			    				<fmt:formatNumber value="${albumInCart.key.cena}" pattern="#,##0.00" var="fmtAlbumPrice" />
			    				<td>${albumInCart.key.naziv} by ${albumInCart.key.izvodjac}</td>
			    				<td>${fmtAlbumPrice}</td> 
			    				<td>( ${albumInCart.value}x )</td>
							</tr>				
						</c:forEach>
		            	<fmt:formatNumber value="${price}" pattern="#,##0.00" var="fmtPrice" />
	            	</table>
	            	Order price: ${fmtPrice} <br>
	            </div>
            	<br>
            	<div class="order-form">
            		<h3> Ordering albums for: <sec:authentication property="principal.k.ime" /></h3>
            		<p>Payment data:</p>
	            	<form method="post" action="${pageContext.request.contextPath}/users/saveOrder">
	            		<table>
	            			<tr>
	            				<td>Card number: </td>
	            				<td><input type="text" name="brKartice"/></td>
	            			</tr>
	            			<tr>
	            				<td>Name on the card: </td>
	            				<td><input type="text" name="imeNaKartici"/></td>
	            			</tr>
	            			<tr>
	            				<td>Card expiration date: </td>
	            				<td><input type="text" name="rokVazenja"/></td>
	            			</tr>
	            			<tr>
	            				<td>CVV: </td>
	            				<td><input type="text" name="cvv"/></td>
	            			</tr>
	            		</table>
	            		<br>
	            		<input type="submit" value="Save order"/>
	            	</form>
	            </div>
	            <br>
	            <c:if test="${not empty message}">
	            	<div class="error">
            			${message}
            		</div>
            	</c:if>
            </c:if>
            <c:if test="${not empty succMessage and empty cart}">
	            <div class="success">
	            	<p>${succMessage}</p>
	            	<a href="${pageContext.request.contextPath}/users/getOrderBill/${idNar}">Get order bill</a> <br>
	            	<a href="${pageContext.request.contextPath}/users/store">Return to the store</a>
	        	</div>
	        </c:if>
            <c:if test="${empty cart and empty succMessage}">
            	<div class="error">
            		Cart is empty... nothing to order!
            	</div>
            </c:if>
        </sec:authorize>
</body>
</html>