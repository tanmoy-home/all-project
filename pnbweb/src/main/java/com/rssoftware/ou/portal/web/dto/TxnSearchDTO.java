package com.rssoftware.ou.portal.web.dto;

public class TxnSearchDTO {
	
	private String error;
	private String txnlistAsString;
	private String searchBy;
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getTxnlistAsString() {
		return txnlistAsString;
	}
	public void setTxnlistAsString(String txnlistAsString) {
		this.txnlistAsString = txnlistAsString;
	}
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
}
