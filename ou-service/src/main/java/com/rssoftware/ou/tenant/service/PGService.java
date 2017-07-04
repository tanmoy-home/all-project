package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.global.TenantDetail;


public interface PGService {
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	/*public List<PGParam> getPGIntegrationPage(String billerId, String refId, String paymentOption,TenantDetail td,String quickPayAmount,BillPaymentRequest billPaymentRequest,String totalAmount,String ccf);*/
	public List<PGParam> getPGIntegrationPage(String billerId, String refId, String paymentOption,TenantDetail td,String quickPayAmount,BillPaymentRequest billPaymentRequest,String totalAmount,String ccf) throws IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public BillPaymentResponse getBillPaymentResponse(String billerId, String refId, String paymentOption, TenantDetail td, String quickPayAmount, BillPaymentRequest billPaymentRequest, String totamount, String ccf, String ipaddress, String selectedPaymentMode) throws ValidationException;
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public void savePGIntegrationField(String fieldName, PGIntegrationFieldType type, String value);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String fetchPGIntegrationField(String fieldName, PGIntegrationFieldType type);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	String getExpression(String name);

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	void saveExpression(String name, String value);

	@TenantTransactional(propagation=Propagation.NOT_SUPPORTED)
	BillPaymentResponse receivePGResponse(List<PGParam> paramList, TenantDetail td,String ipaddress) throws ValidationException, IOException;
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<PGParam> getPGIntegrationFields(String bankId, PGIntegrationFieldType type);
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public BillPaymentResponse processPaymentRequest(BillPaymentRequest billPaymentRequest);
}
