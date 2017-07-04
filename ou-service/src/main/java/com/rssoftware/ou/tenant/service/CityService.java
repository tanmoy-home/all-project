package com.rssoftware.ou.tenant.service;

import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.model.tenant.CityView;

public interface CityService {
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<CityView> getCityList() throws IsoMsgException;

	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	List<CityView> getCityByStateList(Long stateid) throws IsoMsgException;
	
	@TenantTransactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, readOnly = true)
	public String retrieveCity(Long cityId);

}
