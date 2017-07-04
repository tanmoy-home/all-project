package com.rssoftware.ou.model.tenant;

import java.math.BigDecimal;

import com.rssoftware.ou.common.InterchangeFeeDirectionType;

public class InterchangeFeeView {
	
	private long feeId;
	private String billerId;
	private String feeCode;
	private String feeDesc;
	private InterchangeFeeDirectionType feeDirection;
	private Long tranAmtRangeMin;
	private Long tranAmtRangeMax;
	private BigDecimal percentFee;
	private BigDecimal flatFee;
	private String effctvFrom;
	private String effctvTo;	
	
	public long getFeeId() {
		return feeId;
	}
	public void setFeeId(long feeId) {
		this.feeId = feeId;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getFeeDesc() {
		return feeDesc;
	}
	public void setFeeDesc(String feeDesc) {
		this.feeDesc = feeDesc;
	}
	public InterchangeFeeDirectionType getFeeDirection() {
		return feeDirection;
	}
	public void setFeeDirection(InterchangeFeeDirectionType feeDirection) {
		this.feeDirection = feeDirection;
	}
	public Long getTranAmtRangeMin() {
		return tranAmtRangeMin;
	}
	public void setTranAmtRangeMin(Long tranAmtRangeMin) {
		this.tranAmtRangeMin = tranAmtRangeMin;
	}
	public Long getTranAmtRangeMax() {
		return tranAmtRangeMax;
	}
	public void setTranAmtRangeMax(Long tranAmtRangeMax) {
		this.tranAmtRangeMax = tranAmtRangeMax;
	}
	public BigDecimal getPercentFee() {
		return percentFee;
	}
	public void setPercentFee(BigDecimal percentFee) {
		this.percentFee = percentFee;
	}
	public BigDecimal getFlatFee() {
		return flatFee;
	}
	public void setFlatFee(BigDecimal flatFee) {
		this.flatFee = flatFee;
	}
	public String getEffctvFrom() {
		return effctvFrom;
	}
	public void setEffctvFrom(String effctvFrom) {
		this.effctvFrom = effctvFrom;
	}
	public String getEffctvTo() {
		return effctvTo;
	}
	public void setEffctvTo(String effctvTo) {
		this.effctvTo = effctvTo;
	}
}