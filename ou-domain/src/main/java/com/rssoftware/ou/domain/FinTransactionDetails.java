package com.rssoftware.ou.domain;

import java.io.Serializable;

public class FinTransactionDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String clientTxnRefId;
	private String authCode;
	private String status;
	private String paymentAmountDetails;
	private byte[] requestJson;
	private byte[] responseJson;
	private String txnRefId;
	private String refId;
	private String totalAmount;

	public String getClientTxnRefId() {
		return clientTxnRefId;
	}

	public void setClientTxnRefId(String clientTxnRefId) {
		this.clientTxnRefId = clientTxnRefId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentAmountDetails() {
		return paymentAmountDetails;
	}

	public void setPaymentAmountDetails(String paymentAmountDetails) {
		this.paymentAmountDetails = paymentAmountDetails;
	}

	public byte[] getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(byte[] requestJson) {
		this.requestJson = requestJson;
	}

	public byte[] getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(byte[] responseJson) {
		this.responseJson = responseJson;
	}

	public String getTxnRefId() {
		return txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

}
