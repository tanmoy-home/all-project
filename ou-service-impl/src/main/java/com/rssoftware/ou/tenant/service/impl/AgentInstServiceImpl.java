package com.rssoftware.ou.tenant.service.impl;

import in.co.rssoftware.bbps.schema.AgentInst;
import in.co.rssoftware.bbps.schema.AgentInstList;

import java.util.ArrayList;
import java.util.List;

import org.bbps.schema.Ack;
import org.bbps.schema.ErrorMessage;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.ExcelUtil;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.tenant.AgentInstDetail;
import com.rssoftware.ou.model.tenant.AgentInstView;
import com.rssoftware.ou.model.tenant.ContactDetailsView;
import com.rssoftware.ou.model.tenant.ContactDetailsView.ContactType;
import com.rssoftware.ou.model.tenant.ContactDetailsView.LinkedEntityType;
import com.rssoftware.ou.tenant.dao.AgentInstDao;
import com.rssoftware.ou.tenant.service.AgentInstService;
import com.rssoftware.ou.tenant.service.ContactDetailsService;

@Service
public class AgentInstServiceImpl implements AgentInstService {

	@Autowired
	AgentInstDao agentInstDao;

	@Autowired
	ContactDetailsService contactDetailsService;

	@Override
	public AgentInstView getAgentInstById(String agentInstId) {
		AgentInstDetail agentInstDetail = agentInstDao.get(agentInstId);
		return mapFrom(agentInstDetail);
	}

	private AgentInstDetail validateRetrieveForSubmit(AgentInstView aiv)
			throws ValidationException {
		List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>(1);

		ErrorMessage error = null;
		if (aiv == null) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Request object cannot be null!");
			errorMessages.add(error);
		}

		if (aiv != null) {

			if (!CommonUtils.hasValue(aiv.getAgentInstId())) {
				error = new ErrorMessage();
				error.setErrorCd(ValidationErrorReason.NULL.name());
				error.setErrorDtl("ID value can't be Empty!");
				errorMessages.add(error);
			} else if (CommonUtils.hasValue(aiv.getAgentInstId())) {

				AgentInstView agentInstView = getAgentInstById(aiv
						.getAgentInstId());
				if (agentInstView != null) {
					EntityStatus status = agentInstView.getEntityStatus();

					if (!(status == EntityStatus.DRAFT)
							&& !(status == EntityStatus.REJECTED)) {
						error = new ErrorMessage();
						error.setErrorCd(ValidationErrorReason.RECORD_EXIST
								.name());
						error.setErrorDtl("Record with ID: '"
								+ agentInstView.getAgentInstId()
								+ "' is already exist, please choose different ID!");
						errorMessages.add(error);
					}
				}
			}
		}

