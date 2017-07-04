package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.database.entity.tenant.PortalUserDetail;
import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.domain.UserDetail;
import com.rssoftware.ou.tenant.dao.UserDao;
import com.rssoftware.ou.tenant.dao.UserDetailsDao;
import com.rssoftware.ou.tenant.dao.UserRoleDao;
import com.rssoftware.ou.tenant.service.PortalUserDetailService;

public class PortalUserDetailServiceImpl implements PortalUserDetailService {

	@Autowired
	UserDetailsDao userDetailsDao;
	
	/*@Autowired
	UserRoleDao userRoleDao;*/

	@Autowired
	UserDao userDao;
	@Override
	public UserDetail getPortalUserDetails(String userId) throws DataAccessException {
		return convert(userDetailsDao.get(userId));
	}

	private UserDetail convert(PortalUserDetail user) {
		Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority[] {
				new SimpleGrantedAuthority("ROLE_" + user.getUserRole().getRoleName()) });
		
		return new UserDetail(user.getUserId(), user.getPassword(), authorities, user.getPwdExpire(), user.getName(),
				user.getMobile(), user.getRegId(), user.getStatus(), user.getUserRole().getRoleName(),
				TransactionContext.getTenantId());
	}

	@Override
	public void insert(PortalUserDetail portalUserDetail) {
		portalUserDetail.setPassword(encodePassword(portalUserDetail.getPassword()));
		portalUserDetail.setPwdExpire(new Date(System.currentTimeMillis()+(1000*60*60*24*30)));
		userDetailsDao.createOrUpdate(portalUserDetail);		
	}

	private String encodePassword(String password) {
		return new BCryptPasswordEncoder(11).encode(password);
	}

	/*@Override
	public List<PortalUserRole> getUserRollList(PortalUserRole portalUserDetail) {
		List<PortalUserRole>  allUserRoleList = new ArrayList<PortalUserRole>();
		//PortalUserRole[] userR = userDao.retrieveRoleList();
		userRoleDao.getAll();
		List<PortalUserRole> roles=userRoleDao.getAll();
		System.out.println("userDetailsDao:===>>>>"+userDetailsDao);
		return Collections.unmodifiableList(allUserRoleList);
	}*/
		
}
