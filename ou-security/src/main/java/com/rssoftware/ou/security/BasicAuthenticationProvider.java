package com.rssoftware.ou.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.ErrorCode;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.domain.CustomUserDetails;
import com.rssoftware.ou.tenant.service.ErrorCodesService;
import com.rssoftware.ou.tenant.service.RoleService;
import com.rssoftware.ou.tenant.service.UserService;

@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService service;

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private ErrorCodesService errorCodesService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
		String uri = null;
		if (attribs instanceof ServletRequestAttributes) {
			HttpServletRequest request = (HttpServletRequest) ((ServletRequestAttributes) attribs).getRequest();
			uri = request.getRequestURI();
		}
		String userName = (String) token.getPrincipal();
		String password = (String) authentication.getCredentials();
		String userRole = null;

		if (uri != null) {
			final Matcher matcher = Pattern.compile("urn:tenantId:").matcher(uri);
			if (matcher.find()) {
				String tenantId = uri.substring(matcher.end()).trim();
				if (tenantId != null && tenantId.contains(CommonConstants.STRING_SLASH)) {
					String[] tokens = tenantId.split(CommonConstants.STRING_SLASH);
					tenantId = tokens[0];
				}
				TransactionContext.putTenantId(tenantId);
			}

			UserEntity userView = service.getUsrDtlByUserName(userName);
			if (userView == null || !userView.getUsername().equalsIgnoreCase(userName)) {
				throw new BadCredentialsException(errorCodesService.getErrorMessage(ErrorCode.USER_NOT_FOUND).getErrorDtl());
			}

			if (!password.equals(userView.getPassword())) {
				throw new BadCredentialsException(errorCodesService.getErrorMessage(ErrorCode.WRONG_PASSWORD).getErrorDtl());
			}

			List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
			for (RoleEntity uRole : userView.getRoles()) {
				if (Arrays.asList(roleService.getAuthenticRoles()).contains(uRole.getRoleName())) {
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_AccessApi");
					grantedAuthorities.add(authority);
				}
			}

			CustomUserDetails user = new CustomUserDetails(userView.getUsername(), userView.getPassword(),
					userView.getUserRefId(), grantedAuthorities);

			Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

			UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
			auth = new UsernamePasswordAuthenticationToken(user, userView.getPassword(), authorities);
			return auth;
		}
		return token;

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
