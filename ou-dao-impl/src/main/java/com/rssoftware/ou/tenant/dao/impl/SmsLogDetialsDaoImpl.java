package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SmsLogDetials;
import com.rssoftware.ou.tenant.dao.SmsLogDetialsDao;
@Repository
public class SmsLogDetialsDaoImpl extends GenericDynamicDaoImpl<SmsLogDetials, String> implements SmsLogDetialsDao {

	public SmsLogDetialsDaoImpl() {
		super(SmsLogDetials.class);
	}

}
