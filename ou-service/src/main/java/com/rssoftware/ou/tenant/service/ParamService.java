package com.rssoftware.ou.tenant.service;


public interface ParamService {
	void saveGlobalParam(String name, String charValue);
	void saveGlobalParam(String name, byte[] byteValue);
	void saveTenantParam(String name, String charValue);
	void saveTenantParam(String name, byte[] byteValue);
	String retrieveStringParamByName(String name);
	byte[] retrieveByteArrayParamByName(String name);
}
