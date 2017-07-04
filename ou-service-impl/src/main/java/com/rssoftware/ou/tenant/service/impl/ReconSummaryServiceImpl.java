package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.database.entity.tenant.ReconSummary;
import com.rssoftware.ou.model.tenant.ReconSummaryView;
import com.rssoftware.ou.tenant.dao.ReconSummaryDao;
import com.rssoftware.ou.tenant.service.ReconSummaryService;

@Service
public class ReconSummaryServiceImpl implements ReconSummaryService {

	@Autowired
	private ReconSummaryDao reconSummaryDao;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	//based on start date and end date fetch recon summary
	@Override
	public List<ReconSummaryView> getReconList(String startDate, String endDate) throws IOException {
		String METHOD_NAME = "getReconList";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		List<ReconSummaryView> reconSummaryList = null;

		try {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date startDatee = (Date) formatter.parse(startDate);
			Date endDatee = (Date) formatter.parse(endDate);
			Calendar c = Calendar.getInstance();
			c.setTime(endDatee);
			c.add(Calendar.DATE, 1);
			endDatee = c.getTime();
			reconSummaryList = mapFrom(reconSummaryDao.getReconList(
					new Timestamp(startDatee.getTime()),
					new Timestamp(endDatee.getTime())));
		} 
		catch (Exception e) {
			log.error("Error " + e);
			throw new IOException(e);
		}
		return reconSummaryList;
	}

	// for inserting into RECON_SUMMARY table
	@Override
	public void insert(ReconSummaryView reconSummaryView) throws IOException {
		String METHOD_NAME = "insert";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		try {
			reconSummaryDao.create(mapFrom(reconSummaryView));
		} 
		catch (Exception e) {
			log.error("Error " + e);
			throw new IOException(e);
		}
		finally {
			if (log.isDebugEnabled()){
				log.debug("Leaving "+METHOD_NAME);
			}
		}
	}
	
	//to map from ReconSummaryView to ReconSummary
	private ReconSummary mapFrom(ReconSummaryView reconSummaryView) {
		String METHOD_NAME = "mapFrom";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		ReconSummary reconSummary = new ReconSummary();
		reconSummary.setReconId(reconSummaryView.getReconId());
		reconSummary.setBroughtForwardCount(reconSummaryView
				.getBroughtForwardCount());
		reconSummary.setMatchedCount(reconSummaryView.getMatchedCount());
		reconSummary.setMismatchedCount(reconSummaryView.getMismatchedCount());
		reconSummary.setNotInCUCount(reconSummaryView.getNotInCUCount());
		reconSummary.setNotInOUCount(reconSummaryView.getNotInOUCount());
		reconSummary.setPendingCount(reconSummaryView.getPendingCount());
		reconSummary.setReconTs(reconSummaryView.getReconTs());
		return reconSummary;
	}

	//to map from List<ReconSummary> to List<ReconSummaryView>
	private List<ReconSummaryView> mapFrom(List<ReconSummary> reconList) {
		String METHOD_NAME = "mapFrom";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		
		Iterator<ReconSummary> it = reconList.iterator();
		List<ReconSummaryView> domainList = new LinkedList<ReconSummaryView>();
		ReconSummary reconSummary = null;
		ReconSummaryView reconSummaryView = null;
		while (it.hasNext()) {
			reconSummaryView = new ReconSummaryView();
			reconSummary = (ReconSummary) it.next();
			reconSummaryView.setReconId(reconSummary.getReconId());
			reconSummaryView.setBroughtForwardCount(reconSummary
					.getBroughtForwardCount());
			reconSummaryView.setMatchedCount(reconSummary.getMatchedCount());
			reconSummaryView.setMismatchedCount(reconSummary
					.getMismatchedCount());
			reconSummaryView.setNotInCUCount(reconSummary.getNotInCUCount());
			reconSummaryView.setNotInOUCount(reconSummary.getNotInOUCount());
			reconSummaryView.setPendingCount(reconSummary.getPendingCount());
			reconSummaryView.setReconTs(reconSummary.getReconTs());
			domainList.add(reconSummaryView);
		}
		return domainList;
	}

}
