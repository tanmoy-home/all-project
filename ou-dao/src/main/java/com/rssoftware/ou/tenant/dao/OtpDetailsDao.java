package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.OtpDetails;

public interface OtpDetailsDao extends GenericDao<OtpDetails,Long>  {
	
	public List<OtpDetails> fetchAllActiveRow (String custMobileNo, String otp_status);

}
