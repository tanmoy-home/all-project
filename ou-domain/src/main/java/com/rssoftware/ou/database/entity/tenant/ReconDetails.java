package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the RECON_DETAILS database table.
 * 
 */
@Entity
@Table(name = "RECON_DETAILS")
@NamedQuery(name = "ReconDetails.findAll", query = "SELECT r FROM ReconDetails r")
public class ReconDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ReconDetailsPK id;

	@Column(name = "RECON_DESCRIPTION")
	private String reconDescription;

	@Column(name = "RECON_STATUS")
	private String reconStatus;

	@Column(name = "AGENT_ID")
	private String agentId;

	@Column(name = "BILLER_ID")
	private String billerId;

	@Column(name = "TXN_REF_ID")
	private String txnRefId;
	
	public ReconDetails() {
	}

	public ReconDetailsPK getId() {
		return this.id;
	}

	public void setId(ReconDetailsPK id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "{\"id\":" + id + ", \"reconDescription\":\"" + reconDescription
				+ "\", \"reconStatus\":\"" + reconStatus + "\", \"agentId\":\""
				+ agentId + "\", \"txnRefId\":\""
				+ txnRefId + "\",\"billerId\":\"" + billerId + "\"}";
	}

}