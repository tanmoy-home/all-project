package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the RAW_DATA database table.
 * 
 */
@Entity
@Table(name="RAW_DATA")
@NamedQuery(name="RawData.findAll", query="SELECT r FROM RawData r")
public class RawData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="MSG_ID")
	private String msgId;

	@EmbeddedId
	private RawDataPK id;
	
	@Column(name="MTI")
	private String mti;

	@Column(name="TXN_REFERENCE_ID")
	private String txnReferenceId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TXN_DATE")
	private Date txnDate;
	
	@Column(name="CUSTOMER_OU_ID")
	private String customerOuId;
	
	@Column(name="BILLER_OU_ID")
	private String billerOuId;
	
	@Column(name="AGENT_ID")
	private String agentId;
	
	@Column(name="RESPONSE_CODE")
	private String responseCode;
	
	@Column(name="TXN_CURRENCY_CODE")
	private String txnCurrencyCode;
	
	@Column(name="TXN_AMOUNT")
	private BigDecimal txnAmount;
	
	@Column(name="CUSTOMER_CONVENIENCE_FEE")
	private BigDecimal customerConvenienceFee;
	
	@Column(name="CUSTOMER_OU_SWITCHING_FEE")
	private BigDecimal customerOuSwitchingFee;
	
	@Column(name="BILLER_FEE")
	private BigDecimal billerFee;
	
	@Column(name="CLEARING_TIMESTAMP")
	private Timestamp clearingTimestamp;
	
	@Column(name="PAYMENT_CHANNEL")
	private String paymentChannel;

	@Column(name="PAYMENT_MODE")
	private String paymentMode;
	
	@Column(name="CUSTOMER_OU_COUNT_MONTH")
	private BigDecimal customerOuCountMonth;
	
	@Column(name="BILLER_ID")
	private String billerId;
	
	@Column(name="BILLER_CATEGORY")
	private String billerCategory;

	@Type(type= "org.hibernate.type.NumericBooleanType")
	@Column(name="SPLIT_PAY")
	private Boolean splitPay;

	@Column(name="SPLIT_PAY_TXN_AMOUNT")
	private BigDecimal splitPayTxnAmount;

	@Column(name="SPLIT_PAYMENT_MODE")
	private String splitPaymentMode;
	
	@Column(name="CUSTOMER_MOBILE_NUMBER")
	private String customerMobileNumber;
	
	@Type(type= "org.hibernate.type.NumericBooleanType")
	@Column(name="REVERSAL")
	private Boolean reversal;

	@Type(type= "org.hibernate.type.NumericBooleanType")
	@Column(name="DECLINE")
	private Boolean decline;

	@Type(type= "org.hibernate.type.NumericBooleanType")
	@Column(name="CAS_PROCESSED")
	private Boolean casProcessed;
	
	@Column(name="SETTLEMENT_CYCLE_ID")
	private String settlementCycleId;
	
	@Column(name="CUSTOMER_CONVENIENCE_FEE_TAX")
	private Long customerConvenienceFeeTax;
	
	@Column(name="BILLER_FEE_TAX")
	private Long billerFeeTax;
	
	@Column(name="CUSTOMER_OU_SWITCHING_FEE_TAX")
	private Long customerOUSwitchingFeeTax;
	
	@Column(name="SERVICE_FEE")
	private Long serviceFee;
	
	@Column(name="SERVICE_FEE_DESCRIPTION")
	private String serviceFeeDescription;
	
	@Column(name="SERVICE_FEE_TAX")
	private Long serviceFeeTax;
	
	@Column(name="BILLER_OU_SWITCHING_FEE")
	private BigDecimal billerOuSwitchingFee;

	@Column(name="BILLER_OU_COUNT_MONTH")
	private BigDecimal billerOuCountMonth;
	
	@Column(name="BILLER_OU_SWITCHING_FEE_TAX")
	private BigDecimal billerOuSwitchingFeeTax;

	/*@Column(name="BILLER_OU_INTERCHANGE_FEE")
	private BigDecimal billerOuInterchangeFee;

	@Column(name="CUSTOMER_OU_INTERCHANGE_FEE")
	private BigDecimal customerOuInterchangeFee;
	*/
	
	@Column(name="RECON_DESCRIPTION")
	private String reconDescription;

	@Column(name="RECON_STATUS")
	private String reconStatus;

	@Column(name="RECON_TS")
	private Timestamp reconTs;

	@Column(name="OU_TYPE")
	private String ouType;
	
	public RawData() {
	}

	public RawDataPK getId() {
		return this.id;
	}

	public void setId(RawDataPK id) {
		this.id = id;
	}

	public String getAgentId() {
		return this.agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getBillerCategory() {
		return this.billerCategory;
	}

	public void setBillerCategory(String billerCategory) {
		this.billerCategory = billerCategory;
	}

	public String getBillerId() {
		return this.billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public BigDecimal getBillerOuCountMonth() {
		return this.billerOuCountMonth;
	}

	public void setBillerOuCountMonth(BigDecimal billerOuCountMonth) {
		this.billerOuCountMonth = billerOuCountMonth;
	}

	public String getBillerOuId() {
		return this.billerOuId;
	}

	public void setBillerOuId(String billerOuId) {
		this.billerOuId = billerOuId;
	}

	/*public BigDecimal getBillerOuInterchangeFee() {
		return this.billerOuInterchangeFee;
	}

	public void setBillerOuInterchangeFee(BigDecimal billerOuInterchangeFee) {
		this.billerOuInterchangeFee = billerOuInterchangeFee;
	}
*/
	public BigDecimal getBillerOuSwitchingFee() {
		return this.billerOuSwitchingFee;
	}

	public void setBillerOuSwitchingFee(BigDecimal billerOuSwitchingFee) {
		this.billerOuSwitchingFee = billerOuSwitchingFee;
	}

	public Boolean getCasProcessed() {
		return this.casProcessed;
	}

	public void setCasProcessed(Boolean casProcessed) {
		this.casProcessed = casProcessed;
	}

	public Timestamp getClearingTimestamp() {
		return this.clearingTimestamp;
	}

	public void setClearingTimestamp(Timestamp clearingTimestamp) {
		this.clearingTimestamp = clearingTimestamp;
	}

	public BigDecimal getCustomerConvenienceFee() {
		return this.customerConvenienceFee;
	}

	public void setCustomerConvenienceFee(BigDecimal customerConvenienceFee) {
		this.customerConvenienceFee = customerConvenienceFee;
	}

	public String getCustomerMobileNumber() {
		return this.customerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}

	public BigDecimal getCustomerOuCountMonth() {
		return this.customerOuCountMonth;
	}

	public void setCustomerOuCountMonth(BigDecimal customerOuCountMonth) {
		this.customerOuCountMonth = customerOuCountMonth;
	}

	public String getCustomerOuId() {
		return this.customerOuId;
	}

	public void setCustomerOuId(String customerOuId) {
		this.customerOuId = customerOuId;
	}

	/*public BigDecimal getCustomerOuInterchangeFee() {
		return this.customerOuInterchangeFee;
	}

	public void setCustomerOuInterchangeFee(BigDecimal customerOuInterchangeFee) {
		this.customerOuInterchangeFee = customerOuInterchangeFee;
	}*/

	public BigDecimal getCustomerOuSwitchingFee() {
		return this.customerOuSwitchingFee;
	}

	public void setCustomerOuSwitchingFee(BigDecimal customerOuSwitchingFee) {
		this.customerOuSwitchingFee = customerOuSwitchingFee;
	}

	public Boolean getDecline() {
		return this.decline;
	}

	public void setDecline(Boolean decline) {
		this.decline = decline;
	}

	public String getMsgId() {
		return this.msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getMti() {
		return this.mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}

	public String getPaymentChannel() {
		return this.paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getPaymentMode() {
		return this.paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getReconDescription() {
		return this.reconDescription;
	}

	public void setReconDescription(String reconDescription) {
		this.reconDescription = reconDescription;
	}

	public String getReconStatus() {
		return this.reconStatus;
	}

	public void setReconStatus(String reconStatus) {
		this.reconStatus = reconStatus;
	}

	public Timestamp getReconTs() {
		return this.reconTs;
	}

	public void setReconTs(Timestamp reconTs) {
		this.reconTs = reconTs;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Boolean getReversal() {
		return this.reversal;
	}

	public void setReversal(Boolean reversal) {
		this.reversal = reversal;
	}

	public String getSettlementCycleId() {
		return this.settlementCycleId;
	}

	public void setSettlementCycleId(String settlementCycleId) {
		this.settlementCycleId = settlementCycleId;
	}

	public Boolean getSplitPay() {
		return this.splitPay;
	}

	public void setSplitPay(Boolean splitPay) {
		this.splitPay = splitPay;
	}

	public BigDecimal getSplitPayTxnAmount() {
		return this.splitPayTxnAmount;
	}

	public void setSplitPayTxnAmount(BigDecimal splitPayTxnAmount) {
		this.splitPayTxnAmount = splitPayTxnAmount;
	}

	public String getSplitPaymentMode() {
		return this.splitPaymentMode;
	}

	public void setSplitPaymentMode(String splitPaymentMode) {
		this.splitPaymentMode = splitPaymentMode;
	}

	public BigDecimal getTxnAmount() {
		return this.txnAmount;
	}

	public void setTxnAmount(BigDecimal txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTxnCurrencyCode() {
		return this.txnCurrencyCode;
	}

	public void setTxnCurrencyCode(String txnCurrencyCode) {
		this.txnCurrencyCode = txnCurrencyCode;
	}

	public Date getTxnDate() {
		return this.txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getTxnReferenceId() {
		return this.txnReferenceId;
	}

	public void setTxnReferenceId(String txnReferenceId) {
		this.txnReferenceId = txnReferenceId;
	}

	public BigDecimal getBillerFee() {
		return billerFee;
	}

	public void setBillerFee(BigDecimal billerFee) {
		this.billerFee = billerFee;
	}

	public Long getCustomerConvenienceFeeTax() {
		return customerConvenienceFeeTax;
	}

	public void setCustomerConvenienceFeeTax(Long customerConvenienceFeeTax) {
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

	public BigDecimal getBillerOuSwitchingFeeTax() {
		return billerOuSwitchingFeeTax;
	}

	public void setBillerOuSwitchingFeeTax(BigDecimal billerOuSwitchingFeeTax) {
		this.billerOuSwitchingFeeTax = billerOuSwitchingFeeTax;
	}

	public String getOuType() {
		return ouType;
	}

	public void setOuType(String ouType) {
		this.ouType = ouType;
	}
}
