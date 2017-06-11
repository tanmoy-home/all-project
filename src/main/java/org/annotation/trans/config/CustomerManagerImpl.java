package org.annotation.trans.config;

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
