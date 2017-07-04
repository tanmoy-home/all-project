package com.rssoftware.ou.tenant.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;


public interface TenantParamService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	void saveTenantParam(String name, String charValue);
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	void saveTenantParam(String name, byte[] byteValue);
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	String retrieveStringParamByName(String name);
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	byte[] retrieveByteArrayParamByName(String name);
}
