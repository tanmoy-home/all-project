package com.rssoftware.ou.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PGParam {
	private String paramName;
	private String paramValue;
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
}
