package com.rssoftware.ou.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.rssoftware.ou.batch.to.Recon;

public class ReconItemProcessor implements ItemProcessor<Recon, Recon> {
	
	@Override
	public Recon process(Recon item) throws Exception {
		return item;
	}
}