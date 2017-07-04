package com.rssoftware.framework.hibernate.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, PK extends Serializable> {
	PK create(T persistentObject);

	T get(PK id);
	
	T load(PK id);

	List<T> getAll();

	void update(T persistentObject);
	
	T createOrUpdate(T persistentObject);

	void delete(T persistentObject);
}
