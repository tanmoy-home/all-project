package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ResponseCode;
import com.rssoftware.ou.database.entity.tenant.ResponseCodePK;
import com.rssoftware.ou.tenant.dao.ResponseCodeDao;

@Repository
public class ResponseCodeDaoImpl extends GenericDynamicDaoImpl<ResponseCode, ResponseCodePK> implements
		ResponseCodeDao {

	public ResponseCodeDaoImpl() {
		super(ResponseCode.class);
	}
}
