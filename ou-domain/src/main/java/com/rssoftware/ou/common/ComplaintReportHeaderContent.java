package com.rssoftware.ou.common;

import java.util.HashMap;
import java.util.Map;


public enum ComplaintReportHeaderContent {
	
	SR_NO("Sr. No", 1, 1),
	BBPOU_NAME("Name of the BBPOU", 1, 1),
	COMPLAINTS_RECEIVED("Number of Customer Complaints received during the week", 2, 6),
	COMPLAINTS_RESOLVED("Complaints resolved during the Week", 2, 2),
	COMPLAINTS_PENDING("Complaints pending  at the end of the week", 2, 2),
	COMPLAINT_TYPES("Types of complaints received", 2, 2);
	
	private String alias;
	private int colSpanRow1;
	private int colSpanRow2;

	private static Map<String, String[]> headerRowValueMap = new HashMap<>();
	ComplaintReportHeaderContent(String alias,int colSpanRow1, int colSpanRow2){
		this.alias=alias;
		this.colSpanRow1=colSpanRow1;
		this.colSpanRow2=colSpanRow2;
	}
	
	static {
		
		headerRowValueMap.put(ComplaintReportHeaderContent.SR_NO.alias(),new String[]{"",""});
		headerRowValueMap.put(ComplaintReportHeaderContent.BBPOU_NAME.alias(),new String[]{"",""});
		headerRowValueMap.put(ComplaintReportHeaderContent.COMPLAINTS_RECEIVED.alias(),new String[]{"On-Us", "Off-Us", "Outstanding end of previous week", "Received during this week", "Total", "Outstanding end of previous week", "Received during this week", "Total"});
		headerRowValueMap.put(ComplaintReportHeaderContent.COMPLAINTS_RESOLVED.alias(),new String[]{"On-Us", "Off-Us", "", ""});
		headerRowValueMap.put(ComplaintReportHeaderContent.COMPLAINTS_PENDING.alias(),new String[]{"On-Us", "Off-Us", "", ""});
		headerRowValueMap.put(ComplaintReportHeaderContent.COMPLAINT_TYPES.alias(),new String[]{"Transaction Based", "Service Based", "", ""});
	}
	
	public String alias(){
		return this.alias;
	}
	
	public int colSpanRow1(){
		return this.colSpanRow1;
	}
	
	public int colSpanRow2(){
		return this.colSpanRow2;
	}
	
	public String[] getHeaderDetails(ComplaintReportHeaderContent headerItem){
		return this.headerRowValueMap.get(headerItem.alias());
	}
	
 }