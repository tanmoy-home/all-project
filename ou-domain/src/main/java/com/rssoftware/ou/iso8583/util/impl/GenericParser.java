package com.rssoftware.ou.iso8583.util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.rssoftware.ou.iso8583.util.IsoParser;
import com.rssoftware.ou.iso8583.util.impl.xml.BeanPopulator;

public class GenericParser implements IsoParser {

	private static final Logger log = LoggerFactory.getLogger(GenericParser.class);
	private Map<String, String> parsedMessage = new HashMap<String, String>();
	private String payload = null;
	private Map<Integer, IsoMapElement> isoMap = new HashMap<Integer, IsoMapElement>();
	private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

	public GenericParser() {
		super();
		BeanPopulator populator = new BeanPopulator();
		isoMap = populator.getIsoMap();
	}

	@Override
	public Map<Integer,IsoMapElement> unpack(String rawString)
			throws IsoMsgException {
		List<IsoMapElement> returnElements = new ArrayList<IsoMapElement>();
		Map<Integer,IsoMapElement> returnMap = new TreeMap<Integer, IsoMapElement>();
		try {
			parsedMessage.put("MTI", rawString.substring(0, 4));
			IsoMapElement elementMti = new IsoMapElement("MTI", 4,
					String.class, 0);
			elementMti.setValue(rawString.substring(0, 4));
			returnElements.add(elementMti);
			// System.out.println(rawString.substring(0, 4));
			// get primary bitmap
			String primaryBitMap = rawString.substring(4, 20);
			// System.out.println(primaryBitMap);
			//System.out.println("bitmapstr:::"+primaryBitMap);
			String primaryBitMapBin = getBitMap(primaryBitMap);
			//System.out.println("bitmap:::"+primaryBitMap);
			// System.out.println(primaryBitMapBin);
			String bitmapstr = primaryBitMapBin;
			Integer firstBit = Integer.parseInt(primaryBitMapBin
					.substring(0, 1));
			if (firstBit > 0) { // There is a secondary bitmap
				String secondaryBitMap = rawString.substring(20, 36);
				// System.out.println("secondaryBitMap " + secondaryBitMap);
				String secondaryBitMapBin = getBitMap(secondaryBitMap);
				// System.out.println("pri bit: " + bitmapstr);
				// System.out.println("sec bit: " + secondaryBitMapBin);
				bitmapstr += secondaryBitMapBin;
				firstBit = Integer.parseInt(secondaryBitMapBin.substring(0, 1));
				if (firstBit > 0) { // A tertiary bitmap present
					String tertiaryBitMap = rawString.substring(36, 52);
					String tertiaryBitMapBin = getBitMap(tertiaryBitMap);
					bitmapstr += tertiaryBitMapBin;
					payload = rawString.substring(52, rawString.length());
				} else {
					payload = rawString.substring(36, rawString.length());
				}
			} else {
				payload = rawString.substring(20, rawString.length());
			}
			//System.out.println("bitmapstr::::"+bitmapstr);
			List<Integer> posElementPresent = new ArrayList<Integer>();
			for (int i = 1; i <= bitmapstr.length(); i++) {
				char presentbit = bitmapstr.charAt(i - 1);
				if (Integer.parseInt(presentbit + "") > 0) {
					posElementPresent.add(new Integer(i));
				}
			}
			
			//for (Integer i : posElementPresent)
			///System.out.println("Pos" + i);
			//System.out.println(payload);
			int curpos = 0;
			for (Integer i : posElementPresent) {
				if (i == 1)
					continue;
				IsoMapElement element = isoMap.get(i);
				if (element.getVarlength() == 0) {
					int length = element.getLength();
					int endidx = curpos + length;
				/*	parsedMessage.put(element.getName(),
							payload.substring(curpos, endidx));*/				
					element.setValue(payload.substring(curpos, endidx));
					returnElements.add(element);
					/*System.out.println(i + ":::" + element.getName() + "::::"
							+ payload.substring(curpos, endidx));*/
					curpos = endidx;
				} else {
					int lenvar = element.getVarlength();
					//System.out.println(element.getName());
					int variableLength = 0;
					if (lenvar < 3) {
						variableLength = Integer.parseInt(payload.substring(
								curpos, curpos + 2));
						curpos = curpos + 2;
					} else if (lenvar < 4) {
						variableLength = Integer.parseInt(payload.substring(
								curpos, curpos + 3));
						curpos = curpos + 3;
					}
					// System.out.println(curpos+"--ddd---"+variableLength);
					String strVar = payload.substring(curpos, curpos
							+ variableLength);
					curpos = curpos + variableLength;
					//strVar.replaceFirst("^0+(?!$)", "");
					// int lengthofvar = Integer.parseInt(strLength);
					// int endidx = curpos + lengthofvar;
					parsedMessage.put(element.getName(), strVar);
					/*System.out.println(i + ":::" + element.getName() + "::::"
							+ strVar);*/

					// curpos = endidx;
					element.setValue(strVar);
					returnElements.add(element);
				}
			}
			
			//Setting to the Map to return
			
			for(IsoMapElement element : returnElements) {
				returnMap.put(element.getPos(), element);
			}
			
		} catch (Exception ex) {
			//System.out.println("rawString:"+rawString);
            throw new IsoMsgException(ex.getMessage());
		}
		return returnMap;
	}
	
