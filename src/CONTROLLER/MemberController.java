package CONTROLLER;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.MemberDAO;
import SERVICE.MemberService;
import VO.MemberVo;

@WebServlet("/member/*")
public class MemberController extends HttpServlet{
	
	
	MemberService memberservice;
	

	@Override
	public void init() throws ServletException {
		memberservice = new MemberService();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, 
						HttpServletResponse response) 
						throws ServletException, IOException {
		doHandle(request,response);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, 
						HttpServletResponse response) 
						throws ServletException, IOException {
		doHandle(request,response);
		
	}

	private void doHandle(HttpServletRequest request, 
							HttpServletResponse response) 
							throws ServletException, IOException {
		
		//한글처리
		request.setCharacterEncoding("utf-8");
		//웹브라우저로 응답할 데이터 종류 설정
		response.setContentType("text/html;charset=utf-8");
		//웹브라우저와 연결된 출력 스트림 통로 만들기
		PrintWriter out = response.getWriter();
		//서블릿으로 요청한 주소를 request에서 얻기
		String action = request.getPathInfo();//2단계 요청 주소
		System.out.println("요청한  주소 : "+action);
		// /join.me <- 회원가입시 입력하는 화면 요청!
		// /joinIdCheck.me <- 아이디 중복 확인 요청 !
		// /joinPro.me <- 회원가입 요청 주소!
		// /login.me <- 로그인 요청을 위해 아이디와 비밀번호를 입력하는 화면 요청!
		// /loginPro.me <- 로그인을 요청!
		// /logout.me <- 로그아웃 요청!
		
		//조건에 따라서 포워딩 또는 보여줄 VIEW주소 경로를 저장할 변수
		String nextPage = null;
		//요청한 중앙화면 뷰 주소를 저장할 변수
		String center = null;
		
		switch (action) {
		 	//회원가입 화면 요청
			case "/join.me":
//				// members/join.jsp 중앙화면뷰 주소 얻기
//				center = request.getParameter("center");
				
			//부장----	
				center = memberservice.serviceJoinName(request);
				
				// members/join.jsp 중앙화면뷰 주소 바인딩
				request.setAttribute("center", center);
				nextPage = "/CarMain.jsp";
				break;
			//아이디 중복 체크 요청!
			case "/joinIdCheck.me":
//				//입력한 아이디 얻기
//				String id = request.getParameter("id");
				
			  //부장---
				//입력한 아이디가 DB에 저장되어 있는지 중복 체크 작업
				//true -> 중복 , false -> 중복아님 둘중 하나를 반환 받음
				boolean result = memberservice.serviceOverLappedId(request);
				
				 
				//아이디 중복결과를 다시 한번 확인 하여 조건값을 
				//join.jsp파일과 연결된 join.js파일에 작성해 놓은
				//success:function의 data매개변수로 웹브라우저를 거쳐 보냅니다!
				if (result == true) {
					out.write("not_usable");
					return;
				} else if (result == false) {
					out.write("usable");
					return;
				}
				break;
			
			//회원가입 요청 주소를 받았을때!!
			case "/joinPro.me":
				
				//부장---
				memberservice.serviceInsertMember(request);
				
					
				nextPage="/CarMain.jsp";
				
				break;
			
			//로그인 요청 화면
			case "/login.me":
				
				
				//부장---
				//  "members/login.jsp"
				center = memberservice.serviceLoginMember();
				
				//중앙화면 주소 바인딩
				request.setAttribute("center", center);
				
				//전체 메인화면 주소 저장
				nextPage="/CarMain.jsp";
				
				break;
			
			//아이디와 비밀번호를 작성하고 로그인버튼 눌렀을때 !
			case "/loginPro.me":
				
//				String login_id = request.getParameter("id");
//				String login_pass = request.getParameter("pass");
				
				//요청한 값을 이용해 클라이언트의 웹브라우저로 응답할 값을 마련
				//요약 : DB작업 등의 비즈니스로직처리
				//check변수에 저장되는 값이 1이면 아이디, 비밀번호 맞음
				//                   0이면 아이디맞음, 비밀번호 틀림
				//					 -1이면 아이디 틀림 
				int check = memberservice.serviceUserCheck(request);
				
				
				
				if(check == 0) {//아이디 맞고 비밀번호 틀림
					out.println("<script>");
					out.println("window.alert('비밀번호 틀림');");
					out.println("history.go(-1);");
					out.println("</script>");
					return;
				}else if(check == -1){//아이디 틀림
					out.println("<script>");
					out.println("window.alert('아이디 틀림');");
					out.println("history.go(-1);");
					out.println("</script>");
					return;
				}
				
				
				
				//메인화면 view 주소
				nextPage = "/CarMain.jsp";
				break;
			
			//로그아웃 요청을 받았을때...
			case "/logout.me":
				
				
				//---부장
				//로그아웃을 위해 session에 저장되어 있던 아이디를 제거 하기 위해
				//request전달
				memberservice.serviceLogOut(request);
				
				
				//메인화면 view 주소
				nextPage = "/CarMain.jsp";
				
				break;
				
			case "/kakaoLoginPro.me":
				
				
				//---부장
				memberservice.serviceKaKaoLoginMember(request);
					
				//메인화면 view 주소
				nextPage = "/CarMain.jsp";
				
				break;
		
				
			default:
				break;
		}
		
		
		//포워딩 (디스패처 방식)
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
	
	
}
