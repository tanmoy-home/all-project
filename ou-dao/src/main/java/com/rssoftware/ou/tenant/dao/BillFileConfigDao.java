package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.BillFileConfig;


public interface BillFileConfigDao extends GenericDao<BillFileConfig, String>{
	public BillFileConfig getBillFileConfigByBillerId(String id);
}
