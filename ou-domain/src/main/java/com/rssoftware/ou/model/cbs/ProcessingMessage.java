package com.rssoftware.ou.model.cbs;

import java.io.Serializable;

import com.rssoftware.ou.common.ErrorCode;
import com.rssoftware.ou.common.MetaApiName;
import com.rssoftware.ou.model.cbs.NextActionObject;
import com.rssoftware.ou.model.cbs.ProcessingStage;

public class ProcessingMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3497064369099759580L;
	private String transactionId;
	private NextActionObject nao;
	private String sourceCd;
	
	private ProcessingStage processingStage = null;
	private boolean metaApi = false;
	private MetaApiName metaApiName = null;
	private boolean metaApiTimeout = false;
	private ErrorCode metaApiErrorCode = null;

	// following attributes will be useful for IMPS and AEPS adapters
	private boolean isImpsSignOn;
	private boolean isImpsSignOff;
	private boolean isImpsEcho;
	private boolean isImpsCutOver;
	private boolean isAepsSignOn;
	private boolean isAepsSignOff;
	private boolean isAepsEcho;
	private boolean isAepsCutOver;	
	private boolean isFrmSignOn;
	private boolean isFrmSignOff;
	private boolean isFrmEcho;
	private boolean isFrmCutOver;
	
	private boolean eodMessage;
	private boolean settlementMessage;
	
	private boolean partialReversal = false;
	private Object payload;
	public ProcessingMessage() {}
	
	// IMPORTANT : IF YOU ADD ANY ATTRIBUTE PLEASE INCLUDE IT INTO THE COPY CONSTRUCTOR
	public ProcessingMessage(ProcessingMessage msg) {
		if (msg != null){
			transactionId  = msg.transactionId;
			nao = new NextActionObject(msg.nao);
			sourceCd = msg.sourceCd;
			
			processingStage = msg.processingStage;
			metaApi = msg.metaApi;
			metaApiName = msg.metaApiName;
			metaApiTimeout = msg.metaApiTimeout;
			metaApiErrorCode = msg.metaApiErrorCode;

			isImpsSignOn = msg.isImpsSignOn;
			isImpsSignOff = msg.isImpsSignOff;
			isImpsEcho = msg.isImpsEcho;
			isImpsCutOver = msg.isImpsCutOver;
			isAepsSignOn = msg.isAepsSignOn;
			isAepsSignOff = msg.isAepsSignOff;
			isAepsEcho = msg.isAepsEcho;
			isAepsCutOver = msg.isAepsCutOver;
			isFrmSignOn = msg.isFrmSignOn;
			isFrmSignOff = msg.isFrmSignOff;
			isFrmEcho = msg.isFrmEcho;
			isFrmCutOver = msg.isFrmCutOver;
			
			eodMessage = msg.eodMessage;
			settlementMessage = msg.settlementMessage;
			
			partialReversal = msg.partialReversal;
			
		}
	}
	
	
	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getSourceCd() {
		return sourceCd;
	}
	public void setSourceCd(String sourceCd) {
		this.sourceCd = sourceCd;
	}
	public ProcessingStage getProcessingStage() {
		return processingStage;
	}
	public void setProcessingStage(ProcessingStage processingStage) {
		this.processingStage = processingStage;
	}
	public boolean isImpsSignOn() {
		return isImpsSignOn;
	}
	public void setImpsSignOn(boolean isImpsSignOn) {
		this.isImpsSignOn = isImpsSignOn;
	}
	public boolean isImpsSignOff() {
		return isImpsSignOff;
	}
	public void setImpsSignOff(boolean isImpsSignOff) {
		this.isImpsSignOff = isImpsSignOff;
	}
	public boolean isImpsEcho() {
		return isImpsEcho;
	}
	public void setImpsEcho(boolean isImpsEcho) {
		this.isImpsEcho = isImpsEcho;
	}
	public boolean isImpsCutOver() {
		return isImpsCutOver;
	}
	public void setImpsCutOver(boolean isImpsCutOver) {
		this.isImpsCutOver = isImpsCutOver;
	}
	public boolean isFrmSignOn() {
		return isFrmSignOn;
	}
	public void setFrmSignOn(boolean isFrmSignOn) {
		this.isFrmSignOn = isFrmSignOn;
	}
	public boolean isFrmSignOff() {
		return isFrmSignOff;
	}
	public void setFrmSignOff(boolean isFrmSignOff) {
		this.isFrmSignOff = isFrmSignOff;
	}
	public boolean isFrmEcho() {
		return isFrmEcho;
	}
	public void setFrmEcho(boolean isFrmEcho) {
		this.isFrmEcho = isFrmEcho;
	}
	public boolean isFrmCutOver() {
		return isFrmCutOver;
	}
	public void setFrmCutOver(boolean isFrmCutOver) {
		this.isFrmCutOver = isFrmCutOver;
	}
	public boolean isAepsSignOn() {
		return isAepsSignOn;
	}
	public void setAepsSignOn(boolean isAepsSignOn) {
		this.isAepsSignOn = isAepsSignOn;
	}
	public boolean isAepsSignOff() {
		return isAepsSignOff;
	}
	public void setAepsSignOff(boolean isAepsSignOff) {
		this.isAepsSignOff = isAepsSignOff;
	}
	public boolean isAepsEcho() {
		return isAepsEcho;
	}
	public void setAepsEcho(boolean isAepsEcho) {
		this.isAepsEcho = isAepsEcho;
	}
	public boolean isAepsCutOver() {
		return isAepsCutOver;
	}
	public void setAepsCutOver(boolean isAepsCutOver) {
		this.isAepsCutOver = isAepsCutOver;
	}
	public NextActionObject getNao() {
		return nao;
	}
	public void setNao(NextActionObject nao) {
		this.nao = nao;
	}
	public boolean isMetaApi() {
		return metaApi;
	}
	public void setMetaApi(boolean metaApi) {
		this.metaApi = metaApi;
	}
	public MetaApiName getMetaApiName() {
		return metaApiName;
	}
	public void setMetaApiName(MetaApiName metaApiName) {
		this.metaApiName = metaApiName;
	}
	public boolean isMetaApiTimeout() {
		return metaApiTimeout;
	}
	public void setMetaApiTimeout(boolean metaApiTimeout) {
		this.metaApiTimeout = metaApiTimeout;
	}

	public ErrorCode getMetaApiErrorCode() {
		return metaApiErrorCode;
	}

	public void setMetaApiErrorCode(ErrorCode metaApiErrorCode) {
		this.metaApiErrorCode = metaApiErrorCode;
	}

	public boolean isEodMessage() {
		return eodMessage;
	}

	public void setEodMessage(boolean eodMessage) {
		this.eodMessage = eodMessage;
	}

	public boolean isSettlementMessage() {
		return settlementMessage;
	}

	public void setSettlementMessage(boolean settlementMessage) {
		this.settlementMessage = settlementMessage;
	}

	@Override
	public String toString() {
		return "ProcessingMessage [transactionId=" + transactionId + ", nao="
				+ nao + ", sourceCd=" + sourceCd + ", processingStage="
				+ processingStage + ", metaApi=" + metaApi + ", metaApiName="
				+ metaApiName + ", metaApiTimeout=" + metaApiTimeout
				+ ", metaApiErrorCode=" + metaApiErrorCode + ", isImpsSignOn="
				+ isImpsSignOn + ", isImpsSignOff=" + isImpsSignOff
				+ ", isImpsEcho=" + isImpsEcho + ", isImpsCutOver="
				+ isImpsCutOver + ", isAepsSignOn=" + isAepsSignOn
				+ ", isAepsSignOff=" + isAepsSignOff + ", isAepsEcho="
				+ isAepsEcho + ", isAepsCutOver=" + isAepsCutOver
				+ ", isFrmSignOn=" + isFrmSignOn + ", isFrmSignOff="
				+ isFrmSignOff + ", isFrmEcho=" + isFrmEcho + ", isFrmCutOver="
				+ isFrmCutOver + ", eodMessage=" + eodMessage
				+ ", settlementMessage=" + settlementMessage + "]";
	}

	public boolean isPartialReversal() {
		return partialReversal;
	}

	public void setPartialReversal(boolean partialReversal) {
		this.partialReversal = partialReversal;
	}
}
