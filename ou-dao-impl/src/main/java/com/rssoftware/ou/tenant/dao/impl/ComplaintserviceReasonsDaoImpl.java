package com.rssoftware.ou.tenant.dao.impl;

import in.co.rssoftware.bbps.schema.ParticipationType;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ComplaintResponse;
import com.rssoftware.ou.database.entity.tenant.ComplaintserviceReasons;
import com.rssoftware.ou.tenant.dao.ComplaintserviceReasonsDao;
@Repository
public class ComplaintserviceReasonsDaoImpl extends GenericDynamicDaoImpl<ComplaintserviceReasons, String> implements ComplaintserviceReasonsDao{

	private static final String SERVICEREASON_BY_TYPE = "select c from ComplaintserviceReasons c where c.reasonType = :reasonType";
	
	public ComplaintserviceReasonsDaoImpl() {
		super(ComplaintserviceReasons.class);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<ComplaintserviceReasons> getServiceReasonsByType(String reasonType) {
		List<ComplaintserviceReasons> crsList=getSessionFactory().getCurrentSession().createQuery(SERVICEREASON_BY_TYPE).setString("reasonType", reasonType).list();
		return crsList;
	}
}
