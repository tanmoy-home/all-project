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
@Table(name = "MY_BILLER_DETAILS")
@NamedQuery(name = "MyBillerDetail.findAll", query = "SELECT b FROM MyBillerDetail b")
public class MyBillerDetail implements Serializable {
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

	@Column(name = "STATUS")
	private String entityStatus;

	@Column(name = "PARENT_BLR_ID")
	private String parentBlrId;

	@Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "UPDT_USER_ID")
	private String updtUserId;
	
	@Column(name = "ENDPOINT_URL")
	private String endpointURL;

	@Column(name = "BLR_AUTH_LETTER_1")
	private byte[] billerAuthLetter1;
	
	@Column(name = "BLR_AUTH_LETTER_1_NM")
	private String billerAuthLetter1Name;
	
	@Column(name = "BLR_AUTH_LETTER_2")
	private byte[] billerAuthLetter2;
	
	@Column(name = "BLR_AUTH_LETTER_2_NM")
	private String billerAuthLetter2Name;
	
	@Column(name = "BLR_AUTH_LETTER_3")
	private byte[] billerAuthLetter3;
	
	@Column(name = "BLR_AUTH_LETTER_3_NM")
	private String billerAuthLetter3Name;
	
	@Column(name = "REJECTED_TS")
	private Timestamp rejectedTs;

	@Column(name = "REJECTED_BY_USER_ID")
	private String rejectedUserId;
	
	@Column(name = "DELETED_TS")
	private Timestamp deletedTs;

	@Column(name = "DELETED_BY_USER_ID")
	private String deletedUserId;
	
	@Column(name = "APPROVED_TS")
	private Timestamp approvedTs;

	@Column(name = "APPROVED_BY_USER_ID")
	private String approvedUserId;
	
	@Column(name = "BLR_PMT_AMT_EXACTNESS")
	private String payAmountExactness;
	
	@Column(name = "FETCH_REQUIREMENT")
	private String fetchRequirement;

	public String getBlrId() {
		return blrId;
	}

	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}

	public Boolean getBlrAcceptsAdhoc() {
		return blrAcceptsAdhoc;
	}

	public void setBlrAcceptsAdhoc(Boolean blrAcceptsAdhoc) {
		this.blrAcceptsAdhoc = blrAcceptsAdhoc;
	}

	public String getBlrAdditionalInfo() {
		return blrAdditionalInfo;
	}

	public void setBlrAdditionalInfo(String blrAdditionalInfo) {
		this.blrAdditionalInfo = blrAdditionalInfo;
	}

	public String getBlrAliasName() {
		return blrAliasName;
	}

	public void setBlrAliasName(String blrAliasName) {
		this.blrAliasName = blrAliasName;
	}

	public String getBlrAvgTicketSize() {
		return blrAvgTicketSize;
	}

	public void setBlrAvgTicketSize(String blrAvgTicketSize) {
		this.blrAvgTicketSize = blrAvgTicketSize;
	}

	public String getBlrCategoryName() {
		return blrCategoryName;
	}

	public void setBlrCategoryName(String blrCategoryName) {
		this.blrCategoryName = blrCategoryName;
	}

	public String getBlrCommumicationAdrline() {
		return blrCommumicationAdrline;
	}

	public void setBlrCommumicationAdrline(String blrCommumicationAdrline) {
		this.blrCommumicationAdrline = blrCommumicationAdrline;
	}

	public String getBlrCommumicationCity() {
		return blrCommumicationCity;
	}

	public void setBlrCommumicationCity(String blrCommumicationCity) {
		this.blrCommumicationCity = blrCommumicationCity;
	}

	public String getBlrCommumicationCountry() {
		return blrCommumicationCountry;
	}

	public void setBlrCommumicationCountry(String blrCommumicationCountry) {
		this.blrCommumicationCountry = blrCommumicationCountry;
	}

	public String getBlrCommumicationPinCode() {
		return blrCommumicationPinCode;
	}

	public void setBlrCommumicationPinCode(String blrCommumicationPinCode) {
		this.blrCommumicationPinCode = blrCommumicationPinCode;
	}

	public String getBlrCommumicationState() {
		return blrCommumicationState;
	}

	public void setBlrCommumicationState(String blrCommumicationState) {
		this.blrCommumicationState = blrCommumicationState;
	}

	public String getBlrCoverage() {
		return blrCoverage;
	}

	public void setBlrCoverage(String blrCoverage) {
		this.blrCoverage = blrCoverage;
	}

	public String getBlrCustomerParams() {
		return blrCustomerParams;
	}

	public void setBlrCustomerParams(String blrCustomerParams) {
		this.blrCustomerParams = blrCustomerParams;
	}

	public String getBlrEffctvFrom() {
		return blrEffctvFrom;
	}

	public void setBlrEffctvFrom(String blrEffctvFrom) {
		this.blrEffctvFrom = blrEffctvFrom;
	}

	public String getBlrEffctvTo() {
		return blrEffctvTo;
	}

	public void setBlrEffctvTo(String blrEffctvTo) {
		this.blrEffctvTo = blrEffctvTo;
	}

	public String getBlrLinkedOuDefault() {
		return blrLinkedOuDefault;
	}

	public void setBlrLinkedOuDefault(String blrLinkedOuDefault) {
		this.blrLinkedOuDefault = blrLinkedOuDefault;
	}

	public String getBlrMode() {
		return blrMode;
	}

	public void setBlrMode(String blrMode) {
		this.blrMode = blrMode;
	}

	public String getBlrName() {
		return blrName;
	}

	public void setBlrName(String blrName) {
		this.blrName = blrName;
	}

	public String getBlrOwnership() {
		return blrOwnership;
	}

	public void setBlrOwnership(String blrOwnership) {
		this.blrOwnership = blrOwnership;
	}

	public String getBlrPaymentChannels() {
		return blrPaymentChannels;
	}

	public void setBlrPaymentChannels(String blrPaymentChannels) {
		this.blrPaymentChannels = blrPaymentChannels;
	}

	public String getBlrPaymentModes() {
		return blrPaymentModes;
	}

	public void setBlrPaymentModes(String blrPaymentModes) {
		this.blrPaymentModes = blrPaymentModes;
	}

	public String getBlrRegisteredAdrline() {
		return blrRegisteredAdrline;
	}

	public void setBlrRegisteredAdrline(String blrRegisteredAdrline) {
		this.blrRegisteredAdrline = blrRegisteredAdrline;
	}

	public String getBlrRegisteredCity() {
		return blrRegisteredCity;
	}

	public void setBlrRegisteredCity(String blrRegisteredCity) {
		this.blrRegisteredCity = blrRegisteredCity;
	}

	public String getBlrRegisteredCountry() {
		return blrRegisteredCountry;
	}

	public void setBlrRegisteredCountry(String blrRegisteredCountry) {
		this.blrRegisteredCountry = blrRegisteredCountry;
	}

	public String getBlrRegisteredPinCode() {
		return blrRegisteredPinCode;
	}

	public void setBlrRegisteredPinCode(String blrRegisteredPinCode) {
		this.blrRegisteredPinCode = blrRegisteredPinCode;
	}

	public String getBlrRegisteredState() {
		return blrRegisteredState;
	}

	public void setBlrRegisteredState(String blrRegisteredState) {
		this.blrRegisteredState = blrRegisteredState;
	}

	public String getBlrResponseParams() {
		return blrResponseParams;
	}

	public void setBlrResponseParams(String blrResponseParams) {
		this.blrResponseParams = blrResponseParams;
	}

	public String getBlrRocUin() {
		return blrRocUin;
	}

	public void setBlrRocUin(String blrRocUin) {
		this.blrRocUin = blrRocUin;
	}

	public String getBlrTan() {
		return blrTan;
	}

	public void setBlrTan(String blrTan) {
		this.blrTan = blrTan;
	}

	public String getBlrUaadhaar() {
		return blrUaadhaar;
	}

	public void setBlrUaadhaar(String blrUaadhaar) {
		this.blrUaadhaar = blrUaadhaar;
	}

	public String getBlrVolumePerDay() {
		return blrVolumePerDay;
	}

	public void setBlrVolumePerDay(String blrVolumePerDay) {
		this.blrVolumePerDay = blrVolumePerDay;
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

	public String getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

	public String getParentBlrId() {
		return parentBlrId;
	}

	public void setParentBlrId(String parentBlrId) {
		this.parentBlrId = parentBlrId;
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

	public String getEndpointURL() {
		return endpointURL;
	}

	public void setEndpointURL(String endpointURL) {
		this.endpointURL = endpointURL;
	}

	public byte[] getBillerAuthLetter1() {
		return billerAuthLetter1;
	}

	public void setBillerAuthLetter1(byte[] billerAuthLetter1) {
		this.billerAuthLetter1 = billerAuthLetter1;
	}

	public String getBillerAuthLetter1Name() {
		return billerAuthLetter1Name;
	}

	public void setBillerAuthLetter1Name(String billerAuthLetter1Name) {
		this.billerAuthLetter1Name = billerAuthLetter1Name;
	}

	public byte[] getBillerAuthLetter2() {
		return billerAuthLetter2;
	}

	public void setBillerAuthLetter2(byte[] billerAuthLetter2) {
		this.billerAuthLetter2 = billerAuthLetter2;
	}

	public String getBillerAuthLetter2Name() {
		return billerAuthLetter2Name;
	}

	public void setBillerAuthLetter2Name(String billerAuthLetter2Name) {
		this.billerAuthLetter2Name = billerAuthLetter2Name;
	}

	public byte[] getBillerAuthLetter3() {
		return billerAuthLetter3;
	}

	public void setBillerAuthLetter3(byte[] billerAuthLetter3) {
		this.billerAuthLetter3 = billerAuthLetter3;
	}

	public String getBillerAuthLetter3Name() {
		return billerAuthLetter3Name;
	}

	public void setBillerAuthLetter3Name(String billerAuthLetter3Name) {
		this.billerAuthLetter3Name = billerAuthLetter3Name;
	}

	public Timestamp getRejectedTs() {
		return rejectedTs;
	}

	public void setRejectedTs(Timestamp rejectedTs) {
		this.rejectedTs = rejectedTs;
	}

	public String getRejectedUserId() {
		return rejectedUserId;
	}

	public void setRejectedUserId(String rejectedUserId) {
		this.rejectedUserId = rejectedUserId;
	}

	public Timestamp getDeletedTs() {
		return deletedTs;
	}

	public void setDeletedTs(Timestamp deletedTs) {
		this.deletedTs = deletedTs;
	}

	public String getDeletedUserId() {
		return deletedUserId;
	}

	public void setDeletedUserId(String deletedUserId) {
		this.deletedUserId = deletedUserId;
	}

	public String getPayAmountExactness() {
		return payAmountExactness;
	}

	public void setPayAmountExactness(String payAmountExactness) {
		this.payAmountExactness = payAmountExactness;
	}

	public String getFetchRequirement() {
		return fetchRequirement;
	}

	public void setFetchRequirement(String fetchRequirement) {
		this.fetchRequirement = fetchRequirement;
	}

	public Timestamp getApprovedTs() {
		return approvedTs;
	}

	public void setApprovedTs(Timestamp approvedTs) {
		this.approvedTs = approvedTs;
	}

	public String getApprovedUserId() {
		return approvedUserId;
	}

	public void setApprovedUserId(String approvedUserId) {
		this.approvedUserId = approvedUserId;
	}
	
	}