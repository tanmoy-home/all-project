package com.rssoftware.ou.repository;

import org.springframework.data.repository.CrudRepository;

import com.rssoftware.ou.database.entity.tenant.admin.RolePrivilegeEntity;

public interface RolePrivilegeRepository extends CrudRepository<RolePrivilegeEntity, Long>{

	public Iterable<RolePrivilegeEntity> findPrivilegeByRoleid(Long roleId);
}
