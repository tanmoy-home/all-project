package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.bbps.schema.Ack;
import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.CustomerParamsType;
import org.bbps.schema.ReasonType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.TxnType;
import org.bbps.schema.CustomerParamsType.Tag;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.BillStatus;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.DishTvHttpClient;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.StatusField;
import com.rssoftware.ou.common.Utility;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.database.entity.tenant.BillFileConfig;
import com.rssoftware.ou.database.entity.tenant.OfflinebPayment;
import com.rssoftware.ou.database.entity.tenant.SubscriberData;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.ResponseCode;
import com.rssoftware.ou.model.tenant.MyBillerView;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.dao.BillDetailsDao;
import com.rssoftware.ou.tenant.dao.BillFileConfigDao;
import com.rssoftware.ou.tenant.dao.OfflinebPaymentDao;
import com.rssoftware.ou.tenant.dao.SubscriberDataDao;
import com.rssoftware.ou.tenant.service.BillerOUtransactionDataService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CommonBillerService;
import com.rssoftware.ou.tenant.service.FinTransactionDataService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.MyBillerDetailService;
import com.rssoftware.ou.tenant.service.TransactionDataService;
import com.rssoftware.ou.validator.Validator;

@Service
public class CommonBillerServiceImpl implements CommonBillerService{
	
	@Autowired
	MyBillerDetailService myBillerDetailService;
	
	@Autowired
	BillerService billerService;
	
	@Autowired
	BillerOUtransactionDataService billerOUtransactionDataService;
	
	@Autowired
	TransactionDataService transactionDataService;
	
	@Autowired
	private FinTransactionDataService finTranDataService;

	@Autowired
	private IDGeneratorService idGenetarorService;
	
	@Autowired
	private AsyncProcessor asyncProcessor;
	
	@Autowired
	private TenantDetailService tenantDetailService;
	
	@Autowired
	BillFileConfigDao billFileConfigDao;

	@Autowired
	BillDetailsDao billDetailsDao;
	
	@Autowired
	SubscriberDataDao subscriberDataDao;
	
	@Autowired
	OfflinebPaymentDao offlinebPaymentDao;
	
	@Value("${BASIC_AUTH_CREDENTIAL}")
    private String BASIC_AUTH_CREDENTIAL;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean checkIsOnUs(String billerId) throws IOException {
		if(myBillerDetailService.getBillerById(billerId)!=null) 
			return true;
		else
		    return false;
	}
	
	@Override
	public BillFetchResponse fetchBillOnUs(BillFetchRequestExt billFetchRequestExt)
			throws ValidationException, IOException {
		transactionDataService.insert(billFetchRequestExt,Boolean.TRUE);
		BillFetchRequest billFetchRequest=billFetchRequestExt.getBillFetchRequest();
		
		logger.info("*** BOU ON US Bill-Fetch-Request *****************************");
		LogUtils.logReqRespMessage(billFetchRequest,billFetchRequest.getHead().getRefId(), Action.FETCH_REQUEST);
		
		billerOUtransactionDataService.insert(billFetchRequest.getHead().getRefId(), billFetchRequest);
		MyBillerView myBiller=myBillerDetailService.getBillerById(billFetchRequest.getBillDetails().getBiller().getId());
		BillFetchResponse response=fetchBill(billFetchRequest,myBiller);
		response.setHead(billFetchRequest.getHead());
		billerOUtransactionDataService.update(billFetchRequest.getHead().getRefId(), response);
		transactionDataService.update(billFetchRequest.getHead().getRefId(), response);
		
		logger.info("*** BOU ON US Bill-Fetch-Response *****************************");
		LogUtils.logReqRespMessage(response,response.getHead().getRefId(), Action.FETCH_RESPONSE);
		return response;
	}

