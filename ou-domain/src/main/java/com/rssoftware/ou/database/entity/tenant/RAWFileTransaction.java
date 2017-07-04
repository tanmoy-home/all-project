package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transaction", propOrder = {
		"msgId",
		"refId",
		"mti",
		"txnReferenceId",
		"txnType",
		"txnDate",
		"customerOUId",
		"billerOUId",
		"agentId",
		"responseCode",
		"txnCurrencyCode",
		"txnAmount",
		"customerOUInterchangeFee",
		"customerOUSwitchingFee",
		"billerOUInterchangeFee",
		"billerOUSwitchingFee",
		"customerConvenienceFee",
		"clearingTimestamp",
		"paymentChannel",
		"paymentMode",
		"customerOUCountMonth",
		"billerOUCountMonth",
		"billerId",
		"billerCategory",
		"splitPay",
		"splitPaymentMode",
		"splitPayTxnAmount",
		"customerMobileNumber",
		"reversal",
		"decline",
		"casProcessed",
		"settlementCycleId",
		"serviceFee",
		"serviceFeeDescription",
		"serviceFeeTax",
		"billerFee",
		"customerConvenienceFeeTax",
		"billerFeeTax",
		"customerOUSwitchingFeeTax",
		"billerOUSwitchingFeeTax"
})
public class RAWFileTransaction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3062615929571520416L;
	@XmlElement
	private String msgId;
	@XmlElement
	private String refId;
	@XmlElement
	private String mti;
	@XmlElement
	private String txnReferenceId;
	@XmlElement
	private String txnType;
	@XmlElement
	private String txnDate;
	@XmlElement
	private String customerOUId;
	@XmlElement
	private String billerOUId;
	@XmlElement
	private String agentId;
	@XmlElement
	private String responseCode;
	@XmlElement
	private String txnCurrencyCode;
	@XmlElement
	private Long txnAmount;
	@XmlElement
	private BigDecimal customerOUInterchangeFee;
	@XmlElement
	private BigDecimal customerOUSwitchingFee;
	@XmlElement
	private BigDecimal billerOUInterchangeFee;
	@XmlElement
	private BigDecimal billerOUSwitchingFee;
	@XmlElement
	private Long customerConvenienceFee;
	@XmlElement
	private String clearingTimestamp;
	@XmlElement
	private String paymentChannel;
	@XmlElement
	private String paymentMode;
	@XmlElement
	private Long customerOUCountMonth;
	@XmlElement
	private Long billerOUCountMonth;
	@XmlElement
	private String billerId;
	@XmlElement
	private String billerCategory;
	@XmlElement
	private Boolean splitPay;
	@XmlElement
	private String splitPaymentMode;
	@XmlElement
	private Long splitPayTxnAmount;
	@XmlElement
	private String customerMobileNumber;
	@XmlElement
	private Boolean reversal;
	@XmlElement
	private Boolean decline;
	@XmlElement
	private Boolean casProcessed;
	@XmlElement
	private String settlementCycleId;
	@XmlElement
	private Long serviceFee;
	@XmlElement
	private String serviceFeeDescription;
	@XmlElement
	private Long serviceFeeTax;
	@XmlElement
	private BigDecimal billerFee;
	@XmlElement
	private BigDecimal customerConvenienceFeeTax;
	@XmlElement
	private Long billerFeeTax;
	@XmlElement
	private Long customerOUSwitchingFeeTax;
	@XmlElement
	private Long billerOUSwitchingFeeTax;

	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getMti() {
		return mti;
	}
	public void setMti(String mti) {
		this.mti = mti;
	}
	public String getTxnReferenceId() {
		return txnReferenceId;
	}
	public void setTxnReferenceId(String txnReferenceId) {
		this.txnReferenceId = txnReferenceId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getCustomerOUId() {
		return customerOUId;
	}
	public void setCustomerOUId(String customerOUId) {
		this.customerOUId = customerOUId;
	}
	public String getBillerOUId() {
		return billerOUId;
	}
	public void setBillerOUId(String billerOUId) {
		this.billerOUId = billerOUId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getTxnCurrencyCode() {
		return txnCurrencyCode;
	}
	public void setTxnCurrencyCode(String txnCurrencyCode) {
		this.txnCurrencyCode = txnCurrencyCode;
	}
	public Long getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(Long txnAmount) {
		this.txnAmount = txnAmount;
	}
	public BigDecimal getCustomerOUInterchangeFee() {
		return customerOUInterchangeFee;
	}
	public void setCustomerOUInterchangeFee(BigDecimal customerOUInterchangeFee) {
		this.customerOUInterchangeFee = customerOUInterchangeFee;
	}
	public BigDecimal getCustomerOUSwitchingFee() {
		return customerOUSwitchingFee;
	}
	public void setCustomerOUSwitchingFee(BigDecimal customerOUSwitchingFee) {
		this.customerOUSwitchingFee = customerOUSwitchingFee;
	}
	public BigDecimal getBillerOUInterchangeFee() {
		return billerOUInterchangeFee;
	}
	public void setBillerOUInterchangeFee(BigDecimal billerOUInterchangeFee) {
		this.billerOUInterchangeFee = billerOUInterchangeFee;
	}
	public BigDecimal getBillerOUSwitchingFee() {
		return billerOUSwitchingFee;
	}
	public void setBillerOUSwitchingFee(BigDecimal billerOUSwitchingFee) {
		this.billerOUSwitchingFee = billerOUSwitchingFee;
	}
	public Long getCustomerConvenienceFee() {
		return customerConvenienceFee;
	}
	public void setCustomerConvenienceFee(Long customerConvenienceFee) {
		this.customerConvenienceFee = customerConvenienceFee;
	}
	public String getClearingTimestamp() {
		return clearingTimestamp;
	}
	public void setClearingTimestamp(String clearingTimestamp) {
		this.clearingTimestamp = clearingTimestamp;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Long getCustomerOUCountMonth() {
		return customerOUCountMonth;
	}
	public void setCustomerOUCountMonth(Long customerOUCountMonth) {
		this.customerOUCountMonth = customerOUCountMonth;
	}
	public Long getBillerOUCountMonth() {
		return billerOUCountMonth;
	}
	public void setBillerOUCountMonth(Long billerOUCountMonth) {
		this.billerOUCountMonth = billerOUCountMonth;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getBillerCategory() {
		return billerCategory;
	}
	public void setBillerCategory(String billerCategory) {
		this.billerCategory = billerCategory;
	}
	public Boolean isSplitPay() {
		return splitPay;
	}
	public void setSplitPay(Boolean splitPay) {
		this.splitPay = splitPay;
	}
	public String getSplitPaymentMode() {
		return splitPaymentMode;
	}
	public void setSplitPaymentMode(String splitPaymentMode) {
		this.splitPaymentMode = splitPaymentMode;
	}
	public Long getSplitPayTxnAmount() {
		return splitPayTxnAmount;
	}
	public void setSplitPayTxnAmount(Long splitPayTxnAmount) {
		this.splitPayTxnAmount = splitPayTxnAmount;
	}
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}
	public Boolean isReversal() {
		return reversal;
	}
	public void setReversal(Boolean reversal) {
		this.reversal = reversal;
	}
	public Boolean isDecline() {
		return decline;
	}
	public void setDecline(Boolean decline) {
		this.decline = decline;
	}
	public Boolean isCasProcessed() {
		return casProcessed;
	}
	public void setCasProcessed(Boolean casProcessed) {
		this.casProcessed = casProcessed;
	}
	public String getSettlementCycleId() {
		return settlementCycleId;
	}
	public void setSettlementCycleId(String settlementCycleId) {
		this.settlementCycleId = settlementCycleId;
	}
	public Long getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(Long serviceFee) {
		this.serviceFee = serviceFee;
	}
	public String getServiceFeeDescription() {
		return serviceFeeDescription;
	}
	public void setServiceFeeDescription(String serviceFeeDescription) {
		this.serviceFeeDescription = serviceFeeDescription;
	}
	public Long getServiceFeeTax() {
		return serviceFeeTax;
	}
	public void setServiceFeeTax(Long serviceFeeTax) {
		this.serviceFeeTax = serviceFeeTax;
	}
	public Boolean getSplitPay() {
		return splitPay;
	}
	public Boolean getReversal() {
		return reversal;
	}
	public Boolean getDecline() {
		return decline;
	}
	public Boolean getCasProcessed() {
		return casProcessed;
	}
	public BigDecimal getBillerFee() {
		return billerFee;
	}
	public void setBillerFee(BigDecimal billerFee) {
		this.billerFee = billerFee;
	}
	public BigDecimal getCustomerConvenienceFeeTax() {
		return customerConvenienceFeeTax;
	}
	public void setCustomerConvenienceFeeTax(BigDecimal customerConvenienceFeeTax) {
		this.customerConvenienceFeeTax = customerConvenienceFeeTax;
	}
	public Long getBillerFeeTax() {
		return billerFeeTax;
	}
	public void setBillerFeeTax(Long billerFeeTax) {
		this.billerFeeTax = billerFeeTax;
	}
	public Long getCustomerOUSwitchingFeeTax() {
		return customerOUSwitchingFeeTax;
	}
	public void setCustomerOUSwitchingFeeTax(Long customerOUSwitchingFeeTax) {
		this.customerOUSwitchingFeeTax = customerOUSwitchingFeeTax;
	}
	public Long getBillerOUSwitchingFeeTax() {
		return billerOUSwitchingFeeTax;
	}
	public void setBillerOUSwitchingFeeTax(Long billerOUSwitchingFeeTax) {
		this.billerOUSwitchingFeeTax = billerOUSwitchingFeeTax;
	}
	
}