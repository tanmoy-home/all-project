package com.rssoftware.ou.tenant.service;

import org.bbps.schema.ErrorMessage;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.ErrorCode;
import com.rssoftware.ou.database.entity.tenant.COUErrorCodes;
import com.rssoftware.ou.database.entity.tenant.ErrorCodes;

public interface ErrorCodesService {
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public ErrorCodes searchByKey(ErrorCode errorKey);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public ErrorCodes searchByComplianceCode(String complianceCode);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	ErrorMessage getErrorMessage(ErrorCode errorKey);
	
	/* For COU Functionality */
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public String getErrorMessage(String errorCode);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public COUErrorCodes getCOUErrorCodes(String errorCode);

}