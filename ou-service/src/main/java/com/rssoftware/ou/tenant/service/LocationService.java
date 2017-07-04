package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.model.tenant.CityView;
import com.rssoftware.ou.model.tenant.PostalCodeView;
import com.rssoftware.ou.model.tenant.StateView;

public interface LocationService {
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	List<StateView> getAllState() throws DataAccessException; 
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	String retrieveState(Long valueOf);
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<CityView> getCityList() throws IOException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<CityView> getCityByStateList(Long stateid) throws IOException;
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<PostalCodeView> fetchAll();

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<PostalCodeView> retirivePinByStateList(Long stateId);

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<PostalCodeView> retirivePinByCityList(Long cityId);
	
	/*List<CityView> retiriveCityList() throws DataAccessException;
	List<CityView> retiriveCityByStateList(Long stateId) throws DataAccessException;
	List<StateView> retiriveStateList() throws DataAccessException;
	
	List<PostalCodeView> retirivePinCodeByStateList(Long stateId) throws DataAccessException;
	List<PostalCodeView> retirivePinCodeByCityList(Long cityId) throws DataAccessException;
	List<PostalCodeView> retirivePinCode() throws DataAccessException;
	
	String retrieveCity(Long cityId) throws DataAccessException;
	String retrieveState(Long stateId) throws DataAccessException;*/
	
	
}