	@Override
	public Map<Integer, IsoMapElement> unpackInHex(String rawStringInHEX) throws IsoMsgException {
		
		List<IsoMapElement> returnElements = new ArrayList<IsoMapElement>();
		Map<Integer,IsoMapElement> returnMap = new TreeMap<Integer, IsoMapElement>();
		String payLoadInHEX = "";	
		try {
			
			String lengthInHex = rawStringInHEX.substring(0,8);
			int totalLength = Integer.parseInt(lengthInHex, 16) * 2 ;
			
			String mtiValue = hexToString(rawStringInHEX.substring(8,16 ));
		
			parsedMessage.put("MTI", mtiValue);
			 
			IsoMapElement elementMti = new IsoMapElement("MTI", 4,
					String.class, 0);
			elementMti.setValue(mtiValue);
			returnElements.add(elementMti);
			
			 
			 
			// System.out.println(rawString.substring(0, 4));
			// get primary bitmap
			String primaryBitMap = rawStringInHEX.substring(16, 32);
			// System.out.println(primaryBitMap);
			//System.out.println("bitmapstr:::"+primaryBitMap);
			String primaryBitMapBin = getBitMap(primaryBitMap);
			
			
			//System.out.println("bitmap:::"+primaryBitMap);
			// System.out.println(primaryBitMapBin);
			String bitmapstr = primaryBitMapBin;
			Integer firstBit = Integer.parseInt(primaryBitMapBin
					.substring(0, 1));
			if (firstBit > 0) { // There is a secondary bitmap
				String secondaryBitMap = rawStringInHEX.substring(32, 48);
				 //System.out.println("secondaryBitMap " + secondaryBitMap);
				String secondaryBitMapBin = getBitMap(secondaryBitMap);
				// System.out.println("pri bit: " + bitmapstr);
				// System.out.println("sec bit: " + secondaryBitMapBin);
				bitmapstr += secondaryBitMapBin;
				firstBit = Integer.parseInt(secondaryBitMapBin.substring(0, 1));
				if (firstBit > 0) { // A tertiary bitmap present
					String tertiaryBitMap = rawStringInHEX.substring(48, 64);
					String tertiaryBitMapBin = getBitMap(tertiaryBitMap);
					bitmapstr += tertiaryBitMapBin;
					payLoadInHEX = rawStringInHEX.substring(64, rawStringInHEX.length());
				} else {
					payLoadInHEX = rawStringInHEX.substring(48, rawStringInHEX.length());
				}
			} else {
				payLoadInHEX = rawStringInHEX.substring(32, rawStringInHEX.length());
			}
			//System.out.println("payLoadInHEX===" + payLoadInHEX);
			
			payload = hexToString(payLoadInHEX);
			
			//System.out.println("bitmapstr::::"+bitmapstr);
			List<Integer> posElementPresent = new ArrayList<Integer>();
			for (int i = 1; i <= bitmapstr.length(); i++) {
				char presentbit = bitmapstr.charAt(i - 1);
				if (Integer.parseInt(presentbit + "") > 0) {
					posElementPresent.add(new Integer(i));
				}
			}
			
			//for (Integer i : posElementPresent)
			///System.out.println("Pos" + i);
			//System.out.println(payload);
			int curpos = 0;
			for (Integer i : posElementPresent) {
				if (i == 1)
					continue;
				if(i==35){
					if (log.isDebugEnabled()) { 
						log.debug("aaa");
					};
				}
				IsoMapElement element = isoMap.get(i);
				if (element.getVarlength() == 0) {
					int length = element.getLength();
					int endidx = curpos + length;
				/*	parsedMessage.put(element.getName(),
							payload.substring(curpos, endidx));*/				
					element.setValue(payload.substring(curpos, endidx));
					returnElements.add(element);
					/*System.out.println(i + ":::" + element.getName() + "::::"
							+ payload.substring(curpos, endidx));*/
					curpos = endidx;
				} else {
					int lenvar = element.getVarlength();
					//System.out.println(element.getName());
					int variableLength = 0;
					if (lenvar < 3) {
						try{
							variableLength = Integer.parseInt(payload.substring(
								curpos, curpos + 2));
						}catch(Exception ex){
							variableLength = Integer.parseInt(payload.substring(
								curpos, curpos + 2),16);
						}
						curpos = curpos + 2;
					} else if (lenvar < 4) {
						variableLength = Integer.parseInt(payload.substring(
								curpos, curpos + 3));
						curpos = curpos + 3;
					}
					// System.out.println(curpos+"--ddd---"+variableLength);
					String strVar = payload.substring(curpos, curpos
							+ variableLength);
					curpos = curpos + variableLength;
					//strVar.replaceFirst("^0+(?!$)", "");
					// int lengthofvar = Integer.parseInt(strLength);
					// int endidx = curpos + lengthofvar;
					parsedMessage.put(element.getName(), strVar);
					/*System.out.println(i + ":::" + element.getName() + "::::"
							+ strVar);*/

					// curpos = endidx;
					element.setValue(strVar);
					returnElements.add(element);
				}
			}
			
			//Setting to the Map to return
			
			for(IsoMapElement element : returnElements) {
				returnMap.put(element.getPos(), element);
			}
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
            throw new IsoMsgException(ex.getMessage());
		}
		return returnMap;

	}
	
