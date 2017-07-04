package com.rssoftware.ou.gateway;

import org.bbps.schema.Ack;
import org.bbps.schema.TxnStatusComplainRequest;
import org.bbps.schema.TxnStatusComplainResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.rssoftware.ou.common.exception.ValidationException;



@MessagingGateway(name = "complaintGateway")
public interface ComplaintGateway {
	
	@Gateway(requestChannel = "complaintRequestChannel")
	public Ack processComplaintRequest(TxnStatusComplainRequest txnStatusComplainRequest) throws ValidationException;
	
	@Gateway(requestChannel = "complaintResponseChannel")
	public void processComplaintResponse(TxnStatusComplainResponse txnStatusComplainResponse) throws ValidationException;
	
}