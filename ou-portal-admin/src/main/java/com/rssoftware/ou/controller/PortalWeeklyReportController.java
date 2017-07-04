package com.rssoftware.ou.controller;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rssoftware.ou.common.APIURL;
import com.rssoftware.ou.common.BaseDownloadReq;
import com.rssoftware.ou.common.ComplaintReportResp;
import com.rssoftware.ou.common.DownloadType;
import com.rssoftware.ou.common.PortalUtils;
import com.rssoftware.ou.common.SegmentReportResp;
import com.rssoftware.ou.common.SegmentReportRespList;
import com.rssoftware.ou.common.TxnReportResp;
import com.rssoftware.ou.common.WeeklyReportReq;
import com.rssoftware.ou.domain.JsonResponse;
import com.rssoftware.ou.download.service.DownloadService;
import com.rssoftware.ou.service.UserService;

@Controller
@RequestMapping(value = "/weeklyReport")
public class PortalWeeklyReportController {

	private static final Logger LOGGER= LoggerFactory.getLogger(PortalWeeklyReportController.class);

	private static OUInternalRestTemplate ouInternalRestTemplate = OUInternalRestTemplate.createInstance();
		
	@Autowired
	UserService userService;
	
	@Autowired
	DownloadService downloadService;
	
	@RequestMapping(value = "/txnReport", method = RequestMethod.GET)
	public ModelAndView txnReport(Model model) {
		String METHOD_NAME = "txnReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("viewTxnReport");
	}

