package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;

public interface ComplaintRequestDao extends GenericDao<ComplaintRequest, String>{

	public ComplaintRequest getRequestByRefId(String refId);

	public List<ComplaintRequest> getComplaintListByMobile(String mobile);

}
