package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.SchemeAvlblityDetail;
import com.rssoftware.ou.database.entity.tenant.SchemeAvlblityDetailPK;

public interface AgentSchemeAvailabilityDao extends GenericDao<SchemeAvlblityDetail, SchemeAvlblityDetailPK> {

	List<SchemeAvlblityDetail> getAllBySchemeId(String agentSchemeId);
	List<SchemeAvlblityDetail> getAllByAgentInstituteId(String agentInstituteId);
	void saveAll(List<String> agentInstIds, String schemeId);

}
