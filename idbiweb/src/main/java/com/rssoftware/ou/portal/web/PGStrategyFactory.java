package com.rssoftware.ou.portal.web;

import org.springframework.stereotype.Component;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;
import com.rssoftware.ou.portal.web.service.impl.PayByIDBINetBanking;
import com.rssoftware.ou.portal.web.service.impl.PayByIDBIPaymentGateway;

@Component
public class PGStrategyFactory {

	private PGStrategyFactory() {
	}

	public static PaymentGatewayStrategy getPGStrategy(String paymentMode) throws Exception {

		PaymentGatewayStrategy strategy = null;
		if (PaymentMode.Internet_Banking.equals(PaymentMode.getFromExpandedForm(paymentMode))) {

			strategy = (PaymentGatewayStrategy) BeanLocator.getBean(PayByIDBINetBanking.class);

		} else if (PaymentMode.Credit_Card.equals(PaymentMode.getFromExpandedForm(paymentMode))
				|| PaymentMode.Debit_Card.equals(PaymentMode.getFromExpandedForm(paymentMode))
				|| PaymentMode.Prepaid_Card.equals(PaymentMode.getFromExpandedForm(paymentMode))) {

			strategy = (PaymentGatewayStrategy) BeanLocator.getBean(PayByIDBIPaymentGateway.class);
		}
		return strategy;
	}

}
