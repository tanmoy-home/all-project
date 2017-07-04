package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.CityMaster;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.model.tenant.CityView;
import com.rssoftware.ou.tenant.dao.CityDao;
import com.rssoftware.ou.tenant.service.CityService;

@Service
public class CityServiceImpl implements CityService {

	private final static Logger logger = LoggerFactory.getLogger(CityServiceImpl.class);

	@Autowired
	private CityDao cityDao; 

	@Override
	public List<CityView> getCityList() throws IsoMsgException {
		List<CityMaster> cityM = cityDao.getAll();

		if (cityM != null) {
			List<CityView> cv = new ArrayList<CityView>(cityM.size());

			for (CityMaster c : cityM) {
				cv.add(mapFromCity(c));
			}

			return cv;
		}

		return null;

	}

	@Override
	public List<CityView> getCityByStateList(Long stateId) throws IsoMsgException {
		List<CityView> retiriveCityByStateList = Collections.emptyList();
		List<CityMaster> cityByStateList = Collections.emptyList();
		
		try{
			cityByStateList = cityDao.getCityByState(stateId);
			if(!cityByStateList.isEmpty() && cityByStateList.size()>0){
				retiriveCityByStateList = fetchCityViewList(cityByStateList);
				retiriveCityByStateList.add(0,new CityView(new Long(0), "Please Select"));
			}
		}catch(DataAccessException dae){
			logger.error( dae.getMessage(), dae);
	        logger.info("In Excp : " + dae.getMessage());
		}
		
		return retiriveCityByStateList;
	}

	private static CityView mapFromCity(CityMaster city) {
		if (city == null) {
			return null;
		}

		CityView cView = new CityView();
		cView.setCityId(city.getCityId());
		cView.setCityName(city.getCityName());
		cView.setStateId(city.getStateId());

		return cView;
	}
	
	private static List<CityView> fetchCityViewList(List<CityMaster> cityList){
		List<CityView> cityViews = Collections.emptyList();
		if(!cityList.isEmpty() && cityList.size()>0){
			cityViews = new ArrayList<CityView>();
			for (CityMaster c : cityList) {
				cityViews.add(mapFromCity(c));
			}
		}
		return cityViews;
	}
	
	@Override
	public String retrieveCity(Long cityId) {
		return cityDao.retrieveCity(cityId);
	}
}
