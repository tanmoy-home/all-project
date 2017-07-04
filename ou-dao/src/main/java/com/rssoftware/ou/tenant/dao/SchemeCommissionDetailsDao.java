package com.rssoftware.ou.tenant.dao;

import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.SchemeCommissionDetail;

public interface SchemeCommissionDetailsDao extends GenericDao<SchemeCommissionDetail, Long> {

	List<SchemeCommissionDetail> getAllBySchemeId(String agentSchemeUniqueId);

}
