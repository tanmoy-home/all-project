package com.rssoftware.ou.service;

import java.util.List;

import com.rssoftware.ou.database.entity.global.TenantDatasource;

public interface TenantDatasourceService {
	String create(TenantDatasource globalParam);
	List<TenantDatasource> fetchAll();
	TenantDatasource fetchByTenantId(String tenantId);
}
