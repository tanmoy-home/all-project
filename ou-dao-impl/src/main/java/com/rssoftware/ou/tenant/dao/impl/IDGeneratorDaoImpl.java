package com.rssoftware.ou.tenant.dao.impl;

import java.io.Serializable;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.BeanLocator.DatabaseType;
import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.tenant.dao.IDGeneratorDao;
import com.rssoftware.framework.hibernate.dao.common.BeanLocator.DatabaseType;

@Repository
public class IDGeneratorDaoImpl extends GenericDynamicDaoImpl<Void, Serializable> implements IDGeneratorDao {

	public IDGeneratorDaoImpl() {
		super(Void.class);
	}

	@Override
	public long getNextSequence() {
		Query query = null;
		Session currentSession = getSessionFactory().getCurrentSession();
	    DatabaseType databaseType = BeanLocator.getDatabaseType(BeanLocator.getBean("ds", DataSource.class));
		if (BeanLocator.DatabaseType.ORACLE.equals(databaseType)) {
			query = currentSession.createSQLQuery("select SEQ_ID_GENERATOR.nextval from dual");
		} else if (BeanLocator.DatabaseType.POSTGRES.equals(databaseType)) {
			query = currentSession.createSQLQuery("select nextval('SEQ_ID_GENERATOR')");
		}
	    
	    long key = ((Number) query.uniqueResult()).longValue();
	    return key;
	}

}
