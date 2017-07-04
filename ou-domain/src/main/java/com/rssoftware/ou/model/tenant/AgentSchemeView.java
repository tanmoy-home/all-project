package com.rssoftware.ou.model.tenant;

import java.io.Serializable;
import java.util.List;

public class AgentSchemeView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -198722064760281678L;

	private String schemeUniqueId;

	private String schemeId;

	private String schemeName;

	private String schemeDesc;

	private List<String> agentInstIds;

	private String schemeEffctvFrom;

	private String schemeEffctvTo;

	private boolean schemeEffctvForAll;

	private List<SchemeCommission> schemeCommissions;


	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getSchemeDesc() {
		return schemeDesc;
	}

	public void setSchemeDesc(String schemeDesc) {
		this.schemeDesc = schemeDesc;
	}

	public List<String> getAgentInstIds() {
		return agentInstIds;
	}

	public void setAgentInstIds(List<String> agentInstIds) {
		this.agentInstIds = agentInstIds;
	}

	public String getSchemeEffctvFrom() {
		return schemeEffctvFrom;
	}

	public void setSchemeEffctvFrom(String schemeEffctvFrom) {
		this.schemeEffctvFrom = schemeEffctvFrom;
	}

	public String getSchemeEffctvTo() {
		return schemeEffctvTo;
	}

	public void setSchemeEffctvTo(String schemeEffctvTo) {
		this.schemeEffctvTo = schemeEffctvTo;
	}

	public List<SchemeCommission> getSchemeCommissions() {
		return schemeCommissions;
	}

	public void setSchemeCommissions(List<SchemeCommission> schemeCommissions) {
		this.schemeCommissions = schemeCommissions;
	}

	public boolean isSchemeEffctvForAll() {
		return schemeEffctvForAll;
	}

	public void setSchemeEffctvForAll(boolean schemeEffctvForAll) {
		this.schemeEffctvForAll = schemeEffctvForAll;
	}

	public String getSchemeUniqueId() {
		return schemeUniqueId;
	}

	public void setSchemeUniqueId(String schemeUniqueId) {
		this.schemeUniqueId = schemeUniqueId;
	}

}