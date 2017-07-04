package com.rssoftware.ou.tenant.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.InterchangeFeeConf;
import com.rssoftware.ou.tenant.dao.InterchangeFeeConfDao;

@Repository
public class InterchangeFeeConfDaoImpl extends GenericDynamicDaoImpl<InterchangeFeeConf, String> implements InterchangeFeeConfDao   {
	private static final String FETCH_ALL_INTERCHANGE_FEE_CONF_BY_BILLER_ID = "select a from InterchangeFeeConf a where a.blrId=:blrId";
	private static final String DELETE_ALL_INTERCHANGE_FEE_CONF_BY_BILLER_ID = "delete from InterchangeFeeConf a where a.blrId=:blrId";
	
	public InterchangeFeeConfDaoImpl() {
		super(InterchangeFeeConf.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InterchangeFeeConf> fetchAllInterchangeFeeConfByBillerId(String blrId) {
		List<InterchangeFeeConf> tempInterchangeFeeConfs = 
				(List<InterchangeFeeConf>) getSessionFactory().getCurrentSession().
				createQuery(FETCH_ALL_INTERCHANGE_FEE_CONF_BY_BILLER_ID).
				setString("blrId", blrId).
				list();
		
		
		return getEffectiveInterchangeFeeConfList(tempInterchangeFeeConfs);
	}
	
	@Override
	public int deleteAllInterchangeFeeConfByBillerId(String blrId) {
		return getSessionFactory().getCurrentSession().createQuery(DELETE_ALL_INTERCHANGE_FEE_CONF_BY_BILLER_ID).setString("blrId", blrId).executeUpdate();
	}
		
	public List<InterchangeFeeConf> getEffectiveInterchangeFeeConfList(List<InterchangeFeeConf> tempInterchangeFeeConfs) {
		List<InterchangeFeeConf> interchangeFeeConfs = new ArrayList<>();
		for(InterchangeFeeConf iFeeConf : tempInterchangeFeeConfs) {
			if(CommonUtils.isEffective(iFeeConf.getEffectiveFrom(), iFeeConf.getEffectiveTo())) {
				interchangeFeeConfs.add(iFeeConf);
			}			
		}
		return interchangeFeeConfs;
	}
}