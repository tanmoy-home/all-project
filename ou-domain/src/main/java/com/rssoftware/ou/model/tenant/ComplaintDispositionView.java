package com.rssoftware.ou.model.tenant;

public class ComplaintDispositionView {
	
private static final long serialVersionUID = 6085112351075416819L;
	
	private String dispositionId;
	private String dispositionName;
	
	public String getDispositionId() {
		return dispositionId;
	}
	public void setDispositionId(String dispositionId) {
		this.dispositionId = dispositionId;
	}
	public String getDispositionName() {
		return dispositionName;
	}
	public void setDispositionName(String dispositionName) {
		this.dispositionName = dispositionName;
	}
	@Override
	public String toString() {
		return "ComplaintDispositionView [dispositionId=" + dispositionId
				+ ", dispositionName=" + dispositionName + "]";
	}
	
}

