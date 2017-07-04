package com.rssoftware.ou.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.rssoftware.ou.batch.to.ReconBOU;

public class ReconBOUItemProcessor implements ItemProcessor<ReconBOU, ReconBOU> {
	
	@Override
	public ReconBOU process(ReconBOU item) throws Exception {
		return item;
	}
}