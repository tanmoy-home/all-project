package com.rssoftware.ou.tenant.service;

import org.bbps.schema.Biller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.model.tenant.BillerView;

import java.util.List;
import java.util.Set;

public interface PaymentChannelPaymentModeService {

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<Long> fetchPaymentChannelIds(String paymentChannel);

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<Long> fetchPaymentModeIds(List<Long> lstPaymentChannelIds);

	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<String> fetchPaymentModeNames(List<Long> lstPaymentModeIds);
	
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public Set<String> fetchPaymentModes(String appPmtChannel, String agentPmtChannel, BillerView billerView);

}
