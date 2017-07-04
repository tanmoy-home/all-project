package com.rssoftware.ou.database.entity.tenant;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "SMS_TO_DB")
@NamedQuery(name = "DBSms.findAll", query = "SELECT a FROM DBSms a")
public class DBSms {
	
	@Id
	@Column(name = "SMS_ID")
	private String smsId;

	@Column(name = "CONFIG_ID")
	private String configId;

	@Column(name = "SMS_DATA")
	private String message;

	@Column(name = "CRTN_TS")
	private Timestamp creationDt;

	@Column(name = "MOB_NO")
	private String mobileNum;
	
	@Column(name="SMS_TYPE")
	private String smsType;
	

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getCreationDt() {
		return creationDt;
	}

	public void setCreationDt(Timestamp creationDt) {
		this.creationDt = creationDt;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	

}
