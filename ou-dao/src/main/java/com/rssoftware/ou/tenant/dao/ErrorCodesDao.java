package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.COUErrorCodes;
import com.rssoftware.ou.database.entity.tenant.ErrorCodes;


public interface ErrorCodesDao extends GenericDao<ErrorCodes, String>
{

	public ErrorCodes getByComplianceId(String complianceCode);
	
	/* Error codes for COU */
	public COUErrorCodes getErrorMessage(String errorCode);
	public COUErrorCodes getErrorMessage(String errorCode, String responseCode);
	
}