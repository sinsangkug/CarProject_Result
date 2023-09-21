<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page import="java.sql.Connection"%>
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
		//커넥션객체를 저장할 변수
		Connection conn = null;

	
		try {
			//웹프로젝트의 디렉터리에 접근하기 위한 객체 생성
			Context ctx = new InitialContext();
			//커넥션풀 얻기
			DataSource dataFactory = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			
			//커넥션 얻기(DB와 미리연결을 맺은 접속정보객체 얻기)
			conn = dataFactory.getConnection();
			
			if(conn != null){
				out.println("DB연결 성공");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

%>
</body>
</html>