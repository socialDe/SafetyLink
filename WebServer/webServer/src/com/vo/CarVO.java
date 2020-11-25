package com.vo;

public class CarVO {

	private int carid;
	private String userid;
	private String carnum;
	private String carname;
	private String cartype;
	private String carmodel;
	private int caryear;
	private String carimg;
	private String caroiltype;
	private String tablettoken;
	
	public CarVO() {
		super();
	}

	public CarVO(String carnum, String cartype, String carmodel, int caryear, String caroiltype) {
		super();
		this.carnum = carnum;
		this.cartype = cartype;
		this.carmodel = carmodel;
		this.caryear = caryear;
		this.caroiltype = caroiltype;
	}

	public CarVO(String carnum, String carname, String cartype, String carmodel, int caryear, String carimg,
			String caroiltype) {
		super();
		this.carnum = carnum;
		this.carname = carname;
		this.cartype = cartype;
		this.carmodel = carmodel;
		this.caryear = caryear;
		this.carimg = carimg;
		this.caroiltype = caroiltype;
	}

	public CarVO(String carnum, String cartype, String carmodel, int caryear, String carimg, String caroiltype,
			String tablettoken) {
		super();
		this.carnum = carnum;
		this.cartype = cartype;
		this.carmodel = carmodel;
		this.caryear = caryear;
		this.carimg = carimg;
		this.caroiltype = caroiltype;
		this.tablettoken = tablettoken;
	}

	public CarVO(String carnum, String carname, String cartype, String carmodel, int caryear, String caroiltype) {
		super();
		this.carnum = carnum;
		this.carname = carname;
		this.cartype = cartype;
		this.carmodel = carmodel;
		this.caryear = caryear;
		this.caroiltype = caroiltype;
	}

	public CarVO(String carnum, String cartype, String carmodel, int caryear, String caroiltype, String tablettoken) {
		super();
		this.carnum = carnum;
		this.cartype = cartype;
		this.carmodel = carmodel;
		this.caryear = caryear;
		this.caroiltype = caroiltype;
		this.tablettoken = tablettoken;
	}

	public CarVO(String userid, String carname, String cartype, String carmodel, int caryear, String carimg,
			String caroiltype, String tablettoken) {
		super();
		this.userid = userid;
		this.carname = carname;
		this.cartype = cartype;
		this.carmodel = carmodel;
		this.caryear = caryear;
		this.carimg = carimg;
		this.caroiltype = caroiltype;
		this.tablettoken = tablettoken;
	}

	public CarVO(int carid, String userid, String carnum, String carname, String cartype, String carmodel, int caryear,
			String carimg, String caroiltype, String tablettoken) {
		super();
		this.carid = carid;
		this.userid = userid;
		this.carnum = carnum;
		this.carname = carname;
		this.cartype = cartype;
		this.carmodel = carmodel;
		this.caryear = caryear;
		this.carimg = carimg;
		this.caroiltype = caroiltype;
		this.tablettoken = tablettoken;
	}

	public int getCarid() {
		return carid;
	}

	public void setCarid(int carid) {
		this.carid = carid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getCarnum() {
		return carnum;
	}

	public void setCarnum(String carnum) {
		this.carnum = carnum;
	}

	public String getCarname() {
		return carname;
	}

	public void setCarname(String carname) {
		this.carname = carname;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getCarmodel() {
		return carmodel;
	}

	public void setCarmodel(String carmodel) {
		this.carmodel = carmodel;
	}

	public int getCaryear() {
		return caryear;
	}

	public void setCaryear(int caryear) {
		this.caryear = caryear;
	}

	public String getCarimg() {
		return carimg;
	}

	public void setCarimg(String carimg) {
		this.carimg = carimg;
	}

	public String getCaroiltype() {
		return caroiltype;
	}

	public void setCaroiltype(String caroiltype) {
		this.caroiltype = caroiltype;
	}

	public String getTablettoken() {
		return tablettoken;
	}

	public void setTablettoken(String tablettoken) {
		this.tablettoken = tablettoken;
	}

	@Override
	public String toString() {
		return "CarVO [carid=" + carid + ", userid=" + userid + ", carnum=" + carnum + ", carname=" + carname
				+ ", cartype=" + cartype + ", carmodel=" + carmodel + ", caryear=" + caryear + ", carimg=" + carimg
				+ ", caroiltype=" + caroiltype + ", tablettoken=" + tablettoken + "]";
	}
	
	
}
