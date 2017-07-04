package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.TransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;

public interface TransactionDataDao extends GenericDao<TransactionData, TransactionDataPK> {

	public TransactionData getPaymentTransactionDataByTranRefId(String tranRefId);
	public List<TransactionData> getFilteredBillers(String mobilenum);
	public List<TransactionData> getFilteredTransactionView(String strId);
	public List<TransactionData> getAllTxnByMobile(String mobNo);
	public Long countTxnByType(String txnType, String agentId);
	public String totalAmountCollected(String txnType, String agentId, String startDate, String endDate);
	public List<TransactionData> getFilteredTransactionByDate(String agentId, String strStartDate, String strEndDate);
	public List<TransactionData> getFilteredTransactionByDateRangeWithMobile(String mobile, String strStartDate, String strEndDate);

}