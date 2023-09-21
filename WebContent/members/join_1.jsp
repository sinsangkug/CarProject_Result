<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link href="/CarProject/css/default.css" rel="stylesheet" type="text/css">
<link href="/CarProject/css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>

<!-- 본문내용 -->
<center>

		<article>
			<h1>회원가입</h1>
			<%--MemberFrontController서블릿에.. 회원가입 처리 요청! --%>
			<form action="" id="join" method="post" >
				
				<fieldset>
				<legend>기본 정보</legend>
				<label>아이디</label>
					<input type="text" name="id" class="id"><br>
				<label>비밀번호</label>
					<input type="password" name="pass"><br>
				<label>비밀번호 확인</label>
					<input type="password" name="pass2"><br>
				<label>이름</label>
					<input type="text" name="name"><br>
				<label>이메일</label>
					<input type="email" name="email"><br>
				<label>이메일 확인</label>
					<input type="email" name="email2"><br>
				</fieldset>
				
				<fieldset>
				<legend>추가 정보</legend>
				<label>주소</label>
					<input type="text" name="address"><br>
				<label>전화 번호</label>
					<input type="text" name="phone"><br>
				<label>핸드폰 번호</label>
					<input type="text" name="mobile"><br>
				</fieldset>
				<fieldset>
						<input type="submit" value="회원가입" class="submit">
						<input type="reset" value="다시입력" class="cancel">
				</fieldset>			
			</form>
		</article>

</center>
<!-- 본문내용 -->
</body>
</html>