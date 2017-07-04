package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;

public interface RoleDao extends GenericDao<RoleEntity, Long> {

	List<RoleEntity> getAuthenticRoles();

}
