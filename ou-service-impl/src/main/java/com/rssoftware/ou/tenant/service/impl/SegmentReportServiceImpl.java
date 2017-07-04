package com.rssoftware.ou.tenant.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.SegmentReportResp;
import com.rssoftware.ou.common.SegmentReportRespList;
import com.rssoftware.ou.database.entity.tenant.SegmentReport;
import com.rssoftware.ou.tenant.dao.SegmentReportDao;
import com.rssoftware.ou.tenant.service.SegmentReportService;

@Service
public class SegmentReportServiceImpl implements SegmentReportService {

	private final static Logger logger = LoggerFactory
			.getLogger(SegmentReportServiceImpl.class);

	@Autowired
	SegmentReportDao segmentReportDao;

	@Override
	public SegmentReportRespList getSegmentReport(String selectedDate) {
		List<SegmentReport> segmentReports = segmentReportDao.getSegmentReport(selectedDate);
		SegmentReportRespList segmentReportList = new SegmentReportRespList();
				for(SegmentReport report:segmentReports) {
					segmentReportList.getSegmentReportResps().add(mapToJaxb(report));
				}
				return segmentReportList;
	}

	private SegmentReportResp mapToJaxb(SegmentReport segmentReport) {
		SegmentReportResp segmentReportResp = new SegmentReportResp();
		
		if(segmentReport!=null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Segment Report Fetched Id: " +segmentReport.getReportId());
			}
			segmentReportResp.setBbpouName(segmentReport.getBbpouName());
			segmentReportResp.setBlrCategory(segmentReport.getBlrCategory());
			segmentReportResp.setBlrName(segmentReport.getBlrName());
			segmentReportResp.setOffUsCount(segmentReport.getOffUsCount());
			segmentReportResp.setOffUsTot(segmentReport.getOffUsTot());
			segmentReportResp.setOnUsCount(segmentReport.getOnUsCount());
			segmentReportResp.setOnUsTot(segmentReport.getOnUsTot());
		}
		
		return segmentReportResp;	
	}

}
