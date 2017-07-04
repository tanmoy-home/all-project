package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.ExcelUtil;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.ContactDetailsView;
import com.rssoftware.ou.model.tenant.ContactDetailsView.ContactType;
import com.rssoftware.ou.model.tenant.ContactDetailsView.LinkedEntityType;
import com.rssoftware.ou.model.tenant.UserView;
import com.rssoftware.ou.tenant.dao.AgentDao;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.ContactDetailsService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.UserService;

import in.co.rssoftware.bbps.schema.Agent;
import in.co.rssoftware.bbps.schema.AgentList;

@Service
public class AgentServiceImpl implements AgentService {

	private final static Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

	@Autowired
	AgentDao agentDao;

	@Autowired
	ContactDetailsService contactDetailsService;

	@Autowired
	private ParamService paramService;
	
	/*@Autowired
	UserService userService;*/

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public AgentView getAgentById(String agentId) {
		AgentDetail agentDetail = agentDao.get(agentId);
		return mapFrom(agentDetail);
	}

	@Override
	public AgentView save(AgentView agentView) throws ValidationException, IOException {
		AgentDetail agentDetail = mapTo(agentView);
		if (agentView.getContactDetailsView1stLevel() != null) {
			ContactDetailsView cont1 = agentView.getContactDetailsView1stLevel();
			cont1.setLinkedEntityID(agentDetail.getAgentId());
			cont1.setLinkedEntityType(LinkedEntityType.AGENT);
			cont1.setContactType(ContactType.TECH_1);
			// contactDetailsService.submit(cont1);
		}
		if (agentView.getContactDetailsView2ndLevel() != null) {
			ContactDetailsView cont2 = agentView.getContactDetailsView2ndLevel();
			cont2.setLinkedEntityID(agentDetail.getAgentId());
			cont2.setLinkedEntityType(LinkedEntityType.AGENT);
			cont2.setContactType(ContactType.TECH_2);
			// contactDetailsService.submit(cont2);
		}
		return mapFrom(agentDao.createOrUpdate(agentDetail));
	}

	/*
	 * @Override public Agent updateStatus(AgentView av, EntityStatus
	 * entityStatus) throws ValidationException {
	 * 
	 * if (av.getAgentId() == null) { throw
	 * ValidationException.getInstance(ValidationException.ValidationErrorReason
	 * .AGENT_NOT_FOUND); } if (entityStatus == null) { throw
	 * ValidationException.getInstance(ValidationException.ValidationErrorReason
	 * .ILLEGAL_OPERATION); } av.setEntityStatus(entityStatus);
	 * 
	 * return mapToJaxb(mapFrom(agentDao.createOrUpdate(mapTo(av)))); }
	 */

	@Override
	public void delete(String agentId) {
		AgentDetail agent = agentDao.get(agentId);
		agentDao.delete(agent);
	}

