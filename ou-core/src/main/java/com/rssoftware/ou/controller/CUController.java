package com.rssoftware.ou.controller;

import javax.servlet.http.HttpServletRequest;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerFetchResponse;
import org.bbps.schema.ErrorMessage;
import org.bbps.schema.TxnStatusComplainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.bus.Event;
import reactor.bus.EventBus;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.BatchRequest;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.TypeOfBatch;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.domain.Response;
import com.rssoftware.ou.service.ComplaintService;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.utils.SignatureValidationUtil;

@RestController
public class CUController {

	//private static Log logger = LogFactory.getLog(CUController.class);
	@Autowired
	private EventBus eventBus;

	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private ApplicationConfigService applicationConfigService;

	@RequestMapping(value = "/{tenantId}/BillFetchResponse/1.0/urn:referenceId:{referenceId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public Ack billFetchResponse(HttpServletRequest request, @PathVariable String tenantId,
			@PathVariable String referenceId) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		Ack ack = getAck(referenceId, Action.FETCH_RESPONSE);

		BillFetchResponse billFetchResponse = null;

		try {
			billFetchResponse = (BillFetchResponse) SignatureValidationUtil.unmarshall(request);
		} catch (ValidationException ve) {
			setAckWithErrorMessage(ack, ve);
			return ack;
		}
		LogUtils.logReqRespMessage(billFetchResponse, referenceId, Action.FETCH_RESPONSE);
		Response response = getResponse(request, tenantId, Action.FETCH_RESPONSE);
		response.setBillFetchResponse(billFetchResponse);
		eventBus.notify(CommonConstants.CU_RESPONSE_EVENT, Event.wrap(response));

		ack.setRefId(billFetchResponse.getHead().getRefId());
		ack.setMsgId(billFetchResponse.getTxn().getMsgId());
		if(applicationConfigService.getValueByName(CommonConstants.ENABLE_BILL_FETCH_FAILURE_ACK_NAME)!=null  
				&& applicationConfigService.getValueByName(CommonConstants.ENABLE_BILL_FETCH_FAILURE_ACK_NAME).equals(CommonConstants.ENABLE_BILL_FETCH_FAILURE_ACK_VALUE)) {
			ack.setRspCd(CommonConstants.RESP_FAILURE_MSG);
		}
		else {
			ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		}
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		return ack;
	}

	@RequestMapping(value = "/{tenantId}/BillPaymentResponse/1.0/urn:referenceId:{referenceId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public Ack billPaymentResponse(HttpServletRequest request, @PathVariable String tenantId,
			@PathVariable String referenceId) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		Ack ack = getAck(referenceId, Action.PAYMENT_RESPONSE);

		BillPaymentResponse billPaymentResponse = null;

		try {
			billPaymentResponse = (BillPaymentResponse) SignatureValidationUtil.unmarshall(request);
		} catch (ValidationException ve) {
			setAckWithErrorMessage(ack, ve);
			return ack;
		}

		LogUtils.logReqRespMessage(billPaymentResponse, referenceId, Action.PAYMENT_RESPONSE);
		Response response = getResponse(request, tenantId, Action.PAYMENT_RESPONSE);
		response.setBillPaymentResponse(billPaymentResponse);
		eventBus.notify(CommonConstants.CU_RESPONSE_EVENT, Event.wrap(response));

		ack.setRefId(billPaymentResponse.getHead().getRefId());
		ack.setMsgId(billPaymentResponse.getTxn().getMsgId());
		if(applicationConfigService.getValueByName(CommonConstants.ENABLE_BILL_PAYMENT_FAILURE_ACK_NAME)!=null && applicationConfigService.getValueByName(CommonConstants.ENABLE_BILL_PAYMENT_FAILURE_ACK_NAME).equals(CommonConstants.ENABLE_BILL_PAYMENT_FAILURE_ACK_VALUE)
				&& null !=billPaymentResponse.getTxn() && null != billPaymentResponse.getTxn().getType() 
				&& !billPaymentResponse.getTxn().getType().equals("REVERSAL TYPE RESPONSE")) {
			ack.setRspCd(CommonConstants.RESP_FAILURE_MSG);
		}
		else {
			ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		}
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		return ack;
	}

	@RequestMapping(value = "/{tenantId}/BillerFetchResponse/1.0/urn:referenceId:{referenceId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public Ack billerFetchResponse(HttpServletRequest request, @PathVariable String tenantId,
			@PathVariable String referenceId) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		Ack ack = getAck(referenceId, Action.BILLER_LIST_RESPONSE);

		BillerFetchResponse billerFetchResponse = null;

		try {
			billerFetchResponse = (BillerFetchResponse) SignatureValidationUtil.unmarshall(request);
		} catch (ValidationException ve) {
			setAckWithErrorMessage(ack, ve);
			return ack;
		}
		LogUtils.logReqRespMessage(billerFetchResponse, billerFetchResponse.getHead().getRefId(),
				Action.BILLER_LIST_RESPONSE);
		BatchRequest br = new BatchRequest(tenantId, TypeOfBatch.BILLER_SAVE);
		br.setBillerFetchResponse(billerFetchResponse);
		BeanLocator.getBean(EventBus.class).notify("batchRequest", Event.wrap(br));

		ack.setRefId(referenceId);
		ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		return ack;
	}

	@RequestMapping(value = "/{tenantId}/TxnStatusComplainResponse", method = RequestMethod.POST)

	public Ack complaintResponse(HttpServletRequest request, @PathVariable String tenantId) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		Ack ack = getAck(null, Action.COMPLAINT_RESPONSE);

		TxnStatusComplainResponse txnStatusComplainResponse = null;

		try {
			txnStatusComplainResponse = (TxnStatusComplainResponse) SignatureValidationUtil.unmarshall(request);
		} catch (ValidationException ve) {
			setAckWithErrorMessage(ack, ve);
			return ack;
		}

		LogUtils.logReqRespMessage(txnStatusComplainResponse, txnStatusComplainResponse.getHead().getRefId(),
				Action.COMPLAINT_RESPONSE);

		Response response = new Response();
		response.setComplaintResponse(txnStatusComplainResponse);
		response.setAction(Action.COMPLAINT_RESPONSE);
		response.setTenantId(tenantId);
		eventBus.notify(CommonConstants.CU_RESPONSE_EVENT, Event.wrap(response));

		ack.setMsgId(txnStatusComplainResponse.getTxn().getMsgId());
		ack.setRefId(txnStatusComplainResponse.getHead().getRefId());
		ack.setMsgId(txnStatusComplainResponse.getTxn().getMsgId());
		ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);
		return ack;
	}

	private void setAckWithErrorMessage(Ack ack, ValidationException ve) {
		ack.setRspCd(CommonConstants.RESP_FAILURE_MSG);
		ack.getErrorMessages().add(new ErrorMessage());
		ack.getErrorMessages().get(0).setErrorCd(ve.getCode());
		ack.getErrorMessages().get(0).setErrorDtl(ve.getDescription());
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

	private Response getResponse(HttpServletRequest request, String tenantId, Action action) {
		Response response = new Response();
		response.setNodeAddress(CommonUtils.getServerNameWithPort());
		response.setAction(action);
		response.setTenantId(tenantId);
		return response;
	} 

}