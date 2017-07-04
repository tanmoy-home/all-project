package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.CustomerRegistration;

public interface CustomerRegistrationDao extends GenericDao<CustomerRegistration, String> {
	public List<CustomerRegistration> fetchAllActiveCustomerList();
}