	private AgentView mapFrom(AgentDetail agentDetail) {
		if (null == agentDetail) {
			return null;
		}
		AgentView agentView = new AgentView();
		agentView.setAgentId(agentDetail.getAgentId());
		agentView.setAgentName(agentDetail.getAgentName());
		agentView.setAgentAliasName(agentDetail.getAgentAliasName());
		agentView.setAgentShopName(agentDetail.getAgentShopName());
		agentView.setAgentRegisteredAdrline(agentDetail.getAgentRegisteredAdrline());
		agentView.setAgentRegisteredCity(agentDetail.getAgentRegisteredCity());
		agentView.setAgentRegisteredState(agentDetail.getAgentRegisteredState());
		agentView.setAgentRegisteredCountry(agentDetail.getAgentRegisteredCountry());
		agentView.setAgentRegisteredPinCode(agentDetail.getAgentRegisteredPinCode());
		agentView.setAgentMobileNo(agentDetail.getAgentMobileNo());
		agentView.setAgentInst(agentDetail.getAgentLinkedAgentInst());
		agentView.setDummyAgent(agentDetail.isDummyAgent());
		agentView.setAgentBankAccount(agentDetail.getAgentBankAccount());
		agentView.setAgentSchemeId(agentDetail.getAgentSchemeId());
		agentView.setAgentGeoCode(agentDetail.getAgentGeoCode());
		agentView.setBusinessType(agentDetail.getAgentBusnsType());
		agentView.setAgentEffctvFrom(agentDetail.getAgentEffctvFrom());
		agentView.setAgentEffctvTo(agentDetail.getAgentEffctvTo());
		agentView.setAgentType(agentDetail.getAgentType());
		agentView.setIsUpload(agentDetail.getIsUpload());
		agentView.setRejectComment(agentDetail.getRejectComment());
		try {
			if (StringUtils.isNotBlank(agentDetail.getAgentPaymentModes())) {
				String agentPModes = null;
				if (agentDetail.getAgentPaymentModes().contains("[[")) {
					agentPModes = agentDetail.getAgentPaymentModes().replace("[[", "[");
					if (agentPModes.contains("]]")) {
						agentPModes = agentPModes.replace("]]", "]");
					}
				} else {
					agentPModes = agentDetail.getAgentPaymentModes();
				}
				if (!agentPModes.equals("{}")) {
					List<PaymentModeLimit> paymentModeList = mapper.readValue(agentPModes,
							TypeFactory.defaultInstance().constructCollectionType(List.class, PaymentModeLimit.class));
					agentView.setAgentPaymentModes(paymentModeList);
				}
			}

			/*
			 * List<String> list = new
			 * ArrayList<String>(Arrays.asList(agentDetail.getAgentPaymentModes(
			 * ).substring(1,
			 * agentDetail.getAgentPaymentModes().length()-1).split(",")));
			 * 
			 * List<PaymentMode> paymentModeList = new ArrayList<PaymentMode>();
			 * for(String str: list) {
			 * paymentModeList.add(PaymentMode.getFromName(str.trim())); }
			 * agentView.setAgentPaymentModeList(paymentModeList);
			 */

			if (StringUtils.isNotBlank(agentDetail.getAgentPaymentChannels())) {
				String agentPChannels = null;
				if (agentDetail.getAgentPaymentChannels().contains("[[")) {
					agentPChannels = agentDetail.getAgentPaymentChannels().replace("[[", "[");
					if (agentPChannels.contains("]]")) {
						agentPChannels = agentPChannels.replace("]]", "]");
					}
				} else {
					agentPChannels = agentDetail.getAgentPaymentChannels();
				}
				if (!agentPChannels.equals("{}")) {
					List<PaymentChannelLimit> paymentChannelList = mapper
							.readValue(agentPChannels, TypeFactory.defaultInstance()
									.constructCollectionType(List.class, PaymentChannelLimit.class));
					agentView.setAgentPaymentChannels(paymentChannelList);
				}
			}
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
		}

		agentView.setEntityStatus(EntityStatus.valueOf(agentDetail.getEntityStatus()));
		return agentView;
	}

