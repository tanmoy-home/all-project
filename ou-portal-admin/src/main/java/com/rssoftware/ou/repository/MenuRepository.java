package com.rssoftware.ou.repository;

import org.springframework.data.repository.CrudRepository;

import com.rssoftware.ou.database.entity.tenant.admin.MenuEntity;

public interface MenuRepository extends CrudRepository<MenuEntity, Long>{

	
}
