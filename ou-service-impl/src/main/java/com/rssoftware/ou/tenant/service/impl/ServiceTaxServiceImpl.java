package com.rssoftware.ou.tenant.service.impl;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.ServiceTax;
import com.rssoftware.ou.database.entity.tenant.ServiceTaxConf;
import com.rssoftware.ou.model.tenant.ServiceTaxConfView;
import com.rssoftware.ou.model.tenant.ServiceTaxView;
import com.rssoftware.ou.tenant.dao.ServiceTaxDao;
import com.rssoftware.ou.tenant.service.ServiceTaxService;

@Service
public class ServiceTaxServiceImpl implements ServiceTaxService {

	private static final Logger log = LoggerFactory.getLogger(ServiceTaxServiceImpl.class);
	@Autowired
	private ServiceTaxDao serviceTaxDao;
	
	@Override
	public List<ServiceTaxView> fetchAll() {
		List<ServiceTax> sTaxList =  serviceTaxDao.getAll();
		List<ServiceTaxView> sTaxViewList = new ArrayList<>();
		sTaxList.forEach( sTax-> {
			if(sTax.getEffectiveTo()!=null && sTax.getEffectiveFrom().compareTo(sTax.getEffectiveTo())>0)
				updateStatus(String.valueOf(sTax.getServiceTaxId()), EntityStatus.DEACTIVATED);
			sTaxViewList.add(mapFrom(sTax));
		});
		return sTaxViewList;
	}
	@Override
	public List<ServiceTaxView> fetchAllActive() {
		List<ServiceTax> sTaxList =  serviceTaxDao.getAllActive();
		List<ServiceTaxView> sTaxViewList = new ArrayList<>();
		sTaxList.forEach( sTax-> {
			if(sTax.getEffectiveTo()!=null && sTax.getEffectiveFrom().compareTo(sTax.getEffectiveTo())>0)
				updateStatus(String.valueOf(sTax.getServiceTaxId()), EntityStatus.DEACTIVATED);
			else
				sTaxViewList.add(mapFrom(sTax));
		});
		return sTaxViewList;
	}

	@Override
	public ServiceTaxView fetch(String serviceTaxId) {
		return mapFrom(serviceTaxDao.get(Long.parseLong(serviceTaxId)));
	}

	@Override
	public ServiceTaxView save(ServiceTaxView serviceTaxView) {
		try {
			if (serviceTaxView == null)
				throw new NullPointerException("Received object is null!");
			else if(serviceTaxView.getEffectiveFrom()==null)
				throw new NullPointerException("Effective from value cannot be null!");
			else if(serviceTaxView.getCode()==null)
				throw new NullPointerException("Tax Code value cannot be null!");

			/*ServiceTax maxDateServiceTax=selectMaxDate();
			if(maxDateServiceTax.getEffectiveFrom()!=null  && serviceTaxView.getCreatedBy()==null && serviceTaxView.getEffectiveFrom().compareTo(maxDateServiceTax.getEffectiveFrom())<=0) {
				throw new DataIntegrityViolationException("Start Date Should Be Greater Than Previous Start Dates!");
			}*/			
			serviceTaxView.setCrtnTs(CommonUtils.currentTimestamp());
			serviceTaxView.setCreatedBy(CommonUtils.getLoggedInUser());
			serviceTaxView.setEntityStatus(EntityStatus.PENDING_ACTIVATION);
			return mapFrom(serviceTaxDao.createOrUpdate(mapTo(serviceTaxView)));
		}
		catch(Exception e) {
			log.error("Error: ", e);
			throw e;
		}	
	}

	@Override
	public void delete(ServiceTaxView serviceTaxView) {
		serviceTaxDao.delete(mapTo(serviceTaxView));
	}
	
	@Override
	public List<ServiceTaxView> fetchAllActiveList() {
		List<ServiceTaxView> sTaxViewList = new ArrayList<>();
		for(ServiceTax sTax : serviceTaxDao.fetchAllActiveList()) {
			sTaxViewList.add(mapFrom(sTax));
		}
		return sTaxViewList;
	}
	
