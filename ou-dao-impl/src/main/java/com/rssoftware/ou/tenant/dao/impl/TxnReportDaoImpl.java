package com.rssoftware.ou.tenant.dao.impl;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.TxnReport;
import com.rssoftware.ou.tenant.dao.TxnReportDao;


@Repository
public class TxnReportDaoImpl extends GenericDynamicDaoImpl<TxnReport, Long> implements TxnReportDao {

	public TxnReportDaoImpl() {
		super(TxnReport.class);
	}
	private static final String GET_TXN_REPORT = "select a from TxnReport a where a.crtnTs between :selectedDate and :selectedDate+1";
	private static org.joda.time.format.DateTimeFormatter formatterMMddyyyy = DateTimeFormat.forPattern("MM/dd/yyyy");

	@Override
	public TxnReport getTxnReport(String date) {
		Date selectedDate = formatterMMddyyyy.parseDateTime(date).toDate();
		TxnReport txnReport = (TxnReport) getSessionFactory().getCurrentSession().createQuery(GET_TXN_REPORT)
				.setDate("selectedDate", selectedDate)
				.uniqueResult();

		return txnReport;
	}
}
