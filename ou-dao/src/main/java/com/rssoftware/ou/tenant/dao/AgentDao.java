package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.model.tenant.AgentView;

public interface AgentDao extends GenericDao<AgentDetail, String> {

	AgentDetail getAgent(String agentId, String entytystatus);

	static List<AgentDetail> exportReport(String loggedInUsersOuId) {
		// TODO Auto-generated method stub
		return null;
	}
	List<AgentDetail> getAllAgentsByInstituteId(String instId);
	List<AgentDetail> getAllPendingAgentsByInstituteId(String instId);
	List<AgentDetail> getDefaultAgent();
	List<AgentDetail> getAllAgentsByActivationStatus(EntityStatus entityStatus);
	

}
