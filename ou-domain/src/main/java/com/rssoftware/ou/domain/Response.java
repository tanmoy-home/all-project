package com.rssoftware.ou.domain;

import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.TxnStatusComplainResponse;

import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.RequestType;

public class Response {

	private String tenantId;
	private Action action;
	private String nodeAddress;
	private BillFetchResponse billFetchResponse;
	private BillPaymentResponse billPaymentResponse;
	private TxnStatusComplainResponse complaintResponse;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public BillFetchResponse getBillFetchResponse() {
		return billFetchResponse;
	}

	public void setBillFetchResponse(BillFetchResponse billFetchResponse) {
		this.billFetchResponse = billFetchResponse;
	}

	public BillPaymentResponse getBillPaymentResponse() {
		return billPaymentResponse;
	}

	public void setBillPaymentResponse(BillPaymentResponse billPaymentResponse) {
		this.billPaymentResponse = billPaymentResponse;
	}

	public TxnStatusComplainResponse getComplaintResponse() {
		return complaintResponse;
	}

	public void setComplaintResponse(TxnStatusComplainResponse complaintResponse) {
		this.complaintResponse = complaintResponse;
	}

	public String getNodeAddress() {
		return nodeAddress;
	}

	public void setNodeAddress(String nodeAddress) {
		this.nodeAddress = nodeAddress;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

}
