package com.rssoftware.ou.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.tenant.service.CertificateService;

public class BootstrapService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TenantDataSourceManager tenantDataSourceManager;
	@Autowired
	private TenantDatasourceService tenantDatasourceService;
	
	@Autowired
	private TenantDetailService tenantDetailService;
	
	@Autowired
	private CertificateService certificateService; 
	
	@Autowired
	private ApplicationConfigService aplicationConfigService;
	
	@Autowired
	private TenantMailConfigurationManager tenantMailConfigurationManager;
	
	@PostConstruct 
	public void initialize(){
		tenantDataSourceManager.initializeTenantDatasources();
		tenantMailConfigurationManager.initializeMailConfiguration();
		//TenantRoutingDataSource tenantRoutingDataSource = BeanLocator.getBean("tenantRoutingDataSource", TenantRoutingDataSource.class);
		//Map<Object, Object> targetDataSources = new HashMap<>();
		for (TenantDetail td:tenantDetailService.fetchAll()){
			TransactionContext.putTenantId(td.getTenantId());
			try {
				certificateService.refresh();
				aplicationConfigService.refresh();
			} catch (Exception e) {
				logger.error("Could not initialize certificate for tenant:"+td.getTenantId(), e);
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
			//DataSource dataSource = BeanLocator.getBean(TransactionContext.BEAN_DATA_SOURCE_PREFIX+td.getTenantId(), DataSource.class);
			//targetDataSources.put(TransactionContext.BEAN_DATA_SOURCE_PREFIX + td.getTenantId(), dataSource);
		}
		//tenantRoutingDataSource.setTargetDataSources(targetDataSources);
		//logger.debug("List underlying datasources of TenantRoutingDataSource "+targetDataSources.size());
	}
}