	private ServiceTax mapTo(ServiceTaxView serviceTaxView) {
		ServiceTax sTax = new ServiceTax();
		if(serviceTaxDao.get(serviceTaxView.getServiceTaxId())!=null)
			serviceTaxView.setServiceTaxId(serviceTaxView.getServiceTaxId());
		else
		serviceTaxView.setServiceTaxId(UUID.randomUUID().getLeastSignificantBits());
		sTax.setServiceTaxId(serviceTaxView.getServiceTaxId());
		sTax.setCode(serviceTaxView.getCode());
		sTax.setDescription(serviceTaxView.getDescription());
		sTax.setServiceAmtRangeMin(serviceTaxView.getServiceAmtRangeMin());
		sTax.setServiceAmtRangeMax(serviceTaxView.getServiceAmtRangeMax());
		sTax.setFlatTax(serviceTaxView.getFlatTax());
		sTax.setPercentTax(serviceTaxView.getPercentTax());
		sTax.setEffectiveFrom(serviceTaxView.getEffectiveFrom());
		sTax.setEffectiveTo(serviceTaxView.getEffectiveTo());
		sTax.setCrtnTs(serviceTaxView.getCrtnTs());
		sTax.setCreatedBy(serviceTaxView.getCreatedBy());
		sTax.setUpdatedBy(serviceTaxView.getUpdatedBy());
		sTax.setUpdtTs(serviceTaxView.getUpdtTs());
		sTax.setDeactivatedBy(serviceTaxView.getDeactivatedBy());
		sTax.setDeactivatedTs(serviceTaxView.getDeactivatedTs());
		sTax.setActivatedBy(serviceTaxView.getActivatedBy());
		sTax.setActivatedTs(serviceTaxView.getActivatedTs());
		if(serviceTaxView.getEntityStatus()!=null)
			sTax.setEntityStatus(serviceTaxView.getEntityStatus().name());
		return sTax;
	}
	
	private ServiceTaxView mapFrom(ServiceTax serviceTax) {
		if (null == serviceTax) {
			return null;
		}
		
		ServiceTaxView serviceTaxView = new ServiceTaxView();
		serviceTaxView.setServiceTaxId(serviceTax.getServiceTaxId());
		serviceTaxView.setCode(serviceTax.getCode());
		serviceTaxView.setDescription(serviceTax.getDescription());
		serviceTaxView.setServiceAmtRangeMin(serviceTax.getServiceAmtRangeMin() != null ? serviceTax.getServiceAmtRangeMin() : Long.MIN_VALUE);
		serviceTaxView.setServiceAmtRangeMax(serviceTax.getServiceAmtRangeMax() != null ? serviceTax.getServiceAmtRangeMax() : Long.MAX_VALUE);
		serviceTaxView.setFlatTax(serviceTax.getFlatTax());
		serviceTaxView.setPercentTax(serviceTax.getPercentTax());
		serviceTaxView.setEffectiveFrom(serviceTax.getEffectiveFrom());
		serviceTaxView.setEffectiveTo(serviceTax.getEffectiveTo());
		serviceTaxView.setCrtnTs(serviceTax.getCrtnTs());
		serviceTaxView.setCreatedBy(serviceTax.getCreatedBy());
		serviceTaxView.setUpdatedBy(serviceTax.getUpdatedBy());
		serviceTaxView.setUpdtTs(serviceTax.getUpdtTs());
		serviceTaxView.setDeactivatedBy(serviceTax.getDeactivatedBy());
		serviceTaxView.setDeactivatedTs(serviceTax.getDeactivatedTs());
		serviceTaxView.setActivatedBy(serviceTax.getActivatedBy());
		serviceTaxView.setActivatedTs(serviceTax.getActivatedTs());
		if(serviceTax.getEntityStatus()!=null)
		serviceTaxView.setEntityStatus(EntityStatus.valueOf(serviceTax.getEntityStatus()));
		return serviceTaxView;	
	}

