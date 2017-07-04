package com.rssoftware.ou.portal.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.CustomerDtlsType;
import org.bbps.schema.HeadType;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.QckPayType;
import org.bbps.schema.ReasonType;
import org.bbps.schema.SpltPayType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.TxnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.awl.merchanttoolkit.dto.ReqMsgDTO;
import com.awl.merchanttoolkit.dto.ResMsgDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.common.utils.WebAPIURL;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.domain.FinTransactionDetails;
import com.rssoftware.ou.portal.web.BillPayment;
import com.rssoftware.ou.portal.web.modal.BillInfoDTO;
import com.rssoftware.ou.portal.web.modal.ReceiptDTO;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;
import com.rssoftware.ou.portal.web.service.impl.PayByPNB;
import com.rssoftware.ou.portal.web.service.impl.PayBySIB;

@Controller
public class PGController {
	
	private final static Logger logger = LoggerFactory.getLogger(PGController.class);
	
	@Value("${ou.tenantId}")
	private String tenantId;
	
	@Value("${ou.domain}")
	private String uri;
	
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();
	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();
	
	@RequestMapping(value = "/pay")
	public String handleRequest(Model model, HttpServletRequest request) {
		
		//PaymentGatewayStrategy strategy = BeanLocator.getBean(PayByIDBI.class);
		//PaymentGatewayStrategy strategy = BeanLocator.getBean(PayByPNB.class);
		PaymentGatewayStrategy strategy = BeanLocator.getBean(PayBySIB.class);
		BillPayment billPayment = new BillPayment();
		billPayment.setStrategy(strategy);
		//BillFetchResponse billFetchResponse = (BillFetchResponse) request.getAttribute("billFetchResponse");
		String refId = "HENSVVR4QOS7X1UGPY7JGUV444PL9T2C3QM";//request.getParameter("refid");
		
		// Setting bill information into dto object.
		BillInfoDTO billInfoDTO = new BillInfoDTO();
		billInfoDTO.setBillAmount(request.getParameter("inputBillAmount"));
		billInfoDTO.setBillDate(request.getParameter("inputBillDate"));
		billInfoDTO.setBillerAmountOptions(request.getParameter("billerAmountOptions"));
		billInfoDTO.setBillerName(request.getParameter("inputBillerName"));
//		billInfoDTO.setBillNumber(request.getParameter("inputBillNumber"));
		billInfoDTO.setBillPeriod(request.getParameter("inputBillPeriod"));
		billInfoDTO.setCcfTax(request.getParameter("inputCCFTax"));
		billInfoDTO.setCustomerName(request.getParameter("inputCustomerName"));
		billInfoDTO.setDueDate(request.getParameter("inputDueDate"));
		billInfoDTO.setMeterNo(request.getParameter("inputMeterNo"));
		billInfoDTO.setMeterReadingPast(request.getParameter("inputMeterReadingPast"));
		billInfoDTO.setMeterReadingPresent(request.getParameter("inputMeterReadingPresent"));
		billInfoDTO.setPaymentChannel(request.getParameter("inputPaymentChannel"));
		billInfoDTO.setPaymentmethod(request.getParameter("paymentmethod"));
		billInfoDTO.setRefId(request.getParameter("inputRefId"));
//		billInfoDTO.setTotalAmtToPaid(request.getParameter("inputTotalAmtToPaid"));
		billInfoDTO.setTotalAmtToPaid("1.00");
		billInfoDTO.setBillNumber("123464");
		
		// Will be remove later.
//		if(billFetchResponse==null){
//			billFetchResponse = getBillFetchResponse();
//		}
		//----------------------
		
//		List<PGParam> pgParams = (List<PGParam>) billPayment.pay(billFetchResponse);
		PayBySIB.SIBReqDTO reqMsgDTO = (PayBySIB.SIBReqDTO) billPayment.pay(billInfoDTO);
		
				
		//ReqMsgDTO reqMsgDTO = (ReqMsgDTO) billPayment.pay(billInfoDTO);			
//		String merchantRequest = PayByIDBI.generateRequestMessageForMerchant(reqMsgDTO);//(String) billPayment.pay(billFetchResponse);
		
//		PayByPNB.PNBReqDTO reqMsgDTO = (PayByPNB.PNBReqDTO) billPayment.pay(billInfoDTO);
//		Key key;
		/*SecureRandom rand = new SecureRandom();
		KeyGenerator generator;
		String encdata = "";
		try {
			generator = KeyGenerator.getInstance("AES");
			generator.init(rand);
			generator.init(128);
			key = generator.generateKey();
			encdata = reqMsgDTO.getReqMsg(key);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//key = new SecretKeySpec(PayByPNB.ENCRIPTION_KEY.getBytes(), "AES");
//		String encdata = reqMsgDTO.getReqMsg(PayByPNB.ENCRIPTION_KEY);
		
		//String inputKey = PayByPNB.ENCRIPTION_KEY;
		//Cipher cipherEncrypt = Cipher.getInstance("AES");
	    //byte[] inputBytes = cipherEncrypt.doFinal(inputKey.getBytes());
		
		
		
//		model.addAttribute("encdata", encdata);
		// *** Entry a fin transaction data record of status PAYMENT_INITIATED.
//		HttpSession session = request.getSession();
		inseriFinTransactionRecord(request, reqMsgDTO, tenantId, refId, billInfoDTO);
		
//		model.addAttribute("payment_page_heading", "This page will be autometically submitted to IDBI's payment page.");
//		model.addAttribute("submit_url", PayByIDBI.SUBMIT_URL);
//		//model.addAttribute("pgParams", pgParams);
//		model.addAttribute("MID", PayByIDBI.MID);
//		model.addAttribute("merchantRequest", merchantRequest);
		
		return strategy.populateModel(model);
	}
	
	@RequestMapping(value = "/return")
	public String paySuccess(Model model, HttpServletRequest request) {
		
		Map<String, String> requestParams = new HashMap<>();
		Enumeration en = request.getParameterNames();
		while (en.hasMoreElements()) {
            String parameterName = (String) en.nextElement();
            String parameterValue = request.getParameter(parameterName);
            requestParams.put(parameterName, parameterValue);
        }
		
		PayBySIB objSIB = new PayBySIB();
		PayBySIB.SIBResDTO resMsgDtoObj = objSIB.new SIBResDTO();
		resMsgDtoObj.digest(requestParams);
		System.out.println(resMsgDtoObj);
		
		String merchantResponse = request.getParameter("merchantResponse");
		String encdata = request.getParameter("encdata");
		String PAID = request.getParameter("PAID");
		String ErrorMsg = request.getParameter("ErrorMsg");
		PayByPNB obj = new PayByPNB();
		PayByPNB.PNBResDTO resMsgDto = obj.new PNBResDTO().parseResMsg(encdata);
		//resMsgDto.parseResMsg(encdata);
//		logger.info("Parsed Message:"+resMsgDto.toString());
		merchantResponse = "06D86BE71B953AF3BB728CD216AA016E2AC2B51BF2EF52E63ECF9814D46BB8598D0270E5F48C67CBAF9087EB7687E1E58EAF8A2402ABADEB59E5F143A4EB770EBB54A96C62B40A9F7A7C9DF1BD20452CE9AC9D288B080CF82192CEB5F7D09D1FFD6E8CD99470A7CAD0FE6A7182BD6B0510208DD91EEBE93575B23BD65C25B499";
		model.addAttribute("receipt", generateReceipt());
		
		
		return "payment_receipt";
		/*if(merchantResponse != null) {
			// Initialise object to parse response message
			AWLMEAPI transactMeAPI = new AWLMEAPI();

			// Call method to parse PG transaction response
			try {
				ResMsgDTO resMsgDTO = transactMeAPI.parseTrnResMsg(merchantResponse, PayByIDBI.ENCRIPTION_KEY);
				if(resMsgDTO !=null && "S".equals(resMsgDTO.getStatusCode())) {
					// Status success
					
					// *** Update a fin transaction data record of status PAYMENT_SUCCESS.
					updateFinTransactionRecord(resMsgDTO, tenantId);
					
					// Basic authentication credentials.
					String plainCreds = "agent1:password";//CommonConstants.getPropretyFiles("BASIC_AUTH_CREDENTIAL");//"agent1:password";
					byte[] plainCredsBytes = plainCreds.getBytes();
					byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
					String base64Creds = new String(base64CredsBytes);

					// Adding basic authentication in the header
					HttpHeaders headers = new HttpHeaders();
					headers.add("Authorization", "Basic " + base64Creds);
					
					BillPaymentRequest billPaymentRequest = (BillPaymentRequest) session.getAttribute("billPaymentRequest");
					
					// Will be remove later.
					if(billPaymentRequest==null){
						billPaymentRequest = getBillPaymentRequest();
					}
					//----------------------

					// API URL for this function.					
					String apiURL = WebAPIURL.PROCESS_PG_PAYMENT_URL + tenantId;

					try {
						
						HttpEntity httpEntity = new HttpEntity(billPaymentRequest, headers);
						ResponseEntity<BillPaymentResponse> responseEntity = internalRestTemplate.postForEntity(apiURL, httpEntity, BillPaymentResponse.class);
						BillPaymentResponse billPaymentResponse = responseEntity.getBody();
						
						// Validate billpaymentresponse and if validated then generate receipt.
						model.addAttribute("receipt", generateReceipt());
						
					} catch (Exception e) {
						// System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}else{
					// Status failure
					// *** Entry a transaction data record of status PAYMENT_FAILURE.
					// To Do
					logger.info("Status Code:" + resMsgDTO.getStatusCode() + " Status Description:" + resMsgDTO.getStatusDesc());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			// No response found.
			
		}
		
		
		
		
		return "payment_receipt";*/
	}
	
