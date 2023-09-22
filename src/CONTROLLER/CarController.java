package CONTROLLER;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAO.CarDAO;
import DAO.MemberDAO;
import VO.CarConfirmVo;
import VO.CarListVo;
import VO.CarOrderVo;
import VO.MemberVo;


@WebServlet("/Car/*")
public class CarController extends HttpServlet{
	
	//CarDAO객체를 저장할 참조변수 선언
	CarDAO cardao;
	
	MemberDAO memberdao;
	
	@Override
	public void init() throws ServletException {
		cardao = new CarDAO();
		memberdao = new MemberDAO();
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
	protected void doHandle(HttpServletRequest request, 
							HttpServletResponse response) 
							throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String action = request.getPathInfo();
		System.out.println("2단계 요청 주소: "+action);
		// /Main <-메인화면 요청
		// /bb 	 <-예약하기 요청
		// /CarList.do <- 전체차량 조회 요청!
		// /carcategory.do <- 차량 유형별 조회 요청!
		// /CarInfo.do <- carno번호에 해당하는 차량 한대의 정보 조회 요청!
		// /CarOption.do <- 렌트 예약을 위해 옵션을 선택할 화면 요청!
		// /CarOptionResult.do <- 추가한 옵션 금액 + 기본 금액 계산 요청
		// /cc <- 예약확인하기위해 예약당시 입력했던 휴대폰 번호, 비밀번호를
		//			입력하여 예약확인을 요청하는 디자인 View를 요청!
		// /CarReserveConfirm.do <- 입력한 휴대폰 번호와 비밀번호로 예약한 렌트 정보 조회요청!
		// /update.do <- 예약한 정보 수정 하기위해 예약한 아이디로 예약정보 조회요청!
		// /updatePro.do <- 입력한 정보 수정 요청!
		// /delete.do
		// /deletePro.do
		// /freeBoard.bo <- 게시판 리스트 화면 요청!
		
		
		String nextPage = null;
		
		if(action.equals("/Main")) {
			
			nextPage = "/CarMain.jsp";
			
		}else if(action.equals("/bb")) {
			
			//중앙 화면 요청한 주소 얻기
			String center = request.getParameter("center");
			
			//request에 CarReservation.jsp 저장(바인딩)
			request.setAttribute("center", center);
			
			nextPage = "/CarMain.jsp";

		}else if(action.equals("/CarList.do")) {
			
			//CarDAO객체의 getAllCarList()메소드를 호출하여 DB에 저장된 모든 차량정보들을
			//각각 CarListVO객체에 저장 후 각각의 CarListVO객체를 Vector배열에 담아 반환 받습니다.
			Vector vector = cardao.getAllCarList();
			
			//View화면에 조회된 백터의 정보를 보여주기 위해
			//request내장객체 영역에 바인딩
			request.setAttribute("v", vector);
			request.setAttribute("center", "CarList.jsp");
			
			nextPage = "/CarMain.jsp";
			
		}else if(action.equals("/carcategory.do")) {
			
			//소형, 중형, 대형중 선택한 <option>의 값 하나 얻기
			String category = request.getParameter("carcategory");
			
			//선택한 차량 종류에 따른 차량 조회를 위해 CarDAO객체의 메소드 호출!
			Vector vector = cardao.getCategoryList(category);
			
			request.setAttribute("v", vector);
			request.setAttribute("center", "CarList.jsp");
			
			nextPage = "/CarMain.jsp";
			
		}else if(action.equals("/CarInfo.do")) {
			
			//CarList.jsp화면에서 차 이미지를 클릭했을때..
			//차량 한대 정보를 조회 하기 위해 carno정보를 request에서 얻자
			int carno = Integer.parseInt(request.getParameter("carno"));
			
			//carno차번호를 이용해 DB에 저장된 차량 한대의 정보를 조회하기 위해
			//CarDAO객체의 getOneCar(int carno)메소드 호출시 차 번호 전달함
			//조회한 차량 한줄 정보를 CarListVo객체의 각 변수에 담아 반환 받자
			CarListVo vo = cardao.getOneCar(carno);
			
			request.setAttribute("vo", vo);
			request.setAttribute("center", "CarInfo.jsp");
			
			nextPage = "/CarMain.jsp";
			
		}else if(action.equals("/CarOption.do")) {
			
//			String carqty = request.getParameter("carqty");
//			int carno = Integer.parseInt(request.getParameter("carno"));
//			int carprice = Integer.parseInt(request.getParameter("carprice"));
//			String carimg = request.getParameter("carimg");
			
			
			
			request.setAttribute("center", "CarOption.jsp");
			
			nextPage = "/CarMain.jsp";
			
		}else if(action.equals("/CarOptionResult.do")) {
			
			//대여차량번호
			int carno = Integer.parseInt(request.getParameter("carno"));
			//대여시작날짜
			String carbegindate = request.getParameter("carbegindate");
			//대여수량
			int carqty = Integer.parseInt(request.getParameter("carqty"));
			//대여금액
			int carprice = Integer.parseInt(request.getParameter("carprice"));
			//대여기간
			int carreserveday = Integer.parseInt(request.getParameter("carreserveday"));
			//보험여부
			int carins = Integer.parseInt(request.getParameter("carins"));
			//와이파이 여부
			int carwifi = Integer.parseInt(request.getParameter("carwifi"));
			//네비게이션 여부
			int carnave	= Integer.parseInt(request.getParameter("carnave"));
			//베이비시트 여부
			int carbabyseat = Integer.parseInt(request.getParameter("carbabyseat"));
			
			//응답할 값 마련 (렌트 할 금액 최종 계산 , 비즈니스로직처리)
			//차량 기본금액 계산 = 대여수량 *대여금액*대여기간
			int totalreserve = carqty * carprice * carreserveday;
			//추가한 옵션금액 계산
			int totaloption = (carins+carwifi+carbabyseat)*carreserveday*10000*carqty; 
			
			//응답할 값을 View(CarOrder.jsp)중앙화면에 보여주기 위해 
			//CarOrderVo객체의 각변수에 저장
			CarOrderVo vo = new CarOrderVo();
			vo.setCarno(carno); vo.setCarqty(carqty); vo.setCarreserveday(carreserveday);
			vo.setCarbegindate(carbegindate); vo.setCarins(carins); vo.setCarwifi(carwifi);
			vo.setCarnave(carnave); vo.setCarbabyseat(carbabyseat);
			
			request.setAttribute("vo", vo);
			request.setAttribute("totalreserve", totalreserve);
			request.setAttribute("totaloption", totaloption);
			
			
			
			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			
			if(id == null) {
				request.setAttribute("center", "CarOrder.jsp");
			}else {
				
				MemberVo membervo = memberdao.memberOneIdPass(id);
				request.setAttribute("membervo", membervo);
				
				request.setAttribute("center", "LoginCarOrder.jsp");
			}
			
			nextPage = "/CarMain.jsp";
			
		}else if(action.equals("/CarOrder.do")) {
			
			//예약할 정보를 DB에 추가 해야 합니다.
			//그러므로 request에 저장된 예약할 정보를 모두 꺼내어서
			//CarOrderVo객체의 각변수에 저장후
			//대여차량번호
			int carno = Integer.parseInt(request.getParameter("carno"));
			//대여시작날짜
			String carbegindate = request.getParameter("carbegindate");
			//대여수량
			int carqty = Integer.parseInt(request.getParameter("carqty"));
			//대여기간
			int carreserveday = Integer.parseInt(request.getParameter("carreserveday"));
			//보험여부
			int carins = Integer.parseInt(request.getParameter("carins"));
			//와이파이 여부
			int carwifi = Integer.parseInt(request.getParameter("carwifi"));
			//네비게이션 여부
			int carnave	= Integer.parseInt(request.getParameter("carnave"));
			//베이비시트 여부
			int carbabyseat = Integer.parseInt(request.getParameter("carbabyseat"));
			
			CarOrderVo vo = new CarOrderVo();
			vo.setCarno(carno); vo.setCarqty(carqty); vo.setCarreserveday(carreserveday);
			vo.setCarbegindate(carbegindate); vo.setCarins(carins); vo.setCarwifi(carwifi);
			vo.setCarnave(carnave); vo.setCarbabyseat(carbabyseat); 

			HttpSession session = request.getSession();
			String id = (String)session.getAttribute("id");
			
			if(id==null) {//비회원으로 예약할때~
				//비회원 예약을 위해 입력했던 핸드폰 번호
				String memberphone = request.getParameter("memberphone");
				//비회원 예약을 위해 입력했던 비밀번호
				String memberpass = request.getParameter("memberpass");
				vo.setMemberphone(memberphone);
				vo.setMemberpass(memberpass);
			}else {
				String memberid = request.getParameter("memberid");
				String memberpass = request.getParameter("memberpass");
				vo.setId(memberid);
				vo.setMemberpass(memberpass);
			}
			
			//CarDAO객체의 insertCarOrder메소드 호출시~ 매개변수로 전달하여
			//DB의 Carorder테이블에 insert추가 시킵니다.
			cardao.insertCarOrder(vo,session);
			
			PrintWriter pw = response.getWriter();
			pw.print("<script>"+"alert('예약되었습니다.');"
						+"location.href='"+request.getContextPath()
						+"/Car/CarList.do';"
						+"</script>");
			
			return;
		}else if(action.equals("/cc")) {
			
			//중앙 화면 요청한 주소 얻기
			String center = request.getParameter("center");
			
			//request에 CarReserveConfirm.jsp 저장(바인딩)
			request.setAttribute("center", center);
			
			nextPage = "/CarMain.jsp";

		}else if(action.equals("/CarReserveConfirm.do")) {
			//요청한 값 얻기
			//입력한 휴대폰번호, 비밀번호
			String memberphone = request.getParameter("memberphone");
			String memberpass = request.getParameter("memberpass");
			
			//예약한 정보를 조회 하기 위해 CarDAO객체의 getAllCarOrder 메소드 호출
			//매개변수로 입력한 휴대번호와 비밀번호를 전달 하여 SELECT문장을 만든 뒤 
			//조회한 정보들을 각각 CarConfirmVo객체에 담아 Vector배열에 최종 저장 후 반환 받자
			Vector<CarConfirmVo> vector = cardao.getAllCarOrder(memberphone,memberpass);
			
			//예약한 정보를 view화면에 보여주기 위해 먼저 request객체에 Vector를 바인딩 합니다.
			request.setAttribute("v", vector);
			request.setAttribute("memberphone", memberphone);
//			request.setAttribute("memberpass", memberpass);
			//중앙화면 view(예약한 정보를 보여줄 view) 주소 또한 request에 바인딩 합니다.
			request.setAttribute("center", "CarReserveResult.jsp");
			
			nextPage = "/CarMain.jsp";
			
			
		}else if(action.equals("/update.do")) { //예약한 정보 수정을 위해 예약한 정보 조회 요청!
			
			//요청한 값 얻기
			int orderid = Integer.parseInt(request.getParameter("orderid"));
			String carimg = request.getParameter("carimg");
			String memberphone = request.getParameter("memberphone");
//			String memberpass = request.getParameter("memberpass"); 
			
			//예약 아이디를 이용해 예약한 정보를 DB에서 조회하기 위해
			//CarDAO객체의 getOneOrder메소드 호출할때 매개변수로 orderid를 전달 하여 조회 해 오자
			CarConfirmVo vo = cardao.getOneOrder(orderid);
			vo.setCarimg(carimg);
			
			request.setAttribute("vo", vo);
			request.setAttribute("memberphone", memberphone);
//			request.setAttribute("memberpass", memberpass);
			
			request.setAttribute("center", "CarConfirmUpdate.jsp");
		
			nextPage = "/CarMain.jsp";
		}else if(action.equals("/updatePro.do")) {
			
			//수정을 위해 입력한 정보들을 request내장객체 메모리 영역에 저장되어 있으므로
			//DB에 UPDATE시키기 위해 CarDAO객체의 carOrderUpdate메소드 호출할때
			//request객체 메모리를 전달해 UPDATE시키자
			int result = cardao.carOrderUpdate(request);
			
			
//			String memberpass = request.getParameter("memberpass");
			String memberphone = request.getParameter("memberphone");
			int orderid = Integer.parseInt(request.getParameter("orderid"));
			String carimg = request.getParameter("carimg");
			PrintWriter pw = response.getWriter();
			
			if(result == 1) {
				
				pw.print("<script>"+"alert('예약 정보가 수정 되었습니다.');"
						+"location.href='"+request.getContextPath()
						+"/Car/update.do?orderid="+orderid+"&carimg="+carimg+"&memberphone="+memberphone+"'"
						+"</script>");
				return;
			}else {
				
				pw.print("<script> alert('예약정보 수정 실패!');"
						+ "history.back();</script>"
						);
				return;
			}
			
			
		}else if(action.equals("/delete.do")) {//예약 삭제를 위해 비밀번호를 입력하는 화면 요청!
			
			String center = request.getParameter("center");
			
			//request에 CarReserveConfirm.jsp 저장(바인딩)
			request.setAttribute("center", center); //Delete.jsp
			
			nextPage = "/CarMain.jsp";
			
		}else if(action.equals("/deletePro.do")) {//예약정보 삭제 요청!
			
			//요청한 값 얻기
			//삭제할 예약아이디, 삭제를 위해 입력한 비밀번호
			int orderid = Integer.parseInt(request.getParameter("orderid"));
			String memberpass = request.getParameter("memberpass");
			String memberphone = request.getParameter("memberphone");
			//응답할 값 마련
			//예약정보를 삭제(취소)하기 위해 CarDAO객체의 OrderDelete메소드 호출할때
			//매개변수로 삭제할 예약아이디와 입력한 비밀번호 전달하여 DB에서 DELETE시키자
			//삭제에 성공하면 OrderDelete 메소드의 반환값은 삭제에 성공한 레코드 개수 1을 반환받고
			//실패하면 0을 반환 받습니다.
			int result = cardao.OrderDelete(orderid,memberpass);
			
			PrintWriter pw = response.getWriter();
			
			if(result == 1) {
				
//				pw.print("<script>"+"alert('예약 정보가 삭제 되었습니다.');"
//						+"location.href='"+request.getContextPath()
//						+"/Car/Main';"
//						+"</script>");
				pw.print("<script>"+"alert('예약 정보가 삭제 되었습니다.');"
						+"location.href='"+request.getContextPath()
						+"/Car/CarReserveConfirm.do?memberphone="+memberphone+"&memberpass="+memberpass+"';"
						+"</script>");
				
				return;
			}else {
				
				pw.print("<script> alert('예약정보 삭제 실패!');"
						+ "history.back();</script>"
						);
				return;
			}
			
			
		}else if(action.equals("/freeBoard.bo")) {
			
			//중앙 화면 요청한 주소 얻기
			String center = request.getParameter("center");
			
			//request에 CarReserveConfirm.jsp 저장(바인딩)
			request.setAttribute("center", center);
			
			nextPage = "/CarMain.jsp";

		}else if(action.equals("/NaverSearchAPI.do")) {
			//1.인증 정보 설정
			String clientId = "uL7hcg6PQKXJ_jaRqbEM"; //애플리케이션 클라이언트 아이디
	        String clientSecret = "LubIcPzPHQ"; //애플리케이션 클라이언트 시크릿
	        
	        //2.검색 조건 설정
	        int startNum = 0; // 검색 시작 위치
	        String text = null; // 검색어
	        try {
	        	//검색 시작 위치 받아오기
	        	startNum = Integer.parseInt(request.getParameter("startNum"));
	        	//검색어 받아 오기
	        	String searchText = request.getParameter("keyword");
	        	
	        	//검색어는 한글 깨짐을 방지 하기 위해 문자처리 방식을 UTF-8로 인코딩 합니다.
	            text = URLEncoder.encode(searchText, "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("검색어 인코딩 실패",e);
	        }

	        //3. API 주소 조합
	        //검색결과 데이터를 JSON으로 받기 위한 API입니다.
	        //검색어를 쿼리스트링으로 보내는데 여기에는 display와 start매개변수도 추가했습니다.
	        //display속성은 한번에 가져올 검색 결과의 갯수이며,
	        //start속성은 검색 시작 위치 입니다.
	        String apiURL = "https://openapi.naver.com/v1/search/blog?query=" + text
	        			  +"&display=10&start="+startNum;    // JSON 결과
	        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // XML 결과

	        
	        //4. API 호출
	        
	        Map<String, String> requestHeaders = new HashMap<>();
	        
	        //클라이언트의 클라이언트 ID와 시크릿을 요청 헤더로 전달해 
	        requestHeaders.put("X-Naver-Client-Id", clientId);
	        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
	        //API를 호출합니다.
	        String responseBody = get(apiURL,requestHeaders);

	        //검색 결과를 콘솔에 출력하고
	        System.out.println(responseBody);
	        
//	        //웹브라우저로 응답할 데이터 유형 설정
//	        response.setContentType("text/html; charset=utf-8");
//	        //PrintWriter객체 얻어 검색결과 보냄
//	        response.getWriter().write(responseBody);
	        
	        request.setAttribute("searchData", responseBody);
	        request.setAttribute("center", "SearchResult.jsp");
	        
	        nextPage = "/CarMain.jsp";
	        
	        
		}
		
		//포워딩 (디스패처 방식)
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		
		dispatch.forward(request, response);
		
	}
	
	private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}
