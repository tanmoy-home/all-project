package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.BillerFetchRequestResponse;
import com.rssoftware.ou.tenant.dao.BillerFetchRequestResponseDao;

@Repository
public class BillerFetchRequestResponseDaoImpl extends GenericDynamicDaoImpl<BillerFetchRequestResponse,String> implements BillerFetchRequestResponseDao{

	public BillerFetchRequestResponseDaoImpl() {
		super(BillerFetchRequestResponse.class);
		// TODO Auto-generated constructor stub
	}

}
