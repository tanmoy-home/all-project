
package com.rssoftware.ou.tenant.service;

import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.model.tenant.CategoryView;
import com.rssoftware.ou.model.tenant.CityView;
import com.rssoftware.ou.model.tenant.PostalCodeView;
import com.rssoftware.ou.model.tenant.StateView;

public interface CommonService {
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public CategoryView fetchByName(String cmsCategory) throws DataAccessException;
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<CategoryView> fetchAll() throws DataAccessException;
	
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CityView> getCityList() throws IOException;
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<CityView> getCityByStateList(Long stateid) throws IOException;	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public String retrieveCity(Long cityId);
	
	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<StateView> getAllState() throws DataAccessException;	
	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public String retrieveState(Long valueOf);
	
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public PostalCodeView getPostalCodeList() throws IOException;
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<PostalCodeView> fetchAllPostalCode();
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<PostalCodeView> retirivePinByStateList(Long stateId);
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public List<PostalCodeView> retirivePinByCityList(Long cityId);

}
