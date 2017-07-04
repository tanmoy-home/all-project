package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

public class BillerOwnershipView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2862331140266481979L;
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
}
