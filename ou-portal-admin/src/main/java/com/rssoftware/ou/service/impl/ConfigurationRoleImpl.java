package com.rssoftware.ou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.admin.OrganizationEntity;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.repository.RoleRepository;
import com.rssoftware.ou.service.ConfigurationRole;

@Service
public class ConfigurationRoleImpl implements ConfigurationRole{

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public RoleEntity saveRole(RoleEntity roleEntity) {
		roleEntity = roleRepository.save(roleEntity);
		return roleEntity;
	}

	@Override
	public RoleEntity getRole(RoleEntity roleEntity) {
		RoleEntity entity= new RoleEntity();
		//OrganizationVo orgVo= new OrganizationVo();
		OrganizationEntity organizationEntity = new OrganizationEntity();
		//BeanUtils.copyProperties(roleVo, roleEntity);
		entity = roleRepository.findOne(roleEntity.getId());
		organizationEntity.setOrganizationName(roleEntity.getOrganization().getOrganizationName());
		organizationEntity.setIsActive(roleEntity.getOrganization().getIsActive());
		organizationEntity.setParentOrganization(roleEntity.getOrganization().getParentOrganization());
		organizationEntity.setId(roleEntity.getOrganization().getId());
		entity.setOrganization(organizationEntity);
		return entity;
	}
	
	@Override
	public Iterable<RoleEntity> findAllRoles() {
		 return roleRepository.findAll();
	}
	
	@Override
	/*public RoleVo findById(Long id) {
		RoleVo roleVo= new RoleVo();
		OrganizationVo organizationVo= new OrganizationVo();
		RoleEntity roleEntity = roleRepository.findOne(id);
		BeanUtils.copyProperties(roleEntity, roleVo);
		BeanUtils.copyProperties(roleEntity.getOrganization(), organizationVo);
		roleVo.setOrganizationVo(organizationVo);
		return roleVo;
	}*/
	
	public RoleEntity findById(Long id) {
		return roleRepository.findOne(id);
	}

	@Override
	public void deleteRoleByOrganizationId(Long id) {
		roleRepository.delete(id);
	}
	
	

}
