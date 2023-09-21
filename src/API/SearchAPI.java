package API;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//검색결과 확인
//1. 이클립스에서 톰캣 서버를 재시작 합니다.
//2. 웹브라우저 주소창에 다음 주소를 입력하여 접속합니다.
//http://localhost:8090/CarProject/NaverSearchAPI.do
//검색어 키워드를 주소 옆에 ?를 적어주지 않아 500에러가 발생할겁니다.

//3.다음처럼 주소 입력후 옆에 키워드를 입력해서 요청 접속 합니다.
//http://localhost:8090/CarProject/NaverSearchAPI.do?keyword=강남역맛집&startNum=1

@WebServlet("/NaverSearchAPI.do")
public class SearchAPI extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest req, 
							HttpServletResponse resp) 
							throws ServletException, IOException {
		
		//1.인증 정보 설정
		String clientId = "uL7hcg6PQKXJ_jaRqbEM"; //애플리케이션 클라이언트 아이디
        String clientSecret = "LubIcPzPHQ"; //애플리케이션 클라이언트 시크릿
        
        //2.검색 조건 설정
        int startNum = 0; // 검색 시작 위치
        String text = null; // 검색어
        try {
        	//검색 시작 위치 받아오기
        	startNum = Integer.parseInt(req.getParameter("startNum"));
        	//검색어 받아 오기
        	String searchText = req.getParameter("keyword");
        	
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
        
        //웹브라우저로 응답할 데이터 유형 설정
        resp.setContentType("text/html; charset=utf-8");
        //PrintWriter객체 얻어 검색결과 보냄
        resp.getWriter().write(responseBody);
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
