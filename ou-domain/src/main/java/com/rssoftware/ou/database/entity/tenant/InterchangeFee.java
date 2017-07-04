package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

// Entity implementation class for Entity: InterchangeFee

@Entity 
@Table(name="INTERCHANGE_FEE",uniqueConstraints = {
		@UniqueConstraint(columnNames = "FEE_ID")})

public class InterchangeFee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="FEE_ID")
	private long feeId;
	
	@Column(name="BLR_ID")
	private String billerId;
	
	@Column(name="FEE_CODE")
	private String feeCode;
	
	@Column(name="FEE_DESCRIPTION")
	private String feeDesc;
	
	@Column(name="FEE_DIRECTION")
	private String feeDirection;
	
	@Column(name="TRAN_AMT_RANGE_MIN")
	private Long tranAmtRangeMin;
	
	@Column(name="TRAN_AMT_RANGE_MAX")
	private Long tranAmtRangeMax;
	
	@Column(name="PERCENT_FEE")
	private BigDecimal percentFee;
	
	@Column(name="FLAT_FEE")
	private BigDecimal flatFee;
	
	@Column(name="EFFCTV_FROM")
	private String effctvFrom;
	
	@Column(name="EFFCTV_TO")
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

	public String getFeeDirection() {
		return feeDirection;
	}
	public void setFeeDirection(String feeDirection) {
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

	public InterchangeFee(int feeId, String billerId, String feeCode, String feeDesc, String feeDirection) {
		super();
		this.feeId = feeId;
		this.billerId = billerId;
		this.feeCode = feeCode;
		this.feeDesc = feeDesc;
		this.feeDirection = feeDirection;
	}
	
	public InterchangeFee() {
		super();
	}

}
