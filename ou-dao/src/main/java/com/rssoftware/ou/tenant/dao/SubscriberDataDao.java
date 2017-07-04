package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.SubscriberData;

public interface SubscriberDataDao extends GenericDao<SubscriberData, String>{

	public SubscriberData getSubscriberDetails(String billerId, List<String> custParams);

}
