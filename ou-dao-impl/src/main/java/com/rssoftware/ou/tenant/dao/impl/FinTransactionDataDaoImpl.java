package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.tenant.dao.FinTransactionDataDao;

@Repository
public class FinTransactionDataDaoImpl extends GenericDynamicDaoImpl<FinTransactionData, String>
		implements FinTransactionDataDao {

	public static final String UPDATE_RECON_STATUS = "Update FinTransactionData a set a.reconProcessed=:reconProcessed where a.txnRefId=:txnRefId";
	public FinTransactionDataDaoImpl() {
		super(FinTransactionData.class);
	}
	@Override
	public void updateReconStatus(String txnRefId) {
		getSessionFactory().getCurrentSession().createQuery(UPDATE_RECON_STATUS).setString("txnRefId", txnRefId).setString("reconProcessed", "Y").executeUpdate();
	}
}
