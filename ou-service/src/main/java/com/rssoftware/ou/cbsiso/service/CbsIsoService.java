package com.rssoftware.ou.cbsiso.service;

import com.rssoftware.ou.model.cbs.ProcessingMessage;

public interface CbsIsoService {

	byte[] mapToMessage(ProcessingMessage msg);
	void mapFromMessage(byte[] msg);
	void processTimeout(ProcessingMessage msg);
	
}
