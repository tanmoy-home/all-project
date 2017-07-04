package com.rssoftware.ou.iso8583.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTransactionalMsg implements ISO8583{

	private static final Logger log = LoggerFactory.getLogger(AbstractTransactionalMsg.class);
	protected String transactionId;
	protected String stan;

	public final String getTransactionId() {
		return transactionId;
	}

	public final void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
	}
	
}
