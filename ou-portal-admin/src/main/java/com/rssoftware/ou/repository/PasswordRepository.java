package com.rssoftware.ou.repository;

import org.springframework.data.repository.CrudRepository;

import com.rssoftware.ou.database.entity.tenant.admin.ChangePasswordEntity;

import antlr.collections.List;

public interface PasswordRepository extends CrudRepository<ChangePasswordEntity, Long> {
	
	public Iterable<ChangePasswordEntity> findFirstByUserNameOrderByResetOnDesc(String username);
}
