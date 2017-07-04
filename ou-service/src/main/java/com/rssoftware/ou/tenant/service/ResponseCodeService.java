package com.rssoftware.ou.tenant.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.RequestType;

public interface ResponseCodeService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	boolean isValid(String responseCode, RequestType requestType, String responseReason, boolean reversal);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	String getDescription(String responseCode, RequestType requestType, String responseReason, boolean reversal);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	void save(String responseCode, RequestType requestType,	String responseReason, boolean reversal, String codeDescription);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public void refresh();
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String getSuccessfulResponseCode(RequestType requestType, boolean reversal);
}
