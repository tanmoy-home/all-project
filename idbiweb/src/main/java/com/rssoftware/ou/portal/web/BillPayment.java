package com.rssoftware.ou.portal.web;

import java.net.URI;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.bbps.schema.BillFetchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.portal.web.dto.BillInfoDTO;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;

public class BillPayment {
	
	private final static Logger logger = LoggerFactory.getLogger(BillPayment.class);
	
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();
	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();
	
	private PaymentGatewayStrategy strategy;

	public PaymentGatewayStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(PaymentGatewayStrategy strategy) {
		this.strategy = strategy;
	}
	
	public Object pay(BillInfoDTO billInfoDTO) {
		return this.strategy.pay(billInfoDTO);
	}
	
	public Object pay(PaymentGatewayStrategy strategy, BillInfoDTO billInfoDTO) {
		return strategy.pay(billInfoDTO);
	}
	
}
