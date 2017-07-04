package com.rssoftware.ou.consumer;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.domain.Request;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class OURequestProcessor implements Consumer<Event<Request>> {

	private static Log logger = LogFactory.getLog(OURequestProcessor.class);

	@Autowired
	private AsyncProcessor processor;

	@Override
	public void accept(Event<Request> event) {
		Request request = event.getData();
		TransactionContext.putTenantId(request.getTenantId());
		try {
			switch (request.getRequestType()) {
			case FETCH:
				processor.processCUBillFetchRequest(request.getBillFetchRequest());
				break;
			case PAYMENT:
				if(request.getBillPaymentRequest().getTxn().getType().equals(TransactionType.REVERSAL_TYPE_REQUEST.value()))
					processor.processCUReversalPayRequest(request.getBillPaymentRequest());
				else
				    processor.processCUBillPayRequest(request.getBillPaymentRequest());
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
