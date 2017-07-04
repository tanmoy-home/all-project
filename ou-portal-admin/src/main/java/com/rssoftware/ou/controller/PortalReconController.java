package com.rssoftware.ou.controller;

import in.co.rssoftware.bbps.schema.ReconSummaryList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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
import com.rssoftware.ou.common.PortalUtils;
import com.rssoftware.ou.domain.JsonResponse;
import com.rssoftware.ou.model.tenant.ReconFileViews;
import com.rssoftware.ou.service.UserService;

@Controller
@RequestMapping(value = "/reconciliation")
public class PortalReconController {

	private static final Logger LOG = LoggerFactory.getLogger(PortalReconController.class);

	private static OUInternalRestTemplate ouInternalRestTemplate = OUInternalRestTemplate.createInstance();

	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/pgReconFiles", method = RequestMethod.GET)
	public ModelAndView pgReconFiles(Model model) {
		String METHOD_NAME = "pgReconFiles";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("viewPGFiles");
	}

	@RequestMapping(value = "/viewPGReconFiles", method = RequestMethod.POST)
	public @ResponseBody JsonResponse viewPGReconFiles(Model model) {
		JsonResponse json = new JsonResponse();
		String METHOD_NAME = "viewPGReconFiles";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		URI uri = URI.create(APIURL.PG_RECON_REPORT_URL);
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(uri, HttpMethod.POST, PortalUtils.getHttpEntity(userService), ReconFileViews.class);
		ReconFileViews reconFileViews = (ReconFileViews) responseEntity.getBody();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api Length: " + reconFileViews.getReconFileViews().size());
		}
		json.setResult(reconFileViews.getReconFileViews());
		return json;
	}
	
	@RequestMapping(value = "/blrReconFiles", method = RequestMethod.GET)
	public ModelAndView blrReconFiles(Model model) {
		String METHOD_NAME = "blrReconFiles";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("viewBillerFiles");
	}
	@RequestMapping(value = "/viewBlrReconFiles", method = RequestMethod.POST)
	public @ResponseBody JsonResponse viewBlrReconFiles(Model model) {
		JsonResponse json = new JsonResponse();
		String METHOD_NAME = "viewBlrReconFiles";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		URI uri = URI.create(APIURL.BLR_RECON_REPORT_URL);
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(uri, HttpMethod.POST, PortalUtils.getHttpEntity(userService), ReconFileViews.class);
		ReconFileViews reconFileViews = (ReconFileViews) responseEntity.getBody();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api Length: " + reconFileViews.getReconFileViews().size());
		}
		json.setResult(reconFileViews.getReconFileViews());
		return json;
	}
	
	private void flushFile(HttpServletResponse response,String fileName,long contentLength, FileInputStream inputStream)
		      throws IOException {
		    response.setContentType("application/octet-stream");
		    response.setContentLength((int)contentLength);
		    String headerKey = "Content-Disposition";
		    String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		    response.setHeader(headerKey, headerValue);
		    OutputStream outStream = response.getOutputStream();
		    byte[] buffer = new byte[4096];
		    int bytesRead = -1;
		    while ((bytesRead = inputStream.read(buffer)) != -1) {
		      outStream.write(buffer, 0, bytesRead);
		    }
		    inputStream.close();
		    outStream.close();
		  }
	
	@RequestMapping(value = "/download", method = RequestMethod.POST, produces = {"application/json",
	          "application/xls", "application/csv", "application/octet-stream",})
	public @ResponseBody JsonResponse download(HttpServletRequest request, Model model, @RequestParam(value = "inData") String str, HttpServletResponse response) {
		JsonResponse json = new JsonResponse();
		String METHOD_NAME = "download";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(str);
			String fileName = jsonObject.getString("fileName");
			String fileType = jsonObject.getString("fileType");
			URI uri = URI.create(APIURL.DOWNLOAD_FILE_URL+ "/" +fileName+ "/" +fileType+ "/viewFile");
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(uri, HttpMethod.POST, PortalUtils.getHttpEntity(userService), File.class);
			File file = (File) responseEntity.getBody();
			FileInputStream resource = new FileInputStream(file);
			flushFile(response,fileName,file.length(),resource);
//            ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
//            try {
//            	IOUtils.copy(resource, outputStream);
//            } 
//			catch (IOException e) {
//    			LOG.error("Error: ", e);
//            } 
//			finally {
//                outputStream.close();
//            }
//            byte[] byteArray = outputStream.toByteArray();
//            if (LOG.isDebugEnabled()) {
//    			LOG.debug("Received File From Api Length: " + file.length());
//    		}			
//            json.setResult(byteArray);
		} 
		catch (JSONException e) {
			LOG.error("In class PortalReconController >> download JSONException: ", e);
		}
		catch (Exception e) {
			LOG.error("In class PortalReconController >> download Exception: ", e);
		}
		return json;
	}
	
	@RequestMapping(value = "/cuouReconFiles", method = RequestMethod.GET)
	public ModelAndView viewCUOUReconFiles(Model model) {
		String METHOD_NAME = "cuouReconFiles";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("viewCUOUReconFiles");
	}
	
	@RequestMapping(value = "/reconDetailsReport", method = RequestMethod.POST)
	public @ResponseBody JsonResponse reconReport(HttpServletRequest request, Model model, @RequestBody String str) {
		JSONObject jsonObject;
		JsonResponse json = new JsonResponse();
		ReconSummaryList reconLists = new ReconSummaryList();
		try {
			jsonObject = new JSONObject(str);
			String stDate = jsonObject.getString("startDt");
			String endDate = jsonObject.getString("endDt"); 
			URI uri = URI.create(APIURL.RECON_CUOU_REPORT_URL+"?startDate=" + stDate + "&endDate=" + endDate);
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(uri, HttpMethod.POST, PortalUtils.getHttpEntity(userService),ReconSummaryList.class);
			reconLists = (ReconSummaryList) responseEntity.getBody();
		} catch (JSONException e) {
			LOG.error("In class PortalReconController >> reconReport: ", e);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api Length: " + reconLists.getReconSummaryLists().size());
		}
		json.setResult(reconLists.getReconSummaryLists());
		return json;
	}
}