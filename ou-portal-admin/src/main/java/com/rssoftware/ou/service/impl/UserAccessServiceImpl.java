package com.rssoftware.ou.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.database.entity.tenant.admin.ServiceEntity;
import com.rssoftware.ou.service.ConfigurationRole;
import com.rssoftware.ou.service.UserAccessService;
@Service(value="userAccessService")
public class UserAccessServiceImpl implements UserAccessService {
	@Autowired
	private ConfigurationRole configurationRoleService;
	
	public Map<String, Set<String>> findAccessibleServicesForRoles(){
		
		Iterable<RoleEntity> roles = configurationRoleService.findAllRoles();
		Map<String, Set<String>> resourceMap = new HashMap<String,Set<String>>();
		Set<String> roleNames;
		for (RoleEntity role : roles) {
			Iterable<ServiceEntity> services = role.getServices();
			for (ServiceEntity service : services) {
				if(resourceMap.get(service.getServiceURL())!=null)
					roleNames= resourceMap.get(service.getServiceURL());
				else
					roleNames = new HashSet<String>();
				roleNames.add(role.getRoleName());
				resourceMap.put(service.getServiceURL(), roleNames);
			}
		}
		return resourceMap;
	}
}
