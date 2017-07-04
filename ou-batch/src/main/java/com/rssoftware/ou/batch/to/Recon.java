package com.rssoftware.ou.batch.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Recon")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)

public class Recon implements Serializable{
	
	private static final long serialVersionUID = 2106312078100122851L;
	private String rawRefId;
	private String rawTxnType;
	private String rawMsgId;
	private String mti;
	private String txnReferenceId;
	private Date txnDate;
	private String customerOUId;
	private String billerOUId;
	private String agentId;
	private String responseCode;
	private String txnCurrencyCode;
	private BigDecimal txnAmount;

	private BigDecimal customerOUInterchangeFee;
	private BigDecimal customerOUSwitchingFee;
	private BigDecimal billerOUInterchangeFee;
	private BigDecimal billerOUSwitchingFee;
	private BigDecimal customerConvenienceFee;
	private String paymentChannel;
	private String paymentMode;
	private BigDecimal customerOUCountMonth;
	private BigDecimal billerOUCountMonth;
	private String billerId;
	private String billerCategory;
	private Boolean splitPay;
	private String splitPaymentMode;
	private BigDecimal splitPayTxnAmount;
	private String customerMobileNUmber;
	private Boolean reversal;
	private Boolean decline;
	private Boolean casProcessed;
	private String settlementCycleId;
	private String rawReconStatus;
	private String billerFee;
	
	
	private String refId;
	private String txnType;
	private String msgId;
	private String txnRefId;
	private byte[] requestJson;
	private byte[] responseJson;
	private String currentStatus;
	private String reconStatus;
	private BigDecimal reconCycleNo;
	
	private BigDecimal customerConvenienceFeeTax;
	private Long billerFeeTax;
	private Long customerOUSwitchingFeeTax;
	private Long billerOUSwitchingFeeTax;

	public String getRawRefId() {
		return rawRefId;
	}
	public void setRawRefId(String rawRefId) {
		this.rawRefId = rawRefId;
	}
	public String getRawTxnType() {
		return rawTxnType;
	}
	public void setRawTxnType(String rawTxnType) {
		this.rawTxnType = rawTxnType;
	}
	public String getRawMsgId() {
		return rawMsgId;
	}
	public void setRawMsgId(String rawMsgId) {
		this.rawMsgId = rawMsgId;
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
	public Date getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(Date txnDate) {
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
	public BigDecimal getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(BigDecimal txnAmount) {
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
	public BigDecimal getCustomerConvenienceFee() {
		return customerConvenienceFee;
	}
	public void setCustomerConvenienceFee(BigDecimal customerConvenienceFee) {
		this.customerConvenienceFee = customerConvenienceFee;
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
	public BigDecimal getCustomerOUCountMonth() {
		return customerOUCountMonth;
	}
	public void setCustomerOUCountMonth(BigDecimal customerOUCountMonth) {
		this.customerOUCountMonth = customerOUCountMonth;
	}
	public BigDecimal getBillerOUCountMonth() {
		return billerOUCountMonth;
	}
	public void setBillerOUCountMonth(BigDecimal billerOUCountMonth) {
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
	public Boolean getSplitPay() {
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
	public BigDecimal getSplitPayTxnAmount() {
		return splitPayTxnAmount;
	}
	public void setSplitPayTxnAmount(BigDecimal splitPayTxnAmount) {
		this.splitPayTxnAmount = splitPayTxnAmount;
	}
	public String getCustomerMobileNUmber() {
		return customerMobileNUmber;
	}
	public void setCustomerMobileNUmber(String customerMobileNUmber) {
		this.customerMobileNUmber = customerMobileNUmber;
	}
	public Boolean getReversal() {
		return reversal;
	}
	public void setReversal(Boolean reversal) {
		this.reversal = reversal;
	}
	public Boolean getDecline() {
		return decline;
	}
	public void setDecline(Boolean decline) {
		this.decline = decline;
	}
	public Boolean getCasProcessed() {
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
	public String getRawReconStatus() {
		return rawReconStatus;
	}
	public void setRawReconStatus(String rawReconStatus) {
		this.rawReconStatus = rawReconStatus;
	}
	public String getBillerFee() {
		return billerFee;
	}
	public void setBillerFee(String billerFee) {
		this.billerFee = billerFee;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getTxnRefId() {
		return txnRefId;
	}
	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	public String getReconStatus() {
		return reconStatus;
	}
	public void setReconStatus(String reconStatus) {
		this.reconStatus = reconStatus;
	}
	public BigDecimal getReconCycleNo() {
		return reconCycleNo;
	}
	public void setReconCycleNo(BigDecimal reconCycleNo) {
		this.reconCycleNo = reconCycleNo;
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
	public byte[] getRequestJson() {
		return requestJson;
	}
	public void setRequestJson(byte[] requestJson) {
		this.requestJson = requestJson;
	}
	public byte[] getResponseJson() {
		return responseJson;
	}
	public void setResponseJson(byte[] responseJson) {
		this.responseJson = responseJson;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	
}
