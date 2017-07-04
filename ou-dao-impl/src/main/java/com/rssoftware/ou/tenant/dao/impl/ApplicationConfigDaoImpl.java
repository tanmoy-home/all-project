package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ApplicationConfig;
import com.rssoftware.ou.database.entity.tenant.ApplicationConfigPK;
import com.rssoftware.ou.tenant.dao.ApplicationConfigDao;

@Repository
public class ApplicationConfigDaoImpl extends GenericDynamicDaoImpl<ApplicationConfig, ApplicationConfigPK> implements ApplicationConfigDao {

	public ApplicationConfigDaoImpl() {
		super(ApplicationConfig.class);
	}

}
