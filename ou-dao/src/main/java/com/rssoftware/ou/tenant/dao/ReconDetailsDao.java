package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ReconDetails;
import com.rssoftware.ou.database.entity.tenant.ReconDetailsPK;

public interface ReconDetailsDao extends
		GenericDao<ReconDetails, ReconDetailsPK> {

	List<ReconDetails> getReconDetailsList(String reconId);

}
