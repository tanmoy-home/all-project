package com.rssoftware.ou.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.OrganizationEntity;
import com.rssoftware.ou.repository.OrganizationRepository;
import com.rssoftware.ou.service.ConfigurationOrganization;

@Service
public class ConfigurationOrganizationImpl implements ConfigurationOrganization{

	@Autowired
	public OrganizationRepository organizationRepository;
	
	@Override
	public void saveOrganization(OrganizationEntity organizationEntity) {
		organizationRepository.save(organizationEntity);
	}

	@Override
	public Iterable<OrganizationEntity> findAllOrganizations() {
		List<OrganizationEntity> organizationEntity = new ArrayList<OrganizationEntity>();
		Iterable<OrganizationEntity> newOrganizationEntity = organizationRepository.findAll();
		for(OrganizationEntity organization : newOrganizationEntity) {
			OrganizationEntity entity = new OrganizationEntity();
			entity.setId(organization.getId());
			entity.setOrganizationName(organization.getOrganizationName());
			entity.setParentOrganization(organization.getParentOrganization());
			entity.setIsActive(organization.getIsActive());
			organizationEntity.add(entity);
		}
		return organizationEntity;
	}

	@Override
	public OrganizationEntity findById(Long id) {
		OrganizationEntity organizationEntity = organizationRepository.findOne(id);
		return organizationEntity;
	}

	@Override
	public void updateOrganization(OrganizationEntity organizationEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteOrganizationById(Long id) {
		organizationRepository.delete(id);
		
	}

}
