package com.example.flytoyou.srmanager.Bean;

import java.util.Date;

public class Res {
	private int resid;//维修单号
	private int userid;//学生id
	private String username;//学生姓名
	private String resbadmessage;//损坏描述
	private Date Time;//预约时间
	private int restype;//物品分类ID
	private String typeName;//物品分类名称
	private String resmessage;//学生跟进留言
	private String login;//维修进度
	
	public int getResid() {
		return resid;
	}
	public void setResid(int resid) {
		this.resid = resid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getResbadmessage() {
		return resbadmessage;
	}
	public void setResbadmessage(String resbadmessage) {
		this.resbadmessage = resbadmessage;
	}
	public Date getTime() {
		return Time;
	}
	public void setTime(Date time) {
		Time = time;
	}
	public int getRestype() {
		return restype;
	}
	public void setRestype(int restype) {
		this.restype = restype;
	}
	public String getResmessage() {
		return resmessage;
	}
	public void setResmessage(String resmessage) {
		this.resmessage = resmessage;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	
}
