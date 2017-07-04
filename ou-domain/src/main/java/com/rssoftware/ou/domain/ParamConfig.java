package com.rssoftware.ou.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParamConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7851150294320197027L;
	
	public enum DataType {NUMERIC, ALPHANUMERIC};
	
	private String paramName;
	private ParamConfig.DataType dataType;
	private Boolean optional = false;
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public ParamConfig.DataType getDataType() {
		return dataType;
	}
	public void setDataType(ParamConfig.DataType dataType) {
		this.dataType = dataType;
	}
	public Boolean getOptional() {
		return optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	@Override
	public String toString() {
		return "ParamConfig [paramName=" + paramName + ", dataType=" + dataType
				+ ", optional=" + optional + "]";
	}
	
}
