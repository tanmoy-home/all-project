package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.CityMaster;

public interface CityDao extends GenericDao<CityMaster, Long>{
	
	List<CityMaster> getAllCity();

	List<CityMaster> getCityByState(Long StateId);
	
	public String retrieveCity(Long cityId);


}
