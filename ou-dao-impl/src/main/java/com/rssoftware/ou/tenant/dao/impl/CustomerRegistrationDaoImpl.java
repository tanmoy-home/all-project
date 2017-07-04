package com.rssoftware.ou.tenant.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.CustomerRegistration;
import com.rssoftware.ou.tenant.dao.CustomerRegistrationDao;

@Repository("customerRegistrationDao")
public class CustomerRegistrationDaoImpl extends GenericDynamicDaoImpl<CustomerRegistration, String> implements CustomerRegistrationDao{

	private static final String FETCH_ALL_ACTIVE_CUSTOMER_LIST = "select a from CustomerRegistration a";//match with table name
	
	/*public CustomerRegistrationDaoImpl(Class<CustomerRegistration> type) {
		super(type);
	}*/
	public CustomerRegistrationDaoImpl() {
		super(CustomerRegistration.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerRegistration> fetchAllActiveCustomerList() {
		List<CustomerRegistration> tempCustomers = 
				(List<CustomerRegistration>) getSessionFactory().getCurrentSession().
				createQuery(FETCH_ALL_ACTIVE_CUSTOMER_LIST).
				list();
		
		return getEffectiveCustomerList(tempCustomers);
	}
	
	private List<CustomerRegistration> getEffectiveCustomerList(List<CustomerRegistration> tempCustomers) {
		List<CustomerRegistration> customers = new ArrayList<>();
		for(CustomerRegistration c : tempCustomers) {
			if(CommonUtils.isEffective(c.getEffectiveFrom(), c.getEffectiveTo())) {
				customers.add(c);
			}
		}
		return customers;
	}

}


