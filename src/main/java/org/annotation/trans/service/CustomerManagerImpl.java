package org.annotation.trans.service;

import org.annotation.trans.dao.CustomerDAO;
import org.annotation.trans.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerManagerImpl implements CustomerManager {

	@Autowired
	private CustomerDAO customerDAO;


	@Override
	@Transactional("transactionManager")
	public void createCustomer(Customer cust) {
		customerDAO.create(cust);
	}

}
