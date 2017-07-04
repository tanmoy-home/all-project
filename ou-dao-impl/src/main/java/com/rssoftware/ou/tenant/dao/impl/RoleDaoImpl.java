package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.admin.RoleEntity;
import com.rssoftware.ou.tenant.dao.RoleDao;

@Repository
public class RoleDaoImpl extends GenericDynamicDaoImpl<RoleEntity, Long> implements RoleDao {

	public RoleDaoImpl() {
		super(RoleEntity.class);
	}
	
	@Override
	public List<RoleEntity> getAuthenticRoles() {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(RoleEntity.class);
		criteria.add(Restrictions.eq("isApiAccess", true));
		return criteria.list();
	}

}
