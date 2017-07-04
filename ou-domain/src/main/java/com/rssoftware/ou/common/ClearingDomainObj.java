package com.rssoftware.ou.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeBreakup;

public class ClearingDomainObj implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -663539530736132845L;
	private String refId;
	private RequestType requestType;
	private MTI mti;

	private String txnRefId;
	private String txnDate;
	private String custOUId;
	private String billerOUId;
	private String agentId;
	private String responseCode;
	private String originalResponseCode;
	private String currencyCode;
	private Long txnAmount;
	private PaymentMode paymentMode; // main payment mode - in case of splitPay the
								// split pay amount will be set to this
	private boolean splitPay; // flag to denote that payment is split pay
	private PaymentMode splitPaymentMode; // this will be used to set the first
										// payment mode
	private Long splitPayTxnAmount;// this will be used to set the first payment
									// amount

	private List<PaymentModeBreakup> paymentModeBreakups;
	private BigDecimal billerOUCCF;
	private BigDecimal custOUSwitchFee;
	private BigDecimal custOUBF;
	private BigDecimal billerOUSwitchFee;
	private String clearingDate;
	private Timestamp clearingTs;
	private PaymentChannel paymentChannel;

	private String bilrCatName;
	private String blrId;
	private String yearMonth; // not to be persisited
	private ClearingFeeObj feeBreakups; // a JSON String reprenting all
										// interchange fees details for
										// settlement reports
	private String settlementCycleId;

	private long customerOuMonthlyCount;
	private long billerOuMonthlyCount;
	
	boolean reversal = false;
	
	private String disputeId = null;
	private String disputeType = null;
	
	private Timestamp crtnTs;
	private Timestamp lastUpdTs;
	
	private String goodFaithType = null;
	private Long goodFaithAmount = null;
	private String goodFaithAssignedInst = null;
	
	//private Transaction transaction;
	
	private final Map<Integer, Set<String>> paymentModeIndexFees = new HashMap<Integer, Set<String>>();
	
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public RequestType getRequestType() {
		return requestType;
	}
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	public MTI getMti() {
		return mti;
	}
	public void setMti(MTI mti) {
		this.mti = mti;
	}
	public String getTxnRefId() {
		return txnRefId;
	}
	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getCustOUId() {
		return custOUId;
	}
	public void setCustOUId(String custOUId) {
		this.custOUId = custOUId;
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
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public Long getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(Long txnAmount) {
		this.txnAmount = txnAmount;
	}
	public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}
	public boolean isSplitPay() {
		return splitPay;
	}
	public void setSplitPay(boolean splitPay) {
		this.splitPay = splitPay;
	}
	public PaymentMode getSplitPaymentMode() {
		return splitPaymentMode;
	}
	public void setSplitPaymentMode(PaymentMode splitPaymentMode) {
		this.splitPaymentMode = splitPaymentMode;
	}
	public Long getSplitPayTxnAmount() {
		return splitPayTxnAmount;
	}
	public void setSplitPayTxnAmount(Long splitPayTxnAmount) {
		this.splitPayTxnAmount = splitPayTxnAmount;
	}
	public List<PaymentModeBreakup> getPaymentModeBreakups() {
		return paymentModeBreakups;
	}
	public void setPaymentModeBreakups(List<PaymentModeBreakup> paymentModeBreakups) {
		this.paymentModeBreakups = paymentModeBreakups;
	}
	public BigDecimal getCustOUSwitchFee() {
		return custOUSwitchFee;
	}
	public void setCustOUSwitchFee(BigDecimal custOUSwitchFee) {
		this.custOUSwitchFee = custOUSwitchFee;
	}
	public BigDecimal getBillerOUSwitchFee() {
		return billerOUSwitchFee;
	}
	public void setBillerOUSwitchFee(BigDecimal billerOUSwitchFee) {
		this.billerOUSwitchFee = billerOUSwitchFee;
	}
	public String getClearingDate() {
		return clearingDate;
	}
	public void setClearingDate(String clearingDate) {
		this.clearingDate = clearingDate;
	}
	public Timestamp getClearingTs() {
		return clearingTs;
	}
	public void setClearingTs(Timestamp clearingTs) {
		this.clearingTs = clearingTs;
	}
	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getBilrCatName() {
		return bilrCatName;
	}
	public void setBilrCatName(String bilrCatName) {
		this.bilrCatName = bilrCatName;
	}
	public String getBlrId() {
		return blrId;
	}
	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getSettlementCycleId() {
		return settlementCycleId;
	}
	public void setSettlementCycleId(String settlementCycleId) {
		this.settlementCycleId = settlementCycleId;
	}
	public long getCustomerOuMonthlyCount() {
		return customerOuMonthlyCount;
	}
	public void setCustomerOuMonthlyCount(long customerOuMonthlyCount) {
		this.customerOuMonthlyCount = customerOuMonthlyCount;
	}
	public long getBillerOuMonthlyCount() {
		return billerOuMonthlyCount;
	}
	public void setBillerOuMonthlyCount(long billerOuMonthlyCount) {
		this.billerOuMonthlyCount = billerOuMonthlyCount;
	}
	public ClearingFeeObj getFeeBreakups() {
		if (feeBreakups == null){
			feeBreakups = new ClearingFeeObj();
		}
		return feeBreakups;
	}
	public void setFeeBreakups(ClearingFeeObj feeBreakups) {
		this.feeBreakups = feeBreakups;
	}

	public boolean isReversal() {
		return reversal;
	}
	public void setReversal(boolean reversal) {
		this.reversal = reversal;
	}
	public Map<Integer, Set<String>> getPaymentModeIndexFees() {
		return paymentModeIndexFees;
	}
	public String getDisputeId() {
		return disputeId;
	}
	public void setDisputeId(String disputeId) {
		this.disputeId = disputeId;
	}
	public String getDisputeType() {
		return disputeType;
	}
	public void setDisputeType(String disputeType) {
		this.disputeType = disputeType;
	}
	public String getOriginalResponseCode() {
		return originalResponseCode;
	}
	public void setOriginalResponseCode(String originalResponseCode) {
		this.originalResponseCode = originalResponseCode;
	}
	public Timestamp getCrtnTs() {
		return crtnTs;
	}
	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}
	public Timestamp getLastUpdTs() {
		return lastUpdTs;
	}
	public void setLastUpdTs(Timestamp lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}
	public String getGoodFaithType() {
		return goodFaithType;
	}
	public void setGoodFaithType(String goodFaithType) {
		this.goodFaithType = goodFaithType;
	}
	public Long getGoodFaithAmount() {
		return goodFaithAmount;
	}
	public void setGoodFaithAmount(Long goodFaithAmount) {
		this.goodFaithAmount = goodFaithAmount;
	}
	public String getGoodFaithAssignedInst() {
		return goodFaithAssignedInst;
	}
	public void setGoodFaithAssignedInst(String goodFaithAssignedInst) {
		this.goodFaithAssignedInst = goodFaithAssignedInst;
	}
	public BigDecimal getBillerOUCCF() {
		return billerOUCCF;
	}
	public void setBillerOUCCF(BigDecimal billerOUCCF) {
		this.billerOUCCF = billerOUCCF;
	}
	public BigDecimal getCustOUBF() {
		return custOUBF;
	}
	public void setCustOUBF(BigDecimal custOUBF) {
		this.custOUBF = custOUBF;
	}
	@Override
	public String toString() {
		return "ClearingDomainObj [refId=" + refId + ", requestType="
				+ requestType + ", mti=" + mti + ", txnRefId=" + txnRefId
				+ ", txnDate=" + txnDate + ", custOUId=" + custOUId
				+ ", billerOUId=" + billerOUId + ", agentId=" + agentId
				+ ", responseCode=" + responseCode + ", originalResponseCode="
				+ originalResponseCode + ", currencyCode=" + currencyCode
				+ ", txnAmount=" + txnAmount + ", paymentMode=" + paymentMode
				+ ", splitPay=" + splitPay + ", splitPaymentMode="
				+ splitPaymentMode + ", splitPayTxnAmount=" + splitPayTxnAmount
				+ ", paymentModeBreakups=" + paymentModeBreakups
				+ ", billerOUCCF=" + billerOUCCF + ", custOUSwitchFee="
				+ custOUSwitchFee + ", custOUBF=" + custOUBF
				+ ", billerOUSwitchFee=" + billerOUSwitchFee
				+ ", clearingDate=" + clearingDate + ", clearingTs="
				+ clearingTs + ", paymentChannel=" + paymentChannel
				+ ", bilrCatName=" + bilrCatName + ", blrId=" + blrId
				+ ", yearMonth=" + yearMonth + ", feeBreakups=" + feeBreakups
				+ ", settlementCycleId=" + settlementCycleId
				+ ", customerOuMonthlyCount=" + customerOuMonthlyCount
				+ ", billerOuMonthlyCount=" + billerOuMonthlyCount
				+ ", reversal=" + reversal + ", disputeId=" + disputeId
				+ ", disputeType=" + disputeType + ", crtnTs=" + crtnTs
				+ ", lastUpdTs=" + lastUpdTs + ", goodFaithType="
				+ goodFaithType + ", goodFaithAmount=" + goodFaithAmount
				+ ", goodFaithAssignedInst=" + goodFaithAssignedInst
				+ ", paymentModeIndexFees="
				+ paymentModeIndexFees + "]";
	}
	
	
}