package com.rssoftware.ou.iso8583.util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BadValueException extends Exception {

	private static final Logger log = LoggerFactory.getLogger(BadValueException.class);
	public BadValueException(String str) {
		super(str);
	}
}
