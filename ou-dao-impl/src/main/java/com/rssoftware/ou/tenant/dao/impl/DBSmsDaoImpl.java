package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.DBSms;
import com.rssoftware.ou.tenant.dao.DBSmsDao;
@Repository
public class DBSmsDaoImpl extends GenericDynamicDaoImpl<DBSms, String> implements DBSmsDao {

	public DBSmsDaoImpl() {
		super(DBSms.class);
	}
}
