package com.rssoftware.ou.batch.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.batch.job.BillFetchJob;
import com.rssoftware.ou.batch.job.BillPaymentJob;
import com.rssoftware.ou.batch.to.BillDump;
import com.rssoftware.ou.common.TypeOfBatch;

import reactor.bus.Event;
import reactor.fn.Consumer;


@Service
public class BatchFileProcessor implements Consumer<Event<BillDump>>{
	private static Log logger = LogFactory.getLog(BatchFileProcessor.class);
	
	@Autowired 
	private BillFetchJob billFetchJob;

	@Autowired 
	private BillPaymentJob billPaymentJob;

	

	@Override
	public void accept(Event<BillDump> event) 
	{
		BillDump billDump = event.getData();
		TypeOfBatch typeOfBatch = billDump.getBatchType();
		//logger.info(billDump);
		
		if(typeOfBatch == TypeOfBatch.BILL_PAYMENT)
			billPaymentJob.billPayment(billDump.getBillerOU(), billDump.getBillerId());
		
		if(typeOfBatch == TypeOfBatch.BILL_FETCH)
			billFetchJob.billFetch(billDump.getBillerOU(), billDump.getBillerId(), billDump.getInputFile());			
		
		if(typeOfBatch == TypeOfBatch.BILL_PAYMENT_REPORT)
			billPaymentJob.reportBillPayment(billDump.getBillerOU(), billDump.getBillerId());
	}
	
}