	private String getBitMap(String bitMapString) throws IsoMsgException {
		StringBuffer bitMapStringRes = new StringBuffer(64);

		for (int iCo = 0; iCo < 16; iCo++) {
			int iBin = Integer.parseInt(
					String.valueOf(bitMapString.charAt(iCo)), 16);
			String binString = Integer.toBinaryString(iBin);
			binString = lpad(binString, 4);
			bitMapStringRes.append(binString);
		}

		return bitMapStringRes.toString();
	}

	private String lpad(String binString, int numdigits) {
		int length = binString.length();
		int padlength = numdigits - length;
		String retBinString = "";
		for (int i = 0; i < padlength; i++)
			retBinString += "0";
		retBinString += binString;
		return retBinString;
	}

	@Override
	public String pack(IsoMapElement mti, Map<Integer, IsoMapElement> data)
			throws IsoMsgException {
		
		try{
		TreeMap<Integer, IsoMapElement> dataSorted = new TreeMap<Integer, IsoMapElement>(
				data);
		boolean secBitmapExists = false;
		boolean tertBitmapExists = false;
		//System.out.println(dataSorted);
		Set<Integer> keySet = dataSorted.keySet();
		//System.out.println(keySet);
		Iterator<Integer> keyItr = keySet.iterator();
		while (keyItr.hasNext()) {
			Integer keyVal 			= keyItr.next();
			if (keyVal > 64)
				secBitmapExists = true;
			if (keyVal > 128) {
				secBitmapExists = true;
				tertBitmapExists = true;
			}
		}
		//System.out.println(secBitmapExists);
		//System.out.println(tertBitmapExists);
		String priBitMapStr = "";
		if (secBitmapExists)
			priBitMapStr += "1";
		else
			priBitMapStr += "0";
		//System.out.println("bitmapstr::::"+priBitMapStr);
		for (int i = 2; i <= 64; i++) {
			if (keySet.contains(new Integer(i))) {
				priBitMapStr += "1";
			} else {
				priBitMapStr += "0";
			}
		}
		
		String primaryBitMap = emiBitMap(priBitMapStr);
		//System.out.println("bitmapstr:::"+priBitMapStr);
		//System.out.println("bitmap:::"+primaryBitMap);
		String secBitMapStr = "";
		if (secBitmapExists) {
			if (tertBitmapExists)
				secBitMapStr += "1";
			else
				secBitMapStr += "0";
			for (int i = 66; i <= 128; i++) {
				if (keySet.contains(new Integer(i))) {
					secBitMapStr += "1";
				} else {
					secBitMapStr += "0";
				}
			}
			String secBitMap = emiBitMap(secBitMapStr);
			primaryBitMap += secBitMap;
			if (tertBitmapExists) {
				String tertBitmapSTr = "0";
				for (int i = 130; i <= 192; i++) {
					if (keySet.contains(new Integer(i))) {
						tertBitmapSTr += "1";
					} else {
						tertBitmapSTr += "0";
					}
				}
				String tertBitMap = emiBitMap(tertBitmapSTr);
				primaryBitMap += tertBitMap;
			}
		}
		//System.out.println(priBitMapStr);
		primaryBitMap = primaryBitMap.toUpperCase();
		//System.out.println("primaryBitMap::::"+primaryBitMap);
		String packStr = mti.getValue();
		       		
        packStr += primaryBitMap;
		//System.out.println(primaryBitMap);
		Set<Integer> keys = dataSorted.keySet();
		
		for (Map.Entry<Integer, IsoMapElement> entry : dataSorted.entrySet()) {
			IsoMapElement element = entry.getValue();
			int length = element.getLength();
			int varlength = element.getVarlength();
			String value = element.getValue();
			//System.out.println("pos==="+element.getPos());
			//System.out.println("isovalue="+value);
			String strAddElem =  lpad(length, varlength, value);
			packStr += strAddElem;
			
		}
				
		 return packStr;
		}catch(Exception ex)
		{
			throw new IsoMsgException(ex.getMessage());
		}
		
	}
	
