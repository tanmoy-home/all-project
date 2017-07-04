package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.Biller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.BillStatus;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillerView;

public interface BillerService {
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public BillerView getBillerById(String billerId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public String[] getBillerCategories(String[] paymentChannels, String[] paymentModes) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public BillerView[] getBillersByCategory(String billerCategory, String[] paymentChannels, String[] paymentModes) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void refreshBillers(List<BillerView> billers) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public BillerView[] getSubBillersByParentBiller(String parentBillerId, String billerCategory,
			String[] paymentChannels, String[] paymentModes) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void save(BillerView billerView);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(String billerId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<String> getBillerCategoriesForAgent(List<String> paymentChannels, List<String> paymentModes,
			AgentView agent) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<BillerView> getBillersByCategoryAndAgent(String billerCategory, List<String> paymentChannels,
			List<String> paymentModes, AgentView agent) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<BillerView> searchParentBiller();

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<String> getAllCurrentBillerIds();

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<BillerView> searchBiller(String loggedInUsersOuId) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<BillerView> fetchFunctionallyActiveList(int pageNo, int pageSize) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<BillerView> fetchFunctionallyActiveListForUser(int pageNo, int pageSize, String loggedInUsersOuId)
			throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillerView fetchWithIdAndStatus(String billerId, String status) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillerView fetchFunctionalActiveParent(String billerId) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillerView saveAsDraft(BillerView blrview) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillerView submit(BillerView blrview, boolean isParent) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public byte[] exportReportToExcel(String loggedInUsersOuId);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<BillerView> getAllBillers() throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<BillerView> getSubBillersByParentBiller(String parentBillerId) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<BillerView> getActiveBillers() throws IOException;
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillFetchResponse fetchBill(BillFetchRequest billFetchRequest, BillerView biller) throws ValidationException, IOException;
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public BillPaymentResponse payBill(BillPaymentRequest billPaymentRequest, BillerView biller) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	void updateBillDetails(String billNo, BillStatus status);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	BillPaymentResponse reversPayment(BillPaymentRequest billPaymentRequest,
			BillerView biller) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	BillPaymentResponse sendReversalToBiller(BillPaymentRequest billPaymentRequest,
			BillerView biller) throws ValidationException, IOException;

	

	

	
	
}
