package com.rssoftware.ou.iso8583.util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.rssoftware.ou.iso8583.util.IsoMapper;
import com.rssoftware.ou.iso8583.util.impl.GenericParser;
import com.rssoftware.ou.iso8583.util.impl.domain.IsoDomainElement;
import com.rssoftware.ou.iso8583.util.impl.domain.IsoDomainSubElement;
import com.rssoftware.ou.iso8583.util.impl.xml.BeanPopulator;

/*Do Not use this class,use Generic Mapper Instead*/
@Deprecated
public class IsoMapperImpl implements IsoMapper {

	private static final Logger log = LoggerFactory.getLogger(IsoMapperImpl.class);
	
	BeanPopulator  beanPop = new BeanPopulator();
	GenericParser parser = new GenericParser();
	Map<Integer, IsoMapElement> var = new HashMap<Integer, IsoMapElement>();
	
	public  Map<Integer,? extends Object> unpack(String rawString) throws IsoMsgException
	{
		Map<Integer, IsoMapElement> tempMap= parser.unpack(rawString);
		//Converting to a Map of Domain Objects.
		Map<Integer, IsoDomainElement> returnMap = new TreeMap<Integer, IsoDomainElement>();
		
		for (Map.Entry<Integer, IsoMapElement> entry : tempMap.entrySet()) {
	        try{
	        	
	        	IsoMapElement element = entry.getValue();
	        	returnMap.put(element.getPos(), new IsoDomainElement(element.getName(), element.getLength(),
	        			element.getVarlength(), element.getValue(), element.getPos()) );
	        }
	        catch(ClassCastException e){
	            throw new IsoMsgException(e.getMessage());
	        }
	    }
		return returnMap;
	}
	
	
	
	public String pack(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException,BadValueException
	{
		IsoMapElement mtiElement = new IsoMapElement();
		mtiElement.setValue(mti);
		
		//Getting from the Domain Object Map
		for (Map.Entry<Integer, ? extends Object> entry : mapData.entrySet()) {
			IsoDomainElement domainObj = (IsoDomainElement)entry.getValue();
									
			//Getting the isoElement from Bean Populator
			IsoMapElement isoElement = beanPop.getByPosKey(domainObj.getPos());
						
			Map<String, IsoMapSubElement> isoSubMap = null;
			String tlvString;
			if(isoElement.isTLV())
			{
				isoSubMap = isoElement.getSubElementMap();
			    for (Map.Entry<String, ? extends Object> subEntry : domainObj.getSubElementMap().entrySet()) {
			
			    	IsoDomainSubElement subDomainObj = (IsoDomainSubElement)subEntry.getValue();
			        IsoMapSubElement isosubElement =  isoSubMap.get(subDomainObj.getTagValue());
			        //Setting the value
			        isosubElement.setTagValue(subDomainObj.getTagValue());
			        isosubElement.setValue(subDomainObj.getValue());
			    }
			    tlvString = parser.buildSubElementStr(isoSubMap);
			    isoElement.setValue(tlvString);
			
		    }
			else
			{
				isoElement.setValue(domainObj.getValue());
			}
			
			var.put(domainObj.getPos(),   new IsoMapElement(isoElement.getName(), isoElement.getLength(), 
					isoElement.getVarlength(), isoElement.getValue(), isoElement.getPos()));
		
		
	       }
		return parser.pack(mtiElement, var);
	}



	@Override
	public String packInHex(String mti, Map<Integer, ? extends Object> mapData)
			throws IsoMsgException, BadValueException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Map<Integer, ? extends Object> unpackInHex(String rawString) throws IsoMsgException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public byte[] packInByte(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException, BadValueException
	{
		return null;
	}
	public  Map<Integer,? extends Object> unpackInByte(byte[] array) throws IsoMsgException
	{
		return null;
	}
	
	public  Map<Integer,? extends Object> unpackInByteThruAscii(byte[] array) throws IsoMsgException
	{
		return null;
	}
	
	public byte[] packInByteUsingAscii(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException,BadValueException
	{
		return null;
	}
	
	
}
