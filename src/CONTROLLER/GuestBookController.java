package CONTROLLER;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.MemberDAO;
import SERVICE.GestBookService;
import SERVICE.MemberService;
import VO.GuestbookBean;
import VO.MemberVo;

//사장 
@WebServlet("/Guestbook/*")
public class GuestBookController extends HttpServlet{
	
	 
//	MemberService memberservice;
	GestBookService gestbookservice;

	List list;
	GuestbookBean vo;
	
	@Override
	public void init() throws ServletException {
		gestbookservice = new GestBookService();
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

	public void doHandle(HttpServletRequest request, 
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
		
		//  /GuestbookListAction.gb	   방명록 글목록 조회후 글목록 VIEW디자인화면 요청을 받았을때....
		//  /GuestbookWriteAction.gb   방명록 글등록 요청주소를 받았을때..
		//  /GuestbookReplyFormAction.gb  방명록글 답글 VIEW디자인화면 요청을 받았을때...
		
		
		//조건에 따라서 포워딩 또는 보여줄 VIEW주소 경로를 저장할 변수
		String nextPage = null;
		//요청한 중앙화면 뷰 주소를 저장할 변수
		String center = null;
		
		switch (action) {
		
			//방명록 글목록 조회후 글목록 VIEW디자인화면 요청을 받았을때....
			case "/GuestbookListAction.gb":				
				try {
					
					gestbookservice.service(request, response);
					
					//중앙화면
					center = gestbookservice.gestbookView();
					request.setAttribute("center", center);
					
					
					nextPage = "/CarMain.jsp";
										
				} catch (Exception e) {
					e.printStackTrace();
				}
									
				break;
			
			//  /GuestbookWriteAction.gb   방명록 글등록 요청주소를 받았을때..	
			case "/GuestbookWriteAction.gb":	
							
				try {
						//방명록 글이 성공적으로 추가되면 true반환   실패면 false반환
						boolean result = gestbookservice.writeService(request);
						
						if(result == true) {
							
							//중앙화면
							//guestbook/GuestbookForm.jsp
							center = gestbookservice.gestbookView();
						
							request.setAttribute("center", center);
							
							//방명록 글목록 조회후 글목록 VIEW디자인화면 재요청주소 저장 후 포워딩  
							nextPage = "/Guestbook/GuestbookListAction.gb";
							
							break;
						}
						
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			//  /GuestbookReplyFormAction.gb  방명록글 답글 VIEW디자인화면 요청을 받았을때...	
			case "/GuestbookReplyFormAction.gb":	
				
				
				int guestbook_no = Integer.parseInt(request.getParameter("num"));
				String pageNum = request.getParameter("page");
				
				
				GuestbookBean guestbook = gestbookservice.getGuestbookService(guestbook_no);
				
				request.setAttribute("guestbook", guestbook);
				request.setAttribute("pageNum", pageNum);
				
				
				//답변을 달수 있는 팝업창 
				nextPage = "/guestbook/GuestbookReplyForm.jsp";
				
				break;

			//방명록 답변글 창에서 답변글 내용을 입력하고 등록버튼 눌렀을때.. 답변글 DB에 추가 요청	
			case "/GuestbookReplyAction.gb":
				
				try {
						//방명록글의 답변글이 성공적으로 추가되면 true반환   실패면 false반환
						boolean result = gestbookservice.replyWriteService(request);
						
						if(result == true) {
							
							//중앙화면
							//guestbook/GuestbookForm.jsp
							center = gestbookservice.gestbookView();
						
							request.setAttribute("center", center);
							
							
							//방명록 글목록 조회후 글목록 VIEW디자인화면 재요청주소 저장 후 포워딩  
							nextPage = "/Guestbook/GuestbookListAction.gb?page="+request.getParameter("page");
							
							break;
						}
						 
				} catch (Exception e) {
					e.printStackTrace();
				}

			//  /GuestbookUpdateFormAction.gb  
			//  방명록글 수정 하는 VIEW디자인화면 요청을 받았을때...	
			case "/GuestbookUpdateFormAction.gb":	
				
				
				int guestbook_no_ = Integer.parseInt(request.getParameter("num"));
				String pageNum_ = request.getParameter("page");
				
				
				GuestbookBean guestbook_ = gestbookservice.getGuestbookService(guestbook_no_);
				
				request.setAttribute("guestbook", guestbook_);
				request.setAttribute("pageNum", pageNum_);
				
				
				//수정글을 입력할 수 있는 팝업창 
				nextPage = "/guestbook/GuestbookUpdateForm.jsp";
				
				break;				
			
				
			//답변글 내용 수정창에서  수정전  비밀번호를 입력하고 확인을 누르면
			//답변글의 글번호, 입력한 비밀번호가 전달된 답변글을 수정할 화면을 다시 요청한다.
				//수정을 위해 입력한 비밀번호가 DB에 저장된 답변글의 비밀번호와 일치하면?
				//비밀번호 입력 디자인은 웹브라우저 화면에 보이지 않게 하고 
				//대신 ~~~  수정시 입력하는 화면을 보이게 활성화 하게 된다.
			case "/GuestbookPwCheckAction.gb":
				
				try {		
					gestbookservice.GuestbookPwCheckService(request,response);
				
					break;
			
				} catch (Exception e) {
					e.printStackTrace();
				}
				 
				
				
			default:
				break;	
		}
		
		
//		switch (action) {
//		 	//방명록  화면 요청
//			case "/GuestbookListAction.gb":
////				// members/join.jsp 중앙화면뷰 주소 얻기
////				center = request.getParameter("center");
//				
//			//부장----	
//				center = memberservice.serviceJoinName(request);
//				
//				// members/join.jsp 중앙화면뷰 주소 바인딩
//				request.setAttribute("center", center);
//				nextPage = "/CarMain.jsp";
//				break;
//			//아이디 중복 체크 요청!
//			case "/joinIdCheck.me":
////				//입력한 아이디 얻기
////				String id = request.getParameter("id");
//				
//			  //부장---
//				//입력한 아이디가 DB에 저장되어 있는지 중복 체크 작업
//				//true -> 중복 , false -> 중복아님 둘중 하나를 반환 받음
//				boolean result = memberservice.serviceOverLappedId(request);
//				
//				 
//				//아이디 중복결과를 다시 한번 확인 하여 조건값을 
//				//join.jsp파일과 연결된 join.js파일에 작성해 놓은
//				//success:function의 data매개변수로 웹브라우저를 거쳐 보냅니다!
//				if (result == true) {
//					out.write("not_usable");
//					return;
//				} else if (result == false) {
//					out.write("usable");
//					return;
//				}
//				break;
//			
//			//회원가입 요청 주소를 받았을때!!
//			case "/joinPro.me":
//				
//				//부장---
//				memberservice.serviceInsertMember(request);
//				
//					
//				nextPage="/CarMain.jsp";
//				
//				break;
//			
//			//로그인 요청 화면
//			case "/login.me":
//				
//				
//				//부장---
//				center = memberservice.serviceLoginMember();
//				
//				//중앙화면 주소 바인딩
//				request.setAttribute("center", center);
//				
//				//전체 메인화면 주소 저장
//				nextPage="/CarMain.jsp";
//				
//				break;
//			
//			//아이디와 비밀번호를 작성하고 로그인버튼 눌렀을때 !
//			case "/loginPro.me":
//				
////				String login_id = request.getParameter("id");
////				String login_pass = request.getParameter("pass");
//				
//				//요청한 값을 이용해 클라이언트의 웹브라우저로 응답할 값을 마련
//				//요약 : DB작업 등의 비즈니스로직처리
//				//check변수에 저장되는 값이 1이면 아이디, 비밀번호 맞음
//				//                   0이면 아이디맞음, 비밀번호 틀림
//				//					 -1이면 아이디 틀림 
//				int check = memberservice.serviceUserCheck(request);
//				
//				
//				
//				if(check == 0) {//아이디 맞고 비밀번호 틀림
//					out.println("<script>");
//					out.println("window.alert('비밀번호 틀림');");
//					out.println("history.go(-1);");
//					out.println("</script>");
//					return;
//				}else if(check == -1){//아이디 틀림
//					out.println("<script>");
//					out.println("window.alert('아이디 틀림');");
//					out.println("history.go(-1);");
//					out.println("</script>");
//					return;
//				}
//				
//				
//				
//				//메인화면 view 주소
//				nextPage = "/CarMain.jsp";
//				break;
//			
//			//로그아웃 요청을 받았을때...
//			case "/logout.me":
//				
//				
//				//---부장
//				//로그아웃을 위해 session에 저장되어 있던 아이디를 제거 하기 위해
//				//request전달
//				memberservice.serviceLogOut(request);
//				
//				
//				//메인화면 view 주소
//				nextPage = "/CarMain.jsp";
//				
//				break;
//				
//			case "/kakaoLoginPro.me":
//				
//				
//				//---부장
//				memberservice.serviceKaKaoLoginMember(request);
//					
//				//메인화면 view 주소
//				nextPage = "/CarMain.jsp";
//				
//				break;
//		
//				
//			default:
//				break;
//		}
//		
//		
		//포워딩 (디스패처 방식)
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
	
	
}
