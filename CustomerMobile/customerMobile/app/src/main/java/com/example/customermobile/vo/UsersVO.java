package com.example.customermobile.vo;

import java.io.Serializable;
import java.util.Date;

public class UsersVO implements Serializable {
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
	private String sleeppushcheck;
	private String droppushcheck;
	private String mobiletoken;

	public UsersVO() {
		super();
	}

	public UsersVO(String userid, String userpwd) {
		super();
		this.userid = userid;
		this.userpwd = userpwd;
	}

	public UsersVO(String userid, String username, String userphone) {
		super();
		this.userid = userid;
		this.username = username;
		this.userphone = userphone;
	}

	public UsersVO(String userid, String usersubject, String babypushcheck, String accpushcheck, String sleeppushcheck, String droppushcheck) {
		this.userid = userid;
		this.usersubject = usersubject;
		this.babypushcheck = babypushcheck;
		this.accpushcheck = accpushcheck;
		this.sleeppushcheck = sleeppushcheck;
		this.droppushcheck = droppushcheck;
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



	public UsersVO(String userid, String userpwd, String username, String userphone, Date userbirth, String usersex,
				   String mobiletoken) {
		super();
		this.userid = userid;
		this.userpwd = userpwd;
		this.username = username;
		this.userphone = userphone;
		this.userbirth = userbirth;
		this.usersex = usersex;
		this.mobiletoken = mobiletoken;
	}

	public UsersVO(String userid, String userpwd, String username, String userphone, Date userbirth, String usersex,
				   Date userregdate, String userstate, String usersubject, String babypushcheck, String accpushcheck, String mobiletoken) {
		this.userid = userid;
		this.userpwd = userpwd;
		this.username = username;
		this.userphone = userphone;
		this.userbirth = userbirth;
		this.usersex = usersex;
		this.userregdate = userregdate;
		this.userstate = userstate;
		this.usersubject = usersubject;
		this.babypushcheck = babypushcheck;
		this.accpushcheck = accpushcheck;
		this.mobiletoken = mobiletoken;
	}

	public UsersVO(String userid, String userpwd, String username, String userphone, Date userbirth, String usersex, Date userregdate, String userstate, String usersubject, String babypushcheck, String accpushcheck, String sleeppushcheck, String droppushcheck, String mobiletoken) {
		this.userid = userid;
		this.userpwd = userpwd;
		this.username = username;
		this.userphone = userphone;
		this.userbirth = userbirth;
		this.usersex = usersex;
		this.userregdate = userregdate;
		this.userstate = userstate;
		this.usersubject = usersubject;
		this.babypushcheck = babypushcheck;
		this.accpushcheck = accpushcheck;
		this.sleeppushcheck = sleeppushcheck;
		this.droppushcheck = droppushcheck;
		this.mobiletoken = mobiletoken;
	}

	public String getSleeppushcheck() {
		return sleeppushcheck;
	}

	public void setSleeppushcheck(String sleeppushcheck) {
		this.sleeppushcheck = sleeppushcheck;
	}

	public String getDroppushcheck() {
		return droppushcheck;
	}

	public void setDroppushcheck(String droppushcheck) {
		this.droppushcheck = droppushcheck;
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
		return "UsersVO{" +
				"userid='" + userid + '\'' +
				", userpwd='" + userpwd + '\'' +
				", username='" + username + '\'' +
				", userphone='" + userphone + '\'' +
				", userbirth=" + userbirth +
				", usersex='" + usersex + '\'' +
				", userregdate=" + userregdate +
				", userstate='" + userstate + '\'' +
				", usersubject='" + usersubject + '\'' +
				", babypushcheck='" + babypushcheck + '\'' +
				", accpushcheck='" + accpushcheck + '\'' +
				", sleeppushcheck='" + sleeppushcheck + '\'' +
				", droppushcheck='" + droppushcheck + '\'' +
				", mobiletoken='" + mobiletoken + '\'' +
				'}';
	}
}
