package com.rssoftware.ou.batch.common;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.model.tenant.BillPaymentReportDataView;
import com.rssoftware.ou.model.tenant.RawDataView;
import com.rssoftware.ou.model.tenant.TransactionDataView;

public class BillPaymentReportRowMapper implements RowMapper {

	private final static Logger log = LoggerFactory.getLogger(BillPaymentReportRowMapper.class);

	
	@Override
	public BillPaymentReportDataView mapRow(ResultSet rs, int rowNum) throws SQLException {
		BillPaymentReportDataView billPaymentReportDataView = new BillPaymentReportDataView();		
		try {
			
			billPaymentReportDataView.setTransactionDataView(mapTransactionData(rs));
			billPaymentReportDataView.setRawDataView(mapRawData(rs));
			
		} catch (IOException e) {
			 log.error( e.getMessage(), e);
	         log.info("In Excp : " + e.getMessage());
		}		
		return billPaymentReportDataView;
	}
	
	// t.TXN_REF_ID,t.TXN_TYPE,t.AGENT_ID,t.BILL_DATE,t.BILL_AMOUNT
	//form TransactionDataView from the scrollable result
	public TransactionDataView mapTransactionData(ResultSet results)
			throws SQLException, IOException {
		if (results == null) {
			return null;
		}
		TransactionDataView tdv = new TransactionDataView();
		tdv.setTxnRefId(results.getString(1));
		if (results.getString(2) != null)
			tdv.setTransactionType(results.getString(2));
		tdv.setAgentID(results.getString(3));
		tdv.setBillDate(results.getString(4));
		tdv.setBillAmount(results.getBigDecimal(5));
		return tdv;
	}
	
	// r.TXN_DATE,r.CUSTOMER_OU_ID,r.BILLER_OU_ID,r.BILLER_CATEGORY,r.SPLIT_PAYMENT_MODE,r.CUSTOMER_MOBILE_NUMBER,r.BILLER_FEE,r.BILLER_FEE_TAX,r.REVERSAL,
	// r.DECLINE,r.SPLIT_PAY
	//form RawDataView from the scrollable result
	public RawDataView mapRawData(ResultSet results) throws SQLException, IOException {
		if (results == null) {
			return null;
		}
		RawDataView rdv = new RawDataView();
		Date txnDate = results.getDate(6);
		if (txnDate != null)
			rdv.setTxnDate(txnDate);
		rdv.setCustomerOuId(results.getString(7));
		rdv.setBillerOuId(results.getString(8));
		rdv.setBillerCategory(results.getString(9));
		rdv.setSplitPaymentMode(results.getString(10));
		rdv.setCustomerMobileNumber(results.getString(11)==null ? "" : results.getString(11));
		rdv.setBillerFee(results.getBigDecimal(12));
		rdv.setBillerFeeTax(results.getLong(13));
		rdv.setReversal(results.getBoolean(14));
		rdv.setDecline(results.getBoolean(15));
		rdv.setSplitPay(results.getBoolean(16));		
		return rdv;
	}

}
