package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ChannelParameters;
import com.rssoftware.ou.tenant.dao.ChannelParametersDao;


@Repository
public class ChannelParametersDaoImpl extends GenericDynamicDaoImpl<ChannelParameters, String> implements ChannelParametersDao {

	public ChannelParametersDaoImpl() {
		super(ChannelParameters.class);
	} {

	}
}
