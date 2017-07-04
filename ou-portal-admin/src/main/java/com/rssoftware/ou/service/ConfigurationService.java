package com.rssoftware.ou.service;

import java.util.List;

import com.rssoftware.ou.database.entity.tenant.admin.ServiceEntity;

public interface ConfigurationService {

	void saveService(ServiceEntity serviceEntity);
	
	List<ServiceEntity> findAllServices();
	
	ServiceEntity findById(Long id);
	
	void updateService(ServiceEntity serviceEntity);
	
	void deleteServiceById(Long id);
	
}
