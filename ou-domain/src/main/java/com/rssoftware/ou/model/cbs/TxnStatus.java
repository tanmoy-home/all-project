/**
 * 
 */
package com.rssoftware.ou.model.cbs;

/**
 * @author rsdpp
 *
 */
public enum TxnStatus {

	SUCCESS("Success"), FAILURE("Failure");
	
	private String value;
	
	private TxnStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
