package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "AGENT_SCHEMES")
@NamedQuery(name = "AgentScheme.findAll", query = "SELECT a FROM AgentScheme a")
public class AgentScheme implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "SCHEME_UNIQUE_ID")
	private String schemeUniqueId;

	@Column(name = "SCHEME_ID")
	private String schemeId;

	@Column(name = "SCHEME_NAME")
	private String schemeName;

	@Column(name = "SCHEME_DESCRIPTION")
	private String schemeDesc;

	@Column(name = "EFFCTV_FROM")
	private String schemeEffctvFrom;

	@Column(name = "EFFCTV_TO")
	private String schemeEffctvTo;

	@Column(name = "EFFCTV_FOR_ALL")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean schemeEffctvForAll;

	@Column(name = "CRTN_TS")
	private Timestamp crtnTs;

	@Column(name = "CRTN_USER_ID")
	private String crtnUserId;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "UPDT_USER_ID")
	private String updtUserId;

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

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCrtnUserId() {
		return crtnUserId;
	}

	public void setCrtnUserId(String crtnUserId) {
		this.crtnUserId = crtnUserId;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getUpdtUserId() {
		return updtUserId;
	}

	public void setUpdtUserId(String updtUserId) {
		this.updtUserId = updtUserId;
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
