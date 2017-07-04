package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SMSConfig;
import com.rssoftware.ou.tenant.dao.SMSConfigDao;
@Repository
public class SMSConfigDaoImpl extends GenericDynamicDaoImpl<SMSConfig, String> implements SMSConfigDao {

	public SMSConfigDaoImpl() {
		super(SMSConfig.class);
	}
	
	public SMSConfig getSmsConfigByType(String tenantId,String sendType)
	{
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(SMSConfig.class);
		crit.add(Restrictions.eq("tenantId", tenantId));
		crit.add(Restrictions.eq("sendType", sendType));
		crit.add(Restrictions.eq("isAclDefault", true));
		List<SMSConfig> smsConfig =  crit.list();	
		if(smsConfig!=null&&smsConfig.size()>0)
		{
			return smsConfig.get(0); 
		}
		return null;
	}
}


