package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PortalUserDetail;
import com.rssoftware.ou.tenant.dao.UserDetailsDao;

@Repository
public class UserDetailsDaoImpl extends	GenericDynamicDaoImpl<PortalUserDetail, String> implements	UserDetailsDao {

	public UserDetailsDaoImpl() {
		super(PortalUserDetail.class);
	}

}
