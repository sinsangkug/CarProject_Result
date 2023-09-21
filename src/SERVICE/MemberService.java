package SERVICE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import DAO.MemberDAO;
import VO.MemberVo;

//---- 부장

public class MemberService {

	//MemberDAO객체를 저장할 참조변수 선언
	MemberDAO memberdao;
	
	public MemberService() {
		memberdao = new MemberDAO();
	}

		
	//회원가입 중앙 화면 VIEW 요청
	public String serviceJoinName(HttpServletRequest request) {
		 
		// members/join.jsp 중앙화면뷰 주소를 얻어 MemberController로  반환
		return request.getParameter("center"); 
	}
	
	
	//아이디 중복 체크 요청
	public boolean serviceOverLappedId(HttpServletRequest request) {
		
		//입력한 아이디 얻기
		String id = request.getParameter("id");
		
		//입력한 아이디가 DB에 저장되어 있는지 중복 체크 작업
		//true -> 중복 , false -> 중복아님 둘중 하나를 반환 받음
		return memberdao.overlappedId(id);
	}

	//회원등록(가입) 요청
	public void serviceInsertMember(HttpServletRequest request) {
		//입력한 회원  정보 얻기 
		String user_id = request.getParameter("id");
		String user_pass = request.getParameter("pass");
		String user_name = request.getParameter("name");
		int user_age = Integer.parseInt(request.getParameter("age"));
		String user_gender = request.getParameter("gender");
		
		String address1 = request.getParameter("address1");
		String address2 = request.getParameter("address2");
		String address3 = request.getParameter("address3");
		String address4 = request.getParameter("address4");
		String address5 = request.getParameter("address5");
		String user_address = address1+address2+address3+address4+address5;
		
		String user_email = request.getParameter("email");
		String user_tel = request.getParameter("tel");
		String user_hp = request.getParameter("hp");
		
		MemberVo vo = new MemberVo(user_id, user_pass, user_name,
									user_age, user_gender, user_address,
									user_email, user_tel, user_hp);
		memberdao.insertMember(vo);	
	}


	//로그인을 하기 위해 아이디 비밀번호를 입력할 수 있는 중앙 화면 요청
	public String serviceLoginMember() {
		
		return "members/login.jsp";
	}

	//로그인 요청
	public int serviceUserCheck(HttpServletRequest request) {
		
		String login_id = request.getParameter("id");
		String login_pass = request.getParameter("pass");
		
		//check = 1 아이디 맞음, 비밀번호 맞음
		//로그인 처리를 위해 session메모리 영역에 세션값 저장 후 포워딩
		
		//session메모리 생성
		HttpSession session = request.getSession();
		//session메모리에 입력한 아이디 바인딩 (저장)
		session.setAttribute("id", login_id);
		
		
		return memberdao.userCheck(login_id,login_pass);
	}

	//로그아웃 요청
	public void serviceLogOut(HttpServletRequest request) {
		
		//기존에 생성했던 session메모리 얻기
		HttpSession session_ = request.getSession();
		session_.invalidate(); //세션에 저장된 아이디 제거
		
	}
	
	
	//카카오 로그인 요청
	public void serviceKaKaoLoginMember(HttpServletRequest request) {
		
		String name = request.getParameter("name");
		
		//session메모리 생성
		HttpSession session_1 = request.getSession();
		//session메모리에 입력한 아이디 바인딩 (저장)
		session_1.setAttribute("id", name);
		
	}
	
	
	
	
}








