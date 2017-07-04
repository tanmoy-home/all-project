package com.rssoftware.ou.common;

public enum InterchangeFeeDirectionType {
	B2C("Biller To Customer"), C2B("Customer To Biller");
	
    private final String expandedForm;
	
	private InterchangeFeeDirectionType(String expandedForm) {
		this.expandedForm = expandedForm;
	}

	public String getExpandedForm() {
		return expandedForm;
	}
}
