package com.rssoftware.ou.tenant.dao.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.database.entity.tenant.RawData;
import com.rssoftware.ou.database.entity.tenant.RawDataPK;
import com.rssoftware.ou.iso8583.util.impl.IsoMsgException;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.RawDataView.ReconStatus;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.tenant.dao.RawDataDao;

@Repository
public class RawDataDaoImpl extends GenericDynamicDaoImpl<RawData, RawDataPK>
		implements RawDataDao {
	private final static Logger logger = LoggerFactory.getLogger(RawDataDaoImpl.class);


	private static ObjectMapper objectMapper = new ObjectMapper();
	private static final String GET_DATA = "select r.REF_ID AS RAW_REF_ID,r.TXN_TYPE AS RAW_TXN_TYPE,r.MSG_ID AS RAW_MSG_ID,r.MTI,r.TXN_REFERENCE_ID,r.TXN_DATE,r.CUSTOMER_OU_ID,r.BILLER_OU_ID,r.AGENT_ID AS RAW_AGENT_ID,r.RESPONSE_CODE,r.TXN_CURRENCY_CODE,r.TXN_AMOUNT,r.CUSTOMER_OU_INTERCHANGE_FEE,r.CUSTOMER_OU_SWITCHING_FEE,r.BILLER_OU_INTERCHANGE_FEE,r.BILLER_OU_SWITCHING_FEE,r.CUSTOMER_CONVENIENCE_FEE,r.CLEARING_TIMESTAMP,r.PAYMENT_CHANNEL,r.PAYMENT_MODE,r.CUSTOMER_OU_COUNT_MONTH,r.BILLER_OU_COUNT_MONTH,r.BILLER_ID,r.BILLER_CATEGORY,r.SPLIT_PAY,r.SPLIT_PAYMENT_MODE,r.SPLIT_PAY_TXN_AMOUNT,r.CUSTOMER_MOBILE_NUMBER,r.REVERSAL,r.DECLINE,r.CAS_PROCESSED,r.SETTLEMENT_CYCLE_ID,r.RECON_TS AS RAW_RECON_TS,r.RECON_STATUS AS RAW_RECON_STATUS,r.RECON_DESCRIPTION AS RAW_RECON_DESCRIPTION, t.REF_ID,t.TXN_TYPE,t.MSG_ID,t.TXN_REF_ID,t.REQUEST_JSON,t.RESPONSE_JSON,t.CRTN_TS,t.UPDT_TS,t.CURRENT_STATUS,t.RECON_TS,t.RECON_STATUS,t.RECON_DESCRIPTION,RECON_CYCLE_NO,r.biller_fee AS RAW_BILLER_FEE,r.customer_convenience_fee_tax AS RAW_CUSTOMER_CONVENIENCE_FEE_TAX,r.biller_fee_tax AS RAW_BILLER_FEE_TAX,r.biller_ou_switching_fee_tax AS RAW_BILLER_OU_SWITCHING_FEE_TAX,r.customer_ou_switching_fee_tax AS RAW_CUSTOMER_OU_SWITCHING_FEE_TAX,t.agent_id,t.blr_id,t.bill_amount from TRANSACTION_DATA t FULL OUTER JOIN Raw_Data r ON t.ref_Id=r.ref_Id AND t.txn_Type=r.txn_Type";

	public RawDataDaoImpl() {
		super(RawData.class);
	}

	//fetch full outer joined data from raw_data+transaction_data
	@Override
	public List<Object> fetchData() throws IsoMsgException {
		ScrollableResults results = getSessionFactory().getCurrentSession()
				.createSQLQuery(GET_DATA).setFetchSize(10)
				.scroll(ScrollMode.FORWARD_ONLY);
		getSessionFactory().getCurrentSession().setCacheMode(CacheMode.IGNORE);
		List<Object> resultList = new LinkedList<Object>();
		RawDataView rawDataView = null;
		TransactionDataView transactionDataView = null;
		while (results.next()) {
			rawDataView = (RawDataView) formRawData(results);
			try {
				transactionDataView = (TransactionDataView) formTransactionData(results);
			} catch (JsonParseException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			} catch (JsonMappingException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			} catch (IOException e) {
				logger.error( e.getMessage(), e);
		        logger.info("In Excp : " + e.getMessage());
			}
			if (rawDataView.getReconStatus() == ReconStatus.UNREAD
					|| transactionDataView.getReconStatus() == com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.UNREAD
					|| transactionDataView.getReconStatus() == com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus.PENDING) {
				resultList.add(rawDataView);
				resultList.add(transactionDataView);
			}
			getSessionFactory().getCurrentSession().clear();
		}
		results.close();
		return resultList;
	}

	//form RawDataView from the scrollable result
	public RawDataView formRawData(ScrollableResults results) {
		if (results == null) {
			return null;
		}
		RawDataView rdv = new RawDataView();
		rdv.setRefId((String) results.get(0));
		if (results.get(1) != null)
			rdv.setTxnType(RequestType.valueOf((String) results.get(1)));
		rdv.setMsgId((String) results.get(2));
		rdv.setMti((String) results.get(3));
		rdv.setTxnReferenceId((String) results.get(4));
		/*Timestamp timestamp = (Timestamp) results.get(5);
		if (timestamp != null)
			rdv.setTxnDate(new Date(timestamp.getTime()));*/
		rdv.setCustomerOuId((String) results.get(6));
		rdv.setBillerOuId((String) results.get(7));
		rdv.setAgentId((String) results.get(8));
		rdv.setResponseCode((String) results.get(9));
		rdv.setTxnCurrencyCode((String) results.get(10));
		rdv.setTxnAmount((BigDecimal) results.get(11));
		//rdv.setCustomerOuInterchangeFee((BigDecimal) results.get(12));
		rdv.setCustomerOuSwitchingFee((BigDecimal) results.get(13));
		//rdv.setBillerOuInterchangeFee((BigDecimal) results.get(14));
		rdv.setBillerOuSwitchingFee((BigDecimal) results.get(15));
		rdv.setCustomerConvenienceFee((BigDecimal) results.get(16));
		rdv.setClearingTimestamp((Timestamp) results.get(17));
		rdv.setPaymentChannel((String) results.get(18));
		rdv.setPaymentMode((String) results.get(19));
		rdv.setCustomerOuCountMonth((BigDecimal) results.get(20));
		rdv.setBillerOuCountMonth((BigDecimal) results.get(21));
		rdv.setBillerId((String) results.get(22));
		rdv.setBillerCategory((String) results.get(23));
		rdv.setSplitPay((Boolean) results.get(24));
		rdv.setSplitPaymentMode((String) results.get(25));
		rdv.setSplitPayTxnAmount((BigDecimal) results.get(26));
		rdv.setCustomerMobileNumber(results.get(27)==null ? "" : (String) results.get(27));
		rdv.setReversal((Boolean) results.get(28));
		rdv.setDecline((Boolean) results.get(29));
		rdv.setCasProcessed((Boolean) results.get(30));
		rdv.setSettlementCycleId((String) results.get(31));
		rdv.setReconTs((Timestamp) results.get(32));
		if (results.get(33) != null)
			rdv.setReconStatus(ReconStatus.valueOf((String) results.get(33)));
		rdv.setReconDescription((String) results.get(34));
		rdv.setBillerFee((BigDecimal) results.get(48));
		rdv.setBillerFeeTax((Long) results.get(49));
		rdv.setCustomerConvenienceFeeTax((BigDecimal) results.get(50));
		rdv.setBillerOUSwitchingFeeTax((Long) results.get(51));
		rdv.setCustomerOUSwitchingFeeTax((Long) results.get(52));
		
		return rdv;
	}

	
	//form TransactionDataView from the scrollable result
	public TransactionDataView formTransactionData(ScrollableResults results)
			throws JsonParseException, JsonMappingException, IOException {
		if (results == null) {
			return null;
		}
		TransactionDataView tdv = new TransactionDataView();
		tdv.setRefId((String) results.get(35));
		if (results.get(36) != null)
			tdv.setRequestType(RequestType.valueOf((String) results.get(36)));
		tdv.setMessageId((String) results.get(37));
		tdv.setTxnRefId((String) results.get(38));
		
		if(results.get(39) != null)
			System.out.println("results.get(39) " + new String(getBytes(results.get(39))));
		
		if(results.get(40) != null)
			System.out.println("results.get(40) " + new String(getBytes(results.get(40))));

			
		
		byte[] reqBlobAsBytes = results.get(39) == null ? null : getBytes(results.get(39));
		byte[] respBlobAsBytes = results.get(40) == null ? null : getBytes(results.get(40));
		
		/*Blob requestblob = (Blob) results.get(39);		
		byte[] reqBlobAsBytes = null;
		int blobLength = 0;
		if (requestblob != null) {
			try {
				blobLength = (int) requestblob.length();
				reqBlobAsBytes = requestblob.getBytes(1, blobLength);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Blob responseblob = (Blob) results.get(40);
		byte[] respBlobAsBytes = null;
		if (responseblob != null) {
			try {
				blobLength = (int) responseblob.length();
				respBlobAsBytes = responseblob.getBytes(1, blobLength);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}*/

		/*if (tdv.getRequestType() == RequestType.FETCH) {
			if (reqBlobAsBytes != null) {
				tdv.setBillFetchRequest(objectMapper.readValue(reqBlobAsBytes,
						BillFetchRequest.class));
			}
			if (respBlobAsBytes != null) {
				tdv.setBillFetchResponse(objectMapper.readValue(
						respBlobAsBytes, BillFetchResponse.class));
			}
		} else if (tdv.getRequestType() == RequestType.PAYMENT) {
			if (reqBlobAsBytes != null) {
				tdv.setBillPaymentRequest(objectMapper.readValue(
						reqBlobAsBytes, BillPaymentRequest.class));
			}
			if (respBlobAsBytes != null) {
				tdv.setBillPaymentResponse(objectMapper.readValue(
						respBlobAsBytes, BillPaymentResponse.class));
			}
		}*/		
		tdv.setCreationTimestamp((Timestamp) results.get(41));
		tdv.setLastUpdateTimestamp((Timestamp) results.get(42));
		if (results.get(43) != null)
			tdv.setStatus(RequestStatus.valueOf((String) results.get(43)));

		tdv.setReconTs((Timestamp) results.get(44));
		if (results.get(45) != null)
			tdv.setReconStatus(com.rssoftware.ou.model.tenant.TransactionDataView.ReconStatus
					.valueOf((String) results.get(45)));
		tdv.setReconDescription((String) results.get(46));
		tdv.setReconCycleNo((BigDecimal) results.get(47));
		tdv.setAgentID((String) results.get(53));
		tdv.setBillerID((String) results.get(54));
		tdv.setBillAmount((BigDecimal) results.get(55));
		return tdv;
	}
	
	public static byte[] getBytes(Object obj) throws java.io.IOException{
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      ObjectOutputStream oos = new ObjectOutputStream(bos);
	      oos.writeObject(obj);
	      oos.flush();
	      oos.close();
	      bos.close();
	      byte [] data = bos.toByteArray();
	      return data;
	  }
	
}
