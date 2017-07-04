package com.rssoftware.ou.tenant.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.service.GlobalTemplateService;
import com.rssoftware.ou.tenant.service.TemplateService;
import com.rssoftware.ou.tenant.service.TenantTemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {
	@Autowired
	private GlobalTemplateService globalTemplateService;
	
	@Autowired
	private TenantTemplateService tenantTemplateService;
	
	@Override
	public void saveGlobalTemplate(String name, String charValue) {
		globalTemplateService.saveGlobalTemplate(name, charValue);
	}

	@Override
	public void saveGlobalTemplate(String name, byte[] byteValue) {
		globalTemplateService.saveGlobalTemplate(name, byteValue);	}

	@Override
	public void saveTenantTemplate(String name, String charValue) {
		tenantTemplateService.saveTenantTemplate(name, charValue);
	}

	@Override
	public void saveTenantTemplate(String name, byte[] byteValue) {
		tenantTemplateService.saveTenantTemplate(name, byteValue);
	}

	@Override
	public String retrieveStringTemplateByName(String name) {
		String template = tenantTemplateService.retrieveStringTemplateByName(name);
		
		if (template == null){
			template = globalTemplateService.retrieveStringTemplateByName(name);
		}
		
		return template;
	}

	@Override
	public byte[] retrieveByteArrayTemplateByName(String name) {
		byte[] template = null;
		
		template = tenantTemplateService.retrieveByteArrayTemplateByName(name);
		
		if (template == null){
			template = globalTemplateService.retrieveByteArrayTemplateByName(name);
		}
		
		return template;
	}

}
