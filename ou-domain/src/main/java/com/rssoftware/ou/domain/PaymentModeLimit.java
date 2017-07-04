package com.rssoftware.ou.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PaymentModeLimit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7851150294320197027L;
	private PaymentMode paymentMode;
	private Long maxLimit;
	private Long minLimit;
	public Long getMinLimit() {
		return minLimit;
	}
	public void setMinLimit(Long minLimit) {
		this.minLimit = minLimit;
	}
	public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Long getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}
}
