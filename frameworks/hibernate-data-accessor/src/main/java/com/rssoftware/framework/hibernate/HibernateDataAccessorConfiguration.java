package com.rssoftware.framework.hibernate;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TenantPointcutAdvisor;
import com.rssoftware.framework.hibernate.dao.common.TenantTransactionInterceptor;


/**
 * @author rsdpp
 *
 */
@Configuration
public class HibernateDataAccessorConfiguration {

	@Bean
	public BeanLocator beanLocator(){
		return new BeanLocator();
	}

	@Bean
	public TenantTransactionInterceptor tenantTransactionInterceptor(){
		return new TenantTransactionInterceptor();
	}
	
	@Bean
	public TenantPointcutAdvisor tenantPointcutAdvisor(){
		return new TenantPointcutAdvisor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		return defaultAdvisorAutoProxyCreator;
	}
}
