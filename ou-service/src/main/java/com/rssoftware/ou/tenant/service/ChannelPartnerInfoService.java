package com.rssoftware.ou.tenant.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.rssoftware.framework.hibernate.dao.common.TenantTransactional;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.domain.ChannelPartnerInfoDetails;

public interface ChannelPartnerInfoService {

	@TenantTransactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED)
	public String insert(ChannelPartnerInfoDetails channelPartnerInfoDetails) throws ValidationException;

}
