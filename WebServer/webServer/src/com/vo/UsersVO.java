package com.vo;

import java.util.Date;

public class UsersVO {
	private String userid;
	private String userpwd;
	private String username;
	private String userphone;
	private Date userbirth;
	private String usersex;
	private Date userregdate;
	private String userstate;
	private String usersubject;
	private String babypushcheck;
	private String accpushcheck;
	private String mobiletoken;
	
	public UsersVO() {
		super();
	}

	public UsersVO(String userid, String username, String userphone) {
		super();
		this.userid = userid;
		this.username = username;
		this.userphone = userphone;
	}

	public UsersVO(String username, String userphone) {
		super();
		this.username = username;
		this.userphone = userphone;
	}

	public UsersVO(String userid, String userpwd, String username, String userphone, Date userbirth, String usersex) {
		super();
		this.userid = userid;
		this.userpwd = userpwd;
		this.username = username;
		this.userphone = userphone;
		this.userbirth = userbirth;
		this.usersex = usersex;
	}

	public UsersVO(String userid, String userpwd, String username, String userphone, Date userbirth, String usersex,
			Date userregdate) {
		super();
		this.userid = userid;
		this.userpwd = userpwd;
		this.username = username;
		this.userphone = userphone;
		this.userbirth = userbirth;
		this.usersex = usersex;
		this.userregdate = userregdate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public Date getUserbirth() {
		return userbirth;
	}

	public void setUserbirth(Date userbirth) {
		this.userbirth = userbirth;
	}

	public String getUsersex() {
		return usersex;
	}

	public void setUsersex(String usersex) {
		this.usersex = usersex;
	}

	public Date getUserregdate() {
		return userregdate;
	}

	public void setUserregdate(Date userregdate) {
		this.userregdate = userregdate;
	}

	public String getUserstate() {
		return userstate;
	}

	public void setUserstate(String userstate) {
		this.userstate = userstate;
	}

	public String getUsersubject() {
		return usersubject;
	}

	public void setUsersubject(String usersubject) {
		this.usersubject = usersubject;
	}

	public String getBabypushcheck() {
		return babypushcheck;
	}

	public void setBabypushcheck(String babypushcheck) {
		this.babypushcheck = babypushcheck;
	}

	public String getAccpushcheck() {
		return accpushcheck;
	}

	public void setAccpushcheck(String accpushcheck) {
		this.accpushcheck = accpushcheck;
	}

	public String getMobiletoken() {
		return mobiletoken;
	}

	public void setMobiletoken(String mobiletoken) {
		this.mobiletoken = mobiletoken;
	}

	@Override
	public String toString() {
		return "UsersVO [userid=" + userid + ", userpwd=" + userpwd + ", username=" + username + ", userphone="
				+ userphone + ", userbirth=" + userbirth + ", usersex=" + usersex + ", userregdate=" + userregdate
				+ ", userstate=" + userstate + ", usersubject=" + usersubject + ", babypushcheck=" + babypushcheck
				+ ", accpushcheck=" + accpushcheck + ", mobiletoken=" + mobiletoken + "]";
	}

	
}
