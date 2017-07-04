package com.rssoftware.ou.tenant.service;

import in.co.rssoftware.bbps.schema.BillerFetchByParameter;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.BillerFetchRequestResponse;

public interface BillerFetchRequestResponseService {
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void save(BillerFetchRequestResponse billerFetchRequestResponse);
	
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillerFetchRequestResponse fetchByRefId(String refId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillerFetchRequestResponse saveFetchRequest(
			BillerFetchByParameter billerFetchByParameter, String ouName);
	

}
