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
<title>예약할 차량 한대의 정보 조회후 정보를 보여주는 view</title>
</head>
<body>
	<center>
		<%-- <차량 정보 보기> 이미지 --%>
		<img src="<%=contextPath%>/img/cis.jpg" border="0">
		
		<%-- 조회된 차량 정보를 화면에서 보고 대여수량을 선택해 옵션을 추가로 선택하는 화면 요청 --%>
		<form action="<%=contextPath%>/Car/CarOption.do" method="post">
			
			<table width="1000">
				<tr align="center">
					<td rowspan="6" width="600">
						<img src="<%=contextPath%>/img/${vo.carimg}" width="500">
					</td>
					<td align="center" width="200">차량이름</td>
					<td align="center" width="200">${vo.carname}</td>
				</tr>
				<tr>
					<td align="center" width="200">대여 수량</td>
					<td align="center" width="200">
						<select name="carqty">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
						</select>
					</td>
				</tr>
				<tr>
					<td align="center" width="200">차량분류</td>
					<td align="center" width="200">${vo.carcategory}</td>
				</tr>
				<tr>
					<td align="center" width="200">대여금액</td>
					<td align="center" width="200">${vo.carprice}</td>
				</tr>
				<tr>
					<td align="center" width="200">제조회사</td>
					<td align="center" width="200">${vo.carcompany}</td>
				</tr>
				<tr>
					<td align="center" width="200">
						<%--옵션 선택 페이지로 예약할 차번호, 차이미지이름, 대여금액 전달 --%>
						<input type="hidden" name="carno" value="${vo.carno }">
						<input type="hidden" name="carimg" value="${vo.carimg }">
						<input type="hidden" name="carprice" value="${vo.carprice }">
						<input type="button" value="이전" 
						onclick="location.href='<%=contextPath%>/Car/CarList.do'">
					</td>
					<td align="center" width="200">
						<input type="submit" value="옵션선택하기">
					</td>
				</tr>
			</table>
		
		</form>
		<p>
			<b>차량 정보 상세 내용</b>
		</p>
			${vo.carinfo}
	</center>
</body>
</html>