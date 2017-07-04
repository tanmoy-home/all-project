package com.rssoftware.ou.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.rssoftware.ou.batch.to.SegmentReport;

public class SegmentReportItemProcessor implements ItemProcessor<SegmentReport, SegmentReport> {
	private String ouName;
	@Override
	public SegmentReport process(SegmentReport item) throws Exception {
		item.setBbpouName(ouName);
		return item;
	}
	public String getOuName() {
		return ouName;
	}
	public void setOuName(String ouName) {
		this.ouName = ouName;
	}
	
}