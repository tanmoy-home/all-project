package com.rssoftware.ou.domain;

import java.sql.Timestamp;
import java.util.List;


public class Complaint{
	
	private String complaintId;
	private String complaintSrc="CU";
	private String onUSFlg="N";

	private String txnRefSrcOU;
	private String txnSelected;
	private String complaintTypeCd;
	
	private String msgId;
	
	private String transactionId;
	private String transactionType;
	private String transactionDescription;
	private String transactionDate;
	private String transactionAmount;
	private String transactionStatus;
	private String transactionPaymentMode;

	private String serviceReasonCd;
	private String serviceDescription;
	
	private String complaintStatus;
	private String complaintStatusView;
	private String requestStatus;

	private String agentBuzId;
	private String agentName;
	private String agentMobile;
	private String agentAddress;
	
	private String billerBuzId;
	private String billerRef;
	private String billerName;
	
	private String customerName;
	private String customerMobile;
	private String customerEmail;

	private String mobNo;
	private String category;
	
	private String disposition;
	private String subDisposition;
	
	private String custOUInst;
	private String billOUInst;
	private String asgnOUInst;
	
	private String complaintDescription;
	private String statusChgRemark;
	
	private String crtnUserId;
	private String loggedInOUInst;
	
	private String complaintDate;
	private String estimatedTAT;
	
	private int dispositionTat;
	private int couIniTat;
	private int bouIniTat;
	private int couEscTat1;
	private int couEscTat2;
	private int couEscTat3;
	private int bouEscTat1;
	private int bouEscTat2;
	private int bouEscTat3;
	
	private String couIniTatDt;
	private String bouIniTatDt;
	private String couEscTat1Dt;
	private String couEscTat2Dt;
	private String couEscTat3Dt;
	private String bouEscTat1Dt;
	private String bouEscTat2Dt;
	private String bouEscTat3Dt;
	
	private boolean isEsc2DateOver = false;
	private boolean isEsc3DateOver = false;
	
	private String lstUpdatedTime;
	private String isComplaintExists;
	
	List<ComplaintStatus> statChgHistoryList;

	private int totalRcdCnt=0;
	
	private int escLevel=0;
	
	private Timestamp lstUpdatedTimeTs;
	
	private boolean isAddInfoReqAlreadyRaised = false;

