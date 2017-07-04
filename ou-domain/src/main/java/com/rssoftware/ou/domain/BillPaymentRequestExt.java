package com.rssoftware.ou.domain;

import java.io.Serializable;

import org.bbps.schema.BillPaymentRequest;

public class BillPaymentRequestExt implements Serializable {

	private String agentChannelID;
	private String agentChannelCustomerID;
	private String agentChannelTransactionID;
	private String currentNodeAddress;
	BillPaymentRequest billPaymentRequest;
	private FinTransactionDetails finTransactionDetails;

	public String getAgentChannelID() {
		return agentChannelID;
	}

	public void setAgentChannelID(String agentChannelID) {
		this.agentChannelID = agentChannelID;
	}

	public String getAgentChannelCustomerID() {
		return agentChannelCustomerID;
	}

	public void setAgentChannelCustomerID(String agentChannelCustomerID) {
		this.agentChannelCustomerID = agentChannelCustomerID;
	}

	public String getAgentChannelTransactionID() {
		return agentChannelTransactionID;
	}

	public void setAgentChannelTransactionID(String agentChannelTransactionID) {
		this.agentChannelTransactionID = agentChannelTransactionID;
	}

	public BillPaymentRequest getBillPaymentRequest() {
		return billPaymentRequest;
	}

	public void setBillPaymentRequest(BillPaymentRequest billPaymentRequest) {
		this.billPaymentRequest = billPaymentRequest;
	}

	public String getCurrentNodeAddress() {
		return currentNodeAddress;
	}

	public void setCurrentNodeAddress(String currentNodeAddress) {
		this.currentNodeAddress = currentNodeAddress;
	}

	public FinTransactionDetails getFinTransactionDetails() {
		return finTransactionDetails;
	}

	public void setFinTransactionDetails(FinTransactionDetails finTransactionDetails) {
		this.finTransactionDetails = finTransactionDetails;
	}

}
