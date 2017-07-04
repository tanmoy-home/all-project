package com.rssoftware.ou.portal.web.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.common.utils.WebAPIURL;
import com.rssoftware.ou.portal.web.BillPayment;
import com.rssoftware.ou.portal.web.modal.BillInfoDTO;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;

public abstract class AbstractPaymentStrategy implements PaymentGatewayStrategy {
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractPaymentStrategy.class);
	
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();
	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();
	
	private List<PGParam> pgParams;
	
	protected BillPayment billPayment;
	
/*	static {
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("localhost"));
	}*/
		
	public BillPayment getBillPayment() {
		return billPayment;
	}

	public void setBillPayment(BillPayment billPayment) {
		this.billPayment = billPayment;
	}
	
	public List<PGParam> getPgParams() {
		return pgParams;
	}	

	public void setPgParams(List<PGParam> pgParams) {
		this.pgParams = pgParams;
	}
	
	// Abstract method pay
//	public abstract boolean pay();
	public abstract Object pay(BillInfoDTO billInfoDTO);
	
	public abstract String populateModel(Model model);
	
	

	// Validation method implemented by each subclass. By default it is true.
	public boolean validate() {
		return true;
	}
	
	// Load the context by calling the API.
	public void loadPGParams(String tenantId, String uri) {
		logger.info("In the method loadContext of AbstractPaymentStrategy.");
		
		// Basic authentication credentials.
		String plainCreds = "agent1:password";//CommonConstants.getPropretyFiles("BASIC_AUTH_CREDENTIAL");//"agent1:password";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		// Adding basic authentication in the header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
		try {
			
			HttpEntity httpEntity = new HttpEntity(headers);
			String url = uri + WebAPIURL.PGPARAM_LIST_URL;
			ResponseEntity responseEntity = internalRestTemplate.exchange(url, HttpMethod.GET, httpEntity, List.class, tenantId);
			pgParams = (List<PGParam>) responseEntity.getBody();
			
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
		
	}
	
	// Load the context by calling the API.
	public String encrypt(String originalStr) {
		return originalStr;
	}
	
	// Load the context by calling the API.
	public String decrypt(String encryptedStr) {
		return encryptedStr;
	}
	
	public String getPGParamValue(String pgParamName) {
		for (Object obj : pgParams) {
			LinkedHashMap map = (LinkedHashMap) obj;
			if (pgParamName.equals(map.get("paramName"))) {
				return map.get("paramValue").toString();
			}
		}
		return null;
	}
	
}
