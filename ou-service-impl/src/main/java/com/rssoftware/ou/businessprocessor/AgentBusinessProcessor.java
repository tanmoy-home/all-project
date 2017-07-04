package com.rssoftware.ou.businessprocessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillerView;

@Component
public class AgentBusinessProcessor {

	public Map<String, BillerView> filterBillerListOnIncomingPaymentChannelAndMode(List<String> paymentChannels,
			List<String> paymentModes, List<BillerView> billerList, AgentView agent, boolean includeSubBillers) {

		Map<String, BillerView> billerMap = new HashMap<String, BillerView>();

		// Map<String, BillerView> parentFilteredbillerMap = new HashMap<String,
		// BillerView>();

		/**
		 * If no payment modes has been sent, then use agent payment modes to
		 * filter billers
		 */
		List<PaymentModeLimit> agentPaymentModes = agent.getAgentPaymentModes();
		if (CollectionUtils.isEmpty(paymentModes) && CollectionUtils.isNotEmpty(agentPaymentModes)) {
			CommonUtils.getPaymentModesAsStrings(agentPaymentModes, paymentModes);
		}

		/**
		 * If no payment channels has been sent, then use agent payment channels
		 * to filter billers
		 */
		List<PaymentChannelLimit> agentPaymentChannels = agent.getAgentPaymentChannels();
		if (CollectionUtils.isEmpty(paymentChannels) && CollectionUtils.isNotEmpty(agentPaymentChannels)) {
			CommonUtils.getPaymentChannelsAsStrings(agentPaymentChannels, paymentChannels);
		}

		for (BillerView biller : billerList) {

			if (biller.isParentBlr()) {
				billerMap.put(biller.getBlrId(), biller);
			} else if (StringUtils.isBlank(biller.getParentBlrId()) || includeSubBillers) {
				Set<String> billerPaymentChannels = new HashSet<String>();
				Set<String> billerPaymentModes = new HashSet<String>();

				if (biller.getBillerPaymentChannels() != null) {

					CommonUtils.getPaymentChannelsAsStrings(biller.getBillerPaymentChannels(), billerPaymentChannels);
				}

				if (biller.getBillerPaymentModes() != null) {
					CommonUtils.getPaymentModesAsStrings(biller.getBillerPaymentModes(), billerPaymentModes);

					/**
					 * Using an & operator instead of && to force check presence
					 * of both payment channel and payment mode
					 */
					if ((billerPaymentChannels.containsAll(paymentChannels)
							& billerPaymentModes.containsAll(paymentModes))) {
						billerMap.put(biller.getBlrId(), biller);
					}
				}
			}

		}

		return billerMap;

	}

}
