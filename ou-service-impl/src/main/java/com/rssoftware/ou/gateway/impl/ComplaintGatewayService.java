package com.rssoftware.ou.gateway.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.TxnStatusComplainRequest;
import org.bbps.schema.TxnStatusComplainResponse;
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
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.service.ComplaintService;
import com.rssoftware.ou.tenant.service.ParamService;

@Component
public class ComplaintGatewayService {

	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ParamService paramService;
	
	@Autowired
	private ComplaintService complaintService;
	
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();
	
	@ServiceActivator(inputChannel = "complaintRequestChannel")
	public Ack processComplaintRequest(TxnStatusComplainRequest request) throws ValidationException {
		
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		String cuComplaintrequestUrl = cuDomain + CommonConstants.COMPLAINT_REQ_URL;
		String refId=request.getHead().getRefId();
		Ack ack=null;		
		try {
			ack = restTemplate.postForObject(cuComplaintrequestUrl, request, Ack.class);
			//LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.COMPLAINT_REQUEST);	
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
			//	complaintService.updateRequestStatus(refId, RequestStatus.SEND_FAILED_ACK);	
				throw ValidationException.getInstance(ack);
			}
			long startTime = System.currentTimeMillis();
			while (true){
				try {
					Thread.sleep(CommonConstants.POLLING_WAIT_TIME_MILLIS);
				}
				catch(InterruptedException ie){}
				
				ComplaintResponse resp=complaintService.getComplaintResponse(refId);
				if(resp==null)	{
					if ((System.currentTimeMillis() - startTime) > CommonConstants.MAX_POLLING_WAIT_TIME_MILLIS){
					//	complaintService.updateRequestStatus(refId, RequestStatus.TIMEOUT);
						throw ValidationException.getInstance(ValidationException.ValidationErrorReason.TIMEOUT);
					}
				  }
				else{
					if(resp.getOpenComplaint()!=null){				
						if(resp.getOpenComplaint().equals("Y")){
						//	complaintService.updateRequestStatus(refId, RequestStatus.DUPLICATE_REQUEST);	
						}	
						//complaintService.updateRequestStatus(refId, RequestStatus.RESPONSE_SUCCESS);
						
				}
				return ack;
				}
			}
			
		} catch (Exception e) {
			if (e instanceof ValidationException){
				throw (ValidationException)e;
			}
			log.error("Sending Failed :"+e);
		   // e.printStackTrace();
			//complaintService.updateRequestStatus(refId, RequestStatus.SEND_FAILED);
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
		}	
	}		

	
	@ServiceActivator(inputChannel = "complaintResponseChannel")
	public Ack processComplaintResponse(TxnStatusComplainResponse txnStatusComplainResponse) throws ValidationException{
		
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		String cuComplaintequestUrl = cuDomain + CommonConstants.COMPLAINT_RSP_URL;
		Ack ack=null;
		try {
			ack = restTemplate.postForObject(cuComplaintequestUrl, txnStatusComplainResponse, Ack.class);
			LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.COMPLAINT_RESPONSE);
		}
		catch (Exception e) {
			if (e instanceof ValidationException){
				throw (ValidationException)e;
			}
			
			log.error( e.getMessage(), e);
            log.info("In Excp : " + e.getMessage());

			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.NETWORK_ERR);
		}
		return ack;
	}
	
}
