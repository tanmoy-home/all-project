package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the CHANNEL_PARTNER_INFO database table.
 * 
 */
@Entity
@Table(name="CHANNEL_PARTNER_INFO")
@NamedQuery(name="ChannelPartnerInfo.findAll", query="SELECT c FROM ChannelPartnerInfo c")
public class ChannelPartnerInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(name="REF_ID")
	private String refId;

	@Column(name="PASS_KEY")
	private String passKey;

	@Column(name="AGENT_ID")
	private String agentId;
	
	@Column(name="PAYMENT_MODE")
	private String paymentMode;

	@Column(name="IMEI_CODE")
	private String imeiCode;
	
	
	
	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



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



	public ChannelPartnerInfo() {
	}


}