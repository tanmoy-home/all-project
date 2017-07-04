package com.rssoftware.ou.gateway.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.QckPayType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

import reactor.bus.EventBus;

@Component
public class BillPaymentGatewayService {

	private static Log logger = LogFactory.getLog(BillFetchGatewayService.class);

	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private TransactionDataService transactionDataService;

	@Autowired
	private ParamService paramService;
	
	@Autowired
	private ResponseCodeService responseCodeService;
	
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();

	public BillPaymentGatewayService() {
		super();
	}
	
	//Changes for adding AgentChannel Details start 
	//..Modified By Samarjit..//
		@ServiceActivator(inputChannel = "ouBillPaymentRequestChannelExt")
		public BillPaymentResponse processOUBillPaymentRequestExt(BillPaymentRequestExt request)  throws ValidationException, IOException {
			
			
				transactionDataService.insert(request);
		
			
			String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
			String cuBillPaymentRequestUrl = cuDomain + CommonConstants.BILL_PAYMENT_REQUEST_URl
					+ request.getBillPaymentRequest().getHead().getRefId();
			try {
				Ack ack = restTemplate.postForObject(cuBillPaymentRequestUrl, request, Ack.class);
				if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
					transactionDataService.updateStatus(request.getBillPaymentRequest().getHead().getRefId(), RequestType.PAYMENT, RequestStatus.SEND_FAILED_ACK);
					throw ValidationException.getInstance(ack);
				}
				
				// need to wait for the response here
				long startTime = System.currentTimeMillis();
				
				while (true){
					try {
						Thread.sleep(CommonConstants.POLLING_WAIT_TIME_MILLIS);
					}
					catch(InterruptedException ie){}
					
					TransactionDataView tv = transactionDataService.getTransactionData(request.getBillPaymentRequest().getHead().getRefId(), RequestType.PAYMENT);
					
					System.out.println(tv.getStatus());
					
					if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_SUCCESS){
						return tv.getBillPaymentResponse();
					}
					else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_REVERSE){
						throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REVERSAL);
					}
					else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_DECLINE){
						String desc = null;
						String responseCode = tv.getBillPaymentResponse().getReason().getResponseCode();
						if (responseCode != null){
							desc = responseCodeService.getDescription(responseCode, RequestType.PAYMENT, CommonConstants.RESP_FAILURE_MSG, false);
						}
						throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DECLINE, desc);
					}
					
					if ((System.currentTimeMillis() - startTime) > CommonConstants.MAX_POLLING_WAIT_TIME_MILLIS){
						transactionDataService.updateStatus(request.getBillPaymentRequest().getHead().getRefId(), RequestType.PAYMENT, RequestStatus.TIMEOUT);
						throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
					}
				}
			} catch (Exception e) {
				if (e instanceof ValidationException){
					throw (ValidationException)e;
				}
				
				logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
				transactionDataService.updateStatus(request.getBillPaymentRequest().getHead().getRefId(), RequestType.PAYMENT, RequestStatus.SEND_FAILED);
				throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
			}
		}
		
		//Changes for adding AgentChannel Details end

	@ServiceActivator(inputChannel = "ouBillPaymentRequestChannel")

	public BillPaymentResponse processOUBillPaymentRequest(BillPaymentRequest request)  throws ValidationException, IOException {

	
		if(QckPayType.NO == request.getPaymentMethod().getQuickPay()){
		transactionDataService.insert(request.getHead().getRefId(), request);
		}else{
			transactionDataService.update(request.getHead().getRefId(), request);
		}
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		String cuBillPaymentRequestUrl = cuDomain + CommonConstants.BILL_PAYMENT_REQUEST_URl
				+ request.getHead().getRefId();
		try {
			Ack ack = restTemplate.postForObject(cuBillPaymentRequestUrl, request, Ack.class);
			LogUtils.logReqRespMessage(ack, ack.getRspCd(), Action.ACK);
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
				transactionDataService.updateStatus(request.getHead().getRefId(), RequestType.PAYMENT, RequestStatus.SEND_FAILED_ACK);
				throw ValidationException.getInstance(ack);
			}
			
			// need to wait for the response here
			long startTime = System.currentTimeMillis();
			
			while (true){
				try {
					Thread.sleep(CommonConstants.POLLING_WAIT_TIME_MILLIS);
				}
				catch(InterruptedException ie){}
				
				TransactionDataView tv = transactionDataService.getTransactionData(request.getHead().getRefId(), RequestType.PAYMENT);
				
				System.out.println(tv.getStatus());
				
				if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_SUCCESS){
					return tv.getBillPaymentResponse();
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_REVERSE){
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REVERSAL);
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_DECLINE){
					String desc = null;
					String responseCode = tv.getBillPaymentResponse().getReason().getResponseCode();
					if (responseCode != null){
						desc = responseCodeService.getDescription(responseCode, RequestType.PAYMENT, CommonConstants.RESP_FAILURE_MSG, false);
					}
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DECLINE, desc);
				}
				
				if ((System.currentTimeMillis() - startTime) > CommonConstants.MAX_POLLING_WAIT_TIME_MILLIS){
					transactionDataService.updateStatus(request.getHead().getRefId(), RequestType.PAYMENT, RequestStatus.TIMEOUT);
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
				}
			}
		} catch (Exception e) {
			if (e instanceof ValidationException){
				throw (ValidationException)e;
			}
			
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			transactionDataService.updateStatus(request.getHead().getRefId(), RequestType.PAYMENT, RequestStatus.SEND_FAILED);
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
		}
	}

	@ServiceActivator(inputChannel = "ouBillPaymentResponseChannel")

	public void processOUBillPaymentResponse(BillPaymentResponse bpr)  throws  ValidationException, IOException {

			
			transactionDataService.update(bpr.getHead().getRefId(), bpr);
		
		
	}
	
	
	@ServiceActivator(inputChannel = "ouBillPaymentQuickPayRequestChannel")
	public BillPaymentResponse processOUBillQuickPayPaymentRequest(BillPaymentRequest request)  throws ValidationException, IOException {
	
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		String cuBillPaymentRequestUrl = cuDomain + CommonConstants.BILL_PAYMENT_REQUEST_URl
				+ request.getHead().getRefId();
		try {
			Ack ack = restTemplate.postForObject(cuBillPaymentRequestUrl, request, Ack.class);
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
				transactionDataService.updateStatus(request.getHead().getRefId(), RequestType.PAYMENT, RequestStatus.SEND_FAILED_ACK);
				throw ValidationException.getInstance(ack);
			}
			
			// need to wait for the response here
			long startTime = System.currentTimeMillis();
			
			while (true){
				try {
					Thread.sleep(CommonConstants.POLLING_WAIT_TIME_MILLIS);
				}
				catch(InterruptedException ie){}
				
				TransactionDataView tv = transactionDataService.getTransactionData(request.getHead().getRefId(), RequestType.PAYMENT);
				
				System.out.println(request.getHead().getRefId() + ">>>" + tv.getStatus());
				
				if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_SUCCESS){
					return tv.getBillPaymentResponse();
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_REVERSE){
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REVERSAL);
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_DECLINE){
					String desc = null;
					String responseCode = tv.getBillPaymentResponse().getReason().getResponseCode();
					if (responseCode != null){
						desc = responseCodeService.getDescription(responseCode, RequestType.PAYMENT, CommonConstants.RESP_FAILURE_MSG, false);
					}
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DECLINE, desc);
				}
				
				if ((System.currentTimeMillis() - startTime) > CommonConstants.MAX_POLLING_WAIT_TIME_MILLIS){
					transactionDataService.updateStatus(request.getHead().getRefId(), RequestType.PAYMENT, RequestStatus.TIMEOUT);
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
				}
			}
		} catch (Exception e) {
			if (e instanceof ValidationException){
				throw (ValidationException)e;
			}
			
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			transactionDataService.updateStatus(request.getHead().getRefId(), RequestType.PAYMENT, RequestStatus.SEND_FAILED);
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
		}
	}


}
