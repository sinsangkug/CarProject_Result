package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import VO.CarConfirmVo;
import VO.CarListVo;
import VO.CarOrderVo;

//DB와 연결하여 비지니스로직 처리 하는 클래스
public class CarDAO {
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	//커넥션풀 생성 후 커넥션 객체 얻는 생성자
	public CarDAO() {
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
	
	
	
	//모든 차량 조회
	public Vector getAllCarList() {
		
		Vector vector = new Vector();
		
		//조회된 한행의 차량정보를 저장할 CarListVo객체의 참조변수
		CarListVo vo = null;
		
		try {
			//DB접속 : 커넥션풀에 만들어져 있는 커넥션 얻기
			con = ds.getConnection();
			//DB의 carlist테이블 저장된 모든 차량을 조회하는 SELECT문장을 sql변수에 저장
			String sql = "select * from carlist";
			//SELECT문장을 DB의 carlist테이블에 전송해서 조회할 PreparedStatement객체 얻기
			pstmt = con.prepareStatement(sql);
			//SLELCT문장을 실행하여 조회된 데이터들을 ResultSet에 담아 반환 받기
			rs = pstmt.executeQuery();
			//반복문을 활용하여 ResultSet객체에 조회된 한줄 정보씩 얻어와
			//CarListVo객체의 각변수에 저장 후 
			//CarListVo객체를 Vector배열에 추가 하여 담습니다.
			while (rs.next()) {
				
				vo = new CarListVo(rs.getInt("carno"), 
						rs.getString("carname"), 
						rs.getString("carcompany"), 
						rs.getInt("carprice"), 
						rs.getInt("carusepeople"), 
						rs.getString("carinfo"), 
						rs.getString("carimg"), 
						rs.getString("carcategory"));
				
				vector.add(vo);
			}
		}catch (Exception e) {
			System.out.println("getAllCarList메소드에서 SQL오류 : "+ e);
		}finally {
			//자원해제
			closeResource();
		}
		
		return vector;
	}
	
	//유형별 차량 조회
		public Vector getCategoryList(String option) {
			
			Vector vector = new Vector();
			
			//조회된 한행의 차량정보를 저장할 CarListVo객체의 참조변수
			CarListVo vo = null;
			
			try {
				//DB접속 : 커넥션풀에 만들어져 있는 커넥션 얻기
				con = ds.getConnection();
				//DB의 carlist테이블 저장된 모든 차량을 조회하는 SELECT문장을 sql변수에 저장
				String sql = "select * from carlist where carcategory = '"+option+"'";
				//SELECT문장을 DB의 carlist테이블에 전송해서 조회할 PreparedStatement객체 얻기
				pstmt = con.prepareStatement(sql);
				//SLELCT문장을 실행하여 조회된 데이터들을 ResultSet에 담아 반환 받기
				rs = pstmt.executeQuery();
				//반복문을 활용하여 ResultSet객체에 조회된 한줄 정보씩 얻어와
				//CarListVo객체의 각변수에 저장 후 
				//CarListVo객체를 Vector배열에 추가 하여 담습니다.
				while (rs.next()) {
					
					vo = new CarListVo(rs.getInt("carno"), 
							rs.getString("carname"), 
							rs.getString("carcompany"), 
							rs.getInt("carprice"), 
							rs.getInt("carusepeople"), 
							rs.getString("carinfo"), 
							rs.getString("carimg"), 
							rs.getString("carcategory"));
					
					vector.add(vo);
				}
			}catch (Exception e) {
				System.out.println("getCategoryList메소드에서 SQL오류 : "+ e);
			}finally {
				//자원해제
				closeResource();
			}
			
			return vector;
		}
	
		
		public CarListVo getOneCar(int carno) {
			
			CarListVo vo = null;
			
			try {
				con = ds.getConnection();
				
				String sql = "select * from carlist where carno = '"+carno+"'";
				
				pstmt = con.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					
					vo = new CarListVo(rs.getInt("carno"), 
							rs.getString("carname"), 
							rs.getString("carcompany"), 
							rs.getInt("carprice"), 
							rs.getInt("carusepeople"), 
							rs.getString("carinfo"), 
							rs.getString("carimg"), 
							rs.getString("carcategory"));
					
				}
			}catch (Exception e) {
				System.out.println("getOneCar메소드에서 SQL오류 : "+ e);
			}finally {
				//자원해제
				closeResource();
			}
			
			return vo;
		}
		
//		public String memberHp(HttpSession session) {
//			
//			String hp = null;
//			
//			String id = (String)session.getAttribute("id");
//			
//			try {
//				con = ds.getConnection();
//				pstmt = con.prepareStatement("select hp from member where id=?");
//				pstmt.setString(1, id);
//				rs = pstmt.executeQuery();
//				rs.next();
//				hp = rs.getString("hp");
//				
//				
//			} catch (Exception e) {
//				System.out.println("memberHp메소드 내부에서 SELECT문 실행 오류");
//				e.printStackTrace();
//			} finally {
//				closeResource();
//			}
//			
//			
//			return hp;
//		}
		
