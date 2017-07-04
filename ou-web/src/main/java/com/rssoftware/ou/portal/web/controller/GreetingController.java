package com.rssoftware.ou.portal.web.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.bbps.schema.Ack;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.domain.UserDetails;

@Controller
@SessionAttributes("userDetails")
public class GreetingController {
	
	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance(); 
	private static final String hostport = "https://localhost:9091";

	 @ModelAttribute("userDetails")
	    public UserDetails getUserDetails (HttpServletRequest request) {
	        return new UserDetails();
	    }
	 
	 
	 
	 @RequestMapping("/APPService/landing")
	    public String login(@ModelAttribute("userDetails") UserDetails userDetails, Model model, HttpServletRequest request) {
			//if validation failure return back to login page
			if(!isValidated(userDetails))
				return "login-secure";
			
		 System.out.println("/APPService/landing");
		 System.out.println(userDetails.getTenantId());
		 
		 return "landing";
	 	}
	 
	 

	 @RequestMapping("/APPService/login")
	    public String logout(@ModelAttribute("userDetails") UserDetails userDetails, Model model, HttpServletRequest request) {
		 return "login-secure";
	 	}
	 
	 
	
	@RequestMapping("/APPService/greeting")
	    public String greeting(@ModelAttribute("userDetails") UserDetails userDetails, Model model, HttpServletRequest request) {
		
		
		userDetails.setTenantId(request.getParameter("tenantId"));
		userDetails.setUsername(request.getParameter("username"));
		userDetails.setPassword(request.getParameter("password"));
		
		model.addAttribute("userDetails", userDetails);
		
		//if validation failure return back to login page
		if(!isValidated(userDetails))
			return "login-secure";
		
		System.out.println("/APPService/greeting");
		
		model.addAttribute("name", userDetails.getUsername());
	    
		//API call ................
		String url = hostport+"/APIService/postApi/urn:tenantId:"+userDetails.getTenantId();
	        
	        try{
	        	ResponseEntity<Ack> ack = internalRestTemplate.postForEntity(url, getHttpEntity(userDetails), Ack.class);	
	        	
	        }catch(Exception e)
	        {
	        	System.out.println(e.getMessage());
	        }
	        
	        
	        return "greeting";
	    }
	
	
	
	 
	 private HttpEntity<?> getHttpEntity(UserDetails userDetails)
	 {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", "Basic " + getBasicAuth(userDetails));
	        //BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
	        Ack ack = new Ack();
	        HttpEntity<Ack> httpEntity = new HttpEntity<Ack>(ack, headers);

	        return httpEntity;
	 }
	 
	 private String getBasicAuth(UserDetails userDetails)
	 {
		 	String plainCreds = userDetails.getUsername()+":"+userDetails.getPassword();
	        byte[] plainCredsBytes = plainCreds.getBytes();
	        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
	        String base64Creds = new String(base64CredsBytes);
	        return base64Creds;
	 }
	 
	 private boolean isValidated(UserDetails userDetails)
	 {
		if(null == userDetails)
			return false;
		
		
		if(null == userDetails.getTenantId())
			return false;
		else if(userDetails.getTenantId().trim().equalsIgnoreCase(""))
			return false;

		if(null == userDetails.getTenantId())
			return false;
		else if(userDetails.getTenantId().trim().equalsIgnoreCase(""))
			return false;

		if(null == userDetails.getTenantId())
			return false;
		else if(userDetails.getTenantId().trim().equalsIgnoreCase(""))
			return false;
		
		String testURL = hostport+"/APIService/postApi/urn:tenantId:"+userDetails.getTenantId();
	        
	        try{
	        	//ResponseEntity<Ack> responseEntity = internalRestTemplate.postForEntity(testURL, getHttpEntity(userDetails), Ack.class);
	        	ResponseEntity responseEntity = internalRestTemplate.exchange(URI.create(testURL), HttpMethod.POST, getHttpEntity(userDetails), Ack.class);
	        	Ack ack = (Ack) responseEntity.getBody();
	        	System.out.println("@@@@@@@@@@@@@@@@@ "+ack.getRspCd());
	        	
	        	String url_get = hostport+"/APIService/getApi/urn:tenantId:"+userDetails.getTenantId();
	        	java.net.URI uri = URI.create(url_get);
	        	ResponseEntity responseEntity2 = internalRestTemplate.exchange(uri, HttpMethod.GET, getHttpEntity(userDetails), Ack.class);	        	
	        	Ack ack1 = (Ack) responseEntity2.getBody();
	        	System.out.println("$$$$$$$$$$$$$ responseEntity2 = "+responseEntity2.getStatusCode());
	        	System.out.println("############ response: "+ack1.getRspCd());

	        	
	        	if(!responseEntity.getStatusCode().equals(HttpStatus.OK))
	        		return false;
	        	
	        }catch(Exception e)
	        {
	        	System.out.println(e.getMessage());
	        	return false;
	        }

		 return true;
	 }
}
