package com.rssoftware.ou.tenant.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PcPmMapping;
import com.rssoftware.ou.tenant.dao.PcPmMappingDao;

 @Repository
public class PcPmMappingDaoImpl extends GenericDynamicDaoImpl<PcPmMapping, String> implements PcPmMappingDao {
	
	public PcPmMappingDaoImpl() {
		super(PcPmMapping.class);
	}
	
	private static final String GET_PAYMENT_MODE_ID = "select pmId from PcPmMapping a where a.pcId = :pcId and isSupported = 'Y' ";

//	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> fetchPaymentModeIds(List<Long> lstPaymentChannelIds){
			Long pcId = lstPaymentChannelIds.get(0);
			List<Long> lstPaymentModesId = new ArrayList<Long>();
			lstPaymentModesId = getSessionFactory().getCurrentSession().createQuery(GET_PAYMENT_MODE_ID).setLong("pcId", pcId).list();
			return lstPaymentModesId;
	}

}