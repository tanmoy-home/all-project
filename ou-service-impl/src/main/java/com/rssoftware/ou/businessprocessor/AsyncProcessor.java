package com.rssoftware.ou.businessprocessor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.ReasonType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.TxnStatusComplainRequest;
import org.bbps.schema.TxnStatusComplainResponse;
import org.bbps.schema.TxnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.BillStatus;
import com.rssoftware.ou.common.BillerMode;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.ErrorCode;
import com.rssoftware.ou.common.ErrorCodeUtil;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.StatusField;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.common.utils.RequestResponseGenerator;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.database.entity.tenant.ErrorCodes;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.domain.ResponseCode;
import com.rssoftware.ou.model.tenant.MyBillerView;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.service.ComplaintService;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.BillerOUtransactionDataService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CommonBillerService;
import com.rssoftware.ou.tenant.service.ComplaintRequestService;
import com.rssoftware.ou.tenant.service.ErrorCodesService;
import com.rssoftware.ou.tenant.service.MyBillerDetailService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

@Component
public class AsyncProcessor {

	@Autowired
	private TransactionDataService transactionDataService;
	
	@Autowired
	private BillerOUtransactionDataService billerOUtransactionDataService;

	@Autowired
	private ResponseCodeService responseCodeService;

	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private BillerService billerService;
	
	@Autowired
	private ParamService paramService;
	
	@Autowired
	private TenantDetailService tenantDetailService;
	
	@Autowired
	private ErrorCodesService errorCodesService;
	
	@Autowired
	MyBillerDetailService myBillerDetailService;
	
	@Autowired
	CommonBillerService commonBillerService; 
	
	@Autowired
	ComplaintRequestService complaintRequestService;
	
	private Map<String, BlockingQueue<Boolean>> queueMap = new ConcurrentHashMap<String, BlockingQueue<Boolean>>();
	private static Log logger = LogFactory.getLog(AsyncProcessor.class);
	
