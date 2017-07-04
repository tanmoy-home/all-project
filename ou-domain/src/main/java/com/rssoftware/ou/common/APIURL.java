package com.rssoftware.ou.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class APIURL {
	
	public static final String HOST_PORT = System.getProperty("WEB_HOST_PORT", "https://localhost:9095") + "/APIService";
	public static final String OUNAME = "/urn:tenantId:" + System.getProperty("OUName", "OU04");
	
	static final Logger log = LoggerFactory.getLogger(APIURL.class);
		
	public static String STATE_URL = HOST_PORT + OUNAME + "/fetch-state-detail";
	public static String APP_CONF_URL = HOST_PORT + OUNAME + "/applicationConfigGroupName";
	public static String CITY_URL = HOST_PORT + OUNAME + "/fetch-city-detail";
	public static String POSTALCODE_URL = HOST_PORT + OUNAME + "/fetch-postal-detail";
	public static String ONBOARD_AGENT_URL = HOST_PORT + "/agents" + OUNAME + "/agentRegistration";	
	public static String AGENT_PENDING_APPROVAL_URL = HOST_PORT + "/agentInst" + OUNAME + "/pendingAgents";
	public static String AGENT_APPROVE_URL = HOST_PORT + "/agentInst" + OUNAME + "/approve";
	public static String AGENT_REJECT_URL = HOST_PORT + "/agentInst" + OUNAME + "/reject";
	public static String AI_LIST_URL = HOST_PORT + "/agentInst" + OUNAME + "/agentInstitutes";
	
	public static String SCHEME_URL = HOST_PORT + "/agentInst" + OUNAME + "/getSchemes";
	public static String SCHEME_DTL_URL = HOST_PORT + "/agentInst" + OUNAME + "/getSchemes";
	
	public static String RECON_REPORT_URL = HOST_PORT + "/reconcile" + OUNAME + "/agentInstReport/";
	public static String RECON_CUOU_REPORT_URL = HOST_PORT + "/reconcile" + OUNAME + "/reconDetailsReport";
	public static String PG_RECON_REPORT_URL = HOST_PORT + "/reconcile" + OUNAME + "/fetchPGReconFiles";
	public static String CU_SETLMNT_REPORT_URL = HOST_PORT + "/reconcile" + OUNAME + "/fetchCuSettlementFiles";
	public static String BLR_RECON_REPORT_URL = HOST_PORT + "/reconcile" + OUNAME + "/fetchBlrReconFiles";
	public static String DOWNLOAD_FILE_URL = HOST_PORT + "/reconcile" + OUNAME;
	public static String COMMISSION_REPORT_URL = HOST_PORT + "/agentInst" + OUNAME + "/commission/";
	
	public static String View_AGENT_LIST_URL = HOST_PORT + "/agentInst" + OUNAME + "/agents";
	public static String View_AGENT_DTL_URL = HOST_PORT + "/agents" + OUNAME + "/agentId";
	
	public static String TENENT_PARAM_URL = HOST_PORT + "/tenantparam" + OUNAME;
	
	public static final String SEND_SMS_URL = HOST_PORT + OUNAME + "/sms";
	public static final String GET_NOTIFICATION_TEMPLATE_URL = HOST_PORT + OUNAME + "/notificationtemplate?msgType={msgType}&eventType={eventType}";
	public static String TXN_REPORT_DOWNLOAD_URL = HOST_PORT + "/weeklyReport" + OUNAME + "/downloadTxnReport";
	public static String TXN_REPORT_URL = HOST_PORT + "/weeklyReport" + OUNAME + "/txnReport";

	
	public static String COMPLAINT_REPORT_URL = HOST_PORT + "/weeklyReport" + OUNAME + "/complaintReport";
	public static String SEGMENT_REPORT_URL = HOST_PORT + "/weeklyReport" + OUNAME + "/segmentReport";
	
	public static String PAYMENT_MODE_LOOKUP_URL = HOST_PORT + OUNAME + "/lookupPaymentMode";
}