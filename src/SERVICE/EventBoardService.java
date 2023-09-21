package SERVICE;

import java.util.ArrayList;

import DAO.BoardDAO;
import DAO.EventBoardDAO;
import DAO.MemberDAO;
import VO.BoardVo;
import VO.EventBoardVo;
import VO.MemberVo;

//부장
public class EventBoardService {

	EventBoardDAO boarddao; 
    MemberDAO memberdao;
	
	public EventBoardService() {
		boarddao = new EventBoardDAO();
		memberdao = new MemberDAO();
	}
	
	//회원아이디를 매개변수로 받아서  회원 조회후 반환 하는 메소드 
	public MemberVo serviceMemberOne(String memberid){
		return  memberdao.memberOne(memberid);		
	}
	
	//응답할 값 마련 (DB에 새글 정보를 INSERT 후 성공하면 추가메세지 마련)
	// result=1 -> DB에 새글 정보 추가 성공
	// result=0 -> DB에 새글 정보 추가 실패
	public int serviceInsertBoard(EventBoardVo vo) {
		return boarddao.insertBoard(vo);
	}

	//게시판 DB에 저장된 모든 글 조회 
	public ArrayList serviceBoardListAll() {
		return boarddao.boardListAll();
	}

	//현재 게시판 DB에 저장된 글의 총 수 조회 하는 메소드
	public int getTotalRecord() {
		return boarddao.getTotalRecord();
	}

	//검색기준값 과 입력한 검색어를 포함하고 있는 글들을 조회 하는 메소드 
	public ArrayList serviceBoardList(String key, String word) {
		return boarddao.boardList(key,word);
	}
	//검색기준값 과 입력한 검색어를 포함하고 있는 글들의 갯수를 조회 하는 메소드 
	public int serviceGetTotalRecord(String key, String word) {
		return boarddao.getTotalRecord(key,word);
	}

	//글번호 (b_idx)를 이용해 수정 또는 삭제를 위해 DB로부터 조회하기
	public EventBoardVo serviceBoardRead(String b_idx) {
		return boarddao.boardRead(b_idx);
	}
 
	//글을 수정 삭제 하기 위한 버튼 활성화를 위해 입력한 비밀번호가 DB에 있는지 체크 하기 위해 호출!
	public boolean servicePassCheck(String b_idx_, String password) {
		return boarddao.passCheck(b_idx_,password);
	}
	
	//글수 정 요청 하기 위해 메소드 호출 !	
	public int serviceUpdateBoard(String idx_, String email_, 
								  String title_, String content_) {
		//수정에 성공하면 1을 반환 실패하면 0을 반환
		return 	boarddao.updateBoard(idx_,email_,title_,content_);
	}
	
	//글삭제 요청!시 삭제할 글번호 전달
	public String serviceDeleteBoard(String delete_idx) {
		
		//글삭제에 성공하면   "삭제성공" 메세지 반환,  실패하면 "삭제실패" 메세지 반환 
		return boarddao.deleteBoard(delete_idx);
	}

	
	//DB에 입력한 답변글을 추가 
	public void serviceReplyInsertBoard(String super_b_idx, 
										String reply_id, 
										String reply_name, 
										String reply_email,
										String reply_title, 
										String reply_content, 
										String reply_pass) {
		
		
		boarddao.replyInsertBoard(super_b_idx,
				reply_id,
				reply_name,
				reply_email,
				reply_title,
				reply_content,
				reply_pass);
		
		
	}
	
	
	
}
