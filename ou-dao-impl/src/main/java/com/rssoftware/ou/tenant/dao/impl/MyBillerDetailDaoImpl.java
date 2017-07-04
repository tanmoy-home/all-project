package com.rssoftware.ou.tenant.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.database.entity.tenant.MyBillerDetail;
import com.rssoftware.ou.tenant.dao.MyBillerDetailDao;
@Repository
public class MyBillerDetailDaoImpl extends GenericDynamicDaoImpl<MyBillerDetail, String> implements MyBillerDetailDao{
	
	private static final String GET_ACTIVE = "select a from MyBillerDetail a where a.entityStatus = :entityStatus "
			+ "and ((blrEffctvFrom is null or blrEffctvFrom <= :today) or '20000101' <= :today) "
			+ "and ((blrEffctvTo is null or blrEffctvFrom >= :today) or '99990101' >= :today)";
	private static final String FETCH_BILL_DETAILS_QUERY = "select bd from BillDetails bd  ";
	
	private static final String GET_ALL_ACTIVE_BILLER_ID = "select blrId from MyBillerDetail a where a.entityStatus = :entityStatus "
			+ "and ((blrEffctvFrom is null or blrEffctvFrom <= :today) or '20000101' <= :today) "
			+ "and ((blrEffctvTo is null or blrEffctvFrom >= :today) or '99990101' >= :today)";
	
	private static final String FETCH_ALL_BILLER_ID = "select blrId from MyBillerDetail bd  ";
	
	public MyBillerDetailDaoImpl() {
		super(MyBillerDetail.class);
	}
		// fetch all active mybillers
		@SuppressWarnings("unchecked")
		@Override
		public List<MyBillerDetail> getActiveBillers() {
			List<MyBillerDetail> activeBillers = getSessionFactory().getCurrentSession().createQuery(GET_ACTIVE)
					.setString("entityStatus", EntityStatus.ACTIVE.name())
					.setString("today", CommonUtils.getFormattedDateyyyyMMdd(new Date())).list();
			return activeBillers;
		}
		@Override
		public BillDetails getBillDetails(String billerId, List<String> custParams) {
			int index = 1;
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
				query.setString("customerParam"+index, param);
				index++;
			}		 
			BillDetails billDetails = (BillDetails)query.uniqueResult();
			return billDetails;
		}
		@SuppressWarnings("unchecked")
		@Override
		public List<String> getAllCurrentBillerIds() {
			/*List<String> activeBillerIds = getSessionFactory().getCurrentSession().createQuery(GET_ALL_ACTIVE_BILLER_ID)
					.setString("entityStatus", EntityStatus.ACTIVE.name())
					.setString("today", CommonUtils.getFormattedDateyyyyMMdd(new Date())).list();*/
			List<String> activeBillerIds =getSessionFactory().getCurrentSession().createQuery(FETCH_ALL_BILLER_ID).list();
			return activeBillerIds;
		}
}
