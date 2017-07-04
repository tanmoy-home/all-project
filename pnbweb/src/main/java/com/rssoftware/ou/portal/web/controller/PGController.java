package com.rssoftware.ou.portal.web.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.NotificationEventType;
import com.rssoftware.ou.common.NotificationType;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.common.utils.WebAPIURL;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.FinTransactionDetails;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.model.tenant.SmsMessageView.SMSType;
import com.rssoftware.ou.portal.web.BillPayment;
import com.rssoftware.ou.portal.web.dto.BillInfoDTO;
import com.rssoftware.ou.portal.web.dto.ReceiptDTO;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;
import com.rssoftware.ou.portal.web.service.impl.PayByPNB;
import com.rssoftware.ou.portal.web.service.impl.PayByPNB.PNBResDTO;
import com.rssoftware.ou.portal.web.utils.Utility;
import com.rssoftware.ou.tenant.service.EmailService;
import com.rssoftware.ou.tenant.service.SmsService;

import in.co.rssoftware.bbps.schema.AgentType;
import in.co.rssoftware.bbps.schema.BillDetailsType;
import in.co.rssoftware.bbps.schema.BillerType;
import in.co.rssoftware.bbps.schema.CustomerDtlsType;
import in.co.rssoftware.bbps.schema.CustomerParamsType;
import in.co.rssoftware.bbps.schema.DeviceType;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.PayAmountType;
import in.co.rssoftware.bbps.schema.PaymentReceipt;
import in.co.rssoftware.bbps.schema.PaymentRequest;
import in.co.rssoftware.bbps.schema.PmtMtdType;
import in.co.rssoftware.bbps.schema.PymntInfType;
import in.co.rssoftware.bbps.schema.QckPayType;
import in.co.rssoftware.bbps.schema.SmsType;
import in.co.rssoftware.bbps.schema.SpltPayType;

@Controller
public class PGController {

	private final static Logger logger = LoggerFactory.getLogger(PGController.class);

	@Value("${ou.tenantId}")
	private String tenantId;

	@Value("${ou.domain}")
	private String uri;

	@Autowired
	private SmsService smsService;
	

	@Autowired
	private EmailService emailService;

	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();

