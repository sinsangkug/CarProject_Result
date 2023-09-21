<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	request.setCharacterEncoding("utf-8");
// 	String contextPath = request.getContextPath();
%>

<c:set var = "contextPath" value = "${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<center>
		<img src="${contextPath}/img/naeyeok.jpg">
		<br><br>
		
		<table width="1000" border="1" align="center">
			<tr align="center">
				<td align="center" width="150">차량이미지</td>
				<td align="center" width="100">차량명</td>
				<td align="center" width="100">대여일</td>
				<td align="center" width="50">대여기간</td>
				<td align="center" width="100">한대 가격</td>
				<td align="center" width="70">보험</td>
				<td align="center" width="70">와이파이</td>
				<td align="center" width="70">네비게이션</td>
				<td align="center" width="70">베이비시트</td>
				<td align="center" width="100">예약수정</td>
				<td align="center" width="100">예약취소</td>
			</tr>
<c:if test="${empty requestScope.v}">
		<tr align="center">
			<td colspan="11">예약한 정보가 없습니다.</td>
		</tr>	
</c:if>		
	
	<c:forEach var="vo" items="${requestScope.v}">
		<tr align="center">
				<td align="center" width="150">
					<img src="${contextPath}/img/${vo.carimg}" width="140" height="90">
				</td>
				<td align="center" width="100">${vo.carname}</td>
				<td align="center" width="100">${vo.carbegindate}</td>
				<td align="center" width="50">${vo.carreserveday}일</td>
				<td align="center" width="100">${vo.carprice}원</td>
				<td align="center" width="70">
					<c:if test="${vo.carins == 1}">적용</c:if>
					<c:if test="${vo.carins == 0}">미적용</c:if>
				</td>
				<td align="center" width="70">
					<c:if test="${vo.carwifi == 1}">적용</c:if>
					<c:if test="${vo.carwifi == 0}">미적용</c:if>
				</td>
				<td align="center" width="70">
					<c:if test="${vo.carnave == 1}">적용</c:if>
					<c:if test="${vo.carnave == 0}">미적용</c:if>
				</td>
				<td align="center" width="70">
					<c:if test="${vo.carbabyseat == 1}">적용</c:if>
					<c:if test="${vo.carbabyseat == 0}">미적용</c:if>
				</td>
				<td align="center" width="100">
					<a href="${contextPath}/Car/update.do?orderid=${vo.orderid}&carimg=${vo.carimg}&memberpass=${requestScope.memberpass}&memberphone=${requestScope.memberphone}">예약수정</a>
				</td>
				<td align="center" width="100">
					<a href="${contextPath}/Car/delete.do?orderid=${vo.orderid}&center=Delete.jsp&memberphone=${requestScope.memberphone}">예약취소</a>
				</td>
			</tr>
	</c:forEach>
	
		</table>
	</center>
</body>
</html>