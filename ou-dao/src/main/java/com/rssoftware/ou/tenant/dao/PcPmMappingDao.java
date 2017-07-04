package com.rssoftware.ou.tenant.dao;

import java.util.List;

public interface PcPmMappingDao {
	
	public List<Long> fetchPaymentModeIds(List<Long> lstPaymentChannelIds);

}
