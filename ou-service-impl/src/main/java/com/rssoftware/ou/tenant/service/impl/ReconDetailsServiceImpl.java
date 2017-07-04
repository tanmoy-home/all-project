package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.CacheMode;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.database.entity.tenant.ReconDetails;
import com.rssoftware.ou.database.entity.tenant.ReconDetailsPK;
import com.rssoftware.ou.model.tenant.ReconDetailsView;
import com.rssoftware.ou.model.tenant.ReconDetailsView.ReconStatus;
import com.rssoftware.ou.model.tenant.ReconSummaryView;
import com.rssoftware.ou.model.tenant.ReconView;
import com.rssoftware.ou.tenant.dao.ReconDetailsDao;
import com.rssoftware.ou.tenant.service.ReconDetailsService;

@Service
public class ReconDetailsServiceImpl implements ReconDetailsService {
	private final ConcurrentHashMap<String, SessionFactory> sessionFactoryCache = new ConcurrentHashMap<>();

	@Autowired
	private ReconDetailsDao reconDetailsDao;
	private final Logger log = LoggerFactory.getLogger(getClass());

	// for inserting into RECON_DETAILS table
	@Override
	public void insert(ReconDetailsView reconDetailsView) throws IOException {
		String METHOD_NAME = "insert";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		try {
			reconDetailsDao.create(mapFrom(reconDetailsView));
		} catch (Exception e) {
			log.error("Error " + e);
			throw new IOException(e);
		} finally {
			if (log.isDebugEnabled()) {
				log.debug("Leaving " + METHOD_NAME);
			}
		}
	}

	// to form ReconDetails from ReconDetailsView
	private ReconDetails mapFrom(ReconDetailsView reconDetailsView) {
		String METHOD_NAME = "mapFrom";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		ReconDetails reconDetails = new ReconDetails();
		ReconDetailsPK reconDetailsPK = new ReconDetailsPK();
		reconDetailsPK.setReconId(reconDetailsView.getReconId());
		reconDetailsPK.setRefId(reconDetailsView.getRefId());
		reconDetailsPK.setTxnType(reconDetailsView.getRequestType().name());
		reconDetails.setId(reconDetailsPK);
		reconDetails.setTxnRefId(reconDetailsView.getTxnRefId());
		reconDetails.setAgentId(reconDetailsView.getAgentId());
		reconDetails
				.setReconDescription(reconDetailsView.getReconDescription());
		reconDetails.setReconStatus(reconDetailsView.getReconStatus().name());
		return reconDetails;
	}

	// based on recon summary fetches recon details and populates the reconView
	@Override
	public ReconView getReconDetailsList(ReconSummaryView reconSummaryView) {
		String METHOD_NAME = "getReconDetailsList";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		ReconView reconView = new ReconView();

		reconView.setReconId(reconSummaryView.getReconId());
		reconView.setMatchedCount(reconSummaryView.getMatchedCount());
		reconView.setMismatchedCount(reconSummaryView.getMismatchedCount());
		reconView.setNotInCUCount(reconSummaryView.getNotInCUCount());
		reconView.setNotInOUCount(reconSummaryView.getNotInOUCount());
		reconView.setPendingCount(reconSummaryView.getPendingCount());
		reconView.setBroughtForwardCount(reconSummaryView
				.getBroughtForwardCount());
		reconView.setReconTs(reconSummaryView.getReconTs());
		List<ReconDetails> reconDetails = reconDetailsDao
				.getReconDetailsList(reconSummaryView.getReconId());
		Iterator it = reconDetails.iterator();
		ReconDetails entity = null;
		List<ReconDetails> notInOuList = new LinkedList<ReconDetails>();
		List<ReconDetails> notInCuList = new LinkedList<ReconDetails>();
		List<ReconDetails> mismatchedList = new LinkedList<ReconDetails>();
		List<ReconDetails> pendingList = new LinkedList<ReconDetails>();
		List<ReconDetails> broughtForwardList = new LinkedList<ReconDetails>();
		while (it.hasNext()) {
			entity = (ReconDetails) it.next();
			if (entity.getReconStatus().equals(
					ReconStatus.BROUGHT_FORWARD.name()))
				broughtForwardList.add(entity);
			else if (entity.getReconStatus().equals(ReconStatus.PENDING.name()))
				pendingList.add(entity);
			else if (entity.getReconStatus().equals(
					ReconStatus.NOT_IN_CU.name()))
				notInCuList.add(entity);
			else if (entity.getReconStatus().equals(
					ReconStatus.NOT_IN_OU.name()))
				notInOuList.add(entity);
			else
				mismatchedList.add(entity);
		}
		reconView.setBroughtForwardList(broughtForwardList);
		reconView.setMismatchedList(mismatchedList);
		reconView.setNotInCuList(notInCuList);
		reconView.setNotInOuList(notInOuList);
		reconView.setPendingList(pendingList);
		return reconView;
	}

