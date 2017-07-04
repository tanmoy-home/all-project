package com.rssoftware.ou.model.cbs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CBSResponse {
	
	private String responseId;
	private String requestId;
	private String customerId;
	private String accNo;
	private Double accBalance;
	private String accType;
	private String ifsc;
	private TxnType txnType;
//	private String txnType;
	private Double tranAmount;
	private String txnRefId ;
	private String cbsRefId ;
	private String custId;
	private List<AccountDetails> accountDetails = new ArrayList<AccountDetails>();
	private TxnStatus status;
//	private String status;
	private String responseMessage;
	private String errorCode;
	private String errorMessage;
	private String sol;
	private Map<Integer,Object> isoMap;
	
	public Map<Integer, Object> getIsoMap() {
		return isoMap;
	}
	public void setIsoMap(Map<Integer, Object> isoMap) {
		this.isoMap = isoMap;
	}
	public CBSResponse() {
		
	}
	public CBSResponse(String errorCode, String errorMessage, TxnStatus status) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.status = status;
	}
	public CBSResponse(String errorCode, String errorMessage, TxnStatus status, TxnType type) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.status = status;
		this.txnType = type;
	}
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getSol() {
		return sol;
	}
	public void setSol(String sol) {
		this.sol = sol;
	}
	/**
	 * @return the responseId
	 */
	public String getResponseId() {
		return responseId;
	}
	/**
	 * @param responseId the responseId to set
	 */
	public void setResponseId(String responseId) {
		this.responseId = responseId;
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
	 * @return the status
	 */
	public TxnStatus getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(TxnStatus status) {
		this.status = status;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	/**
	 * @return the tranAmount
	 */
	public Double getTranAmount() {
		return tranAmount;
	}
	/**
	 * @param tranAmount the tranAmount to set
	 */
	public void setTranAmount(Double tranAmount) {
		this.tranAmount = tranAmount;
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
	/**
	 * @return the cbsRefId
	 */
	public String getCbsRefId() {
		return cbsRefId;
	}
	/**
	 * @param cbsRefId the cbsRefId to set
	 */
	public void setCbsRefId(String cbsRefId) {
		this.cbsRefId = cbsRefId;
	}
	
	public List<AccountDetails> getAccountDetails() {
		return this.accountDetails;
	}
	public Double getAccBalance() {
		return accBalance;
	}
	public void setAccBalance(Double accBalance) {
		this.accBalance = accBalance;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "CBSResponse [customerId=" + customerId + ", accNo=" + accNo
				+ ", accBalance=" + accBalance + ", ifsc=" + ifsc + "]";
	}

	
}
