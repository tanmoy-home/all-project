package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.CityMaster;
import com.rssoftware.ou.database.entity.tenant.StateMaster;
import com.rssoftware.ou.tenant.dao.CityDao;

@Repository
public class CityDaoImpl extends GenericDynamicDaoImpl<CityMaster, Long> implements CityDao {

	public CityDaoImpl() {
		super(CityMaster.class);
	}
	
	@Override	
	public List<CityMaster> getAllCity() {
		List<CityMaster> CityView = getAll();
		return CityView;
	}
	
	@SuppressWarnings("unchecked")
	@Override	
	public List<CityMaster> getCityByState(Long StateId ) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CityMaster.class);
		criteria.add(Restrictions.eq("stateId", StateId));
		return (List<CityMaster>) criteria.list();
	}
	
	@Override
	public String retrieveCity(Long cityId) {
		List<CityMaster> cityMasters = null;
		if (cityId != null) {
			Criteria crit = getSessionFactory().getCurrentSession().createCriteria(StateMaster.class);
			crit.add(Restrictions.eq("cityId", cityId));
			cityMasters = crit.list();
		}
		if(cityMasters!=null)
			return cityMasters.get(0).getCityName();
		else
			return null;
	}
}
