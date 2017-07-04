package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SchemeAvlblityDetailPK implements Serializable {

	private static final long serialVersionUID = 5008768948231420639L;

	@Column(name = "SCHEME_UNIQUE_ID")
	private String schemeUniqueId;

	@Column(name = "AVAILABLE_TO_AI_ID")
	private String agentInstId;

	public String getSchemeUniqueId() {
		return schemeUniqueId;
	}

	public void setSchemeUniqueId(String schemeUniqueId) {
		this.schemeUniqueId = schemeUniqueId;
	}

	public String getAgentInstId() {
		return agentInstId;
	}

	public void setAgentInstId(String agentInstId) {
		this.agentInstId = agentInstId;
	}

}
