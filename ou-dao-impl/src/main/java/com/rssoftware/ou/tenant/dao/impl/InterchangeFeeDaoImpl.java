package com.rssoftware.ou.tenant.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.InterchangeFee;
import com.rssoftware.ou.tenant.dao.InterchangeFeeDao;

@Repository
public class InterchangeFeeDaoImpl extends GenericDynamicDaoImpl<InterchangeFee, String> implements InterchangeFeeDao {
	private static final String FETCH_ALL_INTERCHANGE_FEE_BY_BILLER_ID = "select a from InterchangeFee a where a.billerId=:billerId";
	private static final String DELETE_ALL_INTERCHANGE_FEE_BY_BILLER_ID = "delete from InterchangeFee a where a.billerId=:billerId";
	private static final String FETCH_BY_BILLERID_FEECODE = "select a from InterchangeFee a where a.billerId=:billerId and a.feeCode=:feeCode";
	
	public InterchangeFeeDaoImpl() {
		super(InterchangeFee.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<InterchangeFee> fetchAllInterchangeFeeByBillerId(String blrId) {
		List<InterchangeFee> tempInterchangeFees = 
				(List<InterchangeFee>) getSessionFactory().getCurrentSession().
				createQuery(FETCH_ALL_INTERCHANGE_FEE_BY_BILLER_ID).
				setString("billerId", blrId).
				list();
		
		return getEffectiveInterchangeFeefList(tempInterchangeFees);
	}
	
	@Override
	public int deleteAllInterchangeFeeByBillerId(String blrId) {
		return getSessionFactory().getCurrentSession().createQuery(DELETE_ALL_INTERCHANGE_FEE_BY_BILLER_ID).setString("billerId", blrId).executeUpdate();
	}
	
	public List<InterchangeFee> getEffectiveInterchangeFeefList(List<InterchangeFee> tempInterchangeFees) {
		List<InterchangeFee> interchangeFees = new ArrayList<>();
		for(InterchangeFee iFee : tempInterchangeFees) {
			if(CommonUtils.isEffective(iFee.getEffctvFrom(), iFee.getEffctvTo())) {
				interchangeFees.add(iFee);
			}
		}
		return interchangeFees;
	}

	@Override
	public InterchangeFee getbyBilleridFeeCode(String billerId, String feeCode) {
		Query query = getSessionFactory().getCurrentSession().createQuery(FETCH_BY_BILLERID_FEECODE);
		query.setString("billerId", billerId);
		query.setString("feeCode", feeCode);
		InterchangeFee interchangeFee = (InterchangeFee) query.uniqueResult();
		return interchangeFee;
	}
}