package com.rssoftware.ou.model.tenant;

import javax.persistence.Column;

import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.ExcelColumn;
import com.rssoftware.ou.common.ExcelReport;

@ExcelReport(reportName="Agent_Inst_Details.xlsx")

public class AgentInstView {

	private String agentInstId;
	private String agentInstName;
	private String agentInstAliasName;
	private String agentInstCommAdrLine1;
	private String agentInstCommAdrLine2;
	private String agentInstCommAdrLine3;

	

	private String agentInstCommCity;
	private String agentInstCommCountry;
	private String agentInstCommPinCode;
	private String agentInstCommState;
	private EntityStatus entityStatus;
	
	
	private String agentInstEffctvFrom;
	private String agentInstEffctvTo;
	//Registration Info
		private String agentInstRegAdrLine1;
		private String agentInstRegAdrLine2;
		private String agentInstRegAdrLine3;
		private String agentInstRegCity;
		private String agentInstRegPinCode;
		private String agentInstRegState;
		private String agentInstRegCountry = "INDIA";
		private String hiddenStateOnEdit;
		private String hiddenCityOnEdit;
		private String hiddenPinOnEdit;
		private String agentInstType;
		private String agentInstBusnsType;
		private String agentInstTanNo;
		private String isEdit = "N";



		//Uploaded file(s) placeholder variables
		private String busAddrAuthLetter;
		private byte[] busAddrAuthLetterScanCopy;
		
		private String licenseToBusiness;
		private byte[] licenseToBusinessScanCopy;
		
		private String residentialAddress;
		private byte[] residentialAddressScanCopy;
		
		private String aadhaarCard;
		private byte[] aadhaarCardScanCopy;
		
		private String voterIdCard;
		private byte[] voterIdCardScanCopy;
		
		private String passport;
		private byte[] passportScanCopy;
		
		//Section B : Contact Details
		private ContactDetailsView contactDetailsView1stLevel;
		private ContactDetailsView contactDetailsView2ndtLevel;
		private String agentInstUaadhaar;
		private String agentInstRocUin;
		private String agentInstAccountNo;

		/*
		private String updateAgentInstFlag = "N";
		private String isView = "N";
		private String isEdit = "N";
		private String ouState = "NOOU";
		private String sortType = "";*/

	public String getAgentInstRegAdrLine1() {
			return agentInstRegAdrLine1;
		}

		public void setAgentInstRegAdrLine1(String agentInstRegAdrLine1) {
			this.agentInstRegAdrLine1 = agentInstRegAdrLine1;
		}

		public String getAgentInstRegAdrLine2() {
			return agentInstRegAdrLine2;
		}

		public void setAgentInstRegAdrLine2(String agentInstRegAdrLine2) {
			this.agentInstRegAdrLine2 = agentInstRegAdrLine2;
		}

		public String getAgentInstRegAdrLine3() {
			return agentInstRegAdrLine3;
		}

		public void setAgentInstRegAdrLine3(String agentInstRegAdrLine3) {
			this.agentInstRegAdrLine3 = agentInstRegAdrLine3;
		}

		public String getAgentInstRegCity() {
			return agentInstRegCity;
		}

		public void setAgentInstRegCity(String agentInstRegCity) {
			this.agentInstRegCity = agentInstRegCity;
		}

		public String getAgentInstRegPinCode() {
			return agentInstRegPinCode;
		}

		public void setAgentInstRegPinCode(String agentInstRegPinCode) {
			this.agentInstRegPinCode = agentInstRegPinCode;
		}

		public String getAgentInstRegState() {
			return agentInstRegState;
		}

		public void setAgentInstRegState(String agentInstRegState) {
			this.agentInstRegState = agentInstRegState;
		}

		public String getAgentInstRegCountry() {
			return agentInstRegCountry;
		}

		public void setAgentInstRegCountry(String agentInstRegCountry) {
			this.agentInstRegCountry = agentInstRegCountry;
		}
	@ExcelColumn(label="AgentInst ID")
	public String getAgentInstId() {
		return agentInstId;
	}

	public void setAgentInstId(String agentInstId) {
		this.agentInstId = agentInstId;
	}
	@ExcelColumn(label="AgentInst Name")
	public String getAgentInstName() {
		return agentInstName;
	}

	public void setAgentInstName(String agentInstName) {
		this.agentInstName = agentInstName;
	}

	public String getAgentInstAliasName() {
		return agentInstAliasName;
	}

	public void setAgentInstAliasName(String agentInstAliasName) {
		this.agentInstAliasName = agentInstAliasName;
	}

	public String getAgentInstCommAdrLine1() {
		return agentInstCommAdrLine1;
	}

	public void setAgentInstCommAdrLine1(String agentInstCommAdrLine1) {
		this.agentInstCommAdrLine1 = agentInstCommAdrLine1;
	}

