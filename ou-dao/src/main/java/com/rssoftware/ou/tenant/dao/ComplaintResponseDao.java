package com.rssoftware.ou.tenant.dao;



import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;

public interface ComplaintResponseDao extends GenericDao<ComplaintResponse, String>{

	public ComplaintResponse getResponseByRefId(String refId);

}
