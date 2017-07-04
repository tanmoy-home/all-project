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

import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.database.entity.tenant.PostalCode;
import com.rssoftware.ou.model.tenant.PostalCodeView;
import com.rssoftware.ou.tenant.dao.PostalCodeDao;
import com.rssoftware.ou.tenant.service.PostalCodeService;

@Service
public class PostalCodeServiceImpl implements PostalCodeService {

	private final static Logger logger = LoggerFactory.getLogger(PostalCodeServiceImpl.class);

	@Autowired
	private PostalCodeDao postalcodeDao;
	
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
				retirivePinCodeByStateList.add(0,new PostalCodeView("Please Select"));
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
				retirivePinCodeByCityList.add(0,new PostalCodeView("Please Select"));
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

	@Override
	public PostalCodeView getPostalCodeList() throws IOException {
		
		return null;
	}
}
