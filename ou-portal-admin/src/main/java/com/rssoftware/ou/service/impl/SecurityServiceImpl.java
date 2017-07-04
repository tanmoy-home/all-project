package com.rssoftware.ou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.database.entity.tenant.admin.MenuEntity;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.database.entity.tenant.admin.ServiceEntity;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.repository.UserRepository;
import com.rssoftware.ou.service.SecurityService;

/**
 * @author MalobikaM
 *
 */
@Component(value = "securityService")
public class SecurityServiceImpl implements SecurityService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean userHasAccess(final Authentication auth, String objectName) {
		Object principal = auth.getPrincipal();
		String userName = null;
		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		UserEntity userEntity = userRepository.findByUsername(userName);

		if (userEntity != null) {
			for (RoleEntity role : userEntity.getRoles()) {
				if (role != null) {
					for (MenuEntity menu : role.getMenus()) {
						if (objectName.equals(menu.getMenuName()))
							return true;
					}
				}
			}
		}
		return false;
	}

}
