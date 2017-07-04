package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.ReconSummaryView;

public interface ReconSummaryService {

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<ReconSummaryView> getReconList(String startDate, String endDate) throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void insert(ReconSummaryView reconSummaryView) throws IOException;

}