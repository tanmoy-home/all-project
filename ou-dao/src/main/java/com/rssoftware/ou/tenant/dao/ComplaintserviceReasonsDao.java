package com.rssoftware.ou.tenant.dao;

import in.co.rssoftware.bbps.schema.ParticipationType;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ComplaintserviceReasons;

public interface ComplaintserviceReasonsDao extends GenericDao<ComplaintserviceReasons, String>{

	public List<ComplaintserviceReasons> getServiceReasonsByType(String type);

}