	/*private ServiceTax selectMaxDate() {
		return serviceTaxDao.selectMaxDate();
	}
	*/
	@Override
	public ServiceTaxView update(ServiceTaxView serviceTaxcoming) {
		String METHOD_NAME = "update";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		try {
			if (serviceTaxcoming == null)
				throw new NullPointerException("Received object is null!");
			
			/*ServiceTax maxDateServiceTax = selectMaxDate();
			  if (maxDateServiceTax.getEffectiveFrom() != null
					&& serviceTaxcoming.getCreatedBy() == null
					&& serviceTaxcoming.getEffectiveFrom().compareTo(
							maxDateServiceTax.getEffectiveFrom()) <= 0) {
				throw new DataIntegrityViolationException(
						"Start Date Should Be Greater Than Previous Start Dates!");}*/
			
			serviceTaxcoming.setUpdatedBy(CommonUtils.getLoggedInUser());
			serviceTaxcoming.setUpdtTs(CommonUtils.currentTimestamp());
			if (serviceTaxDao.get(serviceTaxcoming.getServiceTaxId()) != null)
				return mapFrom(serviceTaxDao.createOrUpdate(mapTo(serviceTaxcoming)));
		}
		catch (Exception e) {
			log.error(METHOD_NAME, e);
			throw e;
		} 
		finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving " + METHOD_NAME);
			}
		}	
		return null;
	}

	@Override
	public List<ServiceTaxView> getServiceTax(String date) {
		String METHOD_NAME = "getServiceTax";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		try {
			List<ServiceTax> sTaxList = serviceTaxDao.getServiceTax(date);
			List<ServiceTaxView> sTaxViewList = new ArrayList<>();
			sTaxList.forEach( sTax-> {
				sTaxViewList.add(mapFrom(sTax));
			});
			return sTaxViewList;
		} catch (Exception e) {
			log.error(METHOD_NAME, e);
			throw e;
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving " + METHOD_NAME);
			}
		}
	}
	
	private void updateEndDate(Date startDate, Date endDate, String code) {
		try {
			ServiceTax serviceTax = serviceTaxDao.updateEndDate(CommonUtils.getFormattedDateyyyyMMdd(startDate),CommonUtils.getFormattedDateyyyyMMdd(endDate), code);
			if(serviceTax!=null) {
				serviceTax.setUpdatedBy(CommonUtils.getLoggedInUser());
				serviceTax.setUpdtTs(CommonUtils.currentTimestamp());
				serviceTax.setEffectiveTo(CommonUtils.getFormattedDateyyyyMMdd(endDate));
				serviceTaxDao.createOrUpdate(serviceTax);
			}
		}
		catch (Exception e) {
			log.error("Failed to update DB. " + e.getMessage() , e);
			throw e;
		}
	}

	private void updateEndDate(Date endDate, String code) {
		try {
			ServiceTax serviceTax = serviceTaxDao.updateEndDate(CommonUtils.getFormattedDateyyyyMMdd(endDate), code);
			if(serviceTax!=null) {
				serviceTax.setUpdatedBy(CommonUtils.getLoggedInUser());
				serviceTax.setUpdtTs(CommonUtils.currentTimestamp());
				serviceTax.setEffectiveTo(CommonUtils.getFormattedDateyyyyMMdd(endDate));
				serviceTaxDao.update(serviceTax);
			}
				
			/*serviceTax = serviceTaxDao.deactivateEndDate(endDate);
			if(serviceTax!=null)
				serviceTax.setUpdatedBy(CommonUtils.getLoggedInUser());
				serviceTax.setUpdtTs(CommonUtils.currentTimestamp());
				serviceTax.setEffectiveTo(null);
				serviceTax.setDeactivatedBy(CommonUtils.getLoggedInUser());
				serviceTax.setDeactivatedTs(CommonUtils.currentTimestamp());
				serviceTax.setEntityStatus(EntityStatus.DEACTIVATED.name());
				serviceTaxDao.createOrUpdate(serviceTax);*/
		
		}
		catch (Exception e) {
			log.error("Failed to update DB. " + e.getMessage() , e);
			throw e;
		}
	}
	
	private ServiceTaxView updateStatus(String serviceTaxId, EntityStatus status) {
		ServiceTax retrieved = serviceTaxDao.get(Long.parseLong(serviceTaxId));
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd"); 
		Date startDate = null;
		Date endDate = null;
		if (retrieved != null) {
			try {
				startDate = new Date(formatter.parse(retrieved.getEffectiveFrom()).getTime());
				if(retrieved.getEffectiveTo()!=null)
					endDate = new Date(formatter.parse(retrieved.getEffectiveTo()).getTime());
			} 
			catch (ParseException e) {
				log.error("Error: ", e);
			} 

		
			if (status == EntityStatus.ACTIVE) {
				updateEndDate(new Date(startDate.getTime() - 1000 * 60 * 60 * 24),retrieved.getCode());
			} 
			else if (status == EntityStatus.DEACTIVATED) {
/*				if (retrieved.getEntityStatus() != EntityStatus.PENDING_APPROVAL.name())
*/					updateEndDate(new Date(startDate.getTime()
							- 1000 * 60 * 60 * 24), endDate, retrieved.getCode());
			}
			retrieved.setEntityStatus(status.name());
			retrieved.setUpdtTs(CommonUtils.currentTimestamp());
			retrieved.setUpdatedBy(CommonUtils.getLoggedInUser());

			if (EntityStatus.ACTIVE == status) {
				retrieved.setActivatedBy(CommonUtils.getLoggedInUser());
				retrieved.setActivatedTs(CommonUtils.currentTimestamp());
			} 
			else if (EntityStatus.DEACTIVATED == status) {
				retrieved.setDeactivatedBy(CommonUtils.getLoggedInUser());
				retrieved.setDeactivatedTs(CommonUtils.currentTimestamp());
			}
			return mapFrom(serviceTaxDao.createOrUpdate(retrieved));
		}
		return null;
	}

	@Override
	public ServiceTaxView activate(String serviceTaxId) {
		return updateStatus(serviceTaxId, EntityStatus.ACTIVE);
	}

	@Override
	public ServiceTaxView deactivate(String serviceTaxId) {
		return updateStatus(serviceTaxId, EntityStatus.DEACTIVATED);
	}

	@Override
	public ServiceTaxView reject(String serviceTaxId) {
		return updateStatus(serviceTaxId, EntityStatus.REJECTED);
	}

	@Override
	public ServiceTaxView pendingDeactivate(String serviceTaxId) {
		return updateStatus(serviceTaxId, EntityStatus.PENDING_DEACTIVATION);
	}
}