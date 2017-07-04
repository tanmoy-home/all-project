package com.rssoftware.ou.database.entity.tenant;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "OFFLINEB_PAYMENT")
@NamedQuery(name = "OfflinebPayment.findAll", query = "SELECT a FROM OfflinebPayment a")
public class OfflinebPayment {
	
	@Id
	@Column(name = "PAYMENT_ID")
	private String paymentId;
	
	@Column(name = "SUBSCRIBER_NO")
	private String subscriberNo;
	
	@Column(name = "AMOUNT")
	private BigDecimal amount;
	
	@Column(name = "PAYMENT_TS")
	private Timestamp paymentTs;
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getSubscriberNo() {
		return subscriberNo;
	}

	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Timestamp getPaymentTs() {
		return paymentTs;
	}

	public void setPaymentTs(Timestamp paymentTs) {
		this.paymentTs = paymentTs;
	}

	

	
}
