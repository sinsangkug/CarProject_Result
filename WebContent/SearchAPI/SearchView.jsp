<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>네이버 검색 API</title>
	
	
	<script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script>
		$(function(){
			//검색 요청 버튼(id="searchBtn")을 눌렀을때...
			//$.ajax 메소드를 호출하도록 정의
			$("#searchBtn").click(function(){
				
				$.ajax({
					//요청 주소는 서블릿을 요청할 주소 사용
					url : "../NaverSearchAPI.do",
					type : "get",
					//요청할 값
					//1.입력한 검색어
					//2.선택한 검색 시작 위치
					data : { keyword : $("#keyword").val() , startNum : $("#startNum option:selected").val() },
					//응답 받을 데이터 형식
					dataType : "json",
					
					success : suncFuncJson, //요청 성공시 호출할 메소드 설정
					
					error : errFunc //요청 실패시 호출할 메소드 설정
				});
			});
		});
		
		
		//검색 성공시 결과를 화면에 뿌려줍니다.
		//요청 성공 했을때.. 호출되는 콜백 메소드 만들기
		function suncFuncJson(d){
			
			console.log(d); 
			
			var str = "";
			
			$.each(d.items, function(index, item){
				
				str += "<ul>";
				str += "<li>" + (index+1) + "</li>"; //검색번호
				str += "<li>" + item.title + "</li>"; //검색결과 문서의 제목
				str += "<li>" + item.description + "</li>"; //검색결과 문서의 내용을 요약한 정보
				str += "<li>" + item.bloggername + "</li>"; //검색결과 블로거 이름
				str += "<li>" + item.bloggerlink + "</li>"; //검색결과 블로그 포스트를 작성한 블로거의 링크
				str += "<li>" + item.postdate + "</li>"; //블로그 포스트를 작성한 날짜
				str += "<li><a href='" + item.link + "'>바로가기</a></li>";
				str += "</ul>";
			});
			
			//id="searchResult"인 <div>영역에 HTML태그형식으로 보내서 출력
			$("#searchResult").html(str);
			
			/*
				$.each()메소드는 다음과 같이 두 가지 형식으로 사용합니다.
				
				형식1>
					DOM 선택 후 반복			반복요소의 index(0부터 시작)
					$('선택자').each(function(index,item){
													반복요소의 데이터
						//선택한 요소의 반복 실행 문장;
					});
			
				형식2>
					$.each(배열, function(index, item){
						
						//배열 요소의 반복 실행 문장;
					});
				
					
				참고. 검색결과를 JSON으로 콜백받아 사용하므로 형식2. 사용
			*/
			
		};
		
		function errFunc(){
			
			alert("요청 실패!");
		}
		
		
	</script>
	
	<style type="text/css">
		ul{
			border:2px #cccccc solid;
		}
	
	</style>
	
</head>
<body>
	
	<div>
		<div>
			<!-- 네이버 검색을 위한 <form>태그 정의 -->
			<form id="searchFrm">
				한 페이지에 10씩 출력됨 <br>
				<%-- 검색 시작 위치를 페이지 단위로 선택하고 --%>
				<select id="startNum">
					<option value="1">1페이지</option>
					<option value="11">2페이지</option>
					<option value="21">3페이지</option>
					<option value="31">4페이지</option>
					<option value="41">5페이지</option>										
				</select>
				<%-- 검색어를 입력할 수 있습니다. --%>
				<input type="text" id="keyword" placeholder="검색어를 입력하세요.">
				<%-- 검색 요청 버튼 --%>
				<button type="button" id="searchBtn">검색 요청</button>
			</form>
		</div>
		
		<%-- 검색 결과가 출력되는 영역입니다. 뒤이어 설명할 자바스크립트 코드에서 결과로 받은 JSON데이터를 파싱하여 --%>
		<div class="row" id="searchResult">
			여기에 검색 결과가 출력됩니다.
		</div>
	
	</div>
	
	
</body>
</html>