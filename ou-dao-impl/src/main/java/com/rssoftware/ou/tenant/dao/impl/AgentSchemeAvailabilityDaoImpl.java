package com.rssoftware.ou.tenant.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.SchemeAvlblityDetail;
import com.rssoftware.ou.database.entity.tenant.SchemeAvlblityDetailPK;
import com.rssoftware.ou.tenant.dao.AgentSchemeAvailabilityDao;

@Repository
public class AgentSchemeAvailabilityDaoImpl extends GenericDynamicDaoImpl<SchemeAvlblityDetail, SchemeAvlblityDetailPK> implements AgentSchemeAvailabilityDao {

	public AgentSchemeAvailabilityDaoImpl() {
		super(SchemeAvlblityDetail.class);
	}

	public static final String GET_SCHEME_AVAILABILITY = "Select a from SchemeAvlblityDetail a where a.id.schemeUniqueId= :schemeId";
	public static final String GET_SCHEME_BY_AI_ID = "Select a from SchemeAvlblityDetail a where a.id.agentInstId= :agentInstId";

	@Override
	public List<SchemeAvlblityDetail> getAllBySchemeId(String agentSchemeId) {
		List<SchemeAvlblityDetail> agentSchemeAvailabilityDetails =  getSessionFactory().getCurrentSession().createQuery(GET_SCHEME_AVAILABILITY).setString("schemeId", agentSchemeId).list();
		return agentSchemeAvailabilityDetails;
	}

	@Override
	public List<SchemeAvlblityDetail> getAllByAgentInstituteId(String agentInstituteId) {
		List<SchemeAvlblityDetail> agentSchemeAvailabilityDetails =  getSessionFactory().getCurrentSession().createQuery(GET_SCHEME_BY_AI_ID).setString("agentInstId", agentInstituteId).list();
		return agentSchemeAvailabilityDetails;
	}

	@Override
	public void saveAll(List<String> agentInstIds, String schemeId) {
		SchemeAvlblityDetail availabilityDetail = null;
		SchemeAvlblityDetailPK avlblityDetailPK = null;
		for(String agentInstId:agentInstIds) {
			for ( int i=0; i<agentInstIds.size(); i++ ) {
				availabilityDetail = new SchemeAvlblityDetail();
				avlblityDetailPK = new SchemeAvlblityDetailPK();
				avlblityDetailPK.setAgentInstId(agentInstId);
				avlblityDetailPK.setSchemeUniqueId(schemeId);
				availabilityDetail.setId(avlblityDetailPK);
				getSessionFactory().getCurrentSession().save(availabilityDetail);
			    if ( i % 20 == 0 ) { //20, same as the JDBC batch size
			        //flush a batch of inserts and release memory:
			    	getSessionFactory().getCurrentSession().flush();
			    	getSessionFactory().getCurrentSession().clear();
			    }
			}
		}		
	}
	
	/*private List<AgentSchemeAvailabilityDetail> mapSchemeAvailabilityDetailFromView(List<String> agentInstIds, String schemeId) {
		List<AgentSchemeAvailabilityDetail> list = new ArrayList<AgentSchemeAvailabilityDetail>();
		for(String agentInstId:agentInstIds) {
			AgentSchemeAvailabilityDetail availabilityDetail = new AgentSchemeAvailabilityDetail();
			availabilityDetail.getId().setAgentInstId(agentInstId);
			availabilityDetail.getId().setAgentInstId(schemeId);
			list.add(availabilityDetail);
		}
		return list;
	}*/
}
