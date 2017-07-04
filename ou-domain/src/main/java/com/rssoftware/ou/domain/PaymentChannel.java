package com.rssoftware.ou.domain;

import java.io.Serializable;

public enum PaymentChannel implements Serializable{
	Internet_Banking("INTB"), 
	Internet("INT"),
	Mobile("MOB"),
	Mobile_Banking("MOBB"),
	POS("POS"),
	MPOS("MPOS"),
	ATM("ATM"),
	Bank_Branch("BNKBRNCH"),
	Kiosk("KIOSK"),
	Agent("AGT"),
	Business_Correspondent("BSC");
	
	private final String expandedForm;
	
	private PaymentChannel(String expandedForm) {
		this.expandedForm = expandedForm;
	}

	public String getExpandedForm() {
		return expandedForm;
	}
	
	public static PaymentChannel getFromName(String name){
		if (name == null){
			return null;
		}
		
		return PaymentChannel.valueOf(name);
	}

	public static PaymentChannel getFromExpandedForm(String expandedForm){
		if (expandedForm == null){
			return null;
		}
		
		for (PaymentChannel pm:PaymentChannel.values()){
			if (pm.getExpandedForm().equals(expandedForm)){
				return pm;
			}
		}
		
		return null;
	}
	
	public static  PaymentChannel getExpPaymentChannel(String paymentChannel) {
		if(paymentChannel != null && paymentChannel.length() >0){
			
			for (PaymentChannel pm : PaymentChannel.values()) {
				if(paymentChannel.equals(pm.name())){
					return pm;
				}
			}
		
		}
		return null;
	}

	
}
