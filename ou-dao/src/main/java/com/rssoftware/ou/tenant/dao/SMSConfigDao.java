package com.rssoftware.ou.tenant.dao;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.AgentScheme;
import com.rssoftware.ou.database.entity.tenant.SMSConfig;

public interface SMSConfigDao extends GenericDao<SMSConfig, String>{
	
	
	public SMSConfig getSmsConfigByType(String tenantId,String sendType);

}
