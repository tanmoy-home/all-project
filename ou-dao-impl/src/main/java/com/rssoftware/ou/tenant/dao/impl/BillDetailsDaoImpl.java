package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.tenant.dao.BillDetailsDao;
@Repository
public class BillDetailsDaoImpl extends GenericDynamicDaoImpl<BillDetails, String> implements BillDetailsDao{
	private static final String FETCH_BILL_DETAILS_QUERY = "select bd from BillDetails bd  ";
	public BillDetailsDaoImpl() {
		super(BillDetails.class);
	}

	@Override
	public BillDetails getBillDetails(String billerId, List<String> custParams) {
		int index = 0;
		//creating where clause dynamically
		StringBuilder whereClause = new StringBuilder("where bd.blrId = :billerId");
		for(int i=1;i<=custParams.size();i++){
			whereClause.append(" and bd.customerParam"+i+"=:customerParam"+i);
		}		
		String sqlQuery = FETCH_BILL_DETAILS_QUERY+whereClause.toString();
		Query query = getSessionFactory().getCurrentSession().createQuery(sqlQuery);
		query.setString("billerId", billerId);
		for(String param: custParams)
		{
			index++;
			query.setString("customerParam"+index, param);	
		}		 
		BillDetails billDetails = (BillDetails)query.uniqueResult();
		return billDetails;
	}

}
