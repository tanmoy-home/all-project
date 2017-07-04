package com.rssoftware.ou.tenant.service;


public interface TemplateService {
	void saveGlobalTemplate(String name, String charValue);
	void saveGlobalTemplate(String name, byte[] byteValue);
	void saveTenantTemplate(String name, String charValue);
	void saveTenantTemplate(String name, byte[] byteValue);
	String retrieveStringTemplateByName(String name);
	byte[] retrieveByteArrayTemplateByName(String name);
}
