package com.rssoftware.ou.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.admin.PersistentLoginEntity;

public interface PersistentLoginService {
	//@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void saveLoginEntity(PersistentLoginEntity persistentLoginEntity);
	//@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public PersistentLoginEntity findByUserId(String userId);


}
