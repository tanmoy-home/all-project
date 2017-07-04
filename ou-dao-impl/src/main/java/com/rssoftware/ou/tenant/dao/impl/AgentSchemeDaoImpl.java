package com.rssoftware.ou.tenant.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.AgentScheme;
import com.rssoftware.ou.database.entity.tenant.SchemeAvlblityDetail;
import com.rssoftware.ou.tenant.dao.AgentSchemeDao;

@Repository
public class AgentSchemeDaoImpl extends
		GenericDynamicDaoImpl<AgentScheme, String> implements AgentSchemeDao {

	public AgentSchemeDaoImpl() {
		super(AgentScheme.class);
	}

	public static final String UPDATE_LAST_SCHEME = "Update AgentScheme a set a.schemeEffctvTo=:schemeEffctvTo where a.schemeEffctvTo is null and a.schemeId=:schemeId";

	@Override
	public List<AgentScheme> getAllSchemes() {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentScheme.class);
		crit.add(Restrictions.or(Restrictions.lt("schemeEffctvFrom", CommonUtils.getFormattedDateyyyyMMdd(new Date())), Restrictions.eqOrIsNull("schemeEffctvFrom", CommonUtils.getFormattedDateyyyyMMdd(new Date()))));
		crit.add(Restrictions.or(Restrictions.gt("schemeEffctvTo", CommonUtils.getFormattedDateyyyyMMdd(new Date())), Restrictions.eqOrIsNull("schemeEffctvTo", CommonUtils.getFormattedDateyyyyMMdd(new Date()))));
		List<AgentScheme> agentSchemes = crit.list();		
		return agentSchemes;
	}
	
	@Override
	public AgentScheme getScheme(String schemeId) {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentScheme.class);
		crit.add(Restrictions.eq("schemeId", schemeId));
		crit.add(Restrictions.or(Restrictions.lt("schemeEffctvFrom", CommonUtils.getFormattedDateyyyyMMdd(new Date())), Restrictions.eqOrIsNull("schemeEffctvFrom", CommonUtils.getFormattedDateyyyyMMdd(new Date()))));
		crit.add(Restrictions.or(Restrictions.gt("schemeEffctvTo", CommonUtils.getFormattedDateyyyyMMdd(new Date())), Restrictions.eqOrIsNull("schemeEffctvTo", CommonUtils.getFormattedDateyyyyMMdd(new Date()))));
		AgentScheme agentScheme = (AgentScheme) crit.uniqueResult();		
		return agentScheme;
	}

	@Override
	public List<AgentScheme> getSchemeForAgentInst(List<SchemeAvlblityDetail> availabilities) {
		String[] schemeIds = null;
		if(!availabilities.isEmpty()) {
			schemeIds = new String[availabilities.size()];
			int i = 0;
			for(SchemeAvlblityDetail list:availabilities) {
				schemeIds[i++] = list.getId().getSchemeUniqueId();
			}
		}
		else {
			schemeIds = new String[1];
			schemeIds[0]="0";
		}
		
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentScheme.class);
		crit.add(Restrictions.or(Restrictions.lt("schemeEffctvFrom", CommonUtils.getFormattedDateyyyyMMdd(new Date())), Restrictions.eqOrIsNull("schemeEffctvFrom", CommonUtils.getFormattedDateyyyyMMdd(new Date()))));
		crit.add(Restrictions.or(Restrictions.gt("schemeEffctvTo", CommonUtils.getFormattedDateyyyyMMdd(new Date())), Restrictions.eqOrIsNull("schemeEffctvTo", CommonUtils.getFormattedDateyyyyMMdd(new Date()))));
		crit.add(Restrictions.or(Restrictions.in("schemeUniqueId", schemeIds),Restrictions.eq("schemeEffctvForAll", true)));
		//crit.add(Restrictions.eq("schemeEffctvForAll", true));
		List<AgentScheme> agentSchemes = crit.list();
		return agentSchemes;
	}

	@Override
	public List<AgentScheme> getSchemeBySchemeName(String schemeName) {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentScheme.class);
		crit.add(Restrictions.eq("schemeName", schemeName));
		List<AgentScheme> agentSchemes = crit.list();
		return agentSchemes;
	}
	/*public static final String GET_SCHEMES_FOR_AI = "Select a from AgentScheme a where (a.schemeEffctvFrom is null or a.schemeEffctvFrom<=:today) and (a.schemeEffctvTo is null or a.schemeEffctvTo<=:today) and a.schemeEffctvForAll=true and";

	@Override
	public List<AgentScheme> getSchemeForAgentInst(List<AvailabilityDetail> availabilities) {
		StringBuilder query = new StringBuilder("");
		query.append(GET_SCHEMES_FOR_AI);
		createInClause(query, availabilities);
		List<AvailabilityDetail> agentSchemeAvailabilityDetails =  getSessionFactory().getCurrentSession().createQuery(GET_SCHEME_AVAILABILITY).setString("schemeId", agentSchemeId).list();
		return agentSchemeAvailabilityDetails;
	}*/
	/*@Override
	public AgentScheme updateLastScheme(String schemeId) {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentScheme.class);
		crit.add(Restrictions.eq("schemeId", schemeId));
		crit.add(Restrictions.isNull("schemeEffctvTo"));
		AgentScheme agentScheme = (AgentScheme) crit.uniqueResult();
		return null;
	}*/
	
	@Override
	public void updateLastScheme(String schemeId, String schemeEffctvTo) {
		getSessionFactory().getCurrentSession().createQuery(UPDATE_LAST_SCHEME).setString("schemeId", schemeId).setString("schemeEffctvTo", schemeEffctvTo).executeUpdate();
	}

	@Override
	public AgentScheme getLastScheme(String schemeName) {
		Criteria crit = getSessionFactory().getCurrentSession().createCriteria(AgentScheme.class);
		crit.add(Restrictions.isNull("schemeEffctvTo"));
		crit.add(Restrictions.eq("schemeName", schemeName));
		AgentScheme agentScheme = (AgentScheme)crit.uniqueResult();
		return agentScheme;
	}
}
