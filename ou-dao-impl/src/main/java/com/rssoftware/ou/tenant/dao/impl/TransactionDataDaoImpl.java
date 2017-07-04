package com.rssoftware.ou.tenant.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.TransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;
import com.rssoftware.ou.tenant.dao.TransactionDataDao;

@Repository
public class TransactionDataDaoImpl extends GenericDynamicDaoImpl<TransactionData, TransactionDataPK>
		implements TransactionDataDao {

	private static final String GET_PAYMENT_TRANSACTION_DATA_BY_TRAN_REF_ID = "select a from TransactionData a where a.txnRefId=:txnRefId";
	private static final String ALL_TXN_BY_MOBILENO = "select t from TransactionData t where t.mobile = :MOBILE and t.id.txnType = :txnType and t.currentStatus = :currentStatus";
	private static final String GET_FILTERED_BILLERS = "select a from TransactionData a where a.mobile = :mobile order by updtTs desc,billerId";
	private static final String GET_FILTERED_AGENT = "select a from TransactionData a where a.agentID = :AGENTID order by updtTs desc";
	//private static final String TXN_COUNT = "select count(*) from TransactionData a where a.agentID = :agentID and a.id.txnType = :txnType and a.currentStatus = :currentStatus and a.updtTs >= to_timestamp(:strStartDate,'DD-MM-YYYY')";
	private static final String TXN_SUM = "select sum (case when a.billAmount = null then 0 else a.billAmount end) from TransactionData a where a.agentID = :agentID and a.id.txnType = :txnType and a.currentStatus = :currentStatus";
	private static final String TXN_SUM_CURRENT_DATE_CLAUSE = " and date(a.crtnTs) = to_timestamp(:CURRENTDATE,'DD-MM-RR'))";
	private static final String TXN_SUM_DATE_RANGE_CLAUSE = " and date(a.crtnTs) Between to_timestamp(:startDate,'DD-MM-RR') and to_timestamp(:endDate,'DD-MM-RR')";
	// private static final String GET_FILTERED_DATE = "select a from
	// TransactionData a where a.agentID = :agentID and date(a.crtnTs) >=
	// to_timestamp(:strStartDate,'DD-MM-YYYY') and date(a.crtnTs) <=
	// to_timestamp(:strEndDate,'DD-MM-YYYY')";
	private static final String GET_FILTERED_DATE = "select a from TransactionData a where a.agentID = :agentID and a.crtnTs >= to_timestamp(:strStartDate,'DD-MM-YYYY') and a.crtnTs <= to_timestamp(:strEndDate,'DD-MM-YYYY')";
	private static final String GET_FILTERED_DATE_RANGE_WITH_MOBILE = "select a from TransactionData a where  a.mobile = :MOBILE and a.crtnTs >= to_timestamp(:strStartDate,'YYYY-MM-DD') and a.crtnTs <= to_timestamp(:strEndDate,'YYYY-MM-DD') and a.id.txnType=:txnType";

	// select * from TRANSACTION_DATA where MOBILE='8670837306' order by UPDT_TS
	// desc, billerid;
	public TransactionDataDaoImpl() {
		super(TransactionData.class);
	}

	@Override
	public List<TransactionData> getFilteredBillers(String mobilenum) {
		List<TransactionData> billers = getSessionFactory().getCurrentSession().createQuery(GET_FILTERED_BILLERS)
				.setString("mobile", mobilenum).list();
		return billers;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionData> getAllTxnByMobile(String mobNo) {
		Query query = getSessionFactory().getCurrentSession().createQuery(ALL_TXN_BY_MOBILENO);
		query.setString("MOBILE", mobNo);
		query.setString("txnType", "PAYMENT");
		query.setString("currentStatus", RequestStatus.RESPONSE_SUCCESS.name());
		List<TransactionData> txnList = (List<TransactionData>) query.list();

		/*
		 * List<TransactionData> txnList = (List<TransactionData>)
		 * getSessionFactory().getCurrentSession()
		 * .createQuery(ALL_TXN_BY_MOBILENO).setString("MOBILE", mobNo).list();
		 */
		return txnList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long countTxnByType(String txnType, String agentId) {
		String query = "TXN_DATE LIKE '" + CommonUtils.getFormattedDateyyyy_MM_dd(new Date()) + "%'";
		Criteria criteria = getSessionFactory()
				.getCurrentSession()
				.createCriteria(TransactionData.class)
				.setProjection(Projections.count("agentID"))
				.add(Restrictions.eq("agentID", agentId))
				.add(Restrictions.eq("id.txnType", txnType))
				.add(Restrictions.eq("currentStatus", RequestStatus.RESPONSE_SUCCESS.name()))
				.add(Restrictions.sqlRestriction(query))
				.add(Restrictions.isNotNull("billAmount"));
		
		List<Integer> txnCount = (List<Integer>) criteria.list();
		return Long.parseLong(String.valueOf(txnCount.get(0)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public String totalAmountCollected(String txnType, String agentId, String startDate, String endDate) {
		String query = "";

		if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate))
			query = String.format("CRTN_TS BETWEEN \'%s\' AND \'%s\'", startDate, endDate);
		else
			query = "CRTN_TS LIKE '" + CommonUtils.getDateFromddMMyy(new Date()) + "%'";	//get current date

		Criteria criteria = getSessionFactory()
										.getCurrentSession()
										.createCriteria(TransactionData.class)
										.setProjection(Projections.sum("billAmount"))
										.add(Restrictions.eq("agentID", agentId))
										.add(Restrictions.eq("id.txnType", txnType))
										.add(Restrictions.eq("currentStatus", RequestStatus.RESPONSE_SUCCESS.name()))
										.add(Restrictions.sqlRestriction(query))
										.add(Restrictions.isNotNull("billAmount"));

		List<Integer> txnamt = (List<Integer>) criteria.list();
		return (CollectionUtils.isEmpty(txnamt) || txnamt.get(0) == null) ? "0" : String.valueOf(txnamt.get(0));
	}

	@SuppressWarnings("unchecked")
	@Override
	public TransactionData getPaymentTransactionDataByTranRefId(String txnRefId) {
		List<TransactionData> tempPaymentTransactionDataList = (List<TransactionData>) getSessionFactory()
				.getCurrentSession().createQuery(GET_PAYMENT_TRANSACTION_DATA_BY_TRAN_REF_ID)
				.setString("txnRefId", txnRefId).list();
		if (tempPaymentTransactionDataList != null && tempPaymentTransactionDataList.size() > 0) {
			return tempPaymentTransactionDataList.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<TransactionData> getFilteredTransactionView(String strId) {
		List<TransactionData> agent = (List<TransactionData>) getSessionFactory().getCurrentSession()
				.createQuery(GET_FILTERED_AGENT).setString("AGENTID", strId).list();
		return agent;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<TransactionData> getFilteredTransactionByDate(String agentId, String strStartDate, String strEndDate) {
		//avoid null pointer exception
		if (!(StringUtils.isNotEmpty(strStartDate) || StringUtils.isNotEmpty(strEndDate)))
			return new ArrayList<TransactionData>();
			
		String query = String.format("CRTN_TS BETWEEN \'%s\' AND \'%s\'", strStartDate, strEndDate);

		Criteria criteria = getSessionFactory()
										.getCurrentSession()
										.createCriteria(TransactionData.class)
										.add(Restrictions.eq("agentID", agentId))
										.add(Restrictions.sqlRestriction(query));

		return (List<TransactionData>) criteria.list();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<TransactionData> getFilteredTransactionByDateRangeWithMobile(String mobile, String strStartDate,
			String strEndDate) {
		Query query = getSessionFactory().getCurrentSession().createQuery(GET_FILTERED_DATE_RANGE_WITH_MOBILE);
		query.setString("MOBILE", mobile);
		query.setString("strStartDate", strStartDate);
		query.setString("strEndDate", strEndDate);
		query.setString("txnType", RequestType.PAYMENT.name());
		List<TransactionData> txnDateSearch = (List<TransactionData>) query.list();
		return txnDateSearch;
	}

}