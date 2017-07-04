package com.rssoftware.ou.controller;

import java.util.ArrayList;
import java.util.List;

import org.bbps.schema.Ack;
import org.bbps.schema.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.ErrorCodeUtil;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.database.entity.tenant.ComplaintDisposition;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.model.tenant.ComplaintserviceReasonsView;
import com.rssoftware.ou.service.ComplaintService;
import com.rssoftware.ou.tenant.service.CUService;
import com.rssoftware.ou.tenant.service.ComplaintDispositionService;
import com.rssoftware.ou.tenant.service.ComplaintserviceReasonsService;
import com.rssoftware.ou.tenant.service.impl.BillerServiceImpl;
import com.rssoftware.ou.tenant.service.impl.ComplaintDispositionServiceImpl;
import com.rssoftware.ou.tenant.service.impl.ComplaintserviceReasonsServiceImpl;

import in.co.rssoftware.bbps.schema.ComplainRequest;
import in.co.rssoftware.bbps.schema.ComplainStatusRequest;
import in.co.rssoftware.bbps.schema.ComplaintInformation;
import in.co.rssoftware.bbps.schema.ComplaintRaisedResponse;
import in.co.rssoftware.bbps.schema.ComplaintSearchResponse;
import in.co.rssoftware.bbps.schema.ComplaintsSearchRequest;
import in.co.rssoftware.bbps.schema.ParticipationData;
import in.co.rssoftware.bbps.schema.TransactionSearchRequest;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

@RestController
@RequestMapping("/APIService/complaint")
public class ComplaintAPIController {

	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private ComplaintserviceReasonsService complaintserviceReasonsService;
	
	@Autowired
	private ComplaintDispositionService complaintDispositionService; 
	
	
	@RequestMapping(value = "/raisecomplaintreq/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public ComplaintRaisedResponse raiseComplaint(@PathVariable String tenantId, @RequestBody ComplainRequest complainRequest)
			throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		ComplaintRaisedResponse response= new ComplaintRaisedResponse();
		try
		{
		ComplaintResponse res= complaintService.sendComplaintRequest(complainRequest);
		 response=mapResponse(res);
		}
		/*catch(ValidationException exception)
		{
			for(ErrorMessage errorMessage:exception.getAck().getErrorMessages())
			{
			in.co.rssoftware.bbps.schema.ErrorMessage message= new in.co.rssoftware.bbps.schema.ErrorMessage();
			message.setErrorCd(errorMessage.getErrorCd());
			message.setErrorDtl(errorMessage.getErrorDtl());
			response.getErrors().add(message);
			}
		}*/
		catch(ValidationException exception) {
			Ack ack = exception.getAck();
			if (ack != null) {
				for (ErrorMessage errorMessage : ack.getErrorMessages()) {
					in.co.rssoftware.bbps.schema.ErrorMessage message = new in.co.rssoftware.bbps.schema.ErrorMessage();
					message.setErrorCd(errorMessage.getErrorCd());
					message.setErrorDtl(errorMessage.getErrorDtl());
					response.getErrors().add(message);
				}
				return response;
			} else if (exception.matchReason(ValidationErrorReason.DUPLICATE_REQUEST)) {
				response = ErrorCodeUtil.complaintErrorResponseForCOU("COU_DUPLICATE_REQUEST");
			} else if (exception.matchReason(ValidationErrorReason.TIMEOUT)) {
				response = ErrorCodeUtil.complaintErrorResponseForCOU("COU_REQUEST_TIMEOUT");
			} else if (exception.matchReason(ValidationErrorReason.NETWORK_ERR)) {
				response = ErrorCodeUtil.complaintErrorResponseForCOU("COU_NETWORK_ERROR");
			} else {
				in.co.rssoftware.bbps.schema.ErrorMessage message = new in.co.rssoftware.bbps.schema.ErrorMessage();				
				message.setErrorCd(exception.getCode());
				message.setErrorDtl(exception.getDescription());
				response.getErrors().add(message);
				return response;
			}			
		}
		return response;

	}

