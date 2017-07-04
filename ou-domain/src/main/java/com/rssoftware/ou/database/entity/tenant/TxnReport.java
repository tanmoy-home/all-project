package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the TXN_REPORT database table.
 * 
 */
@Entity
@Table(name="TXN_REPORT")
@NamedQuery(name="TxnReport.findAll", query="SELECT t FROM TxnReport t")
public class TxnReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="txn_report_seq_gen",sequenceName="TXN_REPORT_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="txn_report_seq_gen")
	@Column(name="REPORT_ID", unique=true, nullable=false)
	private Long reportId;

	@Column(name="OU_NAME")
	private String bbpouName;
	
	@Column(name="NO_OF_AGENT_OUTLETS")
	private BigDecimal noOfAgentOutlets;
	
	@Column(name="onUs_TxnCount")
	private BigDecimal onUsTxnCount;
	
	@Column(name="offUs_TxnCount")
	private BigDecimal offUsTxnCount;
	
	@Column(name="onUs_TxnTot")
	private BigDecimal onUsTxnTot;
	
	@Column(name="offUs_TxnTot")
	private BigDecimal offUsTxnTot;
	
	@Column(name="onUs_FailedTxnCount")
	private BigDecimal onUsFailedTxnCount;
	
	@Column(name="offUs_FailedTxnCount")
	private BigDecimal offUsFailedTxnCount;
	
	@Column(name="onUs_FailedTxnTot")
	private BigDecimal onUsFailedTxnTot;
	
	@Column(name="offUs_FailedTxnTot")
	private BigDecimal offUsFailedTxnTot;
	
	@Column(name="failure_Reason")
	private String failureReason;
	
	@Column(name="cash_PaymentCount")
	private BigDecimal cashPaymentCount;
	
	@Column(name="DCCC_PaymentCount")
	private BigDecimal DCCCPaymentCount;
	
	@Column(name="netBanking_PaymentCount")
	private BigDecimal netBankingPaymentCount;
	
	@Column(name="IMPS_PaymentCount")
	private BigDecimal IMPSPaymentCount;
	
	@Column(name="PPIs_PaymentCount")
	private BigDecimal PPIsPaymentCount;
	
	@Column(name="other_PaymentCount")
	private BigDecimal otherPaymentCount;
	
	@Column(name="crtn_Ts")
	private Timestamp crtnTs;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

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

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

}
