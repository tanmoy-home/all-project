package com.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prototype {
public static final Logger LOG = LoggerFactory.getLogger(Prototype.class);
 
 public void execute() {
 LOG.debug("Execute Method Called");
 }
 
 String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
