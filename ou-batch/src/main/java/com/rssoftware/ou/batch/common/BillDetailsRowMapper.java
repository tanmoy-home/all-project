package com.rssoftware.ou.batch.common;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rssoftware.ou.batch.to.BillDetails;

public class BillDetailsRowMapper implements RowMapper<BillDetails>{

	@Override
	public BillDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		BillDetails billDetails = new BillDetails();
		billDetails.setBillerId(rs.getString("biller_id"));
		billDetails.setCustomerParam1(rs.getString("customer_param1"));
		billDetails.setCustomerParam2(rs.getString("customer_param2"));
		billDetails.setCustomerParam3(rs.getString("customer_param3"));
		billDetails.setCustomerParam4(rs.getString("customer_param4"));
		billDetails.setCustomerParam5(rs.getString("customer_param5"));
		billDetails.setCustomerName(rs.getString("customer_name"));
		billDetails.setActualAmount(rs.getBigDecimal("actual_amount"));
		billDetails.setDueDate(rs.getString("due_date"));
		billDetails.setBillDate(rs.getString("bill_date"));
		billDetails.setBillNumber(rs.getString("bill_number"));
		billDetails.setBillPeriod(rs.getString("bill_period"));
		billDetails.setConcatAA(rs.getString("additional_amounts"));
		billDetails.setConcatAI(rs.getString("additional_info"));
		return billDetails;
	}

}
