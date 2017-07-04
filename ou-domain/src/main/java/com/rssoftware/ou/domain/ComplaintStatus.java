package com.rssoftware.ou.domain;

import java.sql.Timestamp;

public class ComplaintStatus {
	
	private String complaintId;
	private String assignInst;
	private String status;
	private String escLevel;
	private String reason;
	private String updtTs;
	private Timestamp lstUpdatedTimeTs;
	
	public String getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	public String getAssignInst() {
		return assignInst;
	}
	public void setAssignInst(String assignInst) {
		this.assignInst = assignInst;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEscLevel() {
		return escLevel;
	}
	public void setEscLevel(String escLevel) {
		this.escLevel = escLevel;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUpdtTs() {
		return updtTs;
	}
	public void setUpdtTs(String updtTs) {
		this.updtTs = updtTs;
	}
	public Timestamp getLstUpdatedTimeTs() {
		return lstUpdatedTimeTs;
	}
	public void setLstUpdatedTimeTs(Timestamp lstUpdatedTimeTs) {
		this.lstUpdatedTimeTs = lstUpdatedTimeTs;
	}
}
