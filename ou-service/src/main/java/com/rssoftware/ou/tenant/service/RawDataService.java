package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.model.tenant.Differences;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.TransactionDataView;

public interface RawDataService {

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public RawDataView getRawData(String refId, RequestType requestType) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<Object> getData() throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void updateReconStatus(String refId, RequestType requestType,
			RawDataView.ReconStatus status, String description) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public Differences compare(RawDataView r, TransactionDataView t) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void insert(RawDataView rawDataView) throws IOException;

}