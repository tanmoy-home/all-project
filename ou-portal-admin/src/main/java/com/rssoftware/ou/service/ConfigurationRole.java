package com.rssoftware.ou.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;

public interface ConfigurationRole {

	//public RoleVo saveRole(RoleVo roleVo);
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public RoleEntity getRole(RoleEntity roleEntity);
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public  Iterable<RoleEntity> findAllRoles();
	//public RoleVo findById(Long id);
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public RoleEntity saveRole(RoleEntity roleEntity);
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public RoleEntity findById(Long id);
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void deleteRoleByOrganizationId(Long id);
	
}
