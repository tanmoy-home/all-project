package com.rssoftware.ou.cbs.service.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.cbs.service.impl.RBLCBSServiceImpl;
import com.rssoftware.ou.schema.cbs.BankCodes;
import com.rssoftware.ou.service.CBSService;

@Component
public class CBSServiceFactory implements ApplicationContextAware {
	
	private static ApplicationContext context;
	private static final Logger logf = LoggerFactory.getLogger(CBSServiceFactory.class);
	
	public static CBSService getService(String bankCode) {
		if(BankCodes.CBS_RBL.equalsIgnoreCase(bankCode)){
			// RBL impl return.
			logf.debug("bankCode:"+bankCode);
			return context.getBean(RBLCBSServiceImpl.class);
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		context = appContext;		
	}
	
}
