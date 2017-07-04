package com.rssoftware.ou.tenant.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.AgentScheme;
import com.rssoftware.ou.database.entity.tenant.SchemeAvlblityDetail;
import com.rssoftware.ou.database.entity.tenant.SchemeCommissionDetail;
import com.rssoftware.ou.model.tenant.AgentSchemeView;
import com.rssoftware.ou.model.tenant.SchemeCommission;
import com.rssoftware.ou.tenant.dao.AgentSchemeAvailabilityDao;
import com.rssoftware.ou.tenant.dao.AgentSchemeDao;
import com.rssoftware.ou.tenant.dao.SchemeCommissionDetailsDao;
import com.rssoftware.ou.tenant.service.AgentSchemeService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;

@Service
public class AgentSchemeServiceImpl implements AgentSchemeService {

	@Autowired
	private AgentSchemeDao agentSchemeDao;

	@Autowired
	private SchemeCommissionDetailsDao schemeCommissionDetailsDao;

	@Autowired
	private AgentSchemeAvailabilityDao agentSchemeAvailabilityDao;

	@Autowired
	IDGeneratorService idGeneratorService;

	@Override
	public AgentSchemeView getAgentSchemeById(String agentSchemeId) throws ValidationException {

		AgentScheme agentScheme = agentSchemeDao.getScheme(agentSchemeId);
		List<SchemeCommissionDetail> agentSchemeDetails = null;
		List<SchemeAvlblityDetail> agentAvailabilityDetails = null;
		if(agentScheme!=null) {
			agentSchemeDetails = schemeCommissionDetailsDao.getAllBySchemeId(agentScheme.getSchemeUniqueId());
			agentAvailabilityDetails = agentSchemeAvailabilityDao.getAllBySchemeId(agentScheme.getSchemeUniqueId());
			return mapTo(agentScheme, agentSchemeDetails, agentAvailabilityDetails);
		}
		else {
			throw ValidationException.getInstance(ValidationErrorReason.REQUEST_NOT_FOUND, "SchemeId does not exist!");
		}

	}

	@Override
	public List<AgentSchemeView> getAllAgentSchemes() {

		List<AgentScheme> agentSchemes = agentSchemeDao.getAllSchemes();
		List<AgentSchemeView> schemeViews = new ArrayList<AgentSchemeView>();
		for (AgentScheme scheme : agentSchemes) {
			List<SchemeCommissionDetail> schemeCommissionDetails = schemeCommissionDetailsDao
					.getAllBySchemeId(scheme.getSchemeUniqueId());
			List<SchemeAvlblityDetail> agentAvailabilityDetails = agentSchemeAvailabilityDao
					.getAllBySchemeId((scheme.getSchemeUniqueId()));
			schemeViews.add(mapTo(scheme, schemeCommissionDetails, agentAvailabilityDetails));
		}
		return schemeViews;

	}

	@Override
	public List<AgentSchemeView> getAgentSchemesByInstId(String agentInstId) {

		// Alternative Logic:
		// 1. agentSchemeAvailability get with agentInstId
		// 2. get agentSchemes with condition: result of step1 + any plan
		// effectiveForAll + Validity test
		// 3. get agentfeeDetails with schemeUniqueId

		List<SchemeAvlblityDetail> agentAvailabilityDetails = agentSchemeAvailabilityDao
				.getAllByAgentInstituteId(agentInstId);
		List<AgentScheme> agentSchemes = agentSchemeDao.getSchemeForAgentInst(agentAvailabilityDetails);
		List<AgentSchemeView> schemeViews = new ArrayList<AgentSchemeView>();
		for (AgentScheme scheme : agentSchemes) {
			List<SchemeCommissionDetail> schemeCommissionDetails = schemeCommissionDetailsDao
					.getAllBySchemeId(scheme.getSchemeUniqueId());
			List<SchemeAvlblityDetail> agentAvailabilityDetail = agentSchemeAvailabilityDao
					.getAllBySchemeId((scheme.getSchemeUniqueId()));
			schemeViews.add(mapTo(scheme, schemeCommissionDetails, agentAvailabilityDetail));
		}
		return schemeViews;

		/*
		 * List<AgentScheme> agentSchemes = agentSchemeDao.getAllSchemes();
		 * List<AgentScheme> resultAgentSchemes = new ArrayList<AgentScheme>();
		 * 
		 * for(AgentScheme agentScheme:agentSchemes) {
		 * if(agentScheme.isSchemeEffctvForAll()) {
		 * resultAgentSchemes.add(agentScheme); } else {
		 * List<AgentSchemeAvailabilityDetail> agentAvailabilityDetails =
		 * agentSchemeAvailabilityDao.getAllBySchemeId(agentScheme.getSchemeId()
		 * ); for(AgentSchemeAvailabilityDetail
		 * availabilityDetail:agentAvailabilityDetails) {
		 * if(availabilityDetail.getId().getAgentInstId().equals(agentInstId)) {
		 * resultAgentSchemes.add(agentScheme); break; } } } }
		 * 
		 * List<AgentSchemeView> schemeViews = new ArrayList<AgentSchemeView>();
		 * for (AgentScheme scheme : resultAgentSchemes) {
		 * List<AgentSchemeFeeDetail> agentSchemeDetails =
		 * agentSchemeFeeDetailDao.getAllBySchemeId(scheme.getSchemeUniqueId());
		 * List<AgentSchemeAvailabilityDetail> agentAvailabilityDetails =
		 * agentSchemeAvailabilityDao.getAllBySchemeId(scheme.getSchemeId());
		 * schemeViews.add(mapTo(scheme, agentSchemeDetails,
		 * agentAvailabilityDetails)); } return schemeViews;
		 */
	}

