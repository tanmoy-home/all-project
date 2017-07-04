package com.rssoftware.ou.database.entity.tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "COU_ERROR_CODES")
@NamedQuery(name = "COUErrorCodes.findAll", query = "SELECT a FROM COUErrorCodes a")
public class COUErrorCodes {
	
	@Id
	@Column(name = "ERROR_CODE")
	private String errorCode;
	
	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;
	
	@Column(name = "RESPONSE_CODE")
	private String responseCode;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}
