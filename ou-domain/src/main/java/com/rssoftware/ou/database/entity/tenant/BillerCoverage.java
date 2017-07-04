package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

public class BillerCoverage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6354566024908913355L;

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

	@Override
	public String toString() {
		return "BillerCoverage [coverageId=" + coverageId + ", coverageName="
				+ coverageName + ", isSelected=" + isSelected + "]";
	}

}
