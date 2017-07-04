package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the RECON_DETAILS database table.
 * 
 */
@Embeddable
public class ReconDetailsPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "REF_ID")
	private String refId;

	@Column(name = "TXN_TYPE")
	private String txnType;

	@Column(name = "RECON_ID")
	private String reconId;

	public ReconDetailsPK() {
	}

	public String getRefId() {
		return this.refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getTxnType() {
		return this.txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public String getReconId() {
		return reconId;
	}

	public void setReconId(String reconId) {
		this.reconId = reconId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ReconDetailsPK)) {
			return false;
		}
		ReconDetailsPK castOther = (ReconDetailsPK) other;
		return this.refId.equals(castOther.refId)
				&& this.txnType.equals(castOther.txnType)
				&& this.reconId.equals(castOther.reconId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.refId.hashCode();
		hash = hash * prime + this.txnType.hashCode();
		hash = hash * prime + this.reconId.hashCode();
		return hash;
	}

	@Override
	public String toString() {
		return "{\"refId\":\"" + refId + "\", \"txnType\":\"" + txnType
				+ "\", \"reconId\":\"" + reconId + "\"}";
	}

}
