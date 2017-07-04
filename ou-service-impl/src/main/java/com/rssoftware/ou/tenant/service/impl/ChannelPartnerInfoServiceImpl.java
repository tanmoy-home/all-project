package com.rssoftware.ou.tenant.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.ChannelPartnerInfo;
import com.rssoftware.ou.domain.ChannelPartnerInfoDetails;
import com.rssoftware.ou.tenant.dao.ChannelPartnerInfoDao;
import com.rssoftware.ou.tenant.service.ChannelPartnerInfoService;

@Service
public class ChannelPartnerInfoServiceImpl implements ChannelPartnerInfoService{

	@Autowired
	private ChannelPartnerInfoDao channelPartnerInfoDao;

	private final static Logger logger = LoggerFactory.getLogger(ChannelPartnerInfoServiceImpl.class);
	
	@Override
	public String insert(ChannelPartnerInfoDetails channelPartnerInfoDetails) throws ValidationException {
		String pk = String.valueOf(channelPartnerInfoDao.create(mapFrom(channelPartnerInfoDetails)));
		return pk;
	}

	private ChannelPartnerInfo mapFrom(ChannelPartnerInfoDetails channelPartnerInfoDetails){
		
		ChannelPartnerInfo channelPartnerInfo = new ChannelPartnerInfo();
		channelPartnerInfo.setAgentId(channelPartnerInfoDetails.getAgentId());
		channelPartnerInfo.setImeiCode(channelPartnerInfoDetails.getImeiCode());
		channelPartnerInfo.setPassKey(channelPartnerInfoDetails.getPassKey());
		channelPartnerInfo.setPaymentMode(channelPartnerInfoDetails.getPaymentMode());
		channelPartnerInfo.setRefId(channelPartnerInfoDetails.getRefId());
		return channelPartnerInfo;
	}
}
