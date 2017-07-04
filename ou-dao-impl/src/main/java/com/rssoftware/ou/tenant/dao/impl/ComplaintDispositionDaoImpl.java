package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ComplaintDisposition;
import com.rssoftware.ou.tenant.dao.ComplaintDispositionDao;

@Repository
public class ComplaintDispositionDaoImpl extends GenericDynamicDaoImpl<ComplaintDisposition, String> implements ComplaintDispositionDao{

	public ComplaintDispositionDaoImpl() {
		super(ComplaintDisposition.class);
		// TODO Auto-generated constructor stub
	}
}
