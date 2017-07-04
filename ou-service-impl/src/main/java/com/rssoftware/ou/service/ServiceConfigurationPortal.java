package com.rssoftware.ou.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;

import com.rssoftware.ou.dao.DaoConfiguration;
import com.rssoftware.ou.service.impl.TenantDatasourceServiceImpl;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.ContactDetailsService;
import com.rssoftware.ou.tenant.service.CreateRoleService;
import com.rssoftware.ou.tenant.service.PortalUserDetailService;
import com.rssoftware.ou.tenant.service.PortalUserRoleService;
import com.rssoftware.ou.tenant.service.UserRegistrationService;
import com.rssoftware.ou.tenant.service.UserService;
import com.rssoftware.ou.tenant.service.impl.AgentServiceImpl;
import com.rssoftware.ou.tenant.service.impl.ContactDetailsServiceImpl;
import com.rssoftware.ou.tenant.service.impl.CreateRoleServiceImpl;
import com.rssoftware.ou.tenant.service.impl.PortalUserDetailServiceImpl;
import com.rssoftware.ou.tenant.service.impl.PortalUserRoleServiceImpl;
import com.rssoftware.ou.tenant.service.impl.UserRegistrationServiceImpl;
import com.rssoftware.ou.tenant.service.impl.UserServiceImpl;

@Configuration
@Import(value={DaoConfiguration.class,TenantDataSourceManager.class})
//@ComponentScan(basePackages={"com.rssoftware.ou.service.impl"})
@IntegrationComponentScan({ "com.rssoftware.ou" })
public class ServiceConfigurationPortal {

	@Autowired
	private TenantDataSourceManager tenantDataSourceManager;
	
	@Autowired
	private TenantDatasourceService tenantDatasourceService;
	
	@PostConstruct
	void init(){
		//tenantDataSourceManager.initializeTenantDatasources();
		
		/*for (TenantDatasource tenantDS:tenantDatasourceService.fetchAll()){
			tenantDataSourceManager.prepareDatasourceForTenant(tenantDS.getTenantId(), tenantDS);
		}*/
		
		tenantDataSourceManager.prepareDatasourceForTenant("OU04", tenantDatasourceService.fetchByTenantId("OU04"));
	}
	
	@Bean
	TenantDataSourceManager tenantDataSourceManager(){
		return new TenantDataSourceManager();
	}

	@Bean
	TenantDatasourceService tenantDatasourceService(){
		return new TenantDatasourceServiceImpl();
	}
	
	
	
	@Bean
	UserRegistrationService userRegistrationService(){
		return new UserRegistrationServiceImpl();
	}
	
/*	@Bean
	UserRegistrationDao userRegistrationDao(){
		return new UserRegistrationDaoImpl();
	}*/
	
	@Bean
	PortalUserDetailService portalUserDetailService(){
		return new PortalUserDetailServiceImpl();
	}
	
	@Bean
	PortalUserRoleService portalUserRoleService(){
		return new PortalUserRoleServiceImpl();
	}
	
/*	@Bean
	PortaUserRoleDao portalUserRoleDao(){
		return new PortalUserRoleDaoImpl();
	}
*/	
	@Bean
	CreateRoleService createRoleService(){
		return new CreateRoleServiceImpl();
	}
	
	/*@Bean
	CreateRoleDao createRoleDao(){
		return new CreateRoleDaoImpl();
	}*/
	
	
	@Bean
	UserService userService(){
		return new UserServiceImpl();
	}
	
	
	@Bean
	AgentService agentService(){
		return new AgentServiceImpl();
	}
	
	@Bean
	ContactDetailsService contactDetailsService(){
		return new ContactDetailsServiceImpl();
	}
		
	
}
