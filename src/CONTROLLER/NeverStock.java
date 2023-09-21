package CONTROLLER;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

// 실시간 주식 정보 웹 크롤링 해서 얻기

@WebServlet("/stock.do")
public class NeverStock extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, 
						HttpServletResponse response) 
						throws ServletException, IOException {
		
		doHandle(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, 
							HttpServletResponse response) 
							throws ServletException, IOException {
		
		doHandle(request, response);
	}
	
	
	protected void doHandle(HttpServletRequest request, 
							HttpServletResponse response) 
							throws ServletException, IOException {
		
		String URL = "https://finance.naver.com/item/main.naver?code=005930";
		
		Document doc;
		
		try {
			//크롤링 할 주소페이지의 전체 코드를 받아온다.
			doc = Jsoup.connect(URL).get();
//			System.out.println(doc);
			
			//실시간 주식 정보가 보이는 ~ 날짜 시간 추출
			//doc.select를 통해 안에서 원하는 elements만 쏙 뽑아와서
			//이제 내가 원하는 값들을 정리해준다!
			Elements elem = doc.select(".date");
			
			//text()메소드를 이용해 각 HTML내부의 텍스트를 뽑아내고
			//split(" ")메소드에 " "빈공백 기준으로 문자열을 뽑아내 String[]배열에 담아 반환 받는다.
			String[] str = elem.text().split(" ");
			
			for(int i=0; i<str.length; i++) {
				System.out.println(str[i]);
//				2023.02.13		index 0
//				15:18			index 1
//				기준(장중)			index 2
//				(2022.09)		index 3
//				(2022.09)		index 4
			}
//--------------------------------------------------------------------------------------------------------------------------
			
			Elements todaylist = doc.select(".new_totalinfo dl>dd");
			System.out.println(todaylist);
			
			String juga = todaylist.get(3).text().split(" ")[1];
			String DungRakrate = todaylist.get(3).text().split(" ")[6];
			String siga =  todaylist.get(5).text().split(" ")[1];
			String goga = todaylist.get(6).text().split(" ")[1];
			String zeoga = todaylist.get(8).text().split(" ")[1];
			String georaeryang = todaylist.get(10).text().split(" ")[1];

			String stype = todaylist.get(3).text().split(" ")[3];
			String vsyesterday = todaylist.get(3).text().split(" ")[4];
			
			System.out.println("삼성전자 주가------------------");
			System.out.println("주가:"+juga);
			System.out.println("등락률:"+DungRakrate);
			System.out.println("시가:"+siga);
			System.out.println("고가:"+goga);
			System.out.println("저가:"+zeoga);
			System.out.println("거래량:"+georaeryang);
			System.out.println("타입:"+stype);
			System.out.println("전일대비:"+vsyesterday);
			System.out.println("가져오는 시간:"+str[0]+str[1]);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
		}
	}
}
