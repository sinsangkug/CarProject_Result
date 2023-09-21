<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="icon" href="../../favicon.ico">
  <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
     <!-- Custom styles for this template -->
    <link href="signin.css" rel="stylesheet">
    
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="js/ie-emulation-modes-warning.js"></script>
    
    <script  src="http://code.jquery.com/jquery-latest.min.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>

<center>
<!-- 본문내용 -->
    <div class="container">
    
    <%--MemberController서블릿에.. 로그인 처리 요청시! 입력한 id와 패스워드 전달 --%>
      <form class="form-signin" action="<%=request.getContextPath()%>/member/loginPro.me" id="join">    
        <h2 class="form-signin-heading">로그인 화면</h2>
        	<label class="sr-only">아이디</label>
        		<input type="text" id="id" name="id"  placeholder="아이디" required autofocus>
        	<label for="inputPassword" class="sr-only">비밀번호</label>
        		<input type="password" id="pass" name="pass" class="form-control" placeholder="패스워드" required>
	        <button class="btn btn-lg btn-primary btn-block" type="submit">로그인</button>
	        
      </form>
      <div>
       <a href="javascript:kakaoLogin();">
       		<img src="<%=request.getContextPath()%>/img/kakao_login.png" alt="카카오계정 로그인"/>
       </a>
		   
		</div>
    </div> <!-- /container -->
</center>
<!-- 본문내용 -->
	
<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="/CarProject/js/ie10-viewport-bug-workaround.js"></script>	
    
    
    <script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
    <script>
//         window.Kakao.init('9fda7e229eb85ba2a11c20d82164780e');

//         function kakaoLogin() {
//             window.Kakao.Auth.login({
//                 scope: 'profile, account_email, gender, age_range, birthday', //동의항목 페이지에 있는 개인정보 보호 테이블의 활성화된 ID값을 넣습니다.
//                 success: function(response) {
//                     console.log(response) // 로그인 성공하면 받아오는 데이터
//                     window.Kakao.API.request({ // 사용자 정보 가져오기 
//                         url: '/v2/user/me',
//                         success: (res) => {
//                             const kakao_account = res.kakao_account;
//                             console.log(kakao_account)
//                         }
//                     });
//                     // window.location.href='/ex/kakao_login.html' //리다이렉트 되는 코드
//                 },
//                 fail: function(error) {
//                     console.log(error);
//                 }
//             });
//         }
    </script>
    
    
    <script type="text/javascript" src="https://developers.kakao.com/sdk/js/kakao.js"></script>
	<script type="text/javascript">
	    Kakao.init('9fda7e229eb85ba2a11c20d82164780e');
	    function kakaoLogin() {
	        Kakao.Auth.login({
	            success: function (response) {
	            	
	                Kakao.API.request({
	                    url: '/v2/user/me',
	                    success: function (response) {
	                    	
	                    	 console.log( typeof response) // 로그인 성공하면 받아오는 데이터
	                    	 
// 	                    	 {"id":2690453188,"connected_at":"2023-03-03T07:26:56Z","kakao_account":{"has_email":true,"email_needs_agreement":true}}
	                    	
	                    	 alert(JSON.stringify(response))
	                        var name = response.id;
	                        console.log(name);
	                    	location.href='<%=request.getContextPath()%>/member/kakaoLoginPro.me';
	                    },
	                    fail: function (error) {
	                        alert(JSON.stringify(error))
	                    },
	                });
	            },
	            fail: function (error) {
	                alert(JSON.stringify(error))
	            },
	        });
	    }
	</script>
</body>
</html>