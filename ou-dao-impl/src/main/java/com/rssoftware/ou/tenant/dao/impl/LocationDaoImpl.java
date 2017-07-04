package com.rssoftware.ou.tenant.dao.impl;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.StateMaster;
import com.rssoftware.ou.tenant.dao.LocationDao;

@Repository
//@Transactional(isolation=Isolation.READ_COMMITTED, readOnly=true)
public class LocationDaoImpl extends GenericDynamicDaoImpl<StateMaster, String> implements LocationDao {

	public LocationDaoImpl() {
		super(StateMaster.class);
	}

	@Override
	public String retrieveState(Long stateId) {
		List<StateMaster> stateMasters = null;
		if (stateId != null) {
			Criteria crit = getSessionFactory().getCurrentSession().createCriteria(StateMaster.class);
			crit.add(Restrictions.eq("stateId", stateId));
			stateMasters = crit.list();
		}
		if(stateMasters!=null)
			return stateMasters.get(0).getStateName();
		else
			return null;
	}

	
}