		//매개변수로 예약할 정보가 저장된 CarOrderVO객체를 전달 받아
		//DB의 CarOrder예약정보를 저장하는 테이블에 INSERT하는 메소드
		public void insertCarOrder(CarOrderVo vo, HttpSession session) {
//			String hp = memberHp(session);
			
			String id = (String)session.getAttribute("id");
			String sql = null;
			
			
			
			try {
				//커넥션 풀에 만들어져 있는 DB와 미리 연결을 맺은 Connection객체 빌려오기
				//요약 DB연결
				con = ds.getConnection();
				//매개변수로 전달 받는 CarOrderVO객체의 각변수에 저장된 값들을 얻어
				//insert문장 완성하기
				if(id==null) {//비회원으로 예약
					sql = "insert into non_carorder(non_orderid,"
							+ "carno,"
							+ "carqty," 
							+ "carreserveday,"
							+ "carbegindate,"
							+ "carins,"
							+ "carwifi,"
							+ "carnave,"
							+ "carbabyseat," 
							+ "memberphone,"
							+ "memberpass) " 
							+ "values(non_carorder_non_orderid.nextval,"
							+ "?,?,?,?,?,?,?,?,?,?)";
					
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, vo.getCarno());
					pstmt.setInt(2, vo.getCarqty());
					pstmt.setInt(3, vo.getCarreserveday());
					pstmt.setString(4, vo.getCarbegindate());
					pstmt.setInt(5, vo.getCarins());
					pstmt.setInt(6, vo.getCarwifi());
					pstmt.setInt(7, vo.getCarnave());
					pstmt.setInt(8, vo.getCarbabyseat());
					pstmt.setString(9, vo.getMemberphone());
					pstmt.setString(10, vo.getMemberpass());
				}else {//회원 예약
					
					sql ="insert into carorder(orderid,"
							+ "id,"
							+ "carno,"
							+ "carqty," 
							+ "carreserveday,"
							+ "carbegindate,"
							+ "carins,"
							+ "carwifi,"
							+ "carnave,"
							+ "carbabyseat," 
							+ "memberphone,"
							+ "memberpass) " 
							+ "values(carorder_orderid.nextval,"
							+ "?,?,?,?,?,?,?,?,?,(select hp from member where id='"+id+"'),?)";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, vo.getId());
					pstmt.setInt(2, vo.getCarno());
					pstmt.setInt(3, vo.getCarqty());
					pstmt.setInt(4, vo.getCarreserveday());
					pstmt.setString(5, vo.getCarbegindate());
					pstmt.setInt(6, vo.getCarins());
					pstmt.setInt(7, vo.getCarwifi());
					pstmt.setInt(8, vo.getCarnave());
					pstmt.setInt(9, vo.getCarbabyseat());
					pstmt.setString(10, vo.getMemberpass());
					
				
				}
				//PreparedStatement실행객체메모리에 설정된 insert전체 문장을 DB의 테이블에 실행!
				pstmt.executeUpdate();
				
				
				
				
			}catch (Exception e) {
				System.out.println("insertCarOrder메소드 내부에서 SQL실행 오류" + e);
			}finally {
				closeResource();
			}
			
		}
		
		//입력한 휴대폰번호와 비밀번호를 매개변수로 전달받아 그에 해당하는 예약한 정보들을 조회하는 메소드
		public Vector<CarConfirmVo> getAllCarOrder(String memberphone, String memberpass) {
			
			//조회된 CarOrderVo객체들을 저장할 가변길이 배열
			Vector<CarConfirmVo> v = new Vector<CarConfirmVo>();
			
			//조회된 한행의 정보(하나의 렌트정보)를 저장할 변수 선언
			CarConfirmVo vo = null;
			
			try {
				//커넥션풀에서 커넥션 빌려오기
				con = ds.getConnection();
				
				//SELECT문
				//설명 : 예약한 날짜가 현재 날짜 보다 크고? 예약당시 입력한 휴대폰번호와 비밀번호로 예약한 렌트 정보들을 조회하는데..
				//		NON_CARORDER테이블과 carlist테이블을 연결(NATURAL JOIN)해서
				//		정보들을 조회 합니다.
				String sql = "SELECT * FROM NON_CARORDER NATURAL JOIN carlist "
							+ "WHERE sysdate < TO_DATE(carbegindate, 'YYYY-MM-DD') AND "
							+ "memberphone=? AND memberpass=?";
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, memberphone);
				pstmt.setString(2, memberpass);
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					
					vo = new CarConfirmVo();
					vo.setOrderid(rs.getInt("non_orderid")); //주문 ID
					vo.setCarqty(rs.getInt("CARQTY")); //대여차량 갯수
					vo.setCarreserveday(rs.getInt(4)); //대여일수
					vo.setCarbegindate(rs.getString(5)); //대여시작날짜 
					vo.setCarins(rs.getInt("CARINS")); //보험 옵션 적용 여부
					vo.setCarwifi(rs.getInt("CARWIFI")); //와이파이 옵션 적용 여부
					vo.setCarnave(rs.getInt(8)); //네비 옵션 적용 여부
					vo.setCarbabyseat(rs.getInt(9)); //베이비시트 옵션 적용 여부
					vo.setCarname(rs.getString(12)); //차량명
					vo.setCarprice(rs.getInt(14)); //하루 대여 금액
					vo.setCarimg(rs.getString(17)); //차량이미지명
					
					//백터 배열에 VO추가
					v.add(vo);
				}
			}catch (Exception e) {
				System.out.println("getAllCarOrder메소드에서 SQL오류 : "+ e);
				e.printStackTrace();
			}finally {
				closeResource();
			}
			
			return v;
		}

		public CarConfirmVo getOneOrder(int orderid) {
			
			CarConfirmVo vo = null;
			
			try {
				con = ds.getConnection();
				
				String sql = "select * from non_carorder where non_orderid = '"+orderid+"'";
				
				pstmt = con.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					
					vo = new CarConfirmVo();
					vo.setOrderid(orderid);
					vo.setCarbegindate(rs.getString("carbegindate")); //대여시작날짜
					vo.setCarreserveday(rs.getInt("carreserveday")); //대여기간
					vo.setCarins(rs.getInt("carins")); //보험적용여부
					vo.setCarwifi(rs.getInt("carwifi")); //wifi적용여부
					vo.setCarnave(rs.getInt("carnave")); //네비적용여부
					vo.setCarbabyseat(rs.getInt("carbabyseat")); //베이비시트적용여부
					vo.setCarqty(rs.getInt("carqty"));
				}
			}catch (Exception e) {
				System.out.println("getOneOrder메소드에서 SQL오류 : "+ e);
			}finally {
				//자원해제
				closeResource();
			}
			
			return vo;
		}
		
		//입력한 예약정보를 수정 하는 메소드
		public int carOrderUpdate(HttpServletRequest request) {
			
			int result = 0; //수정성공시 1이 저장되고, 수정 실패하면 0이 저장될 변수
			
			try {
				//커넥션 풀에서 DB와 미리 연결을 맺어 만들어져 있는 Connection객체 빌려오기(DB연결)
				con = ds.getConnection();
				//UPDATE구문
				//->예약한 아이디와 예약당시 입력했던 비밀번호와 일치하는 하나의 예약정보를 수정
				String sql = "UPDATE non_carorder SET"
							+ " carbegindate=?, carreserveday=?, carins=?,"
							+ " carwifi=?, carnave=?, carbabyseat=?, carqty=?"
							+ " WHERE non_orderid=? AND memberpass=?";
				
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, request.getParameter("carbegindate"));
				pstmt.setInt(2, Integer.parseInt(request.getParameter("carreserveday")));
				pstmt.setInt(3, Integer.parseInt(request.getParameter("carins")));
				pstmt.setInt(4, Integer.parseInt(request.getParameter("carwifi")));
				pstmt.setInt(5, Integer.parseInt(request.getParameter("carnave")));
				pstmt.setInt(6, Integer.parseInt(request.getParameter("carbabyseat")));
				pstmt.setInt(7, Integer.parseInt(request.getParameter("carqty")));
				pstmt.setInt(8, Integer.parseInt(request.getParameter("orderid")));
				pstmt.setString(9, request.getParameter("memberpass"));
				
				result = pstmt.executeUpdate();
				
				
			}catch (Exception e) {
				System.out.println("carOrderUpdate메소드에서 SQL오류 ");
				e.printStackTrace();
			}finally {
				//자원해제
				closeResource();
			}
			
			return result;
		}

		public int OrderDelete(int orderid, String memberpass) {
			int result = 0; //수정성공시 1이 저장되고, 수정 실패하면 0이 저장될 변수
			
			try {
				//커넥션 풀에서 DB와 미리 연결을 맺어 만들어져 있는 Connection객체 빌려오기(DB연결)
				con = ds.getConnection();
				//DELETE구문
				//->예약한 아이디와 예약당시 입력했던 비밀번호와 일치하는 하나의 예약정보를 삭제
				String sql = "DELETE FROM non_carorder WHERE non_orderid=? AND memberpass=?";
							
				
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, orderid);
				pstmt.setString(2, memberpass);
				
				result = pstmt.executeUpdate();
				
				
				
			}catch (Exception e) {
				System.out.println("OrderDelete메소드에서 SQL오류 ");
				e.printStackTrace();
			}finally {
				//자원해제
				closeResource();
			}
			
			return result;
		}


}
