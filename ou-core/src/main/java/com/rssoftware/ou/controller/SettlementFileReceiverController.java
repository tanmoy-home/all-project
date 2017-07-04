package com.rssoftware.ou.controller;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rssoftware.ou.database.entity.tenant.SettlementFile;
import com.rssoftware.ou.gateway.SettlementFilePersisterGateway;
import com.rssoftware.ou.model.tenant.SettlementFileView;

@RestController
//@RequestMapping(value = "/APIService")
public class SettlementFileReceiverController {

	private static Log logger = LogFactory.getLog(SettlementFileReceiverController.class);
	
	@Autowired
	private SettlementFilePersisterGateway settlementFileGateway;

	@RequestMapping(value = "/{tenantId}/csv", method = RequestMethod.POST)
	public ResponseEntity<Void> csvSettlementFile(@PathVariable String tenantId, @RequestParam("file") MultipartFile file) throws IOException {
		settlementFileGateway.processSettlementFiles(getSettlementFileView(SettlementFile.FileType.CSV_REPORT, file, tenantId));
		logger.debug("Successfully Processed Settlement File , Type CSV , File Original Name " +file.getOriginalFilename());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{tenantId}/pdf", method = RequestMethod.POST)
	public ResponseEntity<Void> pdfSettlementFile(@PathVariable String tenantId, @RequestParam("file") MultipartFile file) throws IOException {
		settlementFileGateway.processSettlementFiles(getSettlementFileView(SettlementFile.FileType.PDF_REPORT, file, tenantId));
		logger.debug("Successfully Processed Settlement File , Type PDF , File Original Name " +file.getOriginalFilename());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{tenantId}/mnsb", method = RequestMethod.POST)
	public ResponseEntity<Void> mnsbSettlementFile(@PathVariable String tenantId, @RequestParam("file") MultipartFile file) throws IOException {
		settlementFileGateway.processSettlementFiles(getSettlementFileView(SettlementFile.FileType.MNSB, file, tenantId));
		logger.debug("Successfully Processed Settlement File , Type MNSB , File Original Name " +file.getOriginalFilename());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{tenantId}/txt", method = RequestMethod.POST)
	public ResponseEntity<Void> txtSettlementFile(@PathVariable String tenantId, @RequestParam("file") MultipartFile file) throws IOException {
		settlementFileGateway.processSettlementFiles(getSettlementFileView(SettlementFile.FileType.FINRAW, file, tenantId));
		logger.debug("Successfully Processed Settlement File , Type TXT , File Original Name " +file.getOriginalFilename());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{tenantId}/xls", method = RequestMethod.POST)
	public ResponseEntity<Void> xlsSettlementFile(@PathVariable String tenantId, @RequestParam("file") MultipartFile file) throws IOException {
		settlementFileGateway.processSettlementFiles(getSettlementFileView(SettlementFile.FileType.XLS_REPORT, file, tenantId));
		logger.debug("Successfully Processed Settlement File , Type XLS , File Original Name " +file.getOriginalFilename());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	private SettlementFileView getSettlementFileView(SettlementFile.FileType fileType, MultipartFile file, String tenantId) throws IOException {
		SettlementFileView sFileView = new SettlementFileView();
		sFileView.setFile(file.getBytes());	
		sFileView.setFileType(fileType);
		sFileView.setTenantId(tenantId);
		sFileView.setFileName(getFileName(file));
		return sFileView;
	}
	
	private String getFileName(MultipartFile file) {
		return file.getOriginalFilename().substring(
				file.getOriginalFilename().lastIndexOf("/")+1, 
				file.getOriginalFilename().lastIndexOf("."));
	}

}