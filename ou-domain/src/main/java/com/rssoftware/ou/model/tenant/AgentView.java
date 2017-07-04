package com.rssoftware.ou.model.tenant;

import java.util.List;

import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.ExcelColumn;
import com.rssoftware.ou.common.ExcelReport;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.domain.ReferenceUser;

@ExcelReport(reportName = "Agent_Details.xlsx")
public class AgentView extends ReferenceUser {

	private String agentId;
	private String agentName;
	private String agentAliasName;
	private String agentShopName;
	private String agentRegisteredAdrline;
	private String agentRegisteredCity;
	private String agentRegisteredCountry;
	private String agentRegisteredPinCode;
	private String agentRegisteredState;
	private String agentMobileNo;
	private List<PaymentModeLimit> agentPaymentModes;
	private List<PaymentChannelLimit> agentPaymentChannels;
	private List<PaymentMode> agentPaymentModeList;
	
	private EntityStatus entityStatus;
	private String businessType;
	private String agentEffctvFrom;
	private String agentEffctvTo;
	private String agentGeoCode;
	private String agentInst;

	private String latitude;
	private String longitude;

	private String agentCUID;
	private String TmpDeactvStartDt;
	private String TmpDeactvEndDt;

	private String isEdit = "N";
	private String isView = "N";

	private byte[] agentAuthLetter1;
	private String agentAuthLetter1Name;
	private byte[] agentAuthLetter2;
	private String agentAuthLetter2Name;
	private byte[] agentAuthLetter3;
	private String agentAuthLetter3Name;

	private String agentBankAccount;
	private String totalAmountCollected;
	private String agentSchemeId;
	private String agentType;
	private Boolean isUpload;
	private String rejectComment;
	private String agentPaymentMode;

	/* private EntityStatus EntityStatus; */

	private boolean dummyAgent;

	private ContactDetailsView contactDetailsView1stLevel;
	private ContactDetailsView contactDetailsView2ndLevel;

	@ExcelColumn(label = "Agent ID")
	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	@ExcelColumn(label = "Agent Name")
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentAliasName() {
		return agentAliasName;
	}

	public void setAgentAliasName(String agentAliasName) {
		this.agentAliasName = agentAliasName;
	}

	public String getAgentShopName() {
		return agentShopName;
	}

	public void setAgentShopName(String agentShopName) {
		this.agentShopName = agentShopName;
	}

	public String getAgentRegisteredAdrline() {
		return agentRegisteredAdrline;
	}

	public void setAgentRegisteredAdrline(String agentRegisteredAdrline) {
		this.agentRegisteredAdrline = agentRegisteredAdrline;
	}

	public String getAgentRegisteredCity() {
		return agentRegisteredCity;
	}

	public void setAgentRegisteredCity(String agentRegisteredCity) {
		this.agentRegisteredCity = agentRegisteredCity;
	}

	public String getAgentRegisteredCountry() {
		return agentRegisteredCountry;
	}

	public void setAgentRegisteredCountry(String agentRegisteredCountry) {
		this.agentRegisteredCountry = agentRegisteredCountry;
	}

	public String getAgentRegisteredPinCode() {
		return agentRegisteredPinCode;
	}

	public void setAgentRegisteredPinCode(String agentRegisteredPinCode) {
		this.agentRegisteredPinCode = agentRegisteredPinCode;
	}

	public String getAgentRegisteredState() {
		return agentRegisteredState;
	}

	public void setAgentRegisteredState(String agentRegisteredState) {
		this.agentRegisteredState = agentRegisteredState;
	}

	@ExcelColumn(label = "Agent Mobile No")
	public String getAgentMobileNo() {
		return agentMobileNo;
	}

	public void setAgentMobileNo(String agentMobileNo) {
		this.agentMobileNo = agentMobileNo;
	}

	public List<PaymentModeLimit> getAgentPaymentModes() {
		return agentPaymentModes;
	}

