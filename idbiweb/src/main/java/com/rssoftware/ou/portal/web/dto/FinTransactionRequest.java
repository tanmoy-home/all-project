package com.rssoftware.ou.portal.web.dto;

import java.io.Serializable;

import com.rssoftware.ou.portal.web.PGStrategyFactory;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;
import com.rssoftware.ou.portal.web.service.impl.PayByIDBINetBanking;

public class FinTransactionRequest implements Serializable {
	
	private Object request;
	private String paymentMode;
	private String tenantId;
	
	public FinTransactionRequest() {
	}
	
	public FinTransactionRequest(Object request) {
		this.request = request;
	}
	
	public Object getRequest() {
		return request;
	}
	public void setRequest(Object request) {
		this.request = request;
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
	
	public String getRequestAsJsonString() {
		PaymentGatewayStrategy strategy = null;
		try {	
			strategy = PGStrategyFactory.getPGStrategy(paymentMode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strategy.parseRequestObjectToJSONString(request);
	}

}
