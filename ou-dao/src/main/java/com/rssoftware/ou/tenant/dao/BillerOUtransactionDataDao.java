package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.BillerOUtransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;

public interface BillerOUtransactionDataDao extends GenericDao<BillerOUtransactionData, TransactionDataPK>{
	public void updateBOUBlrReconStatus(String txnRefId);

	public BillerOUtransactionData getByTransactionRefId(String txnRefId);
}
