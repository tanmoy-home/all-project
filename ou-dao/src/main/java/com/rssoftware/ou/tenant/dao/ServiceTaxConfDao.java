package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ServiceTaxConf;

public interface ServiceTaxConfDao extends GenericDao<ServiceTaxConf, String>  {
	public List<ServiceTaxConf> fetchAllActiveList();
}
