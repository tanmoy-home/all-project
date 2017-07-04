package com.rssoftware.ou.tenant.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.database.entity.tenant.ReconDetails;
import com.rssoftware.ou.database.entity.tenant.ReconDetailsPK;
import com.rssoftware.ou.tenant.dao.ReconDetailsDao;

@Repository
public class ReconDetailsDaoImpl extends
		GenericDynamicDaoImpl<ReconDetails, ReconDetailsPK> implements
		ReconDetailsDao {
	private static final String GET_DATA = "select r from ReconDetails r where r.id.reconId = :reconId";

	public ReconDetailsDaoImpl() {
		super(ReconDetails.class);
	}

	// fetch recon_details based on recon_id
	@Override
	public List<ReconDetails> getReconDetailsList(String reconId) {
		ScrollableResults results = getSessionFactory().getCurrentSession()
				.createQuery(GET_DATA).setString("reconId", reconId)
				.setFetchSize(10).scroll(ScrollMode.FORWARD_ONLY);
		getSessionFactory().getCurrentSession().setCacheMode(CacheMode.IGNORE);
		List<ReconDetails> resultList = new LinkedList<ReconDetails>();
		while (results.next()) {
			resultList.add((ReconDetails) results.get(0));
			getSessionFactory().getCurrentSession().clear();
		}
		results.close();
		return resultList;
	}
}