	@Override
	public BillPaymentResponse payBillOnUs(BillPaymentRequestExt billPaymentRequestExt)
			throws ValidationException, IOException {
		
		if (billPaymentRequestExt.getFinTransactionDetails() != null) {
			finTranDataService.insert(billPaymentRequestExt.getFinTransactionDetails(),
					billPaymentRequestExt.getBillPaymentRequest().getHead().getRefId());
		}
		transactionDataService.insert(billPaymentRequestExt,Boolean.TRUE);
		BillPaymentRequest paymentRequest=billPaymentRequestExt.getBillPaymentRequest();
		
		logger.info("*** BOU ON US Bill-Pay-Request *****************************");
		LogUtils.logReqRespMessage(paymentRequest,paymentRequest.getHead().getRefId(), Action.PAYMENT_REQUEST);
		
		billerOUtransactionDataService.insert(paymentRequest.getHead().getRefId(), paymentRequest);
		MyBillerView myBiller=myBillerDetailService.getBillerById(paymentRequest.getBillDetails().getBiller().getId());
		BillPaymentResponse response=payBill(paymentRequest,myBiller);
		response.setHead(paymentRequest.getHead());
		TxnType txnType = new TxnType();
		txnType.setMsgId(paymentRequest.getTxn().getMsgId());
		txnType.setTxnReferenceId(paymentRequest.getTxn().getTxnReferenceId());
		txnType.setTs(paymentRequest.getTxn().getTs());
		txnType.setType(TransactionType.FORWARD_TYPE_RESPONSE.value());
		response.setTxn(txnType);
		
		billerOUtransactionDataService.update(paymentRequest.getHead().getRefId(), response);
		transactionDataService.update(paymentRequest.getHead().getRefId(), response);
		
		logger.info("*** BOU  ON US Bill-Pay-Response *****************************");
		LogUtils.logReqRespMessage(response,response.getHead().getRefId(), Action.PAYMENT_RESPONSE);
		return response;
	}
	
	@Override
	public BillFetchResponse fetchBill(BillFetchRequest billFetchRequest, MyBillerView biller) throws ValidationException, IOException {

		BillFetchResponse response=null;
		
		switch (biller.getBlrMode()){
										case ONLINE:
											response = fetchOnlineBillSync(billFetchRequest, biller);
											break;
										case ONLINE_ASYNC:
											response = fetchOnlineBillASync(billFetchRequest, biller);
											break;
										case OFFLINEA:
											response = fetchOfflineBill(billFetchRequest, biller);
											break;
										default:
											throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILLER_MODE);
									}
		
