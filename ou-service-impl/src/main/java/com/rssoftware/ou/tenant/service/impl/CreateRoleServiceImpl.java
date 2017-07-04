package com.rssoftware.ou.tenant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.tenant.dao.PortaUserRoleDao;
import com.rssoftware.ou.tenant.service.CreateRoleService;

@Service
public class CreateRoleServiceImpl implements CreateRoleService {
	
	@Autowired
	PortaUserRoleDao portalUserRoleDao;

	@Override
	public void roleCreation(PortalUserRole createRole) {
		portalUserRoleDao.create(createRole);

	}

}