	@RequestMapping(value = "/pay")
	public String handleRequest(Model model, HttpServletRequest request) {

		PaymentGatewayStrategy strategy = BeanLocator.getBean(PayByPNB.class);
		BillPayment billPayment = new BillPayment();
		billPayment.setStrategy(strategy);
		String refId = request.getParameter("refid");
		BillInfoDTO billInfoDTO = new BillInfoDTO();
		if (refId != null && !refId.isEmpty()) {
			// Setting bill information into dto object.

			billInfoDTO.setBillAmount(request.getParameter("inputBillAmount"));
			billInfoDTO.setBillDate(request.getParameter("inputBillDate"));
			billInfoDTO.setBillerAmountOptions(request.getParameter("billerAmountOptions"));
			billInfoDTO.setBillerName(request.getParameter("inputBillerName"));
			billInfoDTO.setBillNumber(request.getParameter("inputBillNumber"));
			billInfoDTO.setBillPeriod(request.getParameter("inputBillPeriod"));
			billInfoDTO.setCcfTax(request.getParameter("inputCCFTax"));
			billInfoDTO.setCustomerName(request.getParameter("inputCustomerName"));
			billInfoDTO.setDueDate(request.getParameter("inputDueDate"));
			billInfoDTO.setMeterNo(request.getParameter("inputMeterNo"));
			billInfoDTO.setMeterReadingPast(request.getParameter("inputMeterReadingPast"));
			billInfoDTO.setMeterReadingPresent(request.getParameter("inputMeterReadingPresent"));
			billInfoDTO.setPaymentChannel(request.getParameter("inputPaymentChannel"));
			billInfoDTO.setPaymentmethod(request.getParameter("paymentmethod"));
			billInfoDTO.setRefId(refId);
			billInfoDTO.setCustomerEmail(request.getParameter("inputEmail"));
			billInfoDTO.setCustomerMobile(request.getParameter("inputMobile"));
			billInfoDTO.setBillerId(request.getParameter("billerId"));
			billInfoDTO.setAgentId(request.getParameter("agentId"));
			billInfoDTO.setTotalAmtToPaid(request.getParameter("inputTotalAmtToPaid"));
			billInfoDTO.setCustomerInfo(new HashMap<>());
			String customerParam = request.getParameter("customparam");
			
			String[] params = customerParam.split("@");
			for (String singleparam : params) {
				String[] paramsvlaue = singleparam.split(":");
				if (!paramsvlaue[1].isEmpty()) {
					billInfoDTO.getCustomerInfo().put(paramsvlaue[0], paramsvlaue[1]);
				}

			}
			billInfoDTO.setBillerTagInfo(new HashMap<>());
			String billTagInfo = request.getParameter("billerResponseTags");
			if(billTagInfo!=null&&!billTagInfo.isEmpty())
			{
			String[] tagParams = billTagInfo.split("@");
			for (String singleparam : tagParams) {
				String[] paramsvlaue = singleparam.split(":");
				if (!paramsvlaue[1].isEmpty()) {
					billInfoDTO.getBillerTagInfo().put(paramsvlaue[0], paramsvlaue[1]);
				}

			}
			}
			
		} else {

			billInfoDTO.setBillAmount(request.getParameter("quickPayAmount"));
			billInfoDTO.setCcfTax(request.getParameter("quickPayCCF"));
			billInfoDTO.setPaymentChannel(request.getParameter("quickPaymentChannel"));
			billInfoDTO.setPaymentmethod(request.getParameter("quickPaymentmethod"));
			billInfoDTO.setCustomerEmail(request.getParameter("quickinputEmail"));
			billInfoDTO.setCustomerMobile(request.getParameter("quickinputMobile"));
			billInfoDTO.setBillerId(request.getParameter("quickbillerId"));
			billInfoDTO.setAgentId(request.getParameter("quickagentId"));
			billInfoDTO.setTotalAmtToPaid(request.getParameter("quickPayTotal"));
			billInfoDTO.setCustomerInfo(new HashMap<>());
			String customerParam = request.getParameter("quickcustomparam");
			String[] params = customerParam.split("@");
			for (String singleparam : params) {
				String[] paramsvlaue = singleparam.split(":");
				if (!paramsvlaue[1].isEmpty()) {
					billInfoDTO.getCustomerInfo().put(paramsvlaue[0], paramsvlaue[1]);
				}

			}

		}

		PayByPNB.PNBReqDTO reqMsgDTO = (PayByPNB.PNBReqDTO) billPayment.pay(billInfoDTO);

		insertFinTransactionRecord(reqMsgDTO, refId, billInfoDTO);
		return strategy.populateModel(model);
	}

	@RequestMapping(value = "/pg-response")
	public String paySuccess(Model model, HttpServletRequest request) {
		String returnPage = null;
		BillInfoDTO billInfoDTO = null;
		Map<String, String> requestParams = new HashMap<>();
		Enumeration en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String parameterName = (String) en.nextElement();
			String parameterValue = request.getParameter(parameterName);
			requestParams.put(parameterName, parameterValue);
		}

		PayByPNB objSIB = new PayByPNB();
		PayByPNB.PNBResDTO resMsgDTO = objSIB.new PNBResDTO();
		resMsgDTO.digest(requestParams);

