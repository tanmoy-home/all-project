package com.rssoftware.ou.model.cbs;

public enum AccountType {

	SAVINGS("S"), CURRENT("C");
	
	private String value;
	
	private AccountType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
