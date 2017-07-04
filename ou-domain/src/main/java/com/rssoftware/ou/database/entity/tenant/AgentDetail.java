package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the AGENT_DETAILS database table.
 * 
 */
@Entity
@Table(name = "AGENT_DETAILS")
@NamedQuery(name = "AgentDetail.findAll", query = "SELECT a FROM AgentDetail a")
public class AgentDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AGENT_ID")
	private String agentId;

	@Column(name = "AGENT_ALIAS_NAME")
	private String agentAliasName;

	@Column(name = "AGENT_BUSNS_TYPE")
	private String agentBusnsType;

	@Column(name = "AGENT_EFFCTV_FROM")
	private String agentEffctvFrom;

	@Column(name = "AGENT_EFFCTV_TO")
	private String agentEffctvTo;

	@Column(name = "AGENT_GEO_CODE")
	private String agentGeoCode;

	@Column(name = "AGENT_LINKED_AGENT_INST")
	private String agentLinkedAgentInst;

	@Column(name = "AGENT_MOBILE_NO")
	private String agentMobileNo;

	@Column(name = "AGENT_NAME")
	private String agentName;

	@Column(name = "AGENT_PAYMENT_CHANNELS")
	private String agentPaymentChannels;

	@Column(name = "AGENT_PAYMENT_MODES")
	private String agentPaymentModes;

	@Column(name = "AGENT_REGISTERED_ADRLINE")
	private String agentRegisteredAdrline;

	@Column(name = "AGENT_REGISTERED_CITY")
	private String agentRegisteredCity;

	@Column(name = "AGENT_REGISTERED_COUNTRY")
	private String agentRegisteredCountry;

	@Column(name = "AGENT_REGISTERED_PIN_CODE")
	private String agentRegisteredPinCode;

	@Column(name = "AGENT_REGISTERED_STATE")
	private String agentRegisteredState;

	@Column(name = "AGENT_SHOP_NAME")
	private String agentShopName;

	@Column(name = "CRTN_TS")
	private Timestamp crtnTs;

	@Column(name = "CRTN_USER_ID")
	private String crtnUserId;

	@Column(name = "ENTITY_STATUS")
	private String entityStatus;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "UPDT_USER_ID")
	private String updtUserId;

	@Column(name = "AGENT_DUMMY")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean dummyAgent;

	@Column(name = "AGENT_BANK_ACCOUNT")
	private String agentBankAccount;

	@Column(name = "AGENT_SCHEME_ID")
	private String agentSchemeId;
	
	@Column(name = "AGENT_TYPE")
	private String agentType;
	
	@Column(name = "IS_UPLOAD")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isUpload;
	
	@Column(name = "AGENT_CU_ID")
	private String agentCUID;
	
	@Column(name = "REJECT_COMMENT")
	private String rejectComment;

	public AgentDetail() {
	}

	public String getAgentBankAccount() {
		return agentBankAccount;
	}

	public void setAgentBankAccount(String agentBankAccount) {
		this.agentBankAccount = agentBankAccount;
	}

	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentAliasName() {
		return this.agentAliasName;
	}

	public void setAgentAliasName(String agentAliasName) {
		this.agentAliasName = agentAliasName;
	}

	public String getAgentBusnsType() {
		return this.agentBusnsType;
	}

	public void setAgentBusnsType(String agentBusnsType) {
		this.agentBusnsType = agentBusnsType;
	}

	public String getAgentEffctvFrom() {
		return this.agentEffctvFrom;
	}

	public void setAgentEffctvFrom(String agentEffctvFrom) {
		this.agentEffctvFrom = agentEffctvFrom;
	}

	public String getAgentEffctvTo() {
		return this.agentEffctvTo;
	}

	public void setAgentEffctvTo(String agentEffctvTo) {
		this.agentEffctvTo = agentEffctvTo;
	}

	public String getAgentGeoCode() {
		return this.agentGeoCode;
	}

	public void setAgentGeoCode(String agentGeoCode) {
		this.agentGeoCode = agentGeoCode;
	}

	public String getAgentLinkedAgentInst() {
		return this.agentLinkedAgentInst;
	}

	public void setAgentLinkedAgentInst(String agentLinkedAgentInst) {
		this.agentLinkedAgentInst = agentLinkedAgentInst;
	}

	public String getAgentMobileNo() {
		return this.agentMobileNo;
	}

	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}

	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentPaymentModes() {
		return this.agentPaymentModes;
	}

	public void setAgentPaymentModes(String agentPaymentModes) {
		this.agentPaymentModes = agentPaymentModes;
	}

	public String getAgentRegisteredAdrline() {
		return this.agentRegisteredAdrline;
	}

	public void setAgentRegisteredAdrline(String agentRegisteredAdrline) {
		this.agentRegisteredAdrline = agentRegisteredAdrline;
	}

	public String getAgentRegisteredCity() {
		return this.agentRegisteredCity;
	}

	public void setAgentRegisteredCity(String agentRegisteredCity) {
		this.agentRegisteredCity = agentRegisteredCity;
	}

	public String getAgentRegisteredCountry() {
		return this.agentRegisteredCountry;
	}

	public void setAgentRegisteredCountry(String agentRegisteredCountry) {
		this.agentRegisteredCountry = agentRegisteredCountry;
	}

	public String getAgentRegisteredPinCode() {
		return this.agentRegisteredPinCode;
	}

	public void setAgentRegisteredPinCode(String agentRegisteredPinCode) {
		this.agentRegisteredPinCode = agentRegisteredPinCode;
	}

	public String getAgentRegisteredState() {
		return this.agentRegisteredState;
	}

	public void setAgentRegisteredState(String agentRegisteredState) {
		this.agentRegisteredState = agentRegisteredState;
	}

	public String getAgentShopName() {
		return this.agentShopName;
	}

	public void setAgentShopName(String agentShopName) {
		this.agentShopName = agentShopName;
	}

	public Timestamp getCrtnTs() {
		return this.crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCrtnUserId() {
		return this.crtnUserId;
	}

	public void setCrtnUserId(String crtnUserId) {
		this.crtnUserId = crtnUserId;
	}

	public String getEntityStatus() {
		return this.entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public Timestamp getUpdtTs() {
		return this.updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getUpdtUserId() {
		return this.updtUserId;
	}

	public void setUpdtUserId(String updtUserId) {
		this.updtUserId = updtUserId;
	}

	public boolean isDummyAgent() {
		return dummyAgent;
	}

	public void setDummyAgent(boolean dummyAgent) {
		this.dummyAgent = dummyAgent;
	}

	public String getAgentPaymentChannels() {
		return agentPaymentChannels;
	}

	public void setAgentPaymentChannels(String agentPaymentChannels) {
		this.agentPaymentChannels = agentPaymentChannels;
	}

	public String getAgentSchemeId() {
		return agentSchemeId;
	}

	public void setAgentSchemeId(String agentSchemeId) {
		this.agentSchemeId = agentSchemeId;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public Boolean getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(Boolean isUpload) {
		this.isUpload = isUpload;
	}

	public String getAgentCUID() {
		return agentCUID;
	}

	public void setAgentCUID(String agentCUID) {
		this.agentCUID = agentCUID;
	}

	public String getRejectComment() {
		return rejectComment;
	}

	public void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}

}