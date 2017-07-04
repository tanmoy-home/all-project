package com.rssoftware.ou.controller;


import in.co.rssoftware.bbps.schema.AgentCommission;
import in.co.rssoftware.bbps.schema.AgentInstList;
import in.co.rssoftware.bbps.schema.AgentList;
import in.co.rssoftware.bbps.schema.AgentScheme;
import in.co.rssoftware.bbps.schema.AgentSchemeList;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.Scheme;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonAPIRequest;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.model.tenant.AgentInstView;
import com.rssoftware.ou.model.tenant.AgentSchemeView;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.SchemeCommission;
import com.rssoftware.ou.tenant.service.AgentInstService;
import com.rssoftware.ou.tenant.service.AgentSchemeService;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.impl.AgentInstServiceImpl;
import com.rssoftware.ou.tenant.service.impl.AgentServiceImpl;

@Controller
@Scope(value=WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/APIService/agentInst/urn:tenantId:{tenantId}")
public class AgentInstAPIController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	AgentInstService agentInstService;
	
	@Autowired
	AgentSchemeService agentSchemeService;
	
	@Autowired
	AgentService agentService;
	
	/**
	 * To be used by Agent Institutes to approve an agent
	 * 
	 * @param tenantId
	 * @param agentId
	 * @return
	 */
	@RequestMapping(value = "/approve", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> approve(@PathVariable String tenantId, final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "approve";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		return updateStatus(commonAPIRequest.getAgentId(), EntityStatus.CU_PENDING, "");
	}

	/**
	 * To be used by Agent Institutes to reject an agent
	 * 
	 * @param tenantId
	 * @param agentId
	 * @return
	 */
	@RequestMapping(value = "/reject", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> reject(@PathVariable String tenantId, final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "reject";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		return updateStatus(commonAPIRequest.getAgentId(), EntityStatus.REJECTED, commonAPIRequest.getRejectComment());
	}

	ResponseEntity<?> updateStatus(String agentId, EntityStatus entityStatus, String rejectComment) {
		ResponseEntity<?> response = null;
		try {
			AgentView agentView = agentService.getAgentById(agentId);
			if (agentView != null) {
				if (agentView.getEntityStatus() == EntityStatus.DRAFT) {
					agentView.setEntityStatus(entityStatus);
					agentView.setRejectComment(rejectComment);
					agentView = agentService.save(agentView);
					List<AgentView> agentViews = new ArrayList<AgentView>();
					agentViews.add(agentView);
					response = new ResponseEntity(AgentServiceImpl.getAgentJaxb(agentViews), HttpStatus.OK);
				} 
				else {
					throw ValidationException.getInstance(ValidationErrorReason.ILLEGAL_OPERATION);
				}
			} 
			else {
				throw ValidationException.getInstance(ValidationErrorReason.AGENT_NOT_FOUND);
			}
		} 
		catch (ValidationException ve) {
			log.error("Error: ", ve);
			AgentList agentList = new AgentList();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			if(ValidationErrorReason.valueOf(ve.getCode())==ValidationErrorReason.ILLEGAL_OPERATION)
				errorMessage.setErrorDtl("Agent In Invalid State!");
			else
				errorMessage.setErrorDtl("Agent could not be found!");
			agentList.getErrors().add(errorMessage);
			response = new ResponseEntity(agentList, HttpStatus.EXPECTATION_FAILED);
		}
		catch (Exception e) {
			log.error("Error: ", e);
			AgentList agentList = new AgentList();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			agentList.getErrors().add(errorMessage);
			response = new ResponseEntity(agentList, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	/**
	 * To be used by Agent Institutes to find all pending_activation type agents
	 * 
	 * @param tenantId
	 * @param agentInstId
	 * @return
	 */
	@RequestMapping(value = "/pendingAgents", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> fetchAllPendingAgentsByInstitute(@PathVariable String tenantId, final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "fetchAllPendingAgentsByInstitute";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			List<AgentView> agentViews = agentService.getAllPendingAgentsByInstituteId(commonAPIRequest.getAgentInstId());
			response = new ResponseEntity(AgentServiceImpl.getAgentJaxb(agentViews), HttpStatus.OK); 
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	/**
	 * To be used by Agent Institutes to find commission for a particular agent
	 * 
	 * @param tenantId
	 * @param agentInstId
	 * @return
	 */
	@RequestMapping(value = "/commission", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> findCommissionForAgent(@PathVariable String tenantId, final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "findCommissionForAgent";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			List<AgentView> agentViews = agentService.getAllAgentsByInstituteId(commonAPIRequest.getAgentInstId());
			//List<AgentView> agentViews = agentService.getAllPendingAgentsByInstituteId(agentId);
			//response = new ResponseEntity(AgentServiceImpl.getAgentJaxb(agentViews), HttpStatus.OK); 
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/agentInstitutes", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> fetchAllAgentInstitutes(@PathVariable String tenantId) {
		String METHOD_NAME = "fetchAllAgentInstitutes";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			List<AgentInstView> agentInstViews = agentInstService.getAgentInstListByOU();
			AgentInstList agentInstList = new AgentInstList();
			agentInstViews.forEach(val->{
				agentInstList.getAgentInsts().add(AgentInstServiceImpl.mapToJaxb(val));
			});
			response = new ResponseEntity(agentInstList, HttpStatus.OK); 
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			AgentInstList agentInstList = new AgentInstList();
			agentInstList.getErrors().add(errorMessage);
			response = new ResponseEntity(agentInstList, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	@RequestMapping(value = "/addScheme", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> addScheme(@PathVariable String tenantId, @RequestBody AgentScheme agentScheme) {
		String METHOD_NAME = "addScheme";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			AgentSchemeView agentSchemeViewSaved = agentSchemeService.save(getViewFromJaxb(agentScheme));
			response = new ResponseEntity(getJaxbFromView(agentSchemeViewSaved), HttpStatus.OK); 
		}
		catch (ValidationException ve) {
			log.error("Error: ", ve);
			AgentScheme scheme = new AgentScheme();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			errorMessage.setErrorDtl(ve.getDescription());
			scheme.getErrors().add(errorMessage);
			response = new ResponseEntity(scheme, HttpStatus.EXPECTATION_FAILED);
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			AgentScheme scheme = new AgentScheme();
			scheme.getErrors().add(errorMessage);
			response = new ResponseEntity(scheme, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	/*@RequestMapping(value = "/updateScheme", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> updateScheme(@PathVariable String tenantId, @RequestBody AgentScheme agentScheme) {
		String METHOD_NAME = "updateScheme";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			AgentSchemeView agentSchemeViewUpdated = agentSchemeService.update(getViewFromJaxb(agentScheme));
			response = new ResponseEntity(getJaxbFromView(agentSchemeViewUpdated), HttpStatus.OK); 
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			AgentScheme scheme = new AgentScheme();
			scheme.getErrors().add(errorMessage);
			response = new ResponseEntity(scheme, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}*/
	
	@RequestMapping(value = "/getSchemes", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> fetchSchemeByInstId(@PathVariable String tenantId, final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "fetchSchemeByInstId";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			//List<AgentSchemeView> agentSchemeViews2 = agentSchemeService.getAllAgentSchemes();

			List<AgentSchemeView> agentSchemeViews = agentSchemeService.getAgentSchemesByInstId(commonAPIRequest.getAgentInstId());
			AgentSchemeList agentSchemeList = new AgentSchemeList();
			agentSchemeViews.forEach(val->{
				agentSchemeList.getSchemes().add(mapToJaxb(val));
			});
			response = new ResponseEntity(agentSchemeList, HttpStatus.OK); 
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			AgentSchemeList agentSchemeList = new AgentSchemeList();
			agentSchemeList.getErrors().add(errorMessage);
			response = new ResponseEntity(agentSchemeList, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/getAllAgents", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> getAllAgentDetails(HttpServletRequest request, @PathVariable String tenantId	) {

		String METHOD_NAME = "getAllAgentDetails";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			List<AgentView> agentView = agentService.getAllAgentList();
			if (agentView != null) {
				response = new ResponseEntity(AgentServiceImpl.getAgentJaxb(agentView), HttpStatus.OK);
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd("AGENT_LIST_NOT_FOUND");
				errorMessage.setErrorDtl("Agent List could not be found!");
				response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/getScheme", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> fetchSchemeById(@PathVariable String tenantId, final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "fetchSchemeById";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			AgentSchemeView agentSchemeView = agentSchemeService.getAgentSchemeById(commonAPIRequest.getSchemeId());
			response = new ResponseEntity(getJaxbFromView(agentSchemeView), HttpStatus.OK); 
		}
		catch (ValidationException ve) {
			log.error("Error: ", ve);
			AgentScheme scheme = new AgentScheme();
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			errorMessage.setErrorDtl(ve.getDescription());
			scheme.getErrors().add(errorMessage);
			response = new ResponseEntity(scheme, HttpStatus.EXPECTATION_FAILED);
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			AgentScheme scheme = new AgentScheme();
			scheme.getErrors().add(errorMessage);
			response = new ResponseEntity(scheme, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	@RequestMapping(value = "/agents", method = RequestMethod.POST)
	@ResponseBody ResponseEntity<?> fetchAllAgentsByInstitute(@PathVariable String tenantId, final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "fetchAllAgentsByInstitute";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		
		try {
			List<AgentView> agentViews = agentService.getAllAgentsByInstituteId(commonAPIRequest.getAgentInstId());
			if(agentViews.isEmpty()) {
				throw ValidationException.getInstance(ValidationErrorReason.NULL);
			}
			/*MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("Content-Type", "application/xml");*/
			//map.add("Content-Type", "application/json");
			response = new ResponseEntity(AgentServiceImpl.getAgentJaxb(agentViews), HttpStatus.OK); 
		}
		catch (ValidationException ve) {
			log.error("Error: ", ve);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("AGENT_INTITUTE_NOT_FOUND");
			errorMessage.setErrorDtl("Agent Institute could not be found!");
			/*MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("Content-Type", "application/xml");*/
			//map.add("Content-Type", "application/json");
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		catch(Exception e) {
			log.error("Error: ", e);
			/*MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("Content-Type", "application/xml");*/
			//map.add("Content-Type", "application/json");
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	
	
	@RequestMapping(value = "/agentInstId", method = RequestMethod.POST)
	@ResponseBody
	ResponseEntity<?> getAgentInstituteDetails(@PathVariable String tenantId,
			final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "getAgentInstituteDetails";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
				AgentInstView agentInstView = agentInstService.getAgentInstById(commonAPIRequest.getAgentInstId());
				if(agentInstView!=null) {
					response = new ResponseEntity(AgentInstServiceImpl.mapToJaxb(agentInstView), HttpStatus.OK);
				}
				else {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorCd("AGENT_INTITUTE_NOT_FOUND");
					errorMessage.setErrorDtl("Agent Institute could not be found!");
					response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
				}
		}
		catch(Exception e) {
			log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	public static Scheme mapToJaxb(AgentSchemeView agentSchemeView) {
		Scheme scheme = new Scheme();
		scheme.setSchemeId(agentSchemeView.getSchemeId());
		scheme.setSchemeUniqueId(agentSchemeView.getSchemeUniqueId());
		scheme.setSchemeName(agentSchemeView.getSchemeName());
		scheme.setSchemeDesc(agentSchemeView.getSchemeDesc());
		scheme.setSchemeEffctvFrom(agentSchemeView.getSchemeEffctvFrom());
		scheme.setSchemeEffctvTo(agentSchemeView.getSchemeEffctvTo());
		
		List<SchemeCommission> schemeCommissions = agentSchemeView.getSchemeCommissions();
		List<in.co.rssoftware.bbps.schema.SchemeCommission> agentCommissions = new ArrayList<in.co.rssoftware.bbps.schema.SchemeCommission>();
		in.co.rssoftware.bbps.schema.SchemeCommission agentCommission = null;
		for(SchemeCommission schemeCommission:schemeCommissions) {
			agentCommission = new in.co.rssoftware.bbps.schema.SchemeCommission();
			agentCommission.setAgentFlatFee(schemeCommission.getAgentFlatFee());
			agentCommission.setAgentPercentFee(schemeCommission.getAgentPercentFee());
			agentCommission.setAiPercentFee(schemeCommission.getAiPercentFee());
			agentCommission.setTranAmtRangeMax(schemeCommission.getTranAmtRangeMax());
			agentCommission.setTranAmtRangeMin(schemeCommission.getTranAmtRangeMin());
			agentCommission.setTranCountRangeMax(schemeCommission.getTranCountRangeMax());
			agentCommission.setTranCountRangeMin(schemeCommission.getTranCountRangeMin());
			agentCommissions.add(agentCommission);

		}
		scheme.getCommissionDetails().addAll(agentCommissions);

		return scheme;
	}

	public static AgentSchemeView getViewFromJaxb(AgentScheme scheme) {
		AgentSchemeView agentSchemeView = new AgentSchemeView();
		agentSchemeView.setSchemeId(scheme.getSchemeId());
		agentSchemeView.setSchemeUniqueId(scheme.getSchemeUniqueId());
		agentSchemeView.setSchemeName(scheme.getSchemeName());
		agentSchemeView.setSchemeDesc(scheme.getSchemeDesc());
		agentSchemeView.setSchemeEffctvFrom(scheme.getSchemeEffctvFrom());
		agentSchemeView.setSchemeEffctvTo(scheme.getSchemeEffctvTo());
		List<String> agentInstIds = scheme.getAgentInstIds();
		agentSchemeView.setAgentInstIds(agentInstIds);
		List<AgentCommission> agentCommissions = scheme.getCommissionDetails();
		List<SchemeCommission> schemeCommissions = new ArrayList<SchemeCommission>();
		SchemeCommission schemeCommission = null;
		for(AgentCommission commission:agentCommissions) {
			schemeCommission = new SchemeCommission();
			schemeCommission.setAgentFlatFee(commission.getAgentFlatFee());
			schemeCommission.setAgentPercentFee(commission.getAgentPercentFee());
			schemeCommission.setAiPercentFee(commission.getAiPercentFee());
			schemeCommission.setTranAmtRangeMax(commission.getTranAmtRangeMax());
			schemeCommission.setTranAmtRangeMin(commission.getTranAmtRangeMin());
			schemeCommission.setTranCountRangeMax(commission.getTranCountRangeMax());
			schemeCommission.setTranCountRangeMin(commission.getTranCountRangeMin());
			schemeCommissions.add(schemeCommission);

		}
		agentSchemeView.setSchemeCommissions(schemeCommissions);

		return agentSchemeView;
	}

	public static AgentScheme getJaxbFromView(AgentSchemeView agentSchemeView) {
		AgentScheme scheme = new AgentScheme();
		scheme.setSchemeId(agentSchemeView.getSchemeId());
		scheme.setSchemeUniqueId(agentSchemeView.getSchemeUniqueId());
		scheme.setSchemeName(agentSchemeView.getSchemeName());
		scheme.setSchemeDesc(agentSchemeView.getSchemeDesc());
		scheme.setSchemeEffctvFrom(agentSchemeView.getSchemeEffctvFrom());
		scheme.setSchemeEffctvTo(agentSchemeView.getSchemeEffctvTo());
		List<String> agentInstIds = agentSchemeView.getAgentInstIds();
		scheme.getAgentInstIds().addAll(agentInstIds);
		List<SchemeCommission> schemeCommissions = agentSchemeView.getSchemeCommissions();
		List<AgentCommission> agentCommissions = new ArrayList<AgentCommission>();
		AgentCommission agentCommission = null;
		for(SchemeCommission schemeCommission:schemeCommissions) {
			agentCommission = new AgentCommission();
			agentCommission.setAgentFlatFee(schemeCommission.getAgentFlatFee());
			agentCommission.setAgentPercentFee(schemeCommission.getAgentPercentFee());
			agentCommission.setAiPercentFee(schemeCommission.getAiPercentFee());
			agentCommission.setTranAmtRangeMax(schemeCommission.getTranAmtRangeMax());
			agentCommission.setTranAmtRangeMin(schemeCommission.getTranAmtRangeMin());
			agentCommission.setTranCountRangeMax(schemeCommission.getTranCountRangeMax());
			agentCommission.setTranCountRangeMin(schemeCommission.getTranCountRangeMin());
			agentCommissions.add(agentCommission);

		}
		scheme.getCommissionDetails().addAll(agentCommissions);

		return scheme;
	}
}
