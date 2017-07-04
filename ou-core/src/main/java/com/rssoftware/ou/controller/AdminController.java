package com.rssoftware.ou.controller;

import in.co.rssoftware.bbps.schema.BillerFetchByParameter;
import in.co.rssoftware.bbps.schema.ComplainRequest;
import in.co.rssoftware.bbps.schema.ComplainStatusRequest;
import in.co.rssoftware.bbps.schema.TransactionSearchRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXB;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillPaymentRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.TypeOfBatch;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.RequestResponseGenerator;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.BillerFetchRequestResponse;
import com.rssoftware.ou.database.entity.tenant.TxnReportReq;
import com.rssoftware.ou.gateway.BillFetchGateway;
import com.rssoftware.ou.gateway.BillPaymentGateway;
import com.rssoftware.ou.service.ComplaintService;
import com.rssoftware.ou.service.SharedSchedulerService;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.BillerFetchRequestResponseService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

/*
 * Handles requests for the PSP Request Response Cycle.
 */
@RestController
@RequestMapping("/admin/urn:tenantId:{tenantId}")
public class AdminController {
	private final Logger log = LoggerFactory.getLogger(getClass());
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private SharedSchedulerService sharedSchedulerService; 
	
	@Autowired
	private IDGeneratorService idGeneratorService;
	
	@Autowired
	private TenantDetailService tenantDetailService;
	
	@Autowired
	private TransactionDataService transactionDataService;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private BillFetchGateway billFetchGateway;
	
	@Autowired
	private BillPaymentGateway billPaymentGateway;
	
	@Autowired
	private BillerFetchRequestResponseService billerFetchRequestResponseService;
	
