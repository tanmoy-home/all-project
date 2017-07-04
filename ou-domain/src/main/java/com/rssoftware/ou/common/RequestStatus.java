package com.rssoftware.ou.common;

import java.io.Serializable;

public enum RequestStatus implements Serializable {
	SENT, 
	SEND_FAILED, 
	SEND_FAILED_ACK, 
	TIMEOUT, 
	RESPONSE_SUCCESS,
	RESPONSE_FAILURE,
	RESPONSE_DECLINE,
	RESPONSE_REVERSE,
	QPAY_INITIATED,
	PAYMENT_INITIATED,
	DUPLICATE_REQUEST,
	REQUEST_RECIEVED,
	RESPONSE_RECIEVED,
	BILLER_NOT_AVAILABLE,
	REFUND_SUCCESS,
	REFUND_DECLINE;

}
