package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.BillerOUtransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;
import com.rssoftware.ou.tenant.dao.BillerOUtransactionDataDao;

@Repository
public class BillerOUtransactionDataDaoImpl extends GenericDynamicDaoImpl<BillerOUtransactionData, TransactionDataPK> implements BillerOUtransactionDataDao{
	
	public static final String UPDATE_RECON_STATUS = "Update BillerOUtransactionData a set a.bouBlrReconProcessed=:bouBlrReconProcessed where a.txnRefId=:txnRefId";
	public static final String GET_BY_TXNREFID = "select t from BillerOUtransactionData t where t.txnRefId=:txnRefId";
	
	public BillerOUtransactionDataDaoImpl() {
		super(BillerOUtransactionData.class);
	}
	
	@Override
	public void updateBOUBlrReconStatus(String txnRefId) {
		getSessionFactory().getCurrentSession().createQuery(UPDATE_RECON_STATUS).setString("txnRefId", txnRefId).setString("bouBlrReconProcessed", "Y").executeUpdate();
	}

	@Override
	public BillerOUtransactionData getByTransactionRefId(String txnRefId) {
		BillerOUtransactionData txnData=(BillerOUtransactionData) getSessionFactory().getCurrentSession().createQuery(GET_BY_TXNREFID).setString("txnRefId", txnRefId).uniqueResult();
		return txnData;
	}
}
