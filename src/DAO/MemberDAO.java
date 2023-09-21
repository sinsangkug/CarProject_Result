package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import VO.MemberVo;

public class MemberDAO {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	public MemberDAO() {
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
	
	//아이디 중복 체크
	public boolean overlappedId(String id) {
		
		boolean result = false;
		
		try {
			
			//DB접속 : 커넥션풀에 만들어져 있는 커넥션 얻기
			con = ds.getConnection();
			//오라클의 decode()함수를 이용하여 서블릿에서 전달되는
			//입력한 ID에 해당하는 데이터를 검색하여 true 또는 false를 반환하는데
			//검색한 갯수가 1(검색한 레코드가 존재하면)이면 'true'를 반환,
			//존재하지 않으면 'false'를 문자열로 반환하여 조회합니다.
			String sql = "select decode(count(*), 1, 'true', 'false') as result from member where id=?";
			//SELECT문장을 DB의 member테이블에 전송해서 조회할 PreparedStatement객체 얻기
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			//SLELCT문장을 실행하여 조회된 데이터들을 ResultSet에 담아 반환 받기
			rs = pstmt.executeQuery();
			
			rs.next(); //조회된 제목줄에 커서(화살표)가 있다가 조회된 줄로 내려가 위치함
			
			String value = rs.getString("result");
			result = Boolean.parseBoolean(value);
			//true면 중복 , false면 중복아님
			
		} catch (Exception e) {
			System.out.println("overlappedId 메소드 내부에서 오류!");
			e.printStackTrace();
		}finally {
			closeResource();
		}
		
		return result;
	}

	public void insertMember(MemberVo vo) {
		
		
		try {
			//커넥션 풀에 만들어져 있는 DB와 미리 연결을 맺은 Connection객체 빌려오기
			//요약 DB연결
			con = ds.getConnection();
			//매개변수로 전달 받는 MemberVo객체의 각변수에 저장된 값들을 얻어
			//insert문장 완성하기
			String sql = "INSERT INTO MEMBER(id, pass, name, reg_date, age, gender, address, email, tel, hp) "
					+" VALUES (?, ?, ?, sysdate, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPass());
			pstmt.setString(3, vo.getName());
			pstmt.setInt(4, vo.getAge());
			pstmt.setString(5, vo.getGender());
			pstmt.setString(6, vo.getAddress());
			pstmt.setString(7, vo.getEmail());
			pstmt.setString(8, vo.getTel());
			pstmt.setString(9, vo.getHp());
			//PreparedStatement실행객체메모리에 설정된 insert전체 문장을 DB의 테이블에 실행!
			pstmt.executeUpdate();
			
			
		}catch (Exception e) {
			System.out.println("insertMember메소드 내부에서 SQL실행 오류" + e);
		}finally {
			closeResource();
		}
		
	}

	public int userCheck(String login_id, String login_pass) {
		
		int check = -1;
		
		try {
			//DB접속
			con = ds.getConnection();
			//매개변수 login_id로 받는 입력한 아이디에 해당되는 행을 조회 SELECT문
			String sql = "select * from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, login_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {//입력한 아이디로 조회한 행이 있으면? (아이디가 있으면?)
				
				//입력한 비밀번호와 조회된 비밀번호와 비교해서 있으면 ?(비밀번호가 있으면?)
				if(login_pass.equals(rs.getString("pass"))) {
					check = 1;
				}else {//아이디는 맞고 , 비밀번호 틀림
					check = 0;
				}
			}else {//아이디가 틀림
				check = -1;
			}
			
		} catch (Exception e) {
			System.out.println("userCheck 메소드 내부에서 오류!");
			e.printStackTrace();
		}finally {
			closeResource();
		}
		
		
		return check;
	}

	public MemberVo memberOne(String memberid) {
		MemberVo vo = null;
		try {    
			//DB접속
			con = ds.getConnection();
			//SELECT문
			String sql = "select email, name, id from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberid);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {//입력한 아이디로 조회한 행이 있으면? (아이디가 있으면?)
				
				vo = new MemberVo();
				vo.setEmail(rs.getString("email"));
				vo.setName(rs.getString("name"));
				vo.setId(rs.getString("id"));
				
			}
			
		} catch (Exception e) {
			System.out.println("memberOne 메소드 내부에서 오류!");
			e.printStackTrace();
		}finally {
			closeResource();
		}
		
		
		
		return vo;
	}

	public MemberVo memberOneIdPass(String id) {
		MemberVo membervo = null;
		
		try {
			//DB접속
			con = ds.getConnection();
			//SELECT문
			String sql = "select id, pass from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {//입력한 아이디로 조회한 행이 있으면? (아이디가 있으면?)
				
				membervo = new MemberVo();
				membervo.setId(rs.getString("id"));
				membervo.setPass(rs.getString("pass"));
			}
			
		} catch (Exception e) {
			System.out.println("memberOneIdPass 메소드 내부에서 오류!");
			e.printStackTrace();
		}finally {
			closeResource();
		}
		
		
		return membervo;
	}
		
		
		
		
		
		
}
