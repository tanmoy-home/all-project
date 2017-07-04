package com.rssoftware.ou.cbsiso.service;

import java.util.Map;

import com.rssoftware.ou.iso8583.util.impl.BadValueException;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;

public interface ISOMessageService {
	
	public byte[] packISOMsg(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException, BadValueException;
	public  Map<Integer,? extends Object> unpackISOMsg(byte[] array) throws IsoMsgException;

}
