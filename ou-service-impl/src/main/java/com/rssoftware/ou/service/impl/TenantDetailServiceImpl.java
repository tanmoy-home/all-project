package com.rssoftware.ou.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.dao.TenantDetailDao;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.service.TenantDetailService;

@Service
public class TenantDetailServiceImpl implements TenantDetailService {
	@Autowired
	private TenantDetailDao tenantDetailDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public String create(TenantDetail tenantDetail) {
		return tenantDetailDao.create(tenantDetail);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<TenantDetail> fetchAll() {
		return tenantDetailDao.getAll();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public TenantDetail fetchByTenantId(String tenantId) {
		return tenantDetailDao.get(tenantId);
	}

	@Override
	public String getOUName(String tenantId) {
		TenantDetail tenantDetail = fetchByTenantId(tenantId);
		return tenantDetail.getOuName();
	}

}
