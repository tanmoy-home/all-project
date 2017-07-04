package com.rssoftware.ou.domain;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.TxnStatusComplainRequest;

import com.rssoftware.ou.common.RequestType;

public class Request {

	private String tenantId;
	private RequestType requestType;
	private BillFetchRequest billFetchRequest;	
	private BillFetchResponse billFetchResponse;
	private BillPaymentRequest billPaymentRequest;	
	private BillPaymentResponse billPaymentResponse;
	private TxnStatusComplainRequest complaintRequest;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public BillPaymentResponse getBillPaymentResponse() {
		return billPaymentResponse;
	}

	public void setBillPaymentResponse(BillPaymentResponse billPaymentResponse) {
		this.billPaymentResponse = billPaymentResponse;
	}

	public TxnStatusComplainRequest getComplaintRequest() {
		return complaintRequest;
	}

	public void setComplaintRequest(TxnStatusComplainRequest complaintRequest) {
		this.complaintRequest = complaintRequest;
	}	

}
