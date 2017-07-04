package com.rssoftware.ou.common;

import java.io.Serializable;

import org.bbps.schema.BillerFetchResponse;

public class BatchRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4617149942997812893L;

	private final String tenantId;
	private final TypeOfBatch typeOfBatch;
	
	
	private BillerFetchResponse billerFetchResponse;
	private String refId;
	
	
	public BatchRequest(String tenantId, TypeOfBatch typeOfBatch) {
		super();
		this.tenantId = tenantId;
		this.typeOfBatch = typeOfBatch;
	}
	
	public String getTenantId() {
		return tenantId;
	}

	public TypeOfBatch getTypeOfBatch() {
		return typeOfBatch;
	}

	public BillerFetchResponse getBillerFetchResponse() {
		return billerFetchResponse;
	}

	public void setBillerFetchResponse(BillerFetchResponse billerFetchResponse) {
		this.billerFetchResponse = billerFetchResponse;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	
}
