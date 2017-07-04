package com.rssoftware.ou.model.tenant;

import java.io.Serializable;


public class ComplaintserviceReasonsView implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4320790989421505590L;
	private String reasonId;
	private String reasonType;
	private String reasonCode;
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
		return "ComplaintserviceReasonsView [reasonId=" + reasonId
				+ ", reasonType=" + reasonType + ", reasonCode=" + reasonCode
				+ ", reasonName=" + reasonName + "]";
	}
}
