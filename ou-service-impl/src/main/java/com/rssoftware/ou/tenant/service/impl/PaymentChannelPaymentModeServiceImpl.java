package com.rssoftware.ou.tenant.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bbps.schema.Biller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.tenant.dao.PaymentChannelTypeDao;
import com.rssoftware.ou.tenant.dao.PaymentModeTypeDao;
import com.rssoftware.ou.tenant.dao.PcPmMappingDao;
import com.rssoftware.ou.tenant.service.PaymentChannelPaymentModeService;

@Service
public class PaymentChannelPaymentModeServiceImpl implements PaymentChannelPaymentModeService {

	@Autowired
	PaymentChannelTypeDao paymentChannelsDao;

	@Autowired
	PaymentModeTypeDao paymentModesDao;

	@Autowired
	PcPmMappingDao pcPmMappingDao;

	public List<Long> fetchPaymentChannelIds(String paymentChannel) {
		return paymentChannelsDao.fetchPaymentChannelIds(paymentChannel);
	}

	public List<Long> fetchPaymentModeIds(List<Long> lstPaymentChannelIds) {
		return pcPmMappingDao.fetchPaymentModeIds(lstPaymentChannelIds);
	}

	public List<String> fetchPaymentModeNames(List<Long> lstPaymentModeIds) {
		return paymentModesDao.fetchPaymentModeNames(lstPaymentModeIds);
	}

	@Override
	public Set<String> fetchPaymentModes(String appPmtChannel, String agentPmtChannel, BillerView billerView) {
		Set<String> applicationPaymentModes = Collections.EMPTY_SET;
		Set<String> agentPaymentModes = Collections.EMPTY_SET;
		Set<String> billerPaymentModes = new HashSet<>();

		// Fetching payment modes for application payment channel
		if (appPmtChannel != null && !appPmtChannel.isEmpty()) {
			applicationPaymentModes = new HashSet(
					fetchPaymentModeNames(fetchPaymentModeIds(fetchPaymentChannelIds(appPmtChannel))));
		}

		// Fetching payment modes for applicatio payment channel
		if (agentPmtChannel != null && !agentPmtChannel.isEmpty()) {
			agentPaymentModes = new HashSet(
					fetchPaymentModeNames(fetchPaymentModeIds(fetchPaymentChannelIds(agentPmtChannel))));
		}

		if (billerView != null) {
			List<PaymentModeLimit> billerPaymentModeLimits = billerView.getBillerPaymentModes();
			if (billerPaymentModeLimits != null && !billerPaymentModeLimits.isEmpty()) {
				for (PaymentModeLimit billerPaymentModeLimit : billerPaymentModeLimits) {
					if (billerPaymentModeLimit != null) {
						billerPaymentModes.add(billerPaymentModeLimit.getPaymentMode().name());
					}
				}
			}
		}

		/**
		 * Intersecting first from application payment modes and agent payment
		 * modes. Then again intersecting resulted (Unique) application payment
		 * modes and biller payment nodes if any.
		 */
		if (!applicationPaymentModes.isEmpty() && !agentPaymentModes.isEmpty()) {
			applicationPaymentModes.retainAll(agentPaymentModes);
		}
		if (!applicationPaymentModes.isEmpty() && !billerPaymentModes.isEmpty()) {
			applicationPaymentModes.retainAll(billerPaymentModes);
		}

		return applicationPaymentModes;
	}
}
