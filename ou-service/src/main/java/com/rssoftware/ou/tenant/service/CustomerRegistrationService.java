package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.CustomerRegistrationView;


public interface CustomerRegistrationService {
	
	 @TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
     public void save(CustomerRegistrationView customerRegistrationView);

     @TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
     public void delete(CustomerRegistrationView customerRegistrationView);

     @TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
     public List<CustomerRegistrationView> fetchAll();

     @TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
     public List<CustomerRegistrationView> fetchAllActiveList();

     @TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
     public CustomerRegistrationView fetchByCustomerId(String custId);


}
