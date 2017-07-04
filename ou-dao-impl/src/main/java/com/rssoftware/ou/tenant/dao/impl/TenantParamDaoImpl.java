package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.TenantParam;
import com.rssoftware.ou.tenant.dao.TenantParamDao;

@Repository
public class TenantParamDaoImpl extends GenericDynamicDaoImpl<TenantParam, String> implements
		TenantParamDao {

	public TenantParamDaoImpl() {
		super(TenantParam.class);
	}
}
