package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the RECON_SUMMARY database table.
 * 
 */
@Entity
@Table(name = "RECON_SUMMARY")
@NamedQuery(name = "ReconSummary.findAll", query = "SELECT r FROM ReconSummary r")
public class ReconSummary implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "RECON_ID")
	private String reconId;

	@Column(name = "MATCHED")
	private BigDecimal matchedCount;

	@Column(name = "NOT_IN_OU")
	private BigDecimal notInOUCount;

	@Column(name = "NOT_IN_CU")
	private BigDecimal notInCUCount;

	@Column(name = "MISMATCHED")
	private BigDecimal mismatchedCount;

	@Column(name = "PENDING")
	private BigDecimal pendingCount;

	@Column(name = "BROUGHT_FORWARD")
	private BigDecimal broughtForwardCount;

	@Column(name = "RECON_TS")
	private Timestamp reconTs;

	public String getReconId() {
		return reconId;
	}

	public void setReconId(String reconId) {
		this.reconId = reconId;
	}

	public BigDecimal getMatchedCount() {
		return matchedCount;
	}

	public void setMatchedCount(BigDecimal matchedCount) {
		this.matchedCount = matchedCount;
	}

	public BigDecimal getNotInOUCount() {
		return notInOUCount;
	}

	public void setNotInOUCount(BigDecimal notInOUCount) {
		this.notInOUCount = notInOUCount;
	}

	public BigDecimal getNotInCUCount() {
		return notInCUCount;
	}

	public void setNotInCUCount(BigDecimal notInCUCount) {
		this.notInCUCount = notInCUCount;
	}

	public BigDecimal getMismatchedCount() {
		return mismatchedCount;
	}

	public void setMismatchedCount(BigDecimal mismatchedCount) {
		this.mismatchedCount = mismatchedCount;
	}

	public BigDecimal getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(BigDecimal pendingCount) {
		this.pendingCount = pendingCount;
	}

	public BigDecimal getBroughtForwardCount() {
		return broughtForwardCount;
	}

	public void setBroughtForwardCount(BigDecimal broughtForwardCount) {
		this.broughtForwardCount = broughtForwardCount;
	}

	public Timestamp getReconTs() {
		return reconTs;
	}

	public void setReconTs(Timestamp reconTs) {
		this.reconTs = reconTs;
	}
}