	public String getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}

	public String getComplaintSrc() {
		return complaintSrc;
	}

	public void setComplaintSrc(String complaintSrc) {
		this.complaintSrc = complaintSrc;
	}

	public String getOnUSFlg() {
		return onUSFlg;
	}

	public void setOnUSFlg(String onUSFlg) {
		this.onUSFlg = onUSFlg;
	}

	public String getTxnRefSrcOU() {
		return txnRefSrcOU;
	}

	public void setTxnRefSrcOU(String txnRefSrcOU) {
		this.txnRefSrcOU = txnRefSrcOU;
	}

	public String getTxnSelected() {
		return txnSelected;
	}

	public void setTxnSelected(String txnSelected) {
		this.txnSelected = txnSelected;
	}

	public String getComplaintTypeCd() {
		return complaintTypeCd;
	}

	public void setComplaintTypeCd(String complaintTypeCd) {
		this.complaintTypeCd = complaintTypeCd;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionPaymentMode() {
		return transactionPaymentMode;
	}

	public void setTransactionPaymentMode(String transactionPaymentMode) {
		this.transactionPaymentMode = transactionPaymentMode;
	}

	public String getServiceReasonCd() {
		return serviceReasonCd;
	}

	public void setServiceReasonCd(String serviceReasonCd) {
		this.serviceReasonCd = serviceReasonCd;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getComplaintStatus() {
		return complaintStatus;
	}

	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public String getComplaintStatusView() {
		return complaintStatusView;
	}

	public void setComplaintStatusView(String complaintStatusView) {
		this.complaintStatusView = complaintStatusView;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getAgentBuzId() {
		return agentBuzId;
	}

	public void setAgentBuzId(String agentBuzId) {
		this.agentBuzId = agentBuzId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentMobile() {
		return agentMobile;
	}

	public void setAgentMobile(String agentMobile) {
		this.agentMobile = agentMobile;
	}

	public String getAgentAddress() {
		return agentAddress;
	}

	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}

	public String getBillerBuzId() {
		return billerBuzId;
	}

	public void setBillerBuzId(String billerBuzId) {
		this.billerBuzId = billerBuzId;
	}

	public String getBillerRef() {
		return billerRef;
	}

	public void setBillerRef(String billerRef) {
		this.billerRef = billerRef;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getSubDisposition() {
		return subDisposition;
	}

	public void setSubDisposition(String subDisposition) {
		this.subDisposition = subDisposition;
	}

	public String getCustOUInst() {
		return custOUInst;
	}

	public void setCustOUInst(String custOUInst) {
		this.custOUInst = custOUInst;
	}

	public String getBillOUInst() {
		return billOUInst;
	}

	public void setBillOUInst(String billOUInst) {
		this.billOUInst = billOUInst;
	}

	public String getAsgnOUInst() {
		return asgnOUInst;
	}

	public void setAsgnOUInst(String asgnOUInst) {
		this.asgnOUInst = asgnOUInst;
	}

	public String getComplaintDescription() {
		return complaintDescription;
	}

	public void setComplaintDescription(String complaintDescription) {
		this.complaintDescription = complaintDescription;
	}

	public String getStatusChgRemark() {
		return statusChgRemark;
	}

	public void setStatusChgRemark(String statusChgRemark) {
		this.statusChgRemark = statusChgRemark;
	}

	public String getCrtnUserId() {
		return crtnUserId;
	}

	public void setCrtnUserId(String crtnUserId) {
		this.crtnUserId = crtnUserId;
	}

	public String getLoggedInOUInst() {
		return loggedInOUInst;
	}

	public void setLoggedInOUInst(String loggedInOUInst) {
		this.loggedInOUInst = loggedInOUInst;
	}

	public String getComplaintDate() {
		return complaintDate;
	}

	public void setComplaintDate(String complaintDate) {
		this.complaintDate = complaintDate;
	}

	public String getEstimatedTAT() {
		return estimatedTAT;
	}

	public void setEstimatedTAT(String estimatedTAT) {
		this.estimatedTAT = estimatedTAT;
	}

	public int getDispositionTat() {
		return dispositionTat;
	}

	public void setDispositionTat(int dispositionTat) {
		this.dispositionTat = dispositionTat;
	}

	public int getCouIniTat() {
		return couIniTat;
	}

	public void setCouIniTat(int couIniTat) {
		this.couIniTat = couIniTat;
	}

	public int getBouIniTat() {
		return bouIniTat;
	}

	public void setBouIniTat(int bouIniTat) {
		this.bouIniTat = bouIniTat;
	}

	public int getCouEscTat1() {
		return couEscTat1;
	}

	public void setCouEscTat1(int couEscTat1) {
		this.couEscTat1 = couEscTat1;
	}

	public int getCouEscTat2() {
		return couEscTat2;
	}

	public void setCouEscTat2(int couEscTat2) {
		this.couEscTat2 = couEscTat2;
	}

	public int getCouEscTat3() {
		return couEscTat3;
	}

	public void setCouEscTat3(int couEscTat3) {
		this.couEscTat3 = couEscTat3;
	}

	public int getBouEscTat1() {
		return bouEscTat1;
	}

	public void setBouEscTat1(int bouEscTat1) {
		this.bouEscTat1 = bouEscTat1;
	}

	public int getBouEscTat2() {
		return bouEscTat2;
	}

	public void setBouEscTat2(int bouEscTat2) {
		this.bouEscTat2 = bouEscTat2;
	}

	public int getBouEscTat3() {
		return bouEscTat3;
	}

	public void setBouEscTat3(int bouEscTat3) {
		this.bouEscTat3 = bouEscTat3;
	}

	public String getCouIniTatDt() {
		return couIniTatDt;
	}

	public void setCouIniTatDt(String couIniTatDt) {
		this.couIniTatDt = couIniTatDt;
	}

	public String getBouIniTatDt() {
		return bouIniTatDt;
	}

	public void setBouIniTatDt(String bouIniTatDt) {
		this.bouIniTatDt = bouIniTatDt;
	}

	public String getCouEscTat1Dt() {
		return couEscTat1Dt;
	}

	public void setCouEscTat1Dt(String couEscTat1Dt) {
		this.couEscTat1Dt = couEscTat1Dt;
	}

	public String getCouEscTat2Dt() {
		return couEscTat2Dt;
	}

	public void setCouEscTat2Dt(String couEscTat2Dt) {
		this.couEscTat2Dt = couEscTat2Dt;
	}

	public String getCouEscTat3Dt() {
		return couEscTat3Dt;
	}

	public void setCouEscTat3Dt(String couEscTat3Dt) {
		this.couEscTat3Dt = couEscTat3Dt;
	}

	public String getBouEscTat1Dt() {
		return bouEscTat1Dt;
	}

	public void setBouEscTat1Dt(String bouEscTat1Dt) {
		this.bouEscTat1Dt = bouEscTat1Dt;
	}

	public String getBouEscTat2Dt() {
		return bouEscTat2Dt;
	}

	public void setBouEscTat2Dt(String bouEscTat2Dt) {
		this.bouEscTat2Dt = bouEscTat2Dt;
	}

	public String getBouEscTat3Dt() {
		return bouEscTat3Dt;
	}

	public void setBouEscTat3Dt(String bouEscTat3Dt) {
		this.bouEscTat3Dt = bouEscTat3Dt;
	}

	public boolean isEsc2DateOver() {
		return isEsc2DateOver;
	}

	public void setEsc2DateOver(boolean isEsc2DateOver) {
		this.isEsc2DateOver = isEsc2DateOver;
	}

	public boolean isEsc3DateOver() {
		return isEsc3DateOver;
	}

	public void setEsc3DateOver(boolean isEsc3DateOver) {
		this.isEsc3DateOver = isEsc3DateOver;
	}

	public String getLstUpdatedTime() {
		return lstUpdatedTime;
	}

	public void setLstUpdatedTime(String lstUpdatedTime) {
		this.lstUpdatedTime = lstUpdatedTime;
	}

	public String getIsComplaintExists() {
		return isComplaintExists;
	}

	public void setIsComplaintExists(String isComplaintExists) {
		this.isComplaintExists = isComplaintExists;
	}

	public List<ComplaintStatus> getStatChgHistoryList() {
		return statChgHistoryList;
	}

	public void setStatChgHistoryList(List<ComplaintStatus> statChgHistoryList) {
		this.statChgHistoryList = statChgHistoryList;
	}

	public int getTotalRcdCnt() {
		return totalRcdCnt;
	}

	public void setTotalRcdCnt(int totalRcdCnt) {
		this.totalRcdCnt = totalRcdCnt;
	}

	public int getEscLevel() {
		return escLevel;
	}

	public void setEscLevel(int escLevel) {
		this.escLevel = escLevel;
	}

	public Timestamp getLstUpdatedTimeTs() {
		return lstUpdatedTimeTs;
	}

	public void setLstUpdatedTimeTs(Timestamp lstUpdatedTimeTs) {
		this.lstUpdatedTimeTs = lstUpdatedTimeTs;
	}

	public boolean isAddInfoReqAlreadyRaised() {
		return isAddInfoReqAlreadyRaised;
	}

	public void setAddInfoReqAlreadyRaised(boolean isAddInfoReqAlreadyRaised) {
		this.isAddInfoReqAlreadyRaised = isAddInfoReqAlreadyRaised;
	}
}
