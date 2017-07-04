package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.TransactionType;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.TransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus;
import com.rssoftware.ou.tenant.dao.TransactionDataDao;
import com.rssoftware.ou.tenant.service.TransactionDataService;

import in.co.rssoftware.bbps.schema.TxnDetailType;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

@Service
public class TransactionDataServiceImpl implements TransactionDataService {
	
	private static Log logger = LogFactory.getLog(TransactionDataServiceImpl.class);

	@Autowired
	private TransactionDataDao transactionDataDao;

	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public TransactionDataView getTransactionData(String refId, RequestType requestType) throws IOException {
		TransactionDataPK pk = new TransactionDataPK();
		pk.setRefId(refId);
		pk.setTxnType(requestType != null ? requestType.name() : null);
		try {
			return mapFrom(transactionDataDao.get(pk));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}

	// ..Modified..//
	@Override
	public void insert(BillFetchRequestExt billFetchRequestExt) throws IOException {
		TransactionDataView tdv = new TransactionDataView();

		// ..Modified..//
		tdv.setAgentChannelID(billFetchRequestExt.getAgentChannelID());
		tdv.setAgentChannelCustID(billFetchRequestExt.getAgentChannelCustomerID());
		tdv.setAgentChannelTxnID(billFetchRequestExt.getAgentChannelTransactionID());
		tdv.setCurrentNodeAddress(billFetchRequestExt.getCurrentNodeAddress());

		if (billFetchRequestExt.getBillFetchRequest() != null) {
			tdv.setAgentID(billFetchRequestExt.getBillFetchRequest().getAgent().getId());
			tdv.setMobile(billFetchRequestExt.getBillFetchRequest().getCustomer().getMobile());
			tdv.setRefId(billFetchRequestExt.getBillFetchRequest().getHead().getRefId());
			String custParam = null;
			/*
			 * for (Tag tag :
			 * billFetchRequestExt.getBillFetchRequest().getCustomer().getTags()
			 * ) { switch (tag.getName()) { case "EMAIL":
			 * tdv.setEMAIL(tag.getValue()); break; case "PAN":
			 * tdv.setPAN(tag.getValue()); break; case "AADHAAR":
			 * tdv.setAADHAAR(tag.getValue()); break; default: custParam +=
			 * tag.getValue() + " , "; break; } }
			 */
			if (custParam != null) {
				custParam = custParam.replaceAll(" , $", "");
			}

			tdv.setTags(custParam);
			tdv.setBillerID(billFetchRequestExt.getBillFetchRequest().getBillDetails().getBiller().getId());
			tdv.setDateOfTxn(billFetchRequestExt.getBillFetchRequest().getTxn().getTs());
			tdv.setMessageId(billFetchRequestExt.getBillFetchRequest().getTxn().getMsgId());
		}
		// ..Modified..//
		tdv.setRequestType(RequestType.FETCH);

		tdv.setBillFetchRequest(billFetchRequestExt.getBillFetchRequest());
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.SENT);

		try {
			transactionDataDao.create(mapFrom(tdv));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}
	// ..Modified..//

	// .. Modified by Samarjit ..//
	@Override
	public void insert(BillPaymentRequestExt billPaymentRequest) throws IOException {
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(billPaymentRequest.getBillPaymentRequest().getHead().getRefId());

		// Changes for adding Agent Details

		tdv.setAgentChannelID(billPaymentRequest.getAgentChannelID());
		tdv.setAgentChannelCustID(billPaymentRequest.getAgentChannelCustomerID());
		tdv.setAgentChannelTxnID(billPaymentRequest.getAgentChannelTransactionID());
		tdv.setAgentID(billPaymentRequest.getBillPaymentRequest().getAgent().getId());
		tdv.setMobile(billPaymentRequest.getBillPaymentRequest().getCustomer().getMobile());
		tdv.setBillAmount(new BigDecimal(billPaymentRequest.getBillPaymentRequest().getAmount().getAmt().getAmount()));
		String custParam = null;
		/*
		 * for (Tag tag :
		 * billPaymentRequest.getBillPaymentRequest().getCustomer().getTags()) {
		 * switch (tag.getName()) { case "EMAIL": tdv.setEMAIL(tag.getValue());
		 * break; case "PAN": tdv.setPAN(tag.getValue()); break; case "AADHAAR":
		 * tdv.setAADHAAR(tag.getValue()); break; default: custParam +=
		 * tag.getValue() + " , "; break; } }
		 */
		if (custParam != null) {
			custParam = custParam.replaceAll(" , $", "");
		}

		tdv.setTags(custParam);
		tdv.setBillerID(billPaymentRequest.getBillPaymentRequest().getBillDetails().getBiller().getId());
		tdv.setDateOfTxn(billPaymentRequest.getBillPaymentRequest().getTxn().getTs());

		tdv.setRequestType(RequestType.PAYMENT);
		tdv.setMessageId(billPaymentRequest.getBillPaymentRequest().getTxn().getMsgId());
		tdv.setTxnRefId(billPaymentRequest.getBillPaymentRequest().getTxn().getTxnReferenceId());
		tdv.setBillPaymentRequest(billPaymentRequest.getBillPaymentRequest());
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.SENT);
		tdv.setCurrentNodeAddress(billPaymentRequest.getCurrentNodeAddress());

		try {
			transactionDataDao.create(mapFrom(tdv));
		} catch (IOException e) {
			    logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}

	}
	// .. Modified by Samarjit ..//

