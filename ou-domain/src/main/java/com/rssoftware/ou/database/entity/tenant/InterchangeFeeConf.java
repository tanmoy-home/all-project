package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

// Entity implementation class for Entity: InterchangeFeeConf

@Entity
@Table(name="INTERCHANGE_FEE_CONFIG")

public class InterchangeFeeConf implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="FEE_CFG_ID")
	private long feeCfgId;
	
	@Column(name="BLR_ID")
	private String blrId;
	
	@Column(name="MTI")
	private String mti;
	
	@Column(name="PAYMENT_MODE")
	private String paymentMode;
	
	@Column(name="PAYMENT_CHANNEL")
	private String paymentChannel;
	
	@Column(name="RESPONSE_CODE")
	private String responseCode;
	
	@Column(name="FEES")
	private String fees;
	
	@Column(name="EFFCTV_FROM")
	private String effectiveFrom;
	
	@Column(name="EFFCTV_TO")
	private String effectiveTo;
	
	@Column(name="IS_DEFAULT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isDefault;
	
	
	public long getFeeCfgId() {
		return feeCfgId;
	}
	public void setFeeCfgId(long feeCfgId) {
		this.feeCfgId = feeCfgId;
	}

	public String getBlrId() {
		return blrId;
	}
	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}

	public String getMti() {
		return mti;
	}
	public void setMti(String mti) {
		this.mti = mti;
	}

	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getFees() {
		return fees;
	}
	public void setFees(String fees) {
		this.fees = fees;
	}

	public String getEffectiveFrom() {
		return effectiveFrom;
	}
	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getEffectiveTo() {
		return effectiveTo;
	}
	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	
	public InterchangeFeeConf() {
		super();
	}

	public InterchangeFeeConf(long feeCfgId, String blrId, String mti, String paymentMode, String paymentChannel,
			String responseCode, String fees, String effectiveFrom, String effectiveTo, boolean isDefault) {
		super();
		this.feeCfgId = feeCfgId;
		this.blrId = blrId;
		this.mti = mti;
		this.paymentMode = paymentMode;
		this.paymentChannel = paymentChannel;
		this.responseCode = responseCode;
		this.fees = fees;
		this.effectiveFrom = effectiveFrom;
		this.effectiveTo = effectiveTo;
		this.isDefault = isDefault;
	}

}
