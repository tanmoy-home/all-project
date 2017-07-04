package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.tenant.dao.AgentDao;




import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AgentDaoImpl extends GenericDynamicDaoImpl<AgentDetail, String> implements AgentDao {

	public AgentDaoImpl() {
		super(AgentDetail.class);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
	public AgentDetail getAgent(String agentId, String entytystatus) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(AgentDetail.class);
		criteria.add(Restrictions.eq("agentId", agentId));
		criteria.add(Restrictions.eq("entityStatus", entytystatus));
		return (AgentDetail) criteria.uniqueResult();
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	@Transactional(isolation = Isolation.READ_COMMITTED)
		public List<AgentDetail> exportReport(String loggedInUsersOuId) {
			List<AgentDetail> billerList = null;
			if (loggedInUsersOuId != null || loggedInUsersOuId.length() == 0) {
				Criteria crit = getSessionFactory().getCurrentSession()
						.createCriteria(AgentDetail.class);
				crit.add(Restrictions.ilike("agentId",
						'%' + loggedInUsersOuId + '%'));
				crit.add(Restrictions.eq("entityStatus", "PENDING_ACTIVATION"));
				billerList = crit.list();
			}
			return billerList;
		}

	@Override
	public List<AgentDetail> getAllAgentsByInstituteId(String instId) {
		List<AgentDetail> agentList = null;
		if (instId != null || instId.length() != 0) {
			Criteria crit = getSessionFactory().getCurrentSession()
					.createCriteria(AgentDetail.class);
			crit.add(Restrictions.eq("agentLinkedAgentInst", instId));
			crit.addOrder(Order.desc("updtTs"));
			agentList = crit.list();
		}
		return agentList;
	}
	
	@Override
	public List<AgentDetail> getAllPendingAgentsByInstituteId(String instId) {
		List<AgentDetail> agentList = null;
		if (instId != null || instId.length() != 0) {
			Criteria crit = getSessionFactory().getCurrentSession()
					.createCriteria(AgentDetail.class);
			crit.add(Restrictions.eq("agentLinkedAgentInst", instId));
			crit.add(Restrictions.eq("entityStatus", EntityStatus.DRAFT.name()));

			agentList = crit.list();
		}
		return agentList;
	}
	
	@Override
	public List<AgentDetail> getDefaultAgent(){
		List<AgentDetail> agentList = null;
		
			Criteria crit = getSessionFactory().getCurrentSession()
					.createCriteria(AgentDetail.class);
			crit.add(Restrictions.eq("dummyAgent", true));
			crit.add(Restrictions.eq("entityStatus", EntityStatus.ACTIVE.name()));

			agentList = crit.list();
		
		return agentList;
	}

	@Override
	public List<AgentDetail> getAllAgentsByActivationStatus(EntityStatus entityStatus) {
		List<AgentDetail> agentList = null;
		
		Criteria crit = getSessionFactory().getCurrentSession()
				.createCriteria(AgentDetail.class);
		crit.add(Restrictions.eq("entityStatus", entityStatus.name()));

		agentList = crit.list();
	
		return agentList;
	}

}
