package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;

public interface InterchangeFeeService {
		
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void save(InterchangeFeeView interchangeFeeView);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<InterchangeFeeView> fetchAll();

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public InterchangeFeeView fetch(String interchangeFeeId);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void delete(InterchangeFeeView serviceTaxConfView);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public List<InterchangeFeeView> fetchAllInterchangeFeeByBillerId(String blrId);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public int deleteAllInterchangeFeeByBillerId(String billerId);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public InterchangeFeeView fetchByBillerIdFeeCode(String billerId, String feeCode);
}
