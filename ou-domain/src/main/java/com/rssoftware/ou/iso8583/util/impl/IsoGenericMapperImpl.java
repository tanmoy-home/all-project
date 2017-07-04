package com.rssoftware.ou.iso8583.util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.lang.model.element.Element;
import javax.xml.bind.DatatypeConverter;

import com.rssoftware.ou.iso8583.util.IsoMapper;
import com.rssoftware.ou.iso8583.util.IsoParser;
import com.rssoftware.ou.iso8583.util.impl.GenericParser;
import com.rssoftware.ou.iso8583.util.impl.domain.IsoDomainElement;
import com.rssoftware.ou.iso8583.util.impl.domain.IsoDomainSubElement;
import com.rssoftware.ou.iso8583.util.impl.xml.BeanPopulator;




public class IsoGenericMapperImpl implements IsoMapper {

	private static final Logger log = LoggerFactory.getLogger(IsoGenericMapperImpl.class);
	
	BeanPopulator  beanPop = new BeanPopulator();
	IsoParser parser = new GenericParser();
	Map<Integer, IsoMapElement> var = new HashMap<Integer, IsoMapElement>();
	
	private static final int OFFSET = 3;
		
	public  Map<Integer,? extends Object> unpack(String rawString) throws IsoMsgException
	{
		Map<Integer, IsoMapElement> tempMap = parser.unpack(rawString);
		//Converting to a Map of Domain Objects.
		Map<Integer, Object> returnMap = new TreeMap<Integer, Object>();
		
		for (Map.Entry<Integer, IsoMapElement> entry : tempMap.entrySet()) {
	        try{
	        	
	        	IsoMapElement element = entry.getValue();
	        	if(element.isTLV())
	        	{
                    //Put the TLV String as a Map of Tav vs Value
	        		returnMap.put(element.getPos(), parseTLV(element.getValue() ));
	        	}
	        	else{
	        	     returnMap.put(element.getPos(), element.getValue() );
	        	}
	        	
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
			//IsoDomainElement domainObj = (IsoDomainElement)entry.getValue();
			Integer domainKeyPos = entry.getKey();
			Object domainValue = (Object)entry.getValue();
									
			//Getting the isoElement from Bean Populator
			IsoMapElement isoElement = beanPop.getByPosKey(domainKeyPos);
						
			Map<String, IsoMapSubElement> isoSubMap = null;
			String tlvString;
			if(isoElement.isTLV())
			{
				isoSubMap = isoElement.getSubElementMap();
				if(!(domainValue instanceof Map))
					continue;
				
				Map<String, ? extends Object> subDomainMap = (Map<String, ? extends Object>)domainValue;
			    for (Map.Entry<String, ? extends Object> subEntry : subDomainMap.entrySet()) {
			
			    	//IsoDomainSubElement subDomainObj = (IsoDomainSubElement)subEntry.getValue();
			        //IsoMapSubElement isosubElement =  isoSubMap.get(subDomainObj.getTagValue());
			    	String subDomainKey = subEntry.getKey();//subDomainKey is the tagValue
			    	String subDomainValue = (String)subEntry.getValue();//Value is the field value
			    	
			    	//System.out.println("Element Pos" + isoElement.getPos());
			    	//System.out.println("Sub Domain Key #####" + subDomainKey + "Value is " + subDomainValue);
			    	IsoMapSubElement isosubElement =  isoSubMap.get(subDomainKey);
			    	
			        //Setting the value
			        isosubElement.setTagValue(subDomainKey);
			        isosubElement.setValue(subDomainValue);
			    }
			    tlvString = parser.buildSubElementStr(isoSubMap);
                			    
			    isoElement.setValue(tlvString);
			
		    }
			else
			{
				isoElement.setValue((String)domainValue);
			}
			
			var.put(domainKeyPos,   new IsoMapElement(isoElement.getName(), isoElement.getLength(), 
					isoElement.getVarlength(), isoElement.getValue(), isoElement.getPos()));
		
		
	       }
		return parser.pack(mtiElement, var);
	}
	//Pack IN HEX
	public String packInHex(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException,BadValueException
	{
		IsoMapElement mtiElement = new IsoMapElement();
		mtiElement.setValue(mti);
		
		//Getting from the Domain Object Map
		for (Map.Entry<Integer, ? extends Object> entry : mapData.entrySet()) {
			//IsoDomainElement domainObj = (IsoDomainElement)entry.getValue();
			Integer domainKeyPos = entry.getKey();
			Object domainValue = (Object)entry.getValue();
									
			//Getting the isoElement from Bean Populator
			IsoMapElement isoElement = beanPop.getByPosKey(domainKeyPos);
						
			Map<String, IsoMapSubElement> isoSubMap = null;
			String tlvString;
			if(isoElement.isTLV())
			{
				isoSubMap = isoElement.getSubElementMap();
				if(!(domainValue instanceof Map))
					continue;
				
				Map<String, ? extends Object> subDomainMap = (Map<String, ? extends Object>)domainValue;
			    for (Map.Entry<String, ? extends Object> subEntry : subDomainMap.entrySet()) {
			
			    	//IsoDomainSubElement subDomainObj = (IsoDomainSubElement)subEntry.getValue();
			        //IsoMapSubElement isosubElement =  isoSubMap.get(subDomainObj.getTagValue());
			    	String subDomainKey = subEntry.getKey();//subDomainKey is the tagValue
			    	String subDomainValue = (String)subEntry.getValue();//Value is the field value
			    	
			    	//System.out.println("Element Pos" + isoElement.getPos());
			    	//System.out.println("Sub Domain Key #####" + subDomainKey + "Value is " + subDomainValue);
			    	IsoMapSubElement isosubElement =  isoSubMap.get(subDomainKey);
			    	
			        //Setting the value
			        isosubElement.setTagValue(subDomainKey);
			        isosubElement.setValue(subDomainValue);
			    }
			    tlvString = parser.buildSubElementStr(isoSubMap);
                			    
			    isoElement.setValue(tlvString);
			
		    }
			else
			{
				isoElement.setValue((String)domainValue);
			}
			
			var.put(domainKeyPos,   new IsoMapElement(isoElement.getName(), isoElement.getLength(), 
					isoElement.getVarlength(), isoElement.getValue(), isoElement.getPos()));
		
		
	       }

		return parser.packInHex(mtiElement, var);
	}



	public Map<Integer, ? extends Object> unpackInHex(String rawString) throws IsoMsgException {
		
		Map<Integer, IsoMapElement> tempMap = parser.unpackInHex(rawString);
		//Converting to a Map of Domain Objects.
		Map<Integer, Object> returnMap = new TreeMap<Integer, Object>();
		
		for (Map.Entry<Integer, IsoMapElement> entry : tempMap.entrySet()) {
	        try{
	        	
	        	IsoMapElement element = entry.getValue();
	        	if(element.isTLV())
	        	{
                    //Put the TLV String as a Map of Tav vs Value
	        		returnMap.put(element.getPos(), parseTLV(element.getValue() ));
	        	}
	        	else{
	        	     returnMap.put(element.getPos(), element.getValue() );
	        	}
	        }
	        catch(ClassCastException e){
	            throw new IsoMsgException(e.getMessage());
	        }
	    }
		return returnMap;	
	}
	
	//This method parses the TLV and returns the TAG and Value in a Map.
	private Map<? extends Object,? extends Object> parseTLV(String rawString)
	{
		int rawStrLen = rawString.length();
		int startPointer = 0; 
		
		Map<String,String> tempMap = new TreeMap<String,String>();
								
		for(; startPointer < rawStrLen ; )
		{
						
			String tag = rawString.substring(startPointer, startPointer + OFFSET);
			startPointer =  startPointer + OFFSET;
					
			String valueLenth = rawString.substring(startPointer,startPointer+OFFSET);
			int valueLen = Integer.parseInt(valueLenth);
						
			startPointer =  startPointer + OFFSET;
			
			String valueStr = rawString.substring(startPointer, startPointer + valueLen); 
			
			startPointer = startPointer+  valueLen ;
			
			tempMap.put(tag, valueStr);
							
					
		}
		
		return tempMap;
	}
		
	//Returns the HEX String in Bytes
	public byte[] packInByte(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException,BadValueException
	{
		IsoMapElement mtiElement = new IsoMapElement();
		mtiElement.setValue(mti);
		
		//Getting from the Domain Object Map
		for (Map.Entry<Integer, ? extends Object> entry : mapData.entrySet()) {
			//IsoDomainElement domainObj = (IsoDomainElement)entry.getValue();
			Integer domainKeyPos = entry.getKey();
			Object domainValue = (Object)entry.getValue();
									
			//Getting the isoElement from Bean Populator
			IsoMapElement isoElement = beanPop.getByPosKey(domainKeyPos);
						
			Map<String, IsoMapSubElement> isoSubMap = null;
			String tlvString;
			if (log.isDebugEnabled()) { 
				log.debug("domainKeyPos "+domainKeyPos);
			};
			if(isoElement.isTLV())
			{
				isoSubMap = isoElement.getSubElementMap();
				if(!(domainValue instanceof Map))
					continue;
				
				Map<String, ? extends Object> subDomainMap = (Map<String, ? extends Object>)domainValue;
			    for (Map.Entry<String, ? extends Object> subEntry : subDomainMap.entrySet()) {
			
			    	//IsoDomainSubElement subDomainObj = (IsoDomainSubElement)subEntry.getValue();
			        //IsoMapSubElement isosubElement =  isoSubMap.get(subDomainObj.getTagValue());
			    	String subDomainKey = subEntry.getKey();//subDomainKey is the tagValue
			    	String subDomainValue = (String)subEntry.getValue();//Value is the field value
			    	
			    	//System.out.println("Element Pos" + isoElement.getPos());
			    	//System.out.println("Sub Domain Key #####" + subDomainKey + "Value is " + subDomainValue);
			    	IsoMapSubElement isosubElement =  isoSubMap.get(subDomainKey);
			    	
			        //Setting the value
			        isosubElement.setTagValue(subDomainKey);
			        isosubElement.setValue(subDomainValue);
			    }
			    tlvString = parser.buildSubElementStr(isoSubMap);
                			    
			    isoElement.setValue(tlvString);
			
		    }
			else
			{
				isoElement.setValue((String)domainValue);
			}
			
			var.put(domainKeyPos,   new IsoMapElement(isoElement.getName(), isoElement.getLength(), 
					isoElement.getVarlength(), isoElement.getValue(), isoElement.getPos()));
		
		
	       }
	  return toByteArray(parser.packInHex(mtiElement, var));
	}

	//Converts the byteArray to HEX String.
	public Map<Integer, ? extends Object> unpackInByte(byte[] array) throws IsoMsgException {
		
		//Converting to HEX.
		String rawString = toHexString(array);
		
		Map<Integer, IsoMapElement> tempMap = parser.unpackInHex(rawString);
		//Converting to a Map of Domain Objects.
		Map<Integer, Object> returnMap = new TreeMap<Integer, Object>();
		
		for (Map.Entry<Integer, IsoMapElement> entry : tempMap.entrySet()) {
	        try{
	        	
	        	IsoMapElement element = entry.getValue();
	        	if(element.isTLV())
	        	{
                    //Put the TLV String as a Map of Tav vs Value
	        		returnMap.put(element.getPos(), parseTLV(element.getValue() ));
	        	}
	        	else{
	        	     returnMap.put(element.getPos(), element.getValue() );
	        	}
	        }
	        catch(ClassCastException e){
	            throw new IsoMsgException(e.getMessage());
	        }
	    }
		return returnMap;	
	}
	
	//Converts the byteArray to ASCII String.
	public Map<Integer, ? extends Object> unpackInByteThruAscii(byte[] array) throws IsoMsgException {
		
		//Converting to HEX.
		String rawString = new String(array);
		
		Map<Integer, IsoMapElement> tempMap = parser.unpack(rawString);
		//Converting to a Map of Domain Objects.
		Map<Integer, Object> returnMap = new TreeMap<Integer, Object>();
		
		for (Map.Entry<Integer, IsoMapElement> entry : tempMap.entrySet()) {
	        try{
	        	
	        	IsoMapElement element = entry.getValue();
	        	if(element.isTLV())
	        	{
                    //Put the TLV String as a Map of Tav vs Value
	        		returnMap.put(element.getPos(), parseTLV(element.getValue() ));
	        	}
	        	else{
	        	     returnMap.put(element.getPos(), element.getValue() );
	        	}
	        }
	        catch(ClassCastException e){
	            throw new IsoMsgException(e.getMessage());
	        }
	    }
		return returnMap;	
	}
	
	/**
	 * Utility Methods
	 */

	public static String toHexString(byte[] array) {
	    return DatatypeConverter.printHexBinary(array);
	}

	public static byte[] toByteArray(String s) {
	    return DatatypeConverter.parseHexBinary(s);
	}
	
	//Returns the Ascii String(MTI numeric,Bitmap Hex, DE(s) Ascii) in Bytes 
	public byte[] packInByteUsingAscii(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException,BadValueException
	{
		IsoMapElement mtiElement = new IsoMapElement();
		mtiElement.setValue(mti);
		
		//Getting from the Domain Object Map
		for (Map.Entry<Integer, ? extends Object> entry : mapData.entrySet()) {
			//IsoDomainElement domainObj = (IsoDomainElement)entry.getValue();
			Integer domainKeyPos = entry.getKey();
			Object domainValue = (Object)entry.getValue();
									
			//Getting the isoElement from Bean Populator
			IsoMapElement isoElement = beanPop.getByPosKey(domainKeyPos);
						
			Map<String, IsoMapSubElement> isoSubMap = null;
			String tlvString;
			if(isoElement.isTLV())
			{
				isoSubMap = isoElement.getSubElementMap();
				if(!(domainValue instanceof Map))
					continue;
				
				Map<String, ? extends Object> subDomainMap = (Map<String, ? extends Object>)domainValue;
			    for (Map.Entry<String, ? extends Object> subEntry : subDomainMap.entrySet()) {
			
			    	//IsoDomainSubElement subDomainObj = (IsoDomainSubElement)subEntry.getValue();
			        //IsoMapSubElement isosubElement =  isoSubMap.get(subDomainObj.getTagValue());
			    	String subDomainKey = subEntry.getKey();//subDomainKey is the tagValue
			    	String subDomainValue = (String)subEntry.getValue();//Value is the field value
			    	
			    	//System.out.println("Element Pos" + isoElement.getPos());
			    	//System.out.println("Sub Domain Key #####" + subDomainKey + "Value is " + subDomainValue);
			    	IsoMapSubElement isosubElement =  isoSubMap.get(subDomainKey);
			    	
			        //Setting the value
			        isosubElement.setTagValue(subDomainKey);
			        isosubElement.setValue(subDomainValue);
			    }
			    tlvString = parser.buildSubElementStr(isoSubMap);
                			    
			    isoElement.setValue(tlvString);
			
		    }
			else
			{
				isoElement.setValue((String)domainValue);
			}
			
			var.put(domainKeyPos,   new IsoMapElement(isoElement.getName(), isoElement.getLength(), 
					isoElement.getVarlength(), isoElement.getValue(), isoElement.getPos()));
		
		
	       }
		String packedStr = parser.pack(mtiElement, var);
		//System.out.println("Inside packingtobyteusingASCII:"+packedStr);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(hexStringToByteArray(toPaddedHex(packedStr.length())), 0, 2);//creating hex header (of length) and converting to bytearray from hex
		try{
			baos.write(packedStr.getBytes(StandardCharsets.US_ASCII));
		}catch(IOException ex){
			throw new IsoMsgException("IsoMsgException: Root cause:"+ex.getMessage());
		}
	  return baos.toByteArray();//toByteArray(parser.pack(mtiElement, var));
	}
	
    //Converts Decimal to HEX in 4 bytes format.
    private static String toPaddedHex(int i) {
    return String.format("%04X", i);
    }

	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	   
	   
}
