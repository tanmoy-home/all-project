package com.rssoftware.ou.common;

import java.io.Serializable;

public enum FinReconStatus implements Serializable {
	RESPONSE_NOT_RECEIVED("SENT"), 
	RESPONSE_SUCCESS("RESPONSE_SUCCESS"), 
	RESPONSE_REVERSE("RESPONSE_REVERSE,SENT,QPAY_INITIATED,TIMEOUT,SEND_FAILED,SEND_FAILED_ACK,RESPONSE_DECLINE"), 
	RESPONSE_FAILURE("RESPONSE_FAILURE"),
	REFUND_SUCCESS("REFUND_SUCCESS"),
	REFUND_SENT("REFUND_SENT"),
	REFUND_DECLINE("REFUND_DECLINE");
	private String incomingStatus;
	private FinReconStatus(String incomingStatus) {
        this.setIncomingStatus(incomingStatus);
	}
	public String getIncomingStatus() {
		return incomingStatus;
	}
	public void setIncomingStatus(String incomingStatus) {
		this.incomingStatus = incomingStatus;
	}
	 public static FinReconStatus get(String status) { 
	        for(FinReconStatus s : values()) {
	            if(s.incomingStatus.equals(status)) return s;
	        }
	        return null;
	    }
}



