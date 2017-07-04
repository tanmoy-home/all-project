package com.rssoftware.ou.tenant.service.impl;

import org.bbps.schema.Ack;
import org.bbps.schema.TxnStatusComplainRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.tenant.service.CUService;
import com.rssoftware.ou.tenant.service.ParamService;

@Service
public class CUServiceImpl implements CUService {

	@Autowired
	private ParamService paramService;

	private static OURestTemplate restTemplate = OURestTemplate.createInstance();

	private String cuDomain;

	@Override
	public Ack postBillFetchRequest(BillFetchRequestExt billFetchRequest) {
		final String cuBillFetchRequestUrl = getCUDomain() + CommonConstants.BILL_FETCH_REQUEST_URl
				+ billFetchRequest.getBillFetchRequest().getHead().getRefId();

		return restTemplate.postForObject(cuBillFetchRequestUrl, billFetchRequest.getBillFetchRequest(), Ack.class);

	}

	@Override
	public Ack postBillPayRequest(BillPaymentRequestExt billPaymentRequest) {
		final String cuBillPaymentRequestUrl = getCUDomain() + CommonConstants.BILL_PAYMENT_REQUEST_URl
				+ billPaymentRequest.getBillPaymentRequest().getHead().getRefId();

		return restTemplate.postForObject(cuBillPaymentRequestUrl, billPaymentRequest.getBillPaymentRequest(),
				Ack.class);
	}

	@Override
	public Ack postComplaintRequest(TxnStatusComplainRequest complaintRequest) {
		String cuComplaintrequestUrl = getCUDomain() + CommonConstants.COMPLAINT_REQ_URL;
		return restTemplate.postForObject(cuComplaintrequestUrl, complaintRequest, Ack.class);
	}	
	
	private String getCUDomain(){
		cuDomain=null;
		if (cuDomain == null) {
			cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		}
		
		return cuDomain;
	}
	
}
