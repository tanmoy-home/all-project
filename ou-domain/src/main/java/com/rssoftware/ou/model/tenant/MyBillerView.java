package com.rssoftware.ou.model.tenant;

import java.util.ArrayList;
import java.util.List;

import com.rssoftware.ou.common.BillerMode;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.ExcelColumn;
import com.rssoftware.ou.common.ExcelReport;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentModeLimit;

@ExcelReport(reportName="Biller_Details.xlsx")

public class MyBillerView implements Comparable<MyBillerView>{

	private String blrId;
	private String blrName;
	private String blrAliasName;
	private boolean isParentBlr;
	private String parentBlrId;
	private boolean blrAcceptsAdhoc;
	private String blrCategoryName;
	private String blrCommumicationAdrline;
	private String blrCommumicationCity;
	private String blrCommumicationCountry;
	private String blrCommumicationPinCode;
	private String blrCommumicationState;
	private String blrRegisteredAdrline;
	private String blrRegisteredCity;
	private String blrRegisteredCountry;
	private String blrRegisteredPinCode;
	private String blrRegisteredState;

	private List<PaymentModeLimit> billerPaymentModes;// json
	private List<PaymentChannelLimit> billerPaymentChannels;// json
	private List<ParamConfig> billerCustomerParams = new ArrayList<ParamConfig>();
	private List<ParamConfig> billerAdditionalInfo = new ArrayList<ParamConfig>();
	private BillerResponseParams billerResponseParams = new BillerResponseParams();
	private EntityStatus entityStatus;
	private String blrEffctvFrom;
	private String blrEffctvTo;
	
	/*Added for incorporating the addition of new fields in XSD start */
	private String blrOwnerShp;
	private String blrCoverage;
	
	private List<InterchangeFeeConfView> interchangeFeeConfView;
	private List<InterchangeFeeView> interchangeFeeView;
	//added for biller registration
	private String isCuUser="N";
	private String isCu = "N";
	private String blrLinkedOuDefault;
	private String tmpDeactvStartDt;
	private String tmpDeactvEndDt;
	private String userComments;
	private String isView = "N";
	private String isFromPending="N";
	private String sameAsReg="N";
	private String isParent="N";

	private byte[] billerAuthLetter1;
	private String billerAuthLetter1Name;
	private byte[] billerAuthLetter2;
	private String billerAuthLetter2Name;
	private byte[] billerAuthLetter3;
	private String billerAuthLetter3Name;
	private String isEdit = "N";
	private String changeOu;
	private int billerDefaultOuChangeVrsn;
	private String billerLinkedOuBackup1;
	private String billerLinkedOuBackup2 ;
	private String billerLinkedOuBackup3 ;
	private BillerMode blrMode;
	private String blrAvgTicketSize;
	private String blrEndpointURL;
	//for BillFileConfig
		private String fileType;
		private String dateFormat;
		private String rootElement;
		private String delimiter;
		private String targetClassName;
	    private List<BillMappingConfigView> billMappingConfigList;
		//for BillMappingConfig
		/*private int endPosition;
		private int startPosition;
		private int sequenceNo;
		private String fieldQualifier;
		private String fieldDataType;
		private String fieldFormat;*/
	private String fetchRequirement;
	public int getBillerDefaultOuChangeVrsn() {
		return billerDefaultOuChangeVrsn;
	}

	public void setBillerDefaultOuChangeVrsn(int billerDefaultOuChangeVrsn) {
		this.billerDefaultOuChangeVrsn = billerDefaultOuChangeVrsn;
	}

	public String getSameAsReg() {
		return sameAsReg;
	}

	public void setSameAsReg(String sameAsReg) {
		this.sameAsReg = sameAsReg;
	}

	public String getUserComments() {
		return userComments;
	}

	public void setUserComments(String userComments) {
		this.userComments = userComments;
	}

	public List<InterchangeFeeConfView> getInterchangeFeeConfView() {
		return interchangeFeeConfView;
	}

	public void setInterchangeFeeConfView(List<InterchangeFeeConfView> interchangeFeeConfView) {
		this.interchangeFeeConfView = interchangeFeeConfView;
	}

	public List<InterchangeFeeView> getInterchangeFeeView() {
		return interchangeFeeView;
	}

	public void setInterchangeFeeView(List<InterchangeFeeView> interchangeFeeView) {
		this.interchangeFeeView = interchangeFeeView;
	}

	public String getBlrOwnerShp() {
		return blrOwnerShp;
	}

	public void setBlrOwnerShp(String blrOwnerShp) {
		this.blrOwnerShp = blrOwnerShp;
	}

	public String getBlrCoverage() {
		return blrCoverage;
	}

	public void setBlrCoverage(String blrCoverage) {
		this.blrCoverage = blrCoverage;
	}
	
