package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ApplicationConfigPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3007207594571249476L;

	@Column(name="CONFIG_PARAM")
	private String configParam;
	
	@Column(name="CONFIG_PARAM_GROUP")
	private String configParamGroup;
	
	public ApplicationConfigPK() {		
	}
	
	public String getConfigParam() {
		return this.configParam;
	}

	public void setConfigParam(String configParam) {
		this.configParam = configParam;
	}	

	public String getConfigParamGroup() {
		return configParamGroup;
	}

	public void setConfigParamGroup(String configParamGroup) {
		this.configParamGroup = configParamGroup;
	}
}