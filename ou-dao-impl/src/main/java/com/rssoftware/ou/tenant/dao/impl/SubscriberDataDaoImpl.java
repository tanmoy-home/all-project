package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SubscriberData;
import com.rssoftware.ou.tenant.dao.SubscriberDataDao;

@Repository
public class SubscriberDataDaoImpl extends GenericDynamicDaoImpl<SubscriberData, String> implements SubscriberDataDao{
	
	private static final String FETCH_SUBSCRIBER= "select t from SubscriberData t  ";
	
	public SubscriberDataDaoImpl() {
			super(SubscriberData.class);
		}
	
	@Override
	public SubscriberData getSubscriberDetails(String billerId, List<String> custParams) {
		int index = 0;
		//creating where clause dynamically
		StringBuilder whereClause = new StringBuilder("where t.blrId = :billerId");
		for(int i=1;i<=custParams.size();i++){
			whereClause.append(" and t.customerParam"+i+"=:customerParam"+i);
		}		
		String sqlQuery = FETCH_SUBSCRIBER+whereClause.toString();
		Query query = getSessionFactory().getCurrentSession().createQuery(sqlQuery);
		query.setString("billerId", billerId);
		for(String param: custParams)
		{
			index++;
			query.setString("customerParam"+index, param);	
		}		 
		SubscriberData subscriberData = (SubscriberData)query.uniqueResult();
		return subscriberData;
	}
}
