package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.TenantTemplate;
import com.rssoftware.ou.tenant.dao.TenantTemplateDao;

@Repository
public class TenantTemplateDaoImpl extends GenericDynamicDaoImpl<TenantTemplate, String>
		implements TenantTemplateDao {

	public TenantTemplateDaoImpl() {
		super(TenantTemplate.class);
	}

}
