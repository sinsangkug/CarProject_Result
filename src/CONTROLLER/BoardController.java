package CONTROLLER;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.BoardDAO;
import DAO.MemberDAO;
import SERVICE.BoardService;
import VO.BoardVo;
import VO.MemberVo;

//사장 

//게시판 관련 기능 요청이 들어오면 호출되는 사장님(컨트롤러)
@WebServlet("/board1/*")
public class BoardController extends HttpServlet{
	
	//부장
	BoardService boardservice;
//	
//	//BoardDAO객체를 저장할 참조변수 선언
//	BoardDAO boarddao;
//	//MemberDAO객체를 저장할 참조변수 선언
//	MemberDAO memberdao;
	
	@Override
	public void init() throws ServletException {
		
		boardservice = new BoardService();
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
		// /write.bo <- 새글 작성 화면 요청!
		// /writePro.bo <- 입력한 새글 정보를 DB에 추가 요청!
		// /list.bo <- DB에 저장된 글 목록을 조회 하여 보여주는 요청 주소
		
		
		//조건에 따라서 포워딩 또는 보여줄 VIEW주소 경로를 저장할 변수
		String nextPage = null;
		//요청한 중앙화면 뷰 주소를 저장할 변수
		String center = null;
		//BoardVo객체를 저장할 참조변수 선언
		BoardVo vo = null;
		ArrayList list = null;
		int count = 0;
		
		switch (action) {
		//새글 입력하는 화면 요청!
		case "/write.bo":
			
			//새글을 입력하는 화면에 로그인한 회원의 이름, 아이디, 이메일을 보여주기 위해
			//member테이블에서 SELECT하여 가져와야 합니다.
			HttpSession session = request.getSession();
			String memberid = (String)session.getAttribute("id");
			
		//-----	
			//부장 Boardservice의 메소드를 호출 할때  회원아이디를 전달하여 
			//아이디에 해당하는 회원 한사람을 조회 시킵니다.
			MemberVo membervo = boardservice.serviceMemberOne(memberid);
			
			request.setAttribute("center", "board/write.jsp");
			request.setAttribute("membervo", membervo);
			
			request.setAttribute("nowPage", request.getParameter("nowPage"));
			request.setAttribute("nowBlock", request.getParameter("nowBlock"));
			
			nextPage = "/CarMain.jsp";
			break;
			
		case "/writePro.bo":
			//요청한 값 얻기
			String writer = request.getParameter("w");
			String email = request.getParameter("e");
			String title = request.getParameter("t");
			String content = request.getParameter("c");
			String pass = request.getParameter("p");
			String id = request.getParameter("i");
			
			//요청한 값을 BoardVo객체의 각 변수에 저장
			vo = new BoardVo();
			vo.setB_name(writer);
			vo.setB_email(email);
			vo.setB_title(title);
			vo.setB_content(content);
			vo.setB_pw(pass);
			vo.setB_id(id);
			
			
			//부장
			//-----	
			//응답할 값 마련 (DB에 새글 정보를 INSERT 후 성공하면 추가메세지 마련)
			// result=1 -> DB에 새글 정보 추가 성공
			// result=0 -> DB에 새글 정보 추가 실패
			int result = boardservice.serviceInsertBoard(vo);
			
			// "1" 또는 "0"
			String go = String.valueOf(result);
			
			//write.jsp로 ($.ajax()메소드 내부의 success:function(data)의 data매개변수로 전달)
			if(go.equals("1")) {
				out.print(go);
			}else {
				out.print(go);
			}
			return;
		
		//게시판 모든 글 조회 요청
		case "/list.bo":
			HttpSession session_ = request.getSession();
			String loginid = (String)session_.getAttribute("id");
			
			//부장 호출!
			list = boardservice.serviceBoardListAll();//게시판 DB에 저장된 모든 글 조회 
			count = boardservice.getTotalRecord();//현재 게시판 DB에 저장된 글의 총 수 조회 하는 메소드

				
			//list.jsp페이지의 페이징 처리 부분에서
			//이전 또는 다음 또는 각 페이지 번호를 클릭했을때.. 요청받는 값 얻기
			String nowPage = request.getParameter("nowPage");
			String nowBlock = request.getParameter("nowBlock");
			
			
			request.setAttribute("center", "board/list.jsp");
			request.setAttribute("list", list);
			request.setAttribute("count", count);
			request.setAttribute("id", loginid);
			request.setAttribute("nowPage", nowPage);
			request.setAttribute("nowBlock", nowBlock);
			
			nextPage = "/CarMain.jsp";
			break;
		
			
		case "/searchlist.bo":
			
			//요청한 값 얻기 (검색을 위해 선택한 option의 값 하나, 입력한 검색어)
			String key = request.getParameter("key");
			String word = request.getParameter("word");
			
			//-----------------
			//부장 호출!
			//검색기준값 과 입력한 검색어를 포함하고 있는 글들을 조회 하는 메소드
			list = boardservice.serviceBoardList(key,word);
			//검색기준값 과 입력한 검색어를 포함하고 있는 글들의 갯수를 조회 하는 메소드 
			count = boardservice.serviceGetTotalRecord(key,word);
			
			
			request.setAttribute("center", "board/list.jsp");
			request.setAttribute("list", list);
			request.setAttribute("count", count);
			
			nextPage = "/CarMain.jsp";
			break;	
		
		//글제목을 눌러 글을 조회한 후 보여주는 중앙 화면 요청!
		case "/read.bo":
			//list.jsp페이지에서 전달하여 요청한 3개의 값을 얻자
			String b_idx = request.getParameter("b_idx");
			String nowPage_ = request.getParameter("nowPage");
			String nowBlock_ = request.getParameter("nowBlock");
			
			//-----------------
			//부장 호출!	
			//글번호 (b_idx)를 이용해 수정 또는 삭제를 위해 DB로부터 조회하기
			vo = boardservice.serviceBoardRead(b_idx);
			
			request.setAttribute("center", "board/read.jsp");
			request.setAttribute("vo", vo);
			
			request.setAttribute("nowPage", nowPage_); //중앙화면 read.jsp로 전달을 위해
			request.setAttribute("nowBlock", nowBlock_);
			request.setAttribute("b_idx", b_idx);
			
			nextPage = "/CarMain.jsp";
			break;
			
		case "/password.do":
			String b_idx_ = request.getParameter("b_idx");
			String password = request.getParameter("pass");
			
			//-----------------
			//부장 호출!	
			//글을 수정 삭제 하기 위한 버튼 활성화를 위해 입력한 비밀번호가 DB에 있는지 체크 하기 위해 호출!
			boolean resultPass = boardservice.servicePassCheck(b_idx_,password);
			
			if(resultPass == true) {
				out.write("비밀번호맞음");
				return;
			}else {
				out.write("비밀번호틀림");
				return;
			}
			
			
		case "/updateBoard.do": //글 수정 요청 주소를 받았을때
			
			String idx_ = request.getParameter("idx");
			String email_ = request.getParameter("email");
			String title_ = request.getParameter("title");
			String content_ = request.getParameter("content");
			
			//-----------------
			//부장 호출!	
			//글수 정 요청 하기 위해 메소드 호출 !	
			int result_ = boardservice.serviceUpdateBoard(idx_,email_,title_,content_);
			
			
			if(result_ == 1) {
				out.write("수정성공");
				return;
			}else {
				out.write("수정실패");
				return;
			}
			
		
		case "/deleteBoard.bo":
			
			String delete_idx = request.getParameter("b_idx");
			
			//-----
			//부장님 호출!!!
			//글삭제 요청!시 삭제할 글번호 전달
			//글삭제에 성공하면   "삭제성공" 메세지 반환,  실패하면 "삭제실패" 메세지 반환 
			String result__ = boardservice.serviceDeleteBoard(delete_idx);
					
			out.write(result__); //AJAX
			
			return;
			
		case "/reply.do":
			
			//주글 (부모글)의 글번호를 얻는다
			String b_idx__ = request.getParameter("b_idx");
			
			String reply_id_ = request.getParameter("id");
			
			
			//-----
			//부장님 호출!!!
			//로그인한 회원 !이 답변글을 작성할수 있도록하기 위해
			//로그인한 회원의 아이디를 전달하여 회원 정보를 조회 함 
			MemberVo reply_vo = boardservice.serviceMemberOne(reply_id_);
			
			//부모글번호  바인딩		
			request.setAttribute("b_idx", b_idx__);
			//조회한 답변글을 작성하는 사람 정보 바인딩 
			request.setAttribute("vo", reply_vo);
			
			//중앙화면(답변글을 작성할 수 있는 화면) View 주소 바인딩
			request.setAttribute("center", "board/reply.jsp");
			
			nextPage = "/CarMain.jsp";
			break;
			
		case "/replyPro.do": //답변글 DB에 추가 요청!
			
			// 주글 (부모글) 글번호 + 작성한 답변글 내용 얻기
			String super_b_idx = request.getParameter("super_b_idx");//부모 글번호
			String reply_id = request.getParameter("id"); //답변글 작성자 아이디
			String reply_name = request.getParameter("writer"); //답변글 작성자 이름
			String reply_email = request.getParameter("email"); //답변글 작성자 이메일			
			String reply_title = request.getParameter("title"); //답변글 제목
			String reply_content = request.getParameter("content"); //답변글 내용
			String reply_pass = request.getParameter("pass"); //답변글 비밀번호
			
			//-----
			//부장님 호출!!!
			//DB에 입력한 답변글을 추가 
			boardservice.serviceReplyInsertBoard(super_b_idx,
												 reply_id,
												 reply_name,
												 reply_email,
												 reply_title,
												 reply_content,
												 reply_pass);	
			
		
			
			//답변글 추가 성공 후 
			//다시 전체글을 조회 하기 위해 요청주소를 nextPage변수에 담아서
			nextPage = "/board1/list.bo";
			
		default:
			break;
		}
		
		
		//포워딩 (디스패처 방식)
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
	
	
}
