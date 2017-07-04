package com.rssoftware.ou.download.service.impl;

import java.io.File;

import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.BaseDownloadReq;
import com.rssoftware.ou.common.ComplaintReportResp;
import com.rssoftware.ou.common.SegmentReportRespList;
import com.rssoftware.ou.common.TxnReportResp;
import com.rssoftware.ou.download.service.DownloadService;

@Service("downloadService")
public class DownloadServiceImpl implements DownloadService {

	@Override
	public File downloadReport(String outPutFormat, BaseDownloadReq baseDownloadReq) {
		
		File file = null;
		switch (baseDownloadReq.getDownloadType())
		{
		case TXN_REPORT: 
			file = new ExcelGenerator().generateTxnReportExcel((TxnReportResp)baseDownloadReq);
		break;
		case SEGMENT_REPORT:
			file = new ExcelGenerator().generateSegmentReportExcel((SegmentReportRespList)baseDownloadReq);
			break;
		case COMPLAINT_REPORT:
			file = new ExcelGenerator().generateComplaintReportExcel((ComplaintReportResp)baseDownloadReq);
			break;
		default:
			break;
		}
		
		return file;
	}

	
}
