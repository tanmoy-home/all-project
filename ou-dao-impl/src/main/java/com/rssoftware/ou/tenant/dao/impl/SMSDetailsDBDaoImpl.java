package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SMSDetails;
import com.rssoftware.ou.tenant.dao.SMSDetailsDBDao;

@Repository
public class SMSDetailsDBDaoImpl extends GenericDynamicDaoImpl<SMSDetails, String> implements SMSDetailsDBDao {

	public SMSDetailsDBDaoImpl(Class<SMSDetails> type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	public SMSDetailsDBDaoImpl() {
		super(SMSDetails.class);
	}
}


