package com.rssoftware.ou.controller;


import in.co.rssoftware.bbps.schema.ErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.WeeklyReportReq;
import com.rssoftware.ou.common.ComplaintReportResp;
import com.rssoftware.ou.common.SegmentReportResp;
import com.rssoftware.ou.common.TxnReportResp;
import com.rssoftware.ou.tenant.service.ComplaintReportService;
import com.rssoftware.ou.tenant.service.SegmentReportService;
import com.rssoftware.ou.tenant.service.TxnReportService;

@Controller
@RequestMapping("/APIService/weeklyReport/urn:tenantId:{tenantId}")
public class WeeklyReportAPIController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	TxnReportService txnReportService;
	@Autowired
	ComplaintReportService complaintReportService;
	@Autowired
	SegmentReportService segmentReportService;
	/**
	 * To be used by admin portal to fetch weekly txn report
	 * 
	 * @param tenantId
	 * @param selectedDate
	 * @return
	 */
	@RequestMapping(value = "/txnReport", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> txnReport(@PathVariable String tenantId, final @RequestBody WeeklyReportReq weeklyReportReq) {
		String METHOD_NAME = "txnReport";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		ResponseEntity<?> response = null;
		try {
		TransactionContext.putTenantId(tenantId);
		response = new ResponseEntity(txnReportService.getTxnReport(weeklyReportReq.getSelectedDate()), HttpStatus.OK);
		}
		catch (Exception e) {
			log.error("Error: ", e);
			TxnReportResp txnReportResp = new TxnReportResp();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(txnReportResp, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	
	/**
	 * To be used by admin portal to fetch weekly complaint report
	 * 
	 * @param tenantId
	 * @param selectedDate
	 * @return
	 */
	@RequestMapping(value = "/complaintReport", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> complaintReport(@PathVariable String tenantId, final @RequestBody WeeklyReportReq weeklyReportReq) {
		String METHOD_NAME = "complaintReport";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		ResponseEntity<?> response = null;
		try {
		TransactionContext.putTenantId(tenantId);
		response = new ResponseEntity(complaintReportService.getComplaintReport(weeklyReportReq.getSelectedDate()), HttpStatus.OK);
		}
		catch (Exception e) {
			log.error("Error: ", e);
			ComplaintReportResp complaintReportResp = new ComplaintReportResp();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(complaintReportResp, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	/**
	 * To be used by admin portal to fetch weekly segment report
	 * 
	 * @param tenantId
	 * @param selectedDate
	 * @return
	 */
	@RequestMapping(value = "/segmentReport", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> segmentReport(@PathVariable String tenantId, final @RequestBody WeeklyReportReq weeklyReportReq) {
		String METHOD_NAME = "segmentReport";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		ResponseEntity<?> response = null;
		try {
		TransactionContext.putTenantId(tenantId);
		response = new ResponseEntity(segmentReportService.getSegmentReport(weeklyReportReq.getSelectedDate()), HttpStatus.OK);
		}
		catch (Exception e) {
			log.error("Error: ", e);
			SegmentReportResp segmentReportResp = new SegmentReportResp();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(segmentReportResp, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	/*@RequestMapping(value = "/exportTxnReport", method = RequestMethod.POST)
	public ByteArrayOutputStream exportTxnReport(@PathVariable String tenantId, @RequestBody TxnReportResp txnReportResp) {
		TransactionContext.putTenantId(tenantId);
	
		final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

		final String temperotyFilePath = tempDirectory.getAbsolutePath();

	

		String fileName = filename+".pdf";

		List<Image> images = new ArrayList<>();

		try {

			


			CreatePDF.createPDF(temperotyFilePath + "\\" + fileName,receiptLabel, receiptdata, images,title,header);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			return baos;
			baos = convertPDFToByteArrayOutputStream(temperotyFilePath + "\\" + fileName);

			OutputStream os = response.getOutputStream();

			baos.writeTo(os);

			os.flush();

		} 
		catch (Exception e) {


		}

	}*/

}
