package com.rssoftware.ou.tenant.dao;

import java.sql.Date;
import java.util.List;

import com.rssoftware.framework.hibernate.dao.GenericDao;
import com.rssoftware.ou.database.entity.tenant.ServiceTax;

public interface ServiceTaxDao extends GenericDao<ServiceTax, Long> {

	public List<ServiceTax> fetchAllActiveList();

	public List<ServiceTax> getServiceTax(String date);

/*	public ServiceTax selectMaxDate();
*/
	public ServiceTax updateEndDate(String startDate, String endDate, String code);

	public ServiceTax updateEndDate(String date, String code);

	public ServiceTax deactivateEndDate(Date endDate);

	public List<ServiceTax> getAllActive();

}
