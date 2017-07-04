package com.rssoftware.ou.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.TxnDetail;
import org.bbps.schema.TxnSearchDateCriteria;
import org.bbps.schema.TxnStatusComplainReq;
import org.bbps.schema.TxnStatusComplainRequest;
import org.bbps.schema.TxnStatusComplainResponse;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.ExchangeId;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.common.utils.RequestResponseGenerator;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.domain.TxnDetailsResponse;
import com.rssoftware.ou.gateway.ComplaintGateway;
import com.rssoftware.ou.model.tenant.ComplaintView;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.service.ComplaintService;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CUService;
import com.rssoftware.ou.tenant.service.ComplaintRequestService;
import com.rssoftware.ou.tenant.service.ComplaintResponseService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

import in.co.rssoftware.bbps.schema.ComplainRequest;
import in.co.rssoftware.bbps.schema.ComplainStatusRequest;
import in.co.rssoftware.bbps.schema.ComplaintDetailType;
import in.co.rssoftware.bbps.schema.ComplaintRaisedResponse;
import in.co.rssoftware.bbps.schema.ComplaintSearchResponse;
import in.co.rssoftware.bbps.schema.ComplaintType;
import in.co.rssoftware.bbps.schema.ComplaintsSearchRequest;
import in.co.rssoftware.bbps.schema.ParticipationType;
import in.co.rssoftware.bbps.schema.TransactionSearchRequest;
import in.co.rssoftware.bbps.schema.TxnDetailType;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

@Service
public class ComplaintServiceImpl implements ComplaintService {

	private final Log log = LogFactory.getLog(getClass());
	private static ObjectMapper objectMapper = new ObjectMapper();
	private final static Logger logger = LoggerFactory.getLogger(ComplaintServiceImpl.class);

	@Autowired
	private BillerService billerService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private ComplaintGateway complaintGateway;

	@Autowired
	private TransactionDataService transactionDataService;

	@Autowired
	private TenantDetailService tenantDetailService;

	@Autowired
	private ComplaintRequestService complaintRequestService;

	@Autowired
	private ComplaintResponseService complaintResponseService;

	@Autowired
	private IDGeneratorService idGeneratorService;

	@Autowired
	private CUService cuService;

	@Autowired
	private AsyncProcessor asyncProcessor;

