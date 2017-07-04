package com.rssoftware.ou.portal.web.dto;

public class AmountValidatorDTO {
	
	private String amount;
	private String billerId;
	private String paymentMethod;
	private String paymentChannel;
	private Boolean isValidated;
	private String amountOption;
	private String errorMsg;
    private Boolean isQuickPay;
    private String basebillAmount;
    	
    
	
	public String getBasebillAmount() {
		return basebillAmount;
	}
	public void setBasebillAmount(String basebillAmount) {
		this.basebillAmount = basebillAmount;
	}
	public Boolean getIsQuickPay() {
		return isQuickPay;
	}
	public void setIsQuickPay(Boolean isQuickPay) {
		this.isQuickPay = isQuickPay;
	}
	public String getAmountOption() {
		return amountOption;
	}
	public void setAmountOption(String amountOption) {
		this.amountOption = amountOption;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Boolean getIsValidated() {
		return isValidated;
	}
	public void setIsValidated(Boolean isValidated) {
		this.isValidated = isValidated;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	
}
