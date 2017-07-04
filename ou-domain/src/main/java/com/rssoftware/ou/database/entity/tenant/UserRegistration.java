package com.rssoftware.ou.database.entity.tenant;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="user_details")
public class UserRegistration {

	@Id
	@Column(name="userid")
	private String name;
	
	@Column(name="contactid")
	private String email;
	
	@Transient
	private String confEmail;
	
	@Column(name="password")
	private String tempPwd;
	
	@Column(name="userrole_id")
	private String role;
	/*
	@Column(name="userroleid")
	private String roleId;*/
	
	@Column(name="pwd_expire")
	private Date pwd_expire;
	
	@Column(name="lastchecked_by")
	private String lastchecked_by;
	
	@Column(name="lastmodified_by")
	private String lastmodified_by;
	
	@Column(name="created_by")
	private String created_by;
	
	@Column(name="approved_by")
	private String approved_by;
	
	@Column(name="status")
	private String status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getConfEmail() {
		return confEmail;
	}
	public void setConfEmail(String confEmail) {
		this.confEmail = confEmail;
	}
	public String getTempPwd() {
		return tempPwd;
	}
	public void setTempPwd(String tempPwd) {
		this.tempPwd = tempPwd;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Date getPwd_expire() {
		return pwd_expire;
	}
	public void setPwd_expire(Date pwd_expire) {
		this.pwd_expire = pwd_expire;
	}
	public String getLastchecked_by() {
		return lastchecked_by;
	}
	public void setLastchecked_by(String lastchecked_by) {
		this.lastchecked_by = lastchecked_by;
	}
	public String getLastmodified_by() {
		return lastmodified_by;
	}
	public void setLastmodified_by(String lastmodified_by) {
		this.lastmodified_by = lastmodified_by;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getApproved_by() {
		return approved_by;
	}
	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
