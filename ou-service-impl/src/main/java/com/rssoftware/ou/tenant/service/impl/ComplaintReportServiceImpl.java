package com.rssoftware.ou.tenant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.ComplaintReportResp;
import com.rssoftware.ou.database.entity.tenant.ComplaintReport;
import com.rssoftware.ou.tenant.dao.ComplaintReportDao;
import com.rssoftware.ou.tenant.service.ComplaintReportService;

@Service
public class ComplaintReportServiceImpl implements ComplaintReportService {

	private final static Logger logger = LoggerFactory
			.getLogger(ComplaintReportServiceImpl.class);

	@Autowired
	ComplaintReportDao complaintReportDao;

	@Override
	public ComplaintReportResp getComplaintReport(String selectedDate) {
		ComplaintReport complaintReport = complaintReportDao.getComplaintReport(selectedDate);
		return mapToJaxb(complaintReport);
	}

	private ComplaintReportResp mapToJaxb(ComplaintReport complaintReport) {
		ComplaintReportResp complaintReportResp = new ComplaintReportResp();
		
		if(complaintReport!=null) {
			if(logger.isDebugEnabled()) {
				logger.debug("Complaint Report Fetched Id: " +complaintReport.getReportId());
			}
			complaintReportResp.setBbpouName(complaintReport.getBbpouName());
			complaintReportResp.setOnUsoutstandingLastWeekCount(complaintReport.getOnUsoutstandingLastWeekCount());
			complaintReportResp.setOnUsreceivedThisWeekCount(complaintReport.getOnUsreceivedThisWeekCount());
			complaintReportResp.setOnUsTot(complaintReport.getOnUsTot());

			complaintReportResp.setOffUsoutstandingLastWeekCount(complaintReport.getOffUsoutstandingLastWeekCount());
			complaintReportResp.setOffUsreceivedThisWeekCount(complaintReport.getOffUsreceivedThisWeekCount());
			complaintReportResp.setOffUsTot(complaintReport.getOffUsTot());
			
			complaintReportResp.setOnUsResolvedCount(complaintReport.getOnUsResolvedCount());
			complaintReportResp.setOffUsResolvedCount(complaintReport.getOffUsResolvedCount());
			
			complaintReportResp.setOnUsPendingCount(complaintReport.getOnUsPendingCount());
			complaintReportResp.setOffUsPendingCount(complaintReport.getOffUsPendingCount());
			

			complaintReportResp.setTxnBasedCount(complaintReport.getTxnBasedCount());
			complaintReportResp.setServiceBasedCount(complaintReport.getServiceBasedCount());
		}
		
		return complaintReportResp;	
	}

}
