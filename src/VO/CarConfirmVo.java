package VO;

//2개의 테이블(carorder,carlist)을 연결(JOIN)하여
//조회된 결과 한행의 정보를 저장할 용도의 VO
public class CarConfirmVo {
	

	//carlist테이블에서 조회된 정보들을 저장할 변수
	private String carname, carimg;
	private int carprice;
	
	private int orderid, carreserveday, carqty,
				carins, carwifi, carnave, carbabyseat;
	
	private String carbegindate;

	public String getCarname() {
		return carname;
	}

	public void setCarname(String carname) {
		this.carname = carname;
	}

	public String getCarimg() {
		return carimg;
	}

	public void setCarimg(String carimg) {
		this.carimg = carimg;
	}

	public int getCarprice() {
		return carprice;
	}

	public void setCarprice(int carprice) {
		this.carprice = carprice;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public int getCarreserveday() {
		return carreserveday;
	}

	public void setCarreserveday(int carreserveday) {
		this.carreserveday = carreserveday;
	}

	public int getCarqty() {
		return carqty;
	}

	public void setCarqty(int carqty) {
		this.carqty = carqty;
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

	public String getCarbegindate() {
		return carbegindate;
	}

	public void setCarbegindate(String carbegindate) {
		this.carbegindate = carbegindate;
	}
	
	
	
	
}
