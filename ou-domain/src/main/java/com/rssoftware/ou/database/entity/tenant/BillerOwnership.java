package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

public class BillerOwnership implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672918366776963894L;

	private String billerOwnershipId;
	private String billerOwnershipName;
	private Boolean isSelected;

	public String getBillerOwnershipId() {
		return billerOwnershipId;
	}

	public void setBillerOwnershipId(String billerOwnershipId) {
		this.billerOwnershipId = billerOwnershipId;
	}

	public String getBillerOwnershipName() {
		return billerOwnershipName;
	}

	public void setBillerOwnershipName(String billerOwnershipName) {
		this.billerOwnershipName = billerOwnershipName;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "BillerOwnership [billerOwnershipId=" + billerOwnershipId
				+ ", billerOwnershipName=" + billerOwnershipName
				+ ", isSelected=" + isSelected + "]";
	}

}
