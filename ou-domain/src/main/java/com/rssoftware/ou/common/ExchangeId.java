package com.rssoftware.ou.common;

public enum ExchangeId {
	
	FOURZEROONE("401"),
	FIVEZEROONE("501"),
	FIVEZEROSIX("506");
	
	private String value;
	private ExchangeId(String val){
		value = val;
	}
	public String getExpandedForm() {
		return value;
	}

}
