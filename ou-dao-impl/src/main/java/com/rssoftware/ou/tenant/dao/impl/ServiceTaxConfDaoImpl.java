package com.rssoftware.ou.tenant.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.ServiceTaxConf;
import com.rssoftware.ou.tenant.dao.ServiceTaxConfDao;

@Repository
public class ServiceTaxConfDaoImpl extends GenericDynamicDaoImpl<ServiceTaxConf, String> implements ServiceTaxConfDao   {
	private static final String FETCH_ALL_ACTIVE_SERVICE_TAX_CONF = "select a from ServiceTaxConf a";
	
	public ServiceTaxConfDaoImpl() {
		super(ServiceTaxConf.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceTaxConf> fetchAllActiveList() {
		List<ServiceTaxConf> tempServiceTaxConfs = 
				(List<ServiceTaxConf>) getSessionFactory().getCurrentSession().
				createQuery(FETCH_ALL_ACTIVE_SERVICE_TAX_CONF).
				list();
		
		return getEffectiveServiceTaxConfList(tempServiceTaxConfs);
	}	
	
	public List<ServiceTaxConf> getEffectiveServiceTaxConfList(List<ServiceTaxConf> tempServiceTaxConfs) {
		List<ServiceTaxConf> serviceTaxConfs = new ArrayList<>();
		for(ServiceTaxConf sTaxConf : tempServiceTaxConfs) {
			if(CommonUtils.isEffective(sTaxConf.getEffctvFrom(), sTaxConf.getEffctvTo())) {
				serviceTaxConfs.add(sTaxConf);
			}
		}
		return serviceTaxConfs;
	}
}