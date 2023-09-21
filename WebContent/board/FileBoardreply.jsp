<%@page import="VO.MemberVo"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<% 
	request.setCharacterEncoding("UTF-8");
	String contextPath = request.getContextPath();
	//부모글의 글 번호를 전달받아
	//DB로부터 부모글의 b_group열 값과, b_level열값을 조회 해야 합니다. 그래서 받아 온 것입니다.
	String b_idx = (String)request.getAttribute("b_idx");
	MemberVo vo = (MemberVo)request.getAttribute("vo");
%>
 
<%
	String id = (String)session.getAttribute("id");
	if(id == null){//로그인 하지 않았을경우
%>		
	<script>	
		alert("로그인 하고 글을 작성하세요!"); 
		history.back(); 
 	</script>
<% 	}%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" type="text/css" href="/MVCBoard/style.css"/>
<title>답변글 작성 화면</title>
</head>
<body>
	<p id="t" align="center" style="background-color:aqua">답변글 작성 화면</p>
<form action="<%=contextPath%>/FileBoard/replyPro.do" method="post" onsubmit="return check();" enctype="multipart/form-data">
	<%--답변글(자식글)을 DB에 Insert하기 위해 주글(부모글)의 글번호를 같이 전달 합니다. --%>
	<input type="hidden" name="super_b_idx" value="<%=b_idx%>">
	<%--답변글을 작성하는 사람의 ID를 전달합니다. --%>
	<input type="hidden" name="id" value="<%=id%>">		
<table width="90%" border="0" cellspacing="0" cellpadding="0">
  <tr height="40"> 
    <td width="41%" style="text-align: left"> &nbsp;&nbsp;&nbsp; 
    	<img src="<%=contextPath%>/board/images/board02.gif" width="150" height="30">
    </td>
    <td width="57%">&nbsp;</td>
    <td width="2%">&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="3"><div align="center"><img src="<%=contextPath%>/board/images/line_870.gif" width="870" height="4"></div></td>
  </tr>
  <tr> 
    <td colspan="3"><div align="center"> 
        <table width="95%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td height="20" colspan="3"></td>
          </tr>
          <tr> 
            <td colspan="3" valign="top">
            <div align="center"> 
                <table width="100%" height="373" border="0" cellpadding="0" cellspacing="1" class="border1">
                  <tr> 
                    <td width="13%" height="29" bgcolor="#e4e4e4" class="text2">
                    	<div align="center">작 성 자</div>
                    </td>
                    <td width="34%" bgcolor="#f5f5f5" style="text-align: left">
                    	<input type="text" name="writer" size="20" class="text2" value="<%=vo.getName()%>" readonly />
                    </td>
                    <td width="13%" height="29" bgcolor="#e4e4e4" class="text2">
                    	<div align="center">아 이 디</div>
                    </td>
                    
                    
                    
                    <td width="34%" bgcolor="#f5f5f5" style="text-align: left">
                    	<input type="text" name="writer_id" 
                    	size="20" class="text2" value="<%=id%>" readonly/>
                    </td>
                   </tr>
                   <tr>
                    <td width="13%" bgcolor="#e4e4e4">
                    	<div align="center"> 
                        	<p class="text2">메일주소</p>
                      	</div>
                    </td>
                    <td colspan="3" bgcolor="#f5f5f5" style="text-align: left">
                        <input type="text" name="email" size="40" class="text2" value="<%=vo.getEmail()%>" readonly/>
                    </td>
                  </tr>             
                  <tr> 
                    <td height="31" bgcolor="#e4e4e4" class="text2">
                    	<div align="center">제&nbsp;&nbsp;&nbsp;목</div>
                    </td>
                    <td colspan="3" bgcolor="#f5f5f5" style="text-align: left">
                    	<input type="text" name="title" size="70"/>
                    </td>
                  </tr>
                  <tr> 
                    <td height="31" bgcolor="#e4e4e4" class="text2">
                    	<div align="center">첨부파일 선택</div>
                    </td>
                    <td colspan="3" bgcolor="#f5f5f5" style="text-align: left">
                    	<input type="file" name="fileName" size="70"/>
                    </td>
                  </tr>
                  <tr> 
                    <td bgcolor="#e4e4e4" class="text2">
                    	<div align="center">내 &nbsp;&nbsp; 용</div>
                    </td>
                    <td colspan="3" bgcolor="#f5f5f5" style="text-align: left">
                    	<textarea name="content" rows="15" cols="100"></textarea>
                    </td>
                  </tr>
                  <tr> 
                    <td bgcolor="#e4e4e4" class="text2">
                    	<div align="center">패스워드</div>
                    </td>
                    <td colspan="3" bgcolor="#f5f5f5" style="text-align: left">
                    	<input type="password" name="pass"/>
                    </td>
                    <td colspan="2" bgcolor="#f5f5f5" style="text-align: left">
						<p id="pwInput"></p>
					</td>
                  </tr>
                </table>
              </div>
              </td>
          </tr>
          <tr> 
            <td colspan="3">&nbsp;</td>
          </tr>
          <tr> 
            <td width="48%">
            <!-- 등록 버튼 -->
            <div align="right">
            	<button type="submit">글 등록</button>
            </div>
            </td>
            <td width="10%">
            <!-- 목록보기 -->
            <div align="center">
				<input type="image" 
						src="<%=contextPath%>/board/images/list.gif"
						id="list" 
						onclick="location.href='<%=contextPath%>/FileBoard/list.bo?nowBlock=0&nowPage=0'" />&nbsp;
			</div>
            </td>
            <td width="42%" id="resultInsert"></td>
          </tr>
        </table>
      </div></td>
  </tr>
</table>
</form>
	<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
	
	<script type="text/javascript">
		
	function check(){
		var writer = $("input[name='writer']").val();
		var email = $("input[name='email']").val();
		var title = $("input[name='title']").val();
		var content = $("input[name='content']").val();
		var pass = $("input[name='pass']").val();
		
		if( writer == "" || email == "" || title == "" || content == "" || pass == "" )
			{
				$("#pwInput").text("모두 작성해 주세요.").css("color","red");
				return false;
			}
			return true;
	}
		
		
	</script>
</body>
</html>