	// based on recon summary fetches recon details and populates the reconView based on agent or biller
	@Override
	public ReconView getReconDetailsList(ReconSummaryView reconSummaryView,
			String id, int billerOrAgent) {
		ReconView reconView = null;
		int broughtForwardTransactions = 0;
		int mismatchNotInOu = 0;
		int mismatchNotInCu = 0;
		int mismatchFields = 0;
		int pending = 0;

		List<ReconDetails> reconDetails = reconDetailsDao
				.getReconDetailsList(reconSummaryView.getReconId());
		Iterator it = reconDetails.iterator();
		ReconDetails entity = null;
		int flag = 0;
		List<ReconDetails> notInOuList = new LinkedList<ReconDetails>();
		List<ReconDetails> notInCuList = new LinkedList<ReconDetails>();
		List<ReconDetails> mismatchedList = new LinkedList<ReconDetails>();
		List<ReconDetails> pendingList = new LinkedList<ReconDetails>();
		List<ReconDetails> broughtForwardList = new LinkedList<ReconDetails>();
		while (it.hasNext()) {
			entity = (ReconDetails) it.next();
			if (billerOrAgent == 0) {
				if (id.equals(entity.getAgentId())) {
					flag = 1;
					if (entity.getReconStatus().equals(
							ReconStatus.BROUGHT_FORWARD.name())) {
						broughtForwardList.add(entity);
						broughtForwardTransactions++;
					} else if (entity.getReconStatus().equals(
							ReconStatus.PENDING.name())) {
						pendingList.add(entity);
						pending++;
					} else if (entity.getReconStatus().equals(
							ReconStatus.NOT_IN_CU.name())) {
						notInCuList.add(entity);
						mismatchNotInCu++;
					} else if (entity.getReconStatus().equals(
							ReconStatus.NOT_IN_OU.name())) {
						notInOuList.add(entity);
						mismatchNotInOu++;
					} else {
						mismatchedList.add(entity);
						mismatchFields++;
					}
				}
			} else if (billerOrAgent == 1) {
				if (entity.getBillerId().equals(id) && entity.getAgentId()==null) {
					flag = 1;
					if (entity.getReconStatus().equals(
							ReconStatus.BROUGHT_FORWARD.name())) {
						broughtForwardList.add(entity);
						broughtForwardTransactions++;
					} else if (entity.getReconStatus().equals(
							ReconStatus.PENDING.name())) {
						pendingList.add(entity);
						pending++;
					} else if (entity.getReconStatus().equals(
							ReconStatus.NOT_IN_CU.name())) {
						notInCuList.add(entity);
						mismatchNotInCu++;
					} else if (entity.getReconStatus().equals(
							ReconStatus.NOT_IN_OU.name())) {
						notInOuList.add(entity);
						mismatchNotInOu++;
					} else {
						mismatchedList.add(entity);
						mismatchFields++;
					}
				}
			}
		}
		if (flag == 1) {
			reconView = new ReconView();
			reconView.setReconId(reconSummaryView.getReconId());
			// reconView.setMatchedCount(reconSummaryView.getMatchedCount());
			reconView.setMismatchedCount(new BigDecimal(mismatchFields));
			reconView.setNotInCUCount(new BigDecimal(mismatchNotInCu));
			reconView.setNotInOUCount(new BigDecimal(mismatchNotInOu));
			reconView.setPendingCount(new BigDecimal(pending));
			reconView.setBroughtForwardCount(new BigDecimal(
					broughtForwardTransactions));
			reconView.setReconTs(reconSummaryView.getReconTs());
			reconView.setBroughtForwardList(broughtForwardList);
			reconView.setMismatchedList(mismatchedList);
			reconView.setNotInCuList(notInCuList);
			reconView.setNotInOuList(notInOuList);
			reconView.setPendingList(pendingList);
		}
		return reconView;
	}
}