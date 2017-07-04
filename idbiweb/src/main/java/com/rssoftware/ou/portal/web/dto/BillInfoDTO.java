package com.rssoftware.ou.portal.web.dto;

import java.util.List;
import java.util.Map;

import in.co.rssoftware.bbps.schema.CustomerParamsType;

public class BillInfoDTO {

	private String txnRefId;
	private String customerName;
	private String billerName;
	private String billDate;
	private String billNumber;
	private String billPeriod;
	private String dueDate;
	private String billAmount;
	private String meterNo;
	private String meterReadingPresent;
	private String meterReadingPast;
	private String ccfTax;
	private String totalAmtToPaid;
	private String paymentChannel;
	private String paymentmethod;
	private String billerAmountOptions;
	private String refId;
	private String customerMobile;
	private String customerEmail;
	private String billerId;
	private Map<String, String> customerInfo;
	private Map<String, String> billerTagInfo;
	private String agentId;

	public String getTxnRefId() {
		return txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
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

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public String getMeterReadingPresent() {
		return meterReadingPresent;
	}

	public void setMeterReadingPresent(String meterReadingPresent) {
		this.meterReadingPresent = meterReadingPresent;
	}

	public String getMeterReadingPast() {
		return meterReadingPast;
	}

	public void setMeterReadingPast(String meterReadingPast) {
		this.meterReadingPast = meterReadingPast;
	}

	public String getCcfTax() {
		return ccfTax;
	}

	public void setCcfTax(String ccfTax) {
		this.ccfTax = ccfTax;
	}

	public String getTotalAmtToPaid() {
		return totalAmtToPaid;
	}

	public void setTotalAmtToPaid(String totalAmtToPaid) {
		this.totalAmtToPaid = totalAmtToPaid;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getPaymentmethod() {
		return paymentmethod;
	}

	public void setPaymentmethod(String paymentmethod) {
		this.paymentmethod = paymentmethod;
	}

	public String getBillerAmountOptions() {
		return billerAmountOptions;
	}

	public void setBillerAmountOptions(String billerAmountOptions) {
		this.billerAmountOptions = billerAmountOptions;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public Map<String, String> getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(Map<String, String> customerInfo) {
		this.customerInfo = customerInfo;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public Map<String, String> getBillerTagInfo() {
		return billerTagInfo;
	}

	public void setBillerTagInfo(Map<String, String> billerTagInfo) {
		this.billerTagInfo = billerTagInfo;
	}

}
