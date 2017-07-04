package com.rssoftware.ou.common;

/**
 * @author rsdpp
 */
public enum MessageType {
	REQ_PAY, 
	RESP_AUTH_DETAILS, 
	RESP_PAY, 
	REQ_AUTH_DETAILS, 
	REQ_TXN_CONFIRMATION, 
	RESP_TXN_CONFIRMATION, 
	REVERSAL, 
	RESP_BAL_ENQ, 
	REQ_BAL_ENQ,
	REQ_VAL_ADD, 
	RESP_VAL_ADD, 
	REQ_SET_CRE, 
	RESP_SET_CRE, 
	REQ_OTP, 
	RESP_OTP, 
	REQ_REG_MOB, 
	RESP_REG_MOB, 
	REQ_LIST_ACCOUNT, 
	RESP_LIST_ACCOUNT,
	REQ_LIST_PSP, 
	RESP_LIST_PSP, 
	REQ_LIST_ACC_PVD, 
	RESP_LIST_ACC_PVD, 
	REQ_LIST_KEYS, 
	RESP_LIST_KEYS, 
	RESP_LIST_VAE, 
	REQ_LIST_VAE,	
	REQ_MANAGE_VAE, 
	RESP_MANAGE_VAE, 
	REQ_CHK_TXN, 
	RESP_CHK_TXN, 
	REQ_PENDING_MSG, 
	RESP_PENDING_MSG, 
	REQ_BAL_ENQ_CLIENT,	
	REQ_TOKEN, 
	REGSTR_KO_TOKEN,
	MODULE_INIT,
	CHECK_REQ_STATUS,
	REQ_ACTIVITY_LOG,
	CBS_RESPONSE;
}
