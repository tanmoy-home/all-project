package com.rssoftware.ou.portal.web.dto;

import in.co.rssoftware.bbps.schema.FetchResponse;

public class BillFetchResponse {
	
	private FetchResponse fetchResponse;
	private String amountOption;
	
	private String billerExactness;
	
	
	public String getBillerExactness() {
		return billerExactness;
	}
	public void setBillerExactness(String billerExactness) {
		this.billerExactness = billerExactness;
	}
	public FetchResponse getFetchResponse() {
		return fetchResponse;
	}
	public void setFetchResponse(FetchResponse fetchResponse) {
		this.fetchResponse = fetchResponse;
	}
	public String getAmountOption() {
		return amountOption;
	}
	public void setAmountOption(String amountOption) {
		this.amountOption = amountOption;
	}
	
	

}
