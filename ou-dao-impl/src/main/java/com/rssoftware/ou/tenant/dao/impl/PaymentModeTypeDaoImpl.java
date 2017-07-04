package com.rssoftware.ou.tenant.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.PaymentModeType;
import com.rssoftware.ou.tenant.dao.PaymentModeTypeDao;

@Repository
public class PaymentModeTypeDaoImpl extends GenericDynamicDaoImpl<PaymentModeType, String> implements PaymentModeTypeDao {

	public PaymentModeTypeDaoImpl() {
		super(PaymentModeType.class);
		// TODO Auto-generated constructor stub
	}
	private static final String GET_PAYMENT_MODE_NAME = "select pmDesc from PaymentModeType a where a.pmId in ( ";

//	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	@Override
	@SuppressWarnings("unchecked")
	public List<String> fetchPaymentModeNames(List<Long> lstPaymentModeIds){
		StringBuilder query = new StringBuilder();
		List<String>  lstPaymentModes = new ArrayList<String>();
		if(null != lstPaymentModeIds && !lstPaymentModeIds.isEmpty()){
				query.append(GET_PAYMENT_MODE_NAME);
				for(int i=0;i<lstPaymentModeIds.size();i++){
					query.append(lstPaymentModeIds.get(i));
					if(i == lstPaymentModeIds.size()-1){
						query.append(")");
					}
					else{
						query.append(",");
					}
				}
				lstPaymentModes = getSessionFactory().getCurrentSession().createQuery(query.toString()).list();
			}
		return lstPaymentModes;
	}

}

