package com.rssoftware.ou.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.dao.TenantDatasourceDao;
import com.rssoftware.ou.database.entity.global.TenantDatasource;
import com.rssoftware.ou.service.TenantDatasourceService;

@Service
public class TenantDatasourceServiceImpl implements TenantDatasourceService {
	@Autowired
	private TenantDatasourceDao tenantDatasourceDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public String create(TenantDatasource tenantDatasource) {
		return tenantDatasourceDao.create(tenantDatasource);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<TenantDatasource> fetchAll() {
		return tenantDatasourceDao.getAll();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public TenantDatasource fetchByTenantId(String tenantId) {
		return tenantDatasourceDao.get(tenantId);
	}

}
