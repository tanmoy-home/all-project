package com.rssoftware.ou.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;

import com.rssoftware.framework.hibernate.dao.impl.GenericDaoImpl;
import com.rssoftware.ou.dao.TenantDatasourceDao;
import com.rssoftware.ou.database.entity.global.TenantDatasource;

public class TenantDatasourceDaoImpl extends GenericDaoImpl<TenantDatasource, String> implements TenantDatasourceDao {
	private static final String QUERY_GET_ALL = "select a from TenantDatasource a, TenantDetail b where a.tenantId = b.tenantId";
	private static final String QUERY_GET_BY_ID = "select a from TenantDatasource a, TenantDetail b where a.tenantId = b.tenantId and a.tenantId = :tenantId"; 
	
	
	public TenantDatasourceDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, TenantDatasource.class);
	}
	
	@Override
	public List<TenantDatasource> getAll() {
		@SuppressWarnings("unchecked")
		List<TenantDatasource> allDataSources = getSessionFactory().getCurrentSession().createQuery(QUERY_GET_ALL).list();
		return allDataSources;
	}
	
	@Override
	public TenantDatasource get(String id) {
		@SuppressWarnings("unchecked")
		List<TenantDatasource> dataSources = getSessionFactory().getCurrentSession().createQuery(QUERY_GET_BY_ID).setString("tenantId", id).list();
		if (dataSources != null && !dataSources.isEmpty()){
			return getUnproxiedObject(dataSources.get(0));
		}
		return null;
	}
}
