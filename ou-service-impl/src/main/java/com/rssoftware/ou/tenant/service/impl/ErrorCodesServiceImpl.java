package com.rssoftware.ou.tenant.service.impl;

import org.bbps.schema.ErrorMessage;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import com.rssoftware.ou.common.ErrorCode;
import com.rssoftware.ou.database.entity.tenant.COUErrorCodes;
import com.rssoftware.ou.database.entity.tenant.ErrorCodes;
import com.rssoftware.ou.tenant.dao.ErrorCodesDao;
import com.rssoftware.ou.tenant.service.ErrorCodesService;
@Service
public class ErrorCodesServiceImpl implements ErrorCodesService{
	

    private final static Logger logger = LoggerFactory.getLogger(ErrorCodesServiceImpl.class);

    	
	@Autowired
	ErrorCodesDao errorCodesDao;

	@Override
	public ErrorCodes searchByKey(ErrorCode errorKey) {
		return errorCodesDao.load(errorKey.name());
	}

	@Override
	public ErrorCodes searchByComplianceCode(String complianceCode) {
		return errorCodesDao.getByComplianceId(complianceCode);
	}
	/*
	 * ErrorMessage error = new ErrorMessage();
			error.setErrorCd("headType");
			error.setErrorDtl("Header object is null.");
	 */
	@Override
	public ErrorMessage getErrorMessage(ErrorCode errorKey){
		
		
		ErrorMessage error = new ErrorMessage();
		
		try{
		logger.info("errorKey:"+errorKey.name());
		ErrorCodes ErrorCodesDetails=errorCodesDao.get(errorKey.name());
		logger.info("errorKey:"+errorKey.name());
		error.setErrorCd(ErrorCodesDetails.getErrorCode());
		error.setErrorDtl(ErrorCodesDetails.getErrorMessage());
		}catch(Exception ex){
			ErrorCodes ErrorCodesDetails=errorCodesDao.get("SYSTEM_ERROR");
			error.setErrorCd(ErrorCodesDetails.getErrorCode());
			error.setErrorDtl(ErrorCodesDetails.getErrorMessage());
			ex.printStackTrace();
		}
		return error;
	}

	@Override
	public String getErrorMessage(String errorCode) {
		
		COUErrorCodes obj = errorCodesDao.getErrorMessage(errorCode);
		if (obj != null) {
			return obj.getErrorMessage();
		}
		return null;
	}
	
	@Override
	public COUErrorCodes getCOUErrorCodes(String errorCode) {		
		return errorCodesDao.getErrorMessage(errorCode);
	}

}
