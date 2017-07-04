package com.rssoftware.ou.model.tenant;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;

import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;

public class TransactionDataView {
	public enum ReconStatus {
		UNREAD, MATCHED, NO_MATCHES_FOUND, NON_MATCHING_FIELDS, PENDING;
	}

	private String refId;

	private RequestType requestType;
	private String messageId;
	private BillFetchRequest billFetchRequest;
	private BillFetchResponse billFetchResponse;
	private BillPaymentRequest billPaymentRequest;
	private BillPaymentResponse billPaymentResponse;
	private Timestamp creationTimestamp;
	private Timestamp lastUpdateTimestamp;
	
	private String reconDescription;
	private String txnRefId;
	private ReconStatus reconStatus;
	private Timestamp reconTs;
	private BigDecimal reconCycleNo;

	private String AgentChannelID;
	private String AgentChannelCustID;
	private String AgentChannelTxnID;
	private String Tags;

	private String agentID;
	private String mobile;
	private String dateOfTxn;
	private String billerID;

	private String billDate;
	private BigDecimal billAmount;
	private String transactionType;
	private String currentNodeAddress;
	private RequestStatus status;
	private Boolean isOnUs;
	
	//required for biller-ou transaction
	private RequestStatus cuStatus;
	private RequestStatus cuRevStatus;
	private RequestStatus blrStatus;
	private RequestStatus blrRevStatus;
	private BillFetchRequest blrFetchRequestJson;
	private BillFetchResponse blrFetchResponseJson;
	private BillPaymentRequest blrPayRequestJson;
	private BillPaymentResponse blrPayResponseJson;
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getAgentChannelID() {
		return AgentChannelID;
	}

	public void setAgentChannelID(String agentChannelID) {
		AgentChannelID = agentChannelID;
	}

	public String getAgentChannelCustID() {
		return AgentChannelCustID;
	}

	public void setAgentChannelCustID(String agentChannelCustID) {
		AgentChannelCustID = agentChannelCustID;
	}

	public String getAgentChannelTxnID() {
		return AgentChannelTxnID;
	}

	public void setAgentChannelTxnID(String agentChannelTxnID) {
		AgentChannelTxnID = agentChannelTxnID;
	}

	public String getTags() {
		return Tags;
	}

	public void setTags(String tags) {
		Tags = tags;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public BillFetchRequest getBillFetchRequest() {
		return billFetchRequest;
	}

	public void setBillFetchRequest(BillFetchRequest billFetchRequest) {
		this.billFetchRequest = billFetchRequest;
	}

	public BillFetchResponse getBillFetchResponse() {
		return billFetchResponse;
	}

	public void setBillFetchResponse(BillFetchResponse billFetchResponse) {
		this.billFetchResponse = billFetchResponse;
	}

	public BillPaymentRequest getBillPaymentRequest() {
		return billPaymentRequest;
	}

	public void setBillPaymentRequest(BillPaymentRequest billPaymentRequest) {
		this.billPaymentRequest = billPaymentRequest;
	}

	public BillPaymentResponse getBillPaymentResponse() {
		return billPaymentResponse;
	}

	public void setBillPaymentResponse(BillPaymentResponse billPaymentResponse) {
		this.billPaymentResponse = billPaymentResponse;
	}

	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public RequestStatus getCuStatus() {
		return cuStatus;
	}

	public void setCuStatus(RequestStatus cuStatus) {
		this.cuStatus = cuStatus;
	}

	public RequestStatus getCuRevStatus() {
		return cuRevStatus;
	}

	public void setCuRevStatus(RequestStatus cuRevStatus) {
		this.cuRevStatus = cuRevStatus;
	}

	public RequestStatus getBlrStatus() {
		return blrStatus;
	}

	public void setBlrStatus(RequestStatus blrStatus) {
		this.blrStatus = blrStatus;
	}

	public RequestStatus getBlrRevStatus() {
		return blrRevStatus;
	}

	public void setBlrRevStatus(RequestStatus blrRevStatus) {
		this.blrRevStatus = blrRevStatus;
	}

	public String getTxnRefId() {
		return txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}

	public ReconStatus getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(ReconStatus reconStatus) {
		this.reconStatus = reconStatus;
	}

	public Timestamp getReconTs() {
		return reconTs;
	}

	public void setReconTs(Timestamp reconTs) {
		this.reconTs = reconTs;
	}

	public String getReconDescription() {
		return reconDescription;
	}

	public void setReconDescription(String reconDescription) {
		this.reconDescription = reconDescription;
	}

	public BigDecimal getReconCycleNo() {
		return reconCycleNo;
	}

	public void setReconCycleNo(BigDecimal reconCycleNo) {
		this.reconCycleNo = reconCycleNo;
	}

	public String getCurrentNodeAddress() {
		return currentNodeAddress;
	}

	public void setCurrentNodeAddress(String currentNodeAddress) {
		this.currentNodeAddress = currentNodeAddress;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDateOfTxn() {
		return dateOfTxn;
	}

	public void setDateOfTxn(String dateOfTxn) {
		this.dateOfTxn = dateOfTxn;
	}

	public String getBillerID() {
		return billerID;
	}

	public void setBillerID(String billerID) {
		this.billerID = billerID;
	}

	public BillFetchRequest getBlrFetchRequestJson() {
		return blrFetchRequestJson;
	}

	public void setBlrFetchRequestJson(BillFetchRequest blrFetchRequestJson) {
		this.blrFetchRequestJson = blrFetchRequestJson;
	}

	public BillFetchResponse getBlrFetchResponseJson() {
		return blrFetchResponseJson;
	}

	public void setBlrFetchResponseJson(BillFetchResponse blrFetchResponseJson) {
		this.blrFetchResponseJson = blrFetchResponseJson;
	}

	public BillPaymentRequest getBlrPayRequestJson() {
		return blrPayRequestJson;
	}

	public void setBlrPayRequestJson(BillPaymentRequest blrPayRequestJson) {
		this.blrPayRequestJson = blrPayRequestJson;
	}

	public BillPaymentResponse getBlrPayResponseJson() {
		return blrPayResponseJson;
	}

	public void setBlrPayResponseJson(BillPaymentResponse blrPayResponseJson) {
		this.blrPayResponseJson = blrPayResponseJson;
	}

	public Boolean isOnUs() {
		return isOnUs;
	}

	public void setOnUs(Boolean isOnUs) {
		this.isOnUs = isOnUs;
	}
	
}
