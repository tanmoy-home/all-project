package com.rssoftware.ou.domain;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserDetail extends User {

	private static final long serialVersionUID = 1L;
	
	private Date pwdExpire;

	private String name;

	private String mobile;

	private String regId;

	private String status;

	private String role;
	
	private String tenantId;

	
	public UserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities,
			Date pwdExpire, String name, String mobile, String regId, String status, String role, String tenantId) {
		super(username, password, authorities);
		this.pwdExpire = pwdExpire;
		this.name = name;
		this.mobile = mobile;
		this.regId = regId;
		this.status = status;
		this.role = role;
		this.tenantId = tenantId;
	}
	
	public Date getPwdExpire() {
		return pwdExpire;
	}

	public void setPwdExpire(Date pwdExpire) {
		this.pwdExpire = pwdExpire;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
}
