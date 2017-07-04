package com.rssoftware.ou.domain;

import java.io.Serializable;

public class ChannelPartnerInfoDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5174824925271007365L;

	private String refId;
	private String passKey;
	private String agentId;
	private String paymentMode;
	private String imeiCode;
	
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getPassKey() {
		return passKey;
	}
	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getImeiCode() {
		return imeiCode;
	}
	public void setImeiCode(String imeiCode) {
		this.imeiCode = imeiCode;
	}
	
}
