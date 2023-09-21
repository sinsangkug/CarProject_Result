package SERVICE;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAO.GestBookDAO;
import VO.GuestbookBean;


//부장
public class GestBookService {

	GestBookDAO gestbookdao;

	public GestBookService() {
		
		gestbookdao = new GestBookDAO();
		
		
	}

	public  void  service(HttpServletRequest request,
					  	 	 HttpServletResponse response) throws Exception {
		
		// 현재 페이지 번호 만들기
		int spage = 1;
		
		String page = request.getParameter("page");

		if(page != null && !page.equals(""))	{
			spage = Integer.parseInt(page);
		}
		
		int listCount = gestbookdao.getGuestbookCount();
		
		// 한 화면에 5개의 게시글을 보여지게함
		// 페이지 번호는 총 5개, 이후로는 [다음]으로 표시
				
		// 전체 페이지 수
		int maxPage = (int)(listCount/5.0 + 0.9);
		// 만약 사용자가 주소창에서 페이지 번호를 maxPage 보다 높은 값을 입력시
		// maxPage에 해당하는 목록을 보여준다.
		if(spage > maxPage) {spage = maxPage;}
		
		//방명록 조회 
		ArrayList<GuestbookBean> list = gestbookdao.getGuestbookList(spage*5-4);
		
		//시작 페이지 번호
		int startPage = (int)(spage/5.0 + 0.8) * 5 - 4;
		//마지막 페이지 번호
		int endPage = startPage + 4;
		//마지막 페이지 번호가  최대페이지 값보다 크면 최대페이지번호를 마지막페이지번호로 설정 
		if(endPage > maxPage)	{
			endPage = maxPage;
		}

		System.out.println("spage : " + spage);
		System.out.println("maxPage : " + maxPage);
		System.out.println("startPage : " + startPage);
		System.out.println("endPage : " + endPage);

		
		// 4개 페이지번호 저장
		request.setAttribute("spage", spage);
		request.setAttribute("maxPage", maxPage);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		// 방명록 목록 저장
		request.setAttribute("list", list);
			
	}

	public String gestbookView() throws Exception{
		
		return "guestbook/GuestbookForm.jsp";
	}

	//방명록 글등록 요청주소를 받았을때..
	//방명록 글이 성공적으로 추가되면 true반환   실패면 false반환
	public boolean writeService(HttpServletRequest request) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		

		String pageNum = request.getParameter("page");

		//입력한 방명록글 정보 얻기 
		String guestbook_id = request.getParameter("guestbook_id");
		String guestbook_password = request.getParameter("guestbook_password");
		String guestbook_content = request.getParameter("guestbook_content");
		
		
		//최신 방명록글 번호 조회해 오기
		int number = gestbookdao.getSeq();
		
		//VO에 저장
		GuestbookBean guestbook = new GuestbookBean();	
		guestbook.setGuestbook_no(number);
		guestbook.setGuestbook_id(guestbook_id);
		guestbook.setGuestbook_password(guestbook_password);
		guestbook.setGuestbook_content(guestbook_content);

		
		//입력한 방명록 답변글 내용 DB에 추가 
		//방명록 답변글이 성공적으로 추가되면 true반환   실패면 false반환
		boolean result = gestbookdao.guestbookInsert(guestbook);
		
		return result;
	}
	
	
//  /GuestbookReplyFormAction.gb  방명록글 답글 VIEW디자인화면 요청을 받았을때...	
	public String gestbookReplyView() {
		return "guestbook/GuestbookForm.jsp";
	}



	//방명록 답변글 창에서 답변글 내용을 입력하고 등록버튼 눌렀을때.. 답변글 DB에 추가 요청	
	public boolean replyWriteService(HttpServletRequest request) throws Exception{
		
		request.setCharacterEncoding("UTF-8");
		 
		//답변글 작성후 성공하면 보여질 방명록 페이지 번호 얻기 
		String pageNum = request.getParameter("page");
		//부모 방명록 글번호 
		int guestbook_no = Integer.parseInt(request.getParameter("guestbook_no"));
		//부모 방명록 글과 답변글을 묶는 그룹 번호 
		int guestbook_group = Integer.parseInt(request.getParameter("guestbook_group"));
		//입력한 방명록글의 답변글 정보 얻기 
		String guestbook_id = request.getParameter("guestbook_id");
		String guestbook_password = request.getParameter("guestbook_password");
		String guestbook_content = request.getParameter("guestbook_content");
		
		

		//최신 방명록글 번호 조회해 오기
		int number = gestbookdao.getSeq();
		
		//VO에 저장
		GuestbookBean guestbook = new GuestbookBean();	
		guestbook.setGuestbook_no(number);//최신 방명록글 번호 저장
		guestbook.setGuestbook_id(guestbook_id);//답변글을 작성하는 사람 아이디 저장
		guestbook.setGuestbook_password(guestbook_password);//답변글 작성자가 입력한 답변글 비밀번호 저장
		guestbook.setGuestbook_content(guestbook_content);//답변글 내용 저장 
		
		guestbook.setGuestbook_group(guestbook_group);//부모 방명록 글과 답변글을 묶는 그룹 번호 
		guestbook.setGuestbook_parent(guestbook_no);//부모 방명록글 번호 저장
		

		
		//입력한 방명록 답변글 내용 DB에 추가 
		//방명록 답변글이 성공적으로 추가되면 true반환   실패면 false반환
		boolean result = gestbookdao.guestbookInsert(guestbook);
		
		return result;
	}

	public GuestbookBean getGuestbookService(int guestbook_no) {
	
		GuestbookBean guestbook = gestbookdao.getGuestbook(guestbook_no);

		return guestbook;
	}

	//답변글 수정을 위해 입력한 비밀번호와 실제 답변글의 비밀번호가 일치한지 비교하는 메소드 
	public void GuestbookPwCheckService(HttpServletRequest request,
										HttpServletResponse response) 
												throws Exception {
		
		String inputPW = request.getParameter("pw");
		String g_no = request.getParameter("num");
		int guestbook_no = Integer.parseInt(g_no);
		
		//방명록 답변글 수정을 위해 글의 비밀번호와  입력한 비밀번호 비교 하여 맞으면 
		//조회한 답변글의 비밀번호를 얻어 리턴
		String dbPW = gestbookdao.getPassword(guestbook_no);
		
		response.setContentType("text/html;charset=euc-kr");
		PrintWriter out = response.getWriter();
		
		//입력한 비밀번호와 조회한 답변글의 비밀번호가 다르면?
		if(!dbPW.equals(inputPW)) {	
			
			out.println("0"); //AJAX
		}
		else {//다르면?	
			out.println("1"); //AJAXs
		}
		
		out.close();
	}
	
}






