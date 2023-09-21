<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<% //
	request.setCharacterEncoding("utf-8");
	String contextPath = request.getContextPath();
%>

	<%-- 빨간 선 --%>
	<hr width="100%" color="red">
	
	<%-- 아래 하단 쪽 로고 이미지 --%>
	<a href="#">
		<img src="<%=contextPath%>/img/bo.jpg" width="500" height="50">
	</a>
	
	<font size="2">
		<b>
			<%--회사 소개 이미지 --%>
			<img src="<%=contextPath%>/img/sodog.jpg">
			<%--개인정보 취급 방침 --%>
			<img src="<%=contextPath%>/img/info.jpg"> | 사이버 신문고 | 이용약관 | 인재채용
		</b>
	
	<br><br>
	<small>
		(주) SM 렌탈 사업자 등록번호 214-98754-9874 
		통신 판매업 신고번호 : 제 2010-충남-05호 <br>
		
		서울시 강남구 역삼동 역삼빌딩 2층 21호 <br><br>
		
		대표전화 : 02-3546-6547<br>
		
		FAX : 01-3546-9874
	</small>
	
	</font>
	
</body>
</html>