package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.ServiceTaxView;

public interface ServiceTaxService {

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<ServiceTaxView> fetchAll();

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public ServiceTaxView fetch(String serviceTaxId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public ServiceTaxView save(ServiceTaxView serviceTaxView);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(ServiceTaxView serviceTaxView);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<ServiceTaxView> fetchAllActiveList();

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public ServiceTaxView update(ServiceTaxView serviceTaxView);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<ServiceTaxView> getServiceTax(String date);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public ServiceTaxView activate(String serviceTaxId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public ServiceTaxView deactivate(String serviceTaxId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public ServiceTaxView reject(String serviceTaxId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public ServiceTaxView pendingDeactivate(String serviceTaxId);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<ServiceTaxView> fetchAllActive();

}