	@RequestMapping(value = "/searchcomplaintstatus/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public ComplaintRaisedResponse searchComplaintStatus(@PathVariable String tenantId,
			@RequestBody ComplainStatusRequest complainStatusRequest) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		ComplaintRaisedResponse response= new ComplaintRaisedResponse();
		try {
			ComplaintResponse res = complaintService.sendComplaintStatusRequest(complainStatusRequest);
			response = mapResponse(res);
		} catch(ValidationException exception) {
			Ack ack = exception.getAck();
			if (ack != null) {
				for (ErrorMessage errorMessage : ack.getErrorMessages()) {
					in.co.rssoftware.bbps.schema.ErrorMessage message = new in.co.rssoftware.bbps.schema.ErrorMessage();
					message.setErrorCd(errorMessage.getErrorCd());
					message.setErrorDtl(errorMessage.getErrorDtl());
					response.getErrors().add(message);
				}
				return response;
			} else if (exception.matchReason(ValidationErrorReason.DUPLICATE_REQUEST)) {
				response = ErrorCodeUtil.complaintErrorResponseForCOU("COU_DUPLICATE_REQUEST");
			} else if (exception.matchReason(ValidationErrorReason.TIMEOUT)) {
				response = ErrorCodeUtil.complaintErrorResponseForCOU("COU_REQUEST_TIMEOUT");
			} else if (exception.matchReason(ValidationErrorReason.NETWORK_ERR)) {
				response = ErrorCodeUtil.complaintErrorResponseForCOU("COU_NETWORK_ERROR");
			} else {
				in.co.rssoftware.bbps.schema.ErrorMessage message = new in.co.rssoftware.bbps.schema.ErrorMessage();				
				message.setErrorCd(exception.getCode());
				message.setErrorDtl(exception.getDescription());
				response.getErrors().add(message);
				return response;
			}			
		}
		/*catch (ValidationException exception) {
			for (ErrorMessage errorMessage : exception.getAck().getErrorMessages()) {
				in.co.rssoftware.bbps.schema.ErrorMessage message = new in.co.rssoftware.bbps.schema.ErrorMessage();
				message.setErrorCd(errorMessage.getErrorCd());
				message.setErrorDtl(errorMessage.getErrorDtl());
				response.getErrors().add(message);
			}
		}*/
		return response;

	}
	private ComplaintRaisedResponse mapResponse(ComplaintResponse resp) {
		ComplaintRaisedResponse compResp=new ComplaintRaisedResponse();
		compResp.setComplaintId(resp.getComplaintIdByCU());
		compResp.setOpenComplaint(resp.getOpenComplaint());
		compResp.setComplaintStatus(resp.getComplaintStatus());
		compResp.setAssigned(resp.getAssignedTo());
		compResp.setResponseCode(resp.getRespCode());
		compResp.setResponseReason(resp.getRespReason());
		return compResp;
	}
	@RequestMapping(value = "/searchtransaction/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public TxnSearchResponse searchTransaction(@PathVariable String tenantId,
			@RequestBody TransactionSearchRequest transactionSearchRequest) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		TxnSearchResponse response = new TxnSearchResponse();
		try {

			response = complaintService.sendTransactionSearchRequestType(transactionSearchRequest);
			/*
			 * LogUtils.logReqRespMessage(ack, ack.getRefId(),
			 * Action.COMPLAINT_REQUEST); TxnSearchResponse resp =
			 * complaintService.getTransactionSearchResponse(
			 * transactionSearchRequest.getMobile(), ack.getRefId());
			 */
		} catch (ValidationException exception) {
			Ack ack = exception.getAck();
			if (ack != null) {
				for (ErrorMessage errorMessage : ack.getErrorMessages()) {
					in.co.rssoftware.bbps.schema.ErrorMessage message = new in.co.rssoftware.bbps.schema.ErrorMessage();
					message.setErrorCd(errorMessage.getErrorCd());
					message.setErrorDtl(errorMessage.getErrorDtl());
					response.getErrors().add(message);
				}
				return response;
			} else if (exception.matchReason(ValidationErrorReason.DUPLICATE_REQUEST)) {
				response = ErrorCodeUtil.txnSearchErrorResponseForCOU("COU_DUPLICATE_REQUEST");
			} else if (exception.matchReason(ValidationErrorReason.TIMEOUT)) {
				response = ErrorCodeUtil.txnSearchErrorResponseForCOU("COU_REQUEST_TIMEOUT");
			} else if (exception.matchReason(ValidationErrorReason.NETWORK_ERR)) {
				response = ErrorCodeUtil.txnSearchErrorResponseForCOU("COU_NETWORK_ERROR");
			} else {
				in.co.rssoftware.bbps.schema.ErrorMessage message = new in.co.rssoftware.bbps.schema.ErrorMessage();
				message.setErrorCd(exception.getCode());
				message.setErrorDtl(exception.getDescription());
				response.getErrors().add(message);
				return response;
			}
		}
		
		return response;

	}

	@ResponseBody
	@RequestMapping(value = "/fetchallcomplaint/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public ComplaintSearchResponse fetchAllComplaints(@PathVariable String tenantId,
			@RequestBody ComplaintsSearchRequest complaintsSearchRequest) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		ComplaintSearchResponse resp = complaintService.fetchAllComplaints(complaintsSearchRequest);
		return resp;

	}
	
	
	/*@RequestMapping(value = "/getAllServiceReason/urn:tenantId:{tenantId}/{particiationType}", method = RequestMethod.GET)
	public  @ResponseBody ResponseEntity<?> getAllServiceReason(@PathVariable String tenantId, @PathVariable String particiationType
			) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		
		ResponseEntity<?> response = null;
		try {
		List<ComplaintserviceReasonsView> serviceReasonList = new ArrayList<ComplaintserviceReasonsView>();
		if (particiationType.equals("agent"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsByAgent();
		if (particiationType.equals("biller"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsByBiller();
		if (particiationType.equals("system"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsBySystem();
		if (serviceReasonList != null) {
			response = new ResponseEntity(ComplaintserviceReasonsServiceImpl.getServiceReasonJaxb(serviceReasonList), HttpStatus.OK);
		} else {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("SERVICE_REASON_LIST_NOT_FOUND");
			errorMessage.setErrorDtl("Service Reason List could not be found!");
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
	} catch (Exception e) {
		
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setErrorCd("EXCEPTION");
		errorMessage.setErrorDtl(e.getMessage());
		response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
	}
		return response;

	}*/
	
	/*@RequestMapping(value = "/getAllServiceReason/urn:tenantId:{tenantId}", method = RequestMethod.POST,produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public  @ResponseBody ResponseEntity<?> getAllServiceReason(@PathVariable String tenantId,
			@RequestBody String particiationType) throws ValidationException
			 {
		
		//String particiationType=info.getParticiationType();
		TransactionContext.putTenantId(tenantId);
		
		ResponseEntity<?> response = null;
		try {
		List<ComplaintserviceReasonsView> serviceReasonList = new ArrayList<ComplaintserviceReasonsView>();
		if (particiationType.equals("agent"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsByAgent();
		if (particiationType.equals("biller"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsByBiller();
		if (particiationType.equals("system"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsBySystem();
		if (serviceReasonList != null) {
			response = new ResponseEntity(ComplaintserviceReasonsServiceImpl.getServiceReasonJaxb(serviceReasonList), HttpStatus.OK);
		} else {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("SERVICE_REASON_LIST_NOT_FOUND");
			errorMessage.setErrorDtl("Service Reason List could not be found!");
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
	} catch (Exception e) {
		
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setErrorCd("EXCEPTION");
		errorMessage.setErrorDtl(e.getMessage());
		response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
	}
		return response;

	}
*/
	
	@RequestMapping(value = "/getAllServiceReason/urn:tenantId:{tenantId}", method = RequestMethod.POST,produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public  @ResponseBody ResponseEntity<?> getAllServiceReason(@PathVariable String tenantId,
			@RequestBody ParticipationData info) throws ValidationException
			 {
		
		//String particiationType=info.getParticiationType();
		TransactionContext.putTenantId(tenantId);
		String participationType=info.getParticipationType();
		ResponseEntity<?> response = null;
		try {
		List<ComplaintserviceReasonsView> serviceReasonList = new ArrayList<ComplaintserviceReasonsView>();
		if (participationType.equals("agent"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsByAgent();
		if (participationType.equals("biller"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsByBiller();
		if (participationType.equals("system"))
			serviceReasonList = complaintserviceReasonsService.fetchServiceReasonsBySystem();
		if (serviceReasonList != null) {
			response = new ResponseEntity(ComplaintserviceReasonsServiceImpl.getServiceReasonJaxb(serviceReasonList), HttpStatus.OK);
		} else {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("SERVICE_REASON_LIST_NOT_FOUND");
			errorMessage.setErrorDtl("Service Reason List could not be found!");
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
	} catch (Exception e) {
		
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setErrorCd("EXCEPTION");
		errorMessage.setErrorDtl(e.getMessage());
		response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
	}
		return response;

	}

	@RequestMapping(value = "/urn:tenantId:{tenantId}/dispositionlist", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?>  getDispositionList(
			@PathVariable String tenantId) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		
		ResponseEntity<?> response = null;
		List<ComplaintDisposition> displist=complaintDispositionService.getDispositionsList();
		
		if (displist != null) {
			response = new ResponseEntity(ComplaintDispositionServiceImpl.getDispositionJaxb(displist), HttpStatus.OK);
		} else {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("Disposition_LIST_NOT_FOUND");
			errorMessage.setErrorDtl("Disposition List could not be found!");
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
}