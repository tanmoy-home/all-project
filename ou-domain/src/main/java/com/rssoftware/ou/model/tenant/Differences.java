package com.rssoftware.ou.model.tenant;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class Differences {
	private String OUmsgId;
	private String OUagentId;
	private String OUbillerId;
	private String OUcustomerMobileNumber;
	private Date OUtxnDate;
	private String OUtxnCurrencyCode;
	private BigDecimal OUcustomerConvenienceFee;
	private BigDecimal OUtxnAmount;
	private String OUresponseCode;
	private String OUtxnReferenceId;

	private String OUbillerCategory;
	private BigDecimal OUbillerOuCountMonth;
	private String OUbillerOuId;
	private BigDecimal OUbillerOuInterchangeFee;
	private BigDecimal OUbillerOuSwitchingFee;
	private BigDecimal OUcasProcessed;
	private Timestamp OUclearingTimestamp;
	private BigDecimal OUcustomerOuCountMonth;
	private String OUcustomerOuId;
	private BigDecimal OUcustomerOuInterchangeFee;
	private BigDecimal OUcustomerOuSwitchingFee;
	private Boolean OUdecline;
	private String OUmti;
	private String OUpaymentChannel;
	private String OUpaymentMode;
	private Boolean OUreversal;
	private Boolean OUsplitPay;
	private BigDecimal OUsplitPayTxnAmount;
	private String OUsplitPaymentMode;
	
	private String CUmsgId;
	private String CUagentId;
	private String CUbillerId;
	private String CUcustomerMobileNumber;
	private Date CUtxnDate;
	private String CUtxnCurrencyCode;
	private BigDecimal CUcustomerConvenienceFee;
	private BigDecimal CUtxnAmount;
	private String CUresponseCode;
	private String CUtxnReferenceId;

	private String CUbillerCategory;
	private BigDecimal CUbillerOuCountMonth;
	private String CUbillerOuId;
	private BigDecimal CUbillerOuInterchangeFee;
	private BigDecimal CUbillerOuSwitchingFee;
	private BigDecimal CUcasProcessed;
	private Timestamp CUclearingTimestamp;
	private BigDecimal CUcustomerOuCountMonth;
	private String CUcustomerOuId;
	private BigDecimal CUcustomerOuInterchangeFee;
	private BigDecimal CUcustomerOuSwitchingFee;
	private Boolean CUdecline;
	private String CUmti;
	private String CUpaymentChannel;
	private String CUpaymentMode;
	private Boolean CUreversal;
	private Boolean CUsplitPay;
	private BigDecimal CUsplitPayTxnAmount;
	private String CUsplitPaymentMode;

	
	public String getOUmsgId() {
		return OUmsgId;
	}


	public void setOUmsgId(String oUmsgId) {
		OUmsgId = oUmsgId;
	}


	public String getOUagentId() {
		return OUagentId;
	}


	public void setOUagentId(String oUagentId) {
		OUagentId = oUagentId;
	}


	public String getOUbillerId() {
		return OUbillerId;
	}


	public void setOUbillerId(String oUbillerId) {
		OUbillerId = oUbillerId;
	}


	public String getOUcustomerMobileNumber() {
		return OUcustomerMobileNumber;
	}


	public void setOUcustomerMobileNumber(String oUcustomerMobileNumber) {
		OUcustomerMobileNumber = oUcustomerMobileNumber;
	}


	public Date getOUtxnDate() {
		return OUtxnDate;
	}


	public void setOUtxnDate(Date oUtxnDate) {
		OUtxnDate = oUtxnDate;
	}


	public String getOUtxnCurrencyCode() {
		return OUtxnCurrencyCode;
	}


	public void setOUtxnCurrencyCode(String oUtxnCurrencyCode) {
		OUtxnCurrencyCode = oUtxnCurrencyCode;
	}


	public BigDecimal getOUcustomerConvenienceFee() {
		return OUcustomerConvenienceFee;
	}


	public void setOUcustomerConvenienceFee(BigDecimal oUcustomerConvenienceFee) {
		OUcustomerConvenienceFee = oUcustomerConvenienceFee;
	}


	public BigDecimal getOUtxnAmount() {
		return OUtxnAmount;
	}


	public void setOUtxnAmount(BigDecimal oUtxnAmount) {
		OUtxnAmount = oUtxnAmount;
	}


	public String getOUresponseCode() {
		return OUresponseCode;
	}


	public void setOUresponseCode(String oUresponseCode) {
		OUresponseCode = oUresponseCode;
	}


	public String getOUtxnReferenceId() {
		return OUtxnReferenceId;
	}


	public void setOUtxnReferenceId(String oUtxnReferenceId) {
		OUtxnReferenceId = oUtxnReferenceId;
	}


	public String getOUbillerCategory() {
		return OUbillerCategory;
	}


	public void setOUbillerCategory(String oUbillerCategory) {
		OUbillerCategory = oUbillerCategory;
	}


	public BigDecimal getOUbillerOuCountMonth() {
		return OUbillerOuCountMonth;
	}


	public void setOUbillerOuCountMonth(BigDecimal oUbillerOuCountMonth) {
		OUbillerOuCountMonth = oUbillerOuCountMonth;
	}


	public String getOUbillerOuId() {
		return OUbillerOuId;
	}


	public void setOUbillerOuId(String oUbillerOuId) {
		OUbillerOuId = oUbillerOuId;
	}


	public BigDecimal getOUbillerOuInterchangeFee() {
		return OUbillerOuInterchangeFee;
	}


	public void setOUbillerOuInterchangeFee(BigDecimal oUbillerOuInterchangeFee) {
		OUbillerOuInterchangeFee = oUbillerOuInterchangeFee;
	}


	public BigDecimal getOUbillerOuSwitchingFee() {
		return OUbillerOuSwitchingFee;
	}


	public void setOUbillerOuSwitchingFee(BigDecimal oUbillerOuSwitchingFee) {
		OUbillerOuSwitchingFee = oUbillerOuSwitchingFee;
	}


	public BigDecimal getOUcasProcessed() {
		return OUcasProcessed;
	}


	public void setOUcasProcessed(BigDecimal oUcasProcessed) {
		OUcasProcessed = oUcasProcessed;
	}


	public Timestamp getOUclearingTimestamp() {
		return OUclearingTimestamp;
	}


	public void setOUclearingTimestamp(Timestamp oUclearingTimestamp) {
		OUclearingTimestamp = oUclearingTimestamp;
	}


	public BigDecimal getOUcustomerOuCountMonth() {
		return OUcustomerOuCountMonth;
	}


	public void setOUcustomerOuCountMonth(BigDecimal oUcustomerOuCountMonth) {
		OUcustomerOuCountMonth = oUcustomerOuCountMonth;
	}


	public String getOUcustomerOuId() {
		return OUcustomerOuId;
	}


	public void setOUcustomerOuId(String oUcustomerOuId) {
		OUcustomerOuId = oUcustomerOuId;
	}


	public BigDecimal getOUcustomerOuInterchangeFee() {
		return OUcustomerOuInterchangeFee;
	}


	public void setOUcustomerOuInterchangeFee(BigDecimal oUcustomerOuInterchangeFee) {
		OUcustomerOuInterchangeFee = oUcustomerOuInterchangeFee;
	}


	public BigDecimal getOUcustomerOuSwitchingFee() {
		return OUcustomerOuSwitchingFee;
	}


	public void setOUcustomerOuSwitchingFee(BigDecimal oUcustomerOuSwitchingFee) {
		OUcustomerOuSwitchingFee = oUcustomerOuSwitchingFee;
	}


	public Boolean getOUdecline() {
		return OUdecline;
	}


	public void setOUdecline(Boolean oUdecline) {
		OUdecline = oUdecline;
	}


	public String getOUmti() {
		return OUmti;
	}


	public void setOUmti(String oUmti) {
		OUmti = oUmti;
	}


	public String getOUpaymentChannel() {
		return OUpaymentChannel;
	}


	public void setOUpaymentChannel(String oUpaymentChannel) {
		OUpaymentChannel = oUpaymentChannel;
	}


	public String getOUpaymentMode() {
		return OUpaymentMode;
	}


	public void setOUpaymentMode(String oUpaymentMode) {
		OUpaymentMode = oUpaymentMode;
	}


	public Boolean getOUreversal() {
		return OUreversal;
	}


	public void setOUreversal(Boolean oUreversal) {
		OUreversal = oUreversal;
	}


	public Boolean getOUsplitPay() {
		return OUsplitPay;
	}


	public void setOUsplitPay(Boolean oUsplitPay) {
		OUsplitPay = oUsplitPay;
	}


	public BigDecimal getOUsplitPayTxnAmount() {
		return OUsplitPayTxnAmount;
	}


	public void setOUsplitPayTxnAmount(BigDecimal oUsplitPayTxnAmount) {
		OUsplitPayTxnAmount = oUsplitPayTxnAmount;
	}


	public String getOUsplitPaymentMode() {
		return OUsplitPaymentMode;
	}


	public void setOUsplitPaymentMode(String oUsplitPaymentMode) {
		OUsplitPaymentMode = oUsplitPaymentMode;
	}


	public String getCUmsgId() {
		return CUmsgId;
	}


	public void setCUmsgId(String cUmsgId) {
		CUmsgId = cUmsgId;
	}


	public String getCUagentId() {
		return CUagentId;
	}


	public void setCUagentId(String cUagentId) {
		CUagentId = cUagentId;
	}


	public String getCUbillerId() {
		return CUbillerId;
	}


	public void setCUbillerId(String cUbillerId) {
		CUbillerId = cUbillerId;
	}


	public String getCUcustomerMobileNumber() {
		return CUcustomerMobileNumber;
	}


	public void setCUcustomerMobileNumber(String cUcustomerMobileNumber) {
		CUcustomerMobileNumber = cUcustomerMobileNumber;
	}


	public Date getCUtxnDate() {
		return CUtxnDate;
	}


	public void setCUtxnDate(Date cUtxnDate) {
		CUtxnDate = cUtxnDate;
	}


	public String getCUtxnCurrencyCode() {
		return CUtxnCurrencyCode;
	}


	public void setCUtxnCurrencyCode(String cUtxnCurrencyCode) {
		CUtxnCurrencyCode = cUtxnCurrencyCode;
	}


	public BigDecimal getCUcustomerConvenienceFee() {
		return CUcustomerConvenienceFee;
	}


	public void setCUcustomerConvenienceFee(BigDecimal cUcustomerConvenienceFee) {
		CUcustomerConvenienceFee = cUcustomerConvenienceFee;
	}


	public BigDecimal getCUtxnAmount() {
		return CUtxnAmount;
	}


	public void setCUtxnAmount(BigDecimal cUtxnAmount) {
		CUtxnAmount = cUtxnAmount;
	}


	public String getCUresponseCode() {
		return CUresponseCode;
	}


	public void setCUresponseCode(String cUresponseCode) {
		CUresponseCode = cUresponseCode;
	}


	public String getCUtxnReferenceId() {
		return CUtxnReferenceId;
	}


	public void setCUtxnReferenceId(String cUtxnReferenceId) {
		CUtxnReferenceId = cUtxnReferenceId;
	}


	public String getCUbillerCategory() {
		return CUbillerCategory;
	}


	public void setCUbillerCategory(String cUbillerCategory) {
		CUbillerCategory = cUbillerCategory;
	}


	public BigDecimal getCUbillerOuCountMonth() {
		return CUbillerOuCountMonth;
	}


	public void setCUbillerOuCountMonth(BigDecimal cUbillerOuCountMonth) {
		CUbillerOuCountMonth = cUbillerOuCountMonth;
	}


	public String getCUbillerOuId() {
		return CUbillerOuId;
	}


	public void setCUbillerOuId(String cUbillerOuId) {
		CUbillerOuId = cUbillerOuId;
	}


	public BigDecimal getCUbillerOuInterchangeFee() {
		return CUbillerOuInterchangeFee;
	}


	public void setCUbillerOuInterchangeFee(BigDecimal cUbillerOuInterchangeFee) {
		CUbillerOuInterchangeFee = cUbillerOuInterchangeFee;
	}


	public BigDecimal getCUbillerOuSwitchingFee() {
		return CUbillerOuSwitchingFee;
	}


	public void setCUbillerOuSwitchingFee(BigDecimal cUbillerOuSwitchingFee) {
		CUbillerOuSwitchingFee = cUbillerOuSwitchingFee;
	}


	public BigDecimal getCUcasProcessed() {
		return CUcasProcessed;
	}


	public void setCUcasProcessed(BigDecimal cUcasProcessed) {
		CUcasProcessed = cUcasProcessed;
	}


	public Timestamp getCUclearingTimestamp() {
		return CUclearingTimestamp;
	}


	public void setCUclearingTimestamp(Timestamp cUclearingTimestamp) {
		CUclearingTimestamp = cUclearingTimestamp;
	}


	public BigDecimal getCUcustomerOuCountMonth() {
		return CUcustomerOuCountMonth;
	}


	public void setCUcustomerOuCountMonth(BigDecimal cUcustomerOuCountMonth) {
		CUcustomerOuCountMonth = cUcustomerOuCountMonth;
	}


	public String getCUcustomerOuId() {
		return CUcustomerOuId;
	}


	public void setCUcustomerOuId(String cUcustomerOuId) {
		CUcustomerOuId = cUcustomerOuId;
	}


	public BigDecimal getCUcustomerOuInterchangeFee() {
		return CUcustomerOuInterchangeFee;
	}


	public void setCUcustomerOuInterchangeFee(BigDecimal cUcustomerOuInterchangeFee) {
		CUcustomerOuInterchangeFee = cUcustomerOuInterchangeFee;
	}


	public BigDecimal getCUcustomerOuSwitchingFee() {
		return CUcustomerOuSwitchingFee;
	}


	public void setCUcustomerOuSwitchingFee(BigDecimal cUcustomerOuSwitchingFee) {
		CUcustomerOuSwitchingFee = cUcustomerOuSwitchingFee;
	}


	public Boolean getCUdecline() {
		return CUdecline;
	}


	public void setCUdecline(Boolean cUdecline) {
		CUdecline = cUdecline;
	}


	public String getCUmti() {
		return CUmti;
	}


	public void setCUmti(String cUmti) {
		CUmti = cUmti;
	}


	public String getCUpaymentChannel() {
		return CUpaymentChannel;
	}


	public void setCUpaymentChannel(String cUpaymentChannel) {
		CUpaymentChannel = cUpaymentChannel;
	}


	public String getCUpaymentMode() {
		return CUpaymentMode;
	}


	public void setCUpaymentMode(String cUpaymentMode) {
		CUpaymentMode = cUpaymentMode;
	}


	public Boolean getCUreversal() {
		return CUreversal;
	}


	public void setCUreversal(Boolean cUreversal) {
		CUreversal = cUreversal;
	}


	public Boolean getCUsplitPay() {
		return CUsplitPay;
	}


	public void setCUsplitPay(Boolean cUsplitPay) {
		CUsplitPay = cUsplitPay;
	}


	public BigDecimal getCUsplitPayTxnAmount() {
		return CUsplitPayTxnAmount;
	}


	public void setCUsplitPayTxnAmount(BigDecimal cUsplitPayTxnAmount) {
		CUsplitPayTxnAmount = cUsplitPayTxnAmount;
	}


	public String getCUsplitPaymentMode() {
		return CUsplitPaymentMode;
	}


	public void setCUsplitPaymentMode(String cUsplitPaymentMode) {
		CUsplitPaymentMode = cUsplitPaymentMode;
	}


	@Override
	public String toString() {
		/*
		 * return "Differences [msgId=" + msgId + ", agentId=" + agentId +
		 * ", billerId=" + billerId + ", customerMobileNumber=" +
		 * customerMobileNumber + ", txnDate=" + txnDate + ", txnCurrencyCode="
		 * + txnCurrencyCode + ", customerConvenienceFee=" +
		 * customerConvenienceFee + ", txnAmount=" + txnAmount +
		 * ", responseCode=" + responseCode + ", txnReferenceId=" +
		 * txnReferenceId + ", billerCategory=" + billerCategory +
		 * ", billerOuCountMonth=" + billerOuCountMonth + ", billerOuId=" +
		 * billerOuId + ", billerOuInterchangeFee=" + billerOuInterchangeFee +
		 * ", billerOuSwitchingFee=" + billerOuSwitchingFee + ", casProcessed="
		 * + casProcessed + ", clearingTimestamp=" + clearingTimestamp +
		 * ", customerOuCountMonth=" + customerOuCountMonth + ", customerOuId="
		 * + customerOuId + ", customerOuInterchangeFee=" +
		 * customerOuInterchangeFee + ", customerOuSwitchingFee=" +
		 * customerOuSwitchingFee + ", decline=" + decline + ", mti=" + mti +
		 * ", paymentChannel=" + paymentChannel + ", paymentMode=" + paymentMode
		 * + ", reversal=" + reversal + ", splitPay=" + splitPay +
		 * ", splitPayTxnAmount=" + splitPayTxnAmount + ", splitPaymentMode=" +
		 * splitPaymentMode + "]";
		 */

		String diff = "Differences ---";
		if (getOUmsgId() != null || getCUmsgId() != null)
			diff = diff + " [OUmsgId=" + OUmsgId+ ", CUmsgId=" + CUmsgId + "]";
		if (getOUagentId() != null || getCUagentId() != null)
			diff = diff + " [OUagentId=" + OUagentId+ ", CUagentId=" + CUagentId + "]";
		if (getOUbillerId() != null || getCUbillerId() != null)
			diff = diff + " [OUbillerId=" + OUbillerId+ ", CUbillerId="+ CUbillerId+ "]";
		if (getOUcustomerMobileNumber() != null || getCUcustomerMobileNumber() != null)
			diff = diff + " [OUcustomerMobileNumber=" + OUcustomerMobileNumber+ ", CUcustomerMobileNumber="+ CUcustomerMobileNumber+ "]";
		if (getOUtxnDate() != null || getCUtxnDate() != null)
			diff = diff + " [OUtxnDate=" + OUtxnDate+ ", CUtxnDate=" +CUtxnDate+ "]";
		if (getOUtxnCurrencyCode() != null || getCUtxnCurrencyCode() != null)
			diff = diff + " [OUtxnCurrencyCode=" + OUtxnCurrencyCode+ ", CUtxnCurrencyCode=" +CUtxnCurrencyCode+ "]";
		if (getOUcustomerConvenienceFee() != null || getCUcustomerConvenienceFee() != null)
			diff = diff + " [OUcustomerConvenienceFee=" + OUcustomerConvenienceFee+ ", CUcustomerConvenienceFee=" + CUcustomerConvenienceFee+ "]";
		if (getOUtxnAmount() != null || getCUtxnAmount() != null)
			diff = diff + " [OUtxnAmount=" + OUtxnAmount+ ", CUtxnAmount="+ CUtxnAmount+ "]";
		if (getOUresponseCode() != null || getCUresponseCode() != null)
			diff = diff + " [OUresponseCode=" + OUresponseCode+ ", CUresponseCode=" + CUresponseCode+ "]";
		if (getOUtxnReferenceId() != null || getCUtxnReferenceId() != null)
			diff = diff + " [OUtxnReferenceId=" + OUtxnReferenceId+ ", CUtxnReferenceId=" + CUtxnReferenceId+ "]";
		if (getOUbillerCategory() != null || getCUbillerCategory() != null)
			diff = diff + " [OUbillerCategory=" + OUbillerCategory+ ", CUbillerCategory=" + CUbillerCategory+ "]";
		if (getOUbillerOuCountMonth() != null || getCUbillerOuCountMonth() != null)
			diff = diff + " [OUbillerOuCountMonth=" + OUbillerOuCountMonth+ ", CUbillerOuCountMonth=" + CUbillerOuCountMonth+ "]";
		if (getOUbillerOuId() != null || getCUbillerOuId() != null)
			diff = diff + " [OUbillerOuId=" + OUbillerOuId+ ", CUbillerOuId=" + CUbillerOuId+ "]";
		if (getOUbillerOuInterchangeFee() != null || getCUbillerOuInterchangeFee() != null)
			diff = diff + " [OUbillerOuInterchangeFee=" + OUbillerOuInterchangeFee+ "CUbillerOuInterchangeFee=" + CUbillerOuInterchangeFee+ "]";
		if (getOUbillerOuSwitchingFee() != null || getCUbillerOuSwitchingFee() != null)
			diff = diff + " [OUbillerOuSwitchingFee=" + OUbillerOuSwitchingFee+ ", CUbillerOuSwitchingFee=" + CUbillerOuSwitchingFee+ "]";
		if (getOUcasProcessed() != null || getCUcasProcessed() != null)
			diff = diff + " [OUcasProcessed=" + OUcasProcessed+ ", [CUcasProcessed=" + CUcasProcessed+ "]";
		if (getOUclearingTimestamp() != null || getCUclearingTimestamp() != null)
			diff = diff + " [OUclearingTimestamp=" + OUclearingTimestamp+ ", CUclearingTimestamp=" + CUclearingTimestamp+ "]";
		if (getOUcustomerOuCountMonth() != null || getCUcustomerOuCountMonth() != null)
			diff = diff + " [OUcustomerOuCountMonth=" + OUcustomerOuCountMonth+ ", CUcustomerOuCountMonth=" + CUcustomerOuCountMonth+"]";
		if (getOUcustomerOuId() != null || getCUcustomerOuId() != null)
			diff = diff + " [OUcustomerOuId=" + OUcustomerOuId+ ", CUcustomerOuId=" + CUcustomerOuId+ "]";
		if (getOUcustomerOuInterchangeFee() != null || getCUcustomerOuInterchangeFee() != null)
			diff = diff + " [OUcustomerOuInterchangeFee="+ OUcustomerOuInterchangeFee+ ", CUcustomerOuInterchangeFee="+ CUcustomerOuInterchangeFee+"]" ;
		if (getOUcustomerOuSwitchingFee() != null || getCUcustomerOuSwitchingFee() != null)
			diff = diff + " [OUcustomerOuSwitchingFee=" + OUcustomerOuSwitchingFee+ ", CUcustomerOuSwitchingFee=" + CUcustomerOuSwitchingFee+"]";
		if (getOUdecline() != null || getCUdecline() != null)
			diff = diff + " [OUdecline=" + OUdecline+ ", CUdecline=" + CUdecline+"]";
		if (getOUmti() != null || getCUmti() != null)
			diff = diff + " [OUmti=" + OUmti+ ", CUmti=" + CUmti+"]";
		if (getOUpaymentChannel() != null || getCUpaymentChannel() != null)
			diff = diff + " [OUpaymentChannel=" + OUpaymentChannel+ ", CUpaymentChannel=" + CUpaymentChannel+ "]";
		if (getOUpaymentMode() != null || getCUpaymentMode() != null)
			diff = diff + " [OUpaymentMode=" + OUpaymentMode+ ", CUpaymentMode=" + CUpaymentMode+ "]";
		if (getOUsplitPay() != null || getCUsplitPay() != null)
			diff = diff + " [OUsplitPay=" + OUsplitPay+ ", CUsplitPay=" + CUsplitPay+ "]";
		if (getOUsplitPaymentMode() != null || getCUsplitPaymentMode() != null)
			diff = diff + " [OUsplitPaymentMode=" + OUsplitPaymentMode+ "CUsplitPaymentMode=" + CUsplitPaymentMode+ "]";
		if (getOUsplitPayTxnAmount() != null || getCUsplitPayTxnAmount() != null)
			diff = diff + " [OUsplitPayTxnAmount=" + OUsplitPayTxnAmount+ ", CUsplitPayTxnAmount=" + CUsplitPayTxnAmount+ "]";
		
		return diff;

	}

}