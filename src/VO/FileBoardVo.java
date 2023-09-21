package VO;

import java.sql.Date;

//조회한 하나의 글정보를 저장할 용도
//수정할 하나의 글정보를 조회한후 저장할 용도
//DB에 추가한 새글정보를 임시로 저장할 용도
public class FileBoardVo {
	
	private int idx;//글번호
	private String b_id,b_pw,b_name,b_email,b_title,b_content;
	private int b_group,b_level;
	private Date b_date;
	private int b_cnt;
	
	private String ofile,sfile;
	private int downcount;
	
	



	public FileBoardVo() {}

	
	
	

	public FileBoardVo(int idx, String b_id, String b_pw, String b_name, String b_email, String b_title,
			String b_content, int b_group, int b_level, int b_cnt, String ofile, String sfile, int downcount) {
		this.idx = idx;
		this.b_id = b_id;
		this.b_pw = b_pw;
		this.b_name = b_name;
		this.b_email = b_email;
		this.b_title = b_title;
		this.b_content = b_content;
		this.b_group = b_group;
		this.b_level = b_level;
		this.b_cnt = b_cnt;
		this.ofile = ofile;
		this.sfile = sfile;
		this.downcount = downcount;
	}





	public FileBoardVo(int idx, String b_id, String b_pw, String b_name, String b_email, String b_title,
			String b_content, int b_group, int b_level, Date b_date, int b_cnt, String ofile, String sfile,
			int downcount) {
		this.idx = idx;
		this.b_id = b_id;
		this.b_pw = b_pw;
		this.b_name = b_name;
		this.b_email = b_email;
		this.b_title = b_title;
		this.b_content = b_content;
		this.b_group = b_group;
		this.b_level = b_level;
		this.b_date = b_date;
		this.b_cnt = b_cnt;
		this.ofile = ofile;
		this.sfile = sfile;
		this.downcount = downcount;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getB_id() {
		return b_id;
	}

	public void setB_id(String b_id) {
		this.b_id = b_id;
	}

	public String getB_pw() {
		return b_pw;
	}

	public void setB_pw(String b_pw) {
		this.b_pw = b_pw;
	}

	public String getB_name() {
		return b_name;
	}

	public void setB_name(String b_name) {
		this.b_name = b_name;
	}

	public String getB_email() {
		return b_email;
	}

	public void setB_email(String b_email) {
		this.b_email = b_email;
	}

	public String getB_title() {
		return b_title;
	}

	public void setB_title(String b_title) {
		this.b_title = b_title;
	}

	public String getB_content() {
		return b_content;
	}

	public void setB_content(String b_content) {
		this.b_content = b_content;
	}

	public int getB_group() {
		return b_group;
	}

	public void setB_group(int b_group) {
		this.b_group = b_group;
	}

	public int getB_level() {
		return b_level;
	}

	public void setB_level(int b_level) {
		this.b_level = b_level;
	}

	public Date getB_date() {
		return b_date;
	}

	public void setB_date(Date b_date) {
		this.b_date = b_date;
	}

	public int getB_cnt() {
		return b_cnt;
	}

	public void setB_cnt(int b_cnt) {
		this.b_cnt = b_cnt;
	}
	
	public String getOfile() {
		return ofile;
	}



	public void setOfile(String ofile) {
		this.ofile = ofile;
	}



	public String getSfile() {
		return sfile;
	}



	public void setSfile(String sfile) {
		this.sfile = sfile;
	}



	public int getDowncount() {
		return downcount;
	}



	public void setDowncount(int downcount) {
		this.downcount = downcount;
	}
	
}
