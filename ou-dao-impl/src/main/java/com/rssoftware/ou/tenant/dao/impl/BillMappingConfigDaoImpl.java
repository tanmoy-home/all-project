package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.BillMappingConfig;
import com.rssoftware.ou.tenant.dao.BillMappingConfigDao;

@Repository
public class BillMappingConfigDaoImpl extends GenericDynamicDaoImpl<BillMappingConfig, String>implements BillMappingConfigDao{

	public BillMappingConfigDaoImpl() {
		super(BillMappingConfig.class);
	}

}
