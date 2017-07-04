package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.CityMaster;
import com.rssoftware.ou.database.entity.tenant.PostalCode;
import com.rssoftware.ou.database.entity.tenant.StateMaster;
import com.rssoftware.ou.model.tenant.CityView;
import com.rssoftware.ou.model.tenant.PostalCodeView;
import com.rssoftware.ou.model.tenant.StateView;
import com.rssoftware.ou.tenant.dao.CityDao;
import com.rssoftware.ou.tenant.dao.LocationDao;
import com.rssoftware.ou.tenant.dao.PostalCodeDao;

@Service
public class LocationServiceImpl implements com.rssoftware.ou.tenant.service.LocationService {

	private final static Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

	@Autowired
	private LocationDao locDao;
	
	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private PostalCodeDao postalcodeDao;
	
	/*@Override
	public StateView getStateList() throws Exception {		
		return mapFromState(locDao.get(null));
	}*/
	
	@Override 
	public List<StateView> getAllState() throws DataAccessException {
		
		List<StateMaster> StateM = locDao.getAll();
		
		if (StateM != null){
			List<StateView> sv = new ArrayList<StateView>(StateM.size());
			
			for (StateMaster c:StateM){
				sv.add(mapFromState(c));
			}
			
			return sv;
		}
		
		return null;
		
	}
	
	private static StateView mapFromState(StateMaster state){
		if (state == null){
			return null;
		}
		
		StateView sView = new StateView();
		sView.setStateId(state.getStateId());
		sView.setStateName(state.getStateName());
		
		return sView;
	}

	@Override
	public String retrieveState(Long stateId) {
		return locDao.retrieveState(stateId);
	}
	
