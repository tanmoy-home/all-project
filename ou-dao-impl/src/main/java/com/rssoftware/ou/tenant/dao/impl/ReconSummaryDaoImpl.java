package com.rssoftware.ou.tenant.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.ReconSummary;
import com.rssoftware.ou.tenant.dao.ReconSummaryDao;

@Repository
public class ReconSummaryDaoImpl extends
		GenericDynamicDaoImpl<ReconSummary, String> implements ReconSummaryDao {

	private static final String GET_DATA = "select r from ReconSummary r where r.reconTs BETWEEN :startDate AND :endDate";

	public ReconSummaryDaoImpl() {
		super(ReconSummary.class);
	}

	// select recon_summary based on start date and end date
	@SuppressWarnings("unchecked")
	@Override
	public List<ReconSummary> getReconList(Timestamp startDate,
			Timestamp endDate) {
		ScrollableResults results = getSessionFactory().getCurrentSession()
				.createQuery(GET_DATA).setTimestamp("startDate", startDate)
				.setTimestamp("endDate", endDate).setFetchSize(10)
				.scroll(ScrollMode.FORWARD_ONLY);
		getSessionFactory().getCurrentSession().setCacheMode(CacheMode.IGNORE);
		List<ReconSummary> resultList = new LinkedList<ReconSummary>();
		while (results.next()) {
			resultList.add((ReconSummary) results.get(0));
			getSessionFactory().getCurrentSession().clear();
		}
		results.close();
		return resultList;
	}
}
