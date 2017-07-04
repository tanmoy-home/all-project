package com.rssoftware.framework.hibernate.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rssoftware.framework.hibernate.dao.GenericDao;

@Transactional(propagation=Propagation.MANDATORY, isolation=Isolation.READ_COMMITTED)
public class GenericDaoImpl<T, PK extends Serializable> extends HibernateDaoSupport implements GenericDao<T, PK> {

	private Class<T> type;

	public GenericDaoImpl(SessionFactory sessionFactory, Class<T> type) {
		super.setSessionFactory(sessionFactory);
		this.type = type;
	}

	@SuppressWarnings("unchecked")
	public PK create(T o) {
		return (PK) getSessionFactory().getCurrentSession().save(o);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public T get(PK id) {
		T value = (T) getSessionFactory().getCurrentSession().get(type, id);
		if (value == null) {
            return null;
        }

        return getUnproxiedObject(value);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public T load(PK id) {
		T value = (T) getSessionFactory().getCurrentSession().load(type, id);
		if (value == null) {
            return null;
        }

        return getUnproxiedObject(value);
	}

	
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, readOnly=true)
	public List<T> getAll() {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(type);
		return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public T createOrUpdate(T o) {
		try {
			getSessionFactory().getCurrentSession().saveOrUpdate(o);
			return o;
		} catch (HibernateException e) {
			return (T) getSessionFactory().getCurrentSession().merge(o);
		}
	}


	public void update(T o) {
		getSessionFactory().getCurrentSession().update(o);
	}

	public void delete(T o) {
		getSessionFactory().getCurrentSession().delete(o);
	}
	
	@SuppressWarnings("unchecked")
	protected T getUnproxiedObject(T value){
        if (value != null && (value instanceof HibernateProxy)) {
			Hibernate.initialize(value);
	        value = (T) ((HibernateProxy) value).getHibernateLazyInitializer().getImplementation();
        }
        return value;
	}

}