package com.rssoftware.ou.dao.impl;

import org.hibernate.SessionFactory;

import com.rssoftware.framework.hibernate.dao.impl.GenericDaoImpl;
import com.rssoftware.ou.dao.GlobalTemplateDao;
import com.rssoftware.ou.database.entity.global.GlobalTemplate;

public class GlobalTemplateDaoImpl extends GenericDaoImpl<GlobalTemplate, String> implements GlobalTemplateDao {

	public GlobalTemplateDaoImpl(SessionFactory sessionFactory) {
		super(sessionFactory, GlobalTemplate.class);
	}
}
