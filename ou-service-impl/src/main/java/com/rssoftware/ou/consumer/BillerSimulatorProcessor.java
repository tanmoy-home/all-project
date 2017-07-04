package com.rssoftware.ou.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.HeadType;
import org.bbps.schema.ReasonType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.TxnStatusComplainRequest;
import org.bbps.schema.TxnType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.domain.Request;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class BillerSimulatorProcessor implements Consumer<Event<Request>> {
	
	@Value("${SIMULATOR_BILL_FETCH_RESPONSE_URL}")
    private String SIMULATOR_BILL_FETCH_RESPONSE_URL;
	
	@Value("${SIMULATOR_BILL_PAY_RESPONSE_URL}")
    private String SIMULATOR_BILL_PAY_RESPONSE_URL;

	@Value("${BASIC_AUTH_CREDENTIAL}")
    private String BASIC_AUTH_CREDENTIAL;
	
	

	private static Log logger = LogFactory.getLog(BillerSimulatorProcessor.class);
	

	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();
	
	@Override
	public void accept(Event<Request> event) {
		Request request = event.getData();
//		TransactionContext.putTenantId(request.getTenantId());
		try {
			switch (request.getRequestType()) {
			case FETCH:
				processBillFetchRequest(request.getBillFetchRequest());
				break;
			case PAYMENT:
				processBillPayRequest(request.getBillPaymentRequest());
				break;
			case COMPLAINT:
				processComplaintRequest(request.getComplaintRequest());
				break;
			}
		} catch (ValidationException ve) {
			logger.error(ve.getMessage());
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processBillFetchRequest(BillFetchRequest billFetchRequest) throws ValidationException {
		String postURL = SIMULATOR_BILL_FETCH_RESPONSE_URL + "?referenceId=" + billFetchRequest.getHead().getRefId();
		
		OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
		HttpEntity httpEntity = new HttpEntity(getBillFetchResponse(billFetchRequest), getHttpHeaders());
		restTemplate.postForEntity(postURL, httpEntity, Ack.class);
		
		/*ResponseEntity<Ack> responseEntity = restTemplate.postForEntity(postURL, httpEntity, Ack.class);
		Ack ack = responseEntity.getBody();*/
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void processBillPayRequest(BillPaymentRequest billPaymentRequest) throws ValidationException {
		// Debit the transaction amount.
		String postURL = SIMULATOR_BILL_PAY_RESPONSE_URL + "?referenceId=" + billPaymentRequest.getHead().getRefId();
		OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
		BillPaymentResponse billPaymentResponse = getBillPaymentResponse(billPaymentRequest);
		
		HttpEntity httpEntity = new HttpEntity(billPaymentResponse, getHttpHeaders());
		restTemplate.postForEntity(postURL, httpEntity, Ack.class);
		
		/*ResponseEntity<Ack> responseEntity = restTemplate.postForEntity(postURL, httpEntity, Ack.class);
		Ack ack = responseEntity.getBody();*/
	}
	
	private void processComplaintRequest(TxnStatusComplainRequest complainRequest) throws ValidationException {
		// Credit the transaction amount, which was debited before.
		return;
	}
	
	public static BillPaymentResponse getBillPaymentResponse(BillPaymentRequest billPaymentRequest) {		
		
		BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
		BillerResponseType billerResponseType = new BillerResponseType();		
		BillDetailsType billDetailsType = billPaymentRequest.getBillDetails();
		billPaymentResponse.setBillDetails(billDetailsType);
		
		billerResponseType.setCustomerName("Sumana Paul");
		billerResponseType.setAmount(billPaymentRequest.getAmount().getAmt().getAmount());
		billerResponseType.setDueDate("2016-02-26");
		billerResponseType.setBillDate("2016-02-06");
		billerResponseType.setBillNumber("Bill001");
		billerResponseType.setBillPeriod("February");
		billerResponseType.setCustConvFee(billPaymentRequest.getAmount().getAmt().getCustConvFee());
		
		billPaymentResponse.setBillerResponse(billerResponseType);	
		
		billPaymentResponse.setBillerResponse(billerResponseType);
		
		ReasonType reasonType = new ReasonType();
		reasonType.setApprovalRefNum("87654321");
		reasonType.setComplianceReason("");
		reasonType.setComplianceRespCd("");
		reasonType.setResponseCode("000");
		reasonType.setResponseReason("Successful");
		billPaymentResponse.setReason(reasonType);
		TxnType txnType = new TxnType();
		txnType.setMsgId(billPaymentRequest.getTxn().getMsgId());
		txnType.setTxnReferenceId(billPaymentRequest.getTxn().getTxnReferenceId());
		txnType.setTs(billPaymentRequest.getTxn().getTs());
		txnType.setType(TransactionType.FORWARD_TYPE_RESPONSE.value());
		billPaymentResponse.setTxn(txnType);
		billPaymentResponse.setHead(billPaymentRequest.getHead());
		
		
		logger.info("================================: " + new Date().toGMTString() + ":  getBillPaymentResponse=============");
		LogUtils.logReqRespMessage(billPaymentResponse, billPaymentResponse.getHead().getRefId(), Action.PAYMENT_RESPONSE);				
		return billPaymentResponse;
	}
	
	public static BillFetchResponse getBillFetchResponse(BillFetchRequest billFetchRequest) {		
//		BillDetailsType billDetailsType = billFetchRequest.getBillDetails();
//		billDetailsType.setBiller(null);
//		billFetchResponse.setBillDetails(billDetailsType);
//		billFetchResponse.setHead(billFetchRequest.getHead());
//		
//		ReasonType reasonType = new ReasonType();
//		reasonType.setApprovalRefNum("AB123456");
//		reasonType.setComplianceReason("Date and Time incorrect.");
//		reasonType.setComplianceRespCd("022");
//		reasonType.setResponseCode("000");
//		reasonType.setResponseReason("Successful");
//		reasonType.setValue(null);
//		billFetchResponse.setReason(reasonType);
//		
//		TxnType txnType = billFetchRequest.getTxn();
//		txnType.setRiskScores(null);
//		billFetchResponse.setTxn(txnType);		
//		billFetchResponse.setBillerResponse(null);	
//		
//		BillerResponseType billerResponse = new BillerResponseType();
//		billerResponse.setAmount("1230.00");		
//		billerResponse.setBillDate(new Date(System.currentTimeMillis()).toString());
//		billerResponse.setBillNumber("1232332");
//		billerResponse.setBillPeriod("January");
//		billerResponse.setCustConvDesc("Customer Service Fee");
//		billerResponse.setCustConvFee("100");
//		billerResponse.setCustomerName("Arnab Sinha");
//		billerResponse.setDueDate(new Date(System.currentTimeMillis()).toString());
//		
//		BillerResponseType.Tag tag1 = new BillerResponseType.Tag();
//		tag1.setName("Amount 1");
//		tag1.setValue("2000");
//		BillerResponseType.Tag tag2 = new BillerResponseType.Tag();
//		tag2.setName("Amount 2");
//		tag2.setValue("3000");
//		billerResponse.getTags().add(tag1);
//		billerResponse.getTags().add(tag2);
//		billFetchResponse.setBillerResponse(billerResponse);
//		
//		AdditionalInfoType additionalInfo = new AdditionalInfoType();
//		AdditionalInfoType.Tag tag3 = new AdditionalInfoType.Tag();
//		tag3.setName("BlRspFld1");
//		tag3.setValue("");
//		AdditionalInfoType.Tag tag4 = new AdditionalInfoType.Tag();
//		tag4.setName("BlRspFld2");
//		tag4.setValue("");
//		additionalInfo.getTags().add(tag3);
//		additionalInfo.getTags().add(tag4);
//		billFetchResponse.setAdditionalInfo(additionalInfo);		
		
		BillFetchResponse billFetchResponse = new BillFetchResponse();			
		BillerResponseType billerResponseType = new BillerResponseType();		
		BillDetailsType billDetailsType = billFetchRequest.getBillDetails();
//		billDetailsType.setBiller(billDetailsType.getBiller());
		billFetchResponse.setBillDetails(billDetailsType);
		
		billerResponseType.setCustomerName("Sumana Paul");
		billerResponseType.setAmount("200");
		billerResponseType.setDueDate("2016-02-26");
		billerResponseType.setBillDate("2016-02-06");
		billerResponseType.setBillNumber("ABC128");
		billerResponseType.setBillPeriod("February");		
		
		
		/*BillerResponseType.Tag billerTag = new BillerResponseType.Tag();		
		billerTag.setName("Energy Charges");
		billerTag.setValue("100");		
		billerResponseType.getTags().add(billerTag);
		
		billerTag = new BillerResponseType.Tag();		
		billerTag.setName("MVCA");
		billerTag.setValue("500");		
		billerResponseType.getTags().add(billerTag);
		
		billerTag = new BillerResponseType.Tag();		
		billerTag.setName("Fixed Demand Charges");
		billerTag.setValue("600");		
		billerResponseType.getTags().add(billerTag);		*/
		billFetchResponse.setBillerResponse(billerResponseType);		
		
//		AdditionalInfoType additionalInfo = new AdditionalInfoType();
//		AdditionalInfoType.Tag additionalInfoTag = new AdditionalInfoType.Tag();
//		
//		additionalInfoTag.setName("Meter No");
//		additionalInfoTag.setValue("4242433");
//		additionalInfo.getTags().add(additionalInfoTag);
		
//		additionalInfoTag = new AdditionalInfoType.Tag();		
//		additionalInfoTag.setName("Meter Reading - Present");
//		additionalInfoTag.setValue("8051");
//		additionalInfo.getTags().add(additionalInfoTag);
//		
//		additionalInfoTag = new AdditionalInfoType.Tag();		
//		additionalInfoTag.setName("Meter Reading - Past");
//		additionalInfoTag.setValue("8073");
//		additionalInfo.getTags().add(additionalInfoTag);
//		
//		additionalInfoTag = new AdditionalInfoType.Tag();		
//		additionalInfoTag.setName("Rate Phase");
//		additionalInfoTag.setValue("01");
//		additionalInfo.getTags().add(additionalInfoTag);
//		billFetchResponse.setAdditionalInfo(additionalInfo);		
		
		TxnType txnType = new TxnType();
		txnType.setMsgId(billFetchRequest.getTxn().getMsgId());
		//txnType.setTxnReferenceId(billFetchRequest.getTxn().getTxnReferenceId());
		txnType.setTs(billFetchRequest.getTxn().getTs());
		billFetchResponse.setTxn(txnType);
		
		ReasonType reasonType = new ReasonType();
		reasonType.setApprovalRefNum("87654321");
		reasonType.setComplianceReason("");
		reasonType.setComplianceRespCd("");
		reasonType.setResponseCode("000");
		reasonType.setResponseReason("Successful");
		billFetchResponse.setReason(reasonType);
								
		return billFetchResponse;
	}
	
	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		//String plainCreds = getPropretyFiles("BASIC_AUTH_CREDENTIAL");
		String plainCreds = BASIC_AUTH_CREDENTIAL;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		headers.add("Authorization", "Basic " + base64Creds);

		return headers;
	}
	
	public static String getPropretyFiles(String key){
		String value=null;
        
        try {
        	Properties prop = new Properties();
	    	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	    	InputStream is = classloader.getResourceAsStream("BOU_Process.properties");
	    	prop.load(is);
			value=prop.getProperty(key);
		 } catch (IOException e) {
			 logger.error(e.getMessage());
		 }
        return value;
	}

	
}

