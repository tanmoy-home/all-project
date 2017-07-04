package  com.rssoftware.ou.iso8583.util;

import java.util.List;
import java.util.Map;

import com.rssoftware.ou.iso8583.util.impl.BadValueException;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;

public interface IsoMapper {
	
	public  Map<Integer,? extends Object> unpack(String rawString) throws IsoMsgException;
	public String pack(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException, BadValueException;
	
	public String packInHex(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException, BadValueException;
	public  Map<Integer,? extends Object> unpackInHex(String rawString) throws IsoMsgException;
	
	public byte[] packInByte(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException, BadValueException;
	public  Map<Integer,? extends Object> unpackInByte(byte[] array) throws IsoMsgException;
	public  Map<Integer,? extends Object> unpackInByteThruAscii(byte[] array) throws IsoMsgException;
	
	public byte[] packInByteUsingAscii(String mti, Map<Integer, ? extends Object> mapData) throws IsoMsgException, BadValueException;


	
	
}
