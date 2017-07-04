package com.rssoftware.ou.batch.writer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.ou.batch.to.SegmentReport;
import com.rssoftware.ou.tenant.dao.impl.SegmentReportDaoImpl;

public class SegmentReportDBItemWriter implements ItemWriter<SegmentReport> {
	private static Logger log = LoggerFactory.getLogger(SegmentReportDBItemWriter.class);

	@Autowired
	SegmentReportDaoImpl segmentReportDao;

	@Override
	public void write(List<? extends SegmentReport> items) throws Exception {
		if (items != null) {
			for (SegmentReport data : items) {
				com.rssoftware.ou.database.entity.tenant.SegmentReport segmentReport = new com.rssoftware.ou.database.entity.tenant.SegmentReport();
				segmentReport.setBbpouName(data.getBbpouName());
				segmentReport.setBlrCategory(data.getBlrCategory());
				segmentReport.setBlrName(data.getBlrName());
				segmentReport.setOnUsCount(data.getOnUsCount()!=null?data.getOnUsCount():BigDecimal.ZERO);
				segmentReport.setOnUsTot(data.getOnUsTot()!=null?data.getOnUsTot():BigDecimal.ZERO);
				segmentReport.setOffUsCount(data.getOffUsCount()!=null?data.getOffUsCount():BigDecimal.ZERO);
				segmentReport.setOffUsTot(data.getOffUsTot()!=null?data.getOffUsTot():BigDecimal.ZERO);
				segmentReport.setCrtnTs(new Timestamp(System.currentTimeMillis()));
				segmentReportDao.createOrUpdate(segmentReport);
				log.debug("Inserted into segment_report table!");
			}
		}
	}

}