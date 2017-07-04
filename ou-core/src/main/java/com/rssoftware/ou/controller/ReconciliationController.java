package com.rssoftware.ou.controller;

/**
 * controller class for reconciliation
 */
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.ReconDetails;
import in.co.rssoftware.bbps.schema.ReconSummary;
import in.co.rssoftware.bbps.schema.ReconSummaryList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.Differences;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.RawDataView.ReconStatus;
import com.rssoftware.ou.model.tenant.ReconDetailsView;
import com.rssoftware.ou.model.tenant.ReconFileView;
import com.rssoftware.ou.model.tenant.ReconFileViews;
import com.rssoftware.ou.model.tenant.ReconSummaryView;
import com.rssoftware.ou.model.tenant.ReconView;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.RawDataService;
import com.rssoftware.ou.tenant.service.ReconDetailsService;
import com.rssoftware.ou.tenant.service.ReconSummaryService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

@Controller
@RequestMapping(value = "/APIService/reconcile/urn:tenantId:{tenantId}")
public class ReconciliationController {
	@Autowired
	private RawDataService rawDataService;
	@Autowired
	private ReconSummaryService reconSummaryService;
	@Autowired
	private ReconDetailsService reconDetailsService;
	@Autowired
	private TransactionDataService transactionDataService;
	@Autowired
	private ParamService paramService;
	@Autowired
	private AgentService agentService;

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * api to be hit for reconciliation process
	 * 
	 * @param tenantId
	 * @param settlementCycleId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update/{settlementCycleId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public String updateRawData(@PathVariable String tenantId, @PathVariable String settlementCycleId) {
		String METHOD_NAME = "updateRawData";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		TransactionContext.putTenantId(tenantId);
		RawDataView rawdata = null;
		TransactionDataView transactionData = null;
		BigDecimal reconMaxTry = new BigDecimal(paramService.retrieveStringParamByName("RECON_MAX_TRY"));

		try {
			List<Object> resultList = rawDataService.getData();

			int matched = 0;
			int broughtForwardTransactions = 0;
			int mismatchNotInOu = 0;
			int mismatchNotInCu = 0;
			int mismatchFields = 0;
			int pending = 0;

			ReconDetailsView reconDetailsView = new ReconDetailsView();
			for (int i = 0; i < resultList.size(); i += 2) {
				rawdata = (RawDataView) resultList.get(i);
				transactionData = (TransactionDataView) resultList.get(i + 1);

				/**
				 * updating brought forward transactions for a reconciliation
				 * cycle
				 */
				if (transactionData != null && transactionData
						.getReconStatus() == com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.PENDING) {
					broughtForwardTransactions++;
					reconDetailsView.setRefId(transactionData.getRefId());
					reconDetailsView.setRequestType(transactionData.getRequestType());
					/***************** MODIFY **********/
					reconDetailsView.setAgentId(transactionData.getAgentID());
					reconDetailsView.setBillerId(transactionData.getBillerID());
					/***************************/
					/*
					 * if (transactionData.getRequestType() ==
					 * RequestType.FETCH) {
					 * if(transactionData.getBillFetchRequest()!=null) {
					 * reconDetailsView.setAgentId(transactionData.
					 * getBillFetchRequest().getAgent().getId());
					 * reconDetailsView.setBillerId(transactionData.
					 * getBillFetchRequest().getBillDetails().getBiller().getId(
					 * )); } } else if (transactionData.getRequestType() ==
					 * RequestType.PAYMENT) {
					 * if(transactionData.getBillPaymentRequest()!=null) {
					 * reconDetailsView.setAgentId(transactionData.
					 * getBillPaymentRequest().getAgent().getId());
					 * reconDetailsView.setBillerId(transactionData.
					 * getBillPaymentRequest().getBillDetails().getBiller().
					 * getId()); } }
					 */
					reconDetailsView.setReconId(settlementCycleId);
					reconDetailsView.setReconStatus(
							com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.BROUGHT_FORWARD);
					reconDetailsService.insert(reconDetailsView);
				}

				/**
				 * updating recon_status for transaction_data not present in
				 * raw_data
				 */
				if (rawdata != null && rawdata.getRefId() == null) {

					if (transactionData != null && transactionData.getReconCycleNo().compareTo(reconMaxTry) >= 0) {
						mismatchNotInCu++;
						reconDetailsView = new ReconDetailsView();
						reconDetailsView.setRefId(transactionData.getRefId());
						reconDetailsView.setRequestType(transactionData.getRequestType());
						/***************** MODIFY **********/
						reconDetailsView.setAgentId(transactionData.getAgentID());
						reconDetailsView.setBillerId(transactionData.getBillerID());
						/***************************/
						/*
						 * if (transactionData.getRequestType() ==
						 * RequestType.FETCH) {
						 * if(transactionData.getBillFetchRequest()!=null) {
						 * reconDetailsView.setAgentId(transactionData.
						 * getBillFetchRequest().getAgent().getId());
						 * reconDetailsView.setBillerId(transactionData.
						 * getBillFetchRequest().getBillDetails().getBiller().
						 * getId()); } } else if
						 * (transactionData.getRequestType() ==
						 * RequestType.PAYMENT) {
						 * if(transactionData.getBillPaymentRequest()!=null) {
						 * reconDetailsView.setAgentId(transactionData.
						 * getBillPaymentRequest().getAgent().getId());
						 * reconDetailsView.setBillerId(transactionData.
						 * getBillPaymentRequest().getBillDetails().getBiller().
						 * getId()); } }
						 */
						reconDetailsView.setReconId(settlementCycleId);
						reconDetailsView
								.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.NOT_IN_CU);
						reconDetailsService.insert(reconDetailsView);
						transactionDataService.updateReconStatus(transactionData.getRefId(),
								transactionData.getRequestType(),
								com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.NO_MATCHES_FOUND, null,
								new BigDecimal(transactionData.getReconCycleNo().intValue() + 1));
					} else {
						pending++;
						reconDetailsView = new ReconDetailsView();
						reconDetailsView.setRefId(transactionData.getRefId());
						reconDetailsView.setRequestType(transactionData.getRequestType());
						/***************** MODIFY **********/
						if (transactionData != null) {
							reconDetailsView.setAgentId(transactionData.getAgentID());
							reconDetailsView.setBillerId(transactionData.getBillerID());
						}
						/***************************/
						if (transactionData != null && transactionData.getRequestType() == RequestType.FETCH) {
							if (transactionData.getBillFetchRequest() != null) {
								reconDetailsView.setAgentId(transactionData.getBillFetchRequest().getAgent().getId());
								reconDetailsView.setBillerId(
										transactionData.getBillFetchRequest().getBillDetails().getBiller().getId());
							}
						} else if (transactionData != null && transactionData.getRequestType() == RequestType.PAYMENT) {
							if (transactionData.getBillPaymentRequest() != null) {
								reconDetailsView.setAgentId(transactionData.getBillPaymentRequest().getAgent().getId());
								reconDetailsView.setBillerId(
										transactionData.getBillPaymentRequest().getBillDetails().getBiller().getId());
							}
						}
						reconDetailsView.setReconId(settlementCycleId);
						reconDetailsView
								.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.PENDING);
						reconDetailsService.insert(reconDetailsView);
						transactionDataService.updateReconStatus(transactionData.getRefId(),
								transactionData.getRequestType(),
								com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.PENDING, null,
								new BigDecimal(transactionData.getReconCycleNo().intValue() + 1));
					}
				}

				/**
				 * updating recon_status for raw_data not present in
				 * transaction_data
				 */
				else if (transactionData != null && transactionData.getRefId() == null) {
					mismatchNotInOu++;
					reconDetailsView = new ReconDetailsView();
					reconDetailsView.setRefId(rawdata.getRefId());
					reconDetailsView.setRequestType(rawdata.getTxnType());
					reconDetailsView.setAgentId(rawdata.getAgentId());
					reconDetailsView.setBillerId(rawdata.getBillerId());
					reconDetailsView.setReconId(settlementCycleId);
					reconDetailsView
							.setReconStatus(com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.NOT_IN_OU);
					reconDetailsService.insert(reconDetailsView);
					rawDataService.updateReconStatus(rawdata.getRefId(), rawdata.getTxnType(),
							ReconStatus.NO_MATCHES_FOUND, null);
				}

				/**
				 * updating recon_status for raw_data present in
				 * transaction_data
				 */
				else {

					Differences diff = rawDataService.compare(rawdata, transactionData);
					// update recon_status where non-matching fields exist
					if (diff != null) {
						mismatchFields++;
						rawDataService.updateReconStatus(rawdata.getRefId(), rawdata.getTxnType(),
								ReconStatus.NON_MATCHING_FIELDS, diff.toString());
						transactionDataService.updateReconStatus(rawdata.getRefId(), rawdata.getTxnType(),
								com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.NON_MATCHING_FIELDS,
								diff.toString(), new BigDecimal(transactionData.getReconCycleNo().intValue() + 1));
						reconDetailsView = new ReconDetailsView();
						reconDetailsView.setRefId(rawdata.getRefId());
						reconDetailsView.setRequestType(rawdata.getTxnType());
						reconDetailsView.setAgentId(rawdata.getAgentId());
						reconDetailsView.setBillerId(rawdata.getBillerId());
						reconDetailsView.setReconId(settlementCycleId);
						reconDetailsView.setReconStatus(
								com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus.NON_MATCHING_FIELDS);
						reconDetailsView.setReconDescription(diff.toString());
						reconDetailsService.insert(reconDetailsView);
					}

					// update recon_status where completely matches
					else {
						matched++;
						rawDataService.updateReconStatus(rawdata.getRefId(), rawdata.getTxnType(), ReconStatus.MATCHED,
								null);
						transactionDataService.updateReconStatus(rawdata.getRefId(), rawdata.getTxnType(),
								com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.MATCHED, null,
								new BigDecimal(transactionData.getReconCycleNo().intValue() + 1));
					}
				}
			}

			ReconSummaryView reconSummaryView = new ReconSummaryView();
			reconSummaryView.setReconId(settlementCycleId);
			reconSummaryView.setMatchedCount(new BigDecimal(matched));
			reconSummaryView.setNotInCUCount(new BigDecimal(mismatchNotInCu));
			reconSummaryView.setNotInOUCount(new BigDecimal(mismatchNotInOu));
			reconSummaryView.setMismatchedCount(new BigDecimal(mismatchFields));
			reconSummaryView.setPendingCount(new BigDecimal(pending));
			reconSummaryView.setBroughtForwardCount(new BigDecimal(broughtForwardTransactions));
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			reconSummaryView.setReconTs(timestamp);
			reconSummaryService.insert(reconSummaryView);
			return "success";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			// log.error("Error " + e);
			return "failure: " + e.getMessage();
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving " + METHOD_NAME);
			}
		}
	}

	/**
	 * api to be hit for fetching recon report by ou in general based on start
	 * and end date
	 * 
	 * @param request
	 * @param tenantId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping(value = "/fetch", method = RequestMethod.GET)
	public String reportRecon(HttpServletRequest request, @PathVariable String tenantId,
			@RequestParam(value = "startDate", defaultValue = "null") String startDate,
			@RequestParam(value = "endDate", defaultValue = "null") String endDate) {
		String METHOD_NAME = "reportRecon";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		request.getSession().setAttribute("tenantId", tenantId);
		List<ReconView> reconViews = new LinkedList<ReconView>();

		if (startDate.equals("null") || endDate.equals("null")) {
			request.setAttribute("reconViews", null);
		}

		// populating the List<ReconView> based on recon_summary and
		// recon_details between start and end date
		else {
			try {
				ReconSummaryView reconSummaryView = null;
				ReconView reconView = null;

				List<ReconSummaryView> reconSummaryViews = reconSummaryService.getReconList(startDate, endDate);
				Iterator<ReconSummaryView> it = reconSummaryViews.iterator();
				while (it.hasNext()) {
					reconSummaryView = it.next();
					reconView = reconDetailsService.getReconDetailsList(reconSummaryView);
					reconViews.add(reconView);
				}
			} catch (Exception e) {
				log.error("Error " + e);
			}
		}

		request.setAttribute("reconViews", reconViews);
		return "showReport";
	}

	/**
	 * api hit from recon_summary to view a particular recon id's details
	 * 
	 * @param request
	 * @param reconView
	 * @return
	 */
	@RequestMapping(value = "/showReconDetails/{reconView}", method = RequestMethod.GET)
	public String reportReconDetails(HttpServletRequest request, @PathVariable String reconView) {
		String METHOD_NAME = "reportReconDetails";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		ObjectMapper m = new ObjectMapper();
		ReconView r = null;
		try {
			r = m.readValue(reconView, ReconView.class);
		} catch (IOException e) {
			log.error("Error " + e);
		}
		List<ReconView> reconViews = new ArrayList<ReconView>();
		reconViews.add(r);
		request.setAttribute("reconViews", reconViews);
		return "showReportDetails";
	}

	/**
	 * api to hit for agent specific reconciliation report
	 * 
	 * @param request
	 * @param tenantId
	 * @param agentId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/agentReport/{agentId}", method = RequestMethod.GET)
	public ResponseEntity<?> reportReconForAgent(@PathVariable String tenantId, @PathVariable String agentId,
			@RequestParam String startDate, @RequestParam String endDate) {
		String METHOD_NAME = "reportReconForAgent";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		ReconSummaryList reconViews = new ReconSummaryList();
		try {
			ReconSummaryView reconSummaryView = null;
			ReconSummary reconView = null;

			List<ReconSummaryView> reconSummaryViews = reconSummaryService.getReconList(startDate, endDate);
			if (reconSummaryViews != null) {
				Iterator<ReconSummaryView> it = reconSummaryViews.iterator();
				while (it.hasNext()) {
					reconSummaryView = it.next();
					reconView = mapToJaxb(reconDetailsService.getReconDetailsList(reconSummaryView, agentId, 0));
					if (reconView != null)
						reconViews.getReconSummaryLists().add(reconView);
				}
			}
			response = new ResponseEntity(reconViews, HttpStatus.OK);

		} catch (ValidationException ve) {
			log.error("Error: ", ve);
			ReconSummaryList summaryList = new ReconSummaryList();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			errorMessage.setErrorDtl(ve.getDescription());
			summaryList.getErrors().add(errorMessage);
			response = new ResponseEntity(summaryList, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			log.error("Error " + e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			ReconSummaryList reconSummary = new ReconSummaryList();
			reconSummary.getErrors().add(errorMessage);
			response = new ResponseEntity(reconSummary, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	/**
	 * api to hit for agent specific reconciliation report
	 * 
	 * @param request
	 * @param tenantId
	 * @param agentId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/agentInstReport/{agentInstId}", method = RequestMethod.POST)
	public ResponseEntity<?> reportReconForAgentInst(HttpServletRequest request, @PathVariable String tenantId,
			@PathVariable String agentInstId, @RequestParam String startDate, @RequestParam String endDate) {
		String METHOD_NAME = "reportReconForAgentInst";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		ReconSummaryList reconViews = new ReconSummaryList();
		try {
			ReconSummaryView reconSummaryView = null;
			ReconSummary reconView = null;

			List<ReconSummaryView> reconSummaryViews = reconSummaryService.getReconList(startDate, endDate);
			if (reconSummaryViews != null) {
				Iterator<ReconSummaryView> it = reconSummaryViews.iterator();
				while (it.hasNext()) {
					reconSummaryView = it.next();
					List<AgentView> agentViews = agentService.getAllAgentsByInstituteId(agentInstId);
					Iterator<AgentView> agentViewIt = agentViews.iterator();
					while (agentViewIt.hasNext()) {
						reconView = mapToJaxb(reconDetailsService.getReconDetailsList(reconSummaryView,
								agentViewIt.next().getAgentId(), 0));
						if (reconView != null)
							reconViews.getReconSummaryLists().add(reconView);
					}
				}
			}
			response = new ResponseEntity(reconViews, HttpStatus.OK);

		} catch (ValidationException ve) {
			log.error("Error: ", ve);
			ReconSummaryList summaryList = new ReconSummaryList();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			errorMessage.setErrorDtl(ve.getDescription());
			summaryList.getErrors().add(errorMessage);
			response = new ResponseEntity(summaryList, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			// log.error("Error " + e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			ReconSummaryList reconSummary = new ReconSummaryList();
			reconSummary.getErrors().add(errorMessage);
			response = new ResponseEntity(reconSummary, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	/**
	 * api to hit for biller specific reconciliation report
	 * 
	 * @param request
	 * @param tenantId
	 * @param billerId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/billerReport/{billerId}", method = RequestMethod.GET)
	public ResponseEntity<?> reportReconForBiller(HttpServletRequest request, @PathVariable String tenantId,
			@PathVariable String billerId, @RequestParam String startDate, @RequestParam String endDate) {
		String METHOD_NAME = "reportReconForBiller";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		ReconSummaryList reconViews = new ReconSummaryList();
		try {
			ReconSummaryView reconSummaryView = null;
			ReconSummary reconView = null;

			List<ReconSummaryView> reconSummaryViews = reconSummaryService.getReconList(startDate, endDate);
			if (reconSummaryViews != null) {
				Iterator<ReconSummaryView> it = reconSummaryViews.iterator();
				while (it.hasNext()) {
					reconSummaryView = it.next();
					reconView = mapToJaxb(reconDetailsService.getReconDetailsList(reconSummaryView, billerId, 1));
					if (reconView != null)
						reconViews.getReconSummaryLists().add(reconView);
				}
			}
			response = new ResponseEntity(reconViews, HttpStatus.OK);

		} catch (ValidationException ve) {
			log.error("Error: ", ve);
			ReconSummaryList summaryList = new ReconSummaryList();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			errorMessage.setErrorDtl(ve.getDescription());
			summaryList.getErrors().add(errorMessage);
			response = new ResponseEntity(summaryList, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			// log.error("Error " + e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			ReconSummaryList reconSummary = new ReconSummaryList();
			reconSummary.getErrors().add(errorMessage);
			response = new ResponseEntity(reconSummary, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	private ReconSummary mapToJaxb(ReconView reconView) throws ValidationException {

		if(reconView!=null) {
/*			throw ValidationException.getInstance(ValidationErrorReason.REQUEST_NOT_FOUND, "Entity not found!");
*/
		ReconSummary reconSummary = new ReconSummary();
		reconSummary.setBroughtForwardCount(reconView.getBroughtForwardCount());
		List<com.rssoftware.ou.database.entity.tenant.ReconDetails> details = reconView.getBroughtForwardList();
		List<ReconDetails> listJaxb= new ArrayList<ReconDetails>();
		ReconDetails reconDetailsJaxb = null;
		for(com.rssoftware.ou.database.entity.tenant.ReconDetails reconDetail:details) {
			reconDetailsJaxb = formReconDetailsJaxb(reconDetail);
			listJaxb.add(reconDetailsJaxb);
		}
		reconSummary.getBroughtForwardLists().addAll(listJaxb);
		reconSummary.setMismatchedCount(reconView.getMismatchedCount());
		details = reconView.getMismatchedList();
		listJaxb= new ArrayList<ReconDetails>();
		for(com.rssoftware.ou.database.entity.tenant.ReconDetails reconDetail:details) {
			reconDetailsJaxb = formReconDetailsJaxb(reconDetail);
			listJaxb.add(reconDetailsJaxb);
		}
		reconSummary.getMismatchedLists().addAll(listJaxb);
		reconSummary.setMatchedCount(reconView.getMatchedCount());
		reconSummary.setNotInCUCount(reconView.getNotInCUCount());
		details = reconView.getNotInCuList();
		listJaxb= new ArrayList<ReconDetails>();
		for(com.rssoftware.ou.database.entity.tenant.ReconDetails reconDetail:details) {
			reconDetailsJaxb = formReconDetailsJaxb(reconDetail);
			listJaxb.add(reconDetailsJaxb);
		}
		reconSummary.getNotInCuLists().addAll(listJaxb);
		reconSummary.setNotInOUCount(reconView.getNotInOUCount());
		details = reconView.getNotInOuList();
		listJaxb= new ArrayList<ReconDetails>();
		for(com.rssoftware.ou.database.entity.tenant.ReconDetails reconDetail:details) {
			reconDetailsJaxb = formReconDetailsJaxb(reconDetail);
			listJaxb.add(reconDetailsJaxb);
		}
		reconSummary.getNotInOuLists().addAll(listJaxb);
		reconSummary.setPendingCount(reconView.getPendingCount());
		details = reconView.getPendingList();
		listJaxb= new ArrayList<ReconDetails>();
		for(com.rssoftware.ou.database.entity.tenant.ReconDetails reconDetail:details) {
			reconDetailsJaxb = formReconDetailsJaxb(reconDetail);
			listJaxb.add(reconDetailsJaxb);
		}
		reconSummary.getPendingLists().addAll(listJaxb);
		reconSummary.setReconId(reconView.getReconId());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(reconView.getReconTs().getTime());
		reconSummary.setReconTs(calendar);
		return reconSummary;
		}
		return null;

	}
	

	private ReconDetails formReconDetailsJaxb(com.rssoftware.ou.database.entity.tenant.ReconDetails reconDetail) {
		ReconDetails reconDetailsJaxb = new ReconDetails();
		reconDetailsJaxb.setReconDescription(reconDetail.getReconDescription());
		reconDetailsJaxb.setReconId(reconDetail.getId().getReconId());
		reconDetailsJaxb.setRefId(reconDetail.getId().getRefId());
		reconDetailsJaxb.setTxnType(reconDetail.getId().getTxnType());
		reconDetailsJaxb.setReconStatus(reconDetail.getReconStatus());
		reconDetailsJaxb.setTxnRefId(reconDetail.getTxnRefId());
		reconDetailsJaxb.setAgentId(reconDetail.getAgentId());
		reconDetailsJaxb.setBillerId(reconDetail.getBillerId());
		return reconDetailsJaxb;
	}
	
	@RequestMapping(value = "/fetchPGReconFiles", method = RequestMethod.POST)
	public @ResponseBody ReconFileViews fetchPGReconFiles() {
		String METHOD_NAME = "fetchPGReconFiles";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		String path = System.getProperty("reconFileLocation").substring(5);
		final File folder = new File(path);
		log.debug("Scanning folder: " + folder);
		return listFilesFromFolder(folder);
	}
	@RequestMapping(value = "/fetchBlrReconFiles", method = RequestMethod.POST)
	public @ResponseBody ReconFileViews fetchBlrReconFiles() {
		String METHOD_NAME = "fetchBlrReconFiles";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		String path = System.getProperty("reconFileLocation").substring(5)+ "/biller";
		final File folder = new File(path);
		log.debug("Scanning folder: " + folder);
		return listFilesFromFolder(folder);
	}
	
	@RequestMapping(value = "/fetchCuSettlementFiles", method = RequestMethod.POST)
	public @ResponseBody ReconFileViews fetchCuSettlementFiles() {
		String METHOD_NAME = "fetchCuSettlementFiles";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		String path = System.getProperty("reconFileLocation").substring(5)+ "/cu";
		final File folder = new File(path);
		log.debug("Scanning folder: " + folder);
		return listFilesFromFolder(folder);
	}
	
	private ReconFileViews listFilesFromFolder(final File folder) {
		ReconFileViews reconFileViews = new ReconFileViews();
		List<ReconFileView> fileViews = new ArrayList<ReconFileView>(1);
		ReconFileView reconFileView = null;

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				//listFilesFromFolder(fileEntry);
			} 
			else {
				if (!fileEntry.isHidden()) {
					reconFileView = new ReconFileView();
					reconFileView.setFile(fileEntry);
					reconFileView.setFileName(fileEntry.getName());
					reconFileView.setDate(new Timestamp(fileEntry.lastModified()));
					fileViews.add(reconFileView);
				}
			}
		}
		reconFileViews.setReconFileViews(fileViews);
		log.debug("setReconFileViews : " + fileViews.get(0).getFileName());
		return reconFileViews;
	}
	@RequestMapping(value = "/{fileName}/{fileType}/viewFile", method = RequestMethod.POST)
	public @ResponseBody File showFile(@PathVariable String fileName, @PathVariable String fileType, HttpServletResponse response) {
		String path = null;
		if (fileType.equals("cu"))
			path = System.getProperty("reconFileLocation").substring(5) + "/cu/" + fileName;
		else if (fileType.equals("blr"))
			path = System.getProperty("reconFileLocation").substring(5) + "/biller/" + fileName;
		else if(fileType.equals("pg"))
			path = System.getProperty("reconFileLocation").substring(5) + "/" + fileName;
		File file = new File(path);
		return file;
		
	}
	
	/**
	 * api to hit for agent specific reconciliation report
	 * 
	 * @param request
	 * @param tenantId
	 * @param agentId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/reconDetailsReport", method = RequestMethod.POST)
	public ResponseEntity<?> reconDetailsReport(@PathVariable String tenantId, @RequestParam String startDate, @RequestParam String endDate) {
		String METHOD_NAME = "reconDetailsReport";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		ReconSummaryList reconViews = new ReconSummaryList();
		try {
			ReconSummaryView reconSummaryView = null;
			ReconSummary reconView = null;

			List<ReconSummaryView> reconSummaryViews = reconSummaryService.getReconList(startDate, endDate);
			if (reconSummaryViews != null) {
				Iterator<ReconSummaryView> it = reconSummaryViews.iterator();
				while (it.hasNext()) {
					reconSummaryView = it.next();
						reconView = mapToJaxb(reconDetailsService.getReconDetailsList(reconSummaryView));
						if (reconView != null)
							reconViews.getReconSummaryLists().add(reconView);
				}
			}
			response = new ResponseEntity(reconViews, HttpStatus.OK);

		} catch (ValidationException ve) {
			log.error("Error: ", ve);
			ReconSummaryList summaryList = new ReconSummaryList();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			errorMessage.setErrorDtl(ve.getDescription());
			summaryList.getErrors().add(errorMessage);
			response = new ResponseEntity(summaryList, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			// log.error("Error " + e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			ReconSummaryList reconSummary = new ReconSummaryList();
			reconSummary.getErrors().add(errorMessage);
			response = new ResponseEntity(reconSummary, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

}
