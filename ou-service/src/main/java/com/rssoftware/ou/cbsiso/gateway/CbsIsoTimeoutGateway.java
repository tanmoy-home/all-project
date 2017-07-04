package com.rssoftware.ou.cbsiso.gateway;

import com.rssoftware.ou.model.cbs.ProcessingMessage;

public interface CbsIsoTimeoutGateway {
	
	public void sendMessage(ProcessingMessage processingMessage);
}

