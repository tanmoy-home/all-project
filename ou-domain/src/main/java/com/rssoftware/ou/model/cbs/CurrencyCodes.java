package com.rssoftware.ou.model.cbs;

public enum CurrencyCodes {

	INR("INR");
	
	private String value;
	
	private CurrencyCodes(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
