package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PortalUserDetail;
import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.tenant.dao.UserDetailsDao;
import com.rssoftware.ou.tenant.dao.UserRoleDao;

@Repository
public class UserRoleDaoImpl extends GenericDynamicDaoImpl<PortalUserRole, String> implements UserRoleDao {

	public UserRoleDaoImpl() {
		super(PortalUserRole.class);
	}

}
