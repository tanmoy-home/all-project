package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.rssoftware.ou.common.RequestStatus;



@Entity
@Table(name="COMPLAINT_RESPONSE")
@NamedQuery(name="ComplaintResponse.findAll", query="SELECT cmp FROM ComplaintResponse cmp")
public class ComplaintResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -38199889554348522L;
	
	
	@Id
	@Column(name="REF_ID")
	private String refId;
	
	@Column(name="MSG_ID")
	private String msgId;
	
	@Column(name="COMPLAINT_ID_CU")
	private String complaintIdByCU;

	@Column(name="OPEN_COMPLAINT")
	private String openComplaint;
	
	@Column(name="ASSIGNED_TO")
	private String assignedTo;
	
	@Column(name="RESP_CD")
	private String respCode;
	
	@Column(name="RESP_REASON")
	private String respReason;
	
	@Column(name="COMPLAINT_STATUS")
	private String complaintStatus;
	
	@Column(name="TXN_SEARCH_RESP")
	private String txnSearchResponse;
	
	@Column(name="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name="MOBILE")
	private String mobile;
	
	@Column(name="RESPONSE_STATUS")
	private String responseStatus;
	
	@Column(name="CRTN_TS")
    private Timestamp crtnTs;
	
	@Column(name="LAST_UPDT_TS")
    private Timestamp updtTs;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getComplaintIdByCU() {
		return complaintIdByCU;
	}

	public void setComplaintIdByCU(String complaintIdByCU) {
		this.complaintIdByCU = complaintIdByCU;
	}

	public String getOpenComplaint() {
		return openComplaint;
	}

	public void setOpenComplaint(String openComplaint) {
		this.openComplaint = openComplaint;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespReason() {
		return respReason;
	}

	public void setRespReason(String respReason) {
		this.respReason = respReason;
	}

	public String getComplaintStatus() {
		return complaintStatus;
	}

	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public String getTxnSearchResponse() {
		return txnSearchResponse;
	}

	public void setTxnSearchResponse(String txnSearchResponse) {
		this.txnSearchResponse = txnSearchResponse;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	@Override
	public String toString() {
		return "ComplaintResponse [refId=" + refId + ", msgId=" + msgId
				+ ", complaintIdByCU=" + complaintIdByCU + ", openComplaint="
				+ openComplaint + ", assignedTo=" + assignedTo + ", respCode="
				+ respCode + ", respReason=" + respReason
				+ ", complaintStatus=" + complaintStatus
				+ ", txnSearchResponse=" + txnSearchResponse
				+ ", customerName=" + customerName + ", mobile=" + mobile
				+ ", responseStatus=" + responseStatus + ", crtnTs=" + crtnTs
				+ ", updtTs=" + updtTs + "]";
	}

	

	
	


	
}
