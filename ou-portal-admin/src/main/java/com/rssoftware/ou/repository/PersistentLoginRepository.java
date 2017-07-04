package com.rssoftware.ou.repository;

import org.springframework.data.repository.CrudRepository;

import com.rssoftware.ou.database.entity.tenant.admin.PersistentLoginEntity;

public interface PersistentLoginRepository extends CrudRepository<PersistentLoginEntity, Long> {

	PersistentLoginEntity findByUserid(String userid);

	
	
}