	@Override
	public AgentSchemeView save(AgentSchemeView agentSchemeView) throws ValidationException {
		// Validations:
		// 1. Plan name existing or not?
		// 2. if yes, update the existing effetiveTo with a day before given
		// EffectiveFrom or today. If no, generate plan
		try {
			AgentScheme scheme = mapSchemeFromView(agentSchemeView);
			AgentScheme savedScheme = agentSchemeDao.createOrUpdate(scheme);
			List<SchemeCommissionDetail> feeDetails = mapSchemeDetailFromView(agentSchemeView,
					savedScheme.getSchemeUniqueId());
			for (SchemeCommissionDetail feeDetail : feeDetails) {
				schemeCommissionDetailsDao.createOrUpdate(feeDetail);
			}
			if (!savedScheme.isSchemeEffctvForAll()) {
				// List<AgentSchemeAvailabilityDetail> availabilityDetails =
				// mapSchemeAvailabilityDetailFromView(agentSchemeView.getAgentInstIds(),
				// agentSchemeView.getSchemeId());
				agentSchemeAvailabilityDao.saveAll(agentSchemeView.getAgentInstIds(), scheme.getSchemeUniqueId());
			}
			return agentSchemeView;
		}
		catch (ValidationException ve) {
			Log.error("Error: ", ve);
			throw ve;

		}
		catch (Exception e) {
			Log.error("Error: ", e);
			return null;
		}
	}

	private AgentScheme mapSchemeFromView(AgentSchemeView agentSchemeView) throws ValidationException{
		AgentScheme agentScheme = new AgentScheme();
		agentScheme.setSchemeUniqueId(idGeneratorService.getUniqueID(5, ""));
		AgentScheme scheme = agentSchemeDao.getLastScheme(agentSchemeView.getSchemeName());
		try {
			//List<AgentScheme> schemes = agentSchemeDao.getSchemeBySchemeName(agentSchemeView.getSchemeName());
			if (scheme == null) {
				agentScheme.setSchemeId(idGeneratorService.getUniqueID(5, TransactionContext.getTenantId() + "PL"));
			} else {
				agentScheme.setSchemeId(scheme.getSchemeId());
				if(CommonUtils.getFormattedDateyyyyMMdd(new Date()).compareTo(agentSchemeView.getSchemeEffctvFrom())>=0 || scheme.getSchemeEffctvFrom().compareTo(agentSchemeView.getSchemeEffctvFrom())>=0) {
					throw ValidationException.getInstance(ValidationErrorReason.INVALID_STATE, "Effective-From Date Should Be Greater Than Today And Previous Effective-From dates!");
				}
				
				Date effectvToDate = new Date(
						CommonUtils.parseDateyyyyMMdd(agentSchemeView.getSchemeEffctvFrom()).getTime()
								- 24 * 60 * 60 * 1000);
				agentSchemeDao.updateLastScheme(agentScheme.getSchemeId(),
						CommonUtils.getFormattedDateyyyyMMdd(effectvToDate));
			}

			agentScheme.setSchemeName(agentSchemeView.getSchemeName());
			agentScheme.setSchemeDesc(agentSchemeView.getSchemeDesc());
			if (agentSchemeView.getAgentInstIds().isEmpty())
				agentScheme.setSchemeEffctvForAll(true);
			else
				agentScheme.setSchemeEffctvForAll(false);
			agentScheme.setSchemeEffctvFrom(agentSchemeView.getSchemeEffctvFrom());
			agentScheme.setSchemeEffctvTo(agentSchemeView.getSchemeEffctvTo());
			agentScheme.setCrtnTs(CommonUtils.currentTimestamp());
			agentScheme.setCrtnUserId(CommonUtils.getLoggedInUser());
		} 
		catch (ValidationException ve) {
			Log.error("Error: ", ve);
			throw ve;

		}
		catch (Exception e) {
			Log.error("Error: ", e);
		}
		return agentScheme;
	}

