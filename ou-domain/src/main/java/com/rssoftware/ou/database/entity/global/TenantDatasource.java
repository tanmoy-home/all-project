package com.rssoftware.ou.database.entity.global;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TENANT_DATASOURCE database table.
 * 
 */
@Entity
@Table(name="TENANT_DATASOURCE")
@NamedQuery(name="TenantDatasource.findAll", query="SELECT t FROM TenantDatasource t")
public class TenantDatasource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TENANT_ID")
	private String tenantId;

	@Column(name="JNDI_NAME")
	private String jndiName;
	
	@Column(name="DRIVER_CLASS_NAME")
	private String driverClassName;

	@Column(name="MAX_ACTIVE")
	private BigDecimal maxActive;

	@Column(name="MAX_IDLE")
	private BigDecimal maxIdle;

	@Column(name="REMOVE_ABANDONED")
	private String removeAbandoned;

	@Column(name="REMOVE_ABANDONED_TIMEOUT")
	private BigDecimal removeAbandonedTimeout;

	@Column(name="TEST_ON_BORROW")
	private String testOnBorrow;

	private String url;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="USER_PASSWORD")
	private String userPassword;

	@Column(name="VALIDATION_QUERY")
	private String validationQuery;

	public TenantDatasource() {
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getDriverClassName() {
		return this.driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public BigDecimal getMaxActive() {
		return this.maxActive;
	}

	public void setMaxActive(BigDecimal maxActive) {
		this.maxActive = maxActive;
	}

	public BigDecimal getMaxIdle() {
		return this.maxIdle;
	}

	public void setMaxIdle(BigDecimal maxIdle) {
		this.maxIdle = maxIdle;
	}

	public String getRemoveAbandoned() {
		return this.removeAbandoned;
	}

	public void setRemoveAbandoned(String removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public BigDecimal getRemoveAbandonedTimeout() {
		return this.removeAbandonedTimeout;
	}

	public void setRemoveAbandonedTimeout(BigDecimal removeAbandonedTimeout) {
		this.removeAbandonedTimeout = removeAbandonedTimeout;
	}

	public String getTestOnBorrow() {
		return this.testOnBorrow;
	}

	public void setTestOnBorrow(String testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getValidationQuery() {
		return this.validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

}