	public String getAgentInstCommAdrLine2() {
		return agentInstCommAdrLine2;
	}

	public void setAgentInstCommAdrLine2(String agentInstCommAdrLine2) {
		this.agentInstCommAdrLine2 = agentInstCommAdrLine2;
	}

	public String getAgentInstCommAdrLine3() {
		return agentInstCommAdrLine3;
	}

	public void setAgentInstCommAdrLine3(String agentInstCommAdrLine3) {
		this.agentInstCommAdrLine3 = agentInstCommAdrLine3;
	}

	public String getAgentInstCommCity() {
		return agentInstCommCity;
	}

	public void setAgentInstCommCity(String agentInstCommCity) {
		this.agentInstCommCity = agentInstCommCity;
	}

	public String getAgentInstCommCountry() {
		return agentInstCommCountry;
	}

	public void setAgentInstCommCountry(String agentInstCommCountry) {
		this.agentInstCommCountry = agentInstCommCountry;
	}

	public String getAgentInstCommPinCode() {
		return agentInstCommPinCode;
	}

	public void setAgentInstCommPinCode(String agentInstCommPinCode) {
		this.agentInstCommPinCode = agentInstCommPinCode;
	}

	public String getAgentInstCommState() {
		return agentInstCommState;
	}

	public void setAgentInstCommState(String agentInstCommState) {
		this.agentInstCommState = agentInstCommState;
	}
	@ExcelColumn(label="Status")	
	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}
	@ExcelColumn(label="Effective From")	
	public String getAgentInstEffctvFrom() {
		return agentInstEffctvFrom;
	}

	public void setAgentInstEffctvFrom(String agentInstEffctvFrom) {
		this.agentInstEffctvFrom = agentInstEffctvFrom;
	}
	@ExcelColumn(label="Effective To")	
	public String getAgentInstEffctvTo() {
		return agentInstEffctvTo;
	}

	public void setAgentInstEffctvTo(String agentInstEffctvTo) {
		this.agentInstEffctvTo = agentInstEffctvTo;
	}

	public String getHiddenStateOnEdit() {
		return hiddenStateOnEdit;
	}

	public void setHiddenStateOnEdit(String hiddenStateOnEdit) {
		this.hiddenStateOnEdit = hiddenStateOnEdit;
	}

	public String getHiddenCityOnEdit() {
		return hiddenCityOnEdit;
	}

	public void setHiddenCityOnEdit(String hiddenCityOnEdit) {
		this.hiddenCityOnEdit = hiddenCityOnEdit;
	}

	public String getHiddenPinOnEdit() {
		return hiddenPinOnEdit;
	}

	public void setHiddenPinOnEdit(String hiddenPinOnEdit) {
		this.hiddenPinOnEdit = hiddenPinOnEdit;
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
	
	@ExcelColumn(label="Inst. Type")	
	public String getAgentInstType() {
		return agentInstType;
	}
	public void setAgentInstType(String agentInstType) {
		this.agentInstType = agentInstType;
	}
	@ExcelColumn(label="Business Type")	
	public String getAgentInstBusnsType() {
		return agentInstBusnsType;
	}

	public void setAgentInstBusnsType(String agentInstBusnsType) {
		this.agentInstBusnsType = agentInstBusnsType;
	}

	public String getAgentInstTanNo() {
		return agentInstTanNo;
	}

	public void setAgentInstTanNo(String agentInstTanNo) {
		this.agentInstTanNo = agentInstTanNo;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public ContactDetailsView getContactDetailsView1stLevel() {
		return contactDetailsView1stLevel;
	}

	public void setContactDetailsView1stLevel(
			ContactDetailsView contactDetailsView1stLevel) {
		this.contactDetailsView1stLevel = contactDetailsView1stLevel;
	}

	public ContactDetailsView getContactDetailsView2ndtLevel() {
		return contactDetailsView2ndtLevel;
	}

	public void setContactDetailsView2ndtLevel(
			ContactDetailsView contactDetailsView2ndtLevel) {
		this.contactDetailsView2ndtLevel = contactDetailsView2ndtLevel;
	}

	public String getAgentInstUaadhaar() {
		return agentInstUaadhaar;
	}

	public void setAgentInstUaadhaar(String agentInstUaadhaar) {
		this.agentInstUaadhaar = agentInstUaadhaar;
	}

	public String getAgentInstRocUin() {
		return agentInstRocUin;
	}

	public void setAgentInstRocUin(String agentInstRocUin) {
		this.agentInstRocUin = agentInstRocUin;
	}

	public String getAgentInstAccountNo() {
		return agentInstAccountNo;
	}

	public void setAgentInstAccountNo(String agentInstAccountNo) {
		this.agentInstAccountNo = agentInstAccountNo;
	}
	
}