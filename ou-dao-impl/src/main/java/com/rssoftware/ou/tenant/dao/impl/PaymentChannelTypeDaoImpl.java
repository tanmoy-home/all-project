package com.rssoftware.ou.tenant.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PaymentChannelType;
import com.rssoftware.ou.tenant.dao.PaymentChannelTypeDao;

@Repository
public class PaymentChannelTypeDaoImpl  extends GenericDynamicDaoImpl<PaymentChannelType, String> implements PaymentChannelTypeDao {
	
	
	
	public PaymentChannelTypeDaoImpl() {
		super(PaymentChannelType.class);
		// TODO Auto-generated constructor stub
	}

	private static final String GET_PAYMENT_CHANNEL_ID = "select pcId from PaymentChannelType a where a.pcDesc = :pcDesc";

//	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> fetchPaymentChannelIds(String paymentChannel){
		List<Long> lstPaymentChannelsId = new ArrayList<Long>();
		lstPaymentChannelsId = getSessionFactory().getCurrentSession().createQuery(GET_PAYMENT_CHANNEL_ID).setString("pcDesc", paymentChannel).list();
		return lstPaymentChannelsId;
	}

}
