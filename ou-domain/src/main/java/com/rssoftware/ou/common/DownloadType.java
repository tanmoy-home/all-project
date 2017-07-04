package com.rssoftware.ou.common;

public enum DownloadType {

	TXN_REPORT(TxnReportResp.class), COMPLAINT_REPORT(ComplaintReportResp.class), SEGMENT_REPORT(SegmentReportRespList.class);
	Class<?> clazz;
	
	DownloadType(Class<?> clazz){
		this.clazz=clazz;
	}
}
