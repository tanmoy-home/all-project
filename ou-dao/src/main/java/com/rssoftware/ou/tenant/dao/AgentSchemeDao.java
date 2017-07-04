package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.AgentScheme;
import com.rssoftware.ou.database.entity.tenant.SchemeAvlblityDetail;

public interface AgentSchemeDao extends GenericDao<AgentScheme, String> {

	public List<AgentScheme> getAllSchemes();
	
	public AgentScheme getScheme(String schemeId);

	public AgentScheme getLastScheme(String schemeName);

	public List<AgentScheme> getSchemeBySchemeName(String schemeName);
	
	public void updateLastScheme(String schemeId, String schemeEffctvTo);
	
	public List<AgentScheme> getSchemeForAgentInst(List<SchemeAvlblityDetail> availabilities);
	
}
