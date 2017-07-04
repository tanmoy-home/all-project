package com.rssoftware.ou.service;

import com.rssoftware.ou.database.entity.tenant.admin.OrganizationEntity;

public interface ConfigurationOrganization {

	void saveOrganization(OrganizationEntity organizationEntity);
	
	Iterable<OrganizationEntity> findAllOrganizations();
	
	OrganizationEntity findById(Long id);
	
	void updateOrganization(OrganizationEntity organizationEntity);
	
	void deleteOrganizationById(Long id);
}
