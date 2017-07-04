package com.rssoftware.ou.service;

import com.rssoftware.ou.database.entity.tenant.admin.RolePrivilegeEntity;

public interface RolePrivilegeService {

	public Iterable<RolePrivilegeEntity> findPrivilegeByRoleid(Long roleId);
}
