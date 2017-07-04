package com.rssoftware.ou.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;

public interface RolePrivMappingUserRepository extends CrudRepository<UserEntity, Long>{
	
	@Query("from UserEntity where id in (select role_map_tbl.userRoleMappingId.userId from UserRoleMappingEntity role_map_tbl where role_map_tbl.userRoleMappingId.roleId in (select rolePrvlgId from RolePrivilegeEntity where roleid=:roleId ))")
	Iterable<UserEntity> findRolesByPrivledge(@Param("roleId")Long rolesPrivId);
	
	
	@Query("from UserEntity where id in "
			+ "(select role_map_tbl.userRoleMappingId.userId from UserRoleMappingEntity role_map_tbl where role_map_tbl.userRoleMappingId.roleId in "
			+ "(select rolePrvlgId from RolePrivilegeEntity where roleid="
			+ "(select role_map_tbl_inner.userRoleMappingId.roleId from UserRoleMappingEntity role_map_tbl_inner where role_map_tbl_inner.userRoleMappingId.userId="
			+ "(select user.id from UserEntity user where user.username=:userName))))")
	Iterable<UserEntity> findUsersByRolePriviledge(@Param("userName")String userName);
	

}
