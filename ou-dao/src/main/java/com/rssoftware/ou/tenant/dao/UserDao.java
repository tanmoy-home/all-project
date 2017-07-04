package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;

@SuppressWarnings("rawtypes")
public interface UserDao extends GenericDao {
	public UserEntity getUserByUserName(String userName);
}