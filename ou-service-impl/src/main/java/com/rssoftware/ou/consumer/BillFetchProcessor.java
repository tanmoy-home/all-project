package com.rssoftware.ou.consumer;


import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.domain.Response;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class BillFetchProcessor implements Consumer<Event<Response>>{
	private static Log logger = LogFactory.getLog(BillFetchProcessor.class);

	@Autowired
	private AsyncProcessor processor;
	
	@Override
	public void accept(Event<Response> event) {
		Response response = event.getData();
		TransactionContext.putTenantId(response.getTenantId());
		try {
			switch (response.getAction()) {
			case FETCH_RESPONSE:
				processor.processBillerBillFetchResponse(response.getBillFetchResponse());
				break;
			case PAYMENT_RESPONSE:
				processor.processBillerBillPayResponse(response.getBillPaymentResponse());
				break;
			case COMPLAINT_RESPONSE:
				// processor.processBillFetchResponse(request.getBillFetchResponse());
				break;
			default:
				break;
			}
		} catch (ValidationException ve) {
			logger.error(ve.getMessage());
		}
		catch (IOException ve) {
			logger.error( ve.getMessage(), ve);
			logger.info("In Excp : " + ve.getMessage());
		}
	}	
	
}
