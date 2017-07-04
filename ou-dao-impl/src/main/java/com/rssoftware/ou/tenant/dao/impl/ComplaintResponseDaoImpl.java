package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.tenant.dao.ComplaintResponseDao;

@Repository
public class ComplaintResponseDaoImpl extends GenericDynamicDaoImpl<ComplaintResponse, String> implements ComplaintResponseDao{

	
	private static final String RESPONSE_BY_REFID = "select t from ComplaintResponse t where t.refId = :refId";
	
	public ComplaintResponseDaoImpl() {
		super(ComplaintResponse.class);
	}
	
	@Override
	public ComplaintResponse getResponseByRefId(String refId) {
		ComplaintResponse complaintResponse=(ComplaintResponse) getSessionFactory().getCurrentSession().createQuery(RESPONSE_BY_REFID).setString("refId", refId).uniqueResult();
		return complaintResponse;
	}
	
	
}
