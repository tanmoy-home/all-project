package com.rssoftware.ou.download.service;

import java.io.File;

import com.rssoftware.ou.common.BaseDownloadReq;

public interface DownloadService {

	File  downloadReport(String outPutFormat,BaseDownloadReq baseDownloadReq );
}
