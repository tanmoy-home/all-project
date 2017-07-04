package org.npci.bbps.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.rssoftware.ou.common.CommonConstants;
import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillerFetchResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Sender implements Runnable{
	private static final String FRAW_URL = "/txt";
	private static final String BILLER_FETCH_RESPONSE_URL =  "/BillerFetchResponse/1.0/urn:referenceId:";
	
	static JAXBContext jaxbContext;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(BillFetchRequest.class,BillPaymentRequest.class, Ack.class, BillerFetchResponse.class);
		} catch (JAXBException e) {
			e.printStackTrace();
			jaxbContext = null;
		}
	}
	
	private boolean isRunningOnQa;
	private Application application;
	private RequestType requestType;
	
	public Sender(boolean isRunningOnQa, Application application, RequestType requestType) {
		super();
		this.isRunningOnQa = isRunningOnQa;
		this.application = application;
		this.requestType = requestType;
	}

	@Override
	public void run() {
//		String txnId ="HENSVVR4QOS7X1UGPY7JGUV444PL9T2C3QN";// UUID.randomUUID().toString();
		String txnId = IDGenerator.generateRandomString(35);		
		
//		String txnRefId = "OU1256789101";
						
		String url = isRunningOnQa ? application.qaDomain : application.localDomain;
		MultiValueMap<String, FileSystemResource> parts = new LinkedMultiValueMap<>();
		if(RequestType.FRAW == requestType) {
			File file = new File("00003OU132016071800.xml");
			url = url + FRAW_URL;
			try {
					parts.add(CommonConstants.BATCH_FILE_UPLOAD_PARAMETER_NAME, new FileSystemResource(file));
					Application.batchRestTemplate.postFileForBatch(url, parts, Void.class, Application.MAX_RETRY_COUNT);
				
			} catch (Exception e) {
					e.printStackTrace();
			}
		}
		if(RequestType.BILLER_FETCH_RESPONSE == requestType) {
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("BillerFetchResponse.xml");
			BillerFetchResponse billerFetchResponse = JAXB.unmarshal(is, BillerFetchResponse.class);
			try {
				is.close();
				url = url + BILLER_FETCH_RESPONSE_URL + txnId;	
				billerFetchResponse.getHead().setRefId(txnId);
				Ack ack = Application.restTemplate.postForEntity(url, billerFetchResponse, Ack.class).getBody();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(ack, System.out);				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}			
		}
		System.out.println(url);				
	}
}