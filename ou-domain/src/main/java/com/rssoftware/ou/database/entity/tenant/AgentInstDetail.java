package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.rssoftware.ou.model.tenant.ContactDetailsView;

/**
 * The persistent class for the AGENT_INST_DETAILS database table.
 * 
 */
@Entity
@Table(name = "AGENT_INST_DETAILS")
@NamedQuery(name = "AgentInstDetail.findAll", query = "SELECT a FROM AgentInstDetail a")
public class AgentInstDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "AGENT_INST_ID")
	private String agentInstId;

	@Column(name = "AGENT_INST_TYPE")
	private String agentInstType;

	@Column(name = "AGENT_INST_BUSNS_TYPE")
	private String agentInstBusnsType;

	@Column(name = "AGENT_INST_NAME")
	private String agentInstName;

	@Column(name = "AGENT_INST_ALIAS_NAME")
	private String agentInstAliasName;

	@Column(name = "AGENT_INST_TAN_NO")
	private String agentInstTanNo;
	
	@Column(name = "AGENT_INST_REG_ADRLINE")
	private String agentInstRegAdrline;
	
	@Column(name = "AGENT_INST_REG_CITY")
	private String agentInstRegCity;
	
	@Column(name = "AGENT_INST_REG_PIN_CODE")
	private String agentInstRegPinCode;

	@Column(name = "AGENT_INST_REG_STATE")
	private String agentInstRegState;
	
	@Column(name = "AGENT_INST_REG_COUNTRY")
	private String agentInstRegCountry;
	
	@Column(name = "AGENT_INST_COMM_ADRLINE")
	private String agentInstCommAdrline;

	@Column(name = "AGENT_INST_COMM_CITY")
	private String agentInstCommCity;

	@Column(name = "AGENT_INST_COMM_PIN_CODE")
	private String agentInstCommPinCode;
	
	@Column(name = "AGENT_INST_COMM_STATE")
	private String agentInstCommState;
	
	@Column(name = "AGENT_INST_COMM_COUNTRY")
	private String agentInstCommCountry;

	@Column(name = "AGENT_INST_EFFCTV_FROM")
	private String agentInstEffctvFrom;

	@Column(name = "AGENT_INST_EFFCTV_TO")
	private String agentInstEffctvTo;

	@Column(name = "AGENT_INST_UAADHAAR")
	private String agentInstUaadhaar;

	@Column(name = "AGENT_INST_ROC_UIN")
	private String agentInstRocUin;

	
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

	@Column(name = "BUS_ADR_AUTH_NUM")
	private String busAddrAuthLetter;

	@Column(name = "BUS_ADR_AUTH_DOC")
	private byte[] busAddrAuthLetterScanCopy;

	@Column(name = "LICENSE_TO_BUSINESS_NUM")
	private String licenseToBusiness;

	@Column(name = "LICENSE_TO_BUSINESS_DOC")
	private byte[] licenseToBusinessScanCopy;

	@Column(name = "RESIDENCE_ADDRESS_NUM")
	private String residentialAddress;

	@Column(name = "RESIDENCE_ADDRESS_DOC")
	private byte[] residentialAddressScanCopy;

	@Column(name = "AADHAR_CARD_NUM")
	private String aadhaarCard;

	@Column(name = "AADHAR_CARD_DOC")
	private byte[] aadhaarCardScanCopy;

	@Column(name = "VOTER_CARD_NUM")
	private String voterIdCard;

	@Column(name = "VOTER_CARD_DOC")
	private byte[] voterIdCardScanCopy;

	@Column(name = "PASSPORT_NUM")
	private String passport;

	@Column(name = "PASSPORT_DOC")
	private byte[] passportScanCopy;

