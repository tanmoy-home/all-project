package com.rssoftware.ou.iso8583.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseException extends Exception {

	private static final Logger log = LoggerFactory.getLogger(ParseException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 7590722646779064029L;

	public ParseException() {
		super();
	}

	public ParseException(String message) {
		super(message);
	}

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	
}
