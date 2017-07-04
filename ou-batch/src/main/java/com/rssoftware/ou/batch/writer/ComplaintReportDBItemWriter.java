package com.rssoftware.ou.batch.writer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.ou.batch.to.ComplaintReport;
import com.rssoftware.ou.tenant.dao.impl.ComplaintReportDaoImpl;

public class ComplaintReportDBItemWriter implements ItemWriter<ComplaintReport> {
	private static Logger log = LoggerFactory.getLogger(ComplaintReportDBItemWriter.class);

	@Autowired
	ComplaintReportDaoImpl complaintReportDao;

	@Override
	public void write(List<? extends ComplaintReport> items) throws Exception {
		if (items != null) {
			for (ComplaintReport data : items) {
				com.rssoftware.ou.database.entity.tenant.ComplaintReport complaintReport = new com.rssoftware.ou.database.entity.tenant.ComplaintReport();
				complaintReport.setBbpouName(data.getBbpouName());
				complaintReport.setOnUsoutstandingLastWeekCount(data.getOnUsoutstandingLastWeekCount()!=null?data.getOnUsoutstandingLastWeekCount():BigDecimal.ZERO);
				complaintReport.setOnUsreceivedThisWeekCount(data.getOnUsreceivedThisWeekCount()!=null?data.getOnUsreceivedThisWeekCount():BigDecimal.ZERO);
				complaintReport.setOnUsTot(data.getOnUsTot()!=null?data.getOnUsTot():BigDecimal.ZERO);
				complaintReport.setOffUsoutstandingLastWeekCount(data.getOffUsoutstandingLastWeekCount()!=null?data.getOffUsoutstandingLastWeekCount():BigDecimal.ZERO);
				complaintReport.setOffUsreceivedThisWeekCount(data.getOffUsreceivedThisWeekCount()!=null?data.getOffUsreceivedThisWeekCount():BigDecimal.ZERO);
				complaintReport.setOffUsTot(data.getOffUsTot()!=null?data.getOffUsTot():BigDecimal.ZERO);
				complaintReport.setOnUsResolvedCount(data.getOnUsResolvedCount()!=null?data.getOnUsResolvedCount():BigDecimal.ZERO);
				complaintReport.setOffUsResolvedCount(data.getOffUsResolvedCount()!=null?data.getOffUsResolvedCount():BigDecimal.ZERO);
				complaintReport.setOnUsPendingCount(data.getOnUsPendingCount()!=null?data.getOnUsPendingCount():BigDecimal.ZERO);
				complaintReport.setOffUsPendingCount(data.getOffUsPendingCount()!=null?data.getOffUsPendingCount():BigDecimal.ZERO);
				complaintReport.setTxnBasedCount(data.getTxnBasedCount()!=null?data.getTxnBasedCount():BigDecimal.ZERO);
				complaintReport.setServiceBasedCount(data.getServiceBasedCount());
				complaintReport.setCrtnTs(new Timestamp(System.currentTimeMillis()));
				complaintReportDao.createOrUpdate(complaintReport);
				log.debug("Inserted into complaint_report table!");
			}
		}
	}

}