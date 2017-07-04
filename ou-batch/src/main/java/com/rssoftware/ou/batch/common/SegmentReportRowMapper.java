package com.rssoftware.ou.batch.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.SegmentReport;

public class SegmentReportRowMapper implements RowMapper<SegmentReport> {

	@Override
	public SegmentReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		SegmentReport segmentReport = new SegmentReport();
		segmentReport.setBlrCategory(rs.getString("blr_category_name"));
		segmentReport.setBlrName(rs.getString("blr_name"));
		segmentReport.setOffUsCount(rs.getBigDecimal("txnCount"));
		segmentReport.setOffUsTot(rs.getBigDecimal("txnTot"));

		return segmentReport;
	}

}