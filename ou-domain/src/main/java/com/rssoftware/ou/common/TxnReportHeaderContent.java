package com.rssoftware.ou.common;

import java.util.HashMap;
import java.util.Map;


public enum TxnReportHeaderContent {
	SR_NO("Sr. No",1),
	BBPOU_NAME("Name of the BBPOU",1),
	AGENT_OUTLET_NO("Total No. of Agent Outlets",1),
	TOT_TXN_VOL("Total Volume of transactions (Number)",2),
	TOT_TXN_VAL("Total value of transactions (in Rs)",2),
	TOT_FAILED_TXN_VOL("Total number of failed transactions",2),
	TOT_FAILED_TXN_VAL("Total Value of failed transactions (in Rs)",2),
	REASON_FOR_FAILURE("Reasons for failed transactions and action taken by the entity",1),
	TRANS_CNT_PER_PAY_MODE("Number of transaction through these mode of payments",6);
	private String alias;
	private int colSpan;
	private static Map<String, String[]> headerRowValueMap = new HashMap<>();
	TxnReportHeaderContent(String alias,int colSpan){
		this.alias=alias;
		this.colSpan=colSpan;
	}
	
	static {
		
		headerRowValueMap.put(TxnReportHeaderContent.SR_NO.alias(),new String[]{"1",""});
		headerRowValueMap.put(TxnReportHeaderContent.BBPOU_NAME.alias(),new String[]{"2",""});
		headerRowValueMap.put(TxnReportHeaderContent.AGENT_OUTLET_NO.alias(),new String[]{"3",""});
		headerRowValueMap.put(TxnReportHeaderContent.TOT_TXN_VOL.alias(),new String[]{"4","On-Us","Off-US"});
		headerRowValueMap.put(TxnReportHeaderContent.TOT_TXN_VAL.alias(),new String[]{"5","On-Us","Off-US"});
		headerRowValueMap.put(TxnReportHeaderContent.TOT_FAILED_TXN_VOL.alias(),new String[]{"6","On-Us","Off-US"});
		headerRowValueMap.put(TxnReportHeaderContent.TOT_FAILED_TXN_VAL.alias(),new String[]{"7","On-Us","Off-US"});
		headerRowValueMap.put(TxnReportHeaderContent.REASON_FOR_FAILURE.alias(),new String[]{"8",""});
		headerRowValueMap.put(TxnReportHeaderContent.TRANS_CNT_PER_PAY_MODE.alias(),new String[]{"9","Cash","DC/CC","Netbanking","IMPS","PPIs"
		,"Other (specify)"});
		
	}
	
	public String alias(){
		return this.alias;
	}
	
	public int colSpan(){
		return this.colSpan;
	}
	
	public String[] getHeaderDetails(TxnReportHeaderContent headerItem){
		return this.headerRowValueMap.get(headerItem.alias());
	}
	
 }