	private List<SchemeCommissionDetail> mapSchemeDetailFromView(AgentSchemeView agentSchemeView,
			String schemeUniqueId) {
		List<SchemeCommissionDetail> schemeCommissionDetails = new ArrayList<>();
		try {
			List<SchemeCommission> agentSchemeCommissions = agentSchemeView.getSchemeCommissions();
			for (SchemeCommission commissiondetail : agentSchemeCommissions) {
				SchemeCommissionDetail schemeCommissionDetail = new SchemeCommissionDetail();
				schemeCommissionDetail.setAgentFlatFee(commissiondetail.getAgentFlatFee());
				schemeCommissionDetail.setAgentPercentFee(commissiondetail.getAgentPercentFee());
				schemeCommissionDetail.setAiPercentFee(commissiondetail.getAiPercentFee());
				schemeCommissionDetail.setSchemeUniqueId(schemeUniqueId);
				schemeCommissionDetail.setTranAmtRangeMax(commissiondetail.getTranAmtRangeMax());
				schemeCommissionDetail.setTranAmtRangeMin(commissiondetail.getTranAmtRangeMin());
				schemeCommissionDetail.setTranCountRangeMax(commissiondetail.getTranCountRangeMax());
				schemeCommissionDetail.setTranCountRangeMin(commissiondetail.getTranCountRangeMin());
				schemeCommissionDetail.setCommissionId(UUID.randomUUID().getLeastSignificantBits());
				schemeCommissionDetails.add(schemeCommissionDetail);
			}

		} catch (Exception e) {
			Log.error("Error: ", e);
		}
		return schemeCommissionDetails;
	}

	@Override
	public AgentSchemeView delete(String agentSchemeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgentSchemeView update(AgentSchemeView agentSchemeView) throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}

	private AgentSchemeView mapTo(AgentScheme agentScheme, List<SchemeCommissionDetail> agentSchemeFeeDetails, List<SchemeAvlblityDetail> agentAvailabilityDetails) {

		AgentSchemeView agentSchemeView = new AgentSchemeView();
		//agentSchemeView.setSchemeUniqueId(agentScheme.getSchemeUniqueId());
		agentSchemeView.setSchemeId(agentScheme.getSchemeId());
		agentSchemeView.setSchemeDesc(agentScheme.getSchemeDesc());
		agentSchemeView.setSchemeEffctvFrom(agentScheme.getSchemeEffctvFrom());
		agentSchemeView.setSchemeEffctvTo(agentScheme.getSchemeEffctvTo());
		agentSchemeView.setSchemeName(agentScheme.getSchemeName());
		agentSchemeView.setSchemeEffctvForAll(agentScheme.isSchemeEffctvForAll());
		List<SchemeCommission> agentSchemeFees = new ArrayList<SchemeCommission>();

		for (SchemeCommissionDetail agentSchemeFeeDetail : agentSchemeFeeDetails) {
			SchemeCommission agentSchemeFee = new SchemeCommission();
			agentSchemeFee.setAgentFlatFee(agentSchemeFeeDetail.getAgentFlatFee());
			agentSchemeFee.setAgentPercentFee(agentSchemeFeeDetail.getAgentFlatFee());
			agentSchemeFee.setAiPercentFee(agentSchemeFeeDetail.getAiPercentFee());
			agentSchemeFee.setTranAmtRangeMax(agentSchemeFeeDetail.getTranAmtRangeMax());
			agentSchemeFee.setTranAmtRangeMin(agentSchemeFeeDetail.getTranAmtRangeMin());
			agentSchemeFees.add(agentSchemeFee);
		}
		agentSchemeView.setSchemeCommissions(agentSchemeFees);
		List<String> list = new ArrayList<String>();
		if (!agentScheme.isSchemeEffctvForAll()) {
			for (SchemeAvlblityDetail agentSchemeAvailabilityDetail : agentAvailabilityDetails) {
				list.add(agentSchemeAvailabilityDetail.getId().getAgentInstId());
				//agentSchemeView.getAgentInstIds().add(agentSchemeAvailabilityDetail.getId().getAgentInstId());
			}
		}
		agentSchemeView.setAgentInstIds(list);
		return agentSchemeView;

	}
}
