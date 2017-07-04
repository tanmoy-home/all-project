package com.rssoftware.framework.hibernate.dao.common;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;

public class TenantTransactionInterceptor extends TransactionInterceptor{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2302679337504041914L;

	private final ConcurrentHashMap<Object, PlatformTransactionManager> transactionManagerCache =
			new ConcurrentHashMap<Object, PlatformTransactionManager>();

	public TenantTransactionInterceptor() {
		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource(new TenantTransactionAnnotationParser());
		setTransactionAttributeSource(atas);
	}
	
	@Override
	protected PlatformTransactionManager determineTransactionManager(
			TransactionAttribute txAttr) {
		// Do not attempt to lookup tx manager if no tx attributes are set
		if (txAttr == null) {
			return getTransactionManager();
		}
		
		String tenantId = TransactionContext.getTenantId();
		
		if (!StringUtils.hasText(tenantId)) {
			throw new IllegalArgumentException("no tenantId in context");
		}
		
		return determineQualifiedTransactionManager(TransactionContext.BEAN_HIBERNATE_TRANSACTION_MANAGER_PREFIX+tenantId);
	}
	
	private PlatformTransactionManager determineQualifiedTransactionManager(String qualifier) {
		PlatformTransactionManager txManager = this.transactionManagerCache.get(qualifier);
		if (txManager == null) {
			txManager = BeanLocator.getBean(qualifier, PlatformTransactionManager.class);
			
			if (txManager == null){
				throw new IllegalArgumentException("no mapped transaction manager in context");
			}
			this.transactionManagerCache.putIfAbsent(qualifier, txManager);
		}
		return txManager;
	}
}
