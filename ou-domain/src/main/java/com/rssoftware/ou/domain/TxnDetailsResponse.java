package com.rssoftware.ou.domain;

import java.io.Serializable;
import java.util.List;

import org.bbps.schema.TxnDetail;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties
@SuppressWarnings("serial")
public class TxnDetailsResponse implements Serializable{
	
	private List<TxnDetail> txnDetails;

	public List<TxnDetail> getTxnDetails() {
		return txnDetails;
	}

	public void setTxnDetails(List<TxnDetail> txnDetails) {
		this.txnDetails = txnDetails;
	}
	
	
}
