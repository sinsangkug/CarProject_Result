<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	String contextPath = request.getContextPath();
	
	//JSONObject객체 형태의 문자열 받기
	String data = (String)request.getAttribute("searchData");
	
	//문자열 -> JSONObject객체로 변환
	//JSONParser객체 생성후!
	JSONParser jsonParser = new JSONParser();
	//parse메소드를 호출할때.. JSONObject객체 형태의 문자열을 전달하면
	//JSONObject객체로 변환해서 반환 해줍니다.
	JSONObject jsonObject = (JSONObject)jsonParser.parse(data);
	
	JSONArray jsonArray = (JSONArray)jsonObject.get("items");
%>

	<hr width="100%" color="red">
	<table width="100%" border="1">
		<tr style="background-color:aqua;">
			<td align="center">제목</td>
			<td align="center">요약내용</td>
			<td align="center">블로거명</td>
			<td align="center">날짜</td>
			<td align="center">링크</td>
		</tr>
	





<%
	for(int i=0; i<jsonArray.size(); i++){
		
		JSONObject object = (JSONObject)jsonArray.get(i);
%>
		<tr>
			<td align="center"><%=object.get("title")%></td>
			<td align="center"><%=object.get("description")%></td>
			<td align="center"><%=object.get("bloggername")%></td>
			<td align="center"><%=object.get("postdate")%></td>
			<td align="center">
				<a href="<%=object.get("link")%>">바로가기</a>
			</td>
		</tr>
				
<%		
	}

%>
</table>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>