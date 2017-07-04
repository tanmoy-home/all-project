package com.rssoftware.ou.tenant.service;

import java.io.IOException;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.ReconDetailsView;
import com.rssoftware.ou.model.tenant.ReconSummaryView;
import com.rssoftware.ou.model.tenant.ReconView;

public interface ReconDetailsService {

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public ReconView getReconDetailsList(ReconSummaryView reconSummaryView);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public ReconView getReconDetailsList(ReconSummaryView reconSummaryView,
			String id, int billerOrAgent);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void insert(ReconDetailsView reconDetailsView) throws IOException;
}