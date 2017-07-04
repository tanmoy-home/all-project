package com.rssoftware.ou.cbsiso.service;

import com.rssoftware.ou.iso8583.util.IsoMapper;
import com.rssoftware.ou.iso8583.util.impl.IsoGenericMapperImpl;
import com.rssoftware.ou.schema.cbs.CBSISOConstants;

public class IsoMessageFactory {
	private static IsoMessageFactory instance = null;
	
	protected IsoMessageFactory() {
	}
		   
	public static IsoMessageFactory getInstance() {
		if(instance == null) {
			instance = new IsoMessageFactory();
		}
		    return instance;
	}
	
	public IsoMapper getMessagingEngine(String parserModel){
		if (CBSISOConstants.isoDocEngine.equalsIgnoreCase(parserModel)){
			return new IsoGenericMapperImpl();
		}else if (CBSISOConstants.isoTypeEngine.equalsIgnoreCase(parserModel)){
			return new IsoGenericMapperImpl();
		}else
			return new IsoGenericMapperImpl();
	}

}
