package VO;

//렌트 예약 정보를 저장하는 Vo클래스 
public class CarOrderVo {
	
	private int orderid; //렌트 예약 주문 id
	
	private String id;
	//차번호를 이용해 2개의 테이블(carlist테이블,carorder테이블)의 정보를
	//조인(연결)하여정보를 검색해서 가져 오기 위해 렌트 예약 차번호가 저장되어야 합니다.
	private int carno; //렌트 예약 차번호

	private int carqty; //렌트 예약 대여수량
	private int carreserveday; //렌트 예약 대여기간
	private String carbegindate; //렌트예약 대여시작날짜
	
	private int carins; //보험적용여부
	private int carwifi; //와이파이적용여부
	private int carnave; //네비게이션적용여부
	private int carbabyseat; //베이비시트 적용여부
	
	private String memberphone; //비회원으로 예약시 입력한 폰 번호 저장
	private String memberpass; //비회원으로 예약시 입력한 비밀번호 저장
	
	
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getCarno() {
		return carno;
	}
	public void setCarno(int carno) {
		this.carno = carno;
	}
	public int getCarqty() {
		return carqty;
	}
	public void setCarqty(int carqty) {
		this.carqty = carqty;
	}
	public int getCarreserveday() {
		return carreserveday;
	}
	public void setCarreserveday(int carreserveday) {
		this.carreserveday = carreserveday;
	}
	public String getCarbegindate() {
		return carbegindate;
	}
	public void setCarbegindate(String carbegindate) {
		this.carbegindate = carbegindate;
	}
	public int getCarins() {
		return carins;
	}
	public void setCarins(int carins) {
		this.carins = carins;
	}
	public int getCarwifi() {
		return carwifi;
	}
	public void setCarwifi(int carwifi) {
		this.carwifi = carwifi;
	}
	public int getCarnave() {
		return carnave;
	}
	public void setCarnave(int carnave) {
		this.carnave = carnave;
	}
	public int getCarbabyseat() {
		return carbabyseat;
	}
	public void setCarbabyseat(int carbabyseat) {
		this.carbabyseat = carbabyseat;
	}
	public String getMemberphone() {
		return memberphone;
	}
	public void setMemberphone(String memberphone) {
		this.memberphone = memberphone;
	}
	public String getMemberpass() {
		return memberpass;
	}
	public void setMemberpass(String memberpass) {
		this.memberpass = memberpass;
	}
	
	
	
}
