package com.rssoftware.ou.gateway.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.model.tenant.SettlementFileView;

import reactor.bus.Event;
import reactor.bus.EventBus;

@MessageEndpoint
public class SettlementFilePersisterGatewayService {

	@Autowired
	private EventBus eventBus;
	
	@ServiceActivator(inputChannel = "settlementFilePersisterChannel")
	public void processSettlementFiles(SettlementFileView sFileView) {
		eventBus.notify(CommonConstants.SETTLEMENT_FILE_EVENT, Event.wrap(sFileView));
	}
}
