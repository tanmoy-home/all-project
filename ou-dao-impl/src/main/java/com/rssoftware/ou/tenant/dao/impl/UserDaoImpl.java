package com.rssoftware.ou.tenant.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.admin.UserEntity;
import com.rssoftware.ou.tenant.dao.UserDao;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class UserDaoImpl extends GenericDynamicDaoImpl implements UserDao {	
	public UserDaoImpl() {
		super(UserEntity.class);
	}

	@Override
	public UserEntity getUserByUserName(String userName) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(UserEntity.class);
		criteria.add(Restrictions.eq("username", userName));
		UserEntity userEntity = (UserEntity) criteria.uniqueResult();
		return userEntity;
	}
}
