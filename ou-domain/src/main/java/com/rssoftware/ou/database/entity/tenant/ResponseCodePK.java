package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;

/**
 * The primary key class for the RESPONSE_CODES database table.
 * 
 */
@Embeddable
public class ResponseCodePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="RESPONSE_CODE")
	private String responseCode;

	@Column(name="REQUEST_TYPE")
	private String requestType;

	@Type(type= "org.hibernate.type.NumericBooleanType")
	@Column(name = "IS_REVERSAL", nullable = false)
	private Boolean isReversal;

	@Column(name="RESPONSE_REASON")
	private String responseReason;
	
	public ResponseCodePK() {
	}
	public String getResponseCode() {
		return this.responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getRequestType() {
		return this.requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public Boolean getIsReversal() {
		return this.isReversal;
	}
	public void setIsReversal(Boolean isReversal) {
		this.isReversal = isReversal;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ResponseCodePK)) {
			return false;
		}
		ResponseCodePK castOther = (ResponseCodePK)other;
		return 
			this.responseCode.equals(castOther.responseCode)
			&& this.requestType.equals(castOther.requestType)
			&& this.responseReason.equals(castOther.responseReason)
			&& this.isReversal.equals(castOther.isReversal);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.responseCode.hashCode();
		hash = hash * prime + this.requestType.hashCode();
		hash = hash * prime + this.responseReason.hashCode();
		hash = hash * prime + this.isReversal.hashCode();
		
		return hash;
	}
	public String getResponseReason() {
		return responseReason;
	}
	public void setResponseReason(String responseReason) {
		this.responseReason = responseReason;
	}
}