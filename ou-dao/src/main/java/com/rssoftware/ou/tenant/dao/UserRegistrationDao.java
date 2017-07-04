package com.rssoftware.ou.tenant.dao;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.database.entity.tenant.UserRegistration;

public interface UserRegistrationDao extends GenericDao<UserRegistration, String>{
	
	/*@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public String userRegistration(UserRegistration userRegistration,String currentLoggedInUser);*/

}
