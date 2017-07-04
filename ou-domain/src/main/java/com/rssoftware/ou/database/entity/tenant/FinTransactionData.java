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
 * The persistent class for the FIN_TRANSACTION_DATA database table.
 * 
 */
@Entity
@Table(name="FIN_TRANSACTION_DATA")
@NamedQuery(name="FinTransactionData.findAll", query="SELECT f FROM FinTransactionData f")
public class FinTransactionData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TXN_REF_ID")
	private String txnRefId;

	@Column(name="AUTH_CODE")
	private String authCode;

	@Column(name="CRTN_TS")
	private Timestamp crtnTs;

	@Column(name="CURRENT_STATUS")
	private String currentStatus;

	@Column(name="REF_ID")
	private String refId;

		
	@Column(name="REQUEST_JSON")
	private byte[] requestJson;

	
	@Column(name="RESPONSE_JSON")
	private byte[] responseJson;
	
	@Column(name="REVERSAL_REQUEST_JSON")
	private byte[] reversalRequestJson;
	
	@Column(name="REVERSAL_RESPONSE_JSON")
	private byte[] reversalResponseJson;

	@Column(name="SELECTED_PAYMENT_OPTIONS")
	private String selectedPaymentOptions;

	@Column(name="UPDT_TS")
	private Timestamp updtTs;
	
	@Column(name="CLIENT_REF_ID")
	private String clientRefId;

	@Column(name="REMITTANCE_ACCOUNT")
	private String remitAcc;

	@Column(name="RECON_PROCESSED")
	private String reconProcessed;
	
	@Column(name="TOTAL_AMOUNT")
	private BigDecimal totalAmount;
	
	public FinTransactionData() {
	}

	public String getTxnRefId() {
		return this.txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	public String getRemitAcc() {
		return remitAcc;
	}
	public void setRemitAcc(String remitAcc) {
		this.remitAcc = remitAcc;
	}

	public String getAuthCode() {
		return this.authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public Timestamp getCrtnTs() {
		return this.crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getRefId() {
		return this.refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public byte[] getRequestJson() {
		return this.requestJson;
	}

	public void setRequestJson(byte[] requestJson) {
		this.requestJson = requestJson;
	}

	public byte[] getResponseJson() {
		return this.responseJson;
	}

	public void setResponseJson(byte[] responseJson) {
		this.responseJson = responseJson;
	}

	public String getSelectedPaymentOptions() {
		return this.selectedPaymentOptions;
	}

	public void setSelectedPaymentOptions(String selectedPaymentOptions) {
		this.selectedPaymentOptions = selectedPaymentOptions;
	}

	public Timestamp getUpdtTs() {
		return this.updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getReconProcessed() {
		return reconProcessed;
	}

	public void setReconProcessed(String reconProcessed) {
		this.reconProcessed = reconProcessed;
	}

	public String getClientRefId() {
		return clientRefId;
	}

	public void setClientRefId(String clientRefId) {
		this.clientRefId = clientRefId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public byte[] getReversalRequestJson() {
		return reversalRequestJson;
	}

	public void setReversalRequestJson(byte[] reversalRequestJson) {
		this.reversalRequestJson = reversalRequestJson;
	}

	public byte[] getReversalResponseJson() {
		return reversalResponseJson;
	}

	public void setReversalResponseJson(byte[] reversalResponseJson) {
		this.reversalResponseJson = reversalResponseJson;
	}
	
	

}