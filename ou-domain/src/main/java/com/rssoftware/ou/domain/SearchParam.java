package com.rssoftware.ou.domain;

import java.io.Serializable;

public class SearchParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6419727772994139613L;
	private String mobNo;
	private String transactionId;
	
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	

}
