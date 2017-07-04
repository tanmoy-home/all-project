package com.rssoftware.ou.portal.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import com.rssoftware.ou.common.TenantRoutingDataSource;

@Configuration
@AutoConfigureAfter({HibernateJpaAutoConfiguration.class})
public class TenantRoutingDataSourceConfiguration implements BeanFactoryAware {
	private static final String BEAN_PLATFORM_DATA_SOURCE_PREFIX = "ds";
	private static BeanFactory beanFactory;
	
	@Autowired
	@Qualifier("ds")
	private DataSource dataSource;
	
	@PostConstruct
	public void postProcessBeanFactory() throws BeansException {
		BeanDefinitionRegistry factory = (BeanDefinitionRegistry) beanFactory;
		Map<Object, Object> hashMap = new HashMap<>();		
		
		/*for(TenantDetail tenantDetail : tenantDetailService.fetchAll()) {			
			hashMap.put(TransactionContext.BEAN_DATA_SOURCE_PREFIX+tenantDetail.getTenantId(), 
					beanFactory.getBean(TransactionContext.BEAN_DATA_SOURCE_PREFIX+tenantDetail.getTenantId()));
		}*/
		
		hashMap.put(BEAN_PLATFORM_DATA_SOURCE_PREFIX, dataSource);
	
		BeanDefinitionBuilder tenantRoutingDatasourceDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(TenantRoutingDataSource.class);
		tenantRoutingDatasourceDefinitionBuilder.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
		tenantRoutingDatasourceDefinitionBuilder.addPropertyValue("targetDataSources",hashMap); 
		factory.registerBeanDefinition("tenantRoutingDataSource", tenantRoutingDatasourceDefinitionBuilder.getBeanDefinition());
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		TenantRoutingDataSourceConfiguration.beanFactory = beanFactory;	
	}	
}
