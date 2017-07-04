package com.rssoftware.ou.tenant.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rssoftware.framework.hibernate.dao.impl.GenericDynamicDaoImpl;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.ServiceTax;
import com.rssoftware.ou.tenant.dao.ServiceTaxDao;

@Repository
public class ServiceTaxDaoImpl extends GenericDynamicDaoImpl<ServiceTax, Long> implements ServiceTaxDao   {
	private static final String FETCH_ALL_ACTIVE_SERVICE_TAX = "select a from ServiceTax a";
	
	private static final String RETRIEVE_SERVICE_TAX = "Select a from ServiceTax a where a.effectiveFrom <=:date and (a.effectiveTo >=:date or a.effectiveTo is null) and a.entityStatus in('ACTIVE','PENDING_DEACTIVATION')";
	
	//private static final String RETRIEVE_MAXDATE_ROW="select a from ServiceTax a where a.effectiveFrom=:date";
	
	//private static final String UPDATE_NULL_ENDDATE = "select a from ServiceTax a where a.effectiveFrom<=:date and a.effectiveTo is null and a.entityStatus in('ACTIVE','PENDING_DEACTIVATION','PENDING_APPROVAL')";
	
	private static final String UPDATE_SQL_ON_DEACTIVATE = "select a from ServiceTax a where a.effectiveTo=:date and a.code=:code and a.entityStatus in('ACTIVE','PENDING_DEACTIVATION')";
	
	private static final String DEACTIVATE_ENDDATE = "select a from ServiceTax a where a.effectiveFrom<=:date and a.entityStatus ='PENDING_APPROVAL'";
	
	public ServiceTaxDaoImpl() {
		super(ServiceTax.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceTax> fetchAllActiveList() {
		List<ServiceTax> tempServiceTaxs = 
				(List<ServiceTax>) getSessionFactory().getCurrentSession().
				createQuery(FETCH_ALL_ACTIVE_SERVICE_TAX).
				list();
		
		return getEffectiveServiceTaxList(tempServiceTaxs);
	}	
	
	public List<ServiceTax> getEffectiveServiceTaxList(List<ServiceTax> tempServiceTaxs) {
		List<ServiceTax> serviceTaxs = new ArrayList<>();
		for(ServiceTax sTax : tempServiceTaxs) {
			if(CommonUtils.isEffective(sTax.getEffectiveFrom(), sTax.getEffectiveTo())) {
				serviceTaxs.add(sTax);
			}
		}
		return serviceTaxs;
	}

	@Override
	public List<ServiceTax> getServiceTax(String date) {
		
		List<ServiceTax> serviceTax = getSessionFactory().getCurrentSession().createQuery(RETRIEVE_SERVICE_TAX).setString("date", date).list();
		return serviceTax;
		
	}

	/*@Override
	public ServiceTax selectMaxDate() {
		try {
		String statusArr[]=new String[]{EntityStatus.ACTIVE.name(), EntityStatus.PENDING_DEACTIVATION.name()};
		Criteria criteria = getSessionFactory().getCurrentSession()
							.createCriteria(ServiceTax.class)
							.setProjection(Projections.max("effectiveFrom"))
							.add(Restrictions.in("entityStatus", statusArr));
		String maxDate = (String)criteria.uniqueResult();
		List<ServiceTax> serviceTax = getSessionFactory().getCurrentSession().createQuery(RETRIEVE_MAXDATE_ROW).setString("date", maxDate).list();
		return serviceTax.get(0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

	@Override
	public ServiceTax updateEndDate(String startDate,
			String endDate, String code) {
		ServiceTax serviceTax = (ServiceTax)getSessionFactory().getCurrentSession().createQuery(UPDATE_SQL_ON_DEACTIVATE).setString("date", startDate).setString("code", code).uniqueResult();
		return serviceTax;
	}

	@Override
	public ServiceTax updateEndDate(String date, String code) {
		String statusArr[]=new String[]{EntityStatus.ACTIVE.name(), EntityStatus.PENDING_DEACTIVATION.name()};

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(ServiceTax.class)
				.add(Restrictions.eq("code", code))
				.add(Restrictions.isNull("effectiveTo"))
				.add(Restrictions.in("entityStatus", statusArr));

		
		ServiceTax serviceTax = (ServiceTax)criteria.uniqueResult();
		return serviceTax;
	}
	
	@Override
	public ServiceTax deactivateEndDate(Date endDate) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(ServiceTax.class)
				.setProjection(Projections.rowCount());
		long count = (long)criteria.uniqueResult();
		ServiceTax serviceTax = null;
		if(count==1) {
			serviceTax = (ServiceTax)getSessionFactory().getCurrentSession().createQuery(DEACTIVATE_ENDDATE).setString("date", CommonUtils.getFormattedDateyyyyMMdd(endDate)).uniqueResult();
		}
		return serviceTax;
	}

	@Override
	public List<ServiceTax> getAllActive() {
		String statusArr[]=new String[]{EntityStatus.ACTIVE.name(), EntityStatus.PENDING_DEACTIVATION.name()};

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(ServiceTax.class)
				.add(Restrictions.in("entityStatus", statusArr));

		
		List<ServiceTax> serviceTaxList = criteria.list();
		return serviceTaxList;
	}
}