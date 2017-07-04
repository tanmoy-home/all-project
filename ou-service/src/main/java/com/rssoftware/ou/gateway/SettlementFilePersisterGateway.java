package com.rssoftware.ou.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.rssoftware.ou.model.tenant.SettlementFileView;

@MessagingGateway
public interface SettlementFilePersisterGateway {

	@Gateway(requestChannel = "settlementFilePersisterChannel")
	public void processSettlementFiles(SettlementFileView sFileView);

}