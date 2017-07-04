package com.rssoftware.ou.batch.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.ReconBOU;
public class ReconBOURowMapper implements RowMapper<ReconBOU>{

	@Override
	public ReconBOU mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReconBOU recon = new ReconBOU();
		recon.setRawRefId(rs.getString("RAW_REF_ID"));
		recon.setRawTxnType(rs.getString("RAW_TXN_TYPE"));
		recon.setRawMsgId(rs.getString("RAW_MSG_ID"));
		recon.setMti(rs.getString("MTI"));
		recon.setTxnReferenceId(rs.getString("TXN_REFERENCE_ID"));
		recon.setTxnDate(rs.getDate("TXN_DATE"));
		recon.setCustomerOUId(rs.getString("CUSTOMER_OU_ID"));
		recon.setBillerOUId(rs.getString("BILLER_OU_ID"));
		recon.setAgentId(rs.getString("AGENT_ID"));
		recon.setResponseCode(rs.getString("RESPONSE_CODE"));
		recon.setTxnCurrencyCode(rs.getString("TXN_CURRENCY_CODE"));
		recon.setTxnAmount(rs.getBigDecimal("TXN_AMOUNT"));
		recon.setCustomerOUInterchangeFee(rs.getBigDecimal("CUSTOMER_OU_INTERCHANGE_FEE"));
		recon.setCustomerOUSwitchingFee(rs.getBigDecimal("CUSTOMER_OU_SWITCHING_FEE"));
		recon.setBillerOUInterchangeFee(rs.getBigDecimal("BILLER_OU_INTERCHANGE_FEE"));
		recon.setBillerOUSwitchingFee(rs.getBigDecimal("BILLER_OU_SWITCHING_FEE"));
		recon.setCustomerConvenienceFee(rs.getBigDecimal("CUSTOMER_CONVENIENCE_FEE"));
		recon.setPaymentChannel(rs.getString("PAYMENT_CHANNEL"));
		recon.setPaymentMode(rs.getString("PAYMENT_MODE"));
		recon.setCustomerOUCountMonth(rs.getBigDecimal("CUSTOMER_OU_COUNT_MONTH"));
		recon.setBillerOUCountMonth(rs.getBigDecimal("BILLER_OU_COUNT_MONTH"));
		recon.setBillerId(rs.getString("BILLER_ID"));
		recon.setBillerCategory(rs.getString("BILLER_CATEGORY"));
		recon.setSplitPay(rs.getBoolean("SPLIT_PAY"));
		recon.setSplitPaymentMode(rs.getString("SPLIT_PAYMENT_MODE"));
		recon.setSplitPayTxnAmount(rs.getBigDecimal("SPLIT_PAY_TXN_AMOUNT"));
		recon.setCustomerMobileNUmber(rs.getString("CUSTOMER_MOBILE_NUMBER"));
		recon.setReversal(rs.getBoolean("REVERSAL"));
		recon.setDecline(rs.getBoolean("DECLINE"));
		recon.setCasProcessed(rs.getBoolean("CAS_PROCESSED"));
		recon.setSettlementCycleId(rs.getString("SETTLEMENT_CYCLE_ID"));
		recon.setRawReconStatus(rs.getString("RAW_RECON_STATUS"));
		recon.setBillerFee(rs.getString("BILLER_FEE"));
		recon.setRefId(rs.getString("REF_ID"));
		recon.setTxnType(rs.getString("TXN_TYPE"));
		recon.setMsgId(rs.getString("MSG_ID"));
		recon.setTxnRefId(rs.getString("TXN_REF_ID"));
		recon.setCuRequestJson(rs.getBytes("CU_REQUEST_JSON"));
		recon.setCuResponseJson(rs.getBytes("CU_RESPONSE_JSON"));
		recon.setBlrRequestJson(rs.getBytes("BLR_REQUEST_JSON"));
		recon.setBlrResponseJson(rs.getBytes("BLR_RESPONSE_JSON"));
		recon.setCuStatus(rs.getString("CU_STATUS"));
		recon.setBlrStatus(rs.getString("BLR_STATUS"));
		recon.setCuRevStatus(rs.getString("CU_REV_STATUS"));
		recon.setBlrRevStatus(rs.getString("BLR_REV_STATUS"));
		recon.setReconStatus(rs.getString("RECON_STATUS"));
		recon.setReconCycleNo(rs.getBigDecimal("RECON_CYCLE_NO"));
		return recon;
	}

}