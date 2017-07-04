package com.rssoftware.ou.tenant.dao;

import java.sql.Timestamp;
import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ReconSummary;

public interface ReconSummaryDao extends GenericDao<ReconSummary, String> {

	List<ReconSummary> getReconList(Timestamp startDate, Timestamp endDate);

}
