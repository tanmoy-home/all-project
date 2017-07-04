package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.ComplaintserviceReasonsView;

public interface ComplaintserviceReasonsService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<ComplaintserviceReasonsView> fetchServiceReasonsByAgent();
    
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<ComplaintserviceReasonsView> fetchServiceReasonsByBiller();

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<ComplaintserviceReasonsView> fetchServiceReasonsBySystem();

}
