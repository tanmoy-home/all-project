package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.BillStatus;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.model.tenant.MyBillerView;

public interface MyBillerDetailService {
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void save(MyBillerView billerView);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(String billerId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	void reject(String billerId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	void approve(String billerId);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<MyBillerView> getActiveBillers() throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	MyBillerView getBillerById(String billerId) throws IOException;

	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	BillPaymentResponse sendReversalToBiller(BillPaymentRequest billPaymentRequest,
			MyBillerView biller) throws ValidationException, IOException;*/
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	void updateBillDetails(String billNo, BillStatus status);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	List<String> getAllCurrentBillerIds();

	

	
}
