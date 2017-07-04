package org.npci.bbps.test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.utils.CommonUtils;

/*
 * Simple re-implementation of RestTemplace class to automatically sign and validate the XML Signature
 */
public final class BBPSBatchRestTemplate {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private final RestTemplate restTemplate;

	@SuppressWarnings("unused")
	private static int MAX_RETRY_COUNT_ = 3;
	
	static JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(Ack.class, BillFetchRequest.class, BillFetchResponse.class, BillPaymentRequest.class, BillPaymentResponse.class);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jaxbContext = null;
		}
	}

	/*
	 * making the constructor private so that the object cannot be created
	 * directly.
	 */
	private BBPSBatchRestTemplate() {
		restTemplate = new RestTemplate(clientHttpRequestFactory());
		BBPSBatchRestTemplateInterceptor ri = new BBPSBatchRestTemplateInterceptor();
		List<ClientHttpRequestInterceptor> ris = new ArrayList<>(1); 
		ris.add(ri);
		restTemplate.setInterceptors(ris);
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		DataPumpClientHttpRequestFactory factory = new DataPumpClientHttpRequestFactory();
		factory.setReadTimeout(60000);
		factory.setConnectTimeout(1000);
		factory.setBufferRequestBody(false);
		
		try {
			if (CommonUtils.hasValue(CommonUtils.getProxyHost()) && CommonUtils.hasValue(CommonUtils.getProxyPort())){
				Proxy proxy= new Proxy(Type.HTTP, new InetSocketAddress(CommonUtils.getProxyHost(), Integer.parseInt(CommonUtils.getProxyPort())));
				factory.setProxy(proxy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not set proxy");
		}
		
		
		return factory;
	}

	/*
	 * @return the object of RestTemplate which contains the Interceptors and
	 * RequestFactory so that the request and response XML can be validated
	 * automatically
	 */
	public static BBPSBatchRestTemplate createObject() {
		// return the RestTemplate object
		return new BBPSBatchRestTemplate();
	}	
	
	public <T> ResponseEntity<T> postFileForBatch(String url, MultiValueMap<String, FileSystemResource> parts, Class<T> responseType,  int MAX_RETRY_COUNT) {
		int i = 1;
		while (true) {
			try {
				
				if (log.isDebugEnabled()){
					try{
						log.info("Attempt ==>"+i+" :: Message for outbound request :"+url);
						FileSystemResource fileSource = parts.getFirst(CommonConstants.BATCH_FILE_UPLOAD_PARAMETER_NAME);						
						log.info(fileSource.getFilename()); 
					}catch(Exception ex){
						log.info("Got JAXB Ecpetion in BBPSRestTemplate while logging message");
					}
				}				
				
				ResponseEntity<T> ack = restTemplate.postForEntity(url, parts, responseType);
				
				if (log.isDebugEnabled()){
						log.info("Acknowlgement received for :"+url);
				}				
				return ack;
			} catch (ResourceAccessException e) {
				log.info("Exception in Connecting URL : "+ url );
				if (i >= MAX_RETRY_COUNT) {
					throw e;
				}
			}
			i++;
		}
	}
}
