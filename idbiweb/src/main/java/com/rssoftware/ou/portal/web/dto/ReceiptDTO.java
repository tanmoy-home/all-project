package com.rssoftware.ou.portal.web.dto;

public class ReceiptDTO {

	private String CustomerName;
	private String CustomerMobileNumber;
	private String CustomerAccountNumber;
	private String CustomerBillDate;
	private String CustomerBillAmount;
	private String CustomerTotalAmount;
	private String CustomerConvFee;
	private String CustomerPaymentChannel;
	private String CustomerPaymentMode;
	private String CustomerAuthCode;
	private String CustomerDateTime;
	private String CustomerTransactionRefId;
	private String transactionStatus;
	private String billerName;
	private String billerId;
	private String billIdentificationParam;

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getCustomerMobileNumber() {
		return CustomerMobileNumber;
	}

	public void setCustomerMobileNumber(String customerMobileNumber) {
		CustomerMobileNumber = customerMobileNumber;
	}

	public String getCustomerAccountNumber() {
		return CustomerAccountNumber;
	}

	public void setCustomerAccountNumber(String customerAccountNumber) {
		CustomerAccountNumber = customerAccountNumber;
	}

	public String getCustomerBillDate() {
		return CustomerBillDate;
	}

	public void setCustomerBillDate(String customerBillDate) {
		CustomerBillDate = customerBillDate;
	}

	public String getCustomerBillAmount() {
		return CustomerBillAmount;
	}

	public void setCustomerBillAmount(String customerBillAmount) {
		CustomerBillAmount = customerBillAmount;
	}

	public String getCustomerTotalAmount() {
		return CustomerTotalAmount;
	}

	public void setCustomerTotalAmount(String customerTotalAmount) {
		CustomerTotalAmount = customerTotalAmount;
	}

	public String getCustomerConvFee() {
		return CustomerConvFee;
	}

	public void setCustomerConvFee(String customerConvFee) {
		CustomerConvFee = customerConvFee;
	}

	public String getCustomerPaymentChannel() {
		return CustomerPaymentChannel;
	}

	public void setCustomerPaymentChannel(String customerPaymentChannel) {
		CustomerPaymentChannel = customerPaymentChannel;
	}

	public String getCustomerPaymentMode() {
		return CustomerPaymentMode;
	}

	public void setCustomerPaymentMode(String customerPaymentMode) {
		CustomerPaymentMode = customerPaymentMode;
	}

	public String getCustomerAuthCode() {
		return CustomerAuthCode;
	}

	public void setCustomerAuthCode(String customerAuthCode) {
		CustomerAuthCode = customerAuthCode;
	}

	public String getCustomerDateTime() {
		return CustomerDateTime;
	}

	public void setCustomerDateTime(String customerDateTime) {
		CustomerDateTime = customerDateTime;
	}

	public String getCustomerTransactionRefId() {
		return CustomerTransactionRefId;
	}

	public void setCustomerTransactionRefId(String customerTransactionRefId) {
		CustomerTransactionRefId = customerTransactionRefId;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public String getBillerId() {
		return billerId;
	}

	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}

	public String getBillIdentificationParam() {
		return billIdentificationParam;
	}

	public void setBillIdentificationParam(String billIdentificationParam) {
		this.billIdentificationParam = billIdentificationParam;
	}

}
