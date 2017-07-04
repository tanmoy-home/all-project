package com.rssoftware.ou.tenant.service;

import java.io.IOException;

import org.bbps.schema.AgentType;
import org.bbps.schema.PmtMtdType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;

public interface CCFCalculatorService 
{
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public String calculateCCF(String billerId, String billAmount, AgentType agent, PmtMtdType pmtMtdType) throws IOException;
}
