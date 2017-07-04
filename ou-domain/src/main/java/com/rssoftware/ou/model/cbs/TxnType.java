/**
 * 
 */
package com.rssoftware.ou.model.cbs;

/**
 * @author rsdpp
 *
 */
public enum TxnType {

	DEBIT("D"), CREDIT("C"), BALANCE_ENQ("BE"), DEBIT_REVERSAL("DR"), CREDIT_REVERSAL("CR");
	
	private String value;
	
	private TxnType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
