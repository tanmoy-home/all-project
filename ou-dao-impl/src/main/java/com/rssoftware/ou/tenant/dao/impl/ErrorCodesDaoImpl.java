package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.COUErrorCodes;
import com.rssoftware.ou.database.entity.tenant.ErrorCodes;
import com.rssoftware.ou.tenant.dao.ErrorCodesDao;
@Repository
public class ErrorCodesDaoImpl extends GenericDynamicDaoImpl<ErrorCodes, String> implements ErrorCodesDao {

	public ErrorCodesDaoImpl() {
		super(ErrorCodes.class);
	}
    
	private static final String GET_BY_COMPLIANCE_CODE="select ec from ErrorCodes ec where ec.complianceCode =:complianceCode";
	private static final String GET_COU_ERROR_MSG = "select ec from COUErrorCodes ec where ec.errorCode =:errCode";
	
	@Override
	public ErrorCodes getByComplianceId(String complianceCode){
		ErrorCodes errorCodes=(ErrorCodes) getSessionFactory().getCurrentSession().createQuery(GET_BY_COMPLIANCE_CODE)
				.setString("complianceCode", complianceCode).setCacheable(true).uniqueResult();	
	return errorCodes;
	}

	@Override
	public COUErrorCodes getErrorMessage(String errorCode) {
		COUErrorCodes errorCodes = (COUErrorCodes) getSessionFactory().getCurrentSession().createQuery(GET_COU_ERROR_MSG)
				.setString("errCode", errorCode).setCacheable(true).uniqueResult();
		return errorCodes;
	}

	@Override
	public COUErrorCodes getErrorMessage(String errorCode, String responseCode) {
		// TODO Auto-generated method stub
		return null;
	}
}
