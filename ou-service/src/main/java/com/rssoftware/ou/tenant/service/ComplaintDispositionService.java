package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.ComplaintDisposition;
import com.rssoftware.ou.model.tenant.ComplaintDispositionView;

public interface ComplaintDispositionService {
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<ComplaintDispositionView> fetchALLDisposition();

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<ComplaintDisposition> getDispositionsList();

}
