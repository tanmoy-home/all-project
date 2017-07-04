package com.rssoftware.ou.portal.web.modal;

import in.co.rssoftware.bbps.schema.FetchResponse;

public class BillFetchResponse {
	
	private FetchResponse fetchResponse;
	private String amountOption;
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
