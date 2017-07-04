package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PostalCode;
import com.rssoftware.ou.tenant.dao.PostalCodeDao;

@Repository

public class PostalCodeDaoImpl extends GenericDynamicDaoImpl<PostalCode, Long> implements PostalCodeDao{
	
	public PostalCodeDaoImpl() {
		super(PostalCode.class);
	}
	
	@Override
	
	public List<PostalCode> fetchAll() {
		List<PostalCode> PCView = getAll();
		return PCView;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PostalCode> retirivePinByStateList(Long stateId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(PostalCode.class);
		criteria.add(Restrictions.eq("stateId", stateId));
		return (List<PostalCode>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PostalCode> retirivePinByCityList(Long cityId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(PostalCode.class);
		criteria.add(Restrictions.eq("cityId", cityId));
		return (List<PostalCode>) criteria.list();
	}
}
