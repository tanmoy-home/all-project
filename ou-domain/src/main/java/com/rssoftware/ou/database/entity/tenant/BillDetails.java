package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the BILL_DETAILS3 database table.
 * 
 */
@Entity
@Table(name = "BILL_DETAILS")
@NamedQuery(name = "BillDetails.findAll", query = "SELECT b FROM BillDetails b")
public class BillDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	
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
	
	//@Type(type = "org.hibernate.type.FloatType")
	@Column(name = "ACTUAL_AMOUNT")
	private BigDecimal actualAmount;

	@Column(name = "DUE_DATE")
	private String dueDate;

	@Column(name = "BILL_DATE")
	private String billDate;

	@Id
	@Column(name = "BILL_NUMBER")
	private String billNumber;

	@Column(name = "BILL_PERIOD")
	private String billPeriod;

	@Column(name = "ADDITIONAL_AMOUNTS")
	private String additionalAmounts;

	@Column(name = "ADDITIONAL_INFO")
	private String additionalInfo;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "CRTN_TS")
	private Timestamp crtnTs;

	@Column(name = "CRTN_USER_ID")
	private String crtnUserId;

    @Column(name = "UPDT_TS")
	private Timestamp updtTs;

	@Column(name = "UPDT_USER_ID")
	private String updtUserId;
	
	public BillDetails() {
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

	public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillPeriod() {
		return billPeriod;
	}

	public void setBillPeriod(String billPeriod) {
		this.billPeriod = billPeriod;
	}

	public String getAdditionalAmounts() {
		return additionalAmounts;
	}

	public void setAdditionalAmounts(String additionalAmounts) {
		this.additionalAmounts = additionalAmounts;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCrtnUserId() {
		return crtnUserId;
	}

	public void setCrtnUserId(String crtnUserId) {
		this.crtnUserId = crtnUserId;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getUpdtUserId() {
		return updtUserId;
	}

	public void setUpdtUserId(String updtUserId) {
		this.updtUserId = updtUserId;
	}
	
}
