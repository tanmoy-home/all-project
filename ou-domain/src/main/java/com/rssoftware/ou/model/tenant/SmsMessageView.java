package com.rssoftware.ou.model.tenant;

import java.util.Map;

import com.rssoftware.ou.model.tenant.SMSDetailsView.SMSType;

public class SmsMessageView {
	public enum SMSType {
		RECEIPT, COMPLAINT_RAISE, COMPLAINT_SETTLE,COMPLAINT_OTP;
	}
	private String tenantId;
	
	private String sendType;
	
	private String baseUrl;
	
	private String mobNo;
	
	private Map<String, String> message;
	
	private SMSType type;


	public SMSType getType() {
		return type;
	}

	public void setType(SMSType type) {
		this.type = type;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public Map<String, String> getMessage() {
		return message;
	}

	public void setMessage(Map<String, String> message) {
		this.message = message;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	
	

}
