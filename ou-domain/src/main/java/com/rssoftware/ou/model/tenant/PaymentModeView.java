package com.rssoftware.ou.model.tenant;

import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;

public class PaymentModeView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String paymentModeId;
	private String paymentMode;
	private String maxLimit;
	private Boolean isChecked;
	private Boolean isSelected;

	public String getPaymentModeId() {
		return paymentModeId;
	}

	public void setPaymentModeId(String paymentModeId) {
		this.paymentModeId = paymentModeId;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}


	public String getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(String maxLimit) {
		this.maxLimit = maxLimit;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	
}
