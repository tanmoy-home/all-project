package com.rssoftware.ou.tenant.service;

import java.util.List;

import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;

public interface RoleService {
	
	//@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	List<RoleEntity> getRoles();
	
	//@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	String[] getAuthenticRoles();

}
