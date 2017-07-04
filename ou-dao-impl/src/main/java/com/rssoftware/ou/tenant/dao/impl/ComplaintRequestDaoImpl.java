package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ComplaintRequest;
import com.rssoftware.ou.tenant.dao.ComplaintRequestDao;

@Repository
public class ComplaintRequestDaoImpl extends GenericDynamicDaoImpl<ComplaintRequest, String> implements ComplaintRequestDao{

	private static final String REQUEST_BY_REFID = "select t from ComplaintRequest t where t.refId = :refId";
	
	private static final String COMPLAINTLIST_BY_MOBILE = "select t from ComplaintRequest t where t.mobile = :mobile";
	
	public ComplaintRequestDaoImpl() {
		super(ComplaintRequest.class);
		
	}
	
	@Override
	public ComplaintRequest getRequestByRefId(String refId) {
		ComplaintRequest req=(ComplaintRequest) getSessionFactory().getCurrentSession().createQuery(REQUEST_BY_REFID).setString("refId", refId).uniqueResult();
		return req;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComplaintRequest> getComplaintListByMobile(String mobile) {
		List<ComplaintRequest> reqList=(List<ComplaintRequest>) getSessionFactory().getCurrentSession().createQuery(COMPLAINTLIST_BY_MOBILE).setString("mobile", mobile).list();
		return reqList;
	}

}
