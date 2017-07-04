package com.rssoftware.ou.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "SegmentReportRespList")
public class SegmentReportRespList extends BaseDownloadReq{
	
	List<SegmentReportResp> segmentReportResps;

	public List<SegmentReportResp> getSegmentReportResps() {
		if(segmentReportResps==null) {
			segmentReportResps = new ArrayList<SegmentReportResp>();
		}
		return segmentReportResps;
	}

	public void setSegmentReportResps(List<SegmentReportResp> segmentReportResps) {
		this.segmentReportResps = segmentReportResps;
	}
	
}