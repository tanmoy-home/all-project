package com.rssoftware.ou.tenant.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.model.tenant.ContactDetailsView;

public interface ContactDetailsService {

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	void submit(ContactDetailsView cdv) throws ValidationException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	void update(ContactDetailsView cdv) throws ValidationException;

	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	List<ContactDetailsView> fetchFunctionallyActiveList(int pageNo,
			int pageSize) throws ValidationException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	List<ContactDetailsView> fetchDeactivatedList(int pageNo, int pageSize)
			throws ValidationException;*/

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	ContactDetailsView fetch(String linkedEntityID, String linkedEntityType,
			String contactType, boolean fromPendingApproval, String entityStatus)
			throws ValidationException;
	
	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	List<ContactDetailsView> fetchPendingApprovalList()
			throws ValidationException;*/

}
