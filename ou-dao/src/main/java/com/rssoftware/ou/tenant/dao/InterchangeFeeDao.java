package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.InterchangeFee;

public interface InterchangeFeeDao extends GenericDao<InterchangeFee, String> {
	public List<InterchangeFee> fetchAllInterchangeFeeByBillerId(String blrId);
	public int deleteAllInterchangeFeeByBillerId(String blrId);
	public InterchangeFee getbyBilleridFeeCode(String billerId, String feeCode);
}
