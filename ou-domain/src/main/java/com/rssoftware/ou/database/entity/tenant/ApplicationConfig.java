package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the APPLICATION_CONFIG database table.
 * 
 */
@Entity
@Table(name="APPLICATION_CONFIG")
@NamedQuery(name="ApplicationConfig.findAll", query="SELECT a FROM ApplicationConfig a")
public class ApplicationConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ApplicationConfigPK applicationConfigPK;
	
	@Column(name="CONFIG_PARAM_VALUE")
	private String configParamValue;
	
	@Column(name="CONFIG_PARAM_DESC")
	private String configParamDesc;

	public ApplicationConfig() {
	}

	public String getConfigParamDesc() {
		return this.configParamDesc;
	}

	public void setConfigParamDesc(String configParamDesc) {
		this.configParamDesc = configParamDesc;
	}

	public String getConfigParamValue() {
		return this.configParamValue;
	}

	public void setConfigParamValue(String configParamValue) {
		this.configParamValue = configParamValue;
	}	
	
	public String getConfigParam() {
		return this.applicationConfigPK.getConfigParam();
	}
	
	public String getConfigParamGroup() {
		return this.applicationConfigPK.getConfigParamGroup();
	}
}