package com.rssoftware.ou.service;


public interface GlobalTemplateService {
	void saveGlobalTemplate(String name, String charValue);
	void saveGlobalTemplate(String name, byte[] byteValue);
	String retrieveStringTemplateByName(String name);
	byte[] retrieveByteArrayTemplateByName(String name);
}
