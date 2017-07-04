package com.rssoftware.ou.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.rssoftware.framework.hibernate.HibernateDataAccessorConfiguration;
import com.rssoftware.ou.dao.common.DataSourceLoader;
import com.rssoftware.ou.dao.impl.GlobalParamDaoImpl;
import com.rssoftware.ou.dao.impl.GlobalTemplateDaoImpl;
import com.rssoftware.ou.dao.impl.TenantDatasourceDaoImpl;
import com.rssoftware.ou.dao.impl.TenantDetailDaoImpl;


/**
 * @author rsdpp
 *
 */
@Configuration
@Import(value={DataSourceLoader.class, HibernateDataAccessorConfiguration.class})
@ImportResource("classpath:platform-hibernate-cfg.xml")
@ComponentScan(basePackages={"com.rssoftware.ou.tenant.dao.impl"})
public class DaoConfiguration {
	@Autowired
	private SessionFactory sessionFactory; 
	
	@Bean
	public GlobalParamDao globalParamDao(){
		return new GlobalParamDaoImpl(sessionFactory);
	}
	
	@Bean
	public TenantDatasourceDao tenantDatasourceDao(){
		return new TenantDatasourceDaoImpl(sessionFactory);
	}
	
	@Bean
	public TenantDetailDao tenantDetailDao(){
		return new TenantDetailDaoImpl(sessionFactory);
	}

	@Bean
	public GlobalTemplateDao globalTemplateDao(){
		return new GlobalTemplateDaoImpl(sessionFactory);
	}
}
