package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.model.tenant.TransactionDataView;

import in.co.rssoftware.bbps.schema.TxnSearchResponse;

public interface TransactionDataService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public TransactionDataView getTransactionData(String refId, RequestType requestType) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insert(String refId, BillFetchRequest billFetchRequest) throws IOException;

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insert(String refId, BillPaymentRequest billPaymentRequest) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insertQuickPay(String refId, BillPaymentRequest billPaymentRequest) throws IOException;

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public TransactionDataView update(String refId, BillFetchResponse billFetchResponse) throws ValidationException, IOException;

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public TransactionDataView update(String refId, BillPaymentResponse billPaymentResponse) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void updateStatus(String refId, RequestType requestType, RequestStatus status) throws IOException;
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void updateReconStatus(String refId, RequestType requestType,
			TransactionDataView.ReconStatus status, String description, BigDecimal reconCycleNo) throws IOException;
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public TransactionDataView getPaymentTransactionDataByTranRefId(String tranRefId);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insert(BillFetchRequestExt billFetchRequestExt) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	void update(String refId, BillPaymentRequest billPaymentRequest) throws ValidationException, IOException;

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void insert(BillPaymentRequestExt billPaymentRequest) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<TransactionDataView> getFilteredBillers(String mobile);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<TransactionDataView> fetchAllTxnByMobile(String mobile);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<TransactionDataView> getFilteredTransactionView(String strId);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public Long txnCountByType(String txnType, String agentId);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String totalTxnAmount(String txnType, String agentId, String startDate, String endDate);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public TxnSearchResponse getTxnJaxb(List<TransactionDataView> txnViews);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<TransactionDataView> getFilteredTransactionByDate(String agentId, String strStartDate, String strEndDate);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public TransactionDataView getFilteredTransactionByID(String txnId);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<TransactionDataView> getFilteredTransactionByDateRangeAndMobile(String mobile, String strStartDate,
			String strEndDate);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public	void insert(BillPaymentRequestExt billPaymentRequest, Boolean isOnus);
			
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public void insert(BillFetchRequestExt billFetchRequestExt, Boolean isOnus);

}