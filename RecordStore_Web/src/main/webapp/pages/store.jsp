<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Insert title here</title>
    <link href="<c:url value="/css/store-style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
    
    <div>
    	<sec:authorize access="hasAnyRole('KUPAC', 'PRODAVAC')">
    	<!-- <sec:authentication property="principal.k.ime" /> -->
    		<div class="header-menu">
	    		<a href="${pageContext.request.contextPath}/logout" id="logout">Log out</a>
	            <div class="sort-form">
	            	<form method="get" action="${pageContext.request.contextPath}/users/sortAlbums">
	            		Sort albums by: <select name="sortType">
	            			<option value="">default</option>
	            			<option value="name">name</option>
	            			<option value="artist">artist</option>
	            			<option value="genre">genre</option>
	            			<option value="priceAsc">price (ascending)</option>
	            			<option value="priceDesc">price (descending)</option>
	            		</select>
	            		<input class="input-button" type="submit" value="Apply" />
	            	</form>
	            </div>
	            <div class="search-form">
	            	<form method="get" action="${pageContext.request.contextPath}/users/search">
	            		<table>
	            			<tr>
	            				<td>Name: <input class="input-field" type="text" name="nameSearch" /></td>
	            				<td>Artist: <input class="input-field" type="text" name="artistSearch" /></td>
	            				<td>Year of release: <input class="input-field" type="number" name="yearSearch" /></td>
	            				<td>Genre: <input class="input-field" type="text" name="genreSearch" /></td>
	            				<td><input class="input-button" type="submit" value="Search" /></td>
	            			</tr>
	            		</table>
	            		
	            	</form>
	            </div>
	            <a href="${pageContext.request.contextPath}/users/profile/${idKorisnik}" id="profile-link">
	            	Profile
	            </a>
            </div>
            <div class="header-bottom-layer"></div>
    	</sec:authorize>
        <sec:authorize access="hasRole('KUPAC')">
            <c:if test="${not empty cart}">
            	<div class="cart">
	            	<h3>Your cart</h3>
	            	<table>
		            	<c:forEach var="albumInCart" items="${cart}" varStatus="status">
			    			<tr>	
			    				<fmt:formatNumber value="${albumInCart.cena}" pattern="#,##0.00" var="fmtAlbumPrice" />
			    				<td>${albumInCart.naziv} by ${albumInCart.izvodjac}<td>
			    				<td>${fmtAlbumPrice} RSD</td> 
				    			<td>	
				    				<a href="${pageContext.request.contextPath}/users/removeFromCart/${status.index}">
				    					<button>Remove</button>
				    				</a>
				    			</td>	
			    			</tr>	
						</c:forEach>
					</table>
					<br>
	            	<fmt:formatNumber value="${price}" pattern="#,##0.00" var="fmtPrice" />
	            	Order price: ${fmtPrice} <br><br>
					<a href="${pageContext.request.contextPath}/users/order">
					    <button>Order</button>
					</a>
				</div>
            	<br>
            </c:if>
            
            <!-- <p><a href="${pageContext.request.contextPath}/users/test2">Kupac test</a></p> -->
            
            <c:forEach var="album" items="${albums}" varStatus="loopStatus">
            	<div class="album-details">
            		<fmt:formatNumber value="${album.cena}" pattern="#,##0.00" var="formatedVal" />
            		<img src="data:image/jpeg;base64,${albumArts[loopStatus.index]}" alt="albumArt" /> <br>
            		<p>${album.naziv} by ${album.izvodjac}</p>
            		<p>${formatedVal} RSD</p>
            		<a href="${pageContext.request.contextPath}/users/addToCart/${album.idAlbum}">
            			<button class="add-button">Add to cart</button>
            		</a>
            	</div>
            </c:forEach>
        </sec:authorize>

        <sec:authorize access="hasRole('PRODAVAC')">
            <!-- <a href="${pageContext.request.contextPath}/admin/test">Prodavac test</a> <br> -->
            <div id="admin-second-header">
	            <a href="${pageContext.request.contextPath}/admin/insertAlbum">Add new album</a>
	            <a href="${pageContext.request.contextPath}/admin/viewOrders">Orders</a>
            </div>
            <div id="sh-bottom-layer"></div>
            <c:if test="${succDel and not empty succDel}">
            	<p id="del">${deleteMessage}</p>
            </c:if>
            <c:forEach var="album" items="${albums}" varStatus="loopStatus">
            	<div class="album-details">
            		<fmt:formatNumber value="${album.cena}" pattern="#,##0.00" var="formatedVal" />
            		<img src="data:image/jpeg;base64,${albumArts[loopStatus.index]}" alt="albumArt" /> <br>
            		${album.naziv} by ${album.izvodjac} <br>
            		${formatedVal} RSD <br>
            		<a href="${pageContext.request.contextPath}/admin/updateAlbum/${album.idAlbum}">
            			<button>Update album</button>
            		</a>
            		<br>
            		<a href="${pageContext.request.contextPath}/admin/deleteAlbum/${album.idAlbum}">
            			<button>Delete album</button>
            		</a>
            	</div>
            </c:forEach>
        </sec:authorize>

        <!-- Access Denied for all other roles -->
        <sec:authorize access="!hasAnyRole('KUPAC', 'PRODAVAC')">
            <jsp:include page="/pages/deniedAccess.jsp" />
        </sec:authorize>
    </div>
</body>
</html>
