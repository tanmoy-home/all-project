package com.rssoftware.ou.tenant.dao.impl;

import java.util.Date;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ComplaintReport;
import com.rssoftware.ou.tenant.dao.ComplaintReportDao;

@Repository
public class ComplaintReportDaoImpl extends GenericDynamicDaoImpl<ComplaintReport, Long> implements ComplaintReportDao {

	public ComplaintReportDaoImpl() {
		super(ComplaintReport.class);
	}
	private static final String GET_COMPLAINT_REPORT = "select a from ComplaintReport a where a.crtnTs between :selectedDate and :selectedDate+1";
	private static org.joda.time.format.DateTimeFormatter formatterMMddyyyy = DateTimeFormat.forPattern("MM/dd/yyyy");

	@Override
	public ComplaintReport getComplaintReport(String date) {
		Date selectedDate = formatterMMddyyyy.parseDateTime(date).toDate();
		ComplaintReport complaintReport = (ComplaintReport) getSessionFactory().getCurrentSession().createQuery(GET_COMPLAINT_REPORT)
				.setDate("selectedDate", selectedDate)
				.uniqueResult();

		return complaintReport;
	}
}