	@Override
	/**
	 * This method does the packing and returns a value in HEX.
	 */
	public String packInHex(IsoMapElement mti, Map<Integer, IsoMapElement> data)
			throws IsoMsgException {
		
		try{
		TreeMap<Integer, IsoMapElement> dataSorted = new TreeMap<Integer, IsoMapElement>(
				data);
		boolean secBitmapExists = false;
		boolean tertBitmapExists = false;
		//System.out.println(dataSorted);
		Set<Integer> keySet = dataSorted.keySet();
		//System.out.println(keySet);
		Iterator<Integer> keyItr = keySet.iterator();
		while (keyItr.hasNext()) {
			Integer keyVal = keyItr.next();
			if (keyVal > 64)
				secBitmapExists = true;
			if (keyVal > 128) {
				secBitmapExists = true;
				tertBitmapExists = true;
			}
		}
		//System.out.println(secBitmapExists);
		//System.out.println(tertBitmapExists);
		String priBitMapStr = "";
		if (secBitmapExists)
			priBitMapStr += "1";
		else
			priBitMapStr += "0";
		//System.out.println("bitmapstr::::"+priBitMapStr);
		for (int i = 2; i <= 64; i++) {
			if (keySet.contains(new Integer(i))) {
				priBitMapStr += "1";
			} else {
				priBitMapStr += "0";
			}
		}
		
		String primaryBitMap = emiBitMap(priBitMapStr);
		//System.out.println("bitmapstr:::"+priBitMapStr);
		//System.out.println("bitmap:::"+primaryBitMap);
		String secBitMapStr = "";
		if (secBitmapExists) {
			if (tertBitmapExists)
				secBitMapStr += "1";
			else
				secBitMapStr += "0";
			for (int i = 66; i <= 128; i++) {
				if (keySet.contains(new Integer(i))) {
					secBitMapStr += "1";
				} else {
					secBitMapStr += "0";
				}
			}
			String secBitMap = emiBitMap(secBitMapStr);
			primaryBitMap += secBitMap;
			if (tertBitmapExists) {
				String tertBitmapSTr = "0";
				for (int i = 130; i <= 192; i++) {
					if (keySet.contains(new Integer(i))) {
						tertBitmapSTr += "1";
					} else {
						tertBitmapSTr += "0";
					}
				}
				String tertBitMap = emiBitMap(tertBitmapSTr);
				primaryBitMap += tertBitMap;
			} 
		}
		//System.out.println(priBitMapStr);
		primaryBitMap = primaryBitMap.toUpperCase();
		//System.out.println("primaryBitMap::::"+primaryBitMap);
		String packStr = mti.getValue();
			
		packStr += primaryBitMap;
		//System.out.println(primaryBitMap);
		Set<Integer> keys = dataSorted.keySet();
				
		
		 //Starting the logic for converting the Value into HEX.
     	 String packStrInHEX =  "";
		 String mtiInHEX = stringToHex(mti.getValue());
				
		 packStrInHEX = mtiInHEX + primaryBitMap;
								
		for (Map.Entry<Integer, IsoMapElement> entry : dataSorted.entrySet()) {
					IsoMapElement element = entry.getValue();
					int length = element.getLength();
					int varlength = element.getVarlength();
					String value = element.getValue();
					//System.out.println("pos==="+element.getPos());
					//System.out.println("isovalue="+value);
					String strAddElemInHex =  stringToHex(lpad(length, varlength, value));
					packStrInHEX += strAddElemInHex.toUpperCase();
					
				}
		     	if (log.isDebugEnabled()) { 
		     		log.debug("Change in Generic parser");
		     	};
				int appendLenghInDecimal = packStrInHEX.length()/2;
				String strLength = new String(appendLenghInDecimal+"");
				int numberOfDigits = strLength.length();
				String paddedZeroString = "";
				for (int i=0; i<4-numberOfDigits ; i++){
					paddedZeroString +="0";
				}
				
				strLength = paddedZeroString + strLength;
				
				String newAppLenghInHex = "";
				for(char c : strLength.toCharArray()){
					newAppLenghInHex += stringToHex(c+"");
				}
			 //END logic for converting the Value into HEX.
			   return newAppLenghInHex+ packStrInHEX;
			
		}catch(Exception ex)
		{
			log.error(ex.getMessage(), ex);
			if (log.isDebugEnabled()) { 
				log.debug(data.toString());
			};
			throw new IsoMsgException(ex.getMessage());
		}
		
	}
	
	
	
