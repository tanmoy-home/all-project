package com.rssoftware.ou.service;

import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;

public interface UserAccessService {
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Map<String, Set<String>> findAccessibleServicesForRoles();
}
