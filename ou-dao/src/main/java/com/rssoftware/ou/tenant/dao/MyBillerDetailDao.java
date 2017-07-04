package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.database.entity.tenant.MyBillerDetail;

public interface MyBillerDetailDao extends GenericDao<MyBillerDetail, String> {
	public List<MyBillerDetail> getActiveBillers();

	public BillDetails getBillDetails(String billerId, List<String> custParams);

	public List<String> getAllCurrentBillerIds();
}
