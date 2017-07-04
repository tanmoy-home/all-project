package com.rssoftware.ou.tenant.dao;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.StateMaster;

public interface LocationDao extends GenericDao<StateMaster, String>{

	public String retrieveState(Long stateId);

}
