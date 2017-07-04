package com.rssoftware.framework.hibernate.dao.common;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;

public class TenantPointcutAdvisor extends AbstractPointcutAdvisor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8698245890882453248L;

	@Autowired
	TenantTransactionInterceptor tenantTransactionInterceptor;
	
	private final StaticMethodMatcherPointcut pointcut = new
            StaticMethodMatcherPointcut() {
                @Override
                public boolean matches(Method method, Class<?> targetClass) {
                    return method.isAnnotationPresent(TenantTransactional.class);
                }
            };
	
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Advice getAdvice() {
		return this.tenantTransactionInterceptor;
	}

}
