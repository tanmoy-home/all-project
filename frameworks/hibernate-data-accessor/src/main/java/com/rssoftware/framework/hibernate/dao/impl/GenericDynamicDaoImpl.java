package com.rssoftware.framework.hibernate.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;

@TenantTransactional(propagation=Propagation.MANDATORY, isolation=Isolation.READ_COMMITTED)
public class GenericDynamicDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {
	private final ConcurrentHashMap<String, SessionFactory> sessionFactoryCache = new ConcurrentHashMap<>();
	private Class<T> type;

	public GenericDynamicDaoImpl(Class<T> type) {
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public PK create(T o) {
		return (PK) getSessionFactory().getCurrentSession().save(o);
	}

	@SuppressWarnings("unchecked")
	@TenantTransactional(propagation=Propagation.MANDATORY, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public T get(PK id) {
		T value = (T) getSessionFactory().getCurrentSession().get(type, id);
		if (value == null) {
            return null;
        }

        return getUnproxiedObject(value);
	}
	
	@SuppressWarnings("unchecked")
	@TenantTransactional(propagation=Propagation.MANDATORY, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public T load(PK id) {
		T value = (T) getSessionFactory().getCurrentSession().load(type, id);
		if (value == null) {
            return null;
        }

        return getUnproxiedObject(value);
	}

	@SuppressWarnings("unchecked")
	@TenantTransactional(propagation=Propagation.MANDATORY, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<T> getAll() {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(type);
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public T createOrUpdate(T o) {
		try {
			Session session = getSessionFactory().getCurrentSession(); 
			session.saveOrUpdate(o);
			session.flush();
			return o;
		} catch (HibernateException e) {
			return (T) getSessionFactory().getCurrentSession().merge(o);
		}
	}


	public void update(T o) {
		Session session = getSessionFactory().getCurrentSession();
		session.update(o);
		session.flush();
	}

	public void delete(T o) {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(o);
		session.flush();
	}
	
	@SuppressWarnings("unchecked")
	protected T getUnproxiedObject(T value){
        if (value != null && (value instanceof HibernateProxy)) {
			Hibernate.initialize(value);
	        value = (T) ((HibernateProxy) value).getHibernateLazyInitializer().getImplementation();
        }
        return value;
	}
	
	protected final SessionFactory getSessionFactory(){
		String tenantId = TransactionContext.getTenantId();
		if (tenantId == null || "".equals(tenantId.trim())){
			throw new IllegalArgumentException("Tenant ID not found in context");
		}
		
		String qualifier = TransactionContext.BEAN_HIBERNATE_SESSION_FACTORY_PREFIX+tenantId;
		
		SessionFactory sessionFactory = this.sessionFactoryCache.get(qualifier);
		if (sessionFactory == null) {
			sessionFactory = BeanLocator.getBean(qualifier, SessionFactory.class);
			
			if (sessionFactory == null){
				throw new IllegalArgumentException("no mapped session factory in context for tenant ID:"+tenantId);
			}
			this.sessionFactoryCache.putIfAbsent(qualifier, sessionFactory);
		}
		return sessionFactory;
	}

}