package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TENANT_PARAMS database table.
 * 
 */
@Entity
@Table(name="TENANT_PARAMS")
@NamedQuery(name="TenantParam.findAll", query="SELECT t FROM TenantParam t")
public class TenantParam implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PARAM_NAME")
	private String paramName;

	@Column(name="PARAM_TYPE")
	private String paramType;

	
	@Column(name="PARAM_VALUE_BIN")
	private byte[] paramValueBin;

	
	@Column(name="PARAM_VALUE_CHAR")
	private String paramValueChar;

	public TenantParam() {
	}

	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamType() {
		return this.paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public byte[] getParamValueBin() {
		return this.paramValueBin;
	}

	public void setParamValueBin(byte[] paramValueBin) {
		this.paramValueBin = paramValueBin;
	}

	public String getParamValueChar() {
		return this.paramValueChar;
	}

	public void setParamValueChar(String paramValueChar) {
		this.paramValueChar = paramValueChar;
	}

}