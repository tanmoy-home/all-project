package com.rssoftware.ou.database.entity.tenant;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "BILLER_FETCH_REQUEST_RESPONSE")
@NamedQuery(name = "BillerFetchRequestResponse.findAll", query = "SELECT b FROM BillerFetchRequestResponse b")
public class BillerFetchRequestResponse {
	/*
	 * BILLER_ID CHARACTER
	 * VARYING(20), MY_BILLER CHARACTER VARYING(1), DATE_RANGE CHARACTER
	 * VARYING(50), REF_ID CHARACTER VARYING(30), REQUEST_JSON BYTEA,
	 * RESPONSE_JSON BYTEA,
	 */
	//@Id
	//@Column(name = "REQUEST_ID")
	///private String requestId;

	@Id
	@Column(name = "REF_ID")
	private String refId;
	
	@Column(name = "BILLER_ID")
	private String billerId;

	@Column(name = "MY_BILLER")
	private String myBiller;

	@Column(name = "DATE_RANGE")
	private String dateRange;

	

	@Column(name = "REQUEST_JSON")
	private byte[] requestJson;

	@Column(name = "RESPONSE_JSON")
	private byte[] responseJson;
	
	@Column(name = "SCHEDULE_TIME")
	private String scheduleTime;
	
	@Column(name = "REQUEST_TIME")
	private Timestamp requestTime;

	/*public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}*/

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String list) {
		this.billerId = list;
	}

	public String getMyBiller() {
		return myBiller;
	}

	public void setMyBiller(String myBiller) {
		this.myBiller = myBiller;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public byte[] getRequestJson() {
		return requestJson;
	}

	public void setRequestJson(byte[] requestJson) {
		this.requestJson = requestJson;
	}

	public byte[] getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(byte[] responseJson) {
		this.responseJson = responseJson;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Timestamp getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

}
