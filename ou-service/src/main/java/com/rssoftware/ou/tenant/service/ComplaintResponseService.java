package com.rssoftware.ou.tenant.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;


public interface ComplaintResponseService {
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void save(ComplaintResponse complaintResponse);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public ComplaintResponse getResponse(String refId);
   
}
