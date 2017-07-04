package com.rssoftware.ou.iso8583.util.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IsoMsgException extends Exception {

	private static final Logger log = LoggerFactory.getLogger(IsoMsgException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 7683370397236961691L;
	private String exceptionString = null;
	public IsoMsgException(String exceptionString) {
		// TODO Auto-generated constructor stub
		this.exceptionString = exceptionString;
	}

	public String toString() {
		return "An error has occurred while unpacking the isomessage\n"+exceptionString;
	}
	
	public String getMessage() {
		return "An error has occurred while unpacking the isomessage\n"+exceptionString;
	}
}
