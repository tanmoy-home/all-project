package com.rssoftware.ou.model.tenant;

import java.sql.Timestamp;

public class SMSDetailsView {
	public enum SMSType {
		RECEIPT, COMPLAINT_RAISE, COMPLAINT_SETTLE,COMPLAINT_OTP,RESET_PASSWORD;
	}
	private long recordId;

	private String createdBy;

	private String smsMessage;

	private SMSType type;

	private String mobileNo;

	private Timestamp creationDate;

	private Timestamp deliveryDate;

	private char status;

	private Timestamp updtTs;

	private String updtBy;
	
	//Dont Change following 5 properties, Needed for SMS Service Only
	private String amount; 
	private String billerName;
	private String date;
	private String txnRefId;
	private String consumerNo;
	private String paymentChannel;
//	private String bankName;
//	private String branchName;
//	private String branchCode;	// IFSC code
//	private String accountNumber;
//	private String consumerNamne;
//	private String verificationCode;

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSmsMessage() {
		return smsMessage;
	}

	public void setSmsMessage(String smsMessage) {
		this.smsMessage = smsMessage;
	}

	public SMSType getType() {
		return type;
	}

	public void setType(SMSType type) {
		this.type = type;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getUpdtBy() {
		return updtBy;
	}

	public void setUpdtBy(String updtBy) {
		this.updtBy = updtBy;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTxnRefId() {
		return txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}

	public String getConsumerNo() {
		return consumerNo;
	}

	public void setConsumerNo(String consumerNo) {
		this.consumerNo = consumerNo;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

//	public String getBankName() {
//		return bankName;
//	}
//
//	public void setBankName(String bankName) {
//		this.bankName = bankName;
//	}
//
//	public String getBranchName() {
//		return branchName;
//	}
//
//	public void setBranchName(String branchName) {
//		this.branchName = branchName;
//	}
//
//	public String getBranchCode() {
//		return branchCode;
//	}
//
//	public void setBranchCode(String branchCode) {
//		this.branchCode = branchCode;
//	}
//
//	public String getAccountNumber() {
//		return accountNumber;
//	}
//
//	public void setAccountNumber(String accountNumber) {
//		this.accountNumber = accountNumber;
//	}
//
//	public String getConsumerNamne() {
//		return consumerNamne;
//	}
//
//	public void setConsumerNamne(String consumerNamne) {
//		this.consumerNamne = consumerNamne;
//	}
//
//	public String getVerificationCode() {
//		return verificationCode;
//	}
//
//	public void setVerificationCode(String verificationCode) {
//		this.verificationCode = verificationCode;
//	}	
	
}