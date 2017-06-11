package com.journaldev.spring.jdbc.service;

import com.journaldev.spring.jdbc.model.Customer;

public interface Sevice {

	Object someServiceMethod(Customer cust);

	Object someServiceMethodWithoutResult(Customer cust);

}