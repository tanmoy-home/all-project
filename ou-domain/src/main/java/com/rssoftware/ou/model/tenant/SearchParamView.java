package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

public class SearchParamView implements Serializable{
	
	private String mobNo;
	private String customerMobile;
	private String transactionId;
	private String ouID;
	private String agentId;
	private String billerId;
	private String paymentMode;
	private String paymentChannel;
	private String fromDate;
	private String toDate;
	
	private String complaintId;
	private String disputeId;
	
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public String getCustomerMobile() {
		return customerMobile;
	}
	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getOuID() {
		return ouID;
	}
	public void setOuID(String ouID) {
		this.ouID = ouID;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	public String getDisputeId() {
		return disputeId;
	}
	public void setDisputeId(String disputeId) {
		this.disputeId = disputeId;
	}
}