	public BillFetchResponse processAgentBillFetchRequest(BillFetchRequestExt billFetchRequestExt, Ack ack)
			throws ValidationException, IOException {
		
		Boolean responseAvailable = null;
		BillFetchResponse billFetchResponse = null;
		RequestType requestTypeFetch = RequestType.FETCH;
		String refId = billFetchRequestExt.getBillFetchRequest().getHead().getRefId();
		LogUtils.logReqRespMessage(ack, refId, Action.ACK);
		String ouName = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName();
		
		try {			
			
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {
				transactionDataService.updateStatus(refId, requestTypeFetch, RequestStatus.SEND_FAILED_ACK);
				return ErrorCodeUtil.billFetchErrorRespForCOU(billFetchRequestExt.getBillFetchRequest(), ouName, ack);
			}
			if (queueMap.containsKey(refId)) {
				return ErrorCodeUtil.billFetchErrorRespForCOU(billFetchRequestExt.getBillFetchRequest(), ouName, "COU_DUPLICATE_REQUEST");
				//throw ValidationException.getInstance(ValidationErrorReason.DUPLICATE_REQUEST);
			}

			// do something to initiate the request to downstream
			queueMap.put(refId, new ArrayBlockingQueue<Boolean>(1));
			// wait 30 seconds for response from CU to update the map
			responseAvailable = queueMap.get(refId).poll(CommonConstants.LONG_MAX_POLLING_WAIT_TIME_MILLIS,
					TimeUnit.MILLISECONDS);

			if (responseAvailable == null || !responseAvailable) {
				transactionDataService.updateStatus(refId, requestTypeFetch, RequestStatus.TIMEOUT);
				return ErrorCodeUtil.billFetchErrorRespForCOU(billFetchRequestExt.getBillFetchRequest(), ouName, "COU_REQUEST_TIMEOUT");
				//throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
			}

			/**
			 * If the above condition is not met, then responseAvailable=true.
			 * So querying db.
			 */

			TransactionDataView transactionDataView = transactionDataService.getTransactionData(refId,
					requestTypeFetch);

			if (transactionDataView != null) {
				if (RequestStatus.RESPONSE_SUCCESS.equals(transactionDataView.getStatus())) {
					billFetchResponse = transactionDataView.getBillFetchResponse();
					//billFetchResponse = ErrorCodeUtil.populateErroneousResponseWithActualErrorMessage(billFetchResponse);
				} else {
					billFetchResponse = transactionDataView.getBillFetchResponse();
					if (billFetchResponse == null) {
						throwErrorBasedOnTransactionStatus(transactionDataView, requestTypeFetch);
					}
					/*if (billFetchResponse != null) {
						billFetchResponse = ErrorCodeUtil.populateErroneousResponseWithActualErrorMessage(billFetchResponse);
					} else {					
						throwErrorBasedOnTransactionStatus(transactionDataView, requestTypeFetch);
					}*/
				}
			}
		} catch (Exception e) {
			if (e instanceof ValidationException) {
				throw (ValidationException) e;
			}

			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
			transactionDataService.updateStatus(refId, RequestType.FETCH, RequestStatus.SEND_FAILED);
			return ErrorCodeUtil.billFetchErrorRespForCOU(billFetchRequestExt.getBillFetchRequest(), ouName, "COU_NETWORK_ERROR");
		} finally {
			queueMap.remove(refId);
		}
		return billFetchResponse;
	}

//	public BillFetchResponse processAgentBillFetchRequest(BillFetchRequestExt billFetchRequestExt, Ack ack)
//			throws ValidationException, IOException {
//		Boolean responseAvailable = null;
//		BillFetchResponse billFetchResponse = null;
//		RequestType requestTypeFetch = RequestType.FETCH;
//		String refId = billFetchRequestExt.getBillFetchRequest().getHead().getRefId();
//		LogUtils.logReqRespMessage(ack, refId, Action.ACK);
//		try {
//			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {
//
//				transactionDataService.updateStatus(refId, requestTypeFetch, RequestStatus.SEND_FAILED_ACK);
//				throw ValidationException.getInstance(ack);
//			}
//			if (queueMap.containsKey(refId)) {
//				throw ValidationException.getInstance(ValidationErrorReason.DUPLICATE_REQUEST);
//			}
//
//			// do something to initiate the request to downstream
//			queueMap.put(refId, new ArrayBlockingQueue<Boolean>(1));
//			// wait 30 seconds for response from CU to update the map 
//			responseAvailable = queueMap.get(refId).poll(CommonConstants.LONG_MAX_POLLING_WAIT_TIME_MILLIS,
//					TimeUnit.MILLISECONDS);
//
//			if (responseAvailable == null || !responseAvailable) {
//				transactionDataService.updateStatus(refId, requestTypeFetch, RequestStatus.TIMEOUT);
//				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
//			}
//
//			/**
//			 * If the above condition is not met, then responseAvailable=true.
//			 * So querying db.
//			 */
//
//			TransactionDataView transactionDataView = transactionDataService.getTransactionData(refId,
//					requestTypeFetch);
//
//			if (transactionDataView != null) {
//				if (RequestStatus.RESPONSE_SUCCESS.equals(transactionDataView.getStatus())) {
//					billFetchResponse = transactionDataView.getBillFetchResponse();
//				} else {
//					throwErrorBasedOnTransactionStatus(transactionDataView, requestTypeFetch);
//				}
//			}
//		} catch (Exception e) {
//			if (e instanceof ValidationException) {
//				throw (ValidationException) e;
//			}
//
//			logger.error( e.getMessage(), e);
//	        logger.info("In Excp : " + e.getMessage());
//			transactionDataService.updateStatus(refId, RequestType.FETCH, RequestStatus.SEND_FAILED);
//			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
//		} finally {
//			queueMap.remove(refId);
//		}
//		return billFetchResponse;
//	}

	public void processBillFetchResponse(BillFetchResponse billFetchResponse) throws ValidationException, IOException {
		String refId = billFetchResponse.getHead().getRefId();
		TransactionDataView transactionDataView = transactionDataService.update(refId, billFetchResponse);
		String originalNodeAddress = transactionDataView.getCurrentNodeAddress();
		if (queueMap.containsKey(refId)) {
			queueMap.get(refId).add(Boolean.TRUE);
		} else if (StringUtils.isNotBlank(originalNodeAddress)
				&& !CommonUtils.getServerNameWithPort().equals(originalNodeAddress)) {
			notifyCorrectNode(originalNodeAddress, refId);
		} else {
			logger.error("Inside processBillFetchResponse: Could not find request in queueMap for refid: " + refId);
		}
	}

