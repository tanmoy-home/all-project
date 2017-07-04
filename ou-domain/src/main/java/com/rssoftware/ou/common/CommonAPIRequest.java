package com.rssoftware.ou.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonInclude;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonAPIRequest implements Serializable {
	
	@XmlElement(nillable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String agentId;

	@XmlElement(nillable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String txnId;

	@XmlElement(nillable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String mobNo;
	
	@XmlElement(nillable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String agentInstId;

	@XmlElement(nillable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String schemeId;
	
	@XmlElement(nillable = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String rejectComment;
	
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public String getAgentInstId() {
		return agentInstId;
	}

	public void setAgentInstId(String agentInstId) {
		this.agentInstId = agentInstId;
	}

	private static final long serialVersionUID = -3731979045008402895L;

	

	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public String getRejectComment() {
		return rejectComment;
	}

	public void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}
}