	//Making it a 
	public String buildSubElementStr(Map<String, IsoMapSubElement> subMap)throws BadValueException
	{
		Set<String> subKeys = subMap.keySet();
		String primaryStr = "";
		String secondaryStr = "";
		for (Map.Entry<String, IsoMapSubElement> subEntry : subMap.entrySet()) {
			IsoMapSubElement subElement = subEntry.getValue();
			
			//If Value is Not Set then continue.
			if(subElement.getValue() == null)
				continue;
			
			int lengthOfValue = subElement.getValue().length();
			
			//String lengthOfValueInHex =  Integer.toHexString(lengthOfValue);
			//String valueInEBCDIC = HexConverter(subElement.getValue(), ISO_VERSION,EBCDIC_VERSION);
															
			int length = subElement.getLength();
			int varlength = subElement.getVarlength();
			String value = subElement.getValue();
			
			int lengthConstant = 3;
			
			//This is commented out and a new implementation is added below.Also remove the left padding for the value
			//secondaryStr = subElement.getTagValue() + lpad(length, varlength, lengthOfValueInHex) + lpad(length, varlength, valueInEBCDIC);
			
			secondaryStr = subElement.getTagValue() + lpad(lengthConstant, varlength, Integer.toString(lengthOfValue)) + value;
								
			primaryStr +=  secondaryStr;
		}
		
	    return primaryStr;	
	}
	
	    
	/*
	 * Utility methods.
	 */
	