	/*Added for incorporating the addition of new fields in XSD end */
	@ExcelColumn(label="Biller ID")
	public String getBlrId() {
		return blrId;
	}

	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}
	@ExcelColumn(label="Biller Name")
	public String getBlrName() {
		return blrName;
	}

	public void setBlrName(String blrName) {
		this.blrName = blrName;
	}

	public String getBlrAliasName() {
		return blrAliasName;
	}

	public boolean isParentBlr() {
		return isParentBlr;
	}

	public void setParentBlr(boolean isParentBlr) {
		this.isParentBlr = isParentBlr;
	}

	public void setBlrAliasName(String blrAliasName) {
		this.blrAliasName = blrAliasName;
	}

	public boolean isBlrAcceptsAdhoc() {
		return blrAcceptsAdhoc;
	}

	public void setBlrAcceptsAdhoc(boolean blrAcceptsAdhoc) {
		this.blrAcceptsAdhoc = blrAcceptsAdhoc;
	}
	@ExcelColumn(label="Biller Category")
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
	@ExcelColumn(label="Status")
	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public List<PaymentModeLimit> getBillerPaymentModes() {
		return billerPaymentModes;
	}

	public void setBillerPaymentModes(List<PaymentModeLimit> billerPaymentModes) {
		this.billerPaymentModes = billerPaymentModes;
	}

	public List<PaymentChannelLimit> getBillerPaymentChannels() {
		return billerPaymentChannels;
	}

	public void setBillerPaymentChannels(List<PaymentChannelLimit> billerPaymentChannels) {
		this.billerPaymentChannels = billerPaymentChannels;
	}

	public List<ParamConfig> getBillerCustomerParams() {
		return billerCustomerParams;
	}

	public void setBillerCustomerParams(List<ParamConfig> billerCustomerParams) {
		this.billerCustomerParams = billerCustomerParams;
	}

	public List<ParamConfig> getBillerAdditionalInfo() {
		return billerAdditionalInfo;
	}

	public void setBillerAdditionalInfo(List<ParamConfig> billerAdditionalInfo) {
		this.billerAdditionalInfo = billerAdditionalInfo;
	}

	public BillerResponseParams getBillerResponseParams() {
		return billerResponseParams;
	}

	public void setBillerResponseParams(BillerResponseParams billerResponseParams) {
		this.billerResponseParams = billerResponseParams;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
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
	@ExcelColumn(label="Parent Biller ID")
	public String getParentBlrId() {
		return parentBlrId;
	}

	public void setParentBlrId(String parentBlrId) {
		this.parentBlrId = parentBlrId;
	}

	@Override
	public int compareTo(MyBillerView o) {
		return (blrName!=null?blrName:"").compareTo(o.blrName!=null?o.blrName:"");
	}

	public String getIsCuUser() {
		return isCuUser;
	}

	public void setIsCuUser(String isCuUser) {
		this.isCuUser = isCuUser;
	}

	public String getIsCu() {
		return isCu;
	}

	public void setIsCu(String isCu) {
		this.isCu = isCu;
	}

	public String getBlrLinkedOuDefault() {
		return blrLinkedOuDefault;
	}

	public void setBlrLinkedOuDefault(String blrLinkedOuDefault) {
		this.blrLinkedOuDefault = blrLinkedOuDefault;
	}

	public String getTmpDeactvStartDt() {
		return tmpDeactvStartDt;
	}

	public void setTmpDeactvStartDt(String tmpDeactvStartDt) {
		this.tmpDeactvStartDt = tmpDeactvStartDt;
	}

	public String getTmpDeactvEndDt() {
		return tmpDeactvEndDt;
	}

	public void setTmpDeactvEndDt(String tmpDeactvEndDt) {
		this.tmpDeactvEndDt = tmpDeactvEndDt;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getIsView() {
		return isView;
	}

	public void setIsView(String isView) {
		this.isView = isView;
	}

	public String getIsFromPending() {
		return isFromPending;
	}

	public void setIsFromPending(String isFromPending) {
		this.isFromPending = isFromPending;
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

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getChangeOu() {
		return changeOu;
	}

	public void setChangeOu(String changeOu) {
		this.changeOu = changeOu;
	}

	public String getBillerLinkedOuBackup1() {
		return billerLinkedOuBackup1;
	}

	public void setBillerLinkedOuBackup1(String billerLinkedOuBackup1) {
		this.billerLinkedOuBackup1 = billerLinkedOuBackup1;
	}

	public String getBillerLinkedOuBackup2() {
		return billerLinkedOuBackup2;
	}

	public void setBillerLinkedOuBackup2(String billerLinkedOuBackup2) {
		this.billerLinkedOuBackup2 = billerLinkedOuBackup2;
	}

	public String getBillerLinkedOuBackup3() {
		return billerLinkedOuBackup3;
	}

	public void setBillerLinkedOuBackup3(String billerLinkedOuBackup3) {
		this.billerLinkedOuBackup3 = billerLinkedOuBackup3;
	}

	public BillerMode getBlrMode() {
		return blrMode;
	}

	public void setBlrMode(BillerMode blrMode) {
		this.blrMode = blrMode;
	}

	public String getBlrAvgTicketSize() {
		return blrAvgTicketSize;
	}

	public void setBlrAvgTicketSize(String blrAvgTicketSize) {
		this.blrAvgTicketSize = blrAvgTicketSize;
	}

	public String getBlrEndpointURL() {
		return blrEndpointURL;
	}

	public void setBlrEndpointURL(String blrEndpointURL) {
		this.blrEndpointURL = blrEndpointURL;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getRootElement() {
		return rootElement;
	}

	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public void setTargetClassName(String targetClassName) {
		this.targetClassName = targetClassName;
	}

	public List<BillMappingConfigView> getBillMappingConfigList() {
		return billMappingConfigList;
	}

	public void setBillMappingConfigList(
			List<BillMappingConfigView> billMappingConfigList) {
		this.billMappingConfigList = billMappingConfigList;
	}

	public String getFetchRequirement() {
		return fetchRequirement;
	}

	public void setFetchRequirement(String fetchRequirement) {
		this.fetchRequirement = fetchRequirement;
	}
	
}