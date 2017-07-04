package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name="COMPLAINT_SERVICE_REASONS")
@NamedQuery(name="ComplaintserviceReasons.findAll", query="SELECT csr FROM ComplaintserviceReasons csr")
public class ComplaintserviceReasons implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2314186761344324708L;
	
	@Id
	@Column(name="COMPLAINT_REASION_ID")
	private String reasonId;

	@Column(name="COMPLAINT_REASON_TYPE")
	private String reasonType;
	
	@Column(name="COMPLAINT_REASON_CODE")
	private String reasonCode;
	
	@Column(name="COMPLAINT_REASON_NAME")
	private String reasonName;

	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getReasonType() {
		return reasonType;
	}

	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReasonName() {
		return reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}

	@Override
	public String toString() {
		return "ComplaintserviceReasons [reasonId=" + reasonId
				+ ", reasonType=" + reasonType + ", reasonCode=" + reasonCode
				+ ", reasonName=" + reasonName + "]";
	}
	
	

}
