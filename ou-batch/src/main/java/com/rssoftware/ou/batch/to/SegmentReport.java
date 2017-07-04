package com.rssoftware.ou.batch.to;

import java.math.BigDecimal;


public class SegmentReport {
	
	private String bbpouName;
	private String blrCategory;
	private String blrName;
	
	private BigDecimal onUsCount;
	private BigDecimal offUsCount;

	private BigDecimal onUsTot;
	private BigDecimal offUsTot;
	public String getBbpouName() {
		return bbpouName;
	}
	public void setBbpouName(String bbpouName) {
		this.bbpouName = bbpouName;
	}
	public String getBlrCategory() {
		return blrCategory;
	}
	public void setBlrCategory(String blrCategory) {
		this.blrCategory = blrCategory;
	}
	public String getBlrName() {
		return blrName;
	}
	public void setBlrName(String blrName) {
		this.blrName = blrName;
	}
	public BigDecimal getOnUsCount() {
		return onUsCount;
	}
	public void setOnUsCount(BigDecimal onUsCount) {
		this.onUsCount = onUsCount;
	}
	public BigDecimal getOffUsCount() {
		return offUsCount;
	}
	public void setOffUsCount(BigDecimal offUsCount) {
		this.offUsCount = offUsCount;
	}
	public BigDecimal getOnUsTot() {
		return onUsTot;
	}
	public void setOnUsTot(BigDecimal onUsTot) {
		this.onUsTot = onUsTot;
	}
	public BigDecimal getOffUsTot() {
		return offUsTot;
	}
	public void setOffUsTot(BigDecimal offUsTot) {
		this.offUsTot = offUsTot;
	}

}