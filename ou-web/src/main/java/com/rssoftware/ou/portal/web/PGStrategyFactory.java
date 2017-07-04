package com.rssoftware.ou.portal.web;

import org.springframework.stereotype.Component;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.portal.web.service.PaymentGatewayStrategy;
import com.rssoftware.ou.portal.web.service.impl.PayByIDBI;

@Component
public class PGStrategyFactory {
	
	private PGStrategyFactory(){}
	
	public static PaymentGatewayStrategy getPGStrategy(String tenantId) {
		
		/*if("OU06".equals(tenantId)) {
			return BeanLocator.getBean(PayByPNB.class);
		} else*/ if("OU05".equals(tenantId)) {
			return (PaymentGatewayStrategy) BeanLocator.getBean(PayByIDBI.class);
		}
		return null;
	}

}
