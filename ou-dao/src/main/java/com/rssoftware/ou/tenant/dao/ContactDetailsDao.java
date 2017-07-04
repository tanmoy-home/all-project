package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ContactDetails;

import in.co.rssoftware.bbps.schema.EntityStatus;

public interface ContactDetailsDao extends GenericDao<ContactDetails, String>{

	List<ContactDetails> fetchAll(String linkedEntityID, String linkedEntityType, String contactType,
			String entityStatus);

	

	
	

}