	public void setAgentPaymentModes(List<PaymentModeLimit> agentPaymentModes) {
		this.agentPaymentModes = agentPaymentModes;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getAgentEffctvFrom() {
		return agentEffctvFrom;
	}

	public void setAgentEffctvFrom(String agentEffctvFrom) {
		this.agentEffctvFrom = agentEffctvFrom;
	}

	public String getAgentEffctvTo() {
		return agentEffctvTo;
	}

	public void setAgentEffctvTo(String agentEffctvTo) {
		this.agentEffctvTo = agentEffctvTo;
	}

	public String getAgentGeoCode() {
		return agentGeoCode;
	}

	public void setAgentGeoCode(String agentGeoCode) {
		this.agentGeoCode = agentGeoCode;
	}

	@ExcelColumn(label = "Agent Institute")
	public String getAgentInst() {
		return agentInst;
	}

	public void setAgentInst(String agentInst) {
		this.agentInst = agentInst;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAgentCUID() {
		return agentCUID;
	}

	public void setAgentCUID(String agentCUID) {
		this.agentCUID = agentCUID;
	}

	public String getTmpDeactvStartDt() {
		return TmpDeactvStartDt;
	}

	public void setTmpDeactvStartDt(String tmpDeactvStartDt) {
		TmpDeactvStartDt = tmpDeactvStartDt;
	}

	public String getTmpDeactvEndDt() {
		return TmpDeactvEndDt;
	}

	public void setTmpDeactvEndDt(String tmpDeactvEndDt) {
		TmpDeactvEndDt = tmpDeactvEndDt;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getIsView() {
		return isView;
	}

	public void setIsView(String isView) {
		this.isView = isView;
	}

	public byte[] getAgentAuthLetter1() {
		return agentAuthLetter1;
	}

	public void setAgentAuthLetter1(byte[] agentAuthLetter1) {
		this.agentAuthLetter1 = agentAuthLetter1;
	}

	public String getAgentAuthLetter1Name() {
		return agentAuthLetter1Name;
	}

	public void setAgentAuthLetter1Name(String agentAuthLetter1Name) {
		this.agentAuthLetter1Name = agentAuthLetter1Name;
	}

	public byte[] getAgentAuthLetter2() {
		return agentAuthLetter2;
	}

	public void setAgentAuthLetter2(byte[] agentAuthLetter2) {
		this.agentAuthLetter2 = agentAuthLetter2;
	}

	public String getAgentAuthLetter2Name() {
		return agentAuthLetter2Name;
	}

	public void setAgentAuthLetter2Name(String agentAuthLetter2Name) {
		this.agentAuthLetter2Name = agentAuthLetter2Name;
	}

	public byte[] getAgentAuthLetter3() {
		return agentAuthLetter3;
	}

	public void setAgentAuthLetter3(byte[] agentAuthLetter3) {
		this.agentAuthLetter3 = agentAuthLetter3;
	}

	public String getAgentAuthLetter3Name() {
		return agentAuthLetter3Name;
	}

	public void setAgentAuthLetter3Name(String agentAuthLetter3Name) {
		this.agentAuthLetter3Name = agentAuthLetter3Name;
	}

	public boolean isDummyAgent() {
		return dummyAgent;
	}

	public void setDummyAgent(boolean dummyAgent) {
		this.dummyAgent = dummyAgent;
	}

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}

	public String getTotalAmountCollected() {
		return totalAmountCollected;
	}

	public void setTotalAmountCollected(String totalAmountCollected) {
		this.totalAmountCollected = totalAmountCollected;
	}

	public ContactDetailsView getContactDetailsView1stLevel() {
		return contactDetailsView1stLevel;
	}

	public void setContactDetailsView1stLevel(ContactDetailsView contactDetailsView1stLevel) {
		this.contactDetailsView1stLevel = contactDetailsView1stLevel;
	}

	public List<PaymentChannelLimit> getAgentPaymentChannels() {
		return agentPaymentChannels;
	}

	public ContactDetailsView getContactDetailsView2ndLevel() {
		return contactDetailsView2ndLevel;
	}

	public void setContactDetailsView2ndLevel(ContactDetailsView contactDetailsView2ndLevel) {
		this.contactDetailsView2ndLevel = contactDetailsView2ndLevel;
	}

	public void setAgentPaymentChannels(List<PaymentChannelLimit> agentPaymentChannels) {
		this.agentPaymentChannels = agentPaymentChannels;
	}

	public List<PaymentMode> getAgentPaymentModeList() {
		return agentPaymentModeList;
	}

	public void setAgentPaymentModeList(List<PaymentMode> agentPaymentModeList) {
		this.agentPaymentModeList = agentPaymentModeList;
	}

	public String getAgentBankAccount() {
		return agentBankAccount;
	}

	public void setAgentBankAccount(String agentBankAccount) {
		this.agentBankAccount = agentBankAccount;
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

	public String getRejectComment() {
		return rejectComment;
	}

	public void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}

	public String getAgentPaymentMode() {
		return agentPaymentMode;
	}

	public void setAgentPaymentMode(String agentPaymentMode) {
		this.agentPaymentMode = agentPaymentMode;
	}


}