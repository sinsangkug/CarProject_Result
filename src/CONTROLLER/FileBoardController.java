package CONTROLLER;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import DAO.FileBoardDAO;
import DAO.MemberDAO;
import SERVICE.FileBoardService;
import VO.BoardVo;
import VO.FileBoardVo;
import VO.MemberVo;

//사장

//게시판 관련 기능 요청이 들어오면 호출되는 사장님(컨트롤러)
@WebServlet("/FileBoard/*")
public class FileBoardController extends HttpServlet{
	
	//부장
	FileBoardService fileboardservice;
	
//	//FileBoardDAO객체를 저장할 참조변수 선언
//	FileBoardDAO fileboarddao;
//	//MemberDAO객체를 저장할 참조변수 선언
//	MemberDAO memberdao;
	
	//업로드되는 파일 위치
	private static String ARTICLE_REPO = "C:\\file_repo";
	
	@Override
	public void init() throws ServletException {
		
		//부장 생성
		fileboardservice = new FileBoardService();
		
//		fileboarddao = new FileBoardDAO();
//		memberdao = new MemberDAO();
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
		PrintWriter out = null;
		//서블릿으로 요청한 주소를 request에서 얻기
		String action = request.getPathInfo();//2단계 요청 주소
		System.out.println("요청한  주소 : "+action);
		
		
		//조건에 따라서 포워딩 또는 보여줄 VIEW주소 경로를 저장할 변수
		String nextPage = null;
		//요청한 중앙화면 뷰 주소를 저장할 변수
		String center = null;
		//FileBoardVo객체를 저장할 참조변수 선언
		FileBoardVo vo = null;
		ArrayList list = null;
		int count = 0;
		
		switch (action) {
			//새글 입력하는 화면 요청!
			case "/write.bo":
						
				//부장----
				//새글을 입력하는 화면 요청시~~
				//새글을 입력하는 화면에 로그인한 회원의 이름, 아이디, 이메일을 보여주기 위해
				//member테이블에서 SELECT하여 가져와야 합니다.
				MemberVo membervo = fileboardservice.serviceMemberOne(request);
				
				//부장----
				//새글을 입력하는 중앙 View화면 주소 요청!
				center = fileboardservice.serviceFileBoardWriteView();
			
				request.setAttribute("center", center);//"board/FileBoardwrite.jsp"
				request.setAttribute("membervo", membervo);
				
				request.setAttribute("nowPage", request.getParameter("nowPage"));
				request.setAttribute("nowBlock", request.getParameter("nowBlock"));
				
				nextPage = "/CarMain.jsp";
				break;
				
			case "/writePro.bo":
				
				
				//부장---
				//첨부파일이 포함된 글쓰기 (DB에 글 추가) 요청! 
				int num;
				
				try {
					num = fileboardservice.serviceInsertBoard(request,response);
					
					out = response.getWriter();
					out.print("<script>");
					out.print(" alert( '" +num+" 글 추가 성공!' );");
					out.print(" location.href='http://localhost:8090/CarProject/FileBoard/list.bo'");
					out.print("</script>");
					
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
								
			
			//게시판 모든 글 조회 요청
			case "/list.bo":
				
				//부장---
				//요청한 값을 이용해 응답할 값 마련(글 조회)
				list = fileboardservice.serviceBoardListAll();
				
				//부장---
				//모든글의 갯수 조회
				count = fileboardservice.serviceGetTotalRecord();
				
				//--부장
				//모든글을 조회해서 보여줄 VIEW주소 반환 
				center = fileboardservice.serviceFileBoardCenterView();
				
				request.setAttribute("center", center);//"board/FileBoardlist.jsp"
				request.setAttribute("list", list);
				request.setAttribute("count", count);
				
				//-------------------------------------------------------
				
				HttpSession session_ = request.getSession();
				String loginid = (String)session_.getAttribute("id");
				
				request.setAttribute("id", loginid);
				
				//list.jsp페이지의 페이징 처리 부분에서
				//이전 또는 다음 또는 각 페이지 번호를 클릭했을때.. 요청받는 값 얻기
				request.setAttribute("nowPage", request.getParameter("nowPage"));
				request.setAttribute("nowBlock", request.getParameter("nowBlock"));
				
				nextPage = "/CarMain.jsp";
				break;
			
			case "/searchlist.bo":
				
				//--부장
				//검색기준값과 입력한 검색어가 포함된 모든 글 조회 
				list = fileboardservice.serviceBoardList(request);
				//--부장
				//검색기준값과 입력한 검색어가 포함된 모든 글 개수 조회  
				count = fileboardservice.serviceGetTotalRecord(request);
				//--부장
				//검색기준값과 입력한 검색어가 포함된 모든글을 조회해서 보여줄 VIEW주소 반환 
				center = fileboardservice.serviceFileBoardCenterView();
				
				request.setAttribute("center", center);//"board/FileBoardlist.jsp"
				request.setAttribute("list", list);
				request.setAttribute("count", count);
				
				nextPage = "/CarMain.jsp";
				
				break;	
			
			//글제목을 눌러 글을 조회한 후 보여주는 중앙 화면 요청!
			case "/read.bo":
				
//				String b_idx = ;
//				System.out.println("/read.bo -> " + b_idx);
				
				//--- 부장
				//글제목을 눌러 글을 조회한 후 보여주기 위해 조회 해 옴
				vo = fileboardservice.serviceBoardRead(request);
				
				
				
				//--- 부장
				//글제목을 눌러 글을 조회한 후 보여주는 중앙 화면  View 주소를 얻어옴
				center = fileboardservice.serviceBoardReadView();
				
		
				request.setAttribute("center", center);
				request.setAttribute("vo", vo);
				
				request.setAttribute("nowPage", request.getParameter("nowPage")); //중앙화면 FileBoardread.jsp로 전달을 위해
				request.setAttribute("nowBlock", request.getParameter("nowBlock"));
				request.setAttribute("b_idx", request.getParameter("b_idx"));//글번호
				
				nextPage = "/CarMain.jsp";
				break;
				
			case "/password.do":
				
				//-- 부장
				//글쓰기 화면에서 글을 입력하기 위해  입력한 비밀번호가 DB에 존재 하는지 확인 요청!
				boolean resultPass = fileboardservice.servicePassCheck(request);
				  
				out = response.getWriter();
				
				if(resultPass == true) {
					out.write("비밀번호맞음");
					return;
				}else {
					out.write("비밀번호틀림");
					return;
				}
				
				
			case "/updateBoard.do": //글 수정 요청 주소를 받았을때
				
				//--- 부장
				//입력한 내용 글 수정 요청!
				int result_ = fileboardservice.serviceUpdateBoard(request);
				
				
				out = response.getWriter();
		
				if(result_ == 1) {
					out.write("수정성공");
					return;
				}else {
					out.write("수정실패");
					return;
				}
				
			
			case "/deleteBoard.bo":
				
				//--- 부장
				//DB에 있는 글 삭제  그리고 글번호 폴더 삭제!
				//글삭제에 성공하면 "삭제성공" 반환 , 실패하면 "삭제실패" 반환
				String result__;
				
				try {
					
					result__ = fileboardservice.serviceDeleteBoard(request);
					out = response.getWriter();
					out.write(result__); //Ajax 글삭제에 성공하면 "삭제성공" 반환 , 실패하면 "삭제실패" 반환
					
					return;
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
		
			case "/reply.do":
				
			 //부장---	
				//답변글을 작성하는 사람의 아이디를 이용해 회원의  email, name, id 를 조회 해서 반환
				MemberVo reply_vo = fileboardservice.serviceMemberOneView(request);	
			
			//부장---	
				//답변글을 작성하는 중앙 View화면 주소 요청
				center =  fileboardservice.serviecFileBoardReplyView();	
				
				//주글 (부모글)의 글번호를 얻는다
				String b_idx__ = request.getParameter("b_idx");
				
				
				request.setAttribute("vo", reply_vo);
				request.setAttribute("b_idx", b_idx__);
				
				//중앙화면(답변글을 작성할 수 있는 화면) View 주소 바인딩
				request.setAttribute("center", center);
				
				nextPage = "/CarMain.jsp";
				
				break;
//------------------------------------------------여기서 부터 수정 하자 
			case "/replyPro.do": //답변글 DB에 추가 요청!
				
				
				//부장----
				//DB에 입력한 답변글을 추가 
				fileboardservice.serviceReplyInsertBoard(request,response);
				
				out = response.getWriter();
				out.print("<script>");
				out.print(" alert( '글 추가 성공!' );");
				out.print(" location.href='http://localhost:8090/CarProject/FileBoard/list.bo'");
				out.print("</script>");
				
	//			//답변글 추가 성공 후 
	//			//다시 전체글을 조회 하기 위해 요청주소를 nextPage변수에 담아서
	//			nextPage = "/board1/list.bo";
				return;
				
			case "/Download.bo": //다운로드 요청이 들어오면
				
				//부장------
				//파일 다운로드 요청이 들어왔을때..
				//파일 다운로드 로직과  다운로드한 파일의 다운로드수를 DB에 1증가 하여 UPDATE시킴 
				fileboardservice.serviceDownload(request,response);
			
				return;
				
			default:
				break;
			}
			
			
			//포워딩 (디스패처 방식)
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
	}
		
	
}
