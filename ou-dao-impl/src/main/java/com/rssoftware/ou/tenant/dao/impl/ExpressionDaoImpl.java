package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.Expression;
import com.rssoftware.ou.tenant.dao.ExpressionDao;

@Repository
public class ExpressionDaoImpl extends GenericDynamicDaoImpl<Expression, String> implements
		ExpressionDao {

	public ExpressionDaoImpl() {
		super(Expression.class);
	}
}
