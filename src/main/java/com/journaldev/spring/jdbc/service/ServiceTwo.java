package com.journaldev.spring.jdbc.service;

import org.springframework.transaction.PlatformTransactionManager;

import com.journaldev.spring.jdbc.model.Customer;

public interface ServiceTwo {

	void setTransactionManager(PlatformTransactionManager transactionManager);

	boolean someServiceMethod(Customer cust);

}