	public BillPaymentResponse processAgentBillPaymentRequest(final BillPaymentRequestExt billPaymentReq, Ack ack)
			throws ValidationException, IOException {
		String refId = billPaymentReq.getBillPaymentRequest().getHead().getRefId();
		LogUtils.logReqRespMessage(ack, refId, Action.ACK);
		Boolean responseAvailable = null;
		BillPaymentResponse billPaymentResponse = null;
		String ouName = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName();
		RequestType requestTypePayment = RequestType.PAYMENT;
		try {
			// Check if this agent has already request payment for this bill
			if (queueMap.containsKey(refId)) {
				//throw ValidationException.getInstance(ValidationErrorReason.DUPLICATE_REQUEST);
				return ErrorCodeUtil.billPaymentErrorResponseForCOU(billPaymentReq.getBillPaymentRequest(), ouName, "COU_DUPLICATE_REQUEST");
			}

			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {
				transactionDataService.updateStatus(refId, requestTypePayment, RequestStatus.SEND_FAILED_ACK);
				return ErrorCodeUtil.billPaymentErrorResponseForCOU(billPaymentReq.getBillPaymentRequest(), ouName, ack);				
				//throw ValidationException.getInstance(ack);
			}

			queueMap.put(refId, new ArrayBlockingQueue<Boolean>(1));
			responseAvailable = queueMap.get(refId).poll(CommonConstants.LONG_MAX_POLLING_WAIT_TIME_MILLIS,
					TimeUnit.MILLISECONDS);

			if (responseAvailable == null || !responseAvailable) {
				transactionDataService.updateStatus(refId, requestTypePayment, RequestStatus.TIMEOUT);
				//throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
				return ErrorCodeUtil.billPaymentErrorResponseForCOU(billPaymentReq.getBillPaymentRequest(), ouName, "COU_REQUEST_TIMEOUT");
			}
			/**
			 * If the above condition is not met, then responseAvailable=true.
			 * So querying db.
			 */
			TransactionDataView transactionDataView = transactionDataService.getTransactionData(refId,
					requestTypePayment);

			if (transactionDataView != null) {
				if (RequestStatus.RESPONSE_SUCCESS.equals(transactionDataView.getStatus())) {
					billPaymentResponse = transactionDataView.getBillPaymentResponse();
				} else {
					billPaymentResponse = transactionDataView.getBillPaymentResponse();
					if (billPaymentResponse == null) {
						throwErrorBasedOnTransactionStatus(transactionDataView, requestTypePayment);
					}
					/*if (billPaymentResponse != null) {
						billPaymentResponse = ErrorCodeUtil.populateErroneousResponseWithActualErrorMessage(billPaymentResponse);
					} else {					
						throwErrorBasedOnTransactionStatus(transactionDataView, requestTypePayment);
					}*/					
				}
			}
		} catch (Exception e) {
			if (e instanceof ValidationException) {
				throw (ValidationException) e;
			}
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			transactionDataService.updateStatus(billPaymentReq.getBillPaymentRequest().getHead().getRefId(),
					requestTypePayment, RequestStatus.SEND_FAILED);
			//throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
			return ErrorCodeUtil.billPaymentErrorResponseForCOU(billPaymentReq.getBillPaymentRequest(), ouName, "COU_NETWORK_ERROR");
		} finally {
			queueMap.remove(refId);
		}
		return billPaymentResponse;
	}

	public void processBillPayResponse(BillPaymentResponse billPaymentResponse) throws ValidationException, IOException {
		String refId = billPaymentResponse.getHead().getRefId();
		TransactionDataView transactionDataView = transactionDataService.update(refId, billPaymentResponse);
		String originalNodeAddress = transactionDataView.getCurrentNodeAddress();
		if (queueMap.containsKey(refId)) {
			queueMap.get(refId).add(Boolean.TRUE);
		} else if (StringUtils.isNotBlank(originalNodeAddress)
				&& !CommonUtils.getServerNameWithPort().equals(originalNodeAddress)) {
			notifyCorrectNode(originalNodeAddress, refId);
		} else {
			logger.error("Inside processBillPayResponse: Could not find request in queueMap for refid: " + refId);
		}
	}
	
	
	

	
	public void processCUBillFetchRequest(BillFetchRequest billFetchRequest) throws ValidationException, IOException{
		
		BillFetchResponse response = null;
		String refId = billFetchRequest.getHead().getRefId();
		String billerId = billFetchRequest.getBillDetails().getBiller().getId();
		MyBillerView biller = myBillerDetailService.getBillerById(billerId);

		try {
			//update billerOUtransactionData with billFetchRequest
			billerOUtransactionDataService.insert(refId, billFetchRequest);
			billerOUtransactionDataService.updateStatus(refId, StatusField.CU_STATUS, RequestType.FETCH,RequestStatus.REQUEST_RECIEVED);
			response = commonBillerService.fetchBill(billFetchRequest, biller);
			if(response==null)
				billerOUtransactionDataService.updateStatus(refId, StatusField.CU_STATUS, RequestType.FETCH,RequestStatus.REQUEST_RECIEVED);
			billerOUtransactionDataService.update(refId, response);
	    	
		}
		catch (ValidationException ve) {
			if(ve.getDescription().equals(ValidationErrorReason.INVALID_PARAMS.getDescription())){
				response = billFetchErrorResp(billFetchRequest,ErrorCode.INVALID_CUSTOMER_PARAM);
			}
			else if(ve.getDescription().equals(ValidationErrorReason.INVALID_BILL_DETAILS.getDescription())){
				response = billFetchErrorResp(billFetchRequest,ErrorCode.INVALID_BILL_DETAILS);
			}
			else if(ve.getDescription().equals(ValidationErrorReason.NO_BILL_DUE.getDescription())){
				response = billFetchErrorResp(billFetchRequest,ErrorCode.NO_BILL_DUE);
			}
			else if(ve.getDescription().equals(ValidationErrorReason.DUE_DATE_EXPIRED.getDescription())){
				response = billFetchErrorResp(billFetchRequest,ErrorCode.DUE_DATE_EXPIRED);
			}
			billerOUtransactionDataService.update(refId, response);
		}
		//setting BOU header Tag to BillFetchResponse 
		String ouId=TransactionContext.getTenantId();
		TenantDetail td = tenantDetailService.fetchByTenantId(ouId);
		String ouName=td.getOuName();
		response.setHead(RequestResponseGenerator.getHead(ouName, billFetchRequest.getHead().getRefId()));
		//response.setTxn(billFetchRequest.getTxn());
		logger.debug("-----Sending the response to CU------------");
		LogUtils.logReqRespMessage(response, response.getHead().getRefId(), Action.FETCH_RESPONSE);
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);				
		String cuPostURL = cuDomain + CommonConstants.BILL_FETCH_RESPONSE_URl + refId;
		OURestTemplate restTemplate = OURestTemplate.createInstance();
		
