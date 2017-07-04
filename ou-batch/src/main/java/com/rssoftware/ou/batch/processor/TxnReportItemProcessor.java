package com.rssoftware.ou.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.rssoftware.ou.batch.to.TxnReport;

public class TxnReportItemProcessor implements ItemProcessor<TxnReport, TxnReport> {
	private String ouName;
	@Override
	public TxnReport process(TxnReport item) throws Exception {
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