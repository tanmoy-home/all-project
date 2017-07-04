package com.rssoftware.ou.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PaymentChannelLimit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7851150294320197027L;
	private PaymentChannel paymentChannel;
	private Long maxLimit;
	private Long minLimit;
	public Long getMinLimit() {
		return minLimit;
	}
	public void setMinLimit(Long minLimit) {
		this.minLimit = minLimit;
	}
	public PaymentChannel getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(PaymentChannel paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public Long getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}
}
