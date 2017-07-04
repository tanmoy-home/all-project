package com.rssoftware.ou.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.CommonConstants;

public class AgentWebAPIURL {
static final Logger log = LoggerFactory.getLogger(CommonConstants.class);
	
	public static final String hostport = "https://localhost:9000/APIService";
	public static final String ouName = "/urn:tenantId:OU03";
	public static String BILLER_CATEGORY_URL = hostport + "/biller-category-list"+ ouName;
	public static String FETCH_BILLER_URL = hostport + "/biller-list"+ ouName+"?billerCategory=";
	public static String BILL_FETCH_URL = hostport + "/bill-fetch-form-post"+ ouName;
	public static String AGENT_TXN_SEARCH_BYDATE_URL = hostport + "/agents"+ ouName+"/agentTxnSearch/";
	public static String AGENT_TXN_SEARCH_BYID_URL = hostport + "/agents"+ ouName+"/agentTxnHistory/";
	public static String AGENT_TXN_SEARCH_BYMOBNO_URL = hostport + "/agents"+ ouName+"/agentTxnByMobile/";
	

}
