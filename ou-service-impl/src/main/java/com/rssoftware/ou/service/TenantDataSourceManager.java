package com.rssoftware.ou.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.BeanLocator.DatabaseType;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.dao.common.BBPSDataSource;
import com.rssoftware.ou.database.entity.global.TenantDatasource;

@Configuration
public class TenantDataSourceManager implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {
	
	protected ConfigurableEnvironment environment;
	protected static BeanFactory beanFactory;
	protected static BeanDefinitionRegistry registry;
	
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment)environment;
    }

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		TenantDataSourceManager.beanFactory = beanFactory;
	}
    
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    	TenantDataSourceManager.registry = registry;
    }
    
    public void prepareDatasourceForTenant(String tenantId, TenantDatasource tenantDSConfig){
        Map<String, String> datasourceDetails = new HashMap<>();
        String datasourceMode = environment.getProperty("datasource.mode");
        if (datasourceMode != null && datasourceMode.equals("BOOT")){
            datasourceDetails.put("url",tenantDSConfig.getUrl());
            datasourceDetails.put("username",tenantDSConfig.getUserName());
            datasourceDetails.put("password",tenantDSConfig.getUserPassword());
            datasourceDetails.put("driverClassName",tenantDSConfig.getDriverClassName());
        	datasourceDetails.put("maxActive",tenantDSConfig.getMaxActive().toString());
        	datasourceDetails.put("maxIdle",tenantDSConfig.getMaxIdle().toString());
        	datasourceDetails.put("removeAbandonedTimeout",tenantDSConfig.getRemoveAbandonedTimeout().toString());
        	datasourceDetails.put("testOnBorrow",tenantDSConfig.getTestOnBorrow());	
        	datasourceDetails.put("removeAbandoned",tenantDSConfig.getRemoveAbandoned());
        	datasourceDetails.put("validationQuery",tenantDSConfig.getValidationQuery());	
        }
        else {
        	datasourceDetails.put("jndiName",tenantDSConfig.getJndiName());
        }
		
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        if (datasourceMode != null && datasourceMode.equals("BOOT")){
            beanDefinition.setBeanClass(BBPSDataSource.class);
        }
        else {
        	beanDefinition.setBeanClass(JndiObjectFactoryBean.class);
        }
        beanDefinition.getPropertyValues().addPropertyValues(datasourceDetails);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        registry.registerBeanDefinition(TransactionContext.BEAN_DATA_SOURCE_PREFIX+tenantId, beanDefinition);
        
        GenericBeanDefinition beanDefinition1 = new GenericBeanDefinition();
        beanDefinition1.setBeanClass(DataSourceTransactionManager.class);
        ConstructorArgumentValues c = new ConstructorArgumentValues();
        c.addIndexedArgumentValue(0, beanDefinition);
        beanDefinition1.getConstructorArgumentValues().addArgumentValues(c);
        beanDefinition1.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        registry.registerBeanDefinition(TransactionContext.BEAN_DATA_SOURCE_TRANSACTION_MANAGER_PREFIX+tenantId, beanDefinition1);
        
        beanDefinition1 = new GenericBeanDefinition();
        beanDefinition1.setBeanClass(JdbcTemplate.class);
        c = new ConstructorArgumentValues();
        c.addIndexedArgumentValue(0, beanDefinition);
        beanDefinition1.getConstructorArgumentValues().addArgumentValues(c);
        beanDefinition1.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        registry.registerBeanDefinition(TransactionContext.BEAN_JDBC_TEMPLATE_PREFIX+tenantId, beanDefinition1);

        beanDefinition1 = new GenericBeanDefinition();
        beanDefinition1.setBeanClass(NamedParameterJdbcTemplate.class);
        c = new ConstructorArgumentValues();
        c.addIndexedArgumentValue(0, beanDefinition);
        beanDefinition1.getConstructorArgumentValues().addArgumentValues(c);
        beanDefinition1.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        registry.registerBeanDefinition(TransactionContext.BEAN_NAMED_JDBC_TEMPLATE_PREFIX+tenantId, beanDefinition1);
        
        beanDefinition1 = new GenericBeanDefinition();
        beanDefinition1.setBeanClass(LocalSessionFactoryBean.class);
        beanDefinition1.getPropertyValues().addPropertyValue("dataSource", beanDefinition);
        beanDefinition1.getPropertyValues().addPropertyValue("packagesToScan", new String[]{TransactionContext.BEAN_TENANT_ENTITY_BASE_PACKAGE});
        Properties props = new Properties();
        DatabaseType databaseType = BeanLocator.driverDBMap.get(tenantDSConfig.getDriverClassName());
		if (BeanLocator.DatabaseType.ORACLE.equals(databaseType)) {
			props.put("hibernate.dialect", TransactionContext.BEAN_HIBERNATE_DIALECT_ORACLE);
		} else if (BeanLocator.DatabaseType.POSTGRES.equals(databaseType)) {
			props.put("hibernate.dialect", TransactionContext.BEAN_HIBERNATE_DIALECT_POSTGRES);
		}
        props.put("hibernate.show_sql", TransactionContext.BEAN_HIBERNATE_SHOW_SQL);
        beanDefinition1.getPropertyValues().addPropertyValue("hibernateProperties", props);
        beanDefinition1.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        registry.registerBeanDefinition(TransactionContext.BEAN_HIBERNATE_SESSION_FACTORY_PREFIX+tenantId, beanDefinition1);
        
        GenericBeanDefinition beanDefinition2 = new GenericBeanDefinition();
        beanDefinition2.setBeanClass(HibernateTransactionManager.class);
        c = new ConstructorArgumentValues();
        c.addIndexedArgumentValue(0, beanDefinition1);
        beanDefinition2.getConstructorArgumentValues().addArgumentValues(c);
        beanDefinition2.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
        registry.registerBeanDefinition(TransactionContext.BEAN_HIBERNATE_TRANSACTION_MANAGER_PREFIX+tenantId, beanDefinition2);
	
    }
    
    public void prepareDatasourceForTenant(String tenantId){
    	
    	TenantDatasourceService dsService = BeanLocator.getBean(TenantDatasourceService.class);
    	TenantDatasource tenantDSConfig = dsService.fetchByTenantId(tenantId);
    	if (tenantDSConfig != null){
    		prepareDatasourceForTenant(tenantId, tenantDSConfig);
    	}
    	else {
    		throw new IllegalArgumentException("no entries found for tenant Id:"+tenantId);
    	}
    }

	public void initializeTenantDatasources() {
		for (TenantDatasource tenantDS:BeanLocator.getBean(TenantDatasourceService.class).fetchAll()){
			prepareDatasourceForTenant(tenantDS.getTenantId(), tenantDS);
		}
	}

}
