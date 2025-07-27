<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link href="<c:url value="/css/deniedAccess-style.css" />" type="text/css" rel="stylesheet">
</head>
<body>
	<div id="container">
		<p>You can't access this page...</p>
		<img src="<c:url value="/images/error.gif" />" alt="error" />
		<p>...but you got rickrolled instead!</p>
	</div>
</body>
</html>