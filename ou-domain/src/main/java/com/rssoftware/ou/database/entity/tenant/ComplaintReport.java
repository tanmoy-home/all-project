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
 * The persistent class for the COMPLAINT_REPORT database table.
 * 
 */
@Entity
@Table(name="COMPLAINT_REPORT")
@NamedQuery(name="ComplaintReport.findAll", query="SELECT c FROM ComplaintReport c")
public class ComplaintReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@SequenceGenerator(name="complaint_report_seq_gen",sequenceName="COMPLAINT_REPORT_ID_SEQ")        
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="complaint_report_seq_gen")
	@Column(name="REPORT_ID", unique=true, nullable=false)
	private Long reportId;

	@Column(name="OU_NAME")
	private String bbpouName;
	
	@Column(name="onUs_outstanding_lastweek")
	private BigDecimal onUsoutstandingLastWeekCount;
	
	@Column(name="onUs_received")
	private BigDecimal OnUsreceivedThisWeekCount;

	@Column(name="onUs_tot")
	private BigDecimal onUsTot;

	@Column(name="offUs_outstanding_lastweek")
	private BigDecimal offUsoutstandingLastWeekCount;
	
	@Column(name="offUs_received")
	private BigDecimal OffUsreceivedThisWeekCount;
	
	@Column(name="offUs_tot")
	private BigDecimal offUsTot;
	
	@Column(name="onUs_resolved")
	private BigDecimal onUsResolvedCount;
	
	@Column(name="offUs_resolved")
	private BigDecimal offUsResolvedCount;
	
	@Column(name="onUs_pending")
	private BigDecimal onUsPendingCount;
	
	@Column(name="offUs_pending")
	private BigDecimal offUsPendingCount;
	
	@Column(name="txn_based")
	private BigDecimal txnBasedCount;
	
	@Column(name="service_based")
	private BigDecimal serviceBasedCount;
	
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

	public BigDecimal getOnUsoutstandingLastWeekCount() {
		return onUsoutstandingLastWeekCount;
	}

	public void setOnUsoutstandingLastWeekCount(
			BigDecimal onUsoutstandingLastWeekCount) {
		this.onUsoutstandingLastWeekCount = onUsoutstandingLastWeekCount;
	}

	public BigDecimal getOnUsreceivedThisWeekCount() {
		return OnUsreceivedThisWeekCount;
	}

	public void setOnUsreceivedThisWeekCount(BigDecimal onUsreceivedThisWeekCount) {
		OnUsreceivedThisWeekCount = onUsreceivedThisWeekCount;
	}

	public BigDecimal getOnUsTot() {
		return onUsTot;
	}

	public void setOnUsTot(BigDecimal onUsTot) {
		this.onUsTot = onUsTot;
	}

	public BigDecimal getOffUsoutstandingLastWeekCount() {
		return offUsoutstandingLastWeekCount;
	}

	public void setOffUsoutstandingLastWeekCount(
			BigDecimal offUsoutstandingLastWeekCount) {
		this.offUsoutstandingLastWeekCount = offUsoutstandingLastWeekCount;
	}

	public BigDecimal getOffUsreceivedThisWeekCount() {
		return OffUsreceivedThisWeekCount;
	}

	public void setOffUsreceivedThisWeekCount(BigDecimal offUsreceivedThisWeekCount) {
		OffUsreceivedThisWeekCount = offUsreceivedThisWeekCount;
	}

	public BigDecimal getOffUsTot() {
		return offUsTot;
	}

	public void setOffUsTot(BigDecimal offUsTot) {
		this.offUsTot = offUsTot;
	}

	public BigDecimal getOnUsResolvedCount() {
		return onUsResolvedCount;
	}

	public void setOnUsResolvedCount(BigDecimal onUsResolvedCount) {
		this.onUsResolvedCount = onUsResolvedCount;
	}

	public BigDecimal getOffUsResolvedCount() {
		return offUsResolvedCount;
	}

	public void setOffUsResolvedCount(BigDecimal offUsResolvedCount) {
		this.offUsResolvedCount = offUsResolvedCount;
	}

	public BigDecimal getOnUsPendingCount() {
		return onUsPendingCount;
	}

	public void setOnUsPendingCount(BigDecimal onUsPendingCount) {
		this.onUsPendingCount = onUsPendingCount;
	}

	public BigDecimal getOffUsPendingCount() {
		return offUsPendingCount;
	}

	public void setOffUsPendingCount(BigDecimal offUsPendingCount) {
		this.offUsPendingCount = offUsPendingCount;
	}

	public BigDecimal getTxnBasedCount() {
		return txnBasedCount;
	}

	public void setTxnBasedCount(BigDecimal txnBasedCount) {
		this.txnBasedCount = txnBasedCount;
	}

	public BigDecimal getServiceBasedCount() {
		return serviceBasedCount;
	}

	public void setServiceBasedCount(BigDecimal serviceBasedCount) {
		this.serviceBasedCount = serviceBasedCount;
	}

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}
	
}
