package com.rssoftware.ou.database.entity.tenant;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "SMS_CONFIG")
@NamedQuery(name = "SMSConfig.findAll", query = "SELECT a FROM SMSConfig a")
public class SMSConfig {
	
	@Id
	@Column(name = "CONFIG_ID")
	private long configId;

	@Column(name = "TENANT_ID")
	private String tenantId;

	@Column(name = "SEND_TYPE")
	private String sendType;

	@Column(name = "BASE_URL")
	private String baseUrl;
	
	@Column(name = "is_acl_tbl_dflt")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isAclDefault;

	@Column(name = "REQUEST_TYPE")
	private String requestType;

	@Column(name = "PARAM1")
	private String parma1 ;

	@Column(name = "PARAM2")
	private String parma2 ;

	@Column(name = "PARAM3")
	private String parma3 ;

	@Column(name = "PARAM4")
	private String parma4 ;

	@Column(name = "PARAM5")
	private String parma5 ;
	
	@Column(name = "PARAM6")
	private String parma6 ;

	@Column(name = "PARAM7")
	private String parma7 ;

	@Column(name = "PARAM8")
	private String parma8 ;

	@Column(name = "PARAM9")
	private String parma9 ;

	@Column(name = "PARAM10")
	private String parma10 ;
	
	@Column(name = "TBL_NAME")
	private String tableName ;

	

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isAclDefault() {
		return isAclDefault;
	}

	public void setAclDefault(boolean isAclDefault) {
		this.isAclDefault = isAclDefault;
	}

	public long getConfigId() {
		return configId;
	}

	public void setConfigId(long configId) {
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

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getParma1() {
		return parma1;
	}

	public void setParma1(String parma1) {
		this.parma1 = parma1;
	}

	public String getParma2() {
		return parma2;
	}

	public void setParma2(String parma2) {
		this.parma2 = parma2;
	}

	public String getParma3() {
		return parma3;
	}

	public void setParma3(String parma3) {
		this.parma3 = parma3;
	}

	public String getParma4() {
		return parma4;
	}

	public void setParma4(String parma4) {
		this.parma4 = parma4;
	}

	public String getParma5() {
		return parma5;
	}

	public void setParma5(String parma5) {
		this.parma5 = parma5;
	}

	public String getParma6() {
		return parma6;
	}

	public void setParma6(String parma6) {
		this.parma6 = parma6;
	}

	public String getParma7() {
		return parma7;
	}

	public void setParma7(String parma7) {
		this.parma7 = parma7;
	}

	public String getParma8() {
		return parma8;
	}

	public void setParma8(String parma8) {
		this.parma8 = parma8;
	}

	public String getParma9() {
		return parma9;
	}

	public void setParma9(String parma9) {
		this.parma9 = parma9;
	}

	public String getParma10() {
		return parma10;
	}

	public void setParma10(String parma10) {
		this.parma10 = parma10;
	}
	
	
	

}
