package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "OTP_CONFIG")
public class OtpConfig implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SeqGen")
	@SequenceGenerator(name = "SeqGen", sequenceName = "OTP_CONFIG_OTP_ID_SEQ", allocationSize = 1)
	@Column(name = "OTP_ID")
	protected Long otpId;

	@Column(name = "MOBILE_NO")
	protected String mobileNo;

	@Column(name = "OTP_NO")
	protected String otpNo;

	@Column(name = "CREATED_TS")
	protected java.sql.Timestamp creationTs;

	public Long getOtpId() {
		return otpId;
	}

	public void setOtpId(Long otpId) {
		this.otpId = otpId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOtpNo() {
		return otpNo;
	}

	public void setOtpNo(String otpNo) {
		this.otpNo = otpNo;
	}

	public java.sql.Timestamp getCreationTs() {
		return creationTs;
	}

	public void setCreationTs(java.sql.Timestamp creationTs) {
		this.creationTs = creationTs;
	}

}
