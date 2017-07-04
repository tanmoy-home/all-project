package com.rssoftware.ou.batch.job;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import reactor.bus.Event;
import reactor.bus.EventBus;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.batch.to.BillDump;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.model.tenant.MyBillerView;


@Component
public class BOUBlrReconJob {

	private static Log logger = LogFactory.getLog(BOUBlrReconJob.class);
	
	public void doBOUBlrRecon(String tenantId, List<MyBillerView> activeBillers) {
		logger.debug("Started BOUBlrReconJob..");
		for(MyBillerView bill: activeBillers){
			BillDump billDump = new BillDump();
			billDump.setBillerId(bill.getBlrId());
			billDump.setBillerOU(tenantId);
			//billDump.setBatchType(TypeOfBatch.BILL_PAYMENT);
			BeanLocator.getBean(EventBus.class).notify(CommonConstants.BOU_BLR_RECON_EVENT, Event.wrap(billDump));
		}
		logger.debug("End of BOUBlrReconJob..");
	}
	
}
