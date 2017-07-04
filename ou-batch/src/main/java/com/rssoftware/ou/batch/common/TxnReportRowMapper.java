package com.rssoftware.ou.batch.common;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.TxnReport;

public class TxnReportRowMapper implements RowMapper<TxnReport> {

	@Override
	public TxnReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		TxnReport txnReport = new TxnReport();
		txnReport.setNetBankingPaymentCount(rs.getBigDecimal("txnCount"));
		txnReport.setNoOfAgentOutlets(new BigDecimal(1));
		txnReport.setOffUsTxnCount(rs.getBigDecimal("txnCount"));
		txnReport.setOffUsTxnTot(rs.getBigDecimal("txnTot"));
		txnReport.setOffUsFailedTxnCount(rs.getBigDecimal("failedTxnCount"));
		txnReport.setOffUsFailedTxnTot(rs.getBigDecimal("failedTxnTot"));
		return txnReport;
	}

}