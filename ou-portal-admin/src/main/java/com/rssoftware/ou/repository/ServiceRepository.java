package com.rssoftware.ou.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.rssoftware.ou.database.entity.tenant.admin.ServiceEntity;

public interface ServiceRepository extends CrudRepository<ServiceEntity, Long> {

	List<ServiceEntity> findByServiceName(String serviceName);
	
}
