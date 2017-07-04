package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.database.entity.tenant.PortalUserRole;
import com.rssoftware.ou.tenant.dao.PortaUserRoleDao;
import com.rssoftware.ou.tenant.dao.UserRoleDao;
import com.rssoftware.ou.tenant.service.PortalUserRoleService;


@Service
public class PortalUserRoleServiceImpl implements PortalUserRoleService {
	
	@Autowired
	PortaUserRoleDao portalUserRoleDao;
	@Autowired
	UserRoleDao userRoleDao;
		
	@Override
	public List<PortalUserRole> userRoles() {
		List<PortalUserRole> roleList=portalUserRoleDao.getAll();
		return roleList;
	}
	@Override
	public List<PortalUserRole> getPortalUserRole(){
		List<PortalUserRole> userRole=new ArrayList<PortalUserRole>();
		userRole=userRoleDao.getAll();
		List<String> roleName=new ArrayList<String>();
		return userRole;		
	}
	/*@Override
	public void insert(PortalUserDetail portalUserDetail) {
		portalUserDetail.setPassword(encodePassword(portalUserDetail.getPassword()));
		portalUserDetail.setPwdExpire(new Date(System.currentTimeMillis()+(1000*60*60*24*30)));
		userDetailsDao.createOrUpdate(portalUserDetail);		
	}*/

}
