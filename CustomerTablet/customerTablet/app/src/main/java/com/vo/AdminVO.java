package com.vo;

public class AdminVO {

	private String adminid;
	private String adminpwd;
	private String adminname;
	private String adminphone;
	private String adminstate;
	
	public AdminVO() {
		super();
	}

	public AdminVO(String adminid, String adminpwd, String adminname, String adminphone) {
		super();
		this.adminid = adminid;
		this.adminpwd = adminpwd;
		this.adminname = adminname;
		this.adminphone = adminphone;
	}

	public AdminVO(String adminid, String adminpwd, String adminname, String adminphone, String adminstate) {
		super();
		this.adminid = adminid;
		this.adminpwd = adminpwd;
		this.adminname = adminname;
		this.adminphone = adminphone;
		this.adminstate = adminstate;
	}

	public String getAdminid() {
		return adminid;
	}

	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}

	public String getAdminpwd() {
		return adminpwd;
	}

	public void setAdminpwd(String adminpwd) {
		this.adminpwd = adminpwd;
	}

	public String getAdminname() {
		return adminname;
	}

	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}

	public String getAdminphone() {
		return adminphone;
	}

	public void setAdminphone(String adminphone) {
		this.adminphone = adminphone;
	}

	public String getAdminstate() {
		return adminstate;
	}

	public void setAdminstate(String adminstate) {
		this.adminstate = adminstate;
	}

	@Override
	public String toString() {
		return "AdminVO [adminid=" + adminid + ", adminpwd=" + adminpwd + ", adminname=" + adminname + ", adminphone="
				+ adminphone + ", adminstate=" + adminstate + "]";
	}
		
	
}
