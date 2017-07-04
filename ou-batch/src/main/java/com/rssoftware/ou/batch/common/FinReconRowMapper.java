package com.rssoftware.ou.batch.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.FinRecon;

public class FinReconRowMapper implements RowMapper<FinRecon>{

	@Override
	public FinRecon mapRow(ResultSet rs, int rowNum) throws SQLException {
		FinRecon finRecon = new FinRecon();
		finRecon.setTxnRefId(rs.getString("TXN_REF_ID"));
		finRecon.setAuthCode(rs.getString("AUTH_CODE"));
		finRecon.setCrtnTs(rs.getTimestamp("CRTN_TS"));
		finRecon.setFinCurrentStatus(rs.getString("CURRENT_STATUS"));
		finRecon.setRequestJson(rs.getBytes("REQUEST_JSON"));
		finRecon.setTxnCurrentStatus(rs.getString("TXN_STATUS"));
		finRecon.setUpdtTs(rs.getTimestamp("UPDT_TS"));
		finRecon.setTotalAmount(rs.getBigDecimal("TOTAL_AMOUNT"));
		return finRecon;
	}

}