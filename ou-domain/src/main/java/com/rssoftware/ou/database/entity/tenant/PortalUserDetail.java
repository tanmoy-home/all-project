package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

//@Entity
//@Table(name = "USER_DETAILS")
//@NamedQuery(name = "PortalUserDetail.findAll", query = "SELECT a from PortalUserDetail a")
public class PortalUserDetail implements Serializable {
	
	public enum Usertype{ADMIN, AGENT,MAKER,CHAKER};

	private static final long serialVersionUID = -5385091010661697330L;

	@Id
	@Column(name = "USERID", nullable = false, length = 20)
	private String userId;

	@Column(name = "PASSWORD", nullable = false, length = 20)
	private String password;

	@Column(name = "PWD_EXPIRE", nullable = false, length = 20)
	private Date pwdExpire;

	@Column(name = "USERNAME", length = 20)
	private String name;

	@Column(name = "MOBILE", length = 10)
	private String mobile;

	@Column(name = "LASTMODIFIED_BY", length = 20)
	private String lastModifiedBy;

	@Column(name = "CREATED_BY", length = 20)
	private String createdBy;

	@Column(name = "APPROVED_BY", length = 20)
	private String approvedBy;

	@Column(name = "REGID", length = 20)
	private String regId;

	@Column(name = "STATUS", length = 20)
	private String status;
	
	@Column(name="EMAILID",length=50)
    private String emailId;
	
	
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@ManyToOne
	@JoinColumn(name = "USERROLE_ID")
	private PortalUserRole userRole;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
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

	public PortalUserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(PortalUserRole userRole) {
		this.userRole = userRole;
	}
}
