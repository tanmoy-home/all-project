package com.rssoftware.ou.model.tenant;

import com.rssoftware.ou.common.RequestType;

public class ReconDetailsView {
	public enum ReconStatus {
		BROUGHT_FORWARD, NOT_IN_OU, NOT_IN_CU, NON_MATCHING_FIELDS, PENDING;
	}

	private String refId;
	private String txnRefId;
	private RequestType requestType;
	private String reconId;
	private String reconDescription;
	private ReconStatus reconStatus;
	private String agentId;
	private String billerId;

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

	public String getReconId() {
		return reconId;
	}

	public void setReconId(String reconId) {
		this.reconId = reconId;
	}

	public String getReconDescription() {
		return reconDescription;
	}

	public void setReconDescription(String reconDescription) {
		this.reconDescription = reconDescription;
	}

	public ReconStatus getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(ReconStatus reconStatus) {
		this.reconStatus = reconStatus;
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

	public String getTxnRefId() {
		return txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	
}
