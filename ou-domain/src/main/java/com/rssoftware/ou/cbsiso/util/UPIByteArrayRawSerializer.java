package com.rssoftware.ou.cbsiso.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer;

public class UPIByteArrayRawSerializer extends ByteArrayLengthHeaderSerializer{
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public byte[] deserialize(InputStream arg0) throws IOException {
		// TODO Auto-generated method stub
		return readImpsTcpMsg(arg0);
		//return super.deserialize(arg0);
	}
	
	@Override
	public void serialize(byte[] bytes, OutputStream outputStream)
			throws IOException {
		// TODO Auto-generated method stub
		super.serialize(bytes, outputStream);
	}
	
	public byte[] readImpsTcpMsg(InputStream inputStrm) throws IOException {
		
	DataInputStream in = new DataInputStream(inputStrm);
		byte[] responseHeaderFromIMPS = new byte[2];
		in.read(responseHeaderFromIMPS);
		String responseHeaderFromIMPSHex = bytesToHex(responseHeaderFromIMPS);
		int lengthOfHalfDataBlock = hex2decimal(responseHeaderFromIMPSHex);
		

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//baos.write(responseHeaderFromIMPS,0,2);
		byte buffer[] = new byte[1];
		
		for (int s = 1; s <= lengthOfHalfDataBlock; s++) {
			in.read(buffer);
			baos.write(buffer, 0, 1);
			// baos.write(buffer);
			
		}
		byte result[] = baos.toByteArray();

/*		String finalizedRespose = bytesToHex(responseHeaderFromIMPS)
				+ bytesToHex(result);*/
		if (log.isDebugEnabled()) {
			log.debug("Final Response from IMPS:" + result);
		}
		

		return result;
	}
	   final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	   public static String bytesToHex(byte[] bytes) {
	       char[] hexChars = new char[bytes.length * 2];
	       for ( int j = 0; j < bytes.length; j++ ) {
	           int v = bytes[j] & 0xFF;
	           hexChars[j * 2] = hexArray[v >>> 4];
	           hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	       }
	       return new String(hexChars);
	   }
	   
	   public static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
	   

}
