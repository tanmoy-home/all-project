package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ChannelPartnerInfo;
import com.rssoftware.ou.tenant.dao.ChannelPartnerInfoDao;

@Repository
public class ChannelPartnerInfoDaoImpl extends GenericDynamicDaoImpl<ChannelPartnerInfo, String> 
			implements ChannelPartnerInfoDao{

	public ChannelPartnerInfoDaoImpl() {
		super(ChannelPartnerInfo.class);
	}

}