	@RequestMapping(value = "/viewTxnReport", method = RequestMethod.POST)
	public @ResponseBody JsonResponse viewTxnReport(Model model, @RequestBody String str) {
		
		String METHOD_NAME = "viewTxnReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();

		JSONObject jsonObject;
		TxnReportResp txnReportResp = new TxnReportResp();
		try {
			jsonObject = new JSONObject(str);
			String selectedDate = jsonObject.getString("selectedDate");
			WeeklyReportReq weeklyReportReq = new WeeklyReportReq();
			weeklyReportReq.setSelectedDate(selectedDate);
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.postForEntity(APIURL.TXN_REPORT_URL,
					PortalUtils.getHttpHeader(weeklyReportReq, userService), TxnReportResp.class);
			txnReportResp = (TxnReportResp) responseEntity.getBody();
		} catch (JSONException e) {
			LOGGER.error("In class PortalWeeklyReportController >> viewTxnReport: ", e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received Resp From Api OU_NAME: " + txnReportResp.getBbpouName());
		}
		json.setResult(txnReportResp);
		return json;
	}
	
	@RequestMapping(value = "/downloadTxnReport", method = RequestMethod.POST, produces = {"application/json",
	          "application/xls", "application/csv", "application/octet-stream",})
	public @ResponseBody JsonResponse downloadTxnReport(Model model, @RequestParam(value = "inData") String str, HttpServletResponse response) {
		String METHOD_NAME = "downloadTxnReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		JSONObject jsonObject;
		File file = null;
		TxnReportResp req = new TxnReportResp();
		try {
			jsonObject = new JSONObject(str);
			
			String bbpouName = jsonObject.getString("bbpouName");
			double noOfAgentOutlets = jsonObject.getDouble("noOfAgentOutlets");
			double onUsTxnCount = jsonObject.getDouble("onUsTxnCount");
			double offUsTxnCount = jsonObject.getDouble("offUsTxnCount");
			double onUsTxnTot = jsonObject.getDouble("onUsTxnTot");
			double offUsTxnTot = jsonObject.getDouble("offUsTxnTot");
			double onUsFailedTxnCount = jsonObject.getDouble("onUsFailedTxnCount");
			double offUsFailedTxnCount = jsonObject.getDouble("offUsFailedTxnCount");
			double onUsFailedTxnTot = jsonObject.getDouble("onUsFailedTxnTot");
			double offUsFailedTxnTot = jsonObject.getDouble("offUsFailedTxnTot");
			String failureReason = jsonObject.getString("failureReason");
			double cashPaymentCount = jsonObject.getDouble("cashPaymentCount");
			double dcccpaymentCount = jsonObject.getDouble("dcccpaymentCount");
			double netBankingPaymentCount = jsonObject.getDouble("netBankingPaymentCount");
			double impspaymentCount = jsonObject.getDouble("impspaymentCount");
			double ppisPaymentCount = jsonObject.getDouble("ppisPaymentCount");
			double otherPaymentCount = jsonObject.getDouble("otherPaymentCount");

			req.setBbpouName(bbpouName);
			req.setNoOfAgentOutlets(new BigDecimal(noOfAgentOutlets));
			req.setOnUsTxnCount(new BigDecimal(onUsTxnCount));
			req.setOffUsTxnCount(new BigDecimal(offUsTxnCount));
			req.setOnUsTxnTot(new BigDecimal(onUsTxnTot));
			req.setOffUsTxnTot(new BigDecimal(offUsTxnTot));
			req.setOnUsFailedTxnCount(new BigDecimal(onUsFailedTxnCount));
			req.setOffUsFailedTxnCount(new BigDecimal(offUsFailedTxnCount));
			req.setOnUsFailedTxnTot(new BigDecimal(onUsFailedTxnTot));
			req.setOffUsFailedTxnTot(new BigDecimal(offUsFailedTxnTot));
			req.setFailureReason(failureReason);
			req.setCashPaymentCount(new BigDecimal(cashPaymentCount));
			req.setDCCCPaymentCount(new BigDecimal(dcccpaymentCount));
			req.setNetBankingPaymentCount(new BigDecimal(netBankingPaymentCount));
			req.setIMPSPaymentCount(new BigDecimal(impspaymentCount));
			req.setPPIsPaymentCount(new BigDecimal(ppisPaymentCount));
			req.setOtherPaymentCount(new BigDecimal(otherPaymentCount));
			BaseDownloadReq baseDownloadReq = req;
			baseDownloadReq.setDownloadType(DownloadType.TXN_REPORT);
			file = (File) downloadService.downloadReport("xls", baseDownloadReq);
			FileInputStream resource = new FileInputStream(file);
			PortalUtils.flushFile(response,file.getName(),file.length(),resource);
		} catch (Exception e) {
			LOGGER.error("In class PortalWeeklyReportController >> downloadTxnReport: ", e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received Resp From Api array size " + file.length());
		}
		
		return json;
	}	
	
	@RequestMapping(value = "/complaintReport", method = RequestMethod.GET)
	public ModelAndView complaintReport(Model model) {
		String METHOD_NAME = "complaintReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("viewComplaintReport");
	}
	@RequestMapping(value = "/viewComplaintReport", method = RequestMethod.POST)
	public @ResponseBody JsonResponse viewComplaintReport(Model model, @RequestBody String str) {
		String METHOD_NAME = "viewComplaintReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		JSONObject jsonObject;
		ComplaintReportResp complaintReportResp = new ComplaintReportResp();
		try {
			jsonObject = new JSONObject(str);
			String complaintDate = jsonObject.getString("complaintDate");
			WeeklyReportReq weeklyReportReq = new WeeklyReportReq();
			weeklyReportReq.setSelectedDate(complaintDate);
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.postForEntity(APIURL.COMPLAINT_REPORT_URL,
					PortalUtils.getHttpHeader(weeklyReportReq, userService), ComplaintReportResp.class);
			complaintReportResp = (ComplaintReportResp) responseEntity.getBody();
		} catch (JSONException e) {
			LOGGER.error("Error: ", e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received Resp From Api OU_NAME: " + complaintReportResp.getBbpouName());
		}
		json.setResult(complaintReportResp);
		return json;
	}
	@RequestMapping(value = "/downloadComplaintReport", method = RequestMethod.POST, produces = {"application/json",
	          "application/xls", "application/csv", "application/octet-stream",})
	public @ResponseBody JsonResponse downloadComplaintReport(Model model, @RequestParam(value = "inData") String str, HttpServletResponse response) {
		String METHOD_NAME = "downloadComplaintReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		JSONObject jsonObject;
		File file = null;
		ComplaintReportResp req = new ComplaintReportResp();
		try {
			jsonObject = new JSONObject(str);
			req.setBbpouName(jsonObject.getString("bbpouName"));
			req.setOnUsoutstandingLastWeekCount(new BigDecimal(jsonObject.getDouble("onUsoutstandingLastWeekCount")));
			req.setOnUsreceivedThisWeekCount(new BigDecimal(jsonObject.getDouble("onUsreceivedThisWeekCount")));
			req.setOnUsTot(new BigDecimal(jsonObject.getDouble("onUsTot")));
			req.setOffUsoutstandingLastWeekCount(new BigDecimal(jsonObject.getDouble("offUsoutstandingLastWeekCount")));
			req.setOffUsreceivedThisWeekCount(new BigDecimal(jsonObject.getDouble("offUsreceivedThisWeekCount")));
			req.setOffUsTot(new BigDecimal(jsonObject.getDouble("offUsTot")));
			req.setOnUsResolvedCount(new BigDecimal(jsonObject.getDouble("onUsResolvedCount")));
			req.setOffUsResolvedCount(new BigDecimal(jsonObject.getDouble("offUsResolvedCount")));
			req.setOnUsPendingCount(new BigDecimal(jsonObject.getDouble("onUsPendingCount")));
			req.setOffUsPendingCount(new BigDecimal(jsonObject.getDouble("offUsPendingCount")));
			req.setTxnBasedCount(new BigDecimal(jsonObject.getDouble("txnBasedCount")));
			req.setServiceBasedCount(new BigDecimal(jsonObject.getDouble("serviceBasedCount")));
		
			BaseDownloadReq baseDownloadReq = req;
			baseDownloadReq.setDownloadType(DownloadType.COMPLAINT_REPORT);
			file = (File) downloadService.downloadReport("xls", baseDownloadReq);
			FileInputStream resource = new FileInputStream(file);
			PortalUtils.flushFile(response,file.getName(),file.length(),resource);
		} catch (Exception e) {
			LOGGER.error("In class PortalWeeklyReportController >> downloadComplaintReport: ", e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received Resp From Api array size " + file.length());
		}
		
		return json;
	}	
	@RequestMapping(value = "/segmentReport", method = RequestMethod.GET)
	public ModelAndView segmentReport(Model model) {
		String METHOD_NAME = "segmentReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("viewSegmentReport");
	}
	
	@RequestMapping(value = "/viewSegmentReport", method = RequestMethod.POST)
	public @ResponseBody JsonResponse viewSegmentReport(HttpServletRequest request, Model model, @RequestBody String str) {
		String METHOD_NAME = "viewSegmentReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		JSONObject jsonObject;
		SegmentReportRespList segmentReportRespList = new SegmentReportRespList();
		try {
			jsonObject = new JSONObject(str);
			String segmentDate = jsonObject.getString("segmentDate");
			WeeklyReportReq weeklyReportReq = new WeeklyReportReq();
			weeklyReportReq.setSelectedDate(segmentDate);
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.postForEntity(APIURL.SEGMENT_REPORT_URL,
					PortalUtils.getHttpHeader(weeklyReportReq, userService), SegmentReportRespList.class);
			segmentReportRespList = (SegmentReportRespList) responseEntity.getBody();
		} catch (JSONException e) {
			LOGGER.error("In class PortalWeeklyReportController >> viewSegmentReport: ", e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received Resp From Api OU_NAME: " + segmentReportRespList.getSegmentReportResps().get(0).getBbpouName());
		}
		json.setResult(segmentReportRespList.getSegmentReportResps());
		return json;
	}
	@RequestMapping(value = "/downloadSegmentReport", method = RequestMethod.POST, produces = {"application/json",
	          "application/xls", "application/csv", "application/octet-stream",})
	public @ResponseBody JsonResponse downloadSegmentReport(Model model, @RequestParam(value = "inData") String str, HttpServletResponse response) {
		String METHOD_NAME = "downloadSegmentReport";
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		JSONArray jsonArray;
		File file = null;
		SegmentReportRespList reqList = new SegmentReportRespList();
		SegmentReportResp req = null;
		List<SegmentReportResp> segmentReportResps = new ArrayList<SegmentReportResp>();
		try {
			jsonArray = new JSONArray(str);
			for(int i = 0; i< jsonArray.length(); i++) {
			    JSONObject row = jsonArray.getJSONObject(i);
			    req = new SegmentReportResp();
			req.setBbpouName(row.getString("bbpouName"));
			req.setBlrCategory(row.getString("blrCategory"));
			req.setBlrName(row.getString("blrName"));
			req.setOnUsCount(new BigDecimal(row.getDouble("onUsCount")));
			req.setOffUsCount(new BigDecimal(row.getDouble("offUsCount")));
			req.setOnUsTot(new BigDecimal(row.getDouble("onUsTot")));
			req.setOffUsTot(new BigDecimal(row.getDouble("offUsTot")));
			segmentReportResps.add(req);
			}
			reqList.getSegmentReportResps().addAll(segmentReportResps);
			BaseDownloadReq baseDownloadReq = reqList;
			baseDownloadReq.setDownloadType(DownloadType.SEGMENT_REPORT);
			file = (File) downloadService.downloadReport("xls", baseDownloadReq);
			FileInputStream resource = new FileInputStream(file);
			PortalUtils.flushFile(response,file.getName(),file.length(),resource);
		} catch (Exception e) {
			LOGGER.error("In class PortalWeeklyReportController >> downloadSegmentReport: ", e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Received Resp From Api array size " + file.length());
		}
		
		return json;
	}	
}