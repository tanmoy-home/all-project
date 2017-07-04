package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.database.entity.tenant.AgentInstDetail;
import com.rssoftware.ou.tenant.dao.AgentInstDao;

@Repository
public class AgentInstDaoImpl extends GenericDynamicDaoImpl<AgentInstDetail, String> implements AgentInstDao {

	private static final String FETCH_ALL_QUERY_BY_OU = "select a from AgentInstDetail a";
	private static final String FETCH_ALL_QUERY_1 = "select a from AgentInstDetail a where a.entityStatus in (";
	private static final String SRCH_QUERY_ORDERBY = " order by a.agentInstName";

	public AgentInstDaoImpl() {
		super(AgentInstDetail.class);
	}

	@Override
	public List<AgentInstDetail> getAllbyOu() {
			Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentInstDetail.class);
			crit.add(Restrictions.eq("entityStatus", EntityStatus.CU_APPROVED.name()));
			List<AgentInstDetail> agentInstList = crit.list();
		
		return agentInstList;
	}
	
	//fetching data of all pending_activation agent institutes under the logged in userid
		@Override
		public List<AgentInstDetail> exportReport() {
				Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentInstDetail.class);
				crit.add(Restrictions.eq("entityStatus", EntityStatus.PENDING_ACTIVATION.name()));
				List<AgentInstDetail> agentInstList = crit.list();
			
			return agentInstList;
		}

		@Override
		public List<AgentInstDetail> searchAgent() {
			
				Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentInstDetail.class);
				crit.addOrder(Order.asc("agentInstName"));
				List<AgentInstDetail> agentInstList = crit.list();
			
			return agentInstList;
		}

		
		@Override
		public List<AgentInstDetail> fetchAllByOU(int offset, int limit, EntityStatus... entityStatus) {
			StringBuilder query = new StringBuilder("");
		
			   
			if (entityStatus == null || entityStatus.length == 0){
				query.append(FETCH_ALL_QUERY_BY_OU);
			}		
			else {
				query.append(FETCH_ALL_QUERY_1);
				createEntityStatusInClause(query, entityStatus);
				query.append(")");
			}
			
			
			
			
			query.append(SRCH_QUERY_ORDERBY);
			
			
				query.append(" offset :offset");
				query.append(" limit :limit");
			
			
			List<AgentInstDetail> agentInstList = getSessionFactory().getCurrentSession().createQuery(query.toString()).setInteger("offset", offset).setInteger("limit", limit).list();
			return agentInstList;
		}
		//create entity status in clause
		public static void createEntityStatusInClause(StringBuilder query,
				EntityStatus[] entityStatus) {
			for (int i = 0; i < entityStatus.length; i++) {
				query.append("'");
				query.append(entityStatus[i].name());
				query.append("'");

				if (i < (entityStatus.length - 1)) {
					query.append(",");
				}
			}
		}
}
