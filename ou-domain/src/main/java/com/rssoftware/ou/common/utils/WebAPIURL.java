package com.rssoftware.ou.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebAPIURL {
	static final Logger log = LoggerFactory.getLogger(WebAPIURL.class);

	// public static final String OUNAME = "OU09";
	// public static final String hostport =
	// "https://localhost:9090/APIService";
	public static final String ouName = "/urn:tenantId:{tenantId}";
	/*
	 * public static final String BILLER_CATEGORY_URL =
	 * "/APIService/biller-category-list" + ouName +
	 * "?paymentChannels={paymentChannels}&paymentModes={paymentModes}";
	 */

	/* edited by Somnath at 18/01/2017 */
	public static final String BILLER_CATEGORY_URL = "/APIService/biller-category-list" + ouName;
	/*
	 * public static final String FETCH_BILLER_URL = "/APIService/biller-list" +
	 * ouName +
	 * "?billerCategory={billerCategory}&paymentChannels={paymentChannels}&paymentModes={paymentModes}";
	 */

	/* edited by Somnath at 18/1/2017 */
	public static final String FETCH_BILLER_URL = "/APIService/biller-list" + ouName;
	/*
	 * public static final String FETCH_SUB_BILLER_URL =
	 * "/APIService/sub-biller-list" + ouName + "?billerId={billerId}";
	 */
	public static final String FETCH_SUB_BILLER_URL = "/APIService/sub-biller-list" + ouName;
	/*
	 * public static final String FETCH_CUSTOM_PARAM_URL =
	 * "/APIService/biller-fetch-custom-params" + ouName+"?billerId={billerId}";
	 */

	/* edited by Somnath at 18/1/2017 */
	public static final String FETCH_CUSTOM_PARAM_URL = "/APIService/biller-fetch-custom-params" + ouName;
	public static final String BILL_FETCH_URL = "/APIService/bill-fetch-form-post" + ouName;
	public static final String AGENT_TXN_SEARCH_BYDATE_URL = "/APIService/agents" + ouName + "/agentTxnSearch/";
	public static final String AGENT_TXN_SEARCH_BYID_URL = "/APIService/agents" + ouName + "/agentTxnHistory/";
	public static final String AGENT_TXN_SEARCH_BYMOBNO_URL = "/APIService/agents" + ouName + "/agentTxnByMobile/";

	public static final String COMPLAINT_CHECK_STATUS_URL = "/APIService/complaint/searchcomplaintstatus" + ouName;
	public static final String COMPLAINT_ALL_SERVICE_REASON_URL = "/APIService/complaint/getAllServiceReason" + ouName;
	public static final String FETCH_ALL_AGENT_URL = "/APIService/agentInst" + ouName + "/getAllAgents";
	public static final String FETCH_ALL_BILLER_URL = "/APIService/BillerOU" + ouName + "/getBillers";
	public static final String RAISE_SERVICE_COMPLAINT_URL = "/APIService/complaint/raisecomplaintreq" + ouName;
	public static final String FETCH_ALL_TRANSACTIONS_LIST_URL = "/APIService/complaint/searchtransaction" + ouName;
	public static final String FETCH_ALL_DISPOSITION_URL = "/APIService/complaint" + ouName + "/dispositionlist";
	public static final String RAISE_TRANSACTION_COMPLAINT_URL = "/APIService/complaint/raisecomplaintreq" + ouName;
	public static final String CALCULATE_CCF_URL = "/APIService/calculate-ccf" + ouName;
	
	public static final String FETCH_DEFAULT_AGENT_URL = "/APIService/agents/" + ouName + "/defaultAgent";
	public static final String FETCH_BILLER_DETAILS_URL = "/APIService/BillerOU/" + ouName
			+ "/getBillerDetails/{billerId}";
	public static final String DUPLICATE_PRINT_RECEIPT_URL = "/APIService/agents" + ouName
			+ "/PaymentTransactionDetail/";
	public static final String PGPARAM_LIST_URL = "/APIService/pgparams" + ouName;
	public static final String INSERT_FIN_TRANSACTION_RECORD_URL = "/APIService/fintransaction_insert" + ouName;
	public static final String UPDATE_FIN_TRANSACTION_RECORD_URL = "/APIService/fintransaction_update" + ouName;
	public static final String GET_FIN_TRANSACTION_RECORD_URL = "/APIService/fintransaction_get" + ouName;
	public static final String PROCESS_PG_PAYMENT_URL = "/APIService/process_pg_payment" + ouName;
	public static final String GET_UNIQUE_ID_URL = "/APIService/getuniqueid" + ouName;
	public static final String BILL_PAYMENT_URL = "/APIService/bill-payment-form-post" + ouName;
	public static final String GET_TENANT_PARAM = "/APIService/tenantparam" + ouName + "/{paramName}";
	public static final String GET_NOTIFICATION_TEMPLATE_URL = "/APIService" + ouName
			+ "/notificationtemplate?msgType={msgType}&eventType={eventType}";
	public static final String SAVE_SMS_URL = "/APIService" + ouName + "/sms";
	public static final String GENERATE_OTP = "/APIService" + ouName + "/generateOtp";
	public static final String VALIDATE_OTP = "/APIService" + ouName + "/validateOtp";
	public static final String SAVEANDSEND_SMS_URL = "/APIService" + ouName + "/sendsms";
	
	//fetch agent detais for specific agent
	public static final String FETCH_AGENT_URL = "/APIService/agents" + ouName + "/agentId:{agentId}";
	public static final String INSERT_CHANNEL_PARTNER_INFO_URL = "/APIService/insertChannelPartnerInfo"+ ouName;
	
}