<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>	
<%
	request.setCharacterEncoding("utf-8");
	String contextPath = request.getContextPath();
%>
	<center>
		<%--자주 렌트 예약되는 차량 이미지를 화면에 보여주자 --%>
		<table width="1000">
			<tr>
				<td align="center" width="333">
					<img src="<%=contextPath%>/img/lfsonata.jpg" width="288">
				</td>
				<td align="center" width="333">
					<img src="<%=contextPath%>/img/k5.jpg" width="288">
				</td>
				<td align="center" width="333">
					<img src="<%=contextPath%>/img/avante.jpg" width="288">
				</td>
			</tr>
		</table>
		
		<%-- <차량 종류보기> 이미지 --%>
		<h1> <img src="<%=contextPath%>/img/ccs.jpg" height="50"></h1>
		
		<%-- 소형, 중형, 대형  중 기준을 선택해서 차량 정보 조회 --%>
		<form action="<%=contextPath%>/Car/carcategory.do" method="post">
			<table width="400" align="center">
				<tr>
					<td width="100">차량 유형</td>
					<td width="100" height="50">
						<select name="carcategory">
							<option value="Small">소형</option>
							<option value="Mid">중형</option>
							<option value="Big">대형</option>
						</select>
					</td>
					<td align="center">
						<input type="submit" value="검색">
					</td>
					<td align="center">
						<input type="button" value="전체검색" onclick="location.href='<%=contextPath%>/Car/CarList.do'">
					</td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>