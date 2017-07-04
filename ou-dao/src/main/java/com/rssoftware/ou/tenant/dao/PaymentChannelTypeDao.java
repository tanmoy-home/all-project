package com.rssoftware.ou.tenant.dao;

import java.util.List;

public interface PaymentChannelTypeDao {
	
	public List<Long> fetchPaymentChannelIds(String paymentChannel);

}
