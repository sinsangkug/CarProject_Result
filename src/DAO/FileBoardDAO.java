package DAO;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import VO.BoardVo;
import VO.FileBoardVo;


//사원

public class FileBoardDAO {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	public FileBoardDAO() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/oracle");
			
		} catch (Exception e) {
			System.out.println("DB연결 실패! - "+ e);
		}
	}
	
	//자원해제 기능
	private void closeResource() {
		if(con != null)try {con.close();} catch (Exception e) {e.printStackTrace();}
		if(pstmt != null)try {pstmt.close();} catch (Exception e) {e.printStackTrace();}
		if(rs != null)try {rs.close();} catch (Exception e) {e.printStackTrace();}
	}
	
	//FileBoard테이블에 저장된 최신 글번호 조회 후 반환 하는 메소드
	public int getNewArticleNO() {
		
		try {
			con = ds.getConnection();
			
			String sql = "select max(idx) from fileboard";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return (rs.getInt(1) + 1); //insert할 글번호 만들어서 반환
			}
			
			
			
		} catch (Exception e) {
			System.out.println("getNewArticleNO메소드 내부에서 오류 :");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		
		
		return 0;
	}
	
	//입력한 새글 정보를 DB에 추가 하는 메소드
	public int insertBoard(FileBoardVo vo) {
		int articleNO = getNewArticleNO(); // 글번호 생성
		String sql = null;
		try {
			//DB접속
			con = ds.getConnection();
			
			//두번째 부터 입력되는 주글 들의 pos를 1증가 시킨다.
			sql = "update fileboard set b_group = b_group +1";
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			
			//insert SQL문 만들기 //b_group , b_level 0 0 으로 insert 규칙3
			sql = "insert into fileboard (idx, b_id, b_pw, b_name, "
							+ "b_email, b_title, b_content, b_group, "
							+ "b_level, b_date, b_cnt, sfile, downcount) "
							+ " values (?,?,?,?,?,?,?,0,0,sysdate,0,?,0)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			pstmt.setString(2, vo.getB_id());
			pstmt.setString(3, vo.getB_pw());
			pstmt.setString(4, vo.getB_name());
			pstmt.setString(5, vo.getB_email());
			pstmt.setString(6, vo.getB_title());
			pstmt.setString(7, vo.getB_content());
			pstmt.setString(8, vo.getSfile());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("insertBoard 메소드 내부에서 오류 !");
			e.printStackTrace();
		}finally {
			closeResource();
		}
		
		return articleNO; //insert한 글번호를 반환 함. 이유 : 글번호 폴더를 만들어서 파일을 그안에 업로드 하기 위함
	}
	
	//현재 게시판 DB에 저장된 글의 총 수 조회 하는 메소드
	public int getTotalRecord() {
		//조회된 글의 글수 저장
		int total = 0;
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement("select count(*) as cnt from fileboard");
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt("cnt");
			
			
		} catch (Exception e) {
			System.out.println("getTotalRecord메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		
		
		return total;
	}
	
	public int getTotalRecord(String key,String word) {
		//조회된 글의 글수 저장
		int total = 0;
		String sql = null;
		
		if(!word.equals("")) {//검색어를 입력했다면 ?
			
			if(key.equals("titleContent")) {//검색 기준값 제목+내용을 선택했다면 ?
				
				sql = "select count(*) as cnt from fileboard "
						+ " where b_title like '%"+word+"%' "
						+ " or b_content like '%"+word+"%'";
			}else{//검색 기준값 작성자를 선택했다면?
				
				sql = "select count(*) as cnt from fileboard where b_name like '%"+word+"%'";
			}
			
			
		}else {
			sql = "select count(*) as cnt from fileboard";
		}
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			total = rs.getInt("cnt");
			
			
		} catch (Exception e) {
			System.out.println("getTotalRecord메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		
		
		return total;
	}
	
	//현재 게시판 DB에 있는 모든 글들을 조회 하는 메소드
	public ArrayList boardListAll() {
		
		String sql = null;
		
		ArrayList list = new ArrayList();
		
		
		try {
			con = ds.getConnection();
			sql = "select * from fileboard order by b_group asc";
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			//조회된 Result의 정보를 한행 단위로 꺼내서
			//BoardVo객체에 한행씩 저장 후 BoardVo객체들을 ArrayList배열에 하나씩 추가해서 저장
			while(rs.next()) {
				FileBoardVo vo = new FileBoardVo(rs.getInt("idx"),
										rs.getString("b_id"),
										rs.getString("b_pw"),
										rs.getString("b_name"), 
										rs.getString("b_email"), 
										rs.getString("b_title"), 
										rs.getString("b_content"), 
										rs.getInt("b_group"), 
										rs.getInt("b_level"), 
										rs.getDate("b_date"), 
										rs.getInt("b_cnt"),
										rs.getString("ofile"),
										rs.getString("sfile"),
										rs.getInt("downcount")
										);
				list.add(vo);
			}
			
		} catch (Exception e) {
			System.out.println("boardListAll 메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		
		
		
		return list;
	}
	
	
	
	//현재 게시판 DB에 있는 글들을 조회 하는 메소드
	//조건 : 선택한 검색기준값과 입력한 검색어 단어를 이용해 글들을 조회!
	public ArrayList boardList(String key,String word) {
		
		String sql = null;
		
		ArrayList list = new ArrayList();
		
		if(!word.equals("")) {//검색어를 입력했다면 ?
			
			if(key.equals("titleContent")) {//검색 기준값 제목+내용을 선택했다면 ?
				
				sql = "select * from fileboard "
						+ " where b_title like '%"+word+"%' "
						+ " or b_content like '%"+word+"%' order by b_group asc";
			}else{//검색 기준값 작성자를 선택했다면?
				
				sql = "select * from fileboard where b_name like '%"+word+"%' order by b_group asc";
			}
			
			
		}else {//검색어를 입력하지 않았다면?
			//모든 글 조회
			//조건 -> b_idx열의 글번호 데이터들을 기준으로 해서 내림 차순으로 정렬 후 조회!
			sql = "select * from fileboard order by b_group asc";
			
			//참고. 정렬 조회 -> order by 정렬기준열 desc(내림차순) 또는 asc(오름차순)
		}
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			//조회된 Result의 정보를 한행 단위로 꺼내서
			//BoardVo객체에 한행씩 저장 후 BoardVo객체들을 ArrayList배열에 하나씩 추가해서 저장
			while(rs.next()) {
				FileBoardVo vo = new FileBoardVo(rs.getInt("idx"),
										rs.getString("b_id"),
										rs.getString("b_pw"),
										rs.getString("b_name"), 
										rs.getString("b_email"), 
										rs.getString("b_title"), 
										rs.getString("b_content"), 
										rs.getInt("b_group"), 
										rs.getInt("b_level"), 
										rs.getDate("b_date"), 
										rs.getInt("b_cnt"),
										rs.getString("sfile"),
										rs.getString("ofile"),
										rs.getInt("downcount"));
				list.add(vo);
			}
			
		} catch (Exception e) {
			System.out.println("boardList 메소드 내부에서 오류!");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		
		
		
		return list;
	}

	public FileBoardVo boardRead(String b_idx) {
		
		FileBoardVo vo = null;
		String sql = null;
		try {
			con = ds.getConnection();
			
			sql = "UPDATE fileboard SET b_cnt = b_cnt +1 where idx = ?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(b_idx));
			pstmt.executeUpdate();
			
			
			sql = "select * from fileboard where idx = ?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(b_idx));
			
			rs = pstmt.executeQuery();
			
			//조회된 Result의 정보를 한행 단위로 꺼내서
			//BoardVo객체에 한행의 정보를 저장합니다.
			if(rs.next()) {
				vo = new FileBoardVo(rs.getInt("idx"),  //getIdx()
									rs.getString("b_id"), //getB_Idx()
									rs.getString("b_pw"),
									rs.getString("b_name"), 
									rs.getString("b_email"), 
									rs.getString("b_title"), 
									rs.getString("b_content"), 
									rs.getInt("b_group"), 
									rs.getInt("b_level"), 
									rs.getDate("b_date"), 
									rs.getInt("b_cnt"),
									rs.getString("ofile"),
									rs.getString("sfile"),
									rs.getInt("downcount")
									);
									
				
			}
		}catch (Exception e) {
			System.out.println("boardRead메소드에서 SQL오류 : ");
			e.printStackTrace();
		}finally {
			//자원해제
			closeResource();
		}
		
		return vo;
		
	}

	public boolean passCheck(String b_idx_, String password) {
		boolean result = false;
		
		try {
			
			con = ds.getConnection();
			
			String sql = "select * from fileboard where b_pw=? and idx=? order by idx desc";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setInt(2, Integer.parseInt(b_idx_));
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = true;
			}else {
				result = false;
			}
			
			
		} catch (Exception e) {
			System.out.println("passCheck메소드에서 SQL오류 : ");
			e.printStackTrace();
		}finally {
			//자원해제
			closeResource();
		}
		
		return result;
	}

	public int updateBoard(String idx_, String email_, String title_, String content_) {
		int result = 0;
		
		
		try {
			con = ds.getConnection();
			//UPDATE구문
			//->예약한 아이디와 예약당시 입력했던 비밀번호와 일치하는 하나의 예약정보를 수정
			String sql = "UPDATE fileboard SET"
						+ " b_email=?, b_title=?, b_content=?"
						+ " WHERE idx=? ";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email_);
			pstmt.setString(2, title_);
			pstmt.setString(3, content_);
			pstmt.setInt(4, Integer.parseInt(idx_));
			
			result = pstmt.executeUpdate();
			
			
		}catch (Exception e) {
			System.out.println("updateBoard메소드 내부에서 오류 ");
			e.printStackTrace();
		}finally {
			//자원해제
			closeResource();
		}
		
		
		return result;
	}

	public String deleteBoard(String delete_idx) {
		String result = null;
		
		try {
			con = ds.getConnection();
			
			String sql = "DELETE FROM fileboard WHERE idx='"+delete_idx+"'";
			
			pstmt = con.prepareStatement(sql);
		
			int val = pstmt.executeUpdate();
			
			if(val == 1) {
				result = "삭제성공";
			}else {
				result = "삭제실패";
			}
			
			
		}catch (Exception e) {
			System.out.println("deleteBoard메소드 내부에서 오류 ");
			e.printStackTrace();
		}finally {
			//자원해제
			closeResource();
		}
		
		
		return result;
	}
	
	
	//답변글 DB에 추가 하는 메소드
	public int replyInsertBoard(String super_b_idx,
								String reply_id,
								String reply_name,
								String reply_email, 
								String reply_title, 
								String reply_content,
								String reply_pass,
								String reply_sfile) {
		int articleNO = getNewArticleNO(); 
		String sql = null;
		
		try {
			con = ds.getConnection();
			
			//1. 부모글의 글번호를 이용해 b_group열의 값과, b_level열의 값을 조회
			sql = "select b_group, b_level from fileboard where idx="+super_b_idx;
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			
			String b_group = rs.getString("b_group");
			String b_level = rs.getString("b_level");
			
			//답변 글 추가 규칙 1
			//2. 부모글의 b_group(그룹번호)보다 큰 그룹번호를 가진 글이 있다면
			//	 1증가하여 update시켜야 합니다
			sql = "update fileboard set b_group = b_group + 1 where b_group > '"+b_group+"'";
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			
			//답변 글 추가 
			//규칙 2. 부모글의 b_group열의 값에 1더한값을 insert
			//규칙 3. 부모글의 b_level열의 값에 1더한값을 insert
			//3. 답변글 내용 DB에 추가
			//답변글 insert SQL문 만들기 
			sql = "insert into fileboard (idx, b_id, b_pw, b_name, "
							+ "b_email, b_title, b_content, b_group, "
							+ "b_level, b_date, b_cnt, sfile, downcount ) "
							+ " values (?,?,?,?,?,?,?,?,?,sysdate,0,?,0)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			pstmt.setString(2, reply_id);
			pstmt.setString(3, reply_pass);
			pstmt.setString(4, reply_name);
			pstmt.setString(5, reply_email);
			pstmt.setString(6, reply_title);
			pstmt.setString(7, reply_content);
			pstmt.setInt(8, Integer.parseInt(b_group)+1);
			pstmt.setInt(9, Integer.parseInt(b_level)+1);
			pstmt.setString(10, reply_sfile);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("replyInsertBoard메소드 내부에서 오류 ");
			e.printStackTrace();
		} finally {
			closeResource();
		}
		
		return articleNO;
	}

	public void downUpdateCount(String idx) {
		
		try {
			con = ds.getConnection();
			
		
			String sql = "UPDATE fileboard SET downcount = downcount + 1 WHERE idx=? ";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(idx));
			pstmt.executeUpdate();
			
			
		}catch (Exception e) {
			System.out.println("downUpdateCount메소드 내부에서 오류 ");
			e.printStackTrace();
		}finally {
			//자원해제
			closeResource();
		}
	}

	
	
	
	
	
		
		
		
}
