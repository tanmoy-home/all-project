package com.rssoftware.ou.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.CustomerParamsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.tenant.service.IDGeneratorService;


public class DishTvHttpClient {

	 private static HttpClient client = HttpClientBuilder.create().build();
	 private static final String PROXY_HOST = "172.25.8.17";
	 private static final int PROXY_PORT = 8080;
	 private final Logger logger = LoggerFactory.getLogger(getClass());
	 private BillPaymentResponse billPaymentResponse;
	 private IDGeneratorService idGenetarorService;
	 private String vcNo = null;
	 
	 public DishTvHttpClient(IDGeneratorService idGenetarorService) {
		 this.idGenetarorService = idGenetarorService;
	 }
	 
		 //public boolean payBill(List<org.bbps.schema.CustomerDtlsType.Tag> tags) throws Exception {
			 public BillPaymentResponse payBill(BillPaymentRequest billPaymentRequest,BillPaymentResponse billPaymentResponse) throws Exception {			 

			this.billPaymentResponse = billPaymentResponse;
				 
			// make sure cookies is turn on
			CookieHandler.setDefault(new CookieManager());

			
			List<NameValuePair> paramListPmt = new ArrayList<NameValuePair>();
			List<NameValuePair> paramListVerify = new ArrayList<NameValuePair>();

			//paramList.add(new BasicNameValuePair("mobileno", billPaymentRequest.getCustomer().getMobile()));	
			paramListPmt.add(new BasicNameValuePair("productcost", billPaymentRequest.getAmount().getAmt().getAmount()));
			paramListPmt.add(new BasicNameValuePair("itzmodule", "BBPS"));
			paramListPmt.add(new BasicNameValuePair("packagecode", "Z"));
			paramListPmt.add(new BasicNameValuePair("dlrcode", "DLRCODE"));
			paramListPmt.add(new BasicNameValuePair("merchanttypekey", "DEFAULT_NPCIDI"));
			paramListVerify.add(new BasicNameValuePair("merchanttypekey", "DEFAULT_NPCIDI"));
			paramListPmt.add(new BasicNameValuePair("orderid", idGenetarorService.getUniqueID(CommonConstants.DISHTV_CUSTOM_PARAM_LEN, "")));
			paramListPmt.add(new BasicNameValuePair("exttransactionid", idGenetarorService.getUniqueID(CommonConstants.DISHTV_CUSTOM_PARAM_LEN, "BBPS")));
			
			CustomerParamsType customerParams = billPaymentRequest.getBillDetails().getCustomerParams();
			List<org.bbps.schema.CustomerParamsType.Tag> tags = customerParams.getTags();

			for(org.bbps.schema.CustomerParamsType.Tag tag: tags) {
				if("Registered Mobile Number / Viewing Card Number".equalsIgnoreCase(tag.getName())
						&& StringUtils.isNotEmpty(tag.getValue())) {
					paramListVerify.add(new BasicNameValuePair("vcnumber", tag.getValue()));
					if(tag.getValue().length() == 10)
						paramListPmt.add(new BasicNameValuePair("mobileno", tag.getValue()));
					else
						paramListPmt.add(new BasicNameValuePair("vcno", tag.getValue()));
				}
			}
				
			if(sendPost(CommonConstants.urlValidate, paramListVerify, true)) {
				if(vcNo != null)
					paramListPmt.add(new BasicNameValuePair("vcno", vcNo));
				sendPost(CommonConstants.urlPayment, paramListPmt, false);
			}
			else
				return null;
			
			return billPaymentResponse;

		  }	 
	 
	  private boolean sendPost(String url, List<NameValuePair> postParams, boolean validation)
		        throws Exception {

			HttpPost post = new HttpPost(url);
			
			// add header
			post.setHeader("Host", "www.itzcash.com");
			//post.setHeader("Host", "demo.itzcash.com"); //UAT
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setHeader("Cache-Control", "no-cache");				
			
			//HttpHost proxy = new HttpHost(PROXY_HOST,Integer.valueOf(PROXY_PORT), "http");
			//RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
			//post.setConfig(config);
			
			
			post.setEntity(new UrlEncodedFormEntity(postParams));
			
			logger.info("\nSending 'POST' request to URL : " + url);
			logger.debug("Post parameters : " + postParams);
			
			HttpResponse response = client.execute(post);

			int responseCode = response.getStatusLine().getStatusCode();
						
			logger.info("Response Code : " + responseCode);

			BufferedReader rd = new BufferedReader(
		                new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			logger.info("result = "+result);
			
			String blrResp = result.toString().split(",")[0];
			logger.info("blrResp :: "+blrResp);
			
			if(validation){
				vcNo = result.toString().split(",")[1];
				String customerName = result.toString().split(",")[2];
				billPaymentResponse.getBillerResponse().setCustomerName(customerName);
			}
			
			try
		      {
		         int res = Integer.parseInt(blrResp);
		         if(res==0)
		        	 return true;
		 
		      }
		      catch (NumberFormatException ex)
		      {
		    	  return false;
		      }
		 
			
			
			return false;
		  }
}
