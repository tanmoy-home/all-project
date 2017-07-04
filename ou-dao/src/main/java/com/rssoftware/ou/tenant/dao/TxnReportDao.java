package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.TxnReport;

public interface TxnReportDao extends GenericDao<TxnReport, Long> {

	public TxnReport getTxnReport(String selectedDate);

}
