package com.rssoftware.ou.dao.impl;

import org.hibernate.SessionFactory;

import com.rssoftware.framework.hibernate.dao.impl.GenericDaoImpl;
import com.rssoftware.ou.dao.TenantDetailDao;
import com.rssoftware.ou.database.entity.global.TenantDetail;

public class TenantDetailDaoImpl extends GenericDaoImpl<TenantDetail, String> implements
		TenantDetailDao {

	public TenantDetailDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, TenantDetail.class);
	}
}
