package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name = "ERROR_CODES")
@NamedQuery(name = "ErrorCodes.findAll", query = "SELECT a FROM ErrorCodes a")
public class ErrorCodes implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 *  CODE_ID CHARACTER VARYING(50),
  ERROR_CODE CHARACTER VARYING(50),
  ERROR_MESSAGE CHARACTER VARYING(50),
  RESPONSE_CODE CHARACTER VARYING(40),
  COMPLIANCE_REASON_CODE CHARACTER VARYING(40),
  COMPLIANCE_CODE CHARACTER VARYING(40),
  COMPLIANCE_DESCRIPTION CHARACTER VARYING(100),
	 */
	@Id
	@Column(name = "CODE_ID")
	private String codeId;

	@Column(name = "ERROR_CODE")
	private String errorCode;

	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;

	@Column(name = "RESPONSE_CODE")
	private String responseCode;

	@Column(name = "COMPLIANCE_REASON_CODE")
	private String complianceReasonCode;
	
	@Column(name = "COMPLIANCE_CODE")
	private String complianceCode;

	@Column(name = "COMPLIANCE_DESCRIPTION")
	private String complianceDescription;

	public String getErrorCodeId() {
		return codeId;
	}

	public void setErrorCodeId(String errorCodeId) {
		this.codeId = errorCodeId;
	}

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

	public String getComplianceReasonCode() {
		return complianceReasonCode;
	}

	public void setComplianceReasonCode(String complianceReasonCode) {
		this.complianceReasonCode = complianceReasonCode;
	}

	public String getComplianceCode() {
		return complianceCode;
	}

	public void setComplianceCode(String complianceCode) {
		this.complianceCode = complianceCode;
	}

	public String getComplianceDescription() {
		return complianceDescription;
	}

	public void setComplianceDescription(String complianceDescription) {
		this.complianceDescription = complianceDescription;
	}

	

	
}