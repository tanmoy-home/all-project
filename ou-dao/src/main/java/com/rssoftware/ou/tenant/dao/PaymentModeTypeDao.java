package com.rssoftware.ou.tenant.dao;

import java.util.List;

public interface PaymentModeTypeDao {
	
	public List<String> fetchPaymentModeNames(List<Long> lstPaymentModeIds);

}
