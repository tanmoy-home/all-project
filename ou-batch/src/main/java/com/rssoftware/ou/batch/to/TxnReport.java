package com.rssoftware.ou.batch.to;

import java.math.BigDecimal;


public class TxnReport {
	private static final long serialVersionUID = 2106312078100122851L;
	
	private String bbpouName;
	private BigDecimal noOfAgentOutlets;
	private BigDecimal onUsTxnCount;
	private BigDecimal offUsTxnCount;
	private BigDecimal onUsTxnTot;
	private BigDecimal offUsTxnTot;
	private BigDecimal onUsFailedTxnCount;
	private BigDecimal offUsFailedTxnCount;
	private BigDecimal onUsFailedTxnTot;
	private BigDecimal offUsFailedTxnTot;
	private String failureReason;
	private BigDecimal cashPaymentCount;
	private BigDecimal DCCCPaymentCount;
	private BigDecimal netBankingPaymentCount;
	private BigDecimal IMPSPaymentCount;
	private BigDecimal PPIsPaymentCount;
	private BigDecimal otherPaymentCount;
	
	public String getBbpouName() {
		return bbpouName;
	}
	public void setBbpouName(String bbpouName) {
		this.bbpouName = bbpouName;
	}
	public BigDecimal getNoOfAgentOutlets() {
		return noOfAgentOutlets;
	}
	public void setNoOfAgentOutlets(BigDecimal noOfAgentOutlets) {
		this.noOfAgentOutlets = noOfAgentOutlets;
	}
	public BigDecimal getOnUsTxnCount() {
		return onUsTxnCount;
	}
	public void setOnUsTxnCount(BigDecimal onUsTxnCount) {
		this.onUsTxnCount = onUsTxnCount;
	}
	public BigDecimal getOffUsTxnCount() {
		return offUsTxnCount;
	}
	public void setOffUsTxnCount(BigDecimal offUsTxnCount) {
		this.offUsTxnCount = offUsTxnCount;
	}
	public BigDecimal getOnUsTxnTot() {
		return onUsTxnTot;
	}
	public void setOnUsTxnTot(BigDecimal onUsTxnTot) {
		this.onUsTxnTot = onUsTxnTot;
	}
	public BigDecimal getOffUsTxnTot() {
		return offUsTxnTot;
	}
	public void setOffUsTxnTot(BigDecimal offUsTxnTot) {
		this.offUsTxnTot = offUsTxnTot;
	}
	public BigDecimal getOnUsFailedTxnCount() {
		return onUsFailedTxnCount;
	}
	public void setOnUsFailedTxnCount(BigDecimal onUsFailedTxnCount) {
		this.onUsFailedTxnCount = onUsFailedTxnCount;
	}
	public BigDecimal getOffUsFailedTxnCount() {
		return offUsFailedTxnCount;
	}
	public void setOffUsFailedTxnCount(BigDecimal offUsFailedTxnCount) {
		this.offUsFailedTxnCount = offUsFailedTxnCount;
	}
	public BigDecimal getOnUsFailedTxnTot() {
		return onUsFailedTxnTot;
	}
	public void setOnUsFailedTxnTot(BigDecimal onUsFailedTxnTot) {
		this.onUsFailedTxnTot = onUsFailedTxnTot;
	}
	public BigDecimal getOffUsFailedTxnTot() {
		return offUsFailedTxnTot;
	}
	public void setOffUsFailedTxnTot(BigDecimal offUsFailedTxnTot) {
		this.offUsFailedTxnTot = offUsFailedTxnTot;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public BigDecimal getCashPaymentCount() {
		return cashPaymentCount;
	}
	public void setCashPaymentCount(BigDecimal cashPaymentCount) {
		this.cashPaymentCount = cashPaymentCount;
	}
	public BigDecimal getDCCCPaymentCount() {
		return DCCCPaymentCount;
	}
	public void setDCCCPaymentCount(BigDecimal dCCCPaymentCount) {
		DCCCPaymentCount = dCCCPaymentCount;
	}
	public BigDecimal getNetBankingPaymentCount() {
		return netBankingPaymentCount;
	}
	public void setNetBankingPaymentCount(BigDecimal netBankingPaymentCount) {
		this.netBankingPaymentCount = netBankingPaymentCount;
	}
	public BigDecimal getIMPSPaymentCount() {
		return IMPSPaymentCount;
	}
	public void setIMPSPaymentCount(BigDecimal iMPSPaymentCount) {
		IMPSPaymentCount = iMPSPaymentCount;
	}
	public BigDecimal getPPIsPaymentCount() {
		return PPIsPaymentCount;
	}
	public void setPPIsPaymentCount(BigDecimal pPIsPaymentCount) {
		PPIsPaymentCount = pPIsPaymentCount;
	}
	public BigDecimal getOtherPaymentCount() {
		return otherPaymentCount;
	}
	public void setOtherPaymentCount(BigDecimal otherPaymentCount) {
		this.otherPaymentCount = otherPaymentCount;
	}
	
}