		System.out.println("Success Status from PG: " + resMsgDTO.getBankstatus());
		billInfoDTO = updateFinTransactionRecord(resMsgDTO);
		boolean pgSuccess = "S".equalsIgnoreCase(resMsgDTO.getBankstatus()) ? true : false;
		if (pgSuccess) {
			// API URL for this function.
			String apiURL = uri + WebAPIURL.BILL_PAYMENT_URL;
			PaymentRequest paymentRequest = getPaymentRequest(billInfoDTO, resMsgDTO.getBanktransid());
			System.out.println("****************************Sending to API********************");
			LogUtils.logReqRespMessage(paymentRequest, billInfoDTO.getRefId(), Action.PAYMENT_REQUEST);
			try {
				ResponseEntity<PaymentReceipt> responseEntity = internalRestTemplate.postForEntity(apiURL,
						Utility.getHttpEntityForPost(paymentRequest), PaymentReceipt.class, tenantId);
				PaymentReceipt paymentReceipt = responseEntity.getBody();
				if (CollectionUtils.isNotEmpty(paymentReceipt.getErrorMessages())) {
					// put errors in paymentError page
					String errorMsg="";
					for(ErrorMessage errorMessage:paymentReceipt.getErrorMessages())
					{
						errorMsg+=" Error Code "+errorMessage.getErrorCd()+" <br> Error Message "+errorMessage.getErrorDtl()+"<br>";
					}
					model.addAttribute("errormsg", errorMsg);
					returnPage = "payment_error";
				} else {
					// Validate billpaymentresponse and if validated then
					// generate
					// receipt.
					ReceiptDTO receiptDTO = generateReceipt(paymentReceipt, resMsgDTO.getBanktransid(),
							paymentRequest.getBillDetails().getCustomerParams().getTags().get(0));
					model.addAttribute("receipt", receiptDTO);
					returnPage = "payment_receipt";
					saveSms(billInfoDTO);
					if (StringUtils.isNotEmpty(billInfoDTO.getCustomerEmail())) {
						sendEmail(receiptDTO, billInfoDTO.getCustomerEmail());
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				logger.debug(e.getMessage(), e);
				returnPage = "payment_error";
			}

		} else {
			returnPage = "payment_error";
		}
		return returnPage;
	}

	/*
	 * private boolean validateMerchantResponse(String merchantResponse, String
	 * encKey) {
	 * 
	 * // Initialise object to parse response message AWLMEAPI transactMeAPI =
	 * new AWLMEAPI();
	 * 
	 * // Call method to parse PG transaction response resMsgDTO =
	 * transactMeAPI.parseTrnResMsg(merchantResponse, enc_key);
	 * 
	 * return false;
	 * 
	 * }
	 * 
	 * private ResMsgDTO getResMsgDTO(String merchantResponse) {
	 * 
	 * // Initialise object to parse response message AWLMEAPI transactMeAPI =
	 * new AWLMEAPI();
	 * 
	 * // Call method to parse PG transaction response try { return
	 * transactMeAPI.parseTrnResMsg(merchantResponse, ENCRIPTION_KEY); } catch
	 * (Exception e) { e.printStackTrace(); } return null; }
	 */

	private ReceiptDTO generateReceipt(PaymentReceipt paymentReceipt, String authCode,
			in.co.rssoftware.bbps.schema.CustomerParamsType.Tag tag) {
		ReceiptDTO receipt = new ReceiptDTO();
		receipt.setCustomerName(paymentReceipt.getCustomerName());
		receipt.setCustomerMobileNumber(paymentReceipt.getCustomerMobile());
		receipt.setCustomerAccountNumber(tag.getValue());
		receipt.setCustomerBillDate(CommonUtils.getFormattedEffectiveDates("yyyy-mm-dd","dd-mm-yyyy",paymentReceipt.getBillDate().toString()));
		receipt.setCustomerBillAmount(CommonUtils.convertPaisaToRupeesAsString(paymentReceipt.getAmount()));
		receipt.setCustomerTotalAmount(getTotalAmount(paymentReceipt.getAmount(), paymentReceipt.getCcf()));
		receipt.setCustomerConvFee(CommonUtils.convertPaisaToRupeesAsString(paymentReceipt.getCcf()));
		receipt.setCustomerPaymentChannel(paymentReceipt.getPaymentChannel());
		receipt.setCustomerPaymentMode(paymentReceipt.getPaymentMode());
		receipt.setCustomerAuthCode(authCode);
		receipt.setCustomerDateTime(CommonUtils.formaterdd_MM_YYYY_HH_MM_A.print(new DateTime(Calendar.getInstance())));
		receipt.setCustomerTransactionRefId(paymentReceipt.getTransactionRefId());
		receipt.setBillerName(paymentReceipt.getBillerName());
		receipt.setBillerId(paymentReceipt.getBillerId());
		receipt.setTransactionStatus("Success");
		receipt.setBillIdentificationParam(tag.getName());
		return receipt;
	}

	private String getTotalAmount(String amount, String ccf) {
		BigDecimal total = new BigDecimal(amount).add(new BigDecimal(ccf));
		return CommonUtils.convertPaisaToRupeesAsString(total.toPlainString());
	}

	private boolean insertFinTransactionRecord(PayByPNB.PNBReqDTO reqMsgDTO, String refId, BillInfoDTO billInfoDTO) {

		boolean flag = false;

		// // Basic authentication credentials.
		// String plainCreds = "agent1:password";//
		// CommonConstants.getPropretyFiles("BASIC_AUTH_CREDENTIAL");//"agent1:password";
		// byte[] plainCredsBytes = plainCreds.getBytes();
		// byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		// String base64Creds = new String(base64CredsBytes);
		//
		// // Adding basic authentication in the header
		// HttpHeaders headers = new HttpHeaders();
		// headers.add("Authorization", "Basic " + base64Creds);

		// API URL for this function.
		String uniqueIdURL = uri + WebAPIURL.GET_UNIQUE_ID_URL;
		// HttpEntity httpEntity = new HttpEntity(refId, headers);
		ResponseEntity responseEntity = internalRestTemplate.exchange(uniqueIdURL, HttpMethod.GET,
				Utility.getHttpEntityForGet(), String.class, tenantId);
		String txnRefId = (String) responseEntity.getBody();
		// setting the txn reference id in the order id.
		// reqMsgDTO.setOrderId(uniqueId);
		reqMsgDTO.setCin(txnRefId);
//		reqMsgDTO.setITC(txnRefId.substring(4)); // setting ITC as txnRefId
													// without the tenantId
													// portion

		// API URL for this function.
		String insertURL = uri + WebAPIURL.INSERT_FIN_TRANSACTION_RECORD_URL;

		try {
			ObjectMapper mapper = new ObjectMapper();
			FinTransactionDetails finTransactionDetails = new FinTransactionDetails();
			finTransactionDetails.setRefId(refId);
			finTransactionDetails.setTxnRefId(txnRefId);
			// finTransactionDetails.setClientTxnRefId("12345");
			finTransactionDetails.setPaymentAmountDetails(billInfoDTO.getBillerAmountOptions());
			finTransactionDetails.setRequestJson(mapper.writeValueAsBytes(reqMsgDTO));
			finTransactionDetails.setResponseJson(mapper.writeValueAsBytes(billInfoDTO));
			finTransactionDetails.setStatus(RequestStatus.SENT.name());
			finTransactionDetails.setTotalAmount(
					String.valueOf(new BigDecimal(reqMsgDTO.getAmount()).multiply(CommonConstants.HUNDRED).longValue()));

			responseEntity = internalRestTemplate.exchange(insertURL, HttpMethod.POST,
					Utility.getHttpEntityForPost(finTransactionDetails), String.class, tenantId);
			flag = true;
		} catch (Exception e) {
			logger.info("Fin transaction not inserted.");
			logger.error(e.getMessage(), e);
		}

		return flag;
	}

	private BillInfoDTO updateFinTransactionRecord(PNBResDTO resMsgDTO) {

		boolean flag = false;

		BillInfoDTO billInfoDTO = null;

		FinTransactionData finTransactionData = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		// Get
		String apiGetURL = uri + WebAPIURL.GET_FIN_TRANSACTION_RECORD_URL;
		try {
			ResponseEntity responseEntity = internalRestTemplate.exchange(apiGetURL, HttpMethod.POST,
					Utility.getHttpEntityForPost(resMsgDTO.getCin()), FinTransactionData.class, tenantId);
			finTransactionData = (FinTransactionData) responseEntity.getBody();
			billInfoDTO = mapper.readValue(finTransactionData.getResponseJson(), BillInfoDTO.class);
			if (finTransactionData != null) {
				finTransactionData.setResponseJson(mapper.writeValueAsBytes(resMsgDTO));
				if ("S".equalsIgnoreCase(resMsgDTO.getBankstatus())) {
					finTransactionData.setCurrentStatus(RequestStatus.RESPONSE_SUCCESS.name());
				} else {
					finTransactionData.setCurrentStatus(RequestStatus.RESPONSE_FAILURE.name());
				}
				finTransactionData.setUpdtTs(new Timestamp(System.currentTimeMillis()));
				finTransactionData.setAuthCode(resMsgDTO.getBanktransid());
			}
		} catch (Exception e) {
			logger.info("Fin transaction not present.");
			logger.error(e.getMessage(), e);
		}

		// API URL for this function.
		String apiURL = uri + WebAPIURL.UPDATE_FIN_TRANSACTION_RECORD_URL;

		try {

			ResponseEntity responseEntity = internalRestTemplate.exchange(apiURL, HttpMethod.POST,
					Utility.getHttpEntityForPost(finTransactionData), String.class, tenantId);
			String responseMessage = (String) responseEntity.getBody();
			if ("Success".equals(responseMessage)) {
				flag = true;
				logger.info("Fin transaction response updated.");
			}
		} catch (Exception e) {
			logger.info("Fin transaction response not updated.");
			logger.error(e.getMessage(), e);
		}

		return billInfoDTO;
	}

	private PaymentRequest getPaymentRequest(BillInfoDTO billInfo, String txnRefId) {

		PaymentRequest paymentRequest = new PaymentRequest();
		boolean quickPay = StringUtils.isNotBlank(billInfo.getRefId()) ? false : true;
		paymentRequest.setRefId(billInfo.getRefId());
		paymentRequest.setTxnRefId(txnRefId);

		CustomerDtlsType custDtlType = new CustomerDtlsType();
		custDtlType.setMobile(billInfo.getCustomerMobile());

		if (StringUtils.isNotEmpty(billInfo.getCustomerEmail())) {
			CustomerDtlsType.Tag tag1 = new CustomerDtlsType.Tag();
			tag1.setName("EMAIL");
			tag1.setValue("nalin@npci.org.in");
			custDtlType.getTags().add(tag1);
		}
		paymentRequest.setCustomer(custDtlType);

		PmtMtdType pmtMtdType = new PmtMtdType();
		if (quickPay) {
			pmtMtdType.setQuickPay(QckPayType.YES);
		} else {
			pmtMtdType.setQuickPay(QckPayType.NO);
		}
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setPaymentMode(billInfo.getPaymentmethod());
		pmtMtdType.setPaymentMode(billInfo.getPaymentmethod());
		paymentRequest.setPaymentMethod(pmtMtdType);

		//billInfo.setPaymentChannel(PaymentChannel.Internet_Banking.getExpandedForm());
		billInfo.setPaymentChannel(billInfo.getPaymentChannel());
		AgentType agentType = new AgentType();
		agentType.setId(billInfo.getAgentId());
		DeviceType deviceType = AgentWebController.getDeviceForINT(billInfo.getPaymentChannel());
		agentType.setDevice(deviceType);
		paymentRequest.setAgent(agentType);

		BillDetailsType billDetailsType = new BillDetailsType();
		BillerType biller = new BillerType();
		biller.setId(billInfo.getBillerId());
		billDetailsType.setBiller(biller);
		CustomerParamsType customerParamsType = new CustomerParamsType();
		billDetailsType.setCustomerParams(customerParamsType);
		for (Entry<String, String> entry : billInfo.getCustomerInfo().entrySet()) {
			CustomerParamsType.Tag tag = new CustomerParamsType.Tag();
			tag.setName(entry.getKey());
			tag.setValue(entry.getValue());
			billDetailsType.getCustomerParams().getTags().add(tag);
		}
		paymentRequest.setBillDetails(billDetailsType);

		System.out.println("Total Amt: " + billInfo.getTotalAmtToPaid() + "CCF: " + billInfo.getCcfTax());
		PayAmountType amount = new PayAmountType();
		String billAmt = (new BigDecimal(billInfo.getTotalAmtToPaid()).subtract(new BigDecimal(billInfo.getCcfTax())))
				.multiply(CommonConstants.HUNDRED).setScale(0, RoundingMode.HALF_UP).toPlainString();
		System.out.println("Calculated Bill amount: " + billAmt);
		amount.setAmount(billAmt);
		amount.setCustomerConvinienceFee(CommonUtils.getCCFAsStringWithOutDecimalPoint(billInfo.getCcfTax()));
		amount.setSplitPayAmount(CommonConstants.ZERO_VALUE_AS_STRING);
		if (!quickPay) {
			getAmountTags(amount, billInfo);
		}
		paymentRequest.setAmount(amount);

		PymntInfType paymentInfo = new PymntInfType();
		PymntInfType.Tag payTag = new PymntInfType.Tag();
		payTag.setName(CommonConstants.PAYMENT_INFO_IFSC_AC);
		payTag.setValue("SIBN0000000|120123121");
		paymentInfo.getTags().add(payTag);
		paymentRequest.setPaymentInformation(paymentInfo);

		return paymentRequest;
	}

	private void getAmountTags(PayAmountType amount, BillInfoDTO billinfo) {
		String[] amtBreakup = billinfo.getBillerAmountOptions().split("\\|");
		Map<String, String> incomingPaymentAmounts = billinfo.getBillerTagInfo();

		if (amtBreakup.length > 1) {

			for (int i = 1; i < amtBreakup.length; i++) {
				String amountComponent = amtBreakup[i];
				if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountComponent)) {
					continue;
				}
				if (incomingPaymentAmounts.get(amountComponent) != null) {
					PayAmountType.Tag tag = new PayAmountType.Tag();
					tag.setName(amountComponent);
					tag.setValue(incomingPaymentAmounts.get(amountComponent));
					amount.getTags().add(tag);
				}
			}
		}
	}
	
	private void saveSms(BillInfoDTO billInfoDTO) {
		String apiUrl = uri + WebAPIURL.GET_NOTIFICATION_TEMPLATE_URL;

		ResponseEntity<?> responseEntity = internalRestTemplate.exchange(apiUrl, HttpMethod.GET,
				Utility.getHttpEntityForGet(), String.class, tenantId, NotificationType.SMS.name(), null);

		String notificationTemplate = (String) responseEntity.getBody();

		String notificationMsg = notificationTemplate
				.replace(CommonConstants.TEMPLATE_TXN_ID, billInfoDTO.getRefId())
				.replace(CommonConstants.TEMPLATE_DATE, CommonUtils.formaterdd_MM_YYYY_HH_MM_A.print(new DateTime()))
				.replace(CommonConstants.TEMPLATE_BENEFICIARY_NAME, billInfoDTO.getBillerName())
				.replace(CommonConstants.TEMPLATE_TRANSACTION_STATE, NotificationEventType.TRANSACTION_SUCCESS.name())
				.replace(CommonConstants.TEMPLATE_TXN_AMOUNT, billInfoDTO.getTotalAmtToPaid())
				.replace(CommonConstants.TEMPLATE_BILLER_NAME, billInfoDTO.getBillerName())
				.replace(CommonConstants.TEMPLATE_CONSUMER_NO,
						billInfoDTO.getCustomerInfo().entrySet().iterator().next().getValue())
				.replace(CommonConstants.TEMPLATE_PAYMENT_CHANNEL, billInfoDTO.getPaymentChannel());

		SmsType smsType = new SmsType();
		smsType.setMessage(notificationMsg);
		smsType.setMobileNo(billInfoDTO.getCustomerMobile());
		smsType.setType(SMSType.RECEIPT.name());
		smsType.setStatus("U");

		apiUrl = uri + WebAPIURL.SAVEANDSEND_SMS_URL;
		responseEntity = internalRestTemplate.postForEntity(apiUrl, Utility.getHttpEntityForPost(smsType), String.class,
				tenantId);
		String status = (String)responseEntity.getBody();
		System.out.println("SMS " + status);

	}
	
	private void sendEmail(ReceiptDTO receiptDTO, String email) {
		String apiUrl = uri + WebAPIURL.GET_NOTIFICATION_TEMPLATE_URL;
		ResponseEntity<?> responseEntity = internalRestTemplate.exchange(apiUrl, HttpMethod.GET,
				Utility.getHttpEntityForGet(), String.class, tenantId, NotificationType.EMAIL.name(), null);
		String notificationTemplate = (String) responseEntity.getBody();

		apiUrl = uri + WebAPIURL.GET_TENANT_PARAM;
		responseEntity = internalRestTemplate.exchange(apiUrl, HttpMethod.GET, Utility.getHttpEntityForGet(),
				String.class, tenantId, CommonConstants.FROM_EMAIL);

		String fromEmail = (String) responseEntity.getBody();
		/*
		 * emailService.createEmail(notificationTemplate,
		 * receiptDTO.getCustomerEmail(), fromEmail,
		 * receiptDTO.getCustomerBillAmount(), receiptDTO.getBillerName(),
		 */
		emailService.createEmail(notificationTemplate, email, fromEmail, receiptDTO.getCustomerBillAmount(),
				receiptDTO.getBillerName(), receiptDTO.getCustomerDateTime(), receiptDTO.getCustomerTransactionRefId(),
				receiptDTO.getCustomerAccountNumber(), receiptDTO.getCustomerPaymentChannel());
	}
}
