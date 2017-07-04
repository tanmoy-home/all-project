package com.rssoftware.ou.tenant.dao.impl;

import java.util.Date;
import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SegmentReport;
import com.rssoftware.ou.tenant.dao.SegmentReportDao;

@Repository
public class SegmentReportDaoImpl extends GenericDynamicDaoImpl<SegmentReport, Long> implements SegmentReportDao {

	public SegmentReportDaoImpl() {
		super(SegmentReport.class);
	}
	private static final String GET_SEGMENT_REPORT = "select a from SegmentReport a where a.crtnTs between :selectedDate and :selectedDate+1";
	private static org.joda.time.format.DateTimeFormatter formatterMMddyyyy = DateTimeFormat.forPattern("MM/dd/yyyy");
	@Override
	public List<SegmentReport> getSegmentReport(String date) {
		Date selectedDate = formatterMMddyyyy.parseDateTime(date).toDate();
		List<SegmentReport> segmentReports =  getSessionFactory().getCurrentSession().createQuery(GET_SEGMENT_REPORT)
				.setDate("selectedDate", selectedDate)
				.list();

		return segmentReports;
	}

}
