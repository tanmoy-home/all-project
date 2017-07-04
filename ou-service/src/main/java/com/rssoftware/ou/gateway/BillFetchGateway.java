package com.rssoftware.ou.gateway;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.web.multipart.MultipartFile;

import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.domain.BillFetchRequestExt;

@MessagingGateway(name = "billFetchGateway")
public interface BillFetchGateway {

	@Gateway(requestChannel = "ouBillFetchRequestChannel1")
	public BillFetchResponse processOUBillFetchRequest1(BillFetchRequestExt billFetchRequestExt) throws ValidationException;

	@Gateway(requestChannel = "ouBillFetchRequestChannel")
	public BillFetchResponse processOUBillFetchRequest(BillFetchRequest billFetchRequest) throws ValidationException;
	
	@Gateway(requestChannel = "ouBillFetchResponseChannel")
	public void processOUBillFetchResponse(BillFetchResponse billFetchResponse) throws ValidationException;

	@Gateway(requestChannel = "ouBatchBillFetchRequestChannel")
	public void processOUBatchBillFetchRequest(MultipartFile billFetchRequestFile);
	
	@Gateway(requestChannel = "ouBatchBillFetchResponseChannel")
	public void processOUBatchBillFetchResponse(MultipartFile billFetchRequestFile);
}