package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.TransactionType;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.StatusField;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.BillerOUtransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.tenant.dao.BillerOUtransactionDataDao;
import com.rssoftware.ou.tenant.service.BillerOUtransactionDataService;


@Service
public class BillerOUtransactionDataServiceImpl implements BillerOUtransactionDataService{

	private final static Logger logger = LoggerFactory.getLogger(BillerOUtransactionDataServiceImpl.class);

	@Autowired
	private BillerOUtransactionDataDao billerOUtransactionDataDao;

	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public TransactionDataView getTransactionData(String refId,
			RequestType requestType) throws IOException{
		TransactionDataPK pk = new TransactionDataPK();
		pk.setRefId(refId);
		pk.setTxnType(requestType != null ? requestType.name() : null);
		try {
			return mapFrom(billerOUtransactionDataDao.get(pk));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}
	
	@Override
	public void insert(String refId, BillFetchRequest billFetchRequest) {
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(refId);
		tdv.setRequestType(RequestType.FETCH);
		tdv.setMessageId(billFetchRequest.getTxn().getMsgId());
		//tdv.setTxnRefId(billFetchRequest.getTxn().getTxnReferenceId());
		tdv.setBillFetchRequest(billFetchRequest);
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.SENT);

		try {
			billerOUtransactionDataDao.create(mapTo(tdv));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void insert(String refId, BillPaymentRequest billPaymentRequest) {
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(refId);
		tdv.setRequestType(RequestType.PAYMENT);
		tdv.setMessageId(billPaymentRequest.getTxn().getMsgId());
		tdv.setTxnRefId(billPaymentRequest.getTxn().getTxnReferenceId());
		tdv.setBillPaymentRequest(billPaymentRequest);
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setStatus(RequestStatus.SENT);
		tdv.setCuStatus(RequestStatus.PAYMENT_INITIATED);
		String ccf = billPaymentRequest.getAmount().getAmt().getCustConvFee();
		double amt = 0;
		if(ccf!=null) {
			amt = Double.parseDouble(billPaymentRequest.getAmount().getAmt().getAmount())+Double.parseDouble(ccf);
		}
		tdv.setBillAmount(new BigDecimal(amt));

		try {
			billerOUtransactionDataDao.create(mapTo(tdv));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	public TransactionDataView update(String refId,
			BillFetchResponse billFetchResponse) throws ValidationException, IOException {
		TransactionDataView tdv = getTransactionData(refId, RequestType.FETCH);
		if (tdv == null) {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.REQUEST_NOT_FOUND);
		} /*else if (tdv.getStatus() != RequestStatus.SENT || billFetchResponse == null) {
			throw ValidationException.getInstance(ValidationException.ValidationErrorReason.ILLEGAL_OPERATION);
		}*/
		tdv.setBillFetchResponse(billFetchResponse);
		tdv.setLastUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		/*		if (billFetchResponse.getReason() != null
				&& CommonConstants.RESP_SUCCESS_MSG.equals(billFetchResponse.getReason().getResponseReason())) {
			tdv.setStatus(RequestStatus.RESPONSE_SUCCESS);
		} else {
			tdv.setStatus(RequestStatus.RESPONSE_DECLINE);
		}
*/      
		try {
			BillerOUtransactionData transactionData = billerOUtransactionDataDao.createOrUpdate(mapTo(tdv));
			//System.out.print(new String);
			return mapFrom(transactionData);
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}

	@Override
	public TransactionDataView update(String refId,
			BillPaymentResponse billPaymentResponse) throws IOException{
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

		try {
			BillerOUtransactionData transactionData = billerOUtransactionDataDao.createOrUpdate(mapTo(tdv));
			return mapFrom(transactionData);
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}
	
	@Override
	public void updateStatus(String refId, StatusField statusField,
			RequestType requestType, RequestStatus status) throws IOException{
		TransactionDataView tdv = getTransactionData(refId, requestType);
		if (tdv != null) {
			switch(statusField){
			case CU_STATUS:
				tdv.setCuStatus(status);
				break;
			case CU_REV_STATUS:	
				tdv.setCuRevStatus(status);
				break;
			case BLR_STATUS:
				tdv.setBlrStatus(status);
				break;
			case BLR_REV_STATUS:
				tdv.setBlrRevStatus(status);
			}
			tdv.setLastUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
			try {
				billerOUtransactionDataDao.createOrUpdate(mapTo(tdv));
			} catch (IOException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
				throw new IOException(e);
			}
		}
		
	}
	
	
	private TransactionDataView mapFrom(
			BillerOUtransactionData td) throws IOException {
		if (td == null) {
			return null;
		}
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(td.getId().getRefId());
		tdv.setRequestType(RequestType.valueOf(td.getId().getTxnType()));
		tdv.setMessageId(td.getMsgId());
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

		if (tdv.getRequestType() == RequestType.FETCH) {
			if (td.getCuRequestJson() != null) {
				tdv.setBillFetchRequest(objectMapper.readValue(td.getCuRequestJson(), BillFetchRequest.class));
			}
			if (td.getCuResponseJson() != null) {
				tdv.setBillFetchResponse(objectMapper.readValue(td.getCuResponseJson(), BillFetchResponse.class));
				System.out.println(tdv.getBillFetchResponse().toString());
			}
		} else if (tdv.getRequestType() == RequestType.PAYMENT) {
			if (td.getCuRequestJson() != null) {
				tdv.setBillPaymentRequest(objectMapper.readValue(td.getCuRequestJson(), BillPaymentRequest.class));
			}
			if (td.getCuResponseJson() != null) {
				tdv.setBillPaymentResponse(objectMapper.readValue(td.getCuResponseJson(), BillPaymentResponse.class));
			}
		}
		if (tdv.getRequestType() == RequestType.FETCH) {
			if (td.getBlrRequestJson() != null) {
				tdv.setBlrFetchRequestJson(objectMapper.readValue(td.getBlrRequestJson(), BillFetchRequest.class));
			}
			if (td.getBlrResponseJson() != null) {
				tdv.setBlrFetchResponseJson(objectMapper.readValue(td.getBlrResponseJson(), BillFetchResponse.class));
				System.out.println(tdv.getBillFetchResponse().toString());
			}
		} else if (tdv.getRequestType() == RequestType.PAYMENT) {
			if (td.getBlrRequestJson() != null) {
				tdv.setBlrPayRequestJson(objectMapper.readValue(td.getBlrRequestJson(), BillPaymentRequest.class));
			}
			if (td.getBlrRequestJson() != null) {
				tdv.setBlrPayResponseJson(objectMapper.readValue(td.getBlrRequestJson(), BillPaymentResponse.class));
			}
		}
		tdv.setCreationTimestamp(td.getCrtnTs());
		tdv.setLastUpdateTimestamp(td.getUpdtTs());
		tdv.setCuStatus(td.getCuStatus()  != null ? RequestStatus.valueOf(td.getCuStatus()) : null);
		tdv.setCuRevStatus(td.getCuRevStatus()  != null ? RequestStatus.valueOf(td.getCuRevStatus()) : null);
		tdv.setBlrStatus(td.getBlrStatus()  != null ? RequestStatus.valueOf(td.getBlrStatus()) : null);
		tdv.setBlrRevStatus(td.getBlrRevStatus()  != null ? RequestStatus.valueOf(td.getBlrRevStatus()) : null);
		tdv.setTxnRefId(td.getTxnRefId());
		tdv.setReconTs(td.getReconTs());
		tdv.setReconDescription(td.getReconDescription());
		// tdv.setReconStatus(ReconStatus.valueOf(td.getReconStatus()));
		return tdv;
	}
	private BillerOUtransactionData mapTo(TransactionDataView tdv) throws IOException {
		if (tdv == null) {
			return null;
		}
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

		BillerOUtransactionData td = new BillerOUtransactionData();
		TransactionDataPK tdPK = new TransactionDataPK();

		tdPK.setRefId(tdv.getRefId());
		tdPK.setTxnType(tdv.getRequestType().name());
		td.setId(tdPK);
		td.setMsgId(tdv.getMessageId());
		td.setAgentChannelID(tdv.getAgentChannelID());
		td.setAgentChannelCustomerID(tdv.getAgentChannelCustID());
		td.setAgentChannelTxnID(tdv.getAgentChannelTxnID());
		td.setAgentID(tdv.getAgentID());
		td.setBillerId(tdv.getBillerID());
		td.setNodeAddress(tdv.getCurrentNodeAddress());
		// td.setPAN(tdv.getPAN());
		// td.setAADHAAR(tdv.getAADHAAR());
		// td.setEMAIL(tdv.getEMAIL());
		td.setMobile(tdv.getMobile());
		td.setDateOfTxn(tdv.getDateOfTxn());
		td.setTags(tdv.getTags());
		if (tdv.getRequestType() == RequestType.FETCH) {
			if (tdv.getBillFetchRequest() != null) {
				td.setCuRequestJson(objectMapper.writeValueAsBytes(tdv.getBillFetchRequest()));
			}
			if (tdv.getBillFetchResponse() != null) {
				td.setCuResponseJson(objectMapper.writeValueAsBytes(tdv.getBillFetchResponse()));
				
			}
		} else if (tdv.getRequestType() == RequestType.PAYMENT) {
			if (tdv.getBillPaymentRequest() != null) {
				td.setCuRequestJson(objectMapper.writeValueAsBytes(tdv.getBillPaymentRequest()));
			}
			if (tdv.getBillPaymentResponse() != null) {
				td.setCuResponseJson(objectMapper.writeValueAsBytes(tdv.getBillPaymentResponse()));
			}
		}
		if (tdv.getRequestType() == RequestType.FETCH) {
			if (tdv.getBlrFetchRequestJson() != null) {
				td.setBlrRequestJson(objectMapper.writeValueAsBytes(tdv.getBlrFetchRequestJson()));
			}
			if (tdv.getBlrFetchResponseJson() != null) {
				td.setBlrResponseJson(objectMapper.writeValueAsBytes(tdv.getBlrFetchResponseJson()));
				
			}
		} else if (tdv.getRequestType() == RequestType.PAYMENT) {
			if (tdv.getBlrFetchRequestJson() != null) {
				td.setBlrRequestJson(objectMapper.writeValueAsBytes(tdv.getBlrFetchRequestJson()));
			}
			if (tdv.getBlrFetchResponseJson() != null) {
				td.setBlrResponseJson(objectMapper.writeValueAsBytes(tdv.getBlrFetchResponseJson()));
			}
		}
		td.setCrtnTs(tdv.getCreationTimestamp() != null ? tdv.getCreationTimestamp() : null);
		td.setUpdtTs(tdv.getLastUpdateTimestamp() != null ? tdv.getLastUpdateTimestamp() : null);
		td.setCuStatus(tdv.getCuStatus() != null ? tdv.getCuStatus().name() : null);
		td.setCuRevStatus(tdv.getCuRevStatus()  != null ? tdv.getCuRevStatus().name() : null);
		td.setBlrStatus(tdv.getBlrStatus() != null ? tdv.getBlrStatus().name() : null);
		td.setBlrRevStatus(tdv.getBlrRevStatus()  != null ? tdv.getBlrRevStatus().name() : null);
		td.setTxnRefId(tdv.getTxnRefId() != null ? tdv.getTxnRefId() : null);
		td.setReconTs(tdv.getReconTs() != null ? tdv.getReconTs() : null);
		td.setReconDescription(tdv.getReconDescription()  != null ? tdv.getReconDescription() : null);
		td.setReconStatus(tdv.getReconStatus() != null ? tdv.getReconStatus().name() : null);
		td.setReconCyclenNo(tdv.getReconCycleNo());
		return td;
	}

	@Override
	public void insertReversalRequest(String refId,BillPaymentRequest billPaymentRequest) {
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId(refId);
		tdv.setRequestType(RequestType.REVERSAL);
		tdv.setMessageId(billPaymentRequest.getTxn().getMsgId());
		tdv.setTxnRefId(billPaymentRequest.getTxn().getTxnReferenceId());
		tdv.setBillPaymentRequest(billPaymentRequest);
		tdv.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		tdv.setCuStatus(RequestStatus.REQUEST_RECIEVED);
		try {
			billerOUtransactionDataDao.create(mapTo(tdv));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public TransactionDataView getByTxnRefId(String txnRefId) throws IOException {
		
		try {
			TransactionDataView txnData = mapFrom(billerOUtransactionDataDao.getByTransactionRefId(txnRefId));
			return txnData;
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}
	@Override
	public TransactionDataView updateReversalresponse(String refId,BillPaymentResponse billPaymentResponse) throws IOException{
		TransactionDataView tdv = getTransactionData(refId, RequestType.REVERSAL);
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

		try {
			BillerOUtransactionData transactionData = billerOUtransactionDataDao.createOrUpdate(mapTo(tdv));
			return mapFrom(transactionData);
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
	}
	

}
