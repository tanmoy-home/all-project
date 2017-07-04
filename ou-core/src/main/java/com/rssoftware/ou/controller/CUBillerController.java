package com.rssoftware.ou.controller;

import java.io.IOException;
import java.util.List;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.bus.Event;
import reactor.bus.EventBus;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.domain.Request;
import com.rssoftware.ou.domain.ResponseCode;
import com.rssoftware.ou.tenant.service.BillerOUtransactionDataService;
import com.rssoftware.ou.tenant.service.ErrorCodesService;
import com.rssoftware.ou.tenant.service.MyBillerDetailService;
import com.rssoftware.ou.utils.BillerUtil;

@RestController
public class CUBillerController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EventBus eventBus;
	
	@Autowired
	private MyBillerDetailService myBillerDetailService;
	
	@Autowired
	private ErrorCodesService errorCodesService;
	
	@Autowired
	private BillerOUtransactionDataService billerOUtransactionDataService;
	
	
	
	@RequestMapping(value = "/{tenantId}/BillFetchRequest/1.0/urn:referenceId:{referenceId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)	
	public @ResponseBody Ack fetchBill(@PathVariable String tenantId, @RequestBody BillFetchRequest billFetchRequest) throws IOException{
		
		logger.info("********************BillFetchRequest ***************************");
		LogUtils.logReqRespMessage(billFetchRequest, null, null);
		Ack ack = new Ack();
		TransactionContext.putTenantId(tenantId);
		if(billFetchRequest.getHead()!=null){//If HEAD tag is present
			ack=getAck(billFetchRequest.getHead().getRefId(), Action.FETCH_REQUEST);	
			ack.setRefId(billFetchRequest.getHead().getRefId());
			ack.setMsgId(billFetchRequest.getTxn().getMsgId());
		}
		List<ErrorMessage> errors = ack.getErrorMessages();
		// Validate the BillFetchRequest object.
		if(BillerUtil.validate(billFetchRequest, errors,errorCodesService,myBillerDetailService,billerOUtransactionDataService)) {						
			Request request = new Request();
			request.setTenantId(tenantId);
			request.setRequestType(RequestType.FETCH);
			request.setBillFetchRequest(billFetchRequest);
			eventBus.notify(CommonConstants.OU_REQUEST_EVENT, Event.wrap(request));
			ack.setRspCd(ResponseCode.Successful.name());
		} else {
			ack.setRspCd(ResponseCode.Failure.name());
			//ack.getErrorMessages().addAll(errors);
		}
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		return ack;
	}
	
	@RequestMapping(value = "/{tenantId}/BillPaymentRequest/1.0/urn:referenceId:{referenceId}", method = RequestMethod.POST)
	public @ResponseBody Ack payBill(@PathVariable String tenantId, @RequestBody BillPaymentRequest billPaymentRequest) throws IOException{
		
		logger.info("********************BillPaymentRequest ***************************");
		LogUtils.logReqRespMessage(billPaymentRequest, null, null);
		logger.info("********************BillPaymentRequest ***************************");

		//Ack ack = getAck(null, Action.PAYMENT_RESPONSE);
		Ack ack = getAck(null, Action.PAYMENT_REQUEST);
		TransactionContext.putTenantId(tenantId);
		if(billPaymentRequest.getHead()!=null){//If HEAD tag is present
			ack=getAck(billPaymentRequest.getHead().getRefId(), Action.PAYMENT_REQUEST);	
			ack.setRefId(billPaymentRequest.getHead().getRefId());	
			ack.setMsgId(billPaymentRequest.getTxn().getMsgId());
		}
		List<ErrorMessage> errors = ack.getErrorMessages();
		// Validate the BillPaymentRequest object.
		if(BillerUtil.validate(billPaymentRequest, errors,errorCodesService,myBillerDetailService,billerOUtransactionDataService)) {
				Request request = new Request();
				request.setTenantId(tenantId);
				request.setRequestType(RequestType.PAYMENT);
				request.setBillPaymentRequest(billPaymentRequest);
				eventBus.notify(CommonConstants.OU_REQUEST_EVENT, Event.wrap(request));
				ack.setRspCd(ResponseCode.Successful.name());
			}
		else {
			ack.setRspCd(ResponseCode.Failure.name());
		}		
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		return ack;
		
	}
	private Ack getAck(String referenceId, Action action) {
		Ack ack = new Ack();
		ack.setApi(action.name());
		ack.setTs(CommonUtils.getFormattedCurrentTimestamp());
		ack.setRefId(referenceId);
		return ack;
	}
	/*private Ack getAck(String referenceId, String apiName) {
		Ack ack = new Ack();
		ack.setApi(apiName);
		ack.setTs(CommonUtils.getFormattedCurrentTimestamp());
		ack.setRefId(referenceId);
		return ack;
	}*/
}
