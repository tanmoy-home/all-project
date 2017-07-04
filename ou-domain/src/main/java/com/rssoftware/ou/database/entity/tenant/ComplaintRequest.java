package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="COMPLAINT_REQUEST")
@NamedQuery(name="ComplaintRequest.findAll", query="SELECT cmp FROM ComplaintRequest cmp")
public class ComplaintRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8365616800446434391L;
	
	@Id
	@Column(name="COMPLAINT_ID")
	private String complaintId;

	@Column(name="TXN_ID")
	private String txnId;
	
	@Column(name="XCHNG_ID")
	private String xchangeId;
	
	
	@Column(name="MSG_ID")
	private String msgId;
	
	@Column(name="REF_ID")
	private String refId;
	
	@Column(name="MOBILE")
	private String mobile;
	
	@Column(name="CMP_TYPE")
	private String cmpType;
	
	@Column(name="CMP_STATUS")
	private String cmpStatus;
	
	@Column(name="RESPONSE_STATUS")
	private String responseStatus;
	
	@Column(name="AGENT_ID")
	private String agentId;
	
	@Column(name="BILLER_ID")
	private String billerId;
	
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name="DISPOSITION")
	private String disposition;
	
		
	@Column(name="DESCRIPTION")
	private String cmsDescription;
	
	@Column(name="SERV_REASON")
	private String servReason;
	
	@Column(name="PARTICIPAT_TYPE")
	private String participatetype;
	
	@Column(name="CRTN_TS")
	private Timestamp crtnTS;
	
	@Column(name="LAST_UPDT_TS")
	private Timestamp lastUpdtTS;
	
	@Column(name = "NODE_ADDRESS")
	private String nodeAddress;

	public String getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getXchangeId() {
		return xchangeId;
	}

	public void setXchangeId(String xchangeId) {
		this.xchangeId = xchangeId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCmpType() {
		return cmpType;
	}

	public void setCmpType(String cmpType) {
		this.cmpType = cmpType;
	}

	public String getCmpStatus() {
		return cmpStatus;
	}

	public void setCmpStatus(String cmpStatus) {
		this.cmpStatus = cmpStatus;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String cmpStatus) {
		this.responseStatus = cmpStatus;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public String getCmsDescription() {
		return cmsDescription;
	}

	public void setCmsDescription(String cmsDescription) {
		this.cmsDescription = cmsDescription;
	}

	public String getServReason() {
		return servReason;
	}

	public void setServReason(String servReason) {
		this.servReason = servReason;
	}

	public String getParticipatetype() {
		return participatetype;
	}

	public void setParticipatetype(String participatetype) {
		this.participatetype = participatetype;
	}

	public Timestamp getCrtnTS() {
		return crtnTS;
	}

	public void setCrtnTS(Timestamp timestamp) {
		this.crtnTS = timestamp;
	}

	public Timestamp getLastUpdtTS() {
		return lastUpdtTS;
	}

	public void setLastUpdtTS(Timestamp lastUpdtTS) {
		this.lastUpdtTS = lastUpdtTS;
	}

	public String getNodeAddress() {
		return nodeAddress;
	}

	public void setNodeAddress(String nodeAddress) {
		this.nodeAddress = nodeAddress;
	}

	@Override
	public String toString() {
		return "ComplaintRequest [complaintId=" + complaintId + ", txnId="
				+ txnId + ", xchangeId=" + xchangeId + ", msgId=" + msgId
				+ ", refId=" + refId + ", mobile=" + mobile + ", cmpType="
				+ cmpType + ", cmpStatus=" + cmpStatus + ", responseStatus=" + responseStatus + ", agentId=" + agentId
				+ ", billerId=" + billerId + ", category=" + category
				+ ", disposition=" + disposition + ", cmsDescription="
				+ cmsDescription + ", servReason=" + servReason
				+ ", participatetype=" + participatetype + ", crtnTS=" + crtnTS
				+ ", lastUpdtTS=" + lastUpdtTS + "]";
	}
	
	

}
