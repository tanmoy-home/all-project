package com.rssoftware.ou.model.tenant;

import java.io.Serializable;
import java.util.List;

import com.rssoftware.ou.common.MTI;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;

public class InterchangeFeeConfView implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long feeCfgId;
	private String blrId;
	private MTI mti;
	private PaymentMode paymentMode;
	private PaymentChannel paymentChannel;
	private String responseCode;
	private List <String> fees;
	private String effectiveFrom;
	private String effectiveTo;
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

	public MTI getMti() {
		return mti;
	}

	public void setMti(MTI mti) {
		this.mti = mti;
	}

	public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}

	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public List<String> getFees() {
		return fees;
	}

	public void setFees(List<String> fees) {
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

}
