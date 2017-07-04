package com.rssoftware.ou.batch.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FinRecon")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)

public class FinRecon implements Serializable{
	
	private static final long serialVersionUID = 2106312078100122851L;
	private String txnRefId;
	private String authCode;
	private Timestamp crtnTs;
	private String finCurrentStatus;
	private byte[] requestJson;
	private Timestamp updtTs;
	private String txnCurrentStatus;
	private Timestamp txnTime;
	private String status;
	private BigDecimal amount;
	private BigDecimal totalAmount;
	
	@XmlAttribute(name = "txnRefId")
	public String getTxnRefId() {
		return txnRefId;
	}
	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	@XmlAttribute(name = "authCode")
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public Timestamp getCrtnTs() {
		return crtnTs;
	}
	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}
	public String getFinCurrentStatus() {
		return finCurrentStatus;
	}
	public void setFinCurrentStatus(String finCurrentStatus) {
		this.finCurrentStatus = finCurrentStatus;
	}
	public byte[] getRequestJson() {
		return requestJson;
	}
	public void setRequestJson(byte[] requestJson) {
		this.requestJson = requestJson;
	}
	public Timestamp getUpdtTs() {
		return updtTs;
	}
	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}
	public String getTxnCurrentStatus() {
		return txnCurrentStatus;
	}
	public void setTxnCurrentStatus(String txnCurrentStatus) {
		this.txnCurrentStatus = txnCurrentStatus;
	}
	//@XmlAttribute(name = "txnTime")
	public Timestamp getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(Timestamp txnTime) {
		this.txnTime = txnTime;
	}
	
	@XmlAttribute(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	@XmlAttribute(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
