package com.rssoftware.ou.tenant.dao.impl;


import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.tenant.dao.PortaUserRoleDao;

@Repository
public class PortalUserRoleDaoImpl extends GenericDynamicDaoImpl<PortalUserRole, String> implements PortaUserRoleDao {

	public PortalUserRoleDaoImpl() {
		super(PortalUserRole.class);
	}

}
