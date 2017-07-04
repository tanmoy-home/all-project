package org.npci.iso.imps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.npci.iso.imps.custom.CardAcceptorLocation;
import org.npci.iso.imps.custom.TlvFormat;
import org.npci.iso.util.ISOUtil;
import org.nulleins.formats.iso8583.Message;
import org.nulleins.formats.iso8583.MessageFactory;
import org.nulleins.formats.iso8583.types.MTI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Maps;


public class ISOMessage {

	private static final Logger log = LoggerFactory.getLogger(ISOMessage.class);

	private static final String CONTEXT_PATH = "classpath:org/nulleins/formats/iso8583/ISOMessage-context.xml";

	private static final ApplicationContext context = new ClassPathXmlApplicationContext(
			CONTEXT_PATH);
	
	private static final int HEADER_LENGTH = 2;

	/**
	 * Create request message
	 * @param Map<String, Object> params
	 * @return Byte Array {byte[]}
	 * @throws IOException
	 */
	
	public byte[] createRequestMessage(String mti, Map<Integer, Object> params) throws IOException {
		MessageFactory bankMessages = context.getBean("isoMessages",MessageFactory.class);
		Message message = bankMessages.createByNumbers(MTI.create(mti), params);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bankMessages.writeToStream(message, baos);
		byte[] dataArr = baos.toByteArray();
		int width = dataArr.length;
		byte[] data = new byte[2 + width];
		data[0] = (byte) ((width >>> 8) & 0xFF);
		data[1] = (byte) (width & 0xFF);
		System.arraycopy(dataArr, 0, data, 2, width);
		return data;
	}

	/**
	 * Response 
	 * @param messageData => Byte Array
	 * @return Map<Integer, Object>
	 * @throws ParseException
	 * @throws IOException
	 */
	
	public Map<Integer, Object> getResponseMessage(byte[] messageData) throws ParseException, IOException{
		MessageFactory bankMessages = context.getBean("isoMessages",	MessageFactory.class);
		byte[] dataWithoutHeader = new byte[messageData.length - HEADER_LENGTH];
		System.arraycopy(messageData, HEADER_LENGTH, dataWithoutHeader, 0, messageData.length - HEADER_LENGTH);
		final Message response = bankMessages.parse(dataWithoutHeader);
		Map<Integer, Object> outParams = new HashMap<>(Maps.transformValues(response.getFields(), MessageFactory.fromOptional()));
		outParams.put(0, response.getMTI().toString());
		return outParams;
	}
}
