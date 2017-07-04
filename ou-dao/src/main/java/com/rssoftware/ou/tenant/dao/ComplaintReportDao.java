package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ComplaintReport;

public interface ComplaintReportDao extends GenericDao<ComplaintReport, Long> {
	ComplaintReport getComplaintReport(String selectedDate);

}
