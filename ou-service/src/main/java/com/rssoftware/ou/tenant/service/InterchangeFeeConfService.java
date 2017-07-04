package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;

public interface InterchangeFeeConfService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void save(InterchangeFeeConfView interchageFeeConfView);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<InterchangeFeeConfView> fetchAll();

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public InterchangeFeeConfView fetch(String interchangeFeeConfId);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void delete(InterchangeFeeConfView interchangeFeeConfView);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<InterchangeFeeConfView> fetchAllInterchangeFeeConfByBillerId(String blrId);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public int deleteAllInterchangeFeeConfByBillerId(String billerId);
}
