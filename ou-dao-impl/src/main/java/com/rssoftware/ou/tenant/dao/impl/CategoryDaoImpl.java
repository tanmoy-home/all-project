package com.rssoftware.ou.tenant.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.BusinessCategory;
import com.rssoftware.ou.tenant.dao.CategoryDao;

@Repository
public class CategoryDaoImpl extends GenericDynamicDaoImpl<BusinessCategory, String> implements CategoryDao {

	public CategoryDaoImpl() {
		super(BusinessCategory.class);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public BusinessCategory fetchByName(String cmsCategory) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(BusinessCategory.class);
		criteria.add(Restrictions.eq("categoryName", cmsCategory));
		return (BusinessCategory) criteria.uniqueResult();
	}

}
