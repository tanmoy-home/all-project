package com.rssoftware.ou.iso8583.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.iso8583.util.impl.BadValueException;
import com.rssoftware.ou.iso8583.util.impl.IsoMapElement;
import com.rssoftware.ou.iso8583.util.impl.IsoMapSubElement;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;

import java.util.Map;


public interface IsoParser {
	
	public static final String ISO_VERSION = "ISO-8859-1";
    public static final String EBCDIC_VERSION = "CP1047";


	
	
	public  Map<Integer,IsoMapElement> unpack(String rawString) throws IsoMsgException;
	public String pack(IsoMapElement mti, Map<Integer, IsoMapElement> data) throws IsoMsgException;
	public String buildSubElementStr(Map<String, IsoMapSubElement> subMap)throws BadValueException;
	
	public String packInHex(IsoMapElement mti, Map<Integer, IsoMapElement> data) throws IsoMsgException;
	public  Map<Integer,IsoMapElement> unpackInHex(String rawString) throws IsoMsgException;

}
