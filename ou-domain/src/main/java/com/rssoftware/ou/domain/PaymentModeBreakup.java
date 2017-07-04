package com.rssoftware.ou.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PaymentModeBreakup implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7851150294320197027L;
	private PaymentMode paymentMode;
	private Long amount;
	public PaymentMode getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
}
