package com.rssoftware.ou.cbsiso.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.serializer.Deserializer;


/**
 * Reads data in an InputStream to a byte[]; expects a 2 byte header of 
 * a hexadecimal length ( this will be included in the resulting byte[]).
 * 
 * For use with IMPS/FRM Spring Integration TCP Socket.
 *
 */
public class UPICustomMsgHeaderDeserializerFRM implements Deserializer {
	
	@Override
	public byte[] deserialize(InputStream arg0) throws IOException {

		return readImpsTcpMsg(arg0);

	}

	public byte[] readImpsTcpMsg(InputStream inputStrm) throws IOException {

		DataInputStream in = new DataInputStream(inputStrm);
		byte[] responseHeaderFromIMPS = new byte[2];
		in.read(responseHeaderFromIMPS);
		//System.out.println("read header complete");
		String responseHeaderFromIMPSHex = bytesToHex(responseHeaderFromIMPS);
/*		System.out.println("responseHeaderFromIMPSHex"
				+ responseHeaderFromIMPSHex);*/
		int lengthOfDataBlockInByte = hex2decimal(responseHeaderFromIMPSHex);
		if(lengthOfDataBlockInByte==0){
			return null;
		}
/*		System.out.println("lengthOfDataBlockInByte_first 2 bytes"
				+ lengthOfDataBlockInByte);*/

		// int lengthOfDataBlockInByte = readHeader(inputStrm);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//baos.write(responseHeaderFromIMPS, 0, 2);
		byte buffer[] = new byte[1];

		for (int s = 1; s <= lengthOfDataBlockInByte; s++) {
			in.read(buffer);
			//System.out.println("s:::" + s);
			baos.write(buffer, 0, 1);
			// baos.write(buffer);
			//System.out.println("Still reading...");
		}
		byte result[] = baos.toByteArray();

		/*
		 * String finalizedRespose = bytesToHex(responseHeaderFromIMPS) +
		 * bytesToHex(result);
		 */

		return result;
	}

	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	private int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

}
