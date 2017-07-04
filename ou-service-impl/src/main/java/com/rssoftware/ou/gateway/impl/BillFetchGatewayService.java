package com.rssoftware.ou.gateway.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.ErrorMessage;
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
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

import reactor.bus.EventBus;

@Component
public class BillFetchGatewayService {

	private static Log logger = LogFactory.getLog(BillFetchGatewayService.class);

	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private TransactionDataService transactionDataService;

	@Autowired
	private ParamService paramService;
	
	@Autowired
	private ResponseCodeService responseCodeService;
	
	@Autowired
	private IDGeneratorService idGenetarorService;
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();

	public BillFetchGatewayService() {
		super();
	}

	@ServiceActivator(inputChannel = "ouBillFetchRequestChannel")
	public BillFetchResponse processOUBillFetchRequest(BillFetchRequest billFetchRequest) throws ValidationException, IOException{
		transactionDataService.insert(billFetchRequest.getHead().getRefId(), billFetchRequest);
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		String cuBillFetchRequestUrl = cuDomain + CommonConstants.BILL_FETCH_REQUEST_URl
				+ billFetchRequest.getHead().getRefId();
		try {
			Ack ack = restTemplate.postForObject(cuBillFetchRequestUrl, billFetchRequest, Ack.class);
			LogUtils.logReqRespMessage(ack, ack.getRspCd(), Action.ACK);
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
				transactionDataService.updateStatus(billFetchRequest.getHead().getRefId(), RequestType.FETCH, RequestStatus.SEND_FAILED_ACK);
				throw ValidationException.getInstance(ack);
			}
			
			// need to wait for the response here
			long startTime = System.currentTimeMillis();
			while (true){
				try {
					Thread.sleep(CommonConstants.POLLING_WAIT_TIME_MILLIS);
				}
				catch(InterruptedException ie){}
				
				TransactionDataView tv = transactionDataService.getTransactionData(billFetchRequest.getHead().getRefId(), RequestType.FETCH);
				
				if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_SUCCESS){
					return tv.getBillFetchResponse();
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_REVERSE){
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REVERSAL);
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_DECLINE){
					String desc = null;
					String responseCode = tv.getBillFetchResponse().getReason().getResponseCode();
					if (responseCode != null){
						desc = responseCodeService.getDescription(responseCode, RequestType.FETCH, CommonConstants.RESP_FAILURE_MSG, false);
					}
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DECLINE, desc);
				}
				
				if ((System.currentTimeMillis() - startTime) > CommonConstants.MAX_POLLING_WAIT_TIME_MILLIS){
					transactionDataService.updateStatus(billFetchRequest.getHead().getRefId(), RequestType.FETCH, RequestStatus.TIMEOUT);
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
				}
			}
		} catch (Exception e) {
			if (e instanceof ValidationException){
				throw (ValidationException)e;
			}
			
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			transactionDataService.updateStatus(billFetchRequest.getHead().getRefId(), RequestType.FETCH, RequestStatus.SEND_FAILED);
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
		}
	}
	
	//...Modified..//
	@ServiceActivator(inputChannel = "ouBillFetchRequestChannel1")
	public BillFetchResponse processOUBillFetchRequest1(BillFetchRequestExt billFetchRequestExt) throws ValidationException, IOException{
		
		transactionDataService.insert(billFetchRequestExt);
		
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		String cuBillFetchRequestUrl = cuDomain + CommonConstants.BILL_FETCH_REQUEST_URl
				+ billFetchRequestExt.getBillFetchRequest().getHead().getRefId();
		try {
			Ack ack = restTemplate.postForObject(cuBillFetchRequestUrl, billFetchRequestExt.getBillFetchRequest(), Ack.class);
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
				transactionDataService.updateStatus(billFetchRequestExt.getBillFetchRequest().getHead().getRefId(), RequestType.FETCH, RequestStatus.SEND_FAILED_ACK);
				throw ValidationException.getInstance(ack);
			}
			
			// need to wait for the response here
			long startTime = System.currentTimeMillis();
			while (true){
				try {
					Thread.sleep(CommonConstants.POLLING_WAIT_TIME_MILLIS);
				}
				catch(InterruptedException ie){}
				
				TransactionDataView tv = transactionDataService.getTransactionData(billFetchRequestExt.getBillFetchRequest().getHead().getRefId(), RequestType.FETCH);
			
				if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_SUCCESS){
					return tv.getBillFetchResponse();
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_REVERSE){
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REVERSAL);
				}
				else if (tv != null && tv.getStatus() == RequestStatus.RESPONSE_DECLINE){
					String desc = null;
					String responseCode = tv.getBillFetchResponse().getReason().getResponseCode();
					if (responseCode != null){
						desc = responseCodeService.getDescription(responseCode, RequestType.FETCH, CommonConstants.RESP_FAILURE_MSG, false);
					}
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.DECLINE, desc);
				}
				
				if ((System.currentTimeMillis() - startTime) > CommonConstants.MAX_POLLING_WAIT_TIME_MILLIS){
					transactionDataService.updateStatus(billFetchRequestExt.getBillFetchRequest().getHead().getRefId(), RequestType.FETCH, RequestStatus.TIMEOUT);
					throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
				}
			}
		} catch (Exception e) {
			if (e instanceof ValidationException){
				throw (ValidationException)e;
			}
			
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			transactionDataService.updateStatus(billFetchRequestExt.getBillFetchRequest().getHead().getRefId(), RequestType.FETCH, RequestStatus.SEND_FAILED);
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
		}
	}

	//..Modified..//

	@ServiceActivator(inputChannel = "ouBillFetchResponseChannel")
	public void processOUBillFetchResponse(BillFetchResponse billFetchResponse) throws ValidationException, IOException {
		transactionDataService.update(billFetchResponse.getHead().getRefId(), billFetchResponse);
	}

}