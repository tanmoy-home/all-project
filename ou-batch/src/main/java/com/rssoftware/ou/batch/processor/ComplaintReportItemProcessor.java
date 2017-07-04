package com.rssoftware.ou.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.rssoftware.ou.batch.to.ComplaintReport;

public class ComplaintReportItemProcessor implements ItemProcessor<ComplaintReport, ComplaintReport> {
	private String ouName;
	@Override
	public ComplaintReport process(ComplaintReport item) throws Exception {
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