	private AgentDetail mapTo(AgentView agentView) throws IOException {
		AgentDetail agentDetail = null;
		// if (null == agentDetail) {
		agentDetail = new AgentDetail();
		agentDetail.setCrtnTs(new Timestamp(new Date().getTime()));
		agentDetail.setUpdtTs(new Timestamp(new Date().getTime()));

		/*
		 * } else { agentDetail.setUpdtTs(new Timestamp(new Date().getTime()));
		 * }
		 */
		agentDetail.setAgentId(agentView.getAgentId());
		agentDetail.setAgentName(agentView.getAgentName());
		agentDetail.setAgentAliasName(agentView.getAgentAliasName());
		agentDetail.setAgentShopName(agentView.getAgentShopName());
		agentDetail.setAgentRegisteredAdrline(agentView.getAgentRegisteredAdrline());
		agentDetail.setAgentRegisteredCity(agentView.getAgentRegisteredCity());
		agentDetail.setAgentRegisteredState(agentView.getAgentRegisteredState());
		agentDetail.setAgentRegisteredCountry(agentView.getAgentRegisteredCountry());
		agentDetail.setAgentRegisteredPinCode(agentView.getAgentRegisteredPinCode());
		agentDetail.setAgentMobileNo(agentView.getAgentMobileNo());
		agentDetail.setAgentCUID(agentView.getAgentCUID());
		try {
			//List<String> agentPaymentModes = new ArrayList<String>();
			//agentPaymentModes.add(mapper.writeValueAsString(agentView.getAgentPaymentModes()));

			agentDetail.setAgentPaymentModes(mapper.writeValueAsString(agentView.getAgentPaymentModes()));

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
		try {
			//List<String> agentPaymentChannels = new ArrayList<String>();
			//agentPaymentChannels.add(mapper.writeValueAsString(agentView.getAgentPaymentChannels()));

			agentDetail.setAgentPaymentChannels(mapper.writeValueAsString(agentView.getAgentPaymentChannels()));

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
		agentDetail.setEntityStatus(agentView.getEntityStatus().name());
		agentDetail.setAgentLinkedAgentInst(agentView.getAgentInst());
		agentDetail.setDummyAgent(agentView.isDummyAgent());
		agentDetail.setAgentBankAccount(agentView.getAgentBankAccount());
		agentDetail.setAgentSchemeId(agentView.getAgentSchemeId());
		agentDetail.setAgentGeoCode(agentView.getAgentGeoCode());
		agentDetail.setAgentBusnsType(agentView.getBusinessType());
		agentDetail.setAgentEffctvFrom(agentView.getAgentEffctvFrom());
		agentDetail.setAgentEffctvTo(agentView.getAgentEffctvTo());
		agentDetail.setAgentType(agentView.getAgentType());
		agentDetail.setIsUpload(agentView.getIsUpload());
		agentDetail.setRejectComment(agentView.getRejectComment());
		return agentDetail;
	}

	@Override
	public AgentView getAgentByIdAndStatus(String agentId, String entytystatus) {
		AgentDetail agentDetail = agentDao.getAgent(agentId, entytystatus);
		return mapFrom(agentDetail);
	}

	@Override
	public List<AgentView> getAllAgentList() throws DataAccessException {

		List<AgentDetail> agntM = agentDao.getAll();

		if (agntM != null) {
			List<AgentView> sv = new ArrayList<AgentView>(agntM.size());

			for (AgentDetail c : agntM) {
				sv.add(mapFrom(c));
			}
			return sv;
		}
		return null;
	}

	@Override
	public List<AgentView> getAllAgentsByInstituteId(String instId) {

		List<AgentDetail> agntM = agentDao.getAllAgentsByInstituteId(instId);

		if (agntM != null) {
			List<AgentView> sv = new ArrayList<AgentView>(agntM.size());

			for (AgentDetail c : agntM) {
				sv.add(mapFrom(c));
			}
			return sv;
		}
		return null;
	}

	@Override
	public byte[] exportReportToExcel(String loggedInUsersOuId) {
		List<AgentDetail> exportList = AgentDao.exportReport(loggedInUsersOuId);
		List<AgentView> exportListView = null;

		byte[] hwb = null;

		if (exportList != null && exportList.size() > 0) {
			try {
				exportListView = new ArrayList<AgentView>(exportList.size());
				for (AgentDetail od : exportList) {
					AgentView bv = new AgentView();
					bv = mapFrom(od);
					exportListView.add(bv);
				}

				hwb = ExcelUtil.writeReportToExcelAnnotation(exportListView, "Biller");

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				logger.info("In Excp : " + e.getMessage());
			}
			return hwb;
		}

		return null;

	}

	public static AgentList getAgentJaxb(List<AgentView> agentViews) {
		AgentList agentList = null;
		if (agentViews != null) {
			agentList = new AgentList();

			for (AgentView aid : agentViews) {
				agentList.getAgentLists().add(mapToJaxb(aid));
			}

		}

		return agentList;
	}

	public static Agent mapToJaxb(AgentView agentView) {
		Agent agent = new Agent();
		agent.setAgentId(agentView.getAgentId());
		agent.setAgentName(agentView.getAgentName());
		agent.setAgentAliasName(agentView.getAgentAliasName());
		agent.setAgentShopName(agentView.getAgentShopName());
		agent.setAgentRegisteredAdrline(agentView.getAgentRegisteredAdrline());
		agent.setAgentRegisteredCity(agentView.getAgentRegisteredCity());
		agent.setAgentRegisteredState(agentView.getAgentRegisteredState());
		agent.setAgentRegisteredCountry(agentView.getAgentRegisteredCountry());
		agent.setAgentRegisteredPinCode(agentView.getAgentRegisteredPinCode());
		agent.setAgentMobileNo(agentView.getAgentMobileNo());
		ObjectMapper mapper = new ObjectMapper();

		try {
			agent.getAgentPaymentModes().add(mapper.writeValueAsString(agentView.getAgentPaymentModes()));
		} catch (Exception e) {
			Log.error("Error: ", e);
		}
		agent.setEntityStatus(in.co.rssoftware.bbps.schema.EntityStatus.valueOf(agentView.getEntityStatus().name()));
		agent.setAgentLinkedAgentInst(agentView.getAgentInst());
		agent.setDummyAgent(agentView.isDummyAgent());
		agent.setAgentEffctvFrom(agentView.getAgentEffctvFrom());

		agent.setAgentEffctvTo(agentView.getAgentEffctvTo());
		agent.setAgentBusnsType(agentView.getBusinessType());
		agent.setAgentGeoCode(agentView.getAgentGeoCode());
		agent.setAgentBankAccount(agentView.getAgentBankAccount());
		agent.setAgentSchemeId(agentView.getAgentSchemeId());
		agent.setAgentType(agentView.getAgentType());
		agent.setIsUpload(agentView.getIsUpload());
		agent.setRejectComment(agentView.getRejectComment());
		return agent;
	}

	@Override
	public double getAvailableBalance(CBSRequest req) {
		// TODO Auto-generated method stub
		return 3000;
	}

	@Override
	public List<AgentView> getAllPendingAgentsByInstituteId(String instId) {
		List<AgentDetail> agntM = agentDao.getAllPendingAgentsByInstituteId(instId);

		if (agntM != null) {
			List<AgentView> sv = new ArrayList<AgentView>(agntM.size());

			for (AgentDetail c : agntM) {
				sv.add(mapFrom(c));
			}
			return sv;
		}
		return null;
	}

	public static in.co.rssoftware.bbps.schema.AgentDetail mapAgentToJaxb(AgentView agentView) {
		in.co.rssoftware.bbps.schema.AgentDetail agent = new in.co.rssoftware.bbps.schema.AgentDetail();
		agent.setAgentID(agentView.getAgentId());
		agent.setAgentName(agentView.getAgentName());
		agent.setAgentAliasName(agentView.getAgentAliasName());
		agent.setAgentShopName(agentView.getAgentShopName());
		agent.setAgentAddr(agentView.getAgentRegisteredAdrline());
		agent.setAgentCity(agentView.getAgentRegisteredCity());
		agent.setAgentState(agentView.getAgentRegisteredState());
		agent.setAgentCountry(agentView.getAgentRegisteredCountry());
		agent.setAgentPin(agentView.getAgentRegisteredPinCode());
		agent.setAgentMobile(agentView.getAgentMobileNo());
		agent.setAgentGEOCode(agentView.getAgentGeoCode());

		if (agentView.getAgentPaymentModes()  != null) {
			for (PaymentModeLimit mode : agentView.getAgentPaymentModes()) {
				if (mode != null && mode.getPaymentMode() != null) {
					in.co.rssoftware.bbps.schema.PaymentModeLimit paymentMode = new in.co.rssoftware.bbps.schema.PaymentModeLimit();
					paymentMode.setPaymentMode(mode.getPaymentMode().name());
					paymentMode.setLimit((mode.getMaxLimit() == null ? "" : mode.getMaxLimit().toString()));
					agent.getAgentPaymentModes().add(paymentMode);
				}
			}
		}
		if (agentView.getAgentPaymentChannels() != null) {
			for (PaymentChannelLimit mode : agentView.getAgentPaymentChannels()) {
				if (mode != null && mode.getPaymentChannel() != null) {
					in.co.rssoftware.bbps.schema.PaymentChannelLimit paymentChannel = new in.co.rssoftware.bbps.schema.PaymentChannelLimit();
					paymentChannel.setPaymentChannel(mode.getPaymentChannel().name());
					paymentChannel.setLimit((mode.getMaxLimit() == null ? "" : mode.getMaxLimit().toString()));
					agent.getAgentPaymentChannels().add(paymentChannel);
				}
			}
		}

		agent.setAgentEntityStatus(agentView.getEntityStatus().name());
		agent.setAgentInst(agentView.getAgentInst());
		agent.setDummyAgent(agentView.isDummyAgent());
		agent.setAgentEffectiveFrom(agentView.getAgentEffctvFrom());
		agent.setAgentEffectiveTo(agentView.getAgentEffctvTo());
		agent.setAgentBankAccount(agentView.getAgentBankAccount());
		agent.setAgentBusinessType(agentView.getBusinessType());
		agent.setAgentSchemeId(agentView.getAgentSchemeId());
		agent.setAgentType(agentView.getAgentType());
		agent.setIsUpload(agentView.getIsUpload());
		agent.setRejectComment(agentView.getRejectComment());
		return agent;
	}

	@Override
	public AgentView getDefaultAgent(String paymentChannel) {
		// TODO Auto-generated method stub
		AgentView agentView = null;
		String agentId = paramService.retrieveStringParamByName(CommonConstants.DEFAULT_AGENT);
		AgentDetail defaultAgent = agentDao.get(agentId);
		agentView = mapFrom(defaultAgent);
		if (agentView != null && agentView.getAgentPaymentChannels() != null
				&& agentView.getAgentPaymentChannels().size() > 0) {
			for (PaymentChannelLimit paymentChannelLimit : agentView.getAgentPaymentChannels()) {
				if (paymentChannelLimit.getPaymentChannel() != null
						&& paymentChannelLimit.getPaymentChannel().name().equalsIgnoreCase(paymentChannel)) {
					return agentView;
				}
			}
		}
		return agentView;
	}
	
	public void saveBulk (List<AgentDetail> agentList) throws IOException{
		for(AgentDetail cuApprovedAgentDtl: agentList){
			AgentView agentView = getAgentById(cuApprovedAgentDtl.getAgentId());
			agentView.setAgentCUID(cuApprovedAgentDtl.getAgentCUID());
			agentView.setEntityStatus(EntityStatus.CU_APPROVED);
			AgentDetail agentDetail = mapTo(agentView);
			agentDao.createOrUpdate(agentDetail);
			
			//UserView userView = new UserView();
			//userView.
			
			//userService.save(userView);
		}
	}

}
