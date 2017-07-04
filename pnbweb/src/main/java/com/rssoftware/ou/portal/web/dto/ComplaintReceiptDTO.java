package com.rssoftware.ou.portal.web.dto;

public class ComplaintReceiptDTO {
	
	private String complaintId;
	
	private String complaintDate;
	
	private String transactionId;
	
	private String reason;
	
	private String assignedTo;
	
	private String status;
	
	private String transactionLabel;
	
	private String responseMsg;
	
	private String isDup;
	
	
	public String getIsDup() {
		return isDup;
	}

	public void setIsDup(String isDup) {
		this.isDup = isDup;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getTransactionLabel() {
		return transactionLabel;
	}

	public void setTransactionLabel(String transationLabel) {
		this.transactionLabel = transationLabel;
	}

	public String getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}

	public String getComplaintDate() {
		return complaintDate;
	}

	public void setComplaintDate(String complaintDate) {
		this.complaintDate = complaintDate;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
