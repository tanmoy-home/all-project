package com.rssoftware.ou.tenant.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.BillDetails;
import com.rssoftware.ou.database.entity.tenant.BillerDetail;
import com.rssoftware.ou.tenant.dao.BillerDao;

@Repository
public class BillerDaoImpl extends GenericDynamicDaoImpl<BillerDetail, String>implements BillerDao {
	private static final String GET_ALL_ACTIVE_BILLER_ID = "select blrId from BillerDetail a where a.entityStatus = :entityStatus "
			+ "and ((blrEffctvFrom is null or blrEffctvFrom <= :today) or '20000101' <= :today) "
			+ "and ((blrEffctvTo is null or blrEffctvFrom >= :today) or '99990101' >= :today)";

	private static final String GET_ACTIVE = "select a from BillerDetail a where a.entityStatus = :entityStatus "
			+ "and ((blrEffctvFrom is null or blrEffctvFrom <= :today) or '20000101' <= :today) "
			+ "and ((blrEffctvTo is null or blrEffctvFrom >= :today) or '99990101' >= :today)";

	private static final String FETCH_ALL_PARENT = "select a from BillerDetail a where entityStatus in ('ACTIVE', 'PENDING_DEACTIVATION', 'PENDING_DELETE') and ((blrEffctvFrom is null and blrEffctvTo is null) or (((blrEffctvFrom is null or blrEffctvFrom <= :today)) and  ((blrEffctvTo is null or blrEffctvTo >= :today))))) and isParentBlr='T'";
	private static final String SEARCH_ALL = "select bd from BillerDetail bd where bd.blrLinkedOuDefault ilike :blrLinkedOuDefault order by bd.blrName";

	private static final String FETCH_ALL_QUERY = "select bd from BillerDetail bd  order by bd.blrId";
	private static final String FETCH_ALL_QUERY_1 = "select bd from BillerDetail bd where bd.entityStatus in (";
	private static final String FETCH_ALL_QUERY_2 = ") order by bd.blrId";

	private static final String FETCH_ALL_QUERY_FOR_USER = "select  bd from BillerDetail bd  where bd.blrLinkedOuDefault =:bd.blrLinkedOuDefault order by bd.blrId";
	private static final String FETCH_ALL_QUERY_FOR_USER_1 = "select bd from BillerDetail bd where  bd.blrLinkedOuDefault =:bd.blrLinkedOuDefault and bd.entityStatus in (";
	private static final String FETCH_ALL_QUERY_FOR_USER_2 = ") order by bd.blrId";

	private static final String FETCH_QUERY = "select bd from BillerDetail bd where bd.blrId =:blrId";
	private static final String FETCH_QUERY_1 = "select bd from BillerDetail bd where bd.blrId =:blrId and bd.entityStatus in (";
	private static final String FETCH_QUERY_2 = ")";

	private static final String FETCH_SUB_BILLERS = "select bd from BillerDetail bd  where bd.parentBlrId = :parentBillerId";
	
	private static final String FETCH_BILL_DETAILS_QUERY = "select bd from BillDetails bd  where bd.blrId = :billerId"
			+ " and bd.customerParam1=:customerParam1"
			+ " and bd.customerParam2=:customerParam2"
			+ " and bd.customerParam3=:customerParam3"
			+ " and bd.customerParam4=:customerParam4"
			+ " and bd.customerParam5=:customerParam5";
	
	private static final String FETCH_BILL_DETAILS_QUERY1 = "select bd from BillDetails bd  ";

	public BillerDaoImpl() {
		super(BillerDetail.class);
	}

	// fetch all active billers
	@SuppressWarnings("unchecked")
	@Override
	public List<BillerDetail> getActiveBillers() {
		List<BillerDetail> activeBillers = getSessionFactory().getCurrentSession().createQuery(GET_ACTIVE)
				.setString("entityStatus", EntityStatus.ACTIVE.name())
				.setString("today", CommonUtils.getFormattedDateyyyyMMdd(new Date())).list();
		return activeBillers;
	}

	// search parent biller
	@Override
	public List<BillerDetail> searchParentBiller() {
		List<BillerDetail> billerList = getSessionFactory().getCurrentSession().createQuery(FETCH_ALL_PARENT).list();
		return billerList;
	}

	// fetch all billers based on billerlinkedoudefault ordered by name
	@Override
	public List<BillerDetail> searchBiller(String loggedInUsersOuId) {

		List<BillerDetail> billerList = null;
		if (loggedInUsersOuId != null && loggedInUsersOuId.length() > 0) {
			billerList = getSessionFactory().getCurrentSession().createQuery(SEARCH_ALL)
					.setString("blrLinkedOuDefault", "%" + loggedInUsersOuId + "%").list();
		}
		return billerList;

	}

