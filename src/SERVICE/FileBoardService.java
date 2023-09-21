package SERVICE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import DAO.FileBoardDAO;
import DAO.MemberDAO;
import VO.FileBoardVo;
import VO.MemberVo;


//부장

public class FileBoardService {

	MemberDAO memberdao;
	FileBoardDAO fileboarddao;
	
	public FileBoardService() {
		this.memberdao = new MemberDAO();
		this.fileboarddao = new FileBoardDAO();
	}
	
	
	//새글을 입력하는 중앙 View화면에 로그인한 회원의 정보를 보여주기 위함 
	public MemberVo serviceMemberOne(HttpServletRequest request) {
		
		//새글을 입력하는 화면에 로그인한 회원의 이름, 아이디, 이메일을 보여주기 위해
		//member테이블에서 SELECT하여 가져와야 합니다.
		HttpSession session = request.getSession();
		String memberid = (String)session.getAttribute("id");
		
		return memberdao.memberOne(memberid);//새글을 작성하는 로그인 한 회원의 email, name, id 조회
	}

	//새글을 입력하는 중앙 View화면 주소 요청!
	public String serviceFileBoardWriteView() {
		
		return "board/FileBoardwrite.jsp";
	}
	
	
	//파일을 웹애플리케이션 서버의 하드디스크 공간에 업로드 하는 기능의 메소드
	private Map<String, String> upload(HttpServletRequest request, 
										HttpServletResponse response) 
										throws ServletException, IOException{
		//가변 길이 메모리
		Map<String, String> articleMap = new HashMap<String, String>();
		
	
		//한글처리
		request.setCharacterEncoding("utf-8");
		
		//인코딩 방식 UTF-8 문자열을 변수에 저장
		String encoding = "UTF-8";
		
		//업로드할 파일 폴더 경로와 연결된 File객체 생성
		File currentDirPath = new File("C:\\file_repo");
		
		//업로드 할 파일 데이터를 임시로 저장할 객체 메모리 생성
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//임시 메모리의 최대 사이즈를 1메가 바이트로 설정
		factory.setSizeThreshold(1024*1024*1);
		//임시 메모리에 파일업로드시~ 지정한 1메가 바이트크기를 넘길경우
		//실제 업로드될 파일 폴더 경로를 설정
		factory.setRepository(currentDirPath);
		
		/*
			참고. DiskFileItemFactory 클래스는
				업로드 파일의 크기가 지정한 임시메모리의 크기를 넘기기전까지는
				업로드 한 파일 데이터를 임시메모리에 저장하고
				지정한 임시메모리 크기를 넘길 경우 최종 업로드할 폴더에 업로드하여 저장시킨다.
		*/
		
		//파일 업로드할 메모리를 생성자 쪽으로 전달받아 저장하여 생성되는 파일업로드 기능을 처리할 객체생성
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			
			//uploadForm.jsp에서 업로드요청할 파일의 정보,
			//입력한 문자열 정보들이 저장된 request객체 메모리를
			//ServletFileUpload객체의 parseRequest메소드 호출시
			//매개변수로 전달 하면 request객체 메모리에 저장된
			//요청하는 아이템들을 파싱(추출)해서 DiskFileItem객체에 각각 저장한 후 
			//DiskFileItem객체들을 ArrayList배열에 추가합니다.
			//그 후 ArrayList배열 자체를 반환 받습니다.
			List items = upload.parseRequest(request);
			
			//ArrayList가변 길이 배열의 크기만큼(DiskFileItem객체의 갯수만큼) 반복
			for(int i=0; i<items.size(); i++ ) {
				
				//ArrayList가변 배열에서.. DiskFileItem객체를 얻는다.
				FileItem fileItem = (FileItem)items.get(i);
				
				//DiskFileItem객체(요청한 아이템 하나의 정보)가 파일 아이템이 아닐경우
				if(fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
				}else {//DiskFileItem객체(요청한 아이템 하나의 정보)가 파일일 경우
					System.out.println("파라미터: "+fileItem.getFieldName());
					System.out.println("파일명: "+fileItem.getName());
					System.out.println("파일크기: "+fileItem.getSize() + "bytes");
				
					//업로드할 파일의 크기가 0보다 크다면?(업로드할 파일이 있다면?)
					if(fileItem.getSize() > 0) {
						
						//업로드할 파일명을 얻어 파일명의 뒤에서부터 \\문자열이 포함되어 있는지
						//index위치번호를 알려주는데 없으면 -1을 반환함
						int idx = fileItem.getName().lastIndexOf("\\");
						
						if(idx == -1) {//업로드할 파일명에 \\문자열이 포함되어 있지 않으면?
							
							idx = fileItem.getName().lastIndexOf("/");
							
						}
						
						//업로드할 파일명 얻기
						String fileName = fileItem.getName().substring(idx+1);
						//업로드할 파일 경로 + 파일명을 만들어서 그경로에 접근할 File객체 생성
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						
						articleMap.put(fileItem.getFieldName(), fileName);
						//실제 위 경로에 파일 업로드
						fileItem.write(uploadFile);
						
					}
				}
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return articleMap;
	}

	//"/writePro.bo" 
	public int serviceInsertBoard(HttpServletRequest request, 
								  HttpServletResponse response) throws Exception {
		
		//업로드 후 업로드한 파일명을 담고 있는 해쉬맵을 반환 받는다.
		Map<String, String> articleMap = upload(request, response);
		
		//작성한 글 정보(업로드할 파일정보 포함)를 HashMap에서 꺼내오기
		String writer = articleMap.get("writer"); //작성자
		String email = articleMap.get("email"); //이메일
		String title = articleMap.get("title"); //제목
		String content = articleMap.get("content"); //내용
		String pass = articleMap.get("pass"); //글 비밀번호
		String id = articleMap.get("writer_id"); //글 작성자 아이디
		String sfile = articleMap.get("fileName"); //글을 작성할때 업로드 하기 위해 첨부한 파일명
		
		
		//요청한 값을 FileBoardVo객체의 각 변수에 저장
		FileBoardVo  vo = new FileBoardVo();
		vo.setB_name(writer);
		vo.setB_email(email);
		vo.setB_title(title);
		vo.setB_content(content);
		vo.setB_pw(pass);
		vo.setB_id(id);
		vo.setSfile(sfile);
		
		
		
		//작성한 글 내용을 DB에 insert하고 추가 시킨 글의 글번호를 조회 후 반환 받습니다.
		//글번호 폴더를 생성하기 위해 글번호를 받아 저장할 변수
		int articleNO = fileboarddao.insertBoard(vo);
		
		if(sfile != null && sfile.length() != 0) {
			File srcFile = new File("C:\\file_repo\\temp\\"+sfile);
			File destDir = new File("C:\\file_repo\\"+articleNO);
			
			//DB에 추가한 글에 대한 글번호를 조회해서 가져왔기 때문에 글 번호 폴더 생성
			destDir.mkdirs();
			
			//temp폴더에 업로드된 파일을 글번호폴더로 이동시키자
			FileUtils.moveFileToDirectory(srcFile, destDir, true);
		
		}
		return articleNO;
	}

	//모든글을 조회 - " /list.bo " 요청시 
	public ArrayList serviceBoardListAll() {

		return fileboarddao.boardListAll();
	}

	//모든글의 갯수 조회   - " /list.bo " 요청시 
	public int serviceGetTotalRecord() {

		return fileboarddao.getTotalRecord();
	}

	//검색기준값과 입력한 검색어가 포함된 모든 글 조회 
	public ArrayList serviceBoardList(HttpServletRequest request) {
		
		//요청한 값 얻기 (검색을 위해 선택한 option의 값 하나, 입력한 검색어)
		String key = request.getParameter("key");
		String word = request.getParameter("word");
		
		return fileboarddao.boardList(key,word);//검색기준값과 입력한 검색어가 포함된 모든 글 조회
	}

	//검색기준값과 입력한 검색어가 포함된 모든 글 개수 조회  
	public int serviceGetTotalRecord(HttpServletRequest request) {
		//요청한 값 얻기 (검색을 위해 선택한 option의 값 하나, 입력한 검색어)
		String key = request.getParameter("key");
		String word = request.getParameter("word");
		
		return fileboarddao.getTotalRecord(key,word);//검색기준값과 입력한 검색어가 포함된 모든 글 개수 조회  
	}

	//"/list.bo" 게시판 모든 글 조회 요청 
	// 또는 
	//"/searchlist.bo" 검색기준값과 입력한 검색어가 포함된 모든글을 조회해서 보여줄 VIEW주소 반환 
	public String serviceFileBoardCenterView() {
		
		return "board/FileBoardlist.jsp";
	}

	//"/read.bo" 글제목을 눌러 글을 조회하여 중앙 화면에 보여주기 위해 
	// 글 조회 요청!
	public FileBoardVo serviceBoardRead(HttpServletRequest request) {
		
		//FileBoardlist.jsp페이지에서 전달하여 요청한 3개의 값 중  글번호를 이용해 글 정보를 조회 하기 위해 
		//글번호 b_idx를 얻자.
		String b_idx = request.getParameter("b_idx");
		System.out.println("서비스" + b_idx);
//		String nowPage_ = request.getParameter("nowPage");   <- 컨트롤러에서 얻어 바인딩 하자
//		String nowBlock_ = request.getParameter("nowBlock"); <- 컨트롤러에서 얻어 바인딩 하자
		
		//글번호 (b_idx)를 이용해 수정 또는 삭제를 위해 DB로부터 조회하기
		return 	fileboarddao.boardRead(b_idx);
				
	}

	//"/read.bo" 글제목을 눌러 글을 조회한 후 보여주는 중앙 화면 요청!
	public String serviceBoardReadView() {
		return "board/FileBoardRead.jsp";
	}


	// "/password.do" 글쓰기 화면에서 글을 입력하기 위해  입력한 비밀번호가 DB에 존재 하는지 확인 요청!
	public boolean servicePassCheck(HttpServletRequest request) {
		
		String b_idx_ = request.getParameter("b_idx");
		String password = request.getParameter("pass");
		
		System.out.println(password);
		System.out.println(b_idx_);
		
		boolean result = fileboarddao.passCheck(password,b_idx_);
		System.out.println(result);
		
		return result; //true 입력한 비밀번호 맞음
														//false 입력한 비밀번호 틀림 
	}

	// "/updateBoard.do" 글 수정 요청 주소를 받았을때
	public int serviceUpdateBoard(HttpServletRequest request) {
		
		String idx_ = request.getParameter("idx");
		String email_ = request.getParameter("email");
		String title_ = request.getParameter("title");
		String content_ = request.getParameter("content");
		
		//수정에 성공하면 1을 반환 실패하면 0을 반환 
		return fileboarddao.updateBoard(idx_,email_,title_,content_);
	}

	//DB에 있는 글 삭제 시  삭제하는 글에 첨부된 첨부폴더(글번호 폴더)도 같이 삭제 
	public String serviceDeleteBoard(HttpServletRequest request) throws Exception {
		
		String delete_idx = request.getParameter("b_idx");//글 삭제를 위해 글번호 얻기 
		
		//DB에 있는 글을 삭제 했으니 그글번호에 해당하는 첨부한 파일도 삭제 하기 위해
		//글번호 폴더 경로 작성 
		File deleteDir = new File("C:\\file_repo\\" + delete_idx);
		
		//글삭제에 성공하면 "삭제성공" 반환 받고, 실패하면 "삭제실패" 반환 받음 
		String result__ = fileboarddao.deleteBoard(delete_idx);
		
		if(result__.equals("삭제성공")) {
			
			if (deleteDir.exists()) {
				
				FileUtils.deleteDirectory(deleteDir);//글번호 폴더도 삭제
			}
			
		}
		
		return result__; //글삭제에 성공하면 "삭제성공" 반환 , 실패하면 "삭제실패" 반환  
	}

//"/reply.do"
	//답변 새글을 입력하는 중앙 board/FileBoardreply.jsp View화면에 
	//로그인한 회원이 답변글을 작성 하기 위해 로그인한 회원의  정보 보여주기
	public MemberVo serviceMemberOneView(HttpServletRequest request) {
	
		//답변글을 작성하는 사람의 아이디를 얻는다.
		String reply_id_ = request.getParameter("id");
				
		//답변글을 작성하는 사람의 아이디를 이용해 회원의  email, name, id 를 조회 해서 반환
		return memberdao.memberOne(reply_id_);
	}
	
//"/reply.do"
	//답변글을 작성하는 중앙 View화면 주소 요청
	public String serviecFileBoardReplyView() {
		return "board/FileBoardreply.jsp";
	}

	
//"/replyPro.do" 	
	//DB에 입력한 답변글을 추가
	public void serviceReplyInsertBoard(HttpServletRequest request, 
										HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, String> rp_articleMap = upload(request,response);
		
		// 주글 (부모글) 글번호 + 작성한 답변글 내용 얻기
		String super_b_idx = rp_articleMap.get("super_b_idx");//부모 글번호
		String reply_id = rp_articleMap.get("id"); //답변글 작성자 아이디
		String reply_name = rp_articleMap.get("writer"); //답변글 작성자 이름
		String reply_email = rp_articleMap.get("email"); //답변글 작성자 이메일			
		String reply_title = rp_articleMap.get("title"); //답변글 제목
		String reply_content = rp_articleMap.get("content"); //답변글 내용
		String reply_pass = rp_articleMap.get("pass"); //답변글 비밀번호
		String reply_sfile = rp_articleMap.get("fileName"); //글을 작성할때 업로드 하기 위해 첨부한 파일명
		

		//DB에 입력한 답변글을 추가 하고 추가한 답변글의 글번호 반환
		int rp_articleNO = fileboarddao.replyInsertBoard(	super_b_idx,
															reply_id,
															reply_name,
															reply_email,
															reply_title,
															reply_content,
															reply_pass,
															reply_sfile
														);
		
		
		//글을 작성할때 업로드 하기 위해 첨부한 파일이 있으면?
		//바로 위의 int rp_articleNo변수로 받은 DB에 추가한 답변글의 글번호를 생성하여 
		//temp폴더에 업로드된 파일을 글번호 폴더로 이동 시키자.
		if(reply_sfile != null && reply_sfile.length() != 0) {
			
			//DB에 추가한 답변글의 첨부파일을 업로드 하기 위한 temp폴더 경로와 파일명 
			File srcFile = new File("C:\\file_repo\\temp\\"+reply_sfile);
			//추가한 답변글의 글번호 폴더
			File destDir = new File("C:\\file_repo\\"+rp_articleNO); 
			
			//DB에 추가한 글에 대한 글번호를 조회해서 가져왔기 때문에 글 번호 폴더 생성
			destDir.mkdirs();
			
			//temp폴더에 업로드된 파일을 글번호폴더로 이동시키자
			FileUtils.moveFileToDirectory(srcFile, destDir, true);
		
		}
	
	}

//"/Download.bo"
	//파일 다운로드 요청이 들어왔을때..
	//파일 다운로드 로직과  다운로드한 파일의 다운로드수를 DB에 1증가 하여 UPDATE시킴 
	public void serviceDownload(HttpServletRequest request, 
								HttpServletResponse response) throws IOException {
		
	 //파일 다운로드 로직 구현 ---------------------------------------------	
		//다운로드할 폴더번호 경로와 다운로드할 파일명 얻기
		String idx = request.getParameter("path");
		String name = request.getParameter("fileName");
		
		//다운로드할 파일이 저장되어 있는 경로를 만들어서 변수에 저장
		String filePath = "C:\\file_repo\\" + idx;
		
		File f = new File(filePath + "\\" + name);
			
		OutputStream outputStream = response.getOutputStream();
		
		//다운로드할 파일과 연결된 입력스트림 통로 얻기
		//1바이트 단위씩 읽어들일 통로
		FileInputStream fileInputStream = new FileInputStream(f);
		
		/*응답 헤더를 통한 캐시제어*/
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		//웹브라우저에서 다운로드할 파일명 클릭시..
		//
		response.setHeader("Content-Disposition", "attachment; fileName=\""+URLEncoder.encode(name,"utf-8")+"\";");
		
		//입출력 작업
		//파일 전체 내용을 배열크기 단위로 읽어서 웹브라우저로 내보내기 ( 다운로드 시키기 )
		byte[] buffer = new byte[1024 * 8];
		
		while (true) {
			
			int cnt = fileInputStream.read(buffer);
			if (cnt == -1)
				break;
			outputStream.write(buffer, 0, cnt);
		}
		fileInputStream.close();
		outputStream.close();
		//파일 다운로드 로직 구현  끝 부분---------------------------------------------
	
		//다운로드 횟수 1 증가시키기
		fileboarddao.downUpdateCount(idx);
	
	}
	
	
	
	
	
	

}







