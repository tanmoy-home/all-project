package com.rssoftware.ou.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		UserEntity user = userService.findByUserName(userName);
		logger.info("User : {}", user);
		if (user == null || !user.getIsActive()) {
			logger.info("User not found");
			throw new UsernameNotFoundException("Password Invalid.");
		}
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), true, true, true, true,
				getGrantedAuthorities(user));
	}
	

	private List<GrantedAuthority> getGrantedAuthorities(UserEntity user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (RoleEntity roleEntity : user.getRoles()) {
			logger.info("UserProfile : {}"+ roleEntity);
			authorities.add(new SimpleGrantedAuthority( roleEntity.getRoleName()));
		}
		logger.info("authorities : {}", authorities);
		return authorities;
	}

}
