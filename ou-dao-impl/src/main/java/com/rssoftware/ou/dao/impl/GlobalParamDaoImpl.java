package com.rssoftware.ou.dao.impl;

import org.hibernate.SessionFactory;

import com.rssoftware.framework.hibernate.dao.impl.GenericDaoImpl;
import com.rssoftware.ou.dao.GlobalParamDao;
import com.rssoftware.ou.database.entity.global.GlobalParam;

public class GlobalParamDaoImpl extends GenericDaoImpl<GlobalParam, String> implements GlobalParamDao {

	public GlobalParamDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, GlobalParam.class);
	}
}
