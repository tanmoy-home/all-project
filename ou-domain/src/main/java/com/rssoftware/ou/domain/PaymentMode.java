package com.rssoftware.ou.domain;

import java.io.Serializable;

public enum PaymentMode implements Serializable{
	Internet_Banking("Internet Banking"), 
	Debit_Card("Debit Card"), 
	Credit_Card("Credit Card"),
	Prepaid_Card("Prepaid Card"),
	IMPS("IMPS"),
	Cash("Cash"),
	UPI("UPI"),
	Wallet("Wallet"),
	NEFT("NEFT");
	
	

	private final String expandedForm;
	
	private PaymentMode(String expandedForm) {
		this.expandedForm = expandedForm;
	}

	public String getExpandedForm() {
		return expandedForm;
	}
	
	public static PaymentMode getFromName(String name){
		if (name == null){
			return null;
		}
		
		return PaymentMode.valueOf(name);
	}

	public static PaymentMode getFromExpandedForm(String expandedForm){
		if (expandedForm == null){
			return null;
		}
		
		for (PaymentMode pm:PaymentMode.values()){
			if (pm.getExpandedForm().equals(expandedForm)){
				return pm;
			}
		}
		
		return null;
	}
	
	public static  PaymentMode getExpPaymentMode(String paymentMode) {
		if(paymentMode != null && paymentMode.length() >0){
			
			for (PaymentMode pm : PaymentMode.values()) {
				if(paymentMode.equals(pm.name())){
					return pm;
				}
			}
		
		}
		return null;
	}	
}
