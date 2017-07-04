package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the BILLER_DETAILS database table.
 * 
 */
@Entity
@Table(name = "BILLER_DETAILS")
@NamedQuery(name = "BillerDetail.findAll", query = "SELECT b FROM BillerDetail b")
public class BillerDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BLR_ID")
	private String blrId;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "BLR_ACCEPTS_ADHOC", nullable = false)
	private Boolean blrAcceptsAdhoc;

	
	@Column(name = "BLR_ADDITIONAL_INFO")
	private String blrAdditionalInfo;

	@Column(name = "BLR_ALIAS_NAME")
	private String blrAliasName;

	@Column(name = "BLR_AVG_TICKET_SIZE")
	private String blrAvgTicketSize;

	@Column(name = "BLR_CATEGORY_NAME")
	private String blrCategoryName;

	@Column(name = "BLR_COMMUMICATION_ADRLINE")
	private String blrCommumicationAdrline;

	@Column(name = "BLR_COMMUMICATION_CITY")
	private String blrCommumicationCity;

	@Column(name = "BLR_COMMUMICATION_COUNTRY")
	private String blrCommumicationCountry;

	@Column(name = "BLR_COMMUMICATION_PIN_CODE")
	private String blrCommumicationPinCode;

	@Column(name = "BLR_COMMUMICATION_STATE")
	private String blrCommumicationState;

	@Column(name = "BLR_COVERAGE")
	private String blrCoverage;

	
	@Column(name = "BLR_CUSTOMER_PARAMS")
	private String blrCustomerParams;

	@Column(name = "BLR_EFFCTV_FROM")
	private String blrEffctvFrom;

	@Column(name = "BLR_EFFCTV_TO")
	private String blrEffctvTo;

	@Column(name = "BLR_LINKED_OU_BACKUP_1")
	private String blrLinkedOuBackup1;

	@Column(name = "BLR_LINKED_OU_BACKUP_2")
	private String blrLinkedOuBackup2;

	@Column(name = "BLR_LINKED_OU_BACKUP_3")
	private String blrLinkedOuBackup3;

	@Column(name = "BLR_LINKED_OU_DEFAULT")
	private String blrLinkedOuDefault;

	@Column(name = "BLR_MODE")
	private String blrMode;

	@Column(name = "BLR_NAME")
	private String blrName;

	@Column(name = "BLR_OWNERSHIP")
	private String blrOwnership;

	 
	@Column(name = "BLR_PAYMENT_CHANNELS")
	private String blrPaymentChannels;

	
	@Column(name = "BLR_PAYMENT_MODES")
	private String blrPaymentModes;

	@Column(name = "BLR_REGISTERED_ADRLINE")
	private String blrRegisteredAdrline;

	@Column(name = "BLR_REGISTERED_CITY")
	private String blrRegisteredCity;

	@Column(name = "BLR_REGISTERED_COUNTRY")
	private String blrRegisteredCountry;

	@Column(name = "BLR_REGISTERED_PIN_CODE")
	private String blrRegisteredPinCode;

	@Column(name = "BLR_REGISTERED_STATE")
	private String blrRegisteredState;

	
	@Column(name = "BLR_RESPONSE_PARAMS")
	private String blrResponseParams;

	@Column(name = "BLR_ROC_UIN")
	private String blrRocUin;

	@Column(name = "BLR_TAN")
	private String blrTan;

	@Column(name = "BLR_UAADHAAR")
	private String blrUaadhaar;

	@Column(name = "BLR_VOLUME_PER_DAY")
	private String blrVolumePerDay;

	@Column(name = "CRTN_TS")
	private Timestamp crtnTs;

	@Column(name = "CRTN_USER_ID")
	private String crtnUserId;

	@Column(name = "ENTITY_STATUS")
	private String entityStatus;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "IS_PARENT_BLR", nullable = false)
	private Boolean isParentBlr;

	@Column(name = "PARENT_BLR_ID")
	private String parentBlrId;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "UPDT_USER_ID")
	private String updtUserId;
	
	@Column(name = "ENDPOINT_URL")
	private String endpointURL;

	@Column(name="FETCH_REQUIREMENT")
	private String fetchRequirement;
	
	@Column(name="BLR_PMT_AMT_EXACTNESS")
	private String billerPaymentExactness;
	
	public BillerDetail() {
	}
	
	public String getBillerPaymentExactness() {
		return billerPaymentExactness;
	}
	public void setBillerPaymentExactness(String billerPaymentExactness) {
		this.billerPaymentExactness = billerPaymentExactness;
	}

	public String getBlrId() {
		return this.blrId;
	}

	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}

	public Boolean getBlrAcceptsAdhoc() {
		return this.blrAcceptsAdhoc;
	}

	public void setBlrAcceptsAdhoc(Boolean blrAcceptsAdhoc) {
		this.blrAcceptsAdhoc = blrAcceptsAdhoc;
	}

	public String getBlrAdditionalInfo() {
		return this.blrAdditionalInfo;
	}

	public void setBlrAdditionalInfo(String blrAdditionalInfo) {
		this.blrAdditionalInfo = blrAdditionalInfo;
	}

	public String getBlrAliasName() {
		return this.blrAliasName;
	}

	public void setBlrAliasName(String blrAliasName) {
		this.blrAliasName = blrAliasName;
	}

	public String getBlrAvgTicketSize() {
		return this.blrAvgTicketSize;
	}

	public void setBlrAvgTicketSize(String blrAvgTicketSize) {
		this.blrAvgTicketSize = blrAvgTicketSize;
	}

	public String getBlrCategoryName() {
		return this.blrCategoryName;
	}

	public void setBlrCategoryName(String blrCategoryName) {
		this.blrCategoryName = blrCategoryName;
	}

	public String getBlrCommumicationAdrline() {
		return this.blrCommumicationAdrline;
	}

	public void setBlrCommumicationAdrline(String blrCommumicationAdrline) {
		this.blrCommumicationAdrline = blrCommumicationAdrline;
	}

	public String getBlrCommumicationCity() {
		return this.blrCommumicationCity;
	}

	public void setBlrCommumicationCity(String blrCommumicationCity) {
		this.blrCommumicationCity = blrCommumicationCity;
	}

	public String getBlrCommumicationCountry() {
		return this.blrCommumicationCountry;
	}

	public void setBlrCommumicationCountry(String blrCommumicationCountry) {
		this.blrCommumicationCountry = blrCommumicationCountry;
	}

	public String getBlrCommumicationPinCode() {
		return this.blrCommumicationPinCode;
	}

	public void setBlrCommumicationPinCode(String blrCommumicationPinCode) {
		this.blrCommumicationPinCode = blrCommumicationPinCode;
	}

	public String getBlrCommumicationState() {
		return this.blrCommumicationState;
	}

	public void setBlrCommumicationState(String blrCommumicationState) {
		this.blrCommumicationState = blrCommumicationState;
	}

	public String getBlrCoverage() {
		return this.blrCoverage;
	}

	public void setBlrCoverage(String blrCoverage) {
		this.blrCoverage = blrCoverage;
	}

	public String getBlrCustomerParams() {
		return this.blrCustomerParams;
	}

	public void setBlrCustomerParams(String blrCustomerParams) {
		this.blrCustomerParams = blrCustomerParams;
	}

	public String getBlrEffctvFrom() {
		return this.blrEffctvFrom;
	}

	public void setBlrEffctvFrom(String blrEffctvFrom) {
		this.blrEffctvFrom = blrEffctvFrom;
	}

	public String getBlrEffctvTo() {
		return this.blrEffctvTo;
	}

	public void setBlrEffctvTo(String blrEffctvTo) {
		this.blrEffctvTo = blrEffctvTo;
	}

	public String getBlrLinkedOuBackup1() {
		return this.blrLinkedOuBackup1;
	}

	public void setBlrLinkedOuBackup1(String blrLinkedOuBackup1) {
		this.blrLinkedOuBackup1 = blrLinkedOuBackup1;
	}

	public String getBlrLinkedOuBackup2() {
		return this.blrLinkedOuBackup2;
	}

	public void setBlrLinkedOuBackup2(String blrLinkedOuBackup2) {
		this.blrLinkedOuBackup2 = blrLinkedOuBackup2;
	}

	public String getBlrLinkedOuBackup3() {
		return this.blrLinkedOuBackup3;
	}

	public void setBlrLinkedOuBackup3(String blrLinkedOuBackup3) {
		this.blrLinkedOuBackup3 = blrLinkedOuBackup3;
	}

	public String getBlrLinkedOuDefault() {
		return this.blrLinkedOuDefault;
	}

	public void setBlrLinkedOuDefault(String blrLinkedOuDefault) {
		this.blrLinkedOuDefault = blrLinkedOuDefault;
	}

	public String getBlrMode() {
		return this.blrMode;
	}

	public void setBlrMode(String blrMode) {
		this.blrMode = blrMode;
	}

	public String getBlrName() {
		return this.blrName;
	}

	public void setBlrName(String blrName) {
		this.blrName = blrName;
	}

	public String getBlrOwnership() {
		return this.blrOwnership;
	}

	public void setBlrOwnership(String blrOwnership) {
		this.blrOwnership = blrOwnership;
	}

	public String getBlrPaymentChannels() {
		return this.blrPaymentChannels;
	}

	public void setBlrPaymentChannels(String blrPaymentChannels) {
		this.blrPaymentChannels = blrPaymentChannels;
	}

	public String getBlrPaymentModes() {
		return this.blrPaymentModes;
	}

	public void setBlrPaymentModes(String blrPaymentModes) {
		this.blrPaymentModes = blrPaymentModes;
	}

	public String getBlrRegisteredAdrline() {
		return this.blrRegisteredAdrline;
	}

	public void setBlrRegisteredAdrline(String blrRegisteredAdrline) {
		this.blrRegisteredAdrline = blrRegisteredAdrline;
	}

	public String getBlrRegisteredCity() {
		return this.blrRegisteredCity;
	}

	public void setBlrRegisteredCity(String blrRegisteredCity) {
		this.blrRegisteredCity = blrRegisteredCity;
	}

	public String getBlrRegisteredCountry() {
		return this.blrRegisteredCountry;
	}

	public void setBlrRegisteredCountry(String blrRegisteredCountry) {
		this.blrRegisteredCountry = blrRegisteredCountry;
	}

	public String getBlrRegisteredPinCode() {
		return this.blrRegisteredPinCode;
	}

	public void setBlrRegisteredPinCode(String blrRegisteredPinCode) {
		this.blrRegisteredPinCode = blrRegisteredPinCode;
	}

	public String getBlrRegisteredState() {
		return this.blrRegisteredState;
	}

	public void setBlrRegisteredState(String blrRegisteredState) {
		this.blrRegisteredState = blrRegisteredState;
	}

	public String getBlrResponseParams() {
		return this.blrResponseParams;
	}

	public void setBlrResponseParams(String blrResponseParams) {
		this.blrResponseParams = blrResponseParams;
	}

	public String getBlrRocUin() {
		return this.blrRocUin;
	}

	public void setBlrRocUin(String blrRocUin) {
		this.blrRocUin = blrRocUin;
	}

	public String getBlrTan() {
		return this.blrTan;
	}

	public void setBlrTan(String blrTan) {
		this.blrTan = blrTan;
	}

	public String getBlrUaadhaar() {
		return this.blrUaadhaar;
	}

	public void setBlrUaadhaar(String blrUaadhaar) {
		this.blrUaadhaar = blrUaadhaar;
	}

	public String getBlrVolumePerDay() {
		return this.blrVolumePerDay;
	}

	public void setBlrVolumePerDay(String blrVolumePerDay) {
		this.blrVolumePerDay = blrVolumePerDay;
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

	public Boolean getIsParentBlr() {
		return this.isParentBlr;
	}

	public void setIsParentBlr(Boolean isParentBlr) {
		this.isParentBlr = isParentBlr;
	}

	public String getParentBlrId() {
		return this.parentBlrId;
	}

	public void setParentBlrId(String parentBlrId) {
		this.parentBlrId = parentBlrId;
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

	public String getEndpointURL() {
		return endpointURL;
	}

	public void setEndpointURL(String endpointURL) {
		this.endpointURL = endpointURL;
	}

	public String getFetchRequirement() {
		return fetchRequirement;
	}

	public void setFetchRequirement(String fetchRequirement) {
		this.fetchRequirement = fetchRequirement;
	}	

}