	@Override
	public void insert(String refId, BillFetchRequest billFetchRequest) throws IOException {
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(refId);
		tdv.setRequestType(RequestType.FETCH);
		tdv.setMessageId(billFetchRequest.getTxn().getMsgId());
		tdv.setBillFetchRequest(billFetchRequest);
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.SENT);

		try {
			transactionDataDao.create(mapFrom(tdv));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}

	@Override
	public void insert(String refId, BillPaymentRequest billPaymentRequest) throws IOException {
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(refId);
		tdv.setRequestType(RequestType.PAYMENT);
		tdv.setMessageId(billPaymentRequest.getTxn().getMsgId());
		tdv.setTxnRefId(billPaymentRequest.getTxn().getTxnReferenceId());
		tdv.setBillPaymentRequest(billPaymentRequest);
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.SENT);

		try {
			transactionDataDao.create(mapFrom(tdv));
		} catch (IOException e) {
		    logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}

	}

	@Override
	public TransactionDataView update(String refId, BillFetchResponse billFetchResponse) throws ValidationException, IOException {
		TransactionDataView tdv = getTransactionData(refId, RequestType.FETCH);
		if (tdv == null) {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REQUEST_NOT_FOUND);
		} else if (tdv.getStatus() != RequestStatus.SENT || billFetchResponse == null) {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.ILLEGAL_OPERATION);
		}

		tdv.setBillFetchResponse(billFetchResponse);
		tdv.setLastUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		if (billFetchResponse.getReason() != null
				&& CommonConstants.RESP_SUCCESS_MSG.equals(billFetchResponse.getReason().getResponseReason())) {
			tdv.setStatus(RequestStatus.RESPONSE_SUCCESS);
		} else {
			tdv.setStatus(RequestStatus.RESPONSE_DECLINE);
		}
		
		if(billFetchResponse.getBillerResponse() != null)
			tdv.setBillDate(billFetchResponse.getBillerResponse().getBillDate());
		if(billFetchResponse.getBillerResponse() != null)
			tdv.setBillAmount(new BigDecimal(billFetchResponse.getBillerResponse().getAmount()));

		try {
			TransactionData transactionData = transactionDataDao.createOrUpdate(mapFrom(tdv));
			return mapFrom(transactionData);
		} catch (IOException e) {
			    logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}

	@Override
	public TransactionDataView update(String refId, BillPaymentResponse billPaymentResponse) throws IOException {
		TransactionDataView tdv = getTransactionData(refId, RequestType.PAYMENT);
		if (tdv == null) {
			ValidationException.getInstance(ValidationException.ValidationErrorReason.REQUEST_NOT_FOUND);
		} else if (tdv.getStatus() != RequestStatus.SENT || billPaymentResponse == null) {
			ValidationException.getInstance(ValidationException.ValidationErrorReason.ILLEGAL_OPERATION);
		}

		tdv.setBillPaymentResponse(billPaymentResponse);
		tdv.setLastUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		if (billPaymentResponse.getReason() != null && billPaymentResponse.getTxn() != null
				&& TransactionType.FORWARD_TYPE_RESPONSE.value().equals(billPaymentResponse.getTxn().getType())
				&& CommonConstants.RESP_SUCCESS_MSG.equals(billPaymentResponse.getReason().getResponseReason())) {
			tdv.setStatus(RequestStatus.RESPONSE_SUCCESS);
		} else if (billPaymentResponse.getTxn() != null
				&& TransactionType.REVERSAL_TYPE_RESPONSE.value().equals(billPaymentResponse.getTxn().getType())) {
			tdv.setStatus(RequestStatus.RESPONSE_REVERSE);
		} else {
			tdv.setStatus(RequestStatus.RESPONSE_DECLINE);
		}
		
		tdv.setBillDate(billPaymentResponse.getBillerResponse().getBillDate());
		tdv.setBillAmount(new BigDecimal(billPaymentResponse.getBillerResponse().getAmount()));

		try {
			TransactionData transactionData = transactionDataDao.createOrUpdate(mapFrom(tdv));
			return mapFrom(transactionData);
		} catch (IOException e) {
			    logger.error( e.getMessage(), e);
	            logger.info("In Excp : " +e.getMessage());
			throw new IOException(e);
		}
	}

	@Override
	public void updateStatus(String refId, RequestType requestType, RequestStatus status) throws IOException {

		TransactionDataView tdv = getTransactionData(refId, requestType);
		if (tdv != null) {
			tdv.setStatus(status);
			tdv.setLastUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			try {
				transactionDataDao.createOrUpdate(mapFrom(tdv));
			} catch (IOException e) {
				logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
				throw new IOException(e);
			}
		}

	}

	@Override
	public void updateReconStatus(String refId, RequestType requestType, ReconStatus status, String description,
			BigDecimal reconCycleNo) throws IOException {
		TransactionDataView tdv = getTransactionData(refId, requestType);
		if (tdv != null) {
			tdv.setReconStatus(status);
			tdv.setReconDescription(description);
			tdv.setReconTs(new Timestamp(System.currentTimeMillis()));
			tdv.setReconCycleNo(reconCycleNo);
			try {
				transactionDataDao.createOrUpdate(mapFrom(tdv));
			} catch (IOException e) {
				    logger.error( e.getMessage(), e);
		            logger.info("In Excp : " + e.getMessage());
				throw new IOException(e);
			}
		}
	}

	private TransactionDataView mapFrom(TransactionData td) throws IOException {
		if (td == null) {
			return null;
		}
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(td.getId().getRefId());
		tdv.setRequestType(RequestType.valueOf(td.getId().getTxnType()));
		tdv.setMessageId(td.getMsgId());

		// ..Modified..//
		tdv.setAgentChannelID(td.getAgentChannelID());
		tdv.setAgentChannelCustID(td.getAgentChannelCustomerID());
		tdv.setAgentChannelTxnID(td.getAgentChannelTxnID());

		tdv.setAgentID(td.getAgentID());
		tdv.setBillerID(td.getBillerId());
		// tdv.setPAN(td.getPAN());
		// tdv.setAADHAAR(td.getAADHAAR());
		// tdv.setEMAIL(td.getEMAIL());
		tdv.setMobile(td.getMobile());
		tdv.setDateOfTxn(td.getDateOfTxn());
		tdv.setTags(td.getTags());
		tdv.setCurrentNodeAddress(td.getNodeAddress());
		tdv.setBillAmount(td.getBillAmount());

		// ..Modified..//

		if (tdv.getRequestType() == RequestType.FETCH) {
			if (td.getRequestJson() != null) {
				tdv.setBillFetchRequest(objectMapper.readValue(td.getRequestJson(), BillFetchRequest.class));
			}
			if (td.getResponseJson() != null) {
				tdv.setBillFetchResponse(objectMapper.readValue(td.getResponseJson(), BillFetchResponse.class));
			}
		} else if (tdv.getRequestType() == RequestType.PAYMENT) {
			if (td.getRequestJson() != null) {
				tdv.setBillPaymentRequest(objectMapper.readValue(td.getRequestJson(), BillPaymentRequest.class));
			}
			if (td.getResponseJson() != null) {
				tdv.setBillPaymentResponse(objectMapper.readValue(td.getResponseJson(), BillPaymentResponse.class));
			}
		}
		tdv.setCreationTimestamp(td.getCrtnTs());
		tdv.setLastUpdateTimestamp(td.getUpdtTs());
		tdv.setStatus(RequestStatus.valueOf(td.getCurrentStatus()));

		tdv.setTxnRefId(td.getTxnRefId());
		tdv.setReconTs(td.getReconTs());
		tdv.setReconDescription(td.getReconDescription());
		// tdv.setReconStatus(ReconStatus.valueOf(td.getReconStatus()));
		tdv.setOnUs(td.isOnus());
		return tdv;
	}

	private TransactionData mapFrom(TransactionDataView tdv) throws IOException {
		if (tdv == null) {
			return null;
		}
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

		TransactionData td = new TransactionData();
		TransactionDataPK tdPK = new TransactionDataPK();

		tdPK.setRefId(tdv.getRefId());
		tdPK.setTxnType(tdv.getRequestType().name());
		td.setId(tdPK);
		td.setMsgId(tdv.getMessageId());

		// ..Modified..//
		td.setAgentChannelID(tdv.getAgentChannelID());
		td.setAgentChannelCustomerID(tdv.getAgentChannelCustID());
		td.setAgentChannelTxnID(tdv.getAgentChannelTxnID());
		td.setAgentID(tdv.getAgentID());
		td.setBillerId(tdv.getBillerID());
		td.setNodeAddress(tdv.getCurrentNodeAddress());
		// td.setPAN(tdv.getPAN());
		// td.setAADHAAR(tdv.getAADHAAR());
		// td.setEMAIL(tdv.getEMAIL());
		td.setBillAmount(tdv.getBillAmount());
		td.setBillDate(tdv.getBillDate());
		td.setMobile(tdv.getMobile());
		td.setDateOfTxn(tdv.getDateOfTxn());
		td.setTags(tdv.getTags());
		// ..Modified..//

		if (tdv.getRequestType() == RequestType.FETCH) {
			if (tdv.getBillFetchRequest() != null) {
				td.setRequestJson(objectMapper.writeValueAsBytes(tdv.getBillFetchRequest()));
			}
			if (tdv.getBillFetchResponse() != null) {
				td.setResponseJson(objectMapper.writeValueAsBytes(tdv.getBillFetchResponse()));
			}
		} else if (tdv.getRequestType() == RequestType.PAYMENT) {
			if (tdv.getBillPaymentRequest() != null) {
				td.setRequestJson(objectMapper.writeValueAsBytes(tdv.getBillPaymentRequest()));
			}
			if (tdv.getBillPaymentResponse() != null) {
				td.setResponseJson(objectMapper.writeValueAsBytes(tdv.getBillPaymentResponse()));
			}
		}

		td.setCrtnTs(tdv.getCreationTimestamp());
		td.setUpdtTs(tdv.getLastUpdateTimestamp());
		td.setCurrentStatus(tdv.getStatus().name());

		td.setTxnRefId(tdv.getTxnRefId());
		td.setReconTs(tdv.getReconTs());
		td.setReconDescription(tdv.getReconDescription());
		td.setReconStatus(tdv.getReconStatus() != null ? tdv.getReconStatus().name() : "UNREAD");
		td.setReconCycleNo(tdv.getReconCycleNo());
		if(tdv.isOnUs()!=null){
		     td.setIsOnus(tdv.isOnUs());
		}else{
			 td.setIsOnus(false);
		}
		return td;
	}

	@Override
	public void insertQuickPay(String refId, BillPaymentRequest billPaymentRequest) throws IOException {

		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(refId);
		tdv.setRequestType(RequestType.PAYMENT);
		tdv.setMessageId(billPaymentRequest.getTxn().getMsgId());
		tdv.setTxnRefId(billPaymentRequest.getTxn().getTxnReferenceId());
		tdv.setBillPaymentRequest(billPaymentRequest);
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.QPAY_INITIATED);

		try {
			transactionDataDao.create(mapFrom(tdv));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}

	}

	@Override
	public List<TransactionDataView> getFilteredBillers(String mobile) {
		List<TransactionDataView> dataViews = null;
		try {
			dataViews = mapPortalFrom(transactionDataDao.getFilteredBillers(mobile));
		} catch (IOException e) {

			    logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
		}
		return dataViews;

	}

	@Override
	public List<TransactionDataView> fetchAllTxnByMobile(String mobile) {
		List<TransactionDataView> rcds = null;
		try {
			List<TransactionData> tdList = transactionDataDao.getAllTxnByMobile(mobile);
			rcds = mapPortalFrom(tdList);
		} catch (IOException e) {

			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
		}
		return rcds;

	}

	private List<TransactionDataView> mapPortalFrom(List<TransactionData> tdList) throws IOException {

		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		if (tdList == null) {
			return null;
		}
		List<TransactionDataView> transactionDataViews = new ArrayList<>();
		TransactionDataView tdv = null;
		for (TransactionData transactionData : tdList) {
			tdv = new TransactionDataView();
			tdv.setRefId(transactionData.getId().getRefId());
			tdv.setRequestType(RequestType.valueOf(transactionData.getId().getTxnType()));
			tdv.setMessageId(transactionData.getMsgId());
			if (tdv.getRequestType() == RequestType.FETCH) {
				if (transactionData.getRequestJson() != null) {
					tdv.setBillFetchRequest(
							objectMapper.readValue(transactionData.getRequestJson(), BillFetchRequest.class));
				}
				if (transactionData.getResponseJson() != null) {
					tdv.setBillFetchResponse(
							objectMapper.readValue(transactionData.getResponseJson(), BillFetchResponse.class));
				}
			} else if (tdv.getRequestType() == RequestType.PAYMENT) {
				if (transactionData.getRequestJson() != null) {
					tdv.setBillPaymentRequest(
							objectMapper.readValue(transactionData.getRequestJson(), BillPaymentRequest.class));
				}
				if (transactionData.getResponseJson() != null) {
					tdv.setBillPaymentResponse(
							objectMapper.readValue(transactionData.getResponseJson(), BillPaymentResponse.class));
				}
			}
			tdv.setCreationTimestamp(transactionData.getCrtnTs());
			tdv.setLastUpdateTimestamp(transactionData.getUpdtTs());
			tdv.setStatus(RequestStatus.valueOf(transactionData.getCurrentStatus()));
			tdv.setBillDate(transactionData.getBillDate());
			tdv.setBillAmount(transactionData.getBillAmount());
			tdv.setTxnRefId(transactionData.getTxnRefId());
			tdv.setReconTs(transactionData.getReconTs());
			tdv.setReconDescription(transactionData.getReconDescription());
			tdv.setBillerID(transactionData.getBillerId());
			tdv.setMobile(transactionData.getMobile());
			transactionDataViews.add(tdv);
		}

		// tdv.setReconStatus(ReconStatus.valueOf(.getReconStatus()));

		return transactionDataViews;
	}

	@Override
	public List<TransactionDataView> getFilteredTransactionView(String strId) { // strId
																				// may
																				// be
																				// AgentID
																				// or
																				// CustomerID
		List<TransactionDataView> dataViews = null;
		try {
			dataViews = mapPortalFrom(transactionDataDao.getFilteredTransactionView(strId));
		} catch (IOException e) {

		    logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
		}
		return dataViews;

	}

	@Override
	public List<TransactionDataView> getFilteredTransactionByDate(String agentId, String strStartDate,
			String strEndDate) {
		List<TransactionDataView> dataViews = null;
		try {
			dataViews = mapPortalFrom(
					transactionDataDao.getFilteredTransactionByDate(agentId, strStartDate, strEndDate));
		} catch (IOException e) {

			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
		}
		return dataViews;

	}
	
	@Override
	public List<TransactionDataView> getFilteredTransactionByDateRangeAndMobile(String mobile, String strStartDate,
			String strEndDate) {
		List<TransactionDataView> dataViews = null;
		try {
			dataViews = mapPortalFrom(
					transactionDataDao.getFilteredTransactionByDateRangeWithMobile(mobile, strStartDate, strEndDate));
		} catch (IOException e) {

			logger.error( e.getMessage(), e);
            logger.info("In Excp : " + e.getMessage());
		}
		return dataViews;

	}

	@Override
	public Long txnCountByType(String txnType, String agentId) {
		Long cnt = transactionDataDao.countTxnByType(txnType, agentId);
		return cnt;
	}

	@Override
	public String totalTxnAmount(String txnType, String agentId, String startDate, String endDate) {
		String amt = transactionDataDao.totalAmountCollected(txnType, agentId, startDate, endDate);
		return amt;
	}

	@Override
	public TxnSearchResponse getTxnJaxb(List<TransactionDataView> txnViews) {
		TxnSearchResponse txnList = null;
		if (txnViews != null) {
			txnList = new TxnSearchResponse();

			for (TransactionDataView tvd : txnViews) {
				txnList.getTxnDetails().add(mapToJaxb(tvd));
			}
		}
		return txnList;
	}

	public static TxnDetailType mapToJaxb(TransactionDataView txnView) {
		TxnDetailType txnDetailType = new TxnDetailType();
		txnDetailType.setAgentId(txnView.getAgentID());
		txnDetailType.setAmount(txnView.getBillAmount() == null ? "0" : txnView.getBillAmount().toPlainString());
		txnDetailType.setBillerId(txnView.getBillerID());

		// txnDetailType.setTxnDate(txnView.getCreationTimestamp().toString());

		txnDetailType.setTxnReferenceId(txnView.getTxnRefId());
		txnDetailType.setTxnStatus(txnView.getStatus().name());
		return txnDetailType;
	}

	@Override
	public TransactionDataView getFilteredTransactionByID(String txnId) {
		TransactionDataView dataViews = null;
		try {
			dataViews = mapFrom(transactionDataDao.getPaymentTransactionDataByTranRefId(txnId));
		} catch (IOException e) {

			e.printStackTrace();
		}
		return dataViews;

	}

	
	@Override
	public TransactionDataView getPaymentTransactionDataByTranRefId(String tranRefId) {
		try {
			return mapFrom(transactionDataDao.getPaymentTransactionDataByTranRefId(tranRefId));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void update(String refId, BillPaymentRequest billPaymentRequest) throws ValidationException, IOException {
		TransactionDataView tdv = getTransactionData(refId, RequestType.PAYMENT);
		if (tdv == null){
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REQUEST_NOT_FOUND);
		}		
		
		tdv.setBillPaymentRequest(billPaymentRequest);;
		tdv.setLastUpdateTimestamp(new Timestamp(System.currentTimeMillis()));	
		
		try {
			transactionDataDao.createOrUpdate(mapFrom(tdv));
		} catch (IOException e) {
			    logger.error( e.getMessage(), e);
	            logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}
	
	@Override
	public void insert(BillPaymentRequestExt billPaymentRequest,Boolean isOnus){
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(billPaymentRequest.getBillPaymentRequest().getHead().getRefId());
    	tdv.setAgentChannelID(billPaymentRequest.getAgentChannelID());
		tdv.setAgentChannelCustID(billPaymentRequest.getAgentChannelCustomerID());
		tdv.setAgentChannelTxnID(billPaymentRequest.getAgentChannelTransactionID());
		tdv.setAgentID(billPaymentRequest.getBillPaymentRequest().getAgent().getId());
		tdv.setMobile(billPaymentRequest.getBillPaymentRequest().getCustomer().getMobile());
		tdv.setBillAmount(new BigDecimal(billPaymentRequest.getBillPaymentRequest().getAmount().getAmt().getAmount()));
		tdv.setBillerID(billPaymentRequest.getBillPaymentRequest().getBillDetails().getBiller().getId());
		tdv.setDateOfTxn(billPaymentRequest.getBillPaymentRequest().getTxn().getTs());
		tdv.setRequestType(RequestType.PAYMENT);
		tdv.setMessageId(billPaymentRequest.getBillPaymentRequest().getTxn().getMsgId());
		tdv.setTxnRefId(billPaymentRequest.getBillPaymentRequest().getTxn().getTxnReferenceId());
		tdv.setBillPaymentRequest(billPaymentRequest.getBillPaymentRequest());
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setCurrentNodeAddress(billPaymentRequest.getCurrentNodeAddress());
		tdv.setStatus(RequestStatus.PAYMENT_INITIATED);
		tdv.setOnUs(isOnus);
		try {
			transactionDataDao.create(mapFrom(tdv));
		} catch (IOException e) {
			    logger.error( "Exception - insert BillPaymentRequestExt", e);
		}

	}
	@Override
	public void insert(BillFetchRequestExt billFetchRequestExt,Boolean isOnus){
		TransactionDataView tdv = new TransactionDataView();
		tdv.setAgentChannelID(billFetchRequestExt.getAgentChannelID());
		tdv.setAgentChannelCustID(billFetchRequestExt.getAgentChannelCustomerID());
		tdv.setAgentChannelTxnID(billFetchRequestExt.getAgentChannelTransactionID());
		tdv.setCurrentNodeAddress(billFetchRequestExt.getCurrentNodeAddress());
		if (billFetchRequestExt.getBillFetchRequest() != null) {
			tdv.setAgentID(billFetchRequestExt.getBillFetchRequest().getAgent().getId());
			tdv.setMobile(billFetchRequestExt.getBillFetchRequest().getCustomer().getMobile());
			tdv.setRefId(billFetchRequestExt.getBillFetchRequest().getHead().getRefId());
			tdv.setBillerID(billFetchRequestExt.getBillFetchRequest().getBillDetails().getBiller().getId());
			tdv.setDateOfTxn(billFetchRequestExt.getBillFetchRequest().getTxn().getTs());
			tdv.setMessageId(billFetchRequestExt.getBillFetchRequest().getTxn().getMsgId());
		}
		tdv.setRequestType(RequestType.FETCH);
		tdv.setBillFetchRequest(billFetchRequestExt.getBillFetchRequest());
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.SENT);
		tdv.setOnUs(isOnus);
		try {
			transactionDataDao.create(mapFrom(tdv));
		} catch (IOException e) {
			logger.error( "Exception - insert BillFetchRequestExt", e);
		}
	}
}
