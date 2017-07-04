package com.rssoftware.ou.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.bbps.schema.BillFetchRequest;

public class BillFetchRequestExt implements Serializable {

	private String agentChannelID;
	private String agentChannelCustomerID;
	private String agentChannelTransactionID;
	private String currentNodeAddress;

	BillFetchRequest billFetchRequest;

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

	public BillFetchRequest getBillFetchRequest() {
		return billFetchRequest;
	}

	public void setBillFetchRequest(BillFetchRequest billFetchRequest) {
		this.billFetchRequest = billFetchRequest;
	}

	public String getCurrentNodeAddress() {
		return currentNodeAddress;
	}

	public void setCurrentNodeAddress(String currentNodeAddress) {
		this.currentNodeAddress = currentNodeAddress;
	}

}
