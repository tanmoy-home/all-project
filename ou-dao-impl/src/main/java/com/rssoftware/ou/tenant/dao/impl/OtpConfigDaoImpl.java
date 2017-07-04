package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.OtpConfig;
import com.rssoftware.ou.tenant.dao.OtpConfigDao;

@Repository
public class OtpConfigDaoImpl extends GenericDynamicDaoImpl<OtpConfig, Long>implements OtpConfigDao{
	
	public OtpConfigDaoImpl() {
		super(OtpConfig.class);
	}

}
