package com.rssoftware.ou.common;

import java.util.HashMap;
import java.util.Map;


public enum SegmentReportHeaderContent {
	SR_NO("Sr. No",1),
	BILLER_NAME("Name of the Biller",1),
	TOT_TXN_VOL("Total Volume of transactions (Number)",2),
	TOT_TXN_VAL("Total value of transactions (in Rs)",2);
	
	private String alias;
	private int colSpan;
	private static Map<String, String[]> headerRowValueMap = new HashMap<>();
	SegmentReportHeaderContent(String alias,int colSpan){
		this.alias=alias;
		this.colSpan=colSpan;
	}
	
	static {
		
		headerRowValueMap.put(SegmentReportHeaderContent.SR_NO.alias(),new String[]{""});
		headerRowValueMap.put(SegmentReportHeaderContent.BILLER_NAME.alias(),new String[]{""});
		headerRowValueMap.put(SegmentReportHeaderContent.TOT_TXN_VOL.alias(),new String[]{"On-Us","Off-US"});
		headerRowValueMap.put(SegmentReportHeaderContent.TOT_TXN_VAL.alias(),new String[]{"On-Us","Off-US"});
	}
	
	public String alias(){
		return this.alias;
	}
	
	public int colSpan(){
		return this.colSpan;
	}
	
	public String[] getHeaderDetails(SegmentReportHeaderContent headerItem){
		return this.headerRowValueMap.get(headerItem.alias());
	}
	
 }