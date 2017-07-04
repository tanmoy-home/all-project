package com.rssoftware.ou.common;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ComplaintReportResp")
public class ComplaintReportResp extends BaseDownloadReq {
	
	private String bbpouName;
	private BigDecimal onUsoutstandingLastWeekCount;
	private BigDecimal OnUsreceivedThisWeekCount;
	private BigDecimal onUsTot;

	private BigDecimal offUsoutstandingLastWeekCount;
	private BigDecimal OffUsreceivedThisWeekCount;
	private BigDecimal offUsTot;
	
	private BigDecimal onUsResolvedCount;
	private BigDecimal offUsResolvedCount;
	
	private BigDecimal onUsPendingCount;
	private BigDecimal offUsPendingCount;
	

	private BigDecimal txnBasedCount;
	private BigDecimal serviceBasedCount;
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
}