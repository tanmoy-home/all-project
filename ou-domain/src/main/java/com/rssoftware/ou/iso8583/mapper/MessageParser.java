package com.rssoftware.ou.iso8583.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.iso8583.exception.ParseException;
import com.rssoftware.ou.iso8583.message.FinTxnDrRes210;
import com.rssoftware.ou.iso8583.message.ISO8583;

public class MessageParser {

	private static final Logger log = LoggerFactory.getLogger(MessageParser.class);
	public static ISO8583 parse(byte[] inMsg) throws ParseException{
		
		return new FinTxnDrRes210(); // just a test message
	}
}
