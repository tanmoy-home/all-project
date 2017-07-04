package com.rssoftware.ou.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long>{
	
	@Query("from RoleEntity role_tbl where role_tbl.id in (select rolePrvlgId from RolePrivilegeEntity where roleid=:roleId )")
	Iterable<RoleEntity> findRolesByPrivledge(@Param("roleId")Long rolesPrivId);
	

}
