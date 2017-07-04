package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationField;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationFieldPK;
import com.rssoftware.ou.tenant.dao.PgIntegrationFieldDao;

@Repository
public class PgIntegrationFieldDaoImpl extends GenericDynamicDaoImpl<PgIntegrationField, PgIntegrationFieldPK>
		implements PgIntegrationFieldDao {

	public PgIntegrationFieldDaoImpl() {
		super(PgIntegrationField.class);
	}
}
