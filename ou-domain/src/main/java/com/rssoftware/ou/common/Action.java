package com.rssoftware.ou.common;

public enum Action {
	FETCH_REQUEST("BillFetchRequest"),
	PAYMENT_REQUEST("BillPaymentRequest"), 
	FETCH_RESPONSE("BillFetchResponse"), 
	PAYMENT_RESPONSE("BillPaymentResponse"),
	HEART_BEAT(""), 
	CMS(""), 
	ACK(""),
	NOT_FOUND(""),
	AGENT_INSTITUTE_REQUEST(""),
	AGENT_REQUEST(""),
	BILLER_REQUEST(""),
	BILLER_LIST_REQUEST(""),
	BILLER_LIST_RESPONSE("BillerFetchResponse"),
	COMPLAINT_RESPONSE("TxnStatusComplainResponse"),
	COMPLAINT_REQUEST(""),
	PAYMENT_GATEWAY_REQUEST(""),
	PAYMENT_GATEWAY_RESPONSE(""),
	PAYMENT_GATEWAY_ERROR_RESPONSE(""),
	PAYMENT_GATEWAY_REFUND_REQUEST(""),
	PAYMENT_GATEWAY_REFUND_RESPONSE(""),
	PAYMENT_GATEWAY_REFUND_ERROR_RESPONSE(""),
	SMS_PARAM_ERROR(""),
	SMS_CONFIG_NOT_FOUND_ERROR(""),
	REVERSAL_REQUEST(""),
	REVERSAL_RESPONSE("");

	String alias;
	
	Action(String alias){
		this.alias = alias;
	}
	
	public String alias(){
		return this.alias;
	}
}
