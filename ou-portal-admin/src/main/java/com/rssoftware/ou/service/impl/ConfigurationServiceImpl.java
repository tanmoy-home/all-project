package com.rssoftware.ou.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.MenuEntity;
import com.rssoftware.ou.database.entity.tenant.admin.ServiceEntity;
import com.rssoftware.ou.repository.ServiceRepository;
import com.rssoftware.ou.service.ConfigurationService;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ServiceRepository serviceRepository;
	
	@Override
	public void saveService(ServiceEntity serviceEntity) {
		serviceRepository.save(serviceEntity);
		
	}

	@Override
	public List<ServiceEntity> findAllServices() {
		Iterable<ServiceEntity> services = serviceRepository.findAll();
		return (List<ServiceEntity>) services;
	}

	@Override
	public ServiceEntity findById(Long id) {
		return serviceRepository.findOne(id);
	}

	@Override
	public void updateService(ServiceEntity serviceEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteServiceById(Long id) {
		serviceRepository.delete(id);
	}

}
