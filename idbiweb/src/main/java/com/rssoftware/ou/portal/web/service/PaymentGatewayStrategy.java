package com.rssoftware.ou.portal.web.service;

import java.util.Map;

import org.springframework.ui.Model;

import com.rssoftware.ou.portal.web.dto.BillInfoDTO;



public interface PaymentGatewayStrategy {
	public Object pay(BillInfoDTO billInfoDTO);
	public String populateModel(Model model);	
	public String getTxnRefId();
	public boolean isPaymentSuccess();	
	public String parseRequestObjectToJSONString(Object request);
	public String parseResponseObjectToJSONString(Object response);
	public Object digestResponseParams(Map<String, String> responseParams);
}
