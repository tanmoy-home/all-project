package com.rssoftware.ou.database.entity.tenant;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "SMS_LOG_DETAILS")
@NamedQuery(name = "SmsLogDetials.findAll", query = "SELECT a FROM SmsLogDetials a")
public class SmsLogDetials {
	
	@Id
	@Column(name = "LOG_ID")
	private String logId;

	@Column(name = "CONFIG_ID")
	private String configId;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "SMS_URL")
	private String smsUrl;

	@Column(name = "SMS_DATA")
	private String data;

	@Column(name = "CRTN_TS")
	private Timestamp creationDt ;

	@Column(name = "MOB_NO")
	private String mobileNum ;
	
	@Column(name="SMS_TYPE")
	private String smsType;
	
	

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
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
