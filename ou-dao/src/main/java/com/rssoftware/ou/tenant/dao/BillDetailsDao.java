package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.BillDetails;


public interface BillDetailsDao extends GenericDao<BillDetails, String>{

	public BillDetails getBillDetails(String billerId, List<String> custParams);
}
