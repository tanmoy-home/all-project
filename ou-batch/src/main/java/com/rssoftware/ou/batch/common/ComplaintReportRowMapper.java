package com.rssoftware.ou.batch.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.ComplaintReport;

public class ComplaintReportRowMapper implements RowMapper<ComplaintReport> {

	@Override
	public ComplaintReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		ComplaintReport complaintReport = new ComplaintReport();
		complaintReport.setTxnBasedCount(rs.getBigDecimal("txnCount"));
		complaintReport.setServiceBasedCount(rs.getBigDecimal("serviceCount"));
		if(complaintReport.getTxnBasedCount()!=null && complaintReport.getServiceBasedCount()!=null)
			complaintReport.setOffUsreceivedThisWeekCount(complaintReport.getTxnBasedCount().add(complaintReport.getServiceBasedCount()));
		else if(complaintReport.getTxnBasedCount()!=null)
			complaintReport.setOffUsreceivedThisWeekCount(complaintReport.getTxnBasedCount());
		else
			complaintReport.setOffUsreceivedThisWeekCount(complaintReport.getServiceBasedCount());
		return complaintReport;
	}

}