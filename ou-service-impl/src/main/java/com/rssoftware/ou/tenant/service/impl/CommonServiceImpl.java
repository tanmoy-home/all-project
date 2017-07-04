package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.BusinessCategory;
import com.rssoftware.ou.database.entity.tenant.CityMaster;
import com.rssoftware.ou.database.entity.tenant.PostalCode;
import com.rssoftware.ou.database.entity.tenant.StateMaster;
import com.rssoftware.ou.model.tenant.CategoryView;
import com.rssoftware.ou.model.tenant.CityView;
import com.rssoftware.ou.model.tenant.PostalCodeView;
import com.rssoftware.ou.model.tenant.StateView;
import com.rssoftware.ou.tenant.dao.CategoryDao;
import com.rssoftware.ou.tenant.dao.CityDao;
import com.rssoftware.ou.tenant.dao.LocationDao;
import com.rssoftware.ou.tenant.dao.PostalCodeDao;
import com.rssoftware.ou.tenant.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService{
	
	@Autowired
	private CategoryDao categoryDao; 
	
	@Autowired
	private CityDao cityDao;
	
	@Autowired
	private LocationDao locDao;
	
	@Autowired
	private PostalCodeDao postalcodeDao;

	@Override
	public List<CategoryView> fetchAll() throws DataAccessException {
		
		List<BusinessCategory> category = categoryDao.getAll();
		
		if (category != null){
			List<CategoryView> cv = new ArrayList<CategoryView>(category.size());
			
			for (BusinessCategory c:category){
				cv.add(mapFromCategory(c));
			}
			
			return cv;
		}
		
		return null;
		
	}

	@Override
	public CategoryView fetchByName(String cmsCategory) throws DataAccessException {
		 BusinessCategory category = categoryDao.fetchByName(cmsCategory);
		 return mapFromCategory(category);
	}
	
	private static CategoryView mapFromCategory(BusinessCategory category){
		if (category == null){
			return null;
		}
	
		CategoryView categoryView = new CategoryView();
		categoryView.setCategoryId(category.getCategoryId());
		categoryView.setCategoryName(category.getCategoryName());
		
		return categoryView;
	}
	
	/*********************** CITY********************/
	
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
				retiriveCityByStateList.add(0,new CityView(new Long(0), "Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
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
	
	/*********************** STATE ********************/
	
	
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
	
	/*********************** POSTAL CODE********************/
	
	@Override
	public List<PostalCodeView> fetchAllPostalCode() {
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
				retirivePinCodeByStateList.add(0,new PostalCodeView("Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
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
				retirivePinCodeByCityList.add(0,new PostalCodeView("Please Select"));
			}
		}catch(DataAccessException dae){
			dae.printStackTrace();
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

	@Override
	public PostalCodeView getPostalCodeList() throws IOException {
		
		return null;
	}

}
