package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.database.entity.tenant.RawData;
import com.rssoftware.ou.database.entity.tenant.RawDataPK;
import com.rssoftware.ou.database.entity.tenant.TransactionData;
import com.rssoftware.ou.database.entity.tenant.TransactionDataPK;
import com.rssoftware.ou.model.tenant.Differences;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.RawDataView.OUType;
import com.rssoftware.ou.model.tenant.RawDataView.ReconStatus;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.tenant.dao.RawDataDao;
import com.rssoftware.ou.tenant.service.RawDataService;

@Service
public class RawDataServiceImpl implements RawDataService {

	@Autowired
	private RawDataDao rawDataDao;
	private static ObjectMapper objectMapper = new ObjectMapper();
	private final Logger log = LoggerFactory.getLogger(getClass());

	// to fetch a particular row of raw_data
	@Override
	public RawDataView getRawData(String refId, RequestType requestType) throws IOException {
		String METHOD_NAME = "getRawData";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		RawDataPK pk = new RawDataPK();
		pk.setRefId(refId);
		pk.setTxnType(requestType != null ? requestType.name() : null);
		try {
			return mapFrom(rawDataDao.get(pk));
		} 
		catch (IOException e) {
			log.error("Error " + e);
			throw new IOException(e);
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	// to update the reconciliation status of a particular row of raw_data
	@Override
	public void updateReconStatus(String refId, RequestType requestType, RawDataView.ReconStatus status, String description) throws IOException {
		String METHOD_NAME = "updateReconStatus";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		RawDataView tdv = getRawData(refId, requestType);
		if (tdv != null) {
			tdv.setReconStatus(status);
			tdv.setReconTs(new Timestamp(System.currentTimeMillis()));
			tdv.setReconDescription(description);
			try {
				rawDataDao.createOrUpdate(mapFrom(tdv));
			} 
			catch (IOException e) {
				log.error("Error " + e);
				throw new IOException(e);
			}
			finally {
				if (log.isDebugEnabled()){
					log.debug("Leaving "+METHOD_NAME);
				}
			}
		}
	}

	// To fetch the 'unread' set of raw_data and transaction_data joined together
	@Override
	public List<Object> getData() throws IOException {
		String METHOD_NAME = "getData";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		List<Object> refIds = null;
		try {
			refIds = rawDataDao.fetchData();
		} 
		catch (Exception e) {
			log.error("Error " + e);
			throw new IOException(e);
		}
		return refIds;
	}

	// for comparing the raw_data values coming from cu to that of the transaction_data in ou end
	@Override
	public Differences compare(RawDataView cuData, TransactionDataView t) throws IOException {
		String METHOD_NAME = "compare";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		Differences diff = null;
		try {
			if (cuData != null && t != null) {
				RawDataView ouData = mapFrom(mapFrom(t));
				// comparing the oudata to cudata and returning a list of differences
				diff = RawDataView.compare(ouData, cuData);
			}
		} 
		catch (Exception e) {
			log.error("Error " + e);
			throw new IOException(e);
		}
		return diff;
	}

	// for inserting into raw_data table
	@Override
	public void insert(RawDataView rawDataView) throws IOException {
		String METHOD_NAME = "insert";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		try {
			rawDataDao.create(mapFrom(rawDataView));
		} 
		catch (IOException e) {
			log.error("Error " + e);
			throw new IOException(e);
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}

	// Converting a TransactionDataView object to TransactionData object
	private TransactionData mapFrom(TransactionDataView tdv) throws IOException {
		String METHOD_NAME = "mapFrom";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		if (tdv == null) {
			return null;
		}
		objectMapper.disable(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

		TransactionData td = new TransactionData();
		TransactionDataPK tdPK = new TransactionDataPK();

		tdPK.setRefId(tdv.getRefId());
		tdPK.setTxnType(tdv.getRequestType().name());
		td.setId(tdPK);
		td.setMsgId(tdv.getMessageId());

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
		td.setReconStatus(tdv.getReconStatus() != null ? tdv.getReconStatus().name() : null);
		td.setReconCycleNo(tdv.getReconCycleNo());
		return td;
	}

	// Converting a RawData object to RawDataView object
	private RawDataView mapFrom(RawData rd) throws IOException {
		String METHOD_NAME = "mapFrom";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		if (rd == null) {
			return null;
		}
		RawDataView rdv = new RawDataView();
		rdv.setTxnType(RequestType.valueOf(rd.getId().getTxnType()));
		rdv.setMsgId(rd.getMsgId());
		rdv.setAgentId(rd.getAgentId());
		rdv.setBillerCategory(rd.getBillerCategory());
		rdv.setBillerId(rd.getBillerId());
		rdv.setBillerOuCountMonth(rd.getBillerOuCountMonth());
		rdv.setBillerOuId(rd.getBillerOuId());
		rdv.setOuType(rd.getOuType()!=null?OUType.valueOf(rd.getOuType()):null);
		//rdv.setBillerOuInterchangeFee(rd.getBillerOuInterchangeFee());
		rdv.setBillerOuSwitchingFee(rd.getBillerOuSwitchingFee());
		rdv.setCasProcessed(rd.getCasProcessed());
		rdv.setClearingTimestamp(rd.getClearingTimestamp());
		rdv.setCustomerConvenienceFee(rd.getCustomerConvenienceFee());
		rdv.setCustomerMobileNumber(rd.getCustomerMobileNumber());
		rdv.setCustomerOuCountMonth(rd.getCustomerOuCountMonth());
		rdv.setCustomerOuId(rd.getCustomerOuId());
		//rdv.setCustomerOuInterchangeFee(rd.getCustomerOuInterchangeFee());
		rdv.setCustomerOuSwitchingFee(rd.getCustomerOuSwitchingFee());
		rdv.setDecline(rd.getDecline());
		rdv.setMti(rd.getMti());
		rdv.setPaymentChannel(rd.getPaymentChannel());
		rdv.setPaymentMode(rd.getPaymentMode());
		rdv.setReconDescription(rd.getReconDescription());
		rdv.setReconStatus(ReconStatus.valueOf(rd.getReconStatus()));
		rdv.setReconTs(rd.getReconTs());
		rdv.setResponseCode(rd.getResponseCode());
		rdv.setReversal(rd.getReversal());
		rdv.setSettlementCycleId(rd.getSettlementCycleId());
		rdv.setSplitPay(rd.getSplitPay());
		rdv.setSplitPayTxnAmount(rd.getSplitPayTxnAmount());
		rdv.setSplitPaymentMode(rd.getSplitPaymentMode());
		rdv.setTxnAmount(rd.getTxnAmount());
		rdv.setTxnCurrencyCode(rd.getTxnCurrencyCode());
		rdv.setTxnDate(rd.getTxnDate());
		rdv.setTxnReferenceId(rd.getTxnReferenceId());
		rdv.setRefId(rd.getId().getRefId());
		rdv.setBillerFee(rd.getBillerFee());
		rdv.setCustomerConvenienceFeeTax(rd.getCustomerConvenienceFeeTax()!=null?new BigDecimal(rd.getCustomerConvenienceFeeTax()):BigDecimal.ZERO);
		rdv.setBillerFeeTax(rd.getBillerFeeTax());
		rdv.setCustomerOUSwitchingFeeTax(rd.getCustomerOUSwitchingFeeTax());
		rdv.setServiceFee(rd.getServiceFee());
		rdv.setServiceFeeDescription(rd.getServiceFeeDescription());
		rdv.setServiceFeeTax(rd.getServiceFeeTax());
		rdv.setBillerOUSwitchingFeeTax(rd.getBillerOuSwitchingFeeTax().longValue());
		return rdv;
	}

	// Converting a TransactionData object to RawDataView object
	private RawDataView mapFrom(TransactionData td) throws IOException {
		String METHOD_NAME = "mapFrom";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		if (td == null) {
			return null;
		}
		RawDataView rdv = new RawDataView();
		rdv.setTxnType(RequestType.valueOf(td.getId().getTxnType()));
		rdv.setMsgId(td.getMsgId());

		if (td.getId().getTxnType() == RequestType.FETCH.name()) {
			if (td.getRequestJson() != null) {
				BillFetchRequest billFetchRequest = objectMapper.readValue(
						td.getRequestJson(), BillFetchRequest.class);
				rdv.setAgentId(billFetchRequest.getAgent().getId());
				rdv.setBillerId(billFetchRequest.getBillDetails().getBiller()
						.getId());
				rdv.setCustomerMobileNumber(billFetchRequest.getCustomer()
						.getMobile());
				if (billFetchRequest.getTxn().getTs() != null) {
					DateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss");
					Date date = null;
					try {
						date = new Date(formatter.parse(
								billFetchRequest.getTxn().getTs()).getTime());
					} catch (ParseException e) {
						log.error( e.getMessage(), e);
			            log.info("In Excp : " + e.getMessage());
					}
					// rdv.setTxnDate(new
					// Date(Timestamp.valueOf(billFetchRequest.getTxn().getTs()).getTime()));
					rdv.setTxnDate(date);
				}
				rdv.setTxnCurrencyCode(billFetchRequest.getTxn().getXchangeId());
			}
			if (td.getResponseJson() != null) {
				BillFetchResponse billFetchResponse = objectMapper.readValue(
						td.getResponseJson(), BillFetchResponse.class);
				/*rdv.setCustomerConvenienceFee(new BigDecimal(billFetchResponse
						.getBillerResponse().getCustConvFee()));*/
				rdv.setTxnAmount(new BigDecimal(billFetchResponse
						.getBillerResponse().getAmount()));
				rdv.setResponseCode(billFetchResponse.getReason()
						.getResponseCode());

			}
		} else if (td.getId().getTxnType() == RequestType.PAYMENT.name()) {
			if (td.getRequestJson() != null) {
				BillPaymentRequest billPaymentRequest = objectMapper.readValue(
						td.getRequestJson(), BillPaymentRequest.class);
				rdv.setAgentId(billPaymentRequest.getAgent().getId());
				rdv.setBillerId(billPaymentRequest.getBillDetails().getBiller()
						.getId());
				rdv.setCustomerMobileNumber(billPaymentRequest.getCustomer()
						.getMobile());
				DateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				Date date = null;
				try {
					date = new Date(formatter.parse(
							billPaymentRequest.getTxn().getTs()).getTime());
				} catch (ParseException e) {
					log.error( e.getMessage(), e);
		            log.info("In Excp : " + e.getMessage());
				}
				// rdv.setTxnDate(new
				// Date(Timestamp.valueOf(billFetchRequest.getTxn().getTs()).getTime()));
				rdv.setTxnDate(date);
				rdv.setTxnCurrencyCode(billPaymentRequest.getTxn()
						.getXchangeId());

			}
			if (td.getResponseJson() != null) {
				BillPaymentResponse billPaymentResponse = objectMapper
						.readValue(td.getResponseJson(),
								BillPaymentResponse.class);
				rdv.setCustomerConvenienceFee(new BigDecimal(
						billPaymentResponse.getBillerResponse()
								.getCustConvFee()));
				rdv.setTxnAmount(new BigDecimal(billPaymentResponse
						.getBillerResponse().getAmount()));
				rdv.setResponseCode(billPaymentResponse.getReason()
						.getResponseCode());

			}
		}
		/*
		 * could not find the mapping in transaction_data for these fields in raw_data
		 */
		// rdv.setBillerCategory(rd.getBillerCategory());
		// rdv.setBillerOuCountMonth(rd.getBillerOuCountMonth());
		// rdv.setBillerOuId(rd.getBillerOuId());
		// rdv.setBillerOuInterchangeFee(rd.getBillerOuInterchangeFee());
		// rdv.setBillerOuSwitchingFee(rd.getBillerOuSwitchingFee());
		// rdv.setCasProcessed(rd.getCasProcessed());
		// rdv.setClearingTimestamp(rd.getClearingTimestamp());
		// rdv.setCustomerOuCountMonth(rd.getCustomerOuCountMonth());
		// rdv.setCustomerOuId(rd.getCustomerOuId());
		// rdv.setCustomerOuInterchangeFee(rd.getCustomerOuInterchangeFee());
		// rdv.setCustomerOuSwitchingFee(rd.getCustomerOuSwitchingFee());
		// rdv.setDecline(rd.getDecline());
		// rdv.setMti(rd.getMti());
		// rdv.setPaymentChannel(rd.getPaymentChannel());
		// rdv.setPaymentMode(rd.getPaymentMode());
		// rdv.setReversal(rd.getReversal());
		// rdv.setSettlementCycleId(rd.getSettlementCycleId());
		// rdv.setSplitPay(rd.getSplitPay());
		// rdv.setSplitPayTxnAmount(rd.getSplitPayTxnAmount());
		// rdv.setSplitPaymentMode(rd.getSplitPaymentMode());
		rdv.setTxnReferenceId(td.getTxnRefId());
		rdv.setRefId(td.getId().getRefId());
		return rdv;
	}

	// converting RawDataView to RawData
	private RawData mapFrom(RawDataView rd) throws IOException {
		String METHOD_NAME = "mapFrom";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		if (rd == null) {
			return null;
		}
		RawData rdv = new RawData();
		RawDataPK rdPK = new RawDataPK();
		rdPK.setRefId(rd.getRefId());
		rdPK.setTxnType(rd.getTxnType().name());
		rdv.setId(rdPK);
		rdv.setMsgId(rd.getMsgId());
		rdv.setAgentId(rd.getAgentId());
		rdv.setBillerCategory(rd.getBillerCategory());
		rdv.setBillerId(rd.getBillerId());
		rdv.setBillerOuCountMonth(rd.getBillerOuCountMonth());
		rdv.setBillerOuId(rd.getBillerOuId());
		//rdv.setBillerOuInterchangeFee(rd.getBillerOuInterchangeFee());
		rdv.setBillerOuSwitchingFee(rd.getBillerOuSwitchingFee());
		rdv.setCasProcessed(rd.getCasProcessed());
		rdv.setClearingTimestamp(rd.getClearingTimestamp());
		rdv.setCustomerConvenienceFee(rd.getCustomerConvenienceFee());
		rdv.setCustomerMobileNumber(rd.getCustomerMobileNumber());
		rdv.setCustomerOuCountMonth(rd.getCustomerOuCountMonth());
		rdv.setCustomerOuId(rd.getCustomerOuId());
		//rdv.setCustomerOuInterchangeFee(rd.getCustomerOuInterchangeFee());
		rdv.setCustomerOuSwitchingFee(rd.getCustomerOuSwitchingFee());
		rdv.setDecline(rd.getDecline());
		rdv.setMti(rd.getMti());
		rdv.setPaymentChannel(rd.getPaymentChannel());
		rdv.setPaymentMode(rd.getPaymentMode());
		rdv.setReconDescription(rd.getReconDescription());
		rdv.setReconStatus(rd.getReconStatus() != null ? rd.getReconStatus().name() : null);
		rdv.setReconTs(rd.getReconTs());
		rdv.setResponseCode(rd.getResponseCode());
		rdv.setReversal(rd.getReversal());
		rdv.setSettlementCycleId(rd.getSettlementCycleId());
		rdv.setSplitPay(rd.getSplitPay());
		rdv.setSplitPayTxnAmount(rd.getSplitPayTxnAmount());
		rdv.setSplitPaymentMode(rd.getSplitPaymentMode());
		rdv.setTxnAmount(rd.getTxnAmount());
		rdv.setTxnCurrencyCode(rd.getTxnCurrencyCode());
		rdv.setTxnDate(rd.getTxnDate());
		rdv.setTxnReferenceId(rd.getTxnReferenceId());
		rdv.setBillerFee(rd.getBillerFee());
		rdv.setCustomerConvenienceFeeTax(rd.getCustomerConvenienceFeeTax()!=null?rd.getCustomerConvenienceFeeTax().longValue():0L);
		rdv.setBillerFeeTax(rd.getBillerFeeTax());
		rdv.setCustomerOUSwitchingFeeTax(rd.getCustomerOUSwitchingFeeTax());
		rdv.setServiceFee(rd.getServiceFee());
		rdv.setServiceFeeDescription(rd.getServiceFeeDescription());
		rdv.setServiceFeeTax(rd.getServiceFeeTax());
		rdv.setBillerOuSwitchingFeeTax(rd.getBillerOUSwitchingFeeTax()!=null?new BigDecimal(rd.getBillerOUSwitchingFeeTax()):BigDecimal.ZERO);
		rdv.setOuType(rd.getOuType()!=null?rd.getOuType().name():null);
		return rdv;
	}
	
	/*
	 * @Override public List<RawDataView> createPdf(String settlementId) {
	 * List<RawDataView> list = null; try { list =
	 * mapFrom(rawDataDao.getFromSettleId(settlementId)); } catch (IOException
	 * e) { e.printStackTrace(); } return list; }
	 */
}