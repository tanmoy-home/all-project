package com.rssoftware.ou.common.utils;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonInclude;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BillSummary implements Serializable {
	private static final long serialVersionUID = -3731979045008402895L;

	@XmlElement(nillable = false)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long billCount;

	@XmlElement(nillable = false)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long paidBillCount;

	@XmlElement(nillable = false)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String todayCollectAmmount;

	public Long getBillCount() {
		return billCount;
	}

	public void setBillCount(Long billCount) {
		this.billCount = billCount;
	}

	public Long getPaidBillCount() {
		return paidBillCount;
	}

	public void setPaidBillCount(Long paidBillCount) {
		this.paidBillCount = paidBillCount;
	}

	public String getTodayCollectAmmount() {
		return todayCollectAmmount;
	}

	public void setTodayCollectAmmount(String todayCollectAmmount) {
		this.todayCollectAmmount = todayCollectAmmount;
	}
}