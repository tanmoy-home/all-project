package com.rssoftware.ou.batch.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.BOUBlrRecon;

public class BOUBlrReconRowMapper implements RowMapper<BOUBlrRecon>{

	@Override
	public BOUBlrRecon mapRow(ResultSet rs, int rowNum) throws SQLException {
		BOUBlrRecon bouBlrRecon = new BOUBlrRecon();
		bouBlrRecon.setTxnRefId(rs.getString("TXN_REF_ID"));
		bouBlrRecon.setMobile(rs.getString("MOBILE"));
		bouBlrRecon.setBlrRequestJson(rs.getBytes("BLR_REQUEST_JSON"));
		bouBlrRecon.setBlrResponseJson(rs.getBytes("BLR_RESPONSE_JSON"));
		bouBlrRecon.setBlrStatus(rs.getString("BLR_STATUS"));
		bouBlrRecon.setBlrRevStatus(rs.getString("BLR_REV_STATUS"));
		bouBlrRecon.setTxnDate(rs.getString("TXN_DATE"));
		bouBlrRecon.setBillAmount(rs.getString("BILL_AMOUNT"));
		return bouBlrRecon;
	}

}