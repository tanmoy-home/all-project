package com.rssoftware.ou.tenant.service;

import java.io.IOException;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.StatusField;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.model.tenant.TransactionDataView;

public interface BillerOUtransactionDataService {
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public TransactionDataView getTransactionData(String refId, RequestType requestType) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insert(String refId, BillFetchRequest billFetchRequest);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insert(String refId, BillPaymentRequest billPaymentRequest);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public TransactionDataView update(String refId, BillFetchResponse billFetchResponse) throws ValidationException, IOException;

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public TransactionDataView update(String refId, BillPaymentResponse billPaymentResponse) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	void updateStatus(String refId, StatusField statusField,RequestType requestType, RequestStatus status) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insertReversalRequest(String refId, BillPaymentRequest billPaymentRequest);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public TransactionDataView getByTxnRefId(String txnRefId) throws IOException;

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public TransactionDataView updateReversalresponse(String refId,
			BillPaymentResponse billPaymentResponse) throws IOException;

}
