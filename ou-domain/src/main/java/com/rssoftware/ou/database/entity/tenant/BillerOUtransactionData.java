package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "BILLEROU_TRANSACTION_DATA")
@NamedQuery(name = "BillerOUtransactionData.findAll", query = "SELECT t FROM BillerOUtransactionData t")
public class BillerOUtransactionData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8138072057924499563L;

	@EmbeddedId
	private TransactionDataPK id;

	@Column(name = "CRTN_TS")
	private Timestamp crtnTs;

	@Column(name = "CU_STATUS")
	private String cuStatus;

	@Column(name = "CU_REV_STATUS")
	private String cuRevStatus;

	@Column(name = "BLR_STATUS")
	private String blrStatus;

	@Column(name = "BLR_REV_STATUS")
	private String blrRevStatus;
	
	@Column(name = "MSG_ID")
	private String msgId;

	@Column(name = "RECON_DESCRIPTION")
	private String reconDescription;

	@Column(name = "RECON_STATUS")
	private String reconStatus;
	
	@Column(name = "BOU_BLR_RECON_PROCESSED")
	private String bouBlrReconProcessed;

	@Column(name = "RECON_TS")
	private Timestamp reconTs;
	
	@Column(name = "CU_REQUEST_JSON")
	private byte[] cuRequestJson;

	@Column(name = "CU_RESPONSE_JSON")
	private byte[] cuResponseJson;
	
	@Column(name = "BLR_REQUEST_JSON")
	private byte[] blrRequestJson;

	@Column(name = "BLR_RESPONSE_JSON")
	private byte[] blrResponseJson;

	@Column(name = "TXN_REF_ID")
	private String txnRefId;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "RECON_CYCLE_NO")
	private BigDecimal reconCyclenNo;

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
	
	/*   Not mentioned fields:::::::
	 *   email character varying(50),
         aadhaar character varying(50),
         pan character varying(50),
    */
  
	public TransactionDataPK getId() {
		return this.id;
	}

	public void setId(TransactionDataPK id) {
		this.id = id;
	}

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCuStatus() {
		return cuStatus;
	}

	public void setCuStatus(String cuStatus) {
		this.cuStatus = cuStatus;
	}

	public String getCuRevStatus() {
		return cuRevStatus;
	}

	public void setCuRevStatus(String cuRevStatus) {
		this.cuRevStatus = cuRevStatus;
	}

	public String getBlrStatus() {
		return blrStatus;
	}

	public void setBlrStatus(String blrStatus) {
		this.blrStatus = blrStatus;
	}

	public String getBlrRevStatus() {
		return blrRevStatus;
	}

	public void setBlrRevStatus(String blrRevStatus) {
		this.blrRevStatus = blrRevStatus;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getReconDescription() {
		return reconDescription;
	}

	public void setReconDescription(String reconDescription) {
		this.reconDescription = reconDescription;
	}

	public String getReconStatus() {
		return reconStatus;
	}

	public void setReconStatus(String reconStatus) {
		this.reconStatus = reconStatus;
	}

	public Timestamp getReconTs() {
		return reconTs;
	}

	public void setReconTs(Timestamp reconTs) {
		this.reconTs = reconTs;
	}

	public String getTxnRefId() {
		return txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public BigDecimal getReconCyclenNo() {
		return reconCyclenNo;
	}

	public void setReconCyclenNo(BigDecimal reconCyclenNo) {
		this.reconCyclenNo = reconCyclenNo;
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

	public String getNodeAddress() {
		return nodeAddress;
	}

	public void setNodeAddress(String nodeAddress) {
		this.nodeAddress = nodeAddress;
	}

	public byte[] getCuRequestJson() {
		return cuRequestJson;
	}

	public void setCuRequestJson(byte[] cuRequestJson) {
		this.cuRequestJson = cuRequestJson;
	}

	public byte[] getCuResponseJson() {
		return cuResponseJson;
	}

	public void setCuResponseJson(byte[] cuResponseJson) {
		this.cuResponseJson = cuResponseJson;
	}

	public byte[] getBlrRequestJson() {
		return blrRequestJson;
	}

	public void setBlrRequestJson(byte[] blrRequestJson) {
		this.blrRequestJson = blrRequestJson;
	}

	public byte[] getBlrResponseJson() {
		return blrResponseJson;
	}

	public void setBlrResponseJson(byte[] blrResponseJson) {
		this.blrResponseJson = blrResponseJson;
	}

	public String getBouBlrReconProcessed() {
		return bouBlrReconProcessed;
	}

	public void setBouBlrReconProcessed(String bouBlrReconProcessed) {
		this.bouBlrReconProcessed = bouBlrReconProcessed;
	}
  
}
