package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the TRANSACTION_DATA database table.
 * 
 */
@Embeddable
public class TransactionDataPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="REF_ID")
	private String refId;

	@Column(name="TXN_TYPE")
	private String txnType;

	public TransactionDataPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TransactionDataPK)) {
			return false;
		}
		TransactionDataPK castOther = (TransactionDataPK)other;
		return 
			this.refId.equals(castOther.refId)
			&& this.txnType.equals(castOther.txnType);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.refId.hashCode();
		hash = hash * prime + this.txnType.hashCode();
		
		return hash;
	}
}