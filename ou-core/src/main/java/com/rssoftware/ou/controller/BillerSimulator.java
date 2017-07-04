package com.rssoftware.ou.controller;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.consumer.BillerSimulatorProcessor;
import com.rssoftware.ou.domain.Request;

import reactor.bus.Event;
import reactor.bus.EventBus;



@RestController
//@RequestMapping("/APIService/BillerOU/Biller_simulator")
@RequestMapping("/BillerOU/Biller_simulator")
public class BillerSimulator {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EventBus eventBus;	


	@RequestMapping(value = "/fetch_sync_online_bill/urn:tenantId:{tenantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody BillFetchResponse fetchSyncBillForOnlineBiller(@RequestBody BillFetchRequest billFetchRequest) {
			
		BillFetchResponse billFetchResponse = BillerSimulatorProcessor.getBillFetchResponse(billFetchRequest);
		return billFetchResponse;
	}
	
	@RequestMapping(value = "/fetch_online_bill/urn:tenantId:{tenantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Ack fetchBillForOnlineBiller(@RequestBody BillFetchRequest billFetchRequest) {
			
		Request request = new Request();
		request.setRequestType(RequestType.FETCH);
		request.setBillFetchRequest(billFetchRequest);
		eventBus.notify(CommonConstants.OU_BILLER_SIMULATOR_EVENT, Event.wrap(request));
		Ack ack = getAck(billFetchRequest.getHead().getRefId(), Action.FETCH_RESPONSE);
		ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		return ack;
			
	}
	

	@RequestMapping(value = "/pay_sync_online_bill/urn:tenantId:{tenantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody BillPaymentResponse paySyncBillForOnlineBiller(@RequestBody BillPaymentRequest billPaymentRequest) {
		logger.info("Before generating bill payment response***************************************************");
		BillPaymentResponse billPaymentResponse = BillerSimulatorProcessor.getBillPaymentResponse(billPaymentRequest);
		return billPaymentResponse;
	}
	
	@RequestMapping(value = "/pay_online_bill/urn:tenantId:{tenantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Ack payBillForOnlineBiller(@RequestBody BillPaymentRequest billPaymentRequest) {
			
		Request request = new Request();
		request.setRequestType(RequestType.PAYMENT);
		request.setBillPaymentRequest(billPaymentRequest);
		eventBus.notify(CommonConstants.OU_BILLER_SIMULATOR_EVENT, Event.wrap(request));
		Ack ack = getAck(billPaymentRequest.getHead().getRefId(), Action.PAYMENT_RESPONSE);
		ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		return ack;
	}
	
	@RequestMapping(value = "/reverse_payment/urn:tenantId:{tenantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Ack reversePaymentTransaction(@RequestBody BillPaymentRequest billPaymentRequest) {
			
		Request request = new Request();
		request.setRequestType(RequestType.REVERSAL);
		request.setBillPaymentRequest(billPaymentRequest);
		eventBus.notify(CommonConstants.OU_BILLER_SIMULATOR_EVENT, Event.wrap(request));
		Ack ack = getAck(billPaymentRequest.getHead().getRefId(), Action.PAYMENT_RESPONSE);
		ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		return ack;
	}
			
	private Ack getAck(String referenceId, Action action) {
		Ack ack = new Ack();
		ack.setApi(action.name());
		ack.setTs(CommonUtils.getFormattedCurrentTimestamp());
		ack.setRefId(referenceId);
		return ack;
	}
	
}