/*	@Column(name = "CONTACT_DETAILS_L1")
	private ContactDetailsView contactDetailsView1stLevel;

	@Column(name = "CONTACT_DETAILS_L2")
	private ContactDetailsView contactDetailsView2ndtLevel;*/

	@Column(name = "AGENT_INST_ACCOUNT_NO")
	private String agentInstAccountNo;
	
	
	
	public AgentInstDetail() {
	}

	public String getAgentInstId() {
		return this.agentInstId;
	}

	public void setAgentInstId(String agentInstId) {
		this.agentInstId = agentInstId;
	}

	public String getAgentInstAliasName() {
		return this.agentInstAliasName;
	}

	public void setAgentInstAliasName(String agentInstAliasName) {
		this.agentInstAliasName = agentInstAliasName;
	}

	public String getAgentInstBusnsType() {
		return this.agentInstBusnsType;
	}

	public void setAgentInstBusnsType(String agentInstBusnsType) {
		this.agentInstBusnsType = agentInstBusnsType;
	}

	public String getAgentInstCommAdrline() {
		return agentInstCommAdrline;
	}

	public void setAgentInstCommAdrline(String agentInstCommAdrline) {
		this.agentInstCommAdrline = agentInstCommAdrline;
	}

	public String getAgentInstRegAdrline() {
		return agentInstRegAdrline;
	}

	public void setAgentInstRegAdrline(String agentInstRegAdrline) {
		this.agentInstRegAdrline = agentInstRegAdrline;
	}

	public String getAgentInstCommCity() {
		return this.agentInstCommCity;
	}

	public void setAgentInstCommCity(String agentInstCommCity) {
		this.agentInstCommCity = agentInstCommCity;
	}

	public String getAgentInstCommCountry() {
		return this.agentInstCommCountry;
	}

	public void setAgentInstCommCountry(String agentInstCommCountry) {
		this.agentInstCommCountry = agentInstCommCountry;
	}

	public String getAgentInstCommPinCode() {
		return this.agentInstCommPinCode;
	}

	public void setAgentInstCommPinCode(String agentInstCommPinCode) {
		this.agentInstCommPinCode = agentInstCommPinCode;
	}

	public String getAgentInstCommState() {
		return this.agentInstCommState;
	}

	public void setAgentInstCommState(String agentInstCommState) {
		this.agentInstCommState = agentInstCommState;
	}

	public String getAgentInstEffctvFrom() {
		return this.agentInstEffctvFrom;
	}

	public void setAgentInstEffctvFrom(String agentInstEffctvFrom) {
		this.agentInstEffctvFrom = agentInstEffctvFrom;
	}

	public String getAgentInstEffctvTo() {
		return this.agentInstEffctvTo;
	}

	public void setAgentInstEffctvTo(String agentInstEffctvTo) {
		this.agentInstEffctvTo = agentInstEffctvTo;
	}

	public String getAgentInstName() {
		return this.agentInstName;
	}

	public void setAgentInstName(String agentInstName) {
		this.agentInstName = agentInstName;
	}

	public String getAgentInstRegCity() {
		return this.agentInstRegCity;
	}

	public void setAgentInstRegCity(String agentInstRegCity) {
		this.agentInstRegCity = agentInstRegCity;
	}

	public String getAgentInstRegCountry() {
		return this.agentInstRegCountry;
	}

	public void setAgentInstRegCountry(String agentInstRegCountry) {
		this.agentInstRegCountry = agentInstRegCountry;
	}

	public String getAgentInstRegPinCode() {
		return this.agentInstRegPinCode;
	}

	public void setAgentInstRegPinCode(String agentInstRegPinCode) {
		this.agentInstRegPinCode = agentInstRegPinCode;
	}

	public String getAgentInstRegState() {
		return this.agentInstRegState;
	}

	public void setAgentInstRegState(String agentInstRegState) {
		this.agentInstRegState = agentInstRegState;
	}

	public String getAgentInstRocUin() {
		return this.agentInstRocUin;
	}

	public void setAgentInstRocUin(String agentInstRocUin) {
		this.agentInstRocUin = agentInstRocUin;
	}

	public String getAgentInstTanNo() {
		return this.agentInstTanNo;
	}

	public void setAgentInstTanNo(String agentInstTanNo) {
		this.agentInstTanNo = agentInstTanNo;
	}

	public String getAgentInstType() {
		return this.agentInstType;
	}

	public void setAgentInstType(String agentInstType) {
		this.agentInstType = agentInstType;
	}

	public String getAgentInstUaadhaar() {
		return this.agentInstUaadhaar;
	}

	public void setAgentInstUaadhaar(String agentInstUaadhaar) {
		this.agentInstUaadhaar = agentInstUaadhaar;
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

	public String getBusAddrAuthLetter() {
		return busAddrAuthLetter;
	}

	public void setBusAddrAuthLetter(String busAddrAuthLetter) {
		this.busAddrAuthLetter = busAddrAuthLetter;
	}

	public byte[] getBusAddrAuthLetterScanCopy() {
		return busAddrAuthLetterScanCopy;
	}

	public void setBusAddrAuthLetterScanCopy(byte[] busAddrAuthLetterScanCopy) {
		this.busAddrAuthLetterScanCopy = busAddrAuthLetterScanCopy;
	}

	public String getLicenseToBusiness() {
		return licenseToBusiness;
	}

	public void setLicenseToBusiness(String licenseToBusiness) {
		this.licenseToBusiness = licenseToBusiness;
	}

	public byte[] getLicenseToBusinessScanCopy() {
		return licenseToBusinessScanCopy;
	}

	public void setLicenseToBusinessScanCopy(byte[] licenseToBusinessScanCopy) {
		this.licenseToBusinessScanCopy = licenseToBusinessScanCopy;
	}

	public String getResidentialAddress() {
		return residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}

	public byte[] getResidentialAddressScanCopy() {
		return residentialAddressScanCopy;
	}

	public void setResidentialAddressScanCopy(byte[] residentialAddressScanCopy) {
		this.residentialAddressScanCopy = residentialAddressScanCopy;
	}

	public String getAadhaarCard() {
		return aadhaarCard;
	}

	public void setAadhaarCard(String aadhaarCard) {
		this.aadhaarCard = aadhaarCard;
	}

	public byte[] getAadhaarCardScanCopy() {
		return aadhaarCardScanCopy;
	}

	public void setAadhaarCardScanCopy(byte[] aadhaarCardScanCopy) {
		this.aadhaarCardScanCopy = aadhaarCardScanCopy;
	}

	public String getVoterIdCard() {
		return voterIdCard;
	}

	public void setVoterIdCard(String voterIdCard) {
		this.voterIdCard = voterIdCard;
	}

	public byte[] getVoterIdCardScanCopy() {
		return voterIdCardScanCopy;
	}

	public void setVoterIdCardScanCopy(byte[] voterIdCardScanCopy) {
		this.voterIdCardScanCopy = voterIdCardScanCopy;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public byte[] getPassportScanCopy() {
		return passportScanCopy;
	}

	public void setPassportScanCopy(byte[] passportScanCopy) {
		this.passportScanCopy = passportScanCopy;
	}

	public String getAgentInstAccountNo() {
		return agentInstAccountNo;
	}

	public void setAgentInstAccountNo(String agentInstAccountNo) {
		this.agentInstAccountNo = agentInstAccountNo;
	}

}