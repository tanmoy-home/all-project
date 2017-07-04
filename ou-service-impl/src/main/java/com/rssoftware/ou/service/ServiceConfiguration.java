package com.rssoftware.ou.service;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.BeanLocator.DatabaseType;
import com.rssoftware.ou.dao.DaoConfiguration;

@Configuration
@Import(value = { DaoConfiguration.class, TenantDataSourceManager.class, TenantMailConfigurationManager.class })
@ComponentScan(basePackages = { "com.rssoftware.ou.service.impl", "com.rssoftware.ou.tenant.service.impl",
		"com.rssoftware.ou.consumer", "com.rssoftware.ou.gateway.impl", "com.rssoftware.ou.cbs.service.factory",
		"com.rssoftware.ou.cbs.service.impl", "com.rssoftware.ou.validator", "com.rssoftware.ou.businessprocessor" })
@IntegrationComponentScan({ "com.rssoftware.ou" })
public class ServiceConfiguration {
	@Autowired
	@Qualifier("ds")
	private DataSource dataSource;

	@Bean
	TenantDataSourceManager tenantDataSourceManager() {
		return new TenantDataSourceManager();
	}
	
	@Bean
	TenantMailConfigurationManager tenantMailConfigurationManager() {
		return new TenantMailConfigurationManager();
	}

	@Bean
	BootstrapService bootstrapService() {
		return new BootstrapService();
	}

	@Bean
	TaskScheduler imMemorySpringScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		return scheduler;
	}

	@Bean
	SchedulerFactoryBean sharedSpringScheduler() {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setDataSource(dataSource);

		Properties props = new Properties();
		props.setProperty("org.quartz.scheduler.instanceId", "AUTO");
		props.setProperty("org.quartz.scheduler.instanceName", "BBPSBatchScheduler");
		props.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		DatabaseType databaseType = BeanLocator.getDatabaseType(dataSource);

		if (BeanLocator.DatabaseType.POSTGRES.equals(databaseType)) {
			props.setProperty("org.quartz.jobStore.driverDelegateClass",
					"org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
		} else {
			props.setProperty("org.quartz.jobStore.driverDelegateClass",
					"org.quartz.impl.jdbcjobstore.oracle.OracleDelegate");
		}
		String tablePrefix = "qrtz_";
		props.setProperty("org.quartz.jobStore.tablePrefix", tablePrefix);
		props.setProperty("org.quartz.jobStore.isClustered", "true");
		props.setProperty("org.quartz.jobStore.clusterCheckinInterval", "20000");
		props.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
		props.setProperty("org.quartz.threadPool.threadCount", "5");
		scheduler.setQuartzProperties(props);
		return scheduler;
	}

}
