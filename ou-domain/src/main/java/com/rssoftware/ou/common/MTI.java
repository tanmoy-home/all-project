package com.rssoftware.ou.common;

public enum MTI {

	PAYMENT("PAYMENT","200"),
	BATCH_PAYMENT("BATCH PAYMENT","1251"),
	FETCH("FETCH","1252"),
	SERVICE_FEE_ADJ("SERVICE FEE ADJUSTMENT","1260"), 
	SERVICE_FEE("SERVICE FEE","1280"),
	COMPLAINT("COMPLAINT","1290"),
	
	REFUND("REFUND","1270"),
	RF_PRE_ARB("REFUND PRE-ARBITRATION","1271"),
	RF_ARB("REFUND ARBITRATION","1272"),
	RF_GF_PART("REFUND PARTIAL GOOD FAITH","1273"),
	RF_GF_FULL("REFUND FULL GOOD FAITH","1274"),
	
	FORCE_PAYMENT("FORCE PAYMENT","1300"),
	FP_PRE_ARB("FORCE PAYMENT PRE-ARBITRATION","1301"),
	FP_ARB("FORCE PAYMENT ARBITRATION","1302"),
	FP_GF_PART("FORCE PAYMENT PARTIAL GOOD FAITH","1303"),
	FP_GF_FULL("FORCE PAYMENT FULL GOOD FAITH","1304"),
	
	;
	
	private final String expandedForm;
	private final String code;
	
	private MTI(String expandedForm,String code) {
		this.expandedForm = expandedForm;
		this.code = code;
	}

	public String getExpandedForm() {
		return expandedForm;
	}

	public String getCode() {
		return code;
	}
	
	public static MTI getFromName(String name){
		if (name == null){
			return null;
		}
		
		return MTI.valueOf(name);
	}

	public static MTI getFromExpandedForm(String expandedForm){
		if (expandedForm == null){
			return null;
		}
		
		for (MTI mti:MTI.values()){
			if (mti.getExpandedForm().equals(expandedForm)){
				return mti;
			}
		}
		
		return null;
	}
	
	public static MTI getFromExpanded(String expandedForm){
		if (expandedForm == null){
			return null;
		}
		
		for (MTI mti:MTI.values()){
			if (mti.name().equals(expandedForm)){
				return mti;
			}
		}
		
		return null;
	}

	public static MTI getFromCode(String code){
		if (code == null){
			return null;
		}
		
		for (MTI mti:MTI.values()){
			if (mti.getCode().equals(code)){
				return mti;
			}
		}		
		return null;
	}
	
	
	
}
