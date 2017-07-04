package com.rssoftware.ou.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.controller.AdminController;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.database.entity.tenant.admin.ServiceEntity;
import com.rssoftware.ou.service.ConfigurationRole;
import com.rssoftware.ou.service.UserAccessService;

@Component("customFilterSecurityMetadata")
public class CustomFilterSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomFilterSecurityMetadataSource.class);
	private static Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();;
	AntPathMatcher urlMatcher = new AntPathMatcher();
	
	@Value("${ou.tenantId}")
	private String tenantId;
	
	@Autowired
	@Qualifier("userAccessService")
	private UserAccessService userAccessService;

	private static boolean isMapSet = false;

	public CustomFilterSecurityMetadataSource() {
		// this.configurationRoleService= configurationRoleService;
		// loadResourceDefine();
		TransactionContext.putTenantId(tenantId);
	}

	private void loadResourceDefine() {
		TransactionContext.putTenantId(tenantId);
		Map<String, Set<String>> roleServiceMapping = userAccessService
				.findAccessibleServicesForRoles();
		resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
		Collection<ConfigAttribute> configAttributes;
		for (Map.Entry<String, Set<String>> serviceRoles : roleServiceMapping
				.entrySet()) {
			configAttributes = new ArrayList<ConfigAttribute>();
			for (String roleName : serviceRoles.getValue()) {
				ConfigAttribute configAttr = new SecurityConfig(roleName);
				configAttributes.add(configAttr);
			}
			resourceMap.put(serviceRoles.getKey(), configAttributes);
		}
		LOGGER.info("resourceMap" + resourceMap);
		isMapSet = true;
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		//if(!isMapSet)
			loadResourceDefine();
		FilterInvocation fi = (FilterInvocation) object;
		String fullRequestUrl = fi.getFullRequestUrl();
		String requestUrl = fi.getRequestUrl();
		String httpMethod = fi.getRequest().getMethod();
		String contextPath = fi.getRequest().getContextPath();
		LOGGER.info("Full request URL: " + fullRequestUrl);
		LOGGER.info("Request URL: " + requestUrl);
		Collection<ConfigAttribute>  configAttributes = new ArrayList<ConfigAttribute>();
		// Lookup your database (or other source) using this information and
		// populate the
		// list of attributes
		if (!requestUrl.equals("/login") && !requestUrl.contains("/static/")
				&& !requestUrl.equals("/home") && !requestUrl.contains("/users/")) {
			LOGGER.info("resourceMap get: " + resourceMap.get(requestUrl));
			
			Set <String> urlSet= resourceMap.keySet();
			for(String resURL: urlSet){
			 if (urlMatcher.matchStart(requestUrl, resURL)) {
				 return resourceMap.get(resURL);
			 }
			}
			/*ConfigAttribute configAttr = new SecurityConfig("ROLE_ANONYMOUS");
			configAttributes.add(configAttr);
		    return configAttributes;*/
		}
		return null;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}