	@Override
	public ArrayList<TxnDetail> fetchTxnForComplaints(String mobile, String startDate, String endDate, String txnRefId,
			String refId) {
		/*List<TransactionDataView> listTxnOU = new ArrayList<>();
		if (txnRefId != null && !txnRefId.isEmpty()) {

			TransactionDataView txnDataView = transactionDataService.getFilteredTransactionByID(txnRefId);
			if (txnDataView != null)
				listTxnOU.add(txnDataView);
		} *//*else {
			listTxnOU = transactionDataService.getFilteredTransactionByDateRangeAndMobile(mobile, startDate, endDate);
		}
		List<TxnDetail> listTxnDetailsOU = new ArrayList<TxnDetail>();
		for (TransactionDataView txnv : listTxnOU) {
			TxnDetail txn = null;
			if (txnv.getBillAmount() == null) {
				continue;
			}
			txn = mapTransactionDetails(txnv);
			listTxnDetailsOU.add(txn);
		}*/
		ComplaintResponse resp = getComplaintResponse(refId);
		List<TxnDetail> listTxnDetailsCU = null;
		try {
			TxnDetailsResponse response = null;
			if (resp.getTxnSearchResponse() != null) {
				response = objectMapper.readValue(resp.getTxnSearchResponse(), TxnDetailsResponse.class);
				listTxnDetailsCU = response.getTxnDetails();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}
		Set<TxnDetail> set = new HashSet<TxnDetail>();
		/*if (listTxnDetailsOU != null)
			set.addAll(listTxnDetailsOU);*/
		if (listTxnDetailsCU != null)
			set.addAll(listTxnDetailsCU);
		ArrayList<TxnDetail> txnList = new ArrayList<TxnDetail>(set);
		return txnList;

	}

	private TxnDetail mapTransactionDetails(TransactionDataView txnView) {
		TxnDetail txnDet = new TxnDetail();
		txnDet.setTxnReferenceId(txnView.getTxnRefId());
		System.out.println("ref Id: " + txnDet.getTxnReferenceId());
		txnDet.setAmount(txnView.getBillAmount().toPlainString());
		txnDet.setTxnDate(txnView.getDateOfTxn());
		txnDet.setAgentId(txnView.getAgentID());
		txnDet.setBillerId(txnView.getBillerID());
		txnDet.setTxnStatus(txnView.getStatus().name());
		return txnDet;
	}

	@Override
	public Ack processComplaintRequest(ComplaintView complaint) throws ValidationException {
		TxnStatusComplainRequest request = mapComplaintRequest(complaint);
		return complaintGateway.processComplaintRequest(request);
	}

	private TxnStatusComplainRequest mapComplaintRequest(ComplaintView complaint) {
		TxnStatusComplainRequest request = new TxnStatusComplainRequest();
		String ouId = complaint.getTenantId();
		String ouName = tenantDetailService.getOUName(ouId);
		request.setHead(RequestResponseGenerator.getHead(ouName, complaint.getBillerRef()));
		request.setTxn(RequestResponseGenerator.getTxn(ouName, complaint.getMsgId()));

		TxnStatusComplainReq txnComplain = new TxnStatusComplainReq();
		txnComplain.setComplaintType(complaint.getComplaintTypeCd());
		txnComplain.setDisposition(complaint.getDisposition());
		txnComplain.setTxnReferenceId(complaint.getTransactionId());
		txnComplain.setMobile(complaint.getMobNo());
		txnComplain.setMsgId(complaint.getMsgId());
		request.setTxnStatusComplainReq(txnComplain);
		return request;
	}

	@Override
	public void saveComplaintResponse(TxnStatusComplainResponse response) {
		ComplaintResponse complaintResponse = new ComplaintResponse();
		complaintResponse.setComplaintIdByCU(response.getTxnStatusComplainResp().getComplaintId());
		complaintResponse.setMsgId(response.getTxnStatusComplainResp().getMsgId());
		complaintResponse.setOpenComplaint(response.getTxnStatusComplainResp().getOpenComplaint());
		complaintResponse.setAssignedTo(response.getTxnStatusComplainResp().getAssigned());
		complaintResponse.setRefId(response.getHead().getRefId());
		complaintResponse.setRespCode(response.getTxnStatusComplainResp().getResponseCode());
		complaintResponse.setRespReason(response.getTxnStatusComplainResp().getResponseReason());
		complaintResponse.setComplaintStatus(response.getTxnStatusComplainResp().getComplaintStatus());
		if (response.getTxnStatusComplainResp().getTxnList() != null) {
			try {
				complaintResponse.setTxnSearchResponse(
						objectMapper.writeValueAsString(response.getTxnStatusComplainResp().getTxnList()));
			} catch (JsonGenerationException e) {
				logger.error(e.getMessage(), e);
				logger.info("In Excp : " + e.getMessage());
			} catch (JsonMappingException e) {
				logger.error(e.getMessage(), e);
				logger.info("In Excp : " + e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				logger.info("In Excp : " + e.getMessage());
			}
		}
		if (response.getTxnStatusComplainResp().getCustomerDetails() != null) {
			complaintResponse.setCustomerName(response.getTxnStatusComplainResp().getCustomerDetails().getName());
			complaintResponse.setMobile(response.getTxnStatusComplainResp().getCustomerDetails().getMobile());
		}
		complaintResponse.setCrtnTs(new Timestamp(new Date().getTime()));
		complaintResponse.setUpdtTs(new Timestamp(new Date().getTime()));
		complaintResponseService.save(complaintResponse);
	}

	@Override
	public ComplaintResponse getComplaintResponse(String refId) {
		ComplaintResponse complaintResponse = complaintResponseService.getResponse(refId);
		return complaintResponse;
	}

	@Override
	public ComplaintRequest getComplaintRequest(String refId) {
		ComplaintRequest req = complaintRequestService.getRequest(refId);
		return req;
	}

	@Override
	public void updateResponseStatus(String refId, RequestStatus status) {
		ComplaintResponse resp = getComplaintResponse(refId);
		if (resp != null) {
			resp.setResponseStatus(status.name());
			resp.setUpdtTs(new Timestamp(System.currentTimeMillis()));
			complaintResponseService.save(resp);
		}

	}

	@Override
	public void updateRequestStatus(String refId, RequestStatus status) {
		ComplaintRequest req = getComplaintRequest(refId);
		if (req != null) {
			req.setResponseStatus(status.name());
			req.setLastUpdtTS(new Timestamp(System.currentTimeMillis()));
			complaintRequestService.save(req);
		}

	}

	// Sending request to raise complaint, Response is saved in Response table
	@Override
	public ComplaintResponse sendComplaintRequest(ComplainRequest complainRequest) throws ValidationException {

		TxnStatusComplainRequest request = new TxnStatusComplainRequest();
		// ComplainReqType reqType = complainRequest.getComplainReq();
		TxnStatusComplainReq txnComplain = new TxnStatusComplainReq();

		String ouId = TransactionContext.getTenantId();
		TenantDetail td = tenantDetailService.fetchByTenantId(ouId);
		String ouName = td.getOuName();
		String msgId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, td.getOuName());
		String refId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, td.getOuName());
		txnComplain.setMsgId(msgId);

		if (complainRequest.getComplaintType() != null) {
			if (complainRequest.getComplaintType() == ComplaintType.TRANSACTION) {
				txnComplain.setComplaintType(complainRequest.getComplaintType().value());
				txnComplain.setTxnReferenceId(complainRequest.getTxnReferenceId());
				txnComplain.setDisposition(complainRequest.getDisposition());
				txnComplain.setDescription(complainRequest.getDescription());
			} else if (complainRequest.getComplaintType() == ComplaintType.SERVICE) {
				txnComplain.setComplaintType(complainRequest.getComplaintType().value());
				txnComplain.setServReason(complainRequest.getServReason());
				txnComplain.setDescription(complainRequest.getDescription());
				txnComplain.setParticipationType(complainRequest.getParticipationType().name());
				if (complainRequest.getParticipationType() == ParticipationType.AGENT) {
					txnComplain.setAgentId(complainRequest.getAgentId());
				} else if (complainRequest.getParticipationType() == ParticipationType.BILLER) {
					txnComplain.setBillerId(complainRequest.getBillerId());
				} else if (complainRequest.getParticipationType() == ParticipationType.SYSTEM) {
					txnComplain.setBillerId(complainRequest.getBillerId());
				} else {
					if (log.isDebugEnabled()) {
						log.debug(ValidationException.ValidationErrorReason.INVALID_PARTICIPATION_TYPE.name());
					}
				}
			}
		} else {
			// ErrorMessage error = new ErrorMessage();
			if (log.isDebugEnabled()) {
				log.debug(ValidationException.ValidationErrorReason.INVALID_COMPLAINT_TYPE.name());
			}

		}
		request.setHead(RequestResponseGenerator.getHead(ouName, refId));
		request.setTxn(RequestResponseGenerator.getTxn(ouName, msgId));
		request.getTxn().setXchangeId(ExchangeId.FIVEZEROONE.getExpandedForm());
		request.getTxn().setMsgId(null);
		request.getTxn().setRiskScores(null);
		request.setTxnStatusComplainReq(txnComplain);
		// saving complaint request in OU DB if it is a raise complaint request
		ComplaintRequest complaintRequest = maptoComplaintReq(txnComplain);
		complaintRequest.setXchangeId(ExchangeId.FIVEZEROONE.getExpandedForm());
		complaintRequest.setRefId(refId);
		// complaintRequest.setComplaintId(complaintIdGenerator(ouName));
		complaintRequest.setMobile(complainRequest.getMobile());
		complaintRequest.setCrtnTS(new Timestamp(new Date().getTime()));
		complaintRequest.setLastUpdtTS(new Timestamp(new Date().getTime()));
		complaintRequest.setNodeAddress(CommonUtils.getServerNameWithPort());
		complaintRequestService.save(complaintRequest);
		request.getTxnStatusComplainReq().setMobile(null);// Mobile no. is a
															// invalid attribute
															// for raise
															// complaint(501)
		LogUtils.logReqRespMessage(request, refId, Action.COMPLAINT_REQUEST);
		// Ack ack=complaintGateway.processComplaintRequest(request);
		Ack ack = cuService.postComplaintRequest(request);
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.COMPLAINT_REQUEST);
		return asyncProcessor.processComplaintRequest(request, ack);
	}

	// Sending request to check the current status of a complaint, Response is
	// saved in Response table
	@Override
	public ComplaintResponse sendComplaintStatusRequest(ComplainStatusRequest complainStatusRequest)
			throws ValidationException {

		TxnStatusComplainRequest request = new TxnStatusComplainRequest();
		TxnStatusComplainReq txnComplain = new TxnStatusComplainReq();
		ComplaintRequest complaintRequest = new ComplaintRequest();
		String ouId = TransactionContext.getTenantId();
		TenantDetail td = tenantDetailService.fetchByTenantId(ouId);
		String ouName = td.getOuName();
		String msgId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, td.getOuName());
		String refId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, td.getOuName());

		txnComplain.setMsgId(msgId);
		txnComplain.setComplaintId(complainStatusRequest.getComplaintId());
		txnComplain.setComplaintType(complainStatusRequest.getComplaintType().value());
		complaintRequest = maptoComplaintReq(txnComplain);
		complaintRequest.setXchangeId(ExchangeId.FIVEZEROSIX.getExpandedForm());
		complaintRequest.setRefId(refId);
		complaintRequest.setMsgId(msgId);
		complaintRequest.setCrtnTS(new Timestamp(new Date().getTime()));
		complaintRequest.setLastUpdtTS(new Timestamp(new Date().getTime()));
		complaintRequest.setNodeAddress(CommonUtils.getServerNameWithPort());
		complaintRequestService.save(complaintRequest);

		request.setHead(RequestResponseGenerator.getHead(ouName, refId));
		request.setTxn(RequestResponseGenerator.getTxn(ouName, msgId));
		request.getTxn().setXchangeId(ExchangeId.FIVEZEROSIX.getExpandedForm());
		request.getTxn().setMsgId(null);
		request.getTxn().setRiskScores(null);
		request.setTxnStatusComplainReq(txnComplain);
		LogUtils.logReqRespMessage(request, refId, Action.COMPLAINT_REQUEST);
		// Ack ack=complaintGateway.processComplaintRequest(request);
		Ack ack = cuService.postComplaintRequest(request);
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.COMPLAINT_REQUEST);
		return asyncProcessor.processComplaintRequest(request, ack);
	}

	// Sending the request to search for all transaction for a mobile no.
	@Override
	public TxnSearchResponse sendTransactionSearchRequestType(TransactionSearchRequest transactionSearchRequest)
			throws ValidationException {

		TxnStatusComplainRequest request = new TxnStatusComplainRequest();
		TxnStatusComplainReq txnComplain = new TxnStatusComplainReq();
		ComplaintRequest complaintRequest = new ComplaintRequest();
		String ouId = TransactionContext.getTenantId();
		TenantDetail td = tenantDetailService.fetchByTenantId(ouId);
		String ouName = td.getOuName();
		String msgId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, td.getOuName());
		String refId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, td.getOuName());

		txnComplain.setMsgId(msgId);
		txnComplain.setMobile(transactionSearchRequest.getMobile());
		txnComplain.setTxnReferenceId(transactionSearchRequest.getTxnReferenceId());
		txnComplain.setComplaintType(transactionSearchRequest.getComplaintType().value());
		complaintRequest = maptoComplaintReq(txnComplain);
		complaintRequest.setXchangeId(ExchangeId.FOURZEROONE.getExpandedForm());
		complaintRequest.setRefId(refId);
		complaintRequest.setMsgId(msgId);
		complaintRequest.setCrtnTS(new Timestamp(new Date().getTime()));
		complaintRequest.setLastUpdtTS(new Timestamp(new Date().getTime()));
		complaintRequest.setNodeAddress(CommonUtils.getServerNameWithPort());
		complaintRequestService.save(complaintRequest);
		request.setHead(RequestResponseGenerator.getHead(ouName, refId));
		request.setTxn(RequestResponseGenerator.getTxn(ouName, msgId));
		request.getTxn().setXchangeId(ExchangeId.FOURZEROONE.getExpandedForm());
		request.getTxn().setMsgId(null);
		request.getTxn().setRiskScores(null);
		request.setTxnStatusComplainReq(txnComplain);

		if (transactionSearchRequest.getTxnReferenceId() == null
				|| transactionSearchRequest.getTxnReferenceId().isEmpty()) {
			TxnSearchDateCriteria criteria = new TxnSearchDateCriteria();
			criteria.setFromDate(transactionSearchRequest.getFromDate());
			criteria.setToDate(transactionSearchRequest.getToDate());
			request.setTxnSearchDateCriteria(criteria);
		}
		LogUtils.logReqRespMessage(request, refId, Action.COMPLAINT_REQUEST);
		// Ack ack= complaintGateway.processComplaintRequest(request);
		Ack ack = cuService.postComplaintRequest(request);
		LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.COMPLAINT_REQUEST);
		asyncProcessor.processComplaintRequest(request, ack);
		TxnSearchResponse res = getTransactionSearchResponse(transactionSearchRequest.getMobile(),
				transactionSearchRequest.getFromDate(), transactionSearchRequest.getToDate(),
				transactionSearchRequest.getTxnReferenceId(), ack.getRefId());
		return res;
	}

	// Getting the response of searching for all transaction for a mobile no.
	@Override
	public TxnSearchResponse getTransactionSearchResponse(String mobileNo, String startDate, String endDate,
			String txnId, String refId) {
		ArrayList<TxnDetail> allTxnList = fetchTxnForComplaints(mobileNo, startDate, endDate, txnId, refId);
		TxnSearchResponse resp = new TxnSearchResponse();
		List<TxnDetailType> txnDetails = resp.getTxnDetails();
		for (TxnDetail txn : allTxnList) {
			TxnDetailType txnType = null;
			txnType = mapTransactionDetailsType(txn);
			txnDetails.add(txnType);
		}
		return resp;
	}

	private TxnDetailType mapTransactionDetailsType(TxnDetail td) {
		TxnDetailType txnDet = new TxnDetailType();
		txnDet.setTxnReferenceId(td.getTxnReferenceId());
		txnDet.setAmount(td.getAmount());
		if (td.getTxnDate() != null) {
			txnDet.setTxnDate(CommonUtils.getFormaterdd_MM_YYYY_HH_MM_A(td.getTxnDate()));
		}
		txnDet.setAgentId(td.getAgentId());
		txnDet.setBillerId(td.getBillerId());
		txnDet.setTxnStatus(td.getTxnStatus());

		return txnDet;
	}

	@Override
	public ComplaintSearchResponse fetchAllComplaints(ComplaintsSearchRequest request) {
		List<ComplaintRequest> compList = complaintRequestService.fetchALLByMobile(request.getMobile());
		ComplaintSearchResponse resp = new ComplaintSearchResponse();
		List<ComplaintDetailType> compListType = resp.getComplaintDetails();
		for (ComplaintRequest cmp : compList) {
			ComplaintDetailType cmpType = null;
			cmpType = mapComplaintDetailsType(cmp);
			compListType.add(cmpType);
		}
		return resp;
	}

	private ComplaintDetailType mapComplaintDetailsType(ComplaintRequest req) {
		ComplaintDetailType reqType = new ComplaintDetailType();
		reqType.setComplaintId(req.getComplaintId());
		reqType.setComplaintType(req.getCmpType());
		reqType.setTxnReferenceId(req.getTxnId());
		reqType.setMobile(req.getMobile());
		reqType.setServReason(req.getServReason());
		reqType.setAgentId(req.getAgentId());
		reqType.setBillerId(req.getBillerId());
		reqType.setDisposition(req.getDisposition());
		return reqType;
	}

	private ComplaintRequest maptoComplaintReq(TxnStatusComplainReq txnComplain) {

		ComplaintRequest complaintRequest = new ComplaintRequest();

		complaintRequest.setComplaintId(idGeneratorService.getUniqueID(15, "COMPLAIN"));
		complaintRequest.setCmpType(txnComplain.getComplaintType());
		complaintRequest.setTxnId(txnComplain.getTxnReferenceId());
		complaintRequest.setMsgId(txnComplain.getMsgId());
		complaintRequest.setMobile(txnComplain.getMobile());
		complaintRequest.setCmpType(txnComplain.getComplaintType());
		complaintRequest.setAgentId(txnComplain.getAgentId());
		complaintRequest.setBillerId(txnComplain.getBillerId());
		complaintRequest.setCategory(txnComplain.getCategory());
		complaintRequest.setServReason(txnComplain.getServReason());
		complaintRequest.setParticipatetype(txnComplain.getParticipationType());
		complaintRequest.setDisposition(txnComplain.getDisposition());
		complaintRequest.setCmsDescription(txnComplain.getDescription());
		/*
		 * complaintRequest.setCrtnTS(crtnTS);
		 * complaintRequest.setLastUpdtTS(lastUpdtTS);
		 * complaintRequest.setCmpStatus(cmpStatus);
		 */
		return complaintRequest;

	}

	@Override
	public ComplaintRaisedResponse getComplaintRaisedResponse(String refId) {
		ComplaintRaisedResponse resp = mapToResponse(complaintResponseService.getResponse(refId));
		return resp;
	}

	private ComplaintRaisedResponse mapToResponse(ComplaintResponse resp) {
		ComplaintRaisedResponse compResp = new ComplaintRaisedResponse();
		compResp.setComplaintId(resp.getComplaintIdByCU());
		compResp.setOpenComplaint(resp.getOpenComplaint());
		compResp.setComplaintStatus(resp.getComplaintStatus());
		compResp.setAssigned(resp.getAssignedTo());
		compResp.setResponseCode(resp.getRespCode());
		compResp.setResponseReason(resp.getRespReason());
		return compResp;
	}
	/*
	 * private String complaintIdGenerator(String ouName){ String
	 * complaintId=null; if(ouName!=null) { complaintId =
	 * ouName.substring(0,2).toUpperCase(); } else { complaintId = "AP"; }
	 * 
	 * return complaintId+System.currentTimeMillis();
	 * 
	 * }
	 */
}