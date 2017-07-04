package com.rssoftware.ou.tenant.dao.impl;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.OfflinebPayment;
import com.rssoftware.ou.tenant.dao.OfflinebPaymentDao;

@Repository
public class OfflinebPaymentDaoImpl extends GenericDynamicDaoImpl<OfflinebPayment, String> implements OfflinebPaymentDao{
		public OfflinebPaymentDaoImpl() {
			super(OfflinebPayment.class);
		}
}
