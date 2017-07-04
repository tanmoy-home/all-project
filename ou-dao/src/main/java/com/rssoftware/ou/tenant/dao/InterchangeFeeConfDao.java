package com.rssoftware.ou.tenant.dao;
import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.InterchangeFeeConf;

public interface InterchangeFeeConfDao extends GenericDao<InterchangeFeeConf, String>  {
	public List<InterchangeFeeConf> fetchAllInterchangeFeeConfByBillerId(String blrId);
	public int deleteAllInterchangeFeeConfByBillerId(String blrId);
}