		if (aiv.getAgentInstId().length() != 4) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.LENGTH_OF_ID_MISMATCH.name());
			error.setErrorDtl("Agent Inst. Id should be of 4 Character!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstName())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Name can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstAliasName())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Alias Name can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstType())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Type can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstBusnsType())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Business Type can't be Empty!");
			errorMessages.add(error);
		}

		if (!CommonUtils.hasValue(aiv.getAgentInstTanNo())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Tan No can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstRegAdrLine1())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Registered Address can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstRegCity())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Registered City can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstRegState())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Registered State can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstRegPinCode())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Registered Pincode can't be Empty!");
			errorMessages.add(error);
		}
		if (!CommonUtils.hasValue(aiv.getAgentInstRegCountry())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Agent Inst Registered Country can't be Empty!");
			errorMessages.add(error);
		}

		if (!errorMessages.isEmpty()) {
			Ack ack = new Ack();
			ack.getErrorMessages().addAll(errorMessages);
			throw ValidationException.getInstance(ack);
		}

		return mapTo(aiv);
	}

	@Override
	public void submit(AgentInstView agentInstView) throws ValidationException {

		AgentInstDetail aInst = validateRetrieveForSubmit(agentInstView);
		aInst.setCrtnTs(CommonUtils.currentTimestamp());
		aInst.setCrtnUserId(CommonUtils.getLoggedInUser());
		aInst.setEntityStatus(EntityStatus.PENDING_ACTIVATION.name());
		if (agentInstView.getContactDetailsView1stLevel() != null) {
			ContactDetailsView cont1 = agentInstView
					.getContactDetailsView1stLevel();
			cont1.setLinkedEntityID(aInst.getAgentInstId());
			cont1.setLinkedEntityType(LinkedEntityType.AGENT_INSTITUTE);
			cont1.setContactType(ContactType.TECH_1);
			contactDetailsService.submit(cont1);
		}
		if (agentInstView.getContactDetailsView2ndtLevel() != null) {
			ContactDetailsView cont2 = agentInstView
					.getContactDetailsView2ndtLevel();
			cont2.setLinkedEntityID(aInst.getAgentInstId());
			cont2.setLinkedEntityType(LinkedEntityType.AGENT_INSTITUTE);
			cont2.setContactType(ContactType.TECH_2);
			contactDetailsService.submit(cont2);
		}
		// need to insert
		agentInstDao.createOrUpdate(aInst);
	}

	private AgentInstDetail validateRetrieveForUpdate(AgentInstView aiv)
			throws ValidationException {
		List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();
		ErrorMessage error = null;
		if (aiv == null || !CommonUtils.hasValue(aiv.getAgentInstId())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("ID value can't be Empty!");
			errorMessages.add(error);
		}

		AgentInstView agentInst = getAgentInstById(aiv.getAgentInstId());

		if (agentInst == null) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("This is in Invalid State!");
			errorMessages.add(error);

		}

		if (agentInst.getEntityStatus().equals(EntityStatus.ACTIVE.name())) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.INVALID_STATE.name());
			error.setErrorDtl("This is in Active State and can't be updated!");
			errorMessages.add(error);
		}

		if (errorMessages.isEmpty()) {
			Ack ack = new Ack();
			ack.getErrorMessages().addAll(errorMessages);
			throw ValidationException.getInstance(ack);
		}

		return mapTo(agentInst);
	}

	@Override
	public void save(AgentInstView agentInstView) {
		AgentInstDetail agentInstDetail = mapTo(agentInstView);
		agentInstDao.createOrUpdate(agentInstDetail);
	}

	@Override
	public void delete(String agentInstId) {
		AgentInstDetail agentInst = agentInstDao.get(agentInstId);
		agentInstDao.delete(agentInst);
	}

	private AgentInstView mapFrom(AgentInstDetail agentInstDetail) {
		if (null == agentInstDetail) {
			return null;
		}
		AgentInstView agentInstView = new AgentInstView();
		/*
		 * if (null == agentInstDetail) { agentInstDetail = new
		 * AgentInstDetail();
		 * agentInstDetail.setCrtnTs(CommonUtils.currentTimestamp());
		 * agentInstDetail.setCrtnUserId(CommonUtils.getLoggedInUser()); } else
		 * { agentInstDetail.setUpdtTs(CommonUtils.currentTimestamp());
		 * agentInstDetail.setUpdtUserId(CommonUtils.getLoggedInUser()); }
		 */
		agentInstView.setAgentInstId(agentInstDetail.getAgentInstId());
		agentInstView.setAgentInstType(agentInstDetail.getAgentInstType());
		agentInstView.setAgentInstBusnsType(agentInstDetail
				.getAgentInstBusnsType());
		agentInstView.setAgentInstName(agentInstDetail.getAgentInstName());
		agentInstView.setAgentInstAliasName(agentInstDetail
				.getAgentInstAliasName());
		agentInstView.setAgentInstTanNo(agentInstDetail.getAgentInstTanNo());
		String agentInstRegAdrLine = agentInstDetail.getAgentInstRegAdrline();
		if (agentInstRegAdrLine != null && !agentInstRegAdrLine.isEmpty()) {
			if (agentInstRegAdrLine.contains("" + System.lineSeparator())) {

				String[] split = agentInstRegAdrLine.split(""
						+ System.lineSeparator());
				if (split.length > 0) {
					int len = split.length;

					if (len == 1 && split[0] != null) {
						agentInstView.setAgentInstRegAdrLine1(split[0]);
					}
					if (len == 2 && split[1] != null) {
						agentInstView.setAgentInstRegAdrLine1(split[0]);
						agentInstView.setAgentInstRegAdrLine2(split[1]);
					}
					if (len == 3 && split[2] != null) {
						agentInstView.setAgentInstRegAdrLine1(split[0]);
						agentInstView.setAgentInstRegAdrLine2(split[1]);
						agentInstView.setAgentInstRegAdrLine3(split[2]);
					}
				}
			} else {
				agentInstView.setAgentInstRegAdrLine1(agentInstRegAdrLine
						.trim());
			}
		}
		agentInstView
				.setAgentInstRegCity(agentInstDetail.getAgentInstRegCity());
		agentInstView.setAgentInstRegPinCode(agentInstDetail
				.getAgentInstRegPinCode());
		agentInstView.setAgentInstRegState(agentInstDetail
				.getAgentInstRegState());
		agentInstView.setAgentInstRegCountry(agentInstDetail
				.getAgentInstRegCountry());
		String agentInstComnAddrLine = agentInstDetail
				.getAgentInstCommAdrline();
		if (agentInstComnAddrLine != null && !agentInstComnAddrLine.isEmpty()) {
			if (agentInstComnAddrLine.contains(String.valueOf(System
					.lineSeparator()))) {

				String[] split = agentInstComnAddrLine.split(String
						.valueOf(System.lineSeparator()));
				if (split.length > 0) {

					int len = split.length;

					if (len == 1 && split[0] != null) {
						agentInstView.setAgentInstCommAdrLine1(split[0]);
					}
					if (len == 2 && split[1] != null) {
						agentInstView.setAgentInstCommAdrLine1(split[0]);
						agentInstView.setAgentInstCommAdrLine2(split[1]);
					}
					if (len == 3 && split[2] != null) {
						agentInstView.setAgentInstCommAdrLine1(split[0]);
						agentInstView.setAgentInstCommAdrLine2(split[1]);
						agentInstView.setAgentInstCommAdrLine3(split[2]);
					}
				}
			} else {
				agentInstView.setAgentInstCommAdrLine1(agentInstComnAddrLine
						.trim());
			}
		}
		agentInstView.setAgentInstCommCity(agentInstDetail
				.getAgentInstCommCity());
		agentInstView.setAgentInstCommPinCode(agentInstDetail
				.getAgentInstCommPinCode());
		agentInstView.setAgentInstCommState(agentInstDetail
				.getAgentInstCommState());
		agentInstView.setAgentInstCommCountry(agentInstDetail
				.getAgentInstCommCountry());

		agentInstView.setAgentInstEffctvFrom(agentInstDetail
				.getAgentInstEffctvFrom());
		agentInstView.setAgentInstEffctvTo(agentInstDetail
				.getAgentInstEffctvTo());
		agentInstView.setAgentInstUaadhaar(agentInstDetail
				.getAgentInstUaadhaar());
		agentInstView.setAgentInstRocUin(agentInstDetail.getAgentInstRocUin());
		agentInstView.setEntityStatus(EntityStatus.valueOf(agentInstDetail
				.getEntityStatus()));

		agentInstView.setBusAddrAuthLetter(agentInstDetail
				.getBusAddrAuthLetter());
		agentInstView.setBusAddrAuthLetterScanCopy(agentInstDetail
				.getBusAddrAuthLetterScanCopy());
		agentInstView.setLicenseToBusiness(agentInstDetail
				.getLicenseToBusiness());
		agentInstView.setLicenseToBusinessScanCopy(agentInstDetail
				.getLicenseToBusinessScanCopy());
		agentInstView.setResidentialAddress(agentInstDetail
				.getResidentialAddress());
		agentInstView.setResidentialAddressScanCopy(agentInstDetail
				.getResidentialAddressScanCopy());
		agentInstView.setAadhaarCard(agentInstDetail.getAadhaarCard());
		agentInstView.setAadhaarCardScanCopy(agentInstDetail
				.getAadhaarCardScanCopy());
		agentInstView.setVoterIdCard(agentInstDetail.getVoterIdCard());
		agentInstView.setVoterIdCardScanCopy(agentInstDetail
				.getVoterIdCardScanCopy());
		agentInstView.setPassport(agentInstDetail.getPassport());
		agentInstView
				.setPassportScanCopy(agentInstDetail.getPassportScanCopy());
		agentInstView
		.setAgentInstAccountNo(agentInstDetail.getAgentInstAccountNo());

		return agentInstView;
	}

	public static AgentInst mapToJaxb(AgentInstView agentInstView) {
		if (null == agentInstView) {
			return null;
		}
		AgentInst agentInst = new AgentInst();
		agentInst.setAgentInstId(agentInstView.getAgentInstId());
		agentInst.setAgentInstType(agentInstView.getAgentInstType());
		agentInst.setAgentInstBusnsType(agentInstView.getAgentInstBusnsType());
		agentInst.setAgentInstName(agentInstView.getAgentInstName());
		agentInst.setAgentInstAliasName(agentInstView.getAgentInstAliasName());
		agentInst.setAgentInstTanNo(agentInstView.getAgentInstTanNo());

		agentInst.setAgentInstRegAdrline1(agentInstView
				.getAgentInstRegAdrLine1());
		agentInst.setAgentInstRegAdrline2(agentInstView
				.getAgentInstRegAdrLine2());
		agentInst.setAgentInstRegAdrline3(agentInstView
				.getAgentInstRegAdrLine3());

		agentInst.setAgentInstRegCity(agentInstView.getAgentInstRegCity());
		agentInst
				.setAgentInstRegPinCode(agentInstView.getAgentInstRegPinCode());
		agentInst.setAgentInstRegState(agentInstView.getAgentInstRegState());
		agentInst
				.setAgentInstRegCountry(agentInstView.getAgentInstRegCountry());

		agentInst.setAgentInstCommAdrline1(agentInstView
				.getAgentInstCommAdrLine1());
		agentInst.setAgentInstCommAdrline2(agentInstView
				.getAgentInstCommAdrLine2());
		agentInst.setAgentInstCommAdrline3(agentInstView
				.getAgentInstCommAdrLine3());

		agentInst.setAgentInstCommCity(agentInstView.getAgentInstCommCity());
		agentInst.setAgentInstCommPinCode(agentInstView
				.getAgentInstCommPinCode());
		agentInst.setAgentInstCommState(agentInstView.getAgentInstCommState());
		agentInst.setAgentInstCommCountry(agentInstView
				.getAgentInstCommCountry());

		agentInst
				.setAgentInstEffctvFrom(agentInstView.getAgentInstEffctvFrom());
		agentInst.setAgentInstEffctvTo(agentInstView.getAgentInstEffctvTo());
		agentInst.setAgentInstUaadhaar(agentInstView.getAgentInstUaadhaar());
		agentInst.setAgentInstRocUin(agentInstView.getAgentInstRocUin());
		agentInst.setEntityStatus(agentInstView.getEntityStatus().name());

		agentInst.setBusAddrAuthLetter(agentInstView.getBusAddrAuthLetter());
		agentInst.setBusAddrAuthLetterScanCopy(agentInstView
				.getBusAddrAuthLetterScanCopy());
		agentInst.setLicenseToBusiness(agentInstView.getLicenseToBusiness());
		agentInst.setLicenseToBusinessScanCopy(agentInstView
				.getLicenseToBusinessScanCopy());
		agentInst.setResidentialAddress(agentInstView.getResidentialAddress());
		agentInst.setResidentialAddressScanCopy(agentInstView
				.getResidentialAddressScanCopy());
		agentInst.setAadhaarCard(agentInstView.getAadhaarCard());
		agentInst
				.setAadhaarCardScanCopy(agentInstView.getAadhaarCardScanCopy());
		agentInst.setVoterIdCard(agentInstView.getVoterIdCard());
		agentInst
				.setVoterIdCardScanCopy(agentInstView.getVoterIdCardScanCopy());
		agentInst.setPassport(agentInstView.getPassport());
		agentInst.setPassportScanCopy(agentInstView.getPassportScanCopy());
		agentInst
		.setAgentInstAccountNo(agentInstView.getAgentInstAccountNo());
		return agentInst;
	}

	private AgentInstDetail mapTo(AgentInstView agentInstView) {
		AgentInstDetail agentInstDetail = agentInstDao.get(agentInstView
				.getAgentInstId());
		if (null == agentInstDetail) {
			agentInstDetail = new AgentInstDetail();
			agentInstDetail.setCrtnTs(CommonUtils.currentTimestamp());
			agentInstDetail.setCrtnUserId(CommonUtils.getLoggedInUser());
		} else {
			agentInstDetail.setUpdtTs(CommonUtils.currentTimestamp());
			agentInstDetail.setUpdtUserId(CommonUtils.getLoggedInUser());
		}

		agentInstDetail.setAgentInstId(agentInstView.getAgentInstId());
		agentInstDetail.setAgentInstType(agentInstView.getAgentInstType());
		agentInstDetail.setAgentInstBusnsType(agentInstView
				.getAgentInstBusnsType());
		agentInstDetail.setAgentInstName(agentInstView.getAgentInstName());
		agentInstDetail.setAgentInstAliasName(agentInstView
				.getAgentInstAliasName());
		agentInstDetail.setAgentInstTanNo(agentInstView.getAgentInstTanNo());

		String regAddr = agentInstView.getAgentInstRegAdrLine1()
				+ System.lineSeparator()
				+ (agentInstView.getAgentInstRegAdrLine2() != null ? agentInstView
						.getAgentInstRegAdrLine2() + System.lineSeparator()
						: "")
				+ (agentInstView.getAgentInstRegAdrLine3() != null ? agentInstView
						.getAgentInstRegAdrLine3() : "");

		agentInstDetail.setAgentInstRegAdrline(regAddr);
		agentInstDetail
				.setAgentInstRegCity(agentInstView.getAgentInstRegCity());
		agentInstDetail.setAgentInstRegPinCode(agentInstView
				.getAgentInstRegPinCode());
		agentInstDetail.setAgentInstRegState(agentInstView
				.getAgentInstRegState());
		agentInstDetail.setAgentInstRegCountry(agentInstView
				.getAgentInstRegCountry());

		String commAddr = agentInstView.getAgentInstCommAdrLine1()
				+ System.lineSeparator()
				+ (agentInstView.getAgentInstCommAdrLine2() != null ? agentInstView
						.getAgentInstCommAdrLine2() + System.lineSeparator()
						: "")
				+ (agentInstView.getAgentInstCommAdrLine3() != null ? agentInstView
						.getAgentInstCommAdrLine3() : "");

		agentInstDetail.setAgentInstCommAdrline(commAddr);
		agentInstDetail.setAgentInstCommCity(agentInstView
				.getAgentInstCommCity());
		agentInstDetail.setAgentInstCommPinCode(agentInstView
				.getAgentInstCommPinCode());
		agentInstDetail.setAgentInstCommState(agentInstView
				.getAgentInstCommState());
		agentInstDetail.setAgentInstCommCountry(agentInstView
				.getAgentInstCommCountry());

		agentInstDetail.setAgentInstEffctvFrom(agentInstView
				.getAgentInstEffctvFrom());
		agentInstDetail.setAgentInstEffctvTo(agentInstView
				.getAgentInstEffctvTo());
		agentInstDetail.setAgentInstUaadhaar(agentInstView
				.getAgentInstUaadhaar());
		agentInstDetail.setAgentInstRocUin(agentInstView.getAgentInstRocUin());
		agentInstDetail.setEntityStatus(agentInstView.getEntityStatus().name());

		agentInstDetail.setBusAddrAuthLetter(agentInstView
				.getBusAddrAuthLetter());
		agentInstDetail.setBusAddrAuthLetterScanCopy(agentInstView
				.getBusAddrAuthLetterScanCopy());
		agentInstDetail.setLicenseToBusiness(agentInstView
				.getLicenseToBusiness());
		agentInstDetail.setLicenseToBusinessScanCopy(agentInstView
				.getLicenseToBusinessScanCopy());
		agentInstDetail.setResidentialAddress(agentInstView
				.getResidentialAddress());
		agentInstDetail.setResidentialAddressScanCopy(agentInstView
				.getResidentialAddressScanCopy());
		agentInstDetail.setAadhaarCard(agentInstView.getAadhaarCard());
		agentInstDetail.setAadhaarCardScanCopy(agentInstView
				.getAadhaarCardScanCopy());
		agentInstDetail.setVoterIdCard(agentInstView.getVoterIdCard());
		agentInstDetail.setVoterIdCardScanCopy(agentInstView
				.getVoterIdCardScanCopy());
		agentInstDetail.setPassport(agentInstView.getPassport());
		agentInstDetail
				.setPassportScanCopy(agentInstView.getPassportScanCopy());
		agentInstDetail
		.setAgentInstAccountNo(agentInstView.getAgentInstAccountNo());
		return agentInstDetail;
	}

	@Override
	public List<AgentInstView> getAgentInstListByOU() {
		List<AgentInstDetail> agentInstDetail = agentInstDao.getAllbyOu();
		List<AgentInstView> aiv = null;
		if (agentInstDetail != null) {
			aiv = new ArrayList<AgentInstView>(agentInstDetail.size());

			for (AgentInstDetail ai : agentInstDetail) {
				aiv.add(mapFrom(ai));
			}

		}

		return aiv;
	}

	/*@Override
	public AgentInstList getAgentInstJaxb(List<AgentInstView> agentInstViews) {
		AgentInstList agentInstList = null;
		if (agentInstViews != null) {
			agentInstList = new AgentInstList();

			for (AgentInstView aid : agentInstViews) {
				agentInstList.getAgentInsts().add(mapToJaxb(aid));
			}

		}

		return agentInstList;
	}*/

	@Override
	public byte[] exportReportToExcel() {

		List<AgentInstDetail> exportList = agentInstDao.exportReport();
		List<AgentInstView> exportListView = null;

		byte[] hwb = null;

		if (exportList != null && exportList.size() > 0) {
			try {
				exportListView = new ArrayList<AgentInstView>(exportList.size());
				for (AgentInstDetail od : exportList) {
					AgentInstView bv = new AgentInstView();
					bv = mapFrom(od);
					exportListView.add(bv);
				}

				hwb = ExcelUtil.writeReportToExcelAnnotation(exportListView,
						"AI Details");

			} catch (Exception e) {
				Log.error("Error: ", e);
			}
			return hwb;
		}

		return null;

	}

	public List<AgentInstView> searchByCriteria(String loggedInOUId) {
		List<AgentInstDetail> instDetails = agentInstDao.searchAgent();
		List<AgentInstView> agentInstViews = new ArrayList<AgentInstView>();
		for (AgentInstDetail aid : instDetails) {
			if (aid != null) {
				agentInstViews.add(mapFrom(aid));
			}
		}
		return agentInstViews;
	}

	public List<AgentInstView> fetchFunctionallyActiveListByOU(int pageNo,
			int pageSize) {

		List<AgentInstDetail> agentInstList = agentInstDao.fetchAllByOU(pageNo
				* pageSize, pageSize, EntityStatus.ACTIVE,
				EntityStatus.PENDING_DELETE, EntityStatus.PENDING_DEACTIVATION);

		if (agentInstList != null) {
			List<AgentInstView> aiv = new ArrayList<AgentInstView>(
					agentInstList.size());

			for (AgentInstDetail aInst : agentInstList) {
				aiv.add(mapFrom(aInst));
			}

			return aiv;
		}
		return null;
	}

	@Override
	public void update(AgentInstView agentInstView) throws ValidationException {
		AgentInstDetail aInst = validateRetrieveForUpdate(agentInstView);
		aInst.setUpdtTs(CommonUtils.currentTimestamp());
		aInst.setUpdtUserId(CommonUtils.getLoggedInUser());
		// update the contact details
		if (agentInstView.getContactDetailsView1stLevel() != null) {
			ContactDetailsView cont1 = agentInstView
					.getContactDetailsView1stLevel();
			cont1.setLinkedEntityID(aInst.getAgentInstId());
			cont1.setLinkedEntityType(LinkedEntityType.AGENT_INSTITUTE);
			cont1.setContactType(ContactType.TECH_1);
			contactDetailsService.update(agentInstView
					.getContactDetailsView1stLevel());
		}
		if (agentInstView.getContactDetailsView2ndtLevel() != null) {
			ContactDetailsView cont2 = agentInstView
					.getContactDetailsView2ndtLevel();
			cont2.setLinkedEntityID(aInst.getAgentInstId());
			cont2.setLinkedEntityType(LinkedEntityType.AGENT_INSTITUTE);
			cont2.setContactType(ContactType.TECH_2);
			contactDetailsService.update(agentInstView
					.getContactDetailsView2ndtLevel());
		}
		agentInstDao.createOrUpdate(aInst);
	}
}