	private String stringToHex(String inputStr) {
        
		byte[] buf = inputStr.getBytes();
		char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
	}
	
	
	private String hexToString(String txtInHex) {
		byte [] txtInByte = new byte [txtInHex.length() / 2];
        int j = 0;
        for (int i = 0; i < txtInHex.length(); i += 2)
        {
                txtInByte[j++] = Byte.parseByte(txtInHex.substring(i, i + 2), 16);
        }
        return new String(txtInByte);
	}
    //Converts Decimal to HEX in 4 bytes format.
    private static String toPaddedHex(int i) {
    return String.format("%04X", i);
    }

	
	public static String fromHexString(String hex) throws  IOException{
		
		String sourceEncoding = ISO_VERSION;
	    ByteArrayOutputStream bout = new ByteArrayOutputStream();
	    byte[] buffer = new byte[512];
	    int _start=0;
	    for (int i = 0; i < hex.length(); i+=2) {
	        buffer[_start++] = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
	        if (_start >=buffer.length || i+2>=hex.length()) {
	            bout.write(buffer);
	            Arrays.fill(buffer, 0, buffer.length, (byte)0);
	            _start  = 0;
	        }
	    }

	    return  new String(bout.toByteArray(), sourceEncoding);
	}

	  /*Utility Method to Convert to and From Hex
    public static String hexconverter (String strToConvert,String in, String out){
       try {

        Charset charset_in = Charset.forName(out);
        Charset charset_out = Charset.forName(in);

        CharsetDecoder decoder = charset_out.newDecoder();

        CharsetEncoder encoder = charset_in.newEncoder();

        CharBuffer uCharBuffer = CharBuffer.wrap(strToConvert);

        ByteBuffer bbuf = encoder.encode(uCharBuffer);

        CharBuffer cbuf = decoder.decode(bbuf);

        String s = cbuf.toString();

        //System.out.println("Original String is: " + s);
        return s;

    } catch (CharacterCodingException e) {

        //System.out.println("Character Coding Error: " + e.getMessage());
        return "";

     }
    }*/

	private String lpad(int length, int varlength, String value)
			throws BadValueException {
		//System.out.println("Value is" + value);
		//System.out.println("Value Length" + value.length());
		//System.out.println("Defined Length" + length);
		if (varlength == 0) {
			
			if (value.length() > length){
				if (log.isDebugEnabled()) { 
					log.debug(value);
				};
				throw new BadValueException(
						"Value length is greater than expected");
			}
				
		} else {//Change Here for adding 
			if (value.length() > length){
				if (log.isDebugEnabled()) { 
					log.debug(value);
				};
				throw new BadValueException(
						"Value length is greater than expected");
			}
		}
		//String strLength = lpad(""+value.length(), varlength);
		//int valueLength = value.length();
		int toPad = length - varlength;
		//System.out.println("return value==="+lpad(value, toPad));
		if(varlength == 0)
			return lpad(value, toPad);
		else
			if(varlength == 2) {
				return lpad(value.length()+"", 2)+value;
			} 
			else
				if(varlength == 3) {
					return lpad(value.length()+"", 3)+value;
				} 
				else
					return value;
	}

	private String emiBitMap(String binString) throws IsoMsgException {
		String hexStr = "";
		char[] binarybits = binString.toCharArray();
		if (binarybits.length != 64) {
			// System.out.println(binarybits.length);
			throw new IsoMsgException("Not a bitmap");
		}
		//String str  = "";
		int curpos = 0;
		for(int i=0; i<8; i++) {
			int nextpos = curpos + 8;
			String bin = binString.substring(curpos, nextpos);
			//System.out.println("bin:::"+bin);
			Integer val = Integer.parseInt(bin,2);
			//System.out.println("val=="+val);
			String hex = Integer.toString(val,16);
			hex = lpad(hex, 2);
			//System.out.println(hex);
			hexStr += hex;
			curpos = nextpos;
		}
		return hexStr;
	}


}
