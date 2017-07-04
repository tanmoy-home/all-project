package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.database.entity.tenant.BillerDetail;

public interface BillerDao extends GenericDao<BillerDetail, String> {

	public List<BillerDetail> getActiveBillers();
	
	public List<BillerDetail> searchParentBiller();

	public List<BillerDetail> searchBiller(String loggedInUsersOuId);

	public List<BillerDetail> fetchAll(int offset, int limit, EntityStatus... entityStatus);

	public List<BillerDetail> fetchAllForUser(int offset, int limit, String loggedInUsersOuId, EntityStatus... entityStatus);

	public List<BillerDetail> fetch(String billerId, EntityStatus... entityStatus);

	public List<BillerDetail> exportReport(String loggedInUsersOuId);
	
	public List<BillerDetail> getSubBillersByParentId(String parentbillerId);
	
	public BillDetails getBillDetails(String billerId, List<String> custParams);

	public  BillDetails getBillDetails(String billerId, String custParamvalue1,
			String custParamvalue2, String custParamvalue3,
			String custParamvalue4, String custParamvalue5);

	List<String> getAllCurrentBillerIds();

}
