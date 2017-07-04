package com.rssoftware.ou.cbsiso.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.rssoftware.ou.model.cbs.ProcessingMessage;

@MessagingGateway
public interface CbsIsoFromUPIGateway {

	@Gateway(requestChannel = "")
	public void sendMessage(ProcessingMessage processingMessage);
}
