<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	request.setCharacterEncoding("utf-8");
	
	String contextPath = request.getContextPath();

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div align="center">
	<%-- <예약확인> 이미지 --%>
	<img src="<%=contextPath%>/img/hwakin.jpg">
	
	<form action="<%=contextPath%>/Car/CarReserveConfirm.do" method="post">
		<table width="400">
			<tr height="60" align="center">
				<td width="200" align="center">휴대폰 번호 입력</td>
				<td width="200" align="center">
					<input type="text" name="memberphone">
				</td>
			</tr>
			<tr height="60" align="center">
				<td width="200" align="center">비밀번호 입력</td>
				<td width="200" align="center">
					<input type="password" name="memberpass">
				</td>
			</tr>
			<tr height="60" align="center">
				<td colspan="2" align="center">
					<input type="submit" value="검색">
				</td>
			</tr>
		</table>
	</form>
</div>	
</body>
</html>