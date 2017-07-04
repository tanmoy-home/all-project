package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

public class BillerCoverageView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9070612585782937205L;
	private String coverageId;
	private String coverageName;
	private Boolean isSelected;
	public String getCoverageId() {
		return coverageId;
	}
	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}
	public String getCoverageName() {
		return coverageName;
	}
	public void setCoverageName(String coverageName) {
		this.coverageName = coverageName;
	}
	public Boolean getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

}
