package com.rssoftware.ou.model.tenant;

import java.io.Serializable;

public class PaymentChannelView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3349979803723118484L;
	private String paymentChannelId;
	private String paymentChannel;
	private String maxLimit;
	
	
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

	private Boolean isChecked;
	private Boolean isSelected;

	public String getPaymentChannelId() {
		return paymentChannelId;
	}

	public void setPaymentChannelId(String paymentModeId) {
		this.paymentChannelId = paymentModeId;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentMode) {
		this.paymentChannel = paymentMode;
	}
}
