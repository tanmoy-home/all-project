package com.rssoftware.ou.tenant.service;

import java.io.IOException;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.model.tenant.MyBillerView;

public interface CommonBillerService {
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	boolean checkIsOnUs(String billerId) throws IOException;
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	BillFetchResponse fetchBillOnUs(BillFetchRequestExt billFetchRequestExt) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	BillPaymentResponse payBillOnUs(BillPaymentRequestExt billPaymentRequestExt) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	BillPaymentResponse payBill(BillPaymentRequest billPaymentRequest,
			MyBillerView myBiller) throws ValidationException, IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	BillFetchResponse fetchBill(BillFetchRequest billFetchRequest,
			MyBillerView biller) throws ValidationException, IOException;

	
}
