package com.rssoftware.ou.portal.web.controller;

import org.apache.commons.codec.binary.Base64;
import org.bbps.schema.Ack;
import org.bbps.schema.BillPaymentRequest;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.rest.OURestTemplate;

import in.co.rssoftware.bbps.schema.BillerCatagory;

@Controller
public class GreetingController {
	private final static Logger logger = LoggerFactory.getLogger(GreetingController.class);
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();
	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance(); 
	
	static {
	    HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("localhost"));
	}
	 @RequestMapping("/APIService/greeting")
	    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model, HttpServletRequest request) {
	        model.addAttribute("name", name);
	        
	        String plainCreds = "agent:password";
	        byte[] plainCredsBytes = plainCreds.getBytes();
	        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
	        String base64Creds = new String(base64CredsBytes);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", "Basic " + base64Creds);
	        
	        String url = "https://localhost:9090/APIService/testApi/urn:tenantId:OU12";
	        //HttpEntity<String> requestEntity = new HttpEntity<String>(headers);
	        //ResponseEntity<?> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
	        TransactionContext.putTenantId("OU12");
	        try{
	        	ResponseEntity<Ack> ack = internalRestTemplate.postForEntity(url, billPaymentRequest, Ack.class);	
	        }catch(Exception e)
	        {
	        	logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
	        }
	        
	        return "greeting";
	    }
}
