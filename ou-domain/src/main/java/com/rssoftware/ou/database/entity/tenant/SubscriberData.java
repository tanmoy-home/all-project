package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SUBSCRIBER_DATA")
@NamedQuery(name = "SubscriberData.findAll", query = "SELECT t FROM SubscriberData t")
public class SubscriberData implements Serializable{

	private static final long serialVersionUID = 2805307139420508786L;
	
	@Id
	@Column(name = "SUBSCRIBER_NO")
	private String subscriberNo;

	@Column(name = "BILLER_ID")
	private String blrId;
	
	@Column(name = "CUSTOMER_PARAM1")
	private String customerParam1;

	@Column(name = "CUSTOMER_PARAM2")
	private String customerParam2;

	@Column(name = "CUSTOMER_PARAM3")
	private String customerParam3;

	@Column(name = "CUSTOMER_PARAM4")
	private String customerParam4;

	@Column(name = "CUSTOMER_PARAM5")
	private String customerParam5;
	
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	
	@Column(name = "MOBILE")
	private String mobile;

	public String getSubscriberNo() {
		return subscriberNo;
	}

	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
	}

	public String getBlrId() {
		return blrId;
	}

	public void setBlrId(String blrId) {
		this.blrId = blrId;
	}

	public String getCustomerParam1() {
		return customerParam1;
	}

	public void setCustomerParam1(String customerParam1) {
		this.customerParam1 = customerParam1;
	}

	public String getCustomerParam2() {
		return customerParam2;
	}

	public void setCustomerParam2(String customerParam2) {
		this.customerParam2 = customerParam2;
	}

	public String getCustomerParam3() {
		return customerParam3;
	}

	public void setCustomerParam3(String customerParam3) {
		this.customerParam3 = customerParam3;
	}

	public String getCustomerParam4() {
		return customerParam4;
	}

	public void setCustomerParam4(String customerParam4) {
		this.customerParam4 = customerParam4;
	}

	public String getCustomerParam5() {
		return customerParam5;
	}

	public void setCustomerParam5(String customerParam5) {
		this.customerParam5 = customerParam5;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	

	
	
}
