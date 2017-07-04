package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;

public interface FinTransactionDataDao extends GenericDao<FinTransactionData, String> {
	public void updateReconStatus(String txnRefId);

}
