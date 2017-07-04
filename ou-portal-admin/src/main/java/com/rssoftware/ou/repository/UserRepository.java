package com.rssoftware.ou.repository;

import org.springframework.data.repository.CrudRepository;

import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;


public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	public UserEntity findByUsername(String username);
	
	public Iterable<UserEntity> findByUpdatedBy(String username);

}
