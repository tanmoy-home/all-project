package com.rssoftware.ou.batch;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;

public class ProxyTransactionManager implements PlatformTransactionManager{

	String tenantId = TransactionContext.getTenantId();
	String qualifier = TransactionContext.BEAN_HIBERNATE_TRANSACTION_MANAGER_PREFIX+tenantId;
	PlatformTransactionManager txManager = BeanLocator.getBean(qualifier, PlatformTransactionManager.class);
	
	@Override
	public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
		return txManager.getTransaction(definition);
	}

	@Override
	public void commit(TransactionStatus status) throws TransactionException {
		txManager.commit(status);
	}

	@Override
	public void rollback(TransactionStatus status) throws TransactionException {
		txManager.rollback(status);		
	}

}