	/*private boolean validateMerchantResponse(String merchantResponse, String encKey) {
		
		// Initialise object to parse response message
		AWLMEAPI transactMeAPI = new AWLMEAPI();

		// Call method to parse PG transaction response
		resMsgDTO = transactMeAPI.parseTrnResMsg(merchantResponse, enc_key);
		
		return false;
		
	}
	
	private ResMsgDTO getResMsgDTO(String merchantResponse) {

		// Initialise object to parse response message
		AWLMEAPI transactMeAPI = new AWLMEAPI();

		// Call method to parse PG transaction response
		try {
			return transactMeAPI.parseTrnResMsg(merchantResponse, ENCRIPTION_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	
	private ReceiptDTO generateReceipt() {
		ReceiptDTO receipt=new ReceiptDTO();
		receipt.setCustomerName("Somnath");
		receipt.setCustomerMobileNumber("9836385751");
		receipt.setCustomerAccountNumber("12");
		receipt.setCustomerBillDate("2016-02-06");
		receipt.setCustomerBillAmount("2.00");
		receipt.setCustomerTotalAmount("2.12");
		receipt.setCustomerConvFee("0.12");
		receipt.setCustomerPaymentChannel("IDBI Mumbai");
		receipt.setCustomerPaymentMode("Cash");
		receipt.setCustomerAuthCode("1483526987");
		receipt.setCustomerDateTime("12-30-2016, 05:16:12 PM");
		receipt.setCustomerTransactionRefId("OU08090036524");
		 return receipt;
	}	
private boolean inseriFinTransactionRecord(HttpServletRequest request, PayBySIB.SIBReqDTO reqMsgDTO, String tenantId, String refId, BillInfoDTO billInfoDTO) {
		
		boolean flag = false;
		
		// Basic authentication credentials.
		String plainCreds = "agent1:password";//CommonConstants.getPropretyFiles("BASIC_AUTH_CREDENTIAL");//"agent1:password";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		// Adding basic authentication in the header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
		
		// API URL for this function.					
		String uniqueIdURL = uri + WebAPIURL.GET_UNIQUE_ID_URL;
		HttpEntity httpEntity = new HttpEntity(refId, headers);
		ResponseEntity<String> responseEntity = internalRestTemplate.exchange(uniqueIdURL, HttpMethod.POST, httpEntity, String.class, tenantId);
		String uniqueId = responseEntity.getBody();
		// setting the txn reference id in the order id.
		//reqMsgDTO.setOrderId(uniqueId);
		reqMsgDTO.setPID(uniqueId);
		
		// API URL for this function.					
		String insertURL = uri + WebAPIURL.INSERT_FIN_TRANSACTION_RECORD_URL;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			FinTransactionDetails finTransactionDetails = new FinTransactionDetails();
			finTransactionDetails.setRefId(refId);
			finTransactionDetails.setTxnRefId(uniqueId);
			finTransactionDetails.setClientTxnRefId("12345");
			finTransactionDetails.setPaymentAmountDetails("100");
			finTransactionDetails.setRequestJson(mapper.writeValueAsBytes(reqMsgDTO));
			finTransactionDetails.setResponseJson(mapper.writeValueAsBytes(billInfoDTO));
			finTransactionDetails.setStatus(RequestStatus.SENT.name());
			finTransactionDetails.setTotalAmount(reqMsgDTO.getAMT());
			
			httpEntity = new HttpEntity(finTransactionDetails, headers);
			responseEntity = internalRestTemplate.exchange(insertURL, HttpMethod.POST, httpEntity, String.class, tenantId);
			flag = true;
		} catch (Exception e) {
			logger.info("Fin transaction not inserted.");
			logger.error(e.getMessage(), e);
		}
		
		return flag;
	}
	
	private boolean updateFinTransactionRecord(ResMsgDTO resMsgDTO, String tenantId) {

		boolean flag = false;

		// Basic authentication credentials.
		String plainCreds = "agent1:password";//CommonConstants.getPropretyFiles("BASIC_AUTH_CREDENTIAL");// "agent1:password";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		// Adding basic authentication in the header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
		FinTransactionData finTransactionData = null;
		ObjectMapper mapper = new ObjectMapper();
		
		// Get
		String apiGetURL = uri+ WebAPIURL.GET_FIN_TRANSACTION_RECORD_URL;
		try {
			HttpEntity httpEntity = new HttpEntity(resMsgDTO.getOrderId(), headers);
			ResponseEntity<FinTransactionData> responseEntity = internalRestTemplate.exchange(apiGetURL, HttpMethod.POST, httpEntity,
					FinTransactionData.class, tenantId);
			finTransactionData = (FinTransactionData) responseEntity.getBody();
			if(finTransactionData != null) {
				finTransactionData.setResponseJson(mapper.writeValueAsBytes(resMsgDTO));
				finTransactionData.setCurrentStatus(RequestStatus.RESPONSE_SUCCESS.name());
				finTransactionData.setUpdtTs(new Timestamp(System.currentTimeMillis()));
				finTransactionData.setAuthCode(resMsgDTO.getAuthZCode());
			}
		} catch (Exception e) {
			logger.info("Fin transaction not present.");
			logger.error(e.getMessage(), e);
		}
		

		// API URL for this function.
		String apiURL = uri + WebAPIURL.UPDATE_FIN_TRANSACTION_RECORD_URL;

		try {
			HttpEntity httpEntity = new HttpEntity(finTransactionData, headers);
			ResponseEntity<String> responseEntity = internalRestTemplate.exchange(apiURL, HttpMethod.POST, httpEntity,
					String.class, tenantId);
			String responseMessage = responseEntity.getBody();
			if ("Success".equals(responseMessage)) {
				flag = true;
				logger.info("Fin transaction inserted.");
			}
		} catch (Exception e) {
			logger.info("Fin transaction not inserted.");
			logger.error(e.getMessage(), e);
		}

		return flag;
	}
	
	private BillPaymentRequest getBillPaymentRequest() {

		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
		HeadType headType = new HeadType();
		headType.setOrigInst("OU06");
		headType.setRefId("HENSVVR4QOS7X1UGPY7JGUV444PL9T2C3QM");
		headType.setTs("2015-02-16T22:02:35+05:30");
		headType.setVer("1.0");		
		billPaymentRequest.setHead(headType);
		
		TxnType txnType = new TxnType();
		txnType.setTxnReferenceId("OU0156789123");
		txnType.setTs("2015-02-16T22:02:35+05:30");
		txnType.setType(TransactionType.FORWARD_TYPE_REQUEST.name());
		txnType.setMsgId("8ENSVVR4QOS7X1UGPY7JGUV444PL9T2C3QY");
		billPaymentRequest.setTxn(txnType);
		
		CustomerDtlsType custDtlType = new CustomerDtlsType();
		custDtlType.setMobile("9830098300");
		CustomerDtlsType.Tag tag1 = new CustomerDtlsType.Tag();
		tag1.setName("EMAIL");
		tag1.setValue("nalin@npci.org.in");
		CustomerDtlsType.Tag tag2 = new CustomerDtlsType.Tag();
		tag1.setName("AADHAAR");
		tag1.setValue("123456789012");
		CustomerDtlsType.Tag tag3 = new CustomerDtlsType.Tag();
		tag1.setName("PAN");
		tag1.setValue("BXXCG7754K");
		custDtlType.getTags().add(tag1);
		custDtlType.getTags().add(tag2);
		custDtlType.getTags().add(tag3);
		billPaymentRequest.setCustomer(custDtlType);
		
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setQuickPay(QckPayType.NO);
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setPaymentMode("Credit Card");
		billPaymentRequest.setPaymentMethod(pmtMtdType);
		
		BillerResponseType billerResponse = new BillerResponseType();
		billerResponse.setAmount("1.00");
		billerResponse.setBillDate(new Date(System.currentTimeMillis()).toString());
		billerResponse.setBillNumber("1232332");
		billerResponse.setBillPeriod("January");
		billerResponse.setCustConvDesc("Customer Service Fee");
		billerResponse.setCustConvFee("100");
		billerResponse.setCustomerName("Arnab Sinha");
		billerResponse.setDueDate(new Date(System.currentTimeMillis()).toString());

		BillerResponseType.Tag tag4 = new BillerResponseType.Tag();
		tag1.setName("Amount 1");
		tag1.setValue("2000");
		BillerResponseType.Tag tag5 = new BillerResponseType.Tag();
		tag2.setName("Amount 2");
		tag2.setValue("3000");
		billerResponse.getTags().add(tag4);
		billerResponse.getTags().add(tag5);
		billPaymentRequest.setBillerResponse(billerResponse);
		
		return billPaymentRequest;
	}

	private BillFetchResponse getBillFetchResponse() {
		BillFetchResponse billFetchResponse = new BillFetchResponse();

		BillDetailsType billDetailsType = null;
		//billDetailsType.setBiller(null);
		billFetchResponse.setBillDetails(billDetailsType);
		
		HeadType headType = new HeadType();
		headType.setOrigInst("OU06");
		headType.setRefId("HENSVVR4QOS7X1UGPY7JGUV444PL9T2C3QM");
		headType.setTs("2015-02-16T22:02:35+05:30");
		headType.setVer("1.0");
		billFetchResponse.setHead(headType);

		ReasonType reasonType = new ReasonType();
		reasonType.setApprovalRefNum("AB123456");
		reasonType.setComplianceReason("Date and Time incorrect.");
		reasonType.setComplianceRespCd("022");
		reasonType.setResponseCode("000");
		reasonType.setResponseReason("Successful");
		reasonType.setValue(null);
		billFetchResponse.setReason(reasonType);

		TxnType txnType = null;
		//txnType.setRiskScores(null);
		billFetchResponse.setTxn(txnType);
		billFetchResponse.setBillerResponse(null);

		BillerResponseType billerResponse = new BillerResponseType();
		billerResponse.setAmount("1.00");
		billerResponse.setBillDate(new Date(System.currentTimeMillis()).toString());
		billerResponse.setBillNumber("1232332");
		billerResponse.setBillPeriod("January");
		billerResponse.setCustConvDesc("Customer Service Fee");
		billerResponse.setCustConvFee("100");
		billerResponse.setCustomerName("Arnab Sinha");
		billerResponse.setDueDate(new Date(System.currentTimeMillis()).toString());

		BillerResponseType.Tag tag1 = new BillerResponseType.Tag();
		tag1.setName("Amount 1");
		tag1.setValue("2000");
		BillerResponseType.Tag tag2 = new BillerResponseType.Tag();
		tag2.setName("Amount 2");
		tag2.setValue("3000");
		billerResponse.getTags().add(tag1);
		billerResponse.getTags().add(tag2);
		billFetchResponse.setBillerResponse(billerResponse);

		AdditionalInfoType additionalInfo = new AdditionalInfoType();
		AdditionalInfoType.Tag tag3 = new AdditionalInfoType.Tag();
		tag3.setName("BlRspFld1");
		tag3.setValue("");
		AdditionalInfoType.Tag tag4 = new AdditionalInfoType.Tag();
		tag4.setName("BlRspFld2");
		tag4.setValue("");
		additionalInfo.getTags().add(tag3);
		additionalInfo.getTags().add(tag4);
		billFetchResponse.setAdditionalInfo(additionalInfo);

		return billFetchResponse;
	}

}
