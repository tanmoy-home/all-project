package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.database.entity.tenant.AgentInstDetail;

public interface AgentInstDao extends GenericDao<AgentInstDetail, String> {

	public List<AgentInstDetail> getAllbyOu();
	public List<AgentInstDetail> exportReport();
	public List<AgentInstDetail> searchAgent();
	public List<AgentInstDetail> fetchAllByOU(int i, int pageSize,EntityStatus... entityStatus);

}
