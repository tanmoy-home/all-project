package com.rssoftware.ou.repository;

import org.springframework.data.repository.CrudRepository;

import com.rssoftware.ou.database.entity.tenant.admin.OrganizationEntity;

public interface OrganizationRepository extends CrudRepository<OrganizationEntity, Long>{

}