	@Override
	public List<CityView> getCityList() throws IOException {
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
	public List<CityView> getCityByStateList(Long stateId) throws IOException {
		List<CityView> retiriveCityByStateList = Collections.emptyList();
		List<CityMaster> cityByStateList = Collections.emptyList();
		
		try{
			cityByStateList = cityDao.getCityByState(stateId);
			if(!cityByStateList.isEmpty() && cityByStateList.size()>0){
				retiriveCityByStateList = fetchCityViewList(cityByStateList);
				//retiriveCityByStateList.add(0,new CityView(new Long(0), "Please Select"));
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
	public List<PostalCodeView> fetchAll() {
		List<PostalCode> PCMaster = postalcodeDao.getAll();

		if (PCMaster != null) {
			List<PostalCodeView> pv = new ArrayList<PostalCodeView>(PCMaster.size());

			for (PostalCode c : PCMaster) {
				pv.add(mapFromPostal(c));
			}

			return pv;
		}

		return null;
	}
	
	@Override
	public List<PostalCodeView> retirivePinByStateList(Long stateId) {
		List<PostalCodeView> retirivePinCodeByStateList = Collections.emptyList();
		List<PostalCode> pinByStateList = Collections.emptyList();
		
		try{
			pinByStateList = postalcodeDao.retirivePinByStateList(stateId);
			if(!pinByStateList.isEmpty() && pinByStateList.size()>0){
				retirivePinCodeByStateList = fetchPostalCodeViewList(pinByStateList);
				//retirivePinCodeByStateList.add(0,new PostalCodeView("Please Select"));
			}
		}catch(DataAccessException dae){
			logger.error( dae.getMessage(), dae);
	        logger.info("In Excp : " + dae.getMessage());
		}
		
		return retirivePinCodeByStateList;
	}
	
	@Override
	public List<PostalCodeView> retirivePinByCityList(Long cityId) {
		List<PostalCodeView> retirivePinCodeByCityList = Collections.emptyList();
		List<PostalCode> pinByCityList = Collections.emptyList();
		
		try{
			pinByCityList = postalcodeDao.retirivePinByCityList(cityId);
			if(!pinByCityList.isEmpty() && pinByCityList.size()>0){
				retirivePinCodeByCityList = fetchPostalCodeViewList(pinByCityList);
				//retirivePinCodeByCityList.add(0,new PostalCodeView("Please Select"));
			}
		}catch(DataAccessException dae){
			logger.error( dae.getMessage(), dae);
	        logger.info("In Excp : " + dae.getMessage());
		}		
		return retirivePinCodeByCityList;
	}
	
	private static PostalCodeView mapFromPostal(PostalCode pc){

		if (pc == null){
			return null;
		}
		
		PostalCodeView pView = new PostalCodeView();

		pView.setPinCode(pc.getPinCode());
		pView.setPinLocation(pc.getPinLocation());
		pView.setCityId(pc.getCityId());
		pView.setStateId(pc.getStateId());
		
		return pView;
	}
	
	private static List<PostalCodeView> fetchPostalCodeViewList(List<PostalCode> pinList){
		List<PostalCodeView> pinCodeView = Collections.emptyList();
		if(!pinList.isEmpty() && pinList.size()>0){
			pinCodeView = new ArrayList<PostalCodeView>();
			for (PostalCode pc : pinList) {
				pinCodeView.add(mapFromPostal(pc));
			}
		}
		return pinCodeView;
	}

	
	/*@Override
	public List<CityView> retiriveCityList() throws DataAccessException {
		List<CityView> retiriveCityList = Collections.emptyList();
		List<CityMaster> cityList = Collections.emptyList();
		
		try{
			cityList = locDao.retiriveCityList();
			
			if(!cityList.isEmpty() && cityList.size()>0){
				retiriveCityList = fetchCityViewList(cityList);
				//retiriveCityList.add(0,new CityView(new Long(0), "Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}
		return retiriveCityList;
	}

	@Override
	public List<StateView> retiriveStateList() throws DataAccessException {
		List<StateView> retiriveStateList = Collections.emptyList();
		List<stateMaster> stateList = Collections.emptyList();
		try{
			stateList = locDao.retiriveStateList();
			
			if(!stateList.isEmpty() && stateList.size()>0){
				retiriveStateList = fetchStateViewList(stateList);
				retiriveStateList.add(0,new StateView(new Long(0), "Please Select"));
			}
			
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}
		return retiriveStateList;
	}

	@Override
	public List<CityView> retiriveCityByStateList(Long stateId)
			throws DataAccessException {
		List<CityView> retiriveCityByStateList = Collections.emptyList();
		List<CityMaster> cityByStateList = Collections.emptyList();
		
		try{
			cityByStateList = locDao.retiriveCityByStateList(stateId);
			if(!cityByStateList.isEmpty() && cityByStateList.size()>0){
				retiriveCityByStateList = fetchCityViewList(cityByStateList);
				retiriveCityByStateList.add(0,new CityView(new Long(0), "Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}
		
		return retiriveCityByStateList;
	}
	
	
	@Override
	public List<PostalCodeView> retirivePinCodeByStateList(Long stateId) throws DataAccessException {
		List<PostalCodeView> retirivePinCodeByStateList = Collections.emptyList();
		List<postalcode> pinByStateList = Collections.emptyList();
		
		try{
			pinByStateList = postalDao.retirivePinByStateList(stateId);
			if(!pinByStateList.isEmpty() && pinByStateList.size()>0){
				retirivePinCodeByStateList = fetchPostalCodeViewList(pinByStateList);
				retirivePinCodeByStateList.add(0,new PostalCodeView("Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}
		
		return retirivePinCodeByStateList;
	}
	
	
	@Override
	public List<PostalCodeView> retirivePinCodeByCityList(Long cityId) throws DataAccessException {
		List<PostalCodeView> retirivePinCodeByCityList = Collections.emptyList();
		List<postalcode> pinByCityList = Collections.emptyList();
		
		try{
			pinByCityList = postalDao.retirivePinByCityList(cityId);
			if(!pinByCityList.isEmpty() && pinByCityList.size()>0){
				retirivePinCodeByCityList = fetchPostalCodeViewList(pinByCityList);
				retirivePinCodeByCityList.add(0,new PostalCodeView("Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}		
		return retirivePinCodeByCityList;
	}
	
	//To Retrieve State Name using state id.
	@Override
	public String retrieveState(Long stateId)
			throws DataAccessException {
		String stateName="";
		List<stateMaster> staeSelect= Collections.emptyList();
		
		try{
			staeSelect = locDao.retirieveState(stateId);
			if(!staeSelect.isEmpty() && staeSelect.size()>0){
				stateName=staeSelect.get(0).getStateName();
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}
		
		return stateName;
	}
	//To Retrieve City Name using city id.
	@Override
	public String retrieveCity(Long cityId)
			throws DataAccessException {
		String cityName="";
		List<CityMaster> citySelect= Collections.emptyList();
		
		try{
			citySelect = locDao.retirieveCity(cityId);
			if(!citySelect.isEmpty() && citySelect.size()>0){
				cityName=citySelect.get(0).getCityName();
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}
		
		return cityName;
	}
	
	@SuppressWarnings("unused")
	private static City mapTo(CityMaster city, CityView cview){
		if (city == null){
			city = new City();
		}
		
		city.setCityId(cview.getCityId());
		city.setCityName(cview.getCityName());
		city.setStateId(cview.getStateId());
		
		return city;
	}
	
	private static CityView mapFromCity(City city){
		if (city == null){
			return null;
		}
		
		CityView cView = new CityView();
		cView.setCityId(city.getCityId());
		cView.setCityName(city.getCityName());
		cView.setStateId(city.getStateId());
		
		return cView;
	}
	
	private static List<CityView> fetchCityViewList(List<City> cityList){
		List<CityView> cityViews = Collections.emptyList();
		if(!cityList.isEmpty() && cityList.size()>0){
			cityViews = new ArrayList<CityView>();
			for (City c : cityList) {
				cityViews.add(mapFromCity(c));
			}
		}
		return cityViews;
	}
	
	private static List<StateView> fetchStateViewList(List<State> stateList){
		List<StateView> stateViews = Collections.emptyList();
		if(!stateList.isEmpty() && stateList.size()>0){
			stateViews = new ArrayList<StateView>();
			for (stateMaster s : stateList) {
				stateViews.add(mapFromState(s));
			}
		}
		return stateViews;
	}
	
	private static List<PostalCodeView> fetchPostalCodeViewList(List<PostalCode> pinList){
		List<PostalCodeView> pinCodeView = Collections.emptyList();
		if(!pinList.isEmpty() && pinList.size()>0){
			pinCodeView = new ArrayList<PostalCodeView>();
			for (postalcode pc : pinList) {
				pinCodeView.add(mapFromPostalCode(pc));
			}
		}
		return pinCodeView;
	}
	
	private static PostalCodeView mapFromPostalCode(PostalCode postalCode){
		if (postalCode == null){
			return null;
		}
		
		PostalCodeView pinView = new PostalCodeView();
		pinView.setPinCode(postalCode.getPinCode());
		pinView.setPinLocation(postalCode.getPinLocation());
		pinView.setStateId(postalCode.getStateId());
		pinView.setCityId(postalCode.getCityId());
		
		return pinView;
	}

	@Override
	public List<PostalCodeView> retirivePinCode() throws DataAccessException {
		// TODO Auto-generated method stub
		List<PostalCodeView> retirivePinCodeByStateList = Collections.emptyList();
		List<postalcode> pinByStateList = Collections.emptyList();
		
		try{
			pinByStateList = postalDao.fetchAll();
			if(!pinByStateList.isEmpty() && pinByStateList.size()>0){
				retirivePinCodeByStateList = fetchPostalCodeViewList(pinByStateList);
				retirivePinCodeByStateList.add(0,new PostalCodeView("Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
		}
		
		return retirivePinCodeByStateList;
	}*/
	
	

}
