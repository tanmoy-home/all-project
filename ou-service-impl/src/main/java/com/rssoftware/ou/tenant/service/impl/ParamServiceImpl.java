package com.rssoftware.ou.tenant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rssoftware.ou.service.GlobalParamService;
import com.rssoftware.ou.service.impl.GlobalParamServiceImpl;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.TenantParamService;

@Service
public class ParamServiceImpl implements ParamService {
	
	
	private final static Logger logger = LoggerFactory.getLogger(ParamServiceImpl.class);
	
	@Autowired
	private GlobalParamService globalParamService;
	
	@Autowired
	private TenantParamService tenantParamService;
	
	@Override
	public void saveGlobalParam(String name, String charValue) {
		globalParamService.saveGlobalParam(name, charValue);
	}

	@Override
	public void saveGlobalParam(String name, byte[] byteValue) {
		globalParamService.saveGlobalParam(name, byteValue);	}

	@Override
	public void saveTenantParam(String name, String charValue) {
		tenantParamService.saveTenantParam(name, charValue);
	}

	@Override
	public void saveTenantParam(String name, byte[] byteValue) {
		tenantParamService.saveTenantParam(name, byteValue);
	}

	@Override
	public String retrieveStringParamByName(String name) {
		String template = tenantParamService.retrieveStringParamByName(name);
		
		if (template == null){
			template = globalParamService.retrieveStringParamByName(name);
		}
		logger.info(name.toUpperCase()+"  "+(template==null?"NOT FOUND":template));
		return template;
	}
	
	public String retrieveStringParamByName(String name, boolean isLocal) {
		TenantParamServiceImpl tenantParamService = new TenantParamServiceImpl();
		GlobalParamServiceImpl globalParamService = new GlobalParamServiceImpl();
		
		String template = tenantParamService.retrieveStringParamByName(name, isLocal);
		
		if (template == null){
			template = globalParamService.retrieveStringParamByName(name, isLocal);
		}	
		return template;		
	}

	@Override
	public byte[] retrieveByteArrayParamByName(String name) {
		byte[] template = tenantParamService.retrieveByteArrayParamByName(name);
		
		if (template == null){
			template = globalParamService.retrieveByteArrayParamByName(name);
		}
		
		return template;
	}

	public byte[] retrieveByteArrayParamByName(String name, boolean isLocal) {
		TenantParamServiceImpl tenantParamService = new TenantParamServiceImpl();
		GlobalParamServiceImpl globalParamService = new GlobalParamServiceImpl();
		byte[] template = tenantParamService.retrieveByteArrayParamByName(name, isLocal);
		if (template == null){
			template = globalParamService.retrieveByteArrayParamByName(name, isLocal);
		}
		return template;
	}
}