	@RequestMapping(value = "/scheduleHeartbeatIntervalMins", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> scheduleHeartbeat(@PathVariable String tenantId, final @RequestBody String intervalStr)
			throws SchedulerException {
		String METHOD_NAME = "scheduleHeartbeat";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(intervalStr) || !CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(intervalStr,
						HttpStatus.EXPECTATION_FAILED);
			}

			int interval = 0;
			
			try {
				interval = Integer.parseInt(intervalStr);
			} catch (Exception e) {
				return new ResponseEntity<String>(intervalStr,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			sharedSchedulerService.unSchedule(TypeOfBatch.HEARTBEAT.name());
			
			String schedulerCron = "0 0/"+ interval +" * * * ?";
			sharedSchedulerService.schedule(TypeOfBatch.HEARTBEAT.name(), TypeOfBatch.HEARTBEAT, schedulerCron);
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (SchedulerException e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	@RequestMapping(value = "/unscheduleHeartbeat", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> unscheduleHeartbeat(@PathVariable String tenantId)
			throws SchedulerException {
		String METHOD_NAME = "unscheduleHeartbeat";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId,
						HttpStatus.EXPECTATION_FAILED);
			}
			TransactionContext.putTenantId(tenantId);
			sharedSchedulerService.unSchedule(TypeOfBatch.HEARTBEAT.name());
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (SchedulerException e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/scheduleBillerFetch", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> scheduleBillerFetch(@PathVariable String tenantId, final @RequestBody String hrColonMins)
			throws SchedulerException {
		String METHOD_NAME = "scheduleBillerFetch";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(hrColonMins) || !CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			String[] hrMins = hrColonMins.split(",");
			
			if (hrMins == null || hrMins.length == 0){
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			List<DateTime> listDateTime = new ArrayList<DateTime>();
			for (String hrColMin:hrMins){
				String[] hrMin = hrColMin.trim().split(":");

				int hr = 0;
				int min = 0;
				
				try {
					hr = Integer.parseInt(hrMin[0]);
					min = Integer.parseInt(hrMin[1]);
				} catch (Exception e) {
					return new ResponseEntity<String>(hrColonMins,
							HttpStatus.EXPECTATION_FAILED);
				}
				
				if (hr < 0 || hr > 23 || min < 0 || min > 59){
					return new ResponseEntity<String>(hrColonMins,
							HttpStatus.EXPECTATION_FAILED);
				}
				
				DateTime dateTime = DateTime.parse(hrColMin, DateTimeFormat.forPattern("HH:mm"));
				listDateTime.add(dateTime);
			}

			if (listDateTime.isEmpty()){
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			int jobCount = 1;
			while (true){
				if (sharedSchedulerService.isJobExist(TypeOfBatch.BILLER_FETCH.name()+'_'+jobCount)){
					sharedSchedulerService.unSchedule(TypeOfBatch.BILLER_FETCH.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			Collections.sort(listDateTime);
			
			jobCount = 1;
			for (DateTime dateTime:listDateTime)
			{
				String settlemntTime = DateTimeFormat.forPattern("HH:mm").print(dateTime);
				String[] hrMin = settlemntTime.split(":");
				String schedulerCron = "0 "+hrMin[1]+" "+hrMin[0]+" * * ?";

				sharedSchedulerService.schedule(TypeOfBatch.BILLER_FETCH.name()+'_'+jobCount, TypeOfBatch.BILLER_FETCH, schedulerCron);
				jobCount++;
			}
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (SchedulerException e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	@RequestMapping(value = "/unscheduleBillerFetch", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> unscheduleBillerFetch(@PathVariable String tenantId)
			throws SchedulerException {
		String METHOD_NAME = "unscheduleBillerFetch";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			int jobCount = 1;
			while (true){
				if (sharedSchedulerService.isJobExist(TypeOfBatch.BILLER_FETCH.name()+'_'+jobCount)){
					sharedSchedulerService.unSchedule(TypeOfBatch.BILLER_FETCH.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (SchedulerException e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}	
	
	/*
	 * Schedule/Unscheduled Bill payment and fetch operation for a particular Biller OU
	 */
	@RequestMapping(value = "/billerFunction:{billerFunction}", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> billerFunction(@PathVariable String tenantId, @PathVariable String billerFunction, 
			final @RequestBody(required=false) String hrColonMins)
			throws SchedulerException {
		String METHOD_NAME = billerFunction;
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			
			if(billerFunction.equalsIgnoreCase("scheduleBillPayment"))
				scheduleBatch(tenantId,hrColonMins,TypeOfBatch.BILL_PAYMENT);
			if(billerFunction.equalsIgnoreCase("unscheduleBillPayment"))
				unscheduleBatch(tenantId,TypeOfBatch.BILL_PAYMENT);
			if(billerFunction.equalsIgnoreCase("scheduleBillFetch"))
				scheduleBatch(tenantId,hrColonMins,TypeOfBatch.BILL_FETCH);
			if(billerFunction.equalsIgnoreCase("unscheduleBillFetch"))
				unscheduleBatch(tenantId,TypeOfBatch.BILL_FETCH);
			if(billerFunction.equalsIgnoreCase("scheduleBillPaymentReport"))
				scheduleBatch(tenantId,hrColonMins,TypeOfBatch.BILL_PAYMENT_REPORT);
			
			return new ResponseEntity<String>(HttpStatus.OK);
			
			
		}
		catch (SchedulerException e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	/*
	 * Common scheduling activities for all kind of batch processing
	 */
	private ResponseEntity<String> scheduleBatch(String tenantId, String hrColonMins, TypeOfBatch batch) throws SchedulerException
	{

		if (!CommonUtils.hasValue(hrColonMins) || !CommonUtils.hasValue(tenantId)){
			return new ResponseEntity<String>(hrColonMins,
					HttpStatus.EXPECTATION_FAILED);
		}
		
		TransactionContext.putTenantId(tenantId);
		String[] hrMins = hrColonMins.split(",");
		
		if (hrMins == null || hrMins.length == 0){
			return new ResponseEntity<String>(hrColonMins,
					HttpStatus.EXPECTATION_FAILED);
		}
		
		List<DateTime> listDateTime = new ArrayList<DateTime>();
		for (String hrColMin:hrMins){
			String[] hrMin = hrColMin.trim().split(":");

			int hr = 0;
			int min = 0;
			
			try {
				hr = Integer.parseInt(hrMin[0]);
				min = Integer.parseInt(hrMin[1]);
			} catch (Exception e) {
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			if (hr < 0 || hr > 23 || min < 0 || min > 59){
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			DateTime dateTime = DateTime.parse(hrColMin, DateTimeFormat.forPattern("HH:mm"));
			listDateTime.add(dateTime);
		}

		if (listDateTime.isEmpty()){
			return new ResponseEntity<String>(hrColonMins,
					HttpStatus.EXPECTATION_FAILED);
		}
		
		int jobCount = 1;
		while (true){
			if (sharedSchedulerService.isJobExist(batch.name()+'_'+jobCount)){
				sharedSchedulerService.unSchedule(batch.name()+'_'+jobCount);
				jobCount++;
			}
			else {
				break;
			}
		}
		Collections.sort(listDateTime);
		
		jobCount = 1;
		for (DateTime dateTime:listDateTime)
		{
			String settlemntTime = DateTimeFormat.forPattern("HH:mm").print(dateTime);
			String[] hrMin = settlemntTime.split(":");
			String schedulerCron = "0 "+hrMin[1]+" "+hrMin[0]+" * * ?";
			sharedSchedulerService.schedule(batch.name()+'_'+jobCount, batch, schedulerCron);
			jobCount++;
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	
	}

	/*
	 * Common unscheduling activities for all kind of batch processing
	 */
	private ResponseEntity<String> unscheduleBatch(String tenantId, TypeOfBatch batch) throws SchedulerException
	{

		if (!CommonUtils.hasValue(tenantId)){
			return new ResponseEntity<String>(tenantId,
					HttpStatus.EXPECTATION_FAILED);
		}
		
		TransactionContext.putTenantId(tenantId);
		
		int jobCount = 1;
		while (true){
			if (sharedSchedulerService.isJobExist(batch.name()+'_'+jobCount)){
				sharedSchedulerService.unSchedule(batch.name()+'_'+jobCount);
				jobCount++;
			}
			else {
				break;
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	
	}

	@RequestMapping(value = "/scheduleFinRecon", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> scheduleFinRecon(@PathVariable String tenantId, final @RequestBody String hrColonMins) throws Exception {
		
		String METHOD_NAME = "scheduleFinRecon";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(hrColonMins) || !CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			String[] hrMins = hrColonMins.split(",");
			
			if (hrMins == null || hrMins.length == 0){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			List<DateTime> listDateTime = new ArrayList<DateTime>();
			for (String hrColMin:hrMins){
				String[] hrMin = hrColMin.trim().split(":");

				int hr = 0;
				int min = 0;
				
				try {
					hr = Integer.parseInt(hrMin[0]);
					min = Integer.parseInt(hrMin[1]);
				} 
				catch (Exception e) {
					return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
				}
				
				if (hr < 0 || hr > 23 || min < 0 || min > 59){
					return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
				}
				
				DateTime dateTime = DateTime.parse(hrColMin, DateTimeFormat.forPattern("HH:mm"));
				listDateTime.add(dateTime);
			}

			if (listDateTime.isEmpty()){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			int jobCount = 1;
			while (true){
				if (sharedSchedulerService.isJobExist(TypeOfBatch.FIN_RECON.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.FIN_RECON.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			Collections.sort(listDateTime);
			
			jobCount = 1;
			for (DateTime dateTime:listDateTime)
			{
				String settlemntTime = DateTimeFormat.forPattern("HH:mm").print(dateTime);
				String[] hrMin = settlemntTime.split(":");
				String schedulerCron = "0 "+hrMin[1]+" "+hrMin[0]+" * * ?";
				//String schedulerCron = "0/10 * * * * ?";
				//String schedulerCron = "0 0 18 * * ?";
				sharedSchedulerService.schedule(TypeOfBatch.FIN_RECON.name()+'_'+jobCount, TypeOfBatch.FIN_RECON, schedulerCron);
				jobCount++;
			}
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/unscheduleFinRecon", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> unscheduleFinRecon(@PathVariable String tenantId) throws Exception {
		
		String METHOD_NAME = "unscheduleFinRecon";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId, HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			int jobCount = 1;
			while (true) {
				if (sharedSchedulerService.isJobExist(TypeOfBatch.FIN_RECON.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.FIN_RECON.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	
	
	
	
	
	@RequestMapping(value = "/scheduleBOUBlrRecon", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> scheduleBOUBlrRecon(@PathVariable String tenantId, final @RequestBody String hrColonMins) throws Exception {
		
		String METHOD_NAME = "scheduleBOUBlrRecon";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(hrColonMins) || !CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			String[] hrMins = hrColonMins.split(",");
			
			if (hrMins == null || hrMins.length == 0){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			List<DateTime> listDateTime = new ArrayList<DateTime>();
			for (String hrColMin:hrMins){
				String[] hrMin = hrColMin.trim().split(":");

				int hr = 0;
				int min = 0;
				
				try {
					hr = Integer.parseInt(hrMin[0]);
					min = Integer.parseInt(hrMin[1]);
				} 
				catch (Exception e) {
					return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
				}
				
				if (hr < 0 || hr > 23 || min < 0 || min > 59){
					return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
				}
				
				DateTime dateTime = DateTime.parse(hrColMin, DateTimeFormat.forPattern("HH:mm"));
				listDateTime.add(dateTime);
			}

			if (listDateTime.isEmpty()){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			int jobCount = 1;
			while (true){
				if (sharedSchedulerService.isJobExist(TypeOfBatch.BOU_BLR_RECON.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.BOU_BLR_RECON.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			Collections.sort(listDateTime);
			
			jobCount = 1;
			for (DateTime dateTime:listDateTime)
			{
				String settlemntTime = DateTimeFormat.forPattern("HH:mm").print(dateTime);
				String[] hrMin = settlemntTime.split(":");
				String schedulerCron = "0 "+hrMin[1]+" "+hrMin[0]+" * * ?";
				//String schedulerCron = "0/10 * * * * ?";
				//String schedulerCron = "0 0 18 * * ?";
				sharedSchedulerService.schedule(TypeOfBatch.BOU_BLR_RECON.name()+'_'+jobCount, TypeOfBatch.BOU_BLR_RECON, schedulerCron);
				jobCount++;
			}
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/unscheduleBOUBlrRecon", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> unscheduleBOUBlrRecon(@PathVariable String tenantId) throws Exception {
		
		String METHOD_NAME = "unscheduleBOUBlrRecon";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId, HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			int jobCount = 1;
			while (true) {
				if (sharedSchedulerService.isJobExist(TypeOfBatch.BOU_BLR_RECON.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.BOU_BLR_RECON.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	
	@RequestMapping(value = "/{tenantId}/executeBillFetch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEntity<String> executeBillFetch(HttpServletRequest request, @PathVariable String tenantId,final @RequestBody String fileNamePrefix) {
		String METHOD_NAME = "executeBillFetch";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId,
						HttpStatus.EXPECTATION_FAILED);
			}
			TransactionContext.putTenantId(tenantId);
			TenantDetail tenantDetail = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId());
			String refId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, tenantDetail.getOuName());
			String msgId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, tenantDetail.getOuName());
			String filePath = System.getProperty("file.path");
			BillFetchRequest billFetchRequest = JAXB.unmarshal(new File(filePath+"/BillFetchRequest"+fileNamePrefix+".xml"), BillFetchRequest.class);
			billFetchRequest.getHead().setTs(CommonUtils.getFormattedCurrentTimestamp());
			billFetchRequest.setAnalytics(RequestResponseGenerator.getAnalytics());
			billFetchRequest.getHead().setRefId(refId);
			billFetchRequest.getTxn().setMsgId(msgId);
			billFetchRequest.getTxn().setTs(CommonUtils.getFormattedCurrentTimestamp());
			billFetchGateway.processOUBillFetchRequest(billFetchRequest);
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e){
			log.error(METHOD_NAME,e);
			//throw e;
			return new ResponseEntity<String>(tenantId,
					HttpStatus.EXPECTATION_FAILED);
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/{tenantId}/executeBillPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEntity<String> executeBillPayment(HttpServletRequest request, @PathVariable String tenantId,final @RequestBody String fileNamePrefix) {
		String METHOD_NAME = "executeBillPayment";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId,
						HttpStatus.EXPECTATION_FAILED);
			}
			TransactionContext.putTenantId(tenantId);
			TenantDetail tenantDetail = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId());
//			String refId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, tenantDetail.getOuName());
			String msgId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, tenantDetail.getOuName());
			String txnRefId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_TXN_REF_ID, tenantDetail.getOuName());
			String filePath = System.getProperty("file.path");
			BillPaymentRequest billPaymentRequest = JAXB.unmarshal(new File(filePath+"/BillPaymentRequest"+fileNamePrefix+".xml"), BillPaymentRequest.class);
			billPaymentRequest.getHead().setTs(CommonUtils.getFormattedCurrentTimestamp());
			billPaymentRequest.setAnalytics(RequestResponseGenerator.getPaymentAnalytics());
//			billPaymentRequest.getHead().setRefId(refId);
			billPaymentRequest.getTxn().setMsgId(msgId);
			String txnRefIdFromJvmParams = System.getProperty("TXN_REF_ID");
			billPaymentRequest.getTxn().setTxnReferenceId(txnRefIdFromJvmParams == null ? txnRefId : txnRefIdFromJvmParams);
			billPaymentRequest.getTxn().setTs(CommonUtils.getFormattedCurrentTimestamp());
			if(billPaymentRequest.getPaymentMethod().getQuickPay() == org.bbps.schema.QckPayType.YES)  {
				transactionDataService.insertQuickPay(billPaymentRequest.getHead().getRefId(), billPaymentRequest);
			}
			billPaymentGateway.processOUBillPaymentRequest(billPaymentRequest);
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e){
			log.error(METHOD_NAME,e);
			//throw e;
			return new ResponseEntity<String>(tenantId,
					HttpStatus.EXPECTATION_FAILED);
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/{tenantId}/complaint/raisecomplaintreq", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> executeRaiseComplaint(@PathVariable String tenantId, final @RequestBody String fileNamePrefix) throws ValidationException {
		String METHOD_NAME = "executeRaiseComplaint";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			String filePath = System.getProperty("file.path");
			ComplainRequest complainRequest = JAXB.unmarshal(new File(filePath+"/ComplainRequest"+fileNamePrefix+".xml"), ComplainRequest.class);
			TransactionContext.putTenantId(tenantId);
			complaintService.sendComplaintRequest(complainRequest);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (Exception e){
			log.error(METHOD_NAME,e);
			//throw e;
			return new ResponseEntity<String>(tenantId,
					HttpStatus.EXPECTATION_FAILED);
		} finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}	
	}
	
	@ResponseBody
	@RequestMapping(value = "/{tenantId}/complaint/searchcomplaintstatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> executeSearchComplaintStatus(@PathVariable String tenantId, final @RequestBody String fileNamePrefix) throws ValidationException {
		String METHOD_NAME = "executeSearchComplaintStatus";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
        try {
			String filePath = System.getProperty("file.path");
			ComplainStatusRequest complainStatusRequest = JAXB.unmarshal(new File(filePath+"/ComplainStatusRequest"+fileNamePrefix+".xml"), ComplainStatusRequest.class);
			TransactionContext.putTenantId(tenantId);
			complaintService.sendComplaintStatusRequest(complainStatusRequest);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (Exception e){
			log.error(METHOD_NAME,e);
			//throw e;
			return new ResponseEntity<String>(tenantId,
					HttpStatus.EXPECTATION_FAILED);
		} finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}		
	}
	
	@ResponseBody
	@RequestMapping(value = "/{tenantId}/complaint/searchtransaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> executeSearchTransaction(@PathVariable String tenantId, final @RequestBody String fileNamePrefix) throws ValidationException {
		String METHOD_NAME = "executeSearchTransaction";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}		
		try {
			String filePath = System.getProperty("file.path");
			TransactionSearchRequest transactionSearchRequest = JAXB.unmarshal(new File(filePath+"/TransactionSearchRequest"+fileNamePrefix+".xml"), TransactionSearchRequest.class);
			TransactionContext.putTenantId(tenantId);
			complaintService.sendTransactionSearchRequestType(transactionSearchRequest);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (Exception e){
			log.error(METHOD_NAME,e);
			//throw e;
			return new ResponseEntity<String>(tenantId,
					HttpStatus.EXPECTATION_FAILED);
		} finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/billerFetchRequestByParameter", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> billerFetchByParameter(@PathVariable String tenantId, final @RequestBody BillerFetchByParameter billerFetchByParameter)
			throws SchedulerException {
		String METHOD_NAME = "BillerFetchByParameter";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		//TenantDetail td = tenantDetailService.fetchByTenantId(tenantId);
		String ouName=tenantDetailService.getOUName(TransactionContext.getTenantId());
		BillerFetchRequestResponse req=billerFetchRequestResponseService.saveFetchRequest(billerFetchByParameter,ouName);
		try{
			//scheduleBillerFetchByParameter(td.getTenantId(),billerFetchByParameter.getHrColonMins(),req.getRefId());
			scheduleBillerFetchByParameter(tenantId,billerFetchByParameter.getHrColonMins(),req.getRefId());
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch(Exception e){
			log.error(METHOD_NAME,e);
			//throw e;
			return new ResponseEntity<String>(tenantId,
					HttpStatus.EXPECTATION_FAILED);
		} finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
		}
	
	private @ResponseBody ResponseEntity<String> scheduleBillerFetchByParameter(String tenantId,String hrColonMins,String refId)
			throws SchedulerException {
		String METHOD_NAME = "scheduleBillerFetch";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(hrColonMins) || !CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			String[] hrMins = hrColonMins.split(",");
			
			if (hrMins == null || hrMins.length == 0){
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			List<DateTime> listDateTime = new ArrayList<DateTime>();
			for (String hrColMin:hrMins){
				String[] hrMin = hrColMin.trim().split(":");

				int hr = 0;
				int min = 0;
				
				try {
					hr = Integer.parseInt(hrMin[0]);
					min = Integer.parseInt(hrMin[1]);
				} catch (Exception e) {
					return new ResponseEntity<String>(hrColonMins,
							HttpStatus.EXPECTATION_FAILED);
				}
				
				if (hr < 0 || hr > 23 || min < 0 || min > 59){
					return new ResponseEntity<String>(hrColonMins,
							HttpStatus.EXPECTATION_FAILED);
				}
				
				DateTime dateTime = DateTime.parse(hrColMin, DateTimeFormat.forPattern("HH:mm"));
				listDateTime.add(dateTime);
			}

			if (listDateTime.isEmpty()){
				return new ResponseEntity<String>(hrColonMins,
						HttpStatus.EXPECTATION_FAILED);
			}
			
			int jobCount = 1;
			while (true){
				if (sharedSchedulerService.isJobExist(TypeOfBatch.BILLER_FETCH_BY_PARAMETER.name()+'_'+jobCount)){
					sharedSchedulerService.unSchedule(TypeOfBatch.BILLER_FETCH_BY_PARAMETER.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			Collections.sort(listDateTime);
			
			jobCount = 1;
			for (DateTime dateTime:listDateTime)
			{
				String settlemntTime = DateTimeFormat.forPattern("HH:mm").print(dateTime);
				String[] hrMin = settlemntTime.split(":");
				String schedulerCron = "0 "+hrMin[1]+" "+hrMin[0]+" * * ?";

				sharedSchedulerService.schedule(TypeOfBatch.BILLER_FETCH_BY_PARAMETER.name()+'_'+jobCount, TypeOfBatch.BILLER_FETCH_BY_PARAMETER, schedulerCron,refId);
				jobCount++;
			}
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (SchedulerException e){
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	

@RequestMapping(value = "/scheduleTxnReport", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> scheduleTransactionReport(@PathVariable String tenantId,final @RequestBody TxnReportReq txnReportReq) throws Exception {
		
		String METHOD_NAME = "scheduleTxnReport";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(txnReportReq.getHrColonMins()) || !CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(txnReportReq.getHrColonMins(), HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			String[] hrMins = txnReportReq.getHrColonMins().split(",");
			
			if (hrMins == null || hrMins.length == 0){
				return new ResponseEntity<String>(txnReportReq.getHrColonMins(), HttpStatus.EXPECTATION_FAILED);
			}
			
			List<DateTime> listDateTime = new ArrayList<DateTime>();
			for (String hrColMin:hrMins){
				String[] hrMin = hrColMin.trim().split(":");

				int hr = 0;
				int min = 0;
				try {
					hr = Integer.parseInt(hrMin[0]);
					min = Integer.parseInt(hrMin[1]);
				} 
				catch (Exception e) {
					return new ResponseEntity<String>(txnReportReq.getHrColonMins(), HttpStatus.EXPECTATION_FAILED);
				}
				
				if (hr < 0 || hr > 23 || min < 0 || min > 59){
					return new ResponseEntity<String>(txnReportReq.getHrColonMins(), HttpStatus.EXPECTATION_FAILED);
				}
				
				DateTime dateTime = DateTime.parse(hrColMin, DateTimeFormat.forPattern("HH:mm"));
				listDateTime.add(dateTime);
			}

			if (listDateTime.isEmpty()){
				return new ResponseEntity<String>(txnReportReq.getHrColonMins(), HttpStatus.EXPECTATION_FAILED);
			}
			
			int jobCount = 1;
			while (true){
				if (sharedSchedulerService.isJobExist(TypeOfBatch.TXN_REPORT.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.TXN_REPORT.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			Collections.sort(listDateTime);
			
			jobCount = 1;
			for (DateTime dateTime:listDateTime)
			{
				String settlemntTime = DateTimeFormat.forPattern("HH:mm").print(dateTime);
				String[] hrMin = settlemntTime.split(":");
				String schedulerCron = "0 "+hrMin[1]+" "+hrMin[0]+" ? * "+txnReportReq.getDay();
				sharedSchedulerService.schedule(TypeOfBatch.TXN_REPORT.name()+'_'+jobCount, TypeOfBatch.TXN_REPORT, schedulerCron);
				jobCount++;
			}
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/unscheduleTxnReport", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> unscheduleTxnReport(@PathVariable String tenantId) throws Exception {
		
		String METHOD_NAME = "unscheduleTxnReport";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId, HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			int jobCount = 1;
			while (true) {
				if (sharedSchedulerService.isJobExist(TypeOfBatch.TXN_REPORT.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.TXN_REPORT.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/scheduleCUAgentApprovalReqPush", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> scheduleCUAgentApprovalReqPush(@PathVariable String tenantId, final @RequestBody String hrColonMins) throws Exception {
		
		String METHOD_NAME = "scheduleCUAgentApprovalReqPush";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(hrColonMins) || !CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			String[] hrMins = hrColonMins.split(",");
			
			if (hrMins == null || hrMins.length == 0){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			List<DateTime> listDateTime = new ArrayList<DateTime>();
			for (String hrColMin:hrMins){
				String[] hrMin = hrColMin.trim().split(":");

				int hr = 0;
				int min = 0;
				
				try {
					hr = Integer.parseInt(hrMin[0]);
					min = Integer.parseInt(hrMin[1]);
				} 
				catch (Exception e) {
					return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
				}
				
				if (hr < 0 || hr > 23 || min < 0 || min > 59){
					return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
				}
				
				DateTime dateTime = DateTime.parse(hrColMin, DateTimeFormat.forPattern("HH:mm"));
				listDateTime.add(dateTime);
			}

			if (listDateTime.isEmpty()){
				return new ResponseEntity<String>(hrColonMins, HttpStatus.EXPECTATION_FAILED);
			}
			
			int jobCount = 1;
			while (true){
				if (sharedSchedulerService.isJobExist(TypeOfBatch.CU_AGENT_APPROVAL_REQ.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.CU_AGENT_APPROVAL_REQ.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			Collections.sort(listDateTime);
			
			jobCount = 1;
			for (DateTime dateTime:listDateTime)
			{
				String settlemntTime = DateTimeFormat.forPattern("HH:mm").print(dateTime);
				String[] hrMin = settlemntTime.split(":");
				String schedulerCron = "0 "+hrMin[1]+" "+hrMin[0]+" * * ?";
				//String schedulerCron = "0/10 * * * * ?";
				//String schedulerCron = "0 0 18 * * ?";
				sharedSchedulerService.schedule(TypeOfBatch.CU_AGENT_APPROVAL_REQ.name()+'_'+jobCount, TypeOfBatch.CU_AGENT_APPROVAL_REQ, schedulerCron);
				jobCount++;
			}
			
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/unscheduleCUAgentApprovalReqPush", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> unscheduleCUAgentApprovalReqPush(@PathVariable String tenantId) throws Exception {
		
		String METHOD_NAME = "unscheduleCUAgentApprovalReqPush";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		try {
			if (!CommonUtils.hasValue(tenantId)){
				return new ResponseEntity<String>(tenantId, HttpStatus.EXPECTATION_FAILED);
			}
			
			TransactionContext.putTenantId(tenantId);
			
			int jobCount = 1;
			while (true) {
				if (sharedSchedulerService.isJobExist(TypeOfBatch.CU_AGENT_APPROVAL_REQ.name()+'_'+jobCount)) {
					sharedSchedulerService.unSchedule(TypeOfBatch.CU_AGENT_APPROVAL_REQ.name()+'_'+jobCount);
					jobCount++;
				}
				else {
					break;
				}
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e) {
			log.error(METHOD_NAME,e);
			throw e;
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	@RequestMapping(value = "/scheduleAllCUApprovedAgentFetch", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> scheduleCUAllApprovedAgentFetch(@PathVariable String tenantId, final @RequestBody String hrColonMins) throws Exception {
		
		String METHOD_NAME = "scheduleAllCUApprovedAgentFetch";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		return scheduleBatch(tenantId, hrColonMins, TypeOfBatch.ALL_CU_APPROVED_AGENTS_FETCH);
	}
	
	@RequestMapping(value = "/unscheduleAllCUApprovedAgentFetch", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> unscheduleAllCUApprovedAgentFetch(@PathVariable String tenantId) throws Exception {
		
		String METHOD_NAME = "unscheduleAllCUApprovedAgentFetch";
		if (log.isDebugEnabled()){
			log.debug("Entering "+METHOD_NAME);
		}
		return unscheduleBatch(tenantId, TypeOfBatch.ALL_CU_APPROVED_AGENTS_FETCH);
	}
	
	
}
