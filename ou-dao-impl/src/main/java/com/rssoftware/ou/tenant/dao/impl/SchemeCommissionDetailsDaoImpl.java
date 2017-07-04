package com.rssoftware.ou.tenant.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SchemeCommissionDetail;
import com.rssoftware.ou.tenant.dao.SchemeCommissionDetailsDao;

@Repository
public class SchemeCommissionDetailsDaoImpl extends GenericDynamicDaoImpl<SchemeCommissionDetail, Long>
		implements SchemeCommissionDetailsDao {

	private static final String GET_DETAILS_BY_SCHEME_ID = "Select a from SchemeCommissionDetail a where a.id.schemeUniqueId= :schemeUniqueId";

	public SchemeCommissionDetailsDaoImpl() {
		super(SchemeCommissionDetail.class);
	}

	@Override
	public List<SchemeCommissionDetail> getAllBySchemeId(String schemeUniqueId) {
		List<SchemeCommissionDetail> agentSchemeDetails = getSessionFactory().getCurrentSession()
				.createQuery(GET_DETAILS_BY_SCHEME_ID).setString("schemeUniqueId", schemeUniqueId).list();
		return agentSchemeDetails;
	}

}