		//checking Acknowledgement received from CU
		Ack ack = restTemplate.postForObject(cuPostURL, response, Ack.class);
		logger.debug("---------Received ACK from CU-------------");
		if(ack!=null)
			processCUAck(refId,ack);
		else
			billerOUtransactionDataService.updateStatus(refId,StatusField.CU_STATUS, RequestType.FETCH, RequestStatus.SEND_FAILED);
		
	}
	
	private BillFetchResponse billFetchErrorResp(BillFetchRequest billFetchRequest,ErrorCode errorKey)
	{
		ErrorCodes errorDetails=errorCodesService.searchByKey(errorKey);
		BillFetchResponse response = new BillFetchResponse();
		response.setHead(RequestResponseGenerator.getHead(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName(), billFetchRequest.getHead().getRefId()));
		//Creating Reason tag
		ReasonType reasonType = new ReasonType();
		reasonType.setResponseCode(errorDetails.getResponseCode());
		reasonType.setResponseReason(ResponseCode.Failure.name());
		reasonType.setComplianceRespCd(errorDetails.getComplianceReasonCode());
		reasonType.setComplianceReason(errorDetails.getComplianceCode());
		response.setReason(reasonType);
		response.setTxn(null);
		// Creating transaction tag.
		TxnType txnType = billFetchRequest.getTxn();
		txnType.setRiskScores(null);
		txnType.setType(null);
		response.setTxn(txnType);	
		return response;
	}
	/*
	 * Handle Ack received from CU
	 */
	private void processCUAck(String refId,Ack ack) throws IOException, ValidationException
	{
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		if (ack.getRspCd().equals(CommonConstants.RESP_SUCCESS_MSG)) {
			//For successful ACK
			billerOUtransactionDataService.updateStatus(refId,StatusField.CU_STATUS, RequestType.FETCH, RequestStatus.RESPONSE_SUCCESS);
		}	
	    if (CommonConstants.RESP_FAILURE_MSG.equals(ack.getRspCd())) {
	    	billerOUtransactionDataService.updateStatus(refId,StatusField.CU_STATUS, RequestType.FETCH, RequestStatus.RESPONSE_DECLINE);
		    throw ValidationException.getInstance(ValidationErrorReason.DECLINE);
	    }

	}
	
	
	

	public void processCUBillPayRequest(BillPaymentRequest billPaymentRequest) throws ValidationException, IOException{
		logger.info("********************************processCUBillPayRequest*****************************");
		
		String billerId = billPaymentRequest.getBillDetails().getBiller().getId();
		MyBillerView myBiller = myBillerDetailService.getBillerById(billerId);
		String refId = billPaymentRequest.getHead().getRefId();

		
		
			
		// Processing BillPaymentRequest .......................................
		
		BillPaymentResponse response = null;
		try {
			//Add the BillPaymentRequest to Biller OU transaction data table 
			billerOUtransactionDataService.insert(refId, billPaymentRequest);
			billerOUtransactionDataService.updateStatus(refId,StatusField.CU_STATUS, RequestType.PAYMENT, RequestStatus.REQUEST_RECIEVED);
			response = commonBillerService.payBill(billPaymentRequest, myBiller);
			if(response == null)
				response = billPaymentErrorResponse(billPaymentRequest,ErrorCode.PAYMENT_FAILED);
			billerOUtransactionDataService.update(refId, response);
			
		} catch (ValidationException ve) {
			if(ve.getDescription().equals(ValidationErrorReason.INVALID_PARAMS.getDescription())){
				response = billPaymentErrorResponse(billPaymentRequest,ErrorCode.INVALID_CUSTOMER_PARAM);
			}
			else if(ve.getDescription().equals(ValidationErrorReason.INVALID_BILL_DETAILS.getDescription())){
				response = billPaymentErrorResponse(billPaymentRequest,ErrorCode.INVALID_BILL_DETAILS);
			}
			else if(ve.getDescription().equals(ValidationErrorReason.NO_BILL_DUE.getDescription())){
				response = billPaymentErrorResponse(billPaymentRequest,ErrorCode.NO_BILL_DUE);
			}
			else if(ve.getDescription().equals(ValidationErrorReason.DUE_DATE_EXPIRED.getDescription())){
				response = billPaymentErrorResponse(billPaymentRequest,ErrorCode.DUE_DATE_EXPIRED);
			}	
			else if(ve.getDescription().equals(ValidationErrorReason.NO_BILL_DATA.getDescription())){
					response = billPaymentErrorResponse(billPaymentRequest,ErrorCode.BILL_NOT_AVAILABLE);	
			}
			billerOUtransactionDataService.updateStatus(refId,StatusField.BLR_STATUS, RequestType.PAYMENT, RequestStatus.RESPONSE_FAILURE);

		}
		catch(DataIntegrityViolationException ex){
			response = billPaymentErrorResponse(billPaymentRequest,ErrorCode.REPEAT_PAYMENT);
			billerOUtransactionDataService.updateStatus(refId,StatusField.BLR_STATUS, RequestType.PAYMENT, RequestStatus.RESPONSE_FAILURE);
		}
		
		//update response before sending to CU
		String ouId=TransactionContext.getTenantId();
		TenantDetail td = tenantDetailService.fetchByTenantId(ouId);
		String ouName=td.getOuName();
		response.setHead(RequestResponseGenerator.getHead(ouName, billPaymentRequest.getHead().getRefId()));
		//response.setTxn(billPaymentRequest.getTxn());
		TxnType txnType = new TxnType();
		txnType.setMsgId(billPaymentRequest.getTxn().getMsgId());
		txnType.setTxnReferenceId(billPaymentRequest.getTxn().getTxnReferenceId());
		//txnType.setTs(CommonUtils.getFormattedCurrentTimestamp());
		txnType.setTs(billPaymentRequest.getTxn().getTs());
		txnType.setType(TransactionType.FORWARD_TYPE_RESPONSE.value());
		response.setTxn(txnType);
		LogUtils.logReqRespMessage(response,billPaymentRequest.getHead().getRefId(), Action.PAYMENT_RESPONSE);
		Ack ackFromCU = null;
		
		try{
			String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
			String cuPostURL = cuDomain + CommonConstants.BILL_PAYMENT_RESPONSE_URl + refId;
			OURestTemplate restTemplate = OURestTemplate.createInstance();
			ackFromCU = restTemplate.postForObject(cuPostURL, response, Ack.class);
		}catch(Exception e)
		{
			logger.info(e.getMessage());
		}
		
		//process CU Acknowledgement after posting biller response
		if(ackFromCU != null)
			processCUAck(ackFromCU,billPaymentRequest,myBiller);
		else{
			//reversal sending to Biller in case CU not found
			//myBillerDetailService.reversPayment(billPaymentRequest, myBiller);
			//processCUReversalPayRequest(billPaymentRequest);
			billerOUtransactionDataService.updateStatus(refId,StatusField.CU_STATUS, RequestType.PAYMENT, RequestStatus.SEND_FAILED);
		}
		
	}
	
	
	private BillPaymentResponse billPaymentErrorResponse(BillPaymentRequest billPaymentRequest,ErrorCode errorKey)
	{
		ErrorCodes errorDetails=errorCodesService.searchByKey(errorKey);
		BillPaymentResponse response = new BillPaymentResponse();
		response.setHead(RequestResponseGenerator.getHead(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName(), billPaymentRequest.getHead().getRefId()));
		//Creating Reason tag
		ReasonType reasonType = new ReasonType();
		reasonType.setResponseCode(errorDetails.getResponseCode());
		reasonType.setResponseReason(ResponseCode.Failure.name());
		reasonType.setComplianceRespCd(errorDetails.getComplianceReasonCode());
		reasonType.setComplianceReason(errorDetails.getComplianceCode());
		response.setReason(reasonType);
		response.setTxn(null);
		// Creating transaction tag.
		TxnType txnType = billPaymentRequest.getTxn();
		txnType.setRiskScores(null);
		txnType.setType(null);
		response.setTxn(txnType);	
		return response;
	}
	
	/*
	 * Update Biller OU process after receive CU Acknowledgement for BillPaymentResponse
	 */
	private void processCUAck(Ack ackFromCU,BillPaymentRequest billPaymentRequest,MyBillerView myBiller) throws IOException, ValidationException
	{
		String refId = billPaymentRequest.getHead().getRefId();
			LogUtils.logReqRespMessage(ackFromCU, ackFromCU.getRefId(), Action.ACK);
			if (ackFromCU.getRspCd().equals(CommonConstants.RESP_SUCCESS_MSG)) {
				//For successful ACK
				billerOUtransactionDataService.updateStatus(refId,StatusField.CU_STATUS, RequestType.PAYMENT, RequestStatus.RESPONSE_SUCCESS);
				
			}
			else if (ackFromCU.getRspCd().equals(CommonConstants.RESP_FAILURE_MSG)) {
				billerOUtransactionDataService.updateStatus(refId,StatusField.CU_STATUS, RequestType.PAYMENT, RequestStatus.RESPONSE_DECLINE);
				logger.info("Received failure ACK from CU : "+CommonConstants.RESP_FAILURE_MSG+", Sending reversal response.");
				if(myBiller.getBlrMode().equals(BillerMode.OFFLINEA)){
					myBillerDetailService.updateBillDetails(billPaymentRequest.getBillerResponse().getBillNumber(), BillStatus.DUE);
				}
			}

	}
	
	
	
	public void processBillerBillPayResponse(BillPaymentResponse billPaymentResponse) throws ValidationException, IOException{
		
		if(billPaymentResponse==null)
			return;

		
		String refId = billPaymentResponse.getHead().getRefId();
		
		if (queueMap.containsKey(refId) && (queueMap.get(refId).size() == 0)) {
			billerOUtransactionDataService.update(refId, billPaymentResponse);
			queueMap.get(refId).add(Boolean.TRUE);
		} else {
			logger.error(
					"Inside processBillerBillPaymentResponse: Could not find request in queueMap for refId: " + refId);
		}
	}
	
	public BillPaymentResponse processBillerBillPaymentRequest(BillPaymentRequest billPaymentRequest, Ack ack)
			throws ValidationException, IOException {

		String refId = billPaymentRequest.getHead().getRefId();
		Boolean responseAvailable = null;
		BillPaymentResponse billPaymentResponse = null;
		try {
		if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {

			throw ValidationException.getInstance(ack);
		}

		if (queueMap.containsKey(refId)) {
			throw ValidationException.getInstance(ValidationErrorReason.DUPLICATE_REQUEST);
		}

		if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {
			throw ValidationException.getInstance(ack);
		}
		queueMap.put(refId, new ArrayBlockingQueue<Boolean>(1));
			responseAvailable = queueMap.get(refId).poll(CommonConstants.LONG_MAX_POLLING_WAIT_TIME_MILLIS,
					TimeUnit.MILLISECONDS);
			
		if (responseAvailable == null || !responseAvailable) {
			billerOUtransactionDataService.updateStatus(refId,StatusField.BLR_STATUS, RequestType.PAYMENT, RequestStatus.TIMEOUT);
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
		}

		/**
		 * If the above condition is not met, then responseAvailable=true. So
		 * querying db.
		 */
		billPaymentResponse = (BillPaymentResponse) billerOUtransactionDataService.getTransactionData(refId, RequestType.PAYMENT).getBillPaymentResponse();

	} catch (InterruptedException e) {
		logger.error( e.getMessage(), e);
        logger.info("In Excp ################## : " + e.getMessage());
	}finally {
		queueMap.remove(refId);
	}

		return billPaymentResponse;
	}

	public BillFetchResponse processBillerBillFetchRequest(BillFetchRequest billFetchRequest, Ack ack)
			throws ValidationException, IOException{

		String refId = billFetchRequest.getHead().getRefId();
		Boolean responseAvailable = null;
		BillFetchResponse billFetchResponse = null;
		try {		
		if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {
			throw ValidationException.getInstance(ack);
		}
		if (queueMap.containsKey(refId)) {
			throw ValidationException.getInstance(ValidationErrorReason.DUPLICATE_REQUEST);
		}
		if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {
			throw ValidationException.getInstance(ack);
		}
		
		queueMap.put(refId, new ArrayBlockingQueue<Boolean>(1));
			responseAvailable = queueMap.get(refId).poll(CommonConstants.LONG_MAX_POLLING_WAIT_TIME_MILLIS,
					TimeUnit.MILLISECONDS);	

			if (responseAvailable == null || !responseAvailable) {
				billerOUtransactionDataService.updateStatus(refId,StatusField.BLR_STATUS,RequestType.FETCH, RequestStatus.TIMEOUT);
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
			}
			//If the above condition is not met, then responseAvailable=true. So querying db.
			billFetchResponse = (BillFetchResponse) billerOUtransactionDataService.getTransactionData(refId, RequestType.FETCH).getBillFetchResponse();
		
		
		} catch (InterruptedException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());		
	        }finally {
	    		queueMap.remove(refId);
	    	}
		
		logger.debug("Received Response from Biller---------");
		LogUtils.logReqRespMessage(billFetchResponse, billFetchResponse.getHead().getRefId(), Action.FETCH_RESPONSE);
		return billFetchResponse;
	}

	public void processBillerBillFetchResponse(BillFetchResponse billFetchResponse) throws ValidationException, IOException{
		String refId = billFetchResponse.getHead().getRefId();
		if (billFetchResponse != null && queueMap.containsKey(refId) && (queueMap.get(refId).size() == 0) )  {
			billerOUtransactionDataService.update(refId, billFetchResponse);
			queueMap.get(refId).add(Boolean.TRUE);
		}
		else {
			logger.error(
					"Inside processBillerBillFetchResponse: Could not find request in queueMap for refId: " + refId);
		}
	}
	

	public ComplaintResponse processComplaintRequest(TxnStatusComplainRequest req, Ack ack)
			throws ValidationException {
		Boolean responseAvailable = null;
		ComplaintResponse resp = null;
		//RequestType requestTypeFetch = RequestType.COMPLAINT;
		String refId = req.getHead().getRefId();

		try {
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())) {
				complaintService.updateRequestStatus(refId,RequestStatus.SEND_FAILED_ACK);
				throw ValidationException.getInstance(ack);
			}
			if (queueMap.containsKey(refId)) {
				throw ValidationException.getInstance(ValidationErrorReason.DUPLICATE_REQUEST);
			}

			// do something to initiate the request to downstream
			queueMap.put(refId, new ArrayBlockingQueue<Boolean>(1));
			responseAvailable = queueMap.get(refId).poll(CommonConstants.LONG_MAX_POLLING_WAIT_TIME_MILLIS,
					TimeUnit.MILLISECONDS);

			if (responseAvailable == null || !responseAvailable) {
				complaintService.updateRequestStatus(refId,RequestStatus.TIMEOUT);
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
			}

			/**
			 * If the above condition is not met, then responseAvailable=true.
			 * So querying db.
			 */

			
			resp=complaintService.getComplaintResponse(refId);
			if (resp != null) {
				if(resp.getOpenComplaint()!=null){				
					if(resp.getOpenComplaint().equals("Y")){
						complaintService.updateRequestStatus(refId, RequestStatus.DUPLICATE_REQUEST);	
					}
			  }
			complaintService.updateRequestStatus(refId, RequestStatus.RESPONSE_SUCCESS);	
			}
		} catch (Exception e) {
			if (e instanceof ValidationException) {
				throw (ValidationException) e;
			}

			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			complaintService.updateRequestStatus(refId,RequestStatus.SEND_FAILED);
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
		} finally {
			queueMap.remove(refId);
		}
		return resp;
	}
	public void processComplaintResponse(TxnStatusComplainResponse complaintResponse) throws ValidationException {
		String refId = complaintResponse.getHead().getRefId();
		complaintService.saveComplaintResponse(complaintResponse);
		String originalNodeAddress = complaintRequestService.getRequest(refId).getNodeAddress();
		if (queueMap.containsKey(refId)) {
			queueMap.get(refId).add(Boolean.TRUE);
		} 
		else if (StringUtils.isNotBlank(originalNodeAddress) && !CommonUtils.getServerNameWithPort().equals(originalNodeAddress))
		{ notifyCorrectNode(originalNodeAddress, refId); }
		else {
			logger.error("Inside processComplaintResponse: Could not find request in queueMap for refId: " + refId);
		}
	}

	private void notifyCorrectNode(String originalNodeAddress, String refId) {
		OUInternalRestTemplate customTemplate = OUInternalRestTemplate.createInstance();
		String url=null;
		if(null!=paramService.retrieveStringParamByName(CommonConstants.IS_HTTP_ENABLED)
				&& "YES".equals(paramService.retrieveStringParamByName(CommonConstants.IS_HTTP_ENABLED))){
			url = CommonConstants.HTTP_URL_PREFIX + originalNodeAddress + CommonConstants.NOTIFICATION_URL
					+ TransactionContext.getTenantId() + CommonConstants.STRING_SLASH + refId;
		}else{
		    url = CommonConstants.HTTPS_URL_PREFIX + originalNodeAddress + CommonConstants.NOTIFICATION_URL
				+ TransactionContext.getTenantId() + CommonConstants.STRING_SLASH + refId;
		}    
//		ResponseEntity responseEntity = customTemplate.exchange((url), HttpMethod.GET,
//				getHttpEntityForGet(), Boolean.class);	
		URI uri =  null;
		try {
			uri=new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			logger.error("unable to retireve uri for refid node"+url,e);
		}
		ResponseEntity responseEntity = customTemplate.exchange(uri, HttpMethod.GET, getHttpEntityForGet(), Boolean.class);
		Boolean  isNotified = ((Boolean)responseEntity.getBody());
		if (!isNotified) {
			logger.error("Could Not send notification to origin Node address");
		}

	}
	
	public static HttpEntity<?> getHttpEntityForGet()
    {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + CommonConstants.DEFAULT_USERNAME_PASSWORD);
            HttpEntity httpEntity = new HttpEntity(headers);
            return httpEntity;
    }

	private void throwErrorBasedOnTransactionStatus(TransactionDataView tv, RequestType requestType)
			throws ValidationException {

		if (tv.getStatus() == RequestStatus.RESPONSE_REVERSE) {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REVERSAL);
		} else if (tv.getStatus() == RequestStatus.RESPONSE_DECLINE) {
			String desc = null;
			String responseCode = RequestType.FETCH.equals(requestType)
					? tv.getBillFetchResponse().getReason().getResponseCode()
					: RequestType.PAYMENT.equals(requestType)
							? tv.getBillPaymentResponse().getReason().getResponseCode() : null;
			if (responseCode != null) {
				desc = responseCodeService.getDescription(responseCode, RequestType.FETCH,
						CommonConstants.RESP_FAILURE_MSG, false);
			}
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DECLINE, desc);
		}
	}

	public boolean notify(String refId) {
		boolean notificationSuccess = false;
		if (queueMap.containsKey(refId)) {
			queueMap.get(refId).add(Boolean.TRUE);
			notificationSuccess = true;
		}
		return notificationSuccess;
	}

	public void processCUReversalPayRequest(BillPaymentRequest reversalRequest) {
		logger.info("********************************processReversalRequest*****************************");
		String refId = reversalRequest.getHead().getRefId();
		try {
			// checking for existing payment record for the given txnRefId
			TransactionDataView txnData = billerOUtransactionDataService.getByTxnRefId(reversalRequest.getTxn().getTxnReferenceId());
			BillPaymentResponse reversalResponse=null;
			boolean foundTxn=false;
			if(txnData!=null){
				foundTxn=true;
				billerOUtransactionDataService.insertReversalRequest(refId, reversalRequest);	
			}
			reversalResponse = createReversalResponse(reversalRequest,foundTxn);
			billerOUtransactionDataService.updateReversalresponse(refId, reversalResponse);
			
			logger.info("********************************Reversal Response*****************************");
			LogUtils.logReqRespMessage(reversalResponse,reversalRequest.getHead().getRefId(), Action.REVERSAL_RESPONSE);
			
			String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
			String cuPostURL = cuDomain + CommonConstants.BILL_PAYMENT_RESPONSE_URl + refId;
			OURestTemplate restTemplate = OURestTemplate.createInstance();
			Ack ackFromCU = restTemplate.postForObject(cuPostURL, reversalResponse, Ack.class);
			
			logger.info("********************************Reversal Response Ack From CU*****************************");
			LogUtils.logReqRespMessage(ackFromCU, ackFromCU.getRefId(), Action.ACK);
			
			if (ackFromCU.getRspCd().equals(CommonConstants.RESP_SUCCESS_MSG)) {
				billerOUtransactionDataService.updateStatus(refId,StatusField.CU_REV_STATUS, RequestType.REVERSAL, RequestStatus.RESPONSE_SUCCESS);
			}else if (ackFromCU.getRspCd().equals(CommonConstants.RESP_FAILURE_MSG)) {
				billerOUtransactionDataService.updateStatus(refId,StatusField.CU_REV_STATUS, RequestType.REVERSAL, RequestStatus.RESPONSE_DECLINE);
			}
		} catch (Exception e) {
			logger.error("**********processReversalRequest*************", e);
		}
		
		
	}

	private BillPaymentResponse createReversalResponse(BillPaymentRequest reversalRequest,boolean foundTxn) {
		
		BillPaymentResponse reversalResponse = new BillPaymentResponse();
		
		// Creating head tag.
		String ouId=TransactionContext.getTenantId();
		TenantDetail td = tenantDetailService.fetchByTenantId(ouId);
		String ouName=td.getOuName();
		reversalResponse.setHead(RequestResponseGenerator.getHead(ouName, reversalRequest.getHead().getRefId()));
		
		// Creating reason tag.
		ReasonType reasonType = new ReasonType();
		if(foundTxn){
			reasonType.setApprovalRefNum("87654321");	
		}else{
			ErrorCodes errorDetails=errorCodesService.searchByKey(ErrorCode.INVALID_BILL_DETAILS);
			reasonType.setComplianceRespCd(errorDetails.getComplianceReasonCode());
			reasonType.setComplianceReason(errorDetails.getComplianceCode());
		}
		reasonType.setResponseCode("103");
		reasonType.setResponseReason(ResponseCode.Failure.name());
		reversalResponse.setReason(reasonType);
		
		// Creating transaction tag.
		TxnType txnType = new TxnType();
		txnType.setMsgId(reversalRequest.getTxn().getMsgId());
		txnType.setTxnReferenceId(reversalRequest.getTxn().getTxnReferenceId());
		txnType.setTs(reversalRequest.getTxn().getTs());
		txnType.setType(TransactionType.REVERSAL_TYPE_RESPONSE.value());
		reversalResponse.setTxn(txnType);		
			
		return reversalResponse;
	}
	
}