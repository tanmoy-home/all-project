package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.InterchangeFeeConf;
import com.rssoftware.ou.database.entity.tenant.ServiceTaxConf;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.ServiceTaxConfView;
import com.rssoftware.ou.tenant.dao.ServiceTaxConfDao;
import com.rssoftware.ou.tenant.service.ServiceTaxConfService; 

@Service
public class ServiceTaxConfServiceImpl implements ServiceTaxConfService {

	@Autowired
	private ServiceTaxConfDao serviceTaxConfDao;
	
	@Override
	public List<ServiceTaxConfView> fetchAll() {
		List<ServiceTaxConf> sTaxConfList =  serviceTaxConfDao.getAll();
		List<ServiceTaxConfView> sTaxConfViewList = new ArrayList<>();
		sTaxConfList.forEach( sTaxConf-> {
			sTaxConfViewList.add(mapFrom(sTaxConf));
		});
		return sTaxConfViewList;
	}

	@Override
	public ServiceTaxConfView fetch(String serviceTaxConfId) {
		return mapFrom(serviceTaxConfDao.get(serviceTaxConfId));
	}

	@Override
	public void save(ServiceTaxConfView serviceTaxConfView) {
		serviceTaxConfDao.createOrUpdate(mapTo(serviceTaxConfView));
	}

	@Override
	public void delete(ServiceTaxConfView serviceTaxConfView) {
		serviceTaxConfDao.delete(mapTo(serviceTaxConfView));
	}
	
	@Override
	public List<ServiceTaxConfView> fetchAllActiveList() {
		List<ServiceTaxConfView> sTaxConfViewList = new ArrayList<>();
		for(ServiceTaxConf sTaxConf : serviceTaxConfDao.fetchAllActiveList()) {
			sTaxConfViewList.add(mapFrom(sTaxConf));
		}
		return sTaxConfViewList;
	}
	
	/**
	 * @param serviceTaxConfView
	 * @return
	 */
	private ServiceTaxConf mapTo(ServiceTaxConfView serviceTaxConfView) {
		ServiceTaxConf sTaxConf = new ServiceTaxConf();
		serviceTaxConfView.setServiceTaxConfId(UUID.randomUUID().getLeastSignificantBits());
		sTaxConf.setServiceTaxConfId(serviceTaxConfView.getServiceTaxConfId());
		sTaxConf.setBillerCategory(serviceTaxConfView.getBillerCategory());
		sTaxConf.setCodes(StringUtils.join(serviceTaxConfView.getCodes(),','));
		sTaxConf.setFeeType(serviceTaxConfView.getFeeType());
		sTaxConf.setFeeSubType(serviceTaxConfView.getFeeSubType());
		sTaxConf.setEffctvFrom(serviceTaxConfView.getEffctvFrom());
		sTaxConf.setEffctvTo(serviceTaxConfView.getEffctvTo());
		return sTaxConf;
	}
	
	private ServiceTaxConfView mapFrom(ServiceTaxConf serviceTaxConf) {
		if (null == serviceTaxConf) {
			return null;
		}
		
		ServiceTaxConfView serviceTaxConfView = new ServiceTaxConfView();
		serviceTaxConfView.setServiceTaxConfId(serviceTaxConf.getServiceTaxConfId());
		serviceTaxConfView.setBillerCategory(serviceTaxConf.getBillerCategory());
		serviceTaxConfView.setCodes(Arrays.asList(serviceTaxConf.getCodes().split(",")));
		serviceTaxConfView.setFeeType(serviceTaxConf.getFeeType());
		serviceTaxConfView.setFeeSubType(serviceTaxConf.getFeeSubType());
		serviceTaxConfView.setEffctvFrom(serviceTaxConf.getEffctvFrom());
		serviceTaxConfView.setEffctvTo(serviceTaxConf.getEffctvTo());
		return serviceTaxConfView;	
	}
}