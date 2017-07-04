package com.rssoftware.ou.batch.to;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BOUBlrRecon")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)

public class BOUBlrRecon implements Serializable{
	
	private static final long serialVersionUID = 2106312078100122851L;
	private String txnRefId;
	private String mobile;
	private byte[] blrRequestJson;
	private byte[] blrResponseJson;
	private String blrStatus;
	private String blrRevStatus;
	private String txnDate;
	private String billAmount;
	private String status;

	public String getTxnRefId() {
		return txnRefId;
	}
	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public byte[] getBlrRequestJson() {
		return blrRequestJson;
	}
	public void setBlrRequestJson(byte[] blrRequestJson) {
		this.blrRequestJson = blrRequestJson;
	}
	public byte[] getBlrResponseJson() {
		return blrResponseJson;
	}
	public void setBlrResponseJson(byte[] blrResponseJson) {
		this.blrResponseJson = blrResponseJson;
	}
	public String getBlrStatus() {
		return blrStatus;
	}
	public void setBlrStatus(String blrStatus) {
		this.blrStatus = blrStatus;
	}
	public String getBlrRevStatus() {
		return blrRevStatus;
	}
	public void setBlrRevStatus(String blrRevStatus) {
		this.blrRevStatus = blrRevStatus;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	
}
