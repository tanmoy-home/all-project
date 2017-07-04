package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.SegmentReport;

public interface SegmentReportDao extends GenericDao<SegmentReport, Long> {
	public List<SegmentReport> getSegmentReport(String selectedDate);
}
