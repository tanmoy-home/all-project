package com.rssoftware.ou.gateway;

import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.web.multipart.MultipartFile;

import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.domain.BillPaymentRequestExt;

@MessagingGateway(name = "billPaymentGateway")
public interface BillPaymentGateway {

	@Gateway(requestChannel = "ouBillPaymentRequestChannel")
	public BillPaymentResponse processOUBillPaymentRequest(BillPaymentRequest billPaymentRequest) throws ValidationException;
	
	
	@Gateway(requestChannel = "ouBillPaymentQuickPayRequestChannel")
	public BillPaymentResponse processOUBillQuickPayPaymentRequest(BillPaymentRequest billPaymentRequest) throws ValidationException;

	@Gateway(requestChannel = "ouBillPaymentResponseChannel")
	public void processOUBillPaymentResponse(BillPaymentResponse billPaymentResponse) throws ValidationException;
	
	@Gateway(requestChannel = "ouBatchBillPaymentRequestChannel")
	public void processOUBatchBillPaymentRequest(MultipartFile billFetchRequestFile);

	@Gateway(requestChannel = "ouBatchBillPaymentResponseChannel")
	public void processOUBatchBillPaymentResponse(MultipartFile billFetchRequestFile);

	@Gateway(requestChannel = "ouBillPaymentRequestChannel")
	public BillPaymentResponse processOUBillPaymentRequestExt(BillPaymentRequestExt billPaymentRequestExt);
}