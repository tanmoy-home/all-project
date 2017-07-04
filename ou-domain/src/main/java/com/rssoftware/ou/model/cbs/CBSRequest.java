package com.rssoftware.ou.model.cbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rssoftware.ou.schema.cbs.CbsApiNames;
//import com.rssoftware.upiint.schema.Cred;

public class CBSRequest {
	
	private String requestId;
	private String sessionId;
	private String DeviceFormat;
	private String operationId;
	private String deviceFamily;
	private int transSeq;
	private String clientApiVer;
	private String upiTxnId;
	private String customerId;
	private String txnRefId ;
	private String originalTxnId ;
	private String accNo;
	private String accType;
	private String cardNo;
	private String expiryDate;
	private String ifsc;
	private String mobileNo;
	private String mmid;
	private String aadhaarNo;
	private String aadhaarIIN;
	private String userVpa;
	private String otpRefNo;
	private String otp;
//	private List<Cred> creds = new ArrayList<Cred>();
	private TxnType txnType;
	private Double txnAmount;
	private String trnParticulars;
	private CurrencyCodes currencyCode;
	private String processingCode;
	private String custId;
	private String custRefId;
	private CbsApiNames apiName;
	private String creditAccNo;
	

	public String getUpiTxnId() {
		return upiTxnId;
	}
	public void setUpiTxnId(String upiTxnId) {
		this.upiTxnId = upiTxnId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getProcessingCode() {
		return processingCode;
	}
	public void setProcessingCode(String processingCode) {
		this.processingCode = processingCode;
	}
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getDeviceFormat() {
		return DeviceFormat;
	}
	public void setDeviceFormat(String deviceFormat) {
		DeviceFormat = deviceFormat;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getDeviceFamily() {
		return deviceFamily;
	}
	public void setDeviceFamily(String deviceFamily) {
		this.deviceFamily = deviceFamily;
	}
	public int getTransSeq() {
		return transSeq;
	}
	public void setTransSeq(int transSeq) {
		this.transSeq = transSeq;
	}
	public String getClientApiVer() {
		return clientApiVer;
	}
	public void setClientApiVer(String clientApiVer) {
		this.clientApiVer = clientApiVer;
	}
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the txnRefId
	 */
	public String getTxnRefId() {
		return txnRefId;
	}
	/**
	 * @param txnRefId the txnRefId to set
	 */
	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	public String getOriginalTxnId() {
		return originalTxnId;
	}
	public void setOriginalTxnId(String originalTxnId) {
		this.originalTxnId = originalTxnId;
	}
	/**
	 * @return the accNo
	 */
	public String getAccNo() {
		return accNo;
	}
	/**
	 * @param accNo the accNo to set
	 */
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	/**
	 * @return the accType
	 */
	public String getAccType() {
		return accType;
	}
	/**
	 * @param accType the accType to set
	 */
	public void setAccType(String accType) {
		this.accType = accType;
	}
	/**
	 * @return the ifsc
	 */
	public String getIfsc() {
		return ifsc;
	}
	/**
	 * @param ifsc the ifsc to set
	 */
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	/**
	 * @return the txnType
	 */
	public TxnType getTxnType() {
		return txnType;
	}
	/**
	 * @param txnType the txnType to set
	 */
	public void setTxnType(TxnType txnType) {
		this.txnType = txnType;
	}
	/**
	 * @return the txnAmount
	 */
	public Double getTxnAmount() {
		return txnAmount;
	}
	/**
	 * @param tranAmount the txnAmount to set
	 */
	public void setTxnAmount(Double txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTrnParticulars() {
		return trnParticulars;
	}
	public void setTrnParticulars(String trnParticulars) {
		this.trnParticulars = trnParticulars;
	}
	
	public CurrencyCodes getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(CurrencyCodes currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getOtpRefNo() {
		return otpRefNo;
	}
	public void setOtpRefNo(String otpRefNo) {
		this.otpRefNo = otpRefNo;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getUserVpa() {
		return userVpa;
	}
	public void setUserVpa(String userVpa) {
		this.userVpa = userVpa;
	}
	public String getMmid() {
		return mmid;
	}
	public void setMmid(String mmid) {
		this.mmid = mmid;
	}
	public String getAadhaarNo() {
		return aadhaarNo;
	}
	public void setAadhaarNo(String aadhaarNo) {
		this.aadhaarNo = aadhaarNo;
	}
	public String getAadhaarIIN() {
		return aadhaarIIN;
	}
	public void setAadhaarIIN(String aadhaarIIN) {
		this.aadhaarIIN = aadhaarIIN;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
//	public List<Cred> getCreds() {
//		return creds;
//	}
	public String getCustRefId() {
		return custRefId;
	}
	public void setCustRefId(String custRefId) {
		this.custRefId = custRefId;
	}
	public void setApiName(CbsApiNames apiName) {
		this.apiName = apiName;
	}
	public CbsApiNames getApiName() {
		return this.apiName;
	}
	public String getCreditAccNo() {
		return creditAccNo;
	}
	public void setCreditAccNo(String creditAccNo) {
		this.creditAccNo = creditAccNo;
	}
	
	
//	@Override
//	public String toString() {
//		return "CBSRequest [requestId=" + requestId + ", customerId="
//				+ customerId + ", txnRefId=" + txnRefId + ", accNo=" + accNo
//				+ ", accType=" + accType + ", ifsc=" + ifsc + ", mobileNo="
//				+ mobileNo + ", creds=" + creds + ", txnType=" + txnType
//				+ ", txnAmount=" + txnAmount + ", trnParticulars="
//				+ trnParticulars + ", currencyCode=" + currencyCode + "]";
//	}


}