		return response;
	}
	/*
	 * Synchronous billFetch from Biller 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private BillFetchResponse fetchOnlineBillSync(BillFetchRequest billFetchRequest, MyBillerView biller) throws IOException {

		String refId = billFetchRequest.getHead().getRefId();
		BillFetchResponse response = null;
		OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
		//String endPointUrl = biller.getBlrEndpointURL() + CommonConstants.BILLER_BILL_SYNC_FETCH_REQUEST_URl + TransactionContext.getTenantId();
		
		String endPointUrl = biller.getBlrEndpointURL() + "/BillerOU/Biller_simulator/fetch_sync_online_bill/urn:tenantId:" + TransactionContext.getTenantId();

		// Adding basic authentication support.
		//HttpEntity httpEntity = new HttpEntity(billFetchRequest, getHttpHeaders());
		//ResponseEntity<BillFetchResponse> responseEntity = restTemplate.postForEntity(endPointUrl, httpEntity,BillFetchResponse.class);
		response = restTemplate.postForObject(endPointUrl, billFetchRequest, BillFetchResponse.class);

		//response = responseEntity.getBody();
		
		if (response != null)
			billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH,RequestStatus.RESPONSE_RECIEVED);
		else
			billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH,RequestStatus.RESPONSE_FAILURE);

		return response;
	}
	/*
	 * Asynchronous billFetch from Biller 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BillFetchResponse fetchOnlineBillASync(BillFetchRequest billFetchRequest, MyBillerView biller)
	{
		String refId = billFetchRequest.getHead().getRefId();
		BillFetchResponse response=null;
		OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
		
		//forwarding the request to Biller
		logger.info("forwarding the request to Biller--------");
		LogUtils.logReqRespMessage(billFetchRequest, billFetchRequest.getHead().getRefId(), Action.FETCH_REQUEST);

		// Adding basic authentication support.
		HttpEntity httpEntity = new HttpEntity(billFetchRequest, getHttpHeaders());
		
		//Asynchronous communication
		String endPointUrl = biller.getBlrEndpointURL() + CommonConstants.BILLER_BILL_FETCH_REQUEST_URl + TransactionContext.getTenantId();
		try{
			ResponseEntity<Ack> responseEntity = restTemplate.postForEntity(endPointUrl, httpEntity, Ack.class);
			logger.debug("Received ACK from Biller------");
			Ack ack = responseEntity.getBody();
			if(ack!=null)
				response = processBillFetchAck(ack,billFetchRequest);
			else
				billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH, RequestStatus.BILLER_NOT_AVAILABLE);
			}
		catch(Exception e){
				logger.debug(e.getMessage());
				response = billFetchErrorResp(billFetchRequest);
			}
		
		return response;
	
	}
	
	/*
	 * BOU offlineA billFetch 
	 */
	private BillFetchResponse fetchOfflineBill(BillFetchRequest billFetchRequest, MyBillerView biller) throws ValidationException, IOException
	{
		String refId = billFetchRequest.getHead().getRefId();
		BillFetchResponse billFetchResponse = null;
		CustomerParamsType customerParams=billFetchRequest.getBillDetails().getCustomerParams();
		if(validateCustomerParams(biller,customerParams)){
			List<Tag> custParam=billFetchRequest.getBillDetails().getCustomerParams().getTags();
			List<String> params = new ArrayList<String>();
			custParam.forEach(tag->params.add(tag.getValue()));
			BillDetails billDetails = billDetailsDao.getBillDetails(biller.getBlrId(), params);
			/*if (billDetails != null) {
				
				if(billDetails.getStatus().equals(BillStatus.PAID.name()))
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NO_BILL_DUE);		
				
				BigDecimal dueDate=new BigDecimal(billDetails.getDueDate());
				BigDecimal currentDate=new BigDecimal(billFetchRequest.getHead().getTs().substring(0, 10).replaceAll("-",""));
				if((dueDate.compareTo(currentDate))==1)
				   billFetchResponse = createBillFetchResponse(billFetchRequest, billDetails);
				else
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DUE_DATE_EXPIRED);	
			}*/
                if (billDetails != null) {
				
				if(billDetails.getStatus().equals(BillStatus.PAID.name()))
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NO_BILL_DUE);
				
				Date dueDate = CommonUtils.parseDateyyyyMMdd(billDetails.getDueDate());
				Date currentDate = CommonUtils.parseDateyyyyMMdd(billFetchRequest.getHead().getTs().substring(0, 10).replaceAll("-",""));
				
				if (dueDate.after(currentDate) || dueDate.equals(currentDate))
					billFetchResponse = createBillFetchResponse(billFetchRequest, billDetails);
				else
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DUE_DATE_EXPIRED);
			}
			else 
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILL_DETAILS);
		}
		if (billFetchResponse != null)
			billerOUtransactionDataService.update(refId, billFetchResponse);
		return billFetchResponse;
		
		
		
		
		
	}
	
	private BillFetchResponse billFetchErrorResp(BillFetchRequest billFetchRequest)
	{
		BillFetchResponse response=null;
		response = new BillFetchResponse();
		response.setHead(billFetchRequest.getHead());
		ReasonType reasonType = new ReasonType();
		reasonType.setComplianceRespCd("BFR008");
		reasonType.setComplianceReason("Unable to get bill details from biller");
		response.setReason(reasonType);
		return response;
	}
    private BillFetchResponse createBillFetchResponse(BillFetchRequest billFetchRequest, BillDetails billDetails) throws ValidationException {
		
		BillFetchResponse billFetchResponse = new BillFetchResponse();
		
		BillDetailsType billDetailsType = billFetchRequest.getBillDetails();
		//billDetailsType.setBiller(billFetchRequest.getBillDetails().getBiller());
		billDetailsType.setBiller(null);
		billFetchResponse.setBillDetails(billDetailsType);
		
		// Creating reason tag.
		ReasonType reasonType = new ReasonType();
		reasonType.setApprovalRefNum("87654321");
		reasonType.setResponseCode("000");
		reasonType.setResponseReason("Successful");
		reasonType.setComplianceRespCd("");
		reasonType.setComplianceReason("");
		billFetchResponse.setReason(reasonType);
		
		// Creating transaction tag.
		TxnType txnType = billFetchRequest.getTxn();
		txnType.setRiskScores(null);
		txnType.setType(null);
		billFetchResponse.setTxn(txnType);		
		billFetchResponse.setBillerResponse(null);
		
		// Creating BillerResponseType object to assigning it to BillFetchResponse object.
		BillerResponseType billerResponse = new BillerResponseType();
		billerResponse.setAmount(billDetails.getActualAmount().toPlainString());
		billerResponse.setBillDate(convertToDate(billDetails.getBillDate()));
		billerResponse.setBillNumber(billDetails.getBillNumber());
		billerResponse.setBillPeriod(billDetails.getBillPeriod());
		//billerResponse.setCustConvDesc("Customer Service Fee");
		//billerResponse.setCustConvFee("600");
		billerResponse.setCustomerName(billDetails.getCustomerName());
		billerResponse.setDueDate(convertToDate(billDetails.getDueDate()));					
		
		// Assigning additional amounts in BillerResponseType object.
		String additionalAmountJSON = billDetails.getAdditionalAmounts();
		if(additionalAmountJSON!=null){
		try {
			JSONArray jsonarray = new JSONArray(additionalAmountJSON);
			for (int i = 0; i < jsonarray.length(); i++) {
			    JSONObject jsonobject = jsonarray.getJSONObject(i);
			    Iterator it = jsonobject.keys();
			    while(it.hasNext()) {
			    	String key = it.next().toString();
			    	BillerResponseType.Tag tag = new BillerResponseType.Tag();
					tag.setName(key);
					tag.setValue(jsonobject.getString(key));
					billerResponse.getTags().add(tag);
			    }
			}
		} catch (JSONException e) {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_JSON);
		}
		}
		billFetchResponse.setBillerResponse(billerResponse);
		
		// Assigning additional info in BillerResponseType object.
		AdditionalInfoType additionalInfo = new AdditionalInfoType();
		String additionalInfoJSON = billDetails.getAdditionalInfo();
		if(additionalInfoJSON!=null){
		try {
			JSONArray jsonarray = new JSONArray(additionalInfoJSON);
			for (int i = 0; i < jsonarray.length(); i++) {
			    JSONObject jsonobject = jsonarray.getJSONObject(i);
			    Iterator it = jsonobject.keys();
			    while(it.hasNext()) {
			    	String key = it.next().toString();
			    	AdditionalInfoType.Tag tag = new AdditionalInfoType.Tag();
					tag.setName(key);
					tag.setValue(jsonobject.getString(key));
					additionalInfo.getTags().add(tag);
			    }
			}
		} catch (JSONException e) {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_JSON);
		}
		billFetchResponse.setAdditionalInfo(additionalInfo);
		}
		return billFetchResponse;
	}


   private String convertToDate(String billDate) {
	   String year=billDate.substring(0,4);
	   String month=billDate.substring(4,6);
	   String day=billDate.substring(6,8);
   return year+"-"+month+"-"+day;
   }
	
	
   private BillFetchResponse processBillFetchAck(Ack ack,BillFetchRequest billFetchRequest) throws ValidationException, IOException{
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		String refId = billFetchRequest.getHead().getRefId();
		BillFetchResponse response = null;
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		response = asyncProcessor.processBillerBillFetchRequest(billFetchRequest, ack);
		if (response != null) {
			billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH,
					RequestStatus.RESPONSE_RECIEVED);
		} else {
			billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.FETCH,
					RequestStatus.RESPONSE_FAILURE);
		}
		return response;
	}

	
	
	
	@Override
	public BillPaymentResponse payBill(BillPaymentRequest billPaymentRequest, MyBillerView myBiller)
			throws ValidationException, IOException {
		
		BillPaymentResponse billPaymentResponse = null;
		logger.info("********************************payBill***************************** MODE: " + myBiller.getBlrMode());
		
		switch (myBiller.getBlrMode()){
										case ONLINE:
											billPaymentResponse = payOnlineBillSync(billPaymentRequest, myBiller);
											break;
										case ONLINE_ASYNC:
											billPaymentResponse = payOnlineBillAsync(billPaymentRequest, myBiller);
											break;
										case OFFLINEA:
											billPaymentResponse = payOfflineA_Bill(billPaymentRequest, myBiller);
											break;
										case OFFLINEB:
											billPaymentResponse = payOfflineB_Bill(billPaymentRequest, myBiller);
											break;
										default:
											throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILLER_MODE);
									}
		
			return billPaymentResponse;
		}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	   private BillPaymentResponse payOnlineBillSync(BillPaymentRequest billPaymentRequest, MyBillerView myBiller) throws IOException, ValidationException{
		   logger.info("********************************payOnlineBillSync*****************************");
			BillPaymentResponse billPaymentResponse = null;
			
			OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
//			String endPointUrl = myBiller.getBlrEndpointURL() + CommonConstants.BILLER_BILL_SYNC_PAY_REQUEST_URl + TransactionContext.getTenantId();
			String endPointUrl = myBiller.getBlrEndpointURL() + "/BillerOU/Biller_simulator/pay_sync_online_bill/urn:tenantId:" + TransactionContext.getTenantId();

			// Adding basic authentication support.
			/*HttpEntity httpEntity = new HttpEntity(billPaymentRequest, getHttpHeaders());
			ResponseEntity<BillPaymentResponse> responseEntity = restTemplate.postForEntity(endPointUrl, httpEntity,BillPaymentResponse.class);

			billPaymentResponse = responseEntity.getBody();*/
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/xml");
			headers.add(HttpHeaders.ACCEPT, "application/xml");
			HttpEntity httpEntity = new HttpEntity(billPaymentRequest, headers);
			
			logger.info("********************************payOnlineBillSync sending to biller simulator*****************************");
			billPaymentResponse = restTemplate.postForObject(endPointUrl, httpEntity, BillPaymentResponse.class);
			LogUtils.logReqRespMessage(billPaymentResponse, billPaymentRequest.getHead().getRefId(), Action.PAYMENT_RESPONSE);

			//if Biller is DishTv
			if(CommonConstants.BILLER_DISH_TV_ALIAS.equalsIgnoreCase(myBiller.getBlrAliasName())) {
				try {
					billPaymentResponse = new DishTvHttpClient(idGenetarorService).payBill(billPaymentRequest,billPaymentResponse);
					logger.info("******************************** Bill payment response from biller (DishTV) *****************************");
					LogUtils.logReqRespMessage(billPaymentResponse, billPaymentRequest.getHead().getRefId(), Action.PAYMENT_RESPONSE);
				} catch (Exception e) {
					e.printStackTrace();
					logger.info(e.getMessage());
					billPaymentResponse = null;
				}			
			}
			
			return billPaymentResponse;
	   }
	@SuppressWarnings({ "rawtypes", "unchecked" })
	   private BillPaymentResponse payOnlineBillAsync(BillPaymentRequest billPaymentRequest, MyBillerView myBiller) throws IOException, ValidationException{
			
		   BillPaymentResponse billPaymentResponse = null;
			
			String refId = billPaymentRequest.getHead().getRefId();
			String endPointUrl = myBiller.getBlrEndpointURL() + CommonConstants.BILLER_BILL_PAY_REQUEST_URl + TransactionContext.getTenantId();
			OUInternalRestTemplate restTemplate = OUInternalRestTemplate.createInstance();
			
			BillPaymentRequest billerBillPaymentRequest = new BillPaymentRequest();
			billerBillPaymentRequest.setHead(billPaymentRequest.getHead());
			billerBillPaymentRequest.setTxn(billPaymentRequest.getTxn());
			billerBillPaymentRequest.setBillDetails(billPaymentRequest.getBillDetails());
			
			// Adding basic authentication support.
			HttpEntity httpEntity = new HttpEntity(billerBillPaymentRequest, getHttpHeaders());
			billerOUtransactionDataService.updateStatus(refId,StatusField.BLR_STATUS, RequestType.PAYMENT, RequestStatus.SENT);
			ResponseEntity<Ack> responseEntity = restTemplate.postForEntity(endPointUrl, httpEntity, Ack.class);
			Ack ack = responseEntity.getBody();
			
			if (ack == null) {
				// Biller not found.
				billerOUtransactionDataService.updateStatus(refId, StatusField.BLR_STATUS, RequestType.PAYMENT, RequestStatus.BILLER_NOT_AVAILABLE);
				billPaymentResponse = new BillPaymentResponse();
				billPaymentResponse.setHead(billPaymentRequest.getHead());
				billPaymentResponse.getReason().setResponseCode("001");
				billPaymentResponse.getReason().setResponseReason("Biller not available.");
			} else {
				// Biller Ack received
				billPaymentResponse = asyncProcessor.processBillerBillPaymentRequest(billerBillPaymentRequest, ack);
			}

			return billPaymentResponse;
	   }
	private BillPaymentResponse payOfflineA_Bill(BillPaymentRequest billPaymentRequest, MyBillerView myBiller) throws IOException, ValidationException{
		   String refId = billPaymentRequest.getHead().getRefId();
		   BillPaymentResponse billPaymentResponse = null;
		   CustomerParamsType customerParams=billPaymentRequest.getBillDetails().getCustomerParams();
		   if(validateCustomerParams(myBiller,customerParams)){
				List<Tag> custParam=billPaymentRequest.getBillDetails().getCustomerParams().getTags();
				List<String> params = new ArrayList<String>();
				custParam.forEach(tag->params.add(tag.getValue()));
				BillDetails billDetails = billDetailsDao.getBillDetails(myBiller.getBlrId(), params);		
				if (billDetails != null) {
					if(billPaymentRequest.getBillerResponse()!=null){
					    if(!validateBillDetails(billDetails,billPaymentRequest.getBillerResponse())){
						     throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_BILL_DETAILS);
					    }
					}
					if(billDetails.getStatus().equals(BillStatus.PAID.name())){
						throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NO_BILL_DUE);	
					}
					else{
						BigDecimal dueDate=new BigDecimal(billDetails.getDueDate());
						BigDecimal currentDate=new BigDecimal(billPaymentRequest.getHead().getTs().substring(0, 10).replaceAll("-",""));
						if((dueDate.compareTo(currentDate))==1){
							billPaymentResponse = createBillPaymentResponse(billPaymentRequest, billDetails);
						}			
						else{
							throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DUE_DATE_EXPIRED);	
						}	
					}
			    }
				else{
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NO_BILL_DATA); 
				}	
		   }
		   if (billPaymentResponse != null){
				billerOUtransactionDataService.update(refId, billPaymentResponse);
				myBillerDetailService.updateBillDetails(billPaymentRequest.getBillerResponse().getBillNumber(), BillStatus.PAID);
		   }	
		   return billPaymentResponse;   
	   }
	
	
	  private BillPaymentResponse payOfflineB_Bill(BillPaymentRequest billPaymentRequest, MyBillerView myBiller) throws IOException, ValidationException{
		  String refId = billPaymentRequest.getHead().getRefId();
		   BillPaymentResponse billPaymentResponse = null;
		   CustomerParamsType customerParams=billPaymentRequest.getBillDetails().getCustomerParams();
		   if(validateCustomerParams(myBiller,customerParams)){
				List<Tag> custParam=billPaymentRequest.getBillDetails().getCustomerParams().getTags();
				List<String> params = new ArrayList<String>();
				custParam.forEach(tag->params.add(tag.getValue()));
				// fetching subscriber data
				SubscriberData subscriberData = subscriberDataDao.getSubscriberDetails(myBiller.getBlrId(), params);
				if (subscriberData != null) {
					// saving payment info
					OfflinebPayment offlinebPayment = new OfflinebPayment();
					
					String paymentId=idGenetarorService.getUniqueID(20,subscriberData.getSubscriberNo());
					offlinebPayment.setPaymentId(paymentId);
					offlinebPayment.setSubscriberNo(subscriberData.getSubscriberNo());
					
					offlinebPayment.setAmount(new BigDecimal(billPaymentRequest.getAmount().getAmt().getAmount()));
					offlinebPayment.setPaymentTs(new Timestamp(System.currentTimeMillis()));
					offlinebPaymentDao.create(offlinebPayment);
					//creating response
					billPaymentResponse = createBillPaymentResponse(billPaymentRequest, null);
					BillerResponseType billerResponse = new BillerResponseType();
					billerResponse.setCustomerName(subscriberData.getCustomerName());
					billerResponse.setAmount(billPaymentRequest.getAmount().getAmt().getAmount());
					billerResponse.setCustConvFee(billPaymentRequest.getAmount().getAmt().getCustConvFee());
					billerResponse.setBillNumber(paymentId);
					billPaymentResponse.setBillerResponse(billerResponse);
				}
				else{
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NO_BILL_DATA); 
				}
		   }	
			if (billPaymentResponse != null)
				billerOUtransactionDataService.update(refId, billPaymentResponse);
			return billPaymentResponse;
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
	  
	  
	  private boolean validateCustomerParams(MyBillerView blr,CustomerParamsType custmParm) throws ValidationException {
			Map<String, String> incomingValues = new HashMap<String, String>();
			Map<String, ParamConfig> mdmMandatoryValues = new HashMap<String, ParamConfig>();
			Map<String, ParamConfig> mdmOptionalValues = new HashMap<String, ParamConfig>();
			
			if (custmParm != null){
				for (CustomerParamsType.Tag tag:custmParm.getTags()){
					if (tag != null){
						if (!CommonUtils.hasValue(tag.getName()) || incomingValues.containsKey(tag.getName())){
							throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
						}
						else {
							incomingValues.put(tag.getName(), tag.getValue());
						}
					}
				}
			}
			
			if (blr.getBillerCustomerParams() != null){
				for (ParamConfig p:blr.getBillerCustomerParams()){
					if (p != null){
						if (p.getOptional()){
							mdmOptionalValues.put(p.getParamName(), p);
						}
						else {
							mdmMandatoryValues.put(p.getParamName(), p);
						}
					}
				}
			}
			
			if (!incomingValues.keySet().containsAll(mdmMandatoryValues.keySet())){
				// mandatory values not present
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
			}
			
			for (String tagName:incomingValues.keySet()){
				String value = incomingValues.get(tagName);
				
				 if((mdmMandatoryValues.get(tagName) != null || mdmOptionalValues.get(tagName) != null) && value!=null && value.length()>255){
					 throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
				 }
				if (mdmMandatoryValues.get(tagName) != null){
					if (ParamConfig.DataType.NUMERIC == mdmMandatoryValues.get(tagName).getDataType()) {
						if (!Validator.isNumeric(value)) {
							throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
						}
					} else if (ParamConfig.DataType.ALPHANUMERIC == mdmMandatoryValues.get(tagName).getDataType()) {
						if (!Validator.isAlphaNumericWithAllSpcl(value)) {
							throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
						}
					}
				}
				else if (mdmOptionalValues.get(tagName) != null){
					if (ParamConfig.DataType.NUMERIC == mdmOptionalValues.get(tagName).getDataType()) {
						if (!Validator.isNumeric(value)) {
							throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
						}
					} else if (ParamConfig.DataType.ALPHANUMERIC == mdmOptionalValues.get(tagName).getDataType()) {
						if (!Validator.isAlphaNumericWithAllSpcl(value)) {
							throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
						}
					}
				}
				else {
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.INVALID_PARAMS);
				}
			}
			return true;
		}
	  
	  
	  
	  private boolean validateBillDetails(BillDetails billDetails,BillerResponseType billerResponseType){
		  return(billDetails.getBillNumber().equals(billerResponseType.getBillNumber()) && billDetails.getBillNumber().equals(billerResponseType.getBillNumber())
			  && billDetails.getBillPeriod().equals(billerResponseType.getBillPeriod()));
	  }	
	  
	  private BillPaymentResponse createBillPaymentResponse(BillPaymentRequest billPaymentRequest, BillDetails billDetails) throws ValidationException {
			logger.info("******************** Create Bill Payment Response ***************************");
			BillPaymentResponse billPaymentResponse = new BillPaymentResponse();
			BillDetailsType billDetailsType = new BillDetailsType();
			billDetailsType.setCustomerParams(billPaymentRequest.getBillDetails().getCustomerParams());
			billPaymentResponse.setBillDetails(billDetailsType);
			
			billPaymentResponse.setHead(billPaymentRequest.getHead());
			
			ReasonType reasonType = new ReasonType();
			reasonType.setApprovalRefNum(idGenetarorService.getUniqueID(8,""));
			reasonType.setResponseCode("000");
			reasonType.setResponseReason(ResponseCode.Successful.name());
			billPaymentResponse.setReason(reasonType);
		
			// Creating transaction tag.
			TxnType txnType = billPaymentRequest.getTxn();
			txnType.setType(TransactionType.FORWARD_TYPE_RESPONSE.name());
			billPaymentResponse.setTxn(txnType);
			
			if(billDetails!=null){
				BillFileConfig billFileConfig = billFileConfigDao.get(billDetails.getBlrId());
				
				//avoid null pointer exception
				if(billFileConfig == null)
					billFileConfig = new BillFileConfig();
				
				// Creating BillerResponseType object to assigning it to BillFetchResponse object.
				BillerResponseType billerResponse = new BillerResponseType();
			//	billerResponse.setAmount(billDetails.getActualAmount().toPlainString());
				billerResponse.setAmount(billPaymentRequest.getAmount().getAmt().getAmount());
				billerResponse.setCustConvFee(billPaymentRequest.getAmount().getAmt().getCustConvFee());
		//		billerResponse.setBillDate(billDetails.getBillDate());
				billerResponse.setBillDate(Utility.convertDateStrToCuFormat(billDetails.getBillDate(), billFileConfig.getDateFormat()));
				billerResponse.setBillNumber(billDetails.getBillNumber());
				billerResponse.setBillPeriod(billDetails.getBillPeriod());
				billerResponse.setCustomerName(billDetails.getCustomerName());
		//		billerResponse.setDueDate(billDetails.getDueDate());
				billerResponse.setDueDate(Utility.convertDateStrToCuFormat(billDetails.getDueDate(), billFileConfig.getDateFormat()));
				billPaymentResponse.setBillerResponse(billerResponse);			
			}
			return billPaymentResponse;
		}
	
}
