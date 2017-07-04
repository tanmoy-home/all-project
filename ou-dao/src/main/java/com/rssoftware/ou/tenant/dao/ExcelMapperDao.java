package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ExcelMapper;

public interface ExcelMapperDao extends GenericDao<ExcelMapper, String> {

	public String getMappedFromName(String mappedToName);

	public String getMappedToName(String mappedFromName);

}
