package com.rssoftware.ou.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.RolePrivilegeEntity;
import com.rssoftware.ou.repository.RolePrivilegeRepository;
import com.rssoftware.ou.service.RolePrivilegeService;


@Service
public class RolePrivilegeServiceImpl implements RolePrivilegeService {
	@Autowired
	RolePrivilegeRepository rolePrivilegeRepository;

	
	@Override
	public Iterable<RolePrivilegeEntity> findPrivilegeByRoleid(Long roleId) {
		Iterable<RolePrivilegeEntity> rolePrivilegeList = rolePrivilegeRepository.findPrivilegeByRoleid(roleId);
		return rolePrivilegeList;
	}
}
