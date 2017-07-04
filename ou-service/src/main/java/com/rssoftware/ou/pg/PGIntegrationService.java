package com.rssoftware.ou.pg;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;

public interface PGIntegrationService {
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)	
	public List<PGParam> getPGFields(List<PGParam> reqVal) throws IOException, NoSuchAlgorithmException, Exception;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)	
	public List<PGParam> validatePGResponse(List<PGParam> reqVal) throws IOException, NoSuchAlgorithmException, Exception;
	
}
