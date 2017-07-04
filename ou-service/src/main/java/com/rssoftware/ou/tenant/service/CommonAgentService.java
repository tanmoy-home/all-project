package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.model.cbs.TxnType;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.TransactionDataView;

public interface CommonAgentService {

	public List<String> getBillerCategories(final List<String> paymentChannels, final List<String> paymentModes)
			throws ValidationException, IOException;

	public List<BillerView> getBillersbyCategory(String billerCategory, List<String> paymentChannels,
			List<String> paymentModes) throws ValidationException, IOException;

	public List<BillerView> getSubBillers(String parentBillerId, List<String> paymentChannels,
			List<String> paymentModes) throws ValidationException, IOException;

	public List<ParamConfig> getCustomerParamsbyBillerId(String billerId) throws IOException;

	public BillPaymentResponse payBill(BillPaymentRequestExt billPaymentReq) throws ValidationException, IOException;

	public BillFetchResponse fetchBill(BillFetchRequestExt billFetchRequestExt) throws ValidationException, IOException;

	public List<TransactionDataView> getTransactions(String agentId, String customerId, String startDate,
			String endDate);

	public BigDecimal checkCommisionForAgent(String agentId, String startDate, String endDate);

	public double checkCurrentBalance(String agentId) throws ValidationException;

	public BillPaymentResponse updateAgentBalance(List<PGParam> inputParams, BillPaymentRequest billPaymentRequest) throws ValidationException;
}