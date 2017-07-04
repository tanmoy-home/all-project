package com.rssoftware.ou.model.tenant;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReconSummaryView {

	private String reconId;
	private BigDecimal matchedCount;
	private BigDecimal notInOUCount;
	private BigDecimal notInCUCount;
	private BigDecimal mismatchedCount;
	private BigDecimal pendingCount;
	private BigDecimal broughtForwardCount;
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
