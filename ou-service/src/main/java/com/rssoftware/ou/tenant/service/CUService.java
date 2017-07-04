package com.rssoftware.ou.tenant.service;

import org.bbps.schema.Ack;
import org.bbps.schema.TxnStatusComplainRequest;

import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;

public interface CUService {

	public Ack postBillFetchRequest(BillFetchRequestExt billFetchrequest);

	public Ack postBillPayRequest(BillPaymentRequestExt billPaymentRequest);

	public Ack postComplaintRequest(TxnStatusComplainRequest complaintRequest);
	

}
