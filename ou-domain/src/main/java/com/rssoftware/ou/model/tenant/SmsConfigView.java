package com.rssoftware.ou.model.tenant;

import java.util.ArrayList;
import java.util.List;

public class SmsConfigView {
	
	

	private String configId;
	
	private String tenantId;
	
	private String sendType;

	private String baseUrl;

	private List<String> paramlist= new ArrayList<>();
	
	private String tableName;
	
	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public List<String> getParamlist() {
		return paramlist;
	}

	public void setParamlist(List<String> paramlist) {
		this.paramlist = paramlist;
	}
	
	

}
