package com.rssoftware.ou.service;

import java.util.List;

import com.rssoftware.ou.database.entity.global.TenantDetail;

public interface TenantDetailService {
	String create(TenantDetail tenantDetail);
	List<TenantDetail> fetchAll();
	TenantDetail fetchByTenantId(String tenantId);
	String getOUName(String tenantId);
}