	// fetch billers based on offset and limit
	@Override
	public List<BillerDetail> fetchAll(int offset, int limit, EntityStatus... entityStatus) {
		StringBuilder query = new StringBuilder("");
		if (entityStatus == null || entityStatus.length == 0) {
			query.append(FETCH_ALL_QUERY);
		} else {
			query.append(FETCH_ALL_QUERY_1);
			createEntityStatusInClause(query, entityStatus);
			query.append(FETCH_ALL_QUERY_2);
		}
		if (limit > 0 && offset >= 0) {
			query.append(" offset ");
			query.append(offset);
			query.append(" limit ");
			query.append(limit);
		}
		List<BillerDetail> billerList = getSessionFactory().getCurrentSession().createQuery(query.toString()).list();
		return billerList;
	}

	// create entity status in clause
	public static void createEntityStatusInClause(StringBuilder query, EntityStatus[] entityStatus) {
		for (int i = 0; i < entityStatus.length; i++) {
			query.append("'");
			query.append(entityStatus[i].name());
			query.append("'");

			if (i < (entityStatus.length - 1)) {
				query.append(",");
			}
		}
	}

	// fetch biller based on billerlinkedoudefault ordered by billerid
	@Override
	public List<BillerDetail> fetchAllForUser(int offset, int limit, String loggedInUsersOuId,
			EntityStatus... entityStatus) {

		StringBuilder query = new StringBuilder("");
		if (entityStatus == null || entityStatus.length == 0) {
			query.append(FETCH_ALL_QUERY_FOR_USER);
		} else {
			query.append(FETCH_ALL_QUERY_FOR_USER_1);
			createEntityStatusInClause(query, entityStatus);
			query.append(FETCH_ALL_QUERY_FOR_USER_2);
		}
		if (limit > 0 && offset >= 0) {
			query.append(" offset ");
			query.append(offset);
			query.append(" limit ");
			query.append(limit);
		}

		List<BillerDetail> billerList = getSessionFactory().getCurrentSession().createQuery(query.toString())
				.setString("blrLinkedOuDefault", loggedInUsersOuId).list();
		return billerList;
	}

	// fetching biller based on id and entitystatus
	@Override
	public List<BillerDetail> fetch(String billerId, EntityStatus... entityStatus) {

		StringBuilder query = new StringBuilder("");
		if (entityStatus == null || entityStatus.length == 0) {
			query.append(FETCH_QUERY);
		} else {
			query.append(FETCH_QUERY_1);
			createEntityStatusInClause(query, entityStatus);
			query.append(FETCH_QUERY_2);
		}
		List<BillerDetail> billerList = getSessionFactory().getCurrentSession().createQuery(query.toString())
				.setString("blrId", billerId).list();
		return billerList;
	}

	// fetching data of all pending_activation billers under the logged in
	// userid
	@Override
	public List<BillerDetail> exportReport(String loggedInUsersOuId) {
		List<BillerDetail> billerList = null;
		if (loggedInUsersOuId != null && loggedInUsersOuId.length() != 0) {
			Criteria crit = getSessionFactory().getCurrentSession().createCriteria(BillerDetail.class);
			crit.add(Restrictions.ilike("blrLinkedOuDefault", '%' + loggedInUsersOuId + '%'));
			crit.add(Restrictions.eq("entityStatus", EntityStatus.PENDING_ACTIVATION.name()));
			billerList = crit.list();
		}
		return billerList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllCurrentBillerIds() {
		List<String> activeBillerIds = getSessionFactory().getCurrentSession().createQuery(GET_ALL_ACTIVE_BILLER_ID)
				.setString("entityStatus", EntityStatus.ACTIVE.name())
				.setString("today", CommonUtils.getFormattedDateyyyyMMdd(new Date())).list();

		return activeBillerIds;
	}

	@Override
	public List<BillerDetail> getSubBillersByParentId(String parentbillerId) {
		List<BillerDetail> subBillers = getSessionFactory().getCurrentSession().createQuery(FETCH_SUB_BILLERS)
				.setString("parentBillerId", parentbillerId).list();
		return subBillers;
	}
	
	@Override
	public BillDetails getBillDetails(String billerId,String param1,String param2,
			String param3,String param4,String param5) {
		Query query = getSessionFactory().getCurrentSession().createQuery(FETCH_BILL_DETAILS_QUERY);
		query.setString("billerId", billerId);
		query.setString("customerParam1", param1);
		query.setString("customerParam2", param2);
		query.setString("customerParam3", param3);
		query.setString("customerParam4", param4);
		query.setString("customerParam5", param5);
		BillDetails billDetails = (BillDetails) query.uniqueResult();
		return billDetails;
	}

	@Override
	public BillDetails getBillDetails(String billerId, List<String> custParams) {
		int index = 1;
		//creating where clause dynamically
		StringBuilder whereClause = new StringBuilder("where bd.blrId = :billerId");
		for(int i=1;i<=custParams.size();i++){
			whereClause.append(" and bd.customerParam"+i+"=:customerParam"+i);
		}		
		String sqlQuery = FETCH_BILL_DETAILS_QUERY1+whereClause.toString();
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
}
