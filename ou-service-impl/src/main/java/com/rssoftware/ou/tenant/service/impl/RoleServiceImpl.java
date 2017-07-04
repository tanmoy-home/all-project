package com.rssoftware.ou.tenant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.tenant.dao.RoleDao;
import com.rssoftware.ou.tenant.service.RoleService;

@Transactional
@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	RoleDao roleDao;	
	
	public List<RoleEntity> getRoles(){		
	    return roleDao.getAll();	
	}
	
	public String[] getAuthenticRoles(){		
		List<RoleEntity> roles = roleDao.getAuthenticRoles();
		int n= roles.size();
		String[] roleList = new String[n];
		for(RoleEntity role: roles){
			roleList[--n]= role.getRoleName();
		}
		
		return roleList;
	}

}
