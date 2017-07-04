package com.rssoftware.ou.tenant.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.tenant.dao.PortaUserRoleDao;
import com.rssoftware.ou.tenant.dao.UserRegistrationDao;
import com.rssoftware.ou.tenant.service.PortalUserRoleService;
import com.rssoftware.ou.tenant.service.UserRegistrationService;
import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.database.entity.tenant.UserRegistration;

@Transactional
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
	
	@Autowired
	UserRegistrationDao userRegistrationDao;
	
	
	
	public void userRegistration(UserRegistration userRegistration/*,String currentLoggedInUser*/){
		/*return userRegistrationDao.userRegistration(userRegistration,currentLoggedInUser);*/
		/*userRegistration.setLastchecked_by(currentLoggedInUser);
		userRegistration.setLastmodified_by(currentLoggedInUser);
		userRegistration.setCreated_by(currentLoggedInUser);
		userRegistration.setApproved_by(currentLoggedInUser);*/
	        userRegistrationDao.create(userRegistration);
		
	}

}
