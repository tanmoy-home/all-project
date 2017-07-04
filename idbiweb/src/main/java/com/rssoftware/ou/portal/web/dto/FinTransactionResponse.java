package com.rssoftware.ou.portal.web.dto;

import java.io.Serializable;

import com.rssoftware.ou.portal.web.PGStrategyFactory;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;

public class FinTransactionResponse implements Serializable {
	
	private Object response;
	private String paymentMode;
	private String tenantId;
	
	public FinTransactionResponse() {
	}
	
	public FinTransactionResponse(Object response) {
		this.response = response;
	}
	
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}	
	
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getResponseAsJsonString() {
		PaymentGatewayStrategy strategy = null;
		try {
			strategy = PGStrategyFactory.getPGStrategy(paymentMode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strategy.parseResponseObjectToJSONString(response);
	}

}
