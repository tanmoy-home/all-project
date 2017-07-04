
package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the TRANSACTION_DATA database table.
 * 
 */


/**
 * The persistent class for the TRANSACTION_DATA database table.
 * 
 */
@Entity
@Table(name = "TRANSACTION_DATA")
@NamedQuery(name = "TransactionData.findAll", query = "SELECT t FROM TransactionData t")
public class TransactionData implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TransactionDataPK id;

	@Column(name = "CRTN_TS")
	private Timestamp crtnTs;

	@Column(name = "CURRENT_STATUS")
	private String currentStatus;

	@Column(name = "MSG_ID")
	private String msgId;

	@Column(name = "RECON_DESCRIPTION")
	private String reconDescription;

	@Column(name = "RECON_STATUS")
	private String reconStatus;

	@Column(name = "RECON_TS")
	private Timestamp reconTs;
	
	@Column(name = "REQUEST_JSON")
	private byte[] requestJson;
	
	@Column(name = "RESPONSE_JSON")
	private byte[] responseJson;

	@Column(name = "TXN_REF_ID")
	private String txnRefId;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "RECON_CYCLE_NO")
	private BigDecimal reconCycleNo;

	@Column(name = "AGNT_CHN_ID")
	private String agentChannelID;

	@Column(name = "AGNT_CHN_CUST_ID")
	private String agentChannelCustomerID;

	@Column(name = "AGNT_CHN_TXN_ID")
	private String agentChannelTxnID;

	@Column(name = "AGENT_ID")
	private String agentID;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "TAGS")
	private String tags;

	@Column(name = "TXN_DATE")
	private String dateOfTxn;

	@Column(name = "BLR_ID")
	private String billerId;

	@Column(name = "BILL_DATE")
	private String billDate;

	@Column(name = "BILL_AMOUNT")
	private BigDecimal billAmount;

	@Column(name = "NODE_ADDRESS")
	private String nodeAddress;
	
	@Column(name = "CLEARING_PROCESSED")
	private String clearingProcessed;

	@Column(name = "IS_ONUS", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isOnus;
	
	public TransactionData() {
	}

	public TransactionDataPK getId() {
		return this.id;
	}

	public void setId(TransactionDataPK id) {
		this.id = id;
	}

	public Timestamp getCrtnTs() {
		return this.crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getReconDescription() {
		return this.reconDescription;
	}

	public void setReconDescription(String reconDescription) {
		this.reconDescription = reconDescription;
	}

	public String getReconStatus() {
		return this.reconStatus;
	}

	public void setReconStatus(String reconStatus) {
		this.reconStatus = reconStatus;
	}

	public Timestamp getReconTs() {
		return this.reconTs;
	}

	public void setReconTs(Timestamp reconTs) {
		this.reconTs = reconTs;
	}

	public byte[] getRequestJson() {
		return this.requestJson;
	}

	public void setRequestJson(byte[] requestJson) {
		this.requestJson = requestJson;
	}

	public byte[] getResponseJson() {
		return this.responseJson;
	}

	public void setResponseJson(byte[] responseJson) {
		this.responseJson = responseJson;
	}

	public String getTxnRefId() {
		return this.txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}

	public Timestamp getUpdtTs() {
		return this.updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public BigDecimal getReconCycleNo() {
		return reconCycleNo;
	}

	public void setReconCycleNo(BigDecimal reconCycleNo) {
		this.reconCycleNo = reconCycleNo;
	}

	public String getAgentID() {
		return agentID;
	}

	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDateOfTxn() {
		return dateOfTxn;
	}

	public void setDateOfTxn(String dateOfTxn) {
		this.dateOfTxn = dateOfTxn;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

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

	public String getAgentChannelTxnID() {
		return agentChannelTxnID;
	}

	public void setAgentChannelTxnID(String agentChannelTxnID) {
		this.agentChannelTxnID = agentChannelTxnID;
	}

	public String getNodeAddress() {
		return nodeAddress;
	}

	public void setNodeAddress(String nodeAddress) {
		this.nodeAddress = nodeAddress;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
	public String getClearingProcessed() {
		return clearingProcessed;
	}

	public void setClearingProcessed(String clearingProcessed) {
		this.clearingProcessed = clearingProcessed;
	}

	public Boolean isOnus() {
		return isOnus;
	}

	public void setIsOnus(Boolean isOnus) {
		this.isOnus = isOnus;
	}
}

