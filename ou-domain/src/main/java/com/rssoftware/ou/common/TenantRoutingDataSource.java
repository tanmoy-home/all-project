package com.rssoftware.ou.common;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return TransactionContext.BEAN_DATA_SOURCE_PREFIX + TransactionContext.getTenantId();
	}
}
