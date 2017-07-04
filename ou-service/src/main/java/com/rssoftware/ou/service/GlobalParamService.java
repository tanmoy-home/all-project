package com.rssoftware.ou.service;


public interface GlobalParamService {
	void saveGlobalParam(String name, String charValue);
	void saveGlobalParam(String name, byte[] byteValue);
	String retrieveStringParamByName(String name);
	byte[] retrieveByteArrayParamByName(String name);

}
