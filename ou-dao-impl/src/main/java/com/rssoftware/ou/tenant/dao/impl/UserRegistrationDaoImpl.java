package com.rssoftware.ou.tenant.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.database.entity.tenant.UserRegistration;
import com.rssoftware.ou.tenant.dao.UserRegistrationDao;
import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;

@Repository
public class UserRegistrationDaoImpl extends
		GenericDynamicDaoImpl<UserRegistration, String> implements
		UserRegistrationDao {

	public UserRegistrationDaoImpl() {
		super(UserRegistration.class);
	}

}
