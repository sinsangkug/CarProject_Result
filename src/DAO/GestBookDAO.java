package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import VO.GuestbookBean;






public class GestBookDAO {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;	
	DataSource ds;
	
	public GestBookDAO(){	
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

	
	public int getGuestbookCount(){
		int result = 0;
		
		try {
			con = ds.getConnection();
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT COUNT(*) FROM GUESTBOOK");
			
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource();
		}
		return result;	
	}
	
	
	// ���� ��� ����
	public ArrayList<GuestbookBean> getGuestbookList(int pageNum)
	{
		
		System.out.println(pageNum);
		ArrayList<GuestbookBean> list = new ArrayList<GuestbookBean>();
		
		try {
			con = ds.getConnection();
			
			/*
			SELECT * FROM
			 (SELECT  ROWNUM AS rnum, data.* FROM
			    (SELECT LEVEL, guestbook_no, guestbook_id,
						guestbook_password, guestbook_content,
						guestbook_group, guestbook_parent, guestbook_date
						FROM GUESTBOOK
						START WITH guestbook_parent = 0
						CONNECT BY PRIOR guestbook_no = guestbook_parent
						ORDER SIBLINGS BY guestbook_group desc)              
						data)
			WHERE rnum>=1 and rnum<=5;
			 */
			
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM");
			sql.append(" (SELECT  ROWNUM AS rnum, data.* FROM ");
			sql.append("	(SELECT LEVEL, guestbook_no, guestbook_id,");
			sql.append("			guestbook_password, guestbook_content,");
			sql.append("			guestbook_group, guestbook_parent, guestbook_date");
			sql.append("	FROM GUESTBOOK");
			sql.append("	START WITH guestbook_parent = 0");
			sql.append("	CONNECT BY PRIOR guestbook_no = guestbook_parent");
			sql.append("	ORDER SIBLINGS BY guestbook_group desc)");              
			sql.append(" data) ");
			sql.append("WHERE rnum>=? and rnum<=?");
			
			
			// ���� ����� �� ȭ�鿡 �� 5���� ���̵��� �Ѵ�.
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, pageNum);
			pstmt.setInt(2, pageNum+4);
			
			rs = pstmt.executeQuery();
			while(rs.next())
			{
				GuestbookBean guestbook = new GuestbookBean();
				guestbook.setGuestbook_level(rs.getInt("LEVEL"));
				guestbook.setGuestbook_no(rs.getInt("guestbook_no"));
				guestbook.setGuestbook_id(rs.getString("guestbook_id"));
				guestbook.setGuestbook_password(rs.getString("guestbook_password"));
				guestbook.setGuestbook_content(rs.getString("guestbook_content"));
				guestbook.setGuestbook_group(rs.getInt("guestbook_group"));
				guestbook.setGuestbook_parent(rs.getInt("guestbook_parent"));
				guestbook.setGuestbook_date(rs.getDate("guestbook_date"));
				list.add(guestbook);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			closeResource();
		}
		
		return list;
	}
	
	
	//DB에 저장된 최신 방명록 번호 조회해 오기
		public int getSeq() 
		{
			int result = 1;
			try {
				con = ds.getConnection();
				
				
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT guestbook_no_seq.NEXTVAL FROM DUAL");

				pstmt = con.prepareStatement(sql.toString());
				rs = pstmt.executeQuery(); 

				if (rs.next())	result = rs.getInt(1);

			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}finally {
				closeResource();
			}

			return result;
		} 
	
	
		//입력한 방명록 글 또는 답변글 내용 DB에 추가 
		public boolean guestbookInsert(GuestbookBean guestbook)
		{
			boolean result = false;
			
			try {
				con = ds.getConnection();
 
				//false를 설정하면 INSERT구문 DB에 실행시 자동 커밋 되지 않고
				//개발자가 직접 commit()메소드를 호출하여 수동 커밋을 해 주어야  INSERT 된다. 
				con.setAutoCommit(false);
				
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO GUESTBOOK");
				sql.append(" (GUESTBOOK_NO, GUESTBOOK_ID, GUESTBOOK_PASSWORD, GUESTBOOK_CONTENT");
				sql.append(" , GUESTBOOK_GROUP, GUESTBOOK_PARENT, GUESTBOOK_DATE)");
				sql.append(" VALUES(?,?,?,?,?,?,sysdate)");
	

				
				int no = guestbook.getGuestbook_no(); //최신 방명록글 번호 저장
				int group = guestbook.getGuestbook_group(); //부모 방명록 글과 답변글을 묶는 그룹 번호 
				int parent = guestbook.getGuestbook_parent(); //부모 방명록글 번호 저장 
				
				//부모 방명록 글의 글번호가 0이면 추가할부모 방명록 글과 답변글을 묶는 그룹 번호를  DB에서 조회한 
				//그룹번호로 저장 
				if(parent == 0) group = no;
		
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setInt(1, no); //방명록 글 또는 답변글 글번호 저장
				pstmt.setString(2, guestbook.getGuestbook_id());//방명록 글 또는 답변글을 작성한 사람 아이디 저장
				pstmt.setString(3, guestbook.getGuestbook_password()); //방명록 글 또는 답변글 비밀번호 저장
				pstmt.setString(4, guestbook.getGuestbook_content()); //방명록 글 또는 답변글 내용 저장
				pstmt.setInt(5, group);//방명록 글과 답변글을 묶는 그룹 번호 저장
				pstmt.setInt(6, parent);//답변글의 부모 방명록 글 글번호 저장

				//방명록 글이 DB에 INSERT되면 1을 반환 실패면 0을 반환
				int flag = pstmt.executeUpdate();
				
				if(flag > 0){
					//방명록 글 또는 답변글이 성공적으로 추가됨
					result = true;
					//수동 커밋 영구 반영
					con.commit();
				}
				
			} catch (Exception e) {
				try {
					//방명록 글 또는 답변글이 DB에 INSERT 되지 않으면 작업을 다시 되돌림
					con.rollback(); 
				} catch (SQLException sqle) {
					sqle.printStackTrace();
				} 
				e.printStackTrace();
				
			}finally {
				closeResource();
			}
			
			return result;		//방명록 글 또는 답변글이 성공적으로 추가되면 true반환   실패면 false반환
		} // end boardInsert();	


		public GuestbookBean getGuestbook(int guestbook_no) {
			GuestbookBean guestbook = null;
			
			try {
				con = ds.getConnection();
				
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM GUESTBOOK where guestbook_no = ?");
				
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setInt(1, guestbook_no);
				
				rs = pstmt.executeQuery();
				while(rs.next())
				{
					guestbook = new GuestbookBean();
					guestbook.setGuestbook_no(rs.getInt("guestbook_no"));
					guestbook.setGuestbook_id(rs.getString("guestbook_id"));
					guestbook.setGuestbook_password(rs.getString("guestbook_password"));
					guestbook.setGuestbook_content(rs.getString("guestbook_content"));
					guestbook.setGuestbook_group(rs.getInt("guestbook_group"));
					guestbook.setGuestbook_parent(rs.getInt("guestbook_parent"));
					guestbook.setGuestbook_date(rs.getDate("guestbook_date"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				closeResource();	
			}
			return guestbook; 
		}


		//방명록 답변글 수정을 위해 글의 비밀번호와  입력한 비밀번호 비교 하여 맞으면 조회한 답변글의 비밀번호를 얻어 리턴
		public String getPassword(int guestbook_no) {
			String password = null;
			
			try {
				con = ds.getConnection();
				
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT guestbook_password FROM GUESTBOOK where guestbook_no = ?");
				
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setInt(1, guestbook_no);
				
				rs = pstmt.executeQuery();
				if(rs.next()) password = rs.getString("guestbook_password");
				
			} catch (Exception e) {
			
				e.printStackTrace();
				
			} finally {
				closeResource();
			}

			return password;
		}
		
	
	
}





