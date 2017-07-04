package com.rssoftware.ou.domain;


import java.io.Serializable;


public enum InterChangeFeeDirection implements Serializable {
	B2_C("B2C"),
	C2_B("C2B");
	
	private final String expandedForm;
	
	private InterChangeFeeDirection(String expandedForm) {
		this.expandedForm = expandedForm;
	}

	public String getExpandedForm() {
		return expandedForm;
	}
	
	public static InterChangeFeeDirection getFromName(String name){
		if (name == null){
			return null;
		}
		
		return InterChangeFeeDirection.valueOf(name);
	}
	
	public static InterChangeFeeDirection fromValue(String v) {
		for (InterChangeFeeDirection c : InterChangeFeeDirection.values()) {
			if (c.expandedForm.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
