package com.rssoftware.ou.controller;

import in.co.rssoftware.bbps.schema.Agent;
import in.co.rssoftware.bbps.schema.AgentDetail;
import in.co.rssoftware.bbps.schema.AgentList;
import in.co.rssoftware.bbps.schema.AgentSchemeList;
import in.co.rssoftware.bbps.schema.ApplicationConfigDetail;
import in.co.rssoftware.bbps.schema.ApplicationConfigList;
import in.co.rssoftware.bbps.schema.CityDetail;
import in.co.rssoftware.bbps.schema.CityList;
import in.co.rssoftware.bbps.schema.ParamValue;
import in.co.rssoftware.bbps.schema.PaymentModeFilterReq;
import in.co.rssoftware.bbps.schema.PaymentModeFilterResponse;
import in.co.rssoftware.bbps.schema.PostalCodeList;
import in.co.rssoftware.bbps.schema.PostalDetail;
import in.co.rssoftware.bbps.schema.ReconSummaryList;
import in.co.rssoftware.bbps.schema.Scheme;
import in.co.rssoftware.bbps.schema.StateDetail;
import in.co.rssoftware.bbps.schema.StateList;

import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bbps.schema.Ack;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rssoftware.ou.common.APIURL;
import com.rssoftware.ou.common.CommonAPIRequest;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.PortalUtils;
import com.rssoftware.ou.domain.JsonResponse;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.PaymentModeView;
import com.rssoftware.ou.service.UserService;

@Controller
@RequestMapping(value = "/agentInstitute")
public class AgentInstController {

	private static final Logger LOG = LoggerFactory.getLogger(AgentInstController.class);

	private static OUInternalRestTemplate ouInternalRestTemplate = OUInternalRestTemplate.createInstance();

	@Autowired
	UserService userService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/agentBoarding/{type}/{agentID}", method = RequestMethod.GET)
	public ModelAndView agentRegistration(@ModelAttribute("agent") AgentView agent, Model model,
			@PathVariable("type") String type, @PathVariable("agentID") String agentID) {
		String METHOD_NAME = "agentBoarding";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		List<StateDetail> stateLists = Collections.EMPTY_LIST;
		List<Scheme> schemeLists = Collections.EMPTY_LIST;

		List<CityDetail> cityLists = Collections.EMPTY_LIST;
		List<PostalDetail> postalLists = Collections.EMPTY_LIST;

		/****************** Populate Dropdown Data **********************/
		StateList statelst = new StateList();
		statelst.setStateId(Long.parseLong("0"));
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.STATE_URL),
				HttpMethod.POST, PortalUtils.getHttpHeader(statelst, userService), StateList.class);
		StateList stateList = (StateList) responseEntity.getBody();
		stateLists = stateList.getStateLists();

		/*ResponseEntity<?> responseEntityPM = ouInternalRestTemplate.exchange(URI.create(APIURL.APP_CONF_URL),
				HttpMethod.POST, PortalUtils.getHttpHeader("Payment_Mode_Vs_Channel", userService),
				ApplicationConfigList.class);
		ApplicationConfigList pMode = (ApplicationConfigList) responseEntityPM.getBody();
		paymentModes = pMode.getApplicationConfigs();*/
		Set<String> paymentModes = new HashSet<>();
		ResponseEntity<?> responseEntityRoleSet = ouInternalRestTemplate.exchange(URI.create(APIURL.TENENT_PARAM_URL + "/" + "DEFAULT_PAY_CHANNEL"), HttpMethod.GET,
				PortalUtils.getHttpEntity(userService), String.class);
		String defaultPayChannel = (String) responseEntityRoleSet.getBody();
		
		PaymentModeFilterReq paymentModeFilterReq = new PaymentModeFilterReq();
		paymentModeFilterReq.setDefaultPaymentChannel(defaultPayChannel);
		paymentModeFilterReq.setAgentPaymentChannel("Agent");
		
		ResponseEntity<?> responseEntityPM = ouInternalRestTemplate.exchange(URI.create(APIURL.PAYMENT_MODE_LOOKUP_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader(paymentModeFilterReq, userService), PaymentModeFilterResponse.class);
		PaymentModeFilterResponse paymentModeFilterResponse = (PaymentModeFilterResponse) responseEntityPM.getBody();
		for (String paymentMode : paymentModeFilterResponse.getPaymentModes()) {
			paymentModes.add(PaymentMode.getExpPaymentMode(paymentMode).getExpandedForm());
		}

		CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
		commonAPIRequest.setAgentInstId(getPrincipal());
		ResponseEntity<?> responseEntityScheme = ouInternalRestTemplate.exchange(URI.create(APIURL.SCHEME_URL),
				HttpMethod.POST, PortalUtils.getHttpHeader(commonAPIRequest, userService), AgentSchemeList.class);
		AgentSchemeList schemeList = (AgentSchemeList) responseEntityScheme.getBody();
		schemeLists = schemeList.getSchemes();
		
		/**************************Role****************/
		
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (LOG.isDebugEnabled()) {
			LOG.debug("loggedinuserrole :  " + authentication.getAuthorities().toString());
		}
		model.addAttribute("loggedinuserrole", authentication.getAuthorities().toString());
		/*****************
		 * Populate Dropdown Data
		 ******************************/
		model.addAttribute("stateList", stateLists);
		//model.addAttribute("paymentModeViewList", paymentModes);
		model.addAttribute("schemeList", schemeLists);

		if (type.equals("I")) {
			agent.setBusinessType("ABC Business Type");
			agent.setAgentInst(getPrincipal());
			agent.setAgentRegisteredCountry("India");
			agent.setAgentType("AGENT");
			agent.setEntityStatus(EntityStatus.NEW);
			String agentPModes = "";
			for(String pm:paymentModes){
				agentPModes = pm + "," + agentPModes;
			}
			agent.setAgentPaymentMode(agentPModes.substring(0, agentPModes.lastIndexOf(",")));
		} else {
			CommonAPIRequest commonAPIRequest1 = new CommonAPIRequest();
			commonAPIRequest1.setAgentId(agentID);
			ResponseEntity<?> responseEntityAgt = ouInternalRestTemplate.exchange(URI.create(APIURL.View_AGENT_DTL_URL),
					HttpMethod.POST, PortalUtils.getHttpHeader(commonAPIRequest1, userService), AgentDetail.class);
			AgentDetail agentList = (AgentDetail) responseEntityAgt.getBody();

			agent.setAgentId(agentList.getAgentID());
			agent.setAgentName(agentList.getAgentName());
			agent.setAgentAliasName(agentList.getAgentAliasName());
			agent.setAgentInst(agentList.getAgentInst());
			agent.setAgentMobileNo(agentList.getAgentMobile());
			agent.setAgentShopName(agentList.getAgentShopName());
			agent.setBusinessType(agentList.getAgentBusinessType());
			agent.setAgentRegisteredAdrline(agentList.getAgentAddr());
			agent.setAgentRegisteredState(agentList.getAgentState());
			agent.setAgentRegisteredCity(agentList.getAgentCity());
			agent.setAgentRegisteredCountry(agentList.getAgentCountry());
			agent.setAgentRegisteredPinCode(agentList.getAgentPin());
			agent.setAgentType("AGENT");

			StateList statelst1 = new StateList();
			statelst1.setStateId(Long.parseLong(agentList.getAgentState()));
			ResponseEntity<?> responseEntityCity = ouInternalRestTemplate.exchange(URI.create(APIURL.CITY_URL),
					HttpMethod.POST, PortalUtils.getHttpHeader(statelst1, userService), CityList.class);
			CityList cityList = (CityList) responseEntityCity.getBody();
			cityLists = cityList.getCities();

			CityList cityLst = new CityList();
			cityLst.setCityId(Long.parseLong(agentList.getAgentCity()));
			ResponseEntity<?> responseEntity2 = ouInternalRestTemplate.exchange(URI.create(APIURL.POSTALCODE_URL),
					HttpMethod.POST, PortalUtils.getHttpHeader(cityLst, userService), PostalCodeList.class);
			PostalCodeList postalList = (PostalCodeList) responseEntity2.getBody();
			postalLists = postalList.getPostals();

			model.addAttribute("cityList", cityLists);
			model.addAttribute("pinList", postalLists);
			// model.addAttribute("status",
			// EntityStatus.fromValue(agentList.getAgentEntityStatus()));

			// ObjectMapper mapper = new ObjectMapper();

			if (agentList.getAgentPaymentModes() != null) {
				String agentPModes = "";
				for(in.co.rssoftware.bbps.schema.PaymentModeLimit pModeLmt:agentList.getAgentPaymentModes()){
					agentPModes = PaymentMode.getExpPaymentMode(pModeLmt.getPaymentMode()).getExpandedForm() + "," + agentPModes;
				}

				if (!agentPModes.equals("{}")) {
					agent.setAgentPaymentMode(agentPModes.substring(0, agentPModes.lastIndexOf(",")));
					//model.addAttribute("agentPModes", agentPModes.substring(0, agentPModes.lastIndexOf(",")));
				}

				/*
				 * uri = URI.create(APIURL.PAYMENT_MODE_URL); ResponseEntity<?>
				 * responseEntityPM = ouInternalRestTemplate.exchange(uri,
				 * HttpMethod.POST,
				 * PortalUtils.getHttpHeader("Payment_Mode_Vs_Channel",
				 * userService), ApplicationConfigList.class);
				 * ApplicationConfigList pChannel = (ApplicationConfigList)
				 * responseEntityPM.getBody(); List<ParamValue> pChanl =
				 * Collections.EMPTY_LIST; paymentChannel =
				 * pChannel.getApplicationConfigs(); if (paymentChannel != null)
				 * { for (ApplicationConfigDetail acd : paymentChannel) { if
				 * (PaymentMode.getExpPaymentMode(agentPModes).getExpandedForm()
				 * .equals(acd.getConfigParam())) { pChanl =
				 * acd.getConfigParamValues(); } } }
				 * model.addAttribute("pchList", pChanl);
				 */
			}

			/*
			 * if (agentList.getAgentPaymentChannels() != null) { if
			 * (!agentList.getAgentPaymentChannels().isEmpty()) { String
			 * agentPChannels =
			 * agentList.getAgentPaymentChannels().get(0).getPaymentChannel();
			 * 
			 * if (!agentPChannels.equals("{}")) {
			 * model.addAttribute("agentPChannels",
			 * PaymentChannel.getExpPaymentChannel(agentPChannels).
			 * getExpandedForm()); } } }
			 */

			agent.setEntityStatus(com.rssoftware.ou.common.EntityStatus.valueOf(agentList.getAgentEntityStatus()));
			agent.setAgentGeoCode(agentList.getAgentGEOCode());
			agent.setAgentEffctvFrom((agentList.getAgentEffectiveFrom() == ""
					|| agentList.getAgentEffectiveFrom() == null || agentList.getAgentEffectiveFrom().isEmpty()) ? ""
							: agentList.getAgentEffectiveFrom().substring(0, 2) + "/"
									+ agentList.getAgentEffectiveFrom().substring(2, 4) + "/"
									+ agentList.getAgentEffectiveFrom().substring(4));
			agent.setAgentEffctvTo((agentList.getAgentEffectiveTo() == "" || agentList.getAgentEffectiveTo() == null
					|| agentList.getAgentEffectiveFrom().isEmpty()) ? ""
							: agentList.getAgentEffectiveTo().substring(0, 2) + "/"
									+ agentList.getAgentEffectiveTo().substring(2, 4) + "/"
									+ agentList.getAgentEffectiveTo().substring(4));
			agent.setAgentBankAccount(agentList.getAgentBankAccount());
			agent.setAgentSchemeId(agentList.getAgentSchemeId());
			agent.setRejectComment(agentList.getRejectComment());

			if (type.equals("V")) {
				agent.setIsView("Y");
				agent.setIsEdit("N");
			} else {
				agent.setIsView("N");
				agent.setIsEdit("Y");
			}
		}

		model.addAttribute("agent", agent);

		return new ModelAndView("agentOnBoard", "agent", agent);
	}

	@RequestMapping(value = "/saveAgent", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
	public String saveAgent(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String METHOD_NAME = "saveAgent";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		String LinkedAI = null;
		HttpSession httpSession = request.getSession(false);
		if(!httpSession.getId().isEmpty()){
			LinkedAI = httpSession.getAttribute("userRefID").toString();
		}
		System.out.print("\n************Agent Institute Controller For Save Agent Details**********");
		try {
			AgentDetail agentDtl = new AgentDetail();
			agentDtl.setAgentID(request.getParameter("agentId") == null ? null : request.getParameter("agentId"));
			agentDtl.setAgentName(request.getParameter("agentName"));
			agentDtl.setAgentAliasName(request.getParameter("agentAliasName"));
			agentDtl.setAgentInst(LinkedAI);
			agentDtl.setAgentMobile(request.getParameter("agentMobileNo"));
			agentDtl.setAgentShopName(request.getParameter("agentShopName"));
			agentDtl.setAgentBusinessType(request.getParameter("businessType"));
			agentDtl.setAgentAddr(request.getParameter("agentRegisteredAdrline"));
			agentDtl.setAgentState(request.getParameter("agentRegisteredState"));
			agentDtl.setAgentCity(request.getParameter("agentRegisteredCity"));
			agentDtl.setAgentPin(request.getParameter("agentRegisteredPinCode"));
			agentDtl.setAgentCountry(request.getParameter("agentRegisteredCountry"));
			agentDtl.setAgentType("AGT");
			agentDtl.setDummyAgent(false);
			// ObjectMapper mapper = new ObjectMapper();

			if (null != request.getParameterValues("agentPaymentMode")) {
				for(String pMode : request.getParameterValues("agentPaymentMode")[0].split(",")){
				in.co.rssoftware.bbps.schema.PaymentModeLimit paymentMode = new in.co.rssoftware.bbps.schema.PaymentModeLimit();
				paymentMode.setPaymentMode(
						PaymentMode.getFromExpandedForm(pMode).name());
				paymentMode.setLimit(null);
				agentDtl.getAgentPaymentModes().add(paymentMode);
				}
				/*
				 * agentDtl.getAgentPaymentModes().get(0)
				 * .setPaymentMode((mapper.writeValueAsString(request.
				 * getParameter("agentPaymentModes"))));
				 */
			}
			
			/*if (null != request.getParameter("agentPaymentChannels")
					&& !request.getParameter("agentPaymentChannels").isEmpty()) {
				in.co.rssoftware.bbps.schema.PaymentChannelLimit paymentChannel = new in.co.rssoftware.bbps.schema.PaymentChannelLimit();
				paymentChannel.setPaymentChannel(
						PaymentChannel.getFromExpandedForm(request.getParameter("agentPaymentChannels")).name());
				paymentChannel.setLimit(null);
				agentDtl.getAgentPaymentChannels().add(paymentChannel);
			}*/

			agentDtl.setAgentEntityStatus(request.getParameter("entityStatus"));
			agentDtl.setAgentGEOCode(request.getParameter("agentGeoCode"));
			agentDtl.setAgentEffectiveFrom(request.getParameter("agentEffctvFrom") == null ? null
					: request.getParameter("agentEffctvFrom").replaceAll("/", ""));
			agentDtl.setAgentEffectiveTo(request.getParameter("agentEffctvTo") == null ? null
					: request.getParameter("agentEffctvTo").replaceAll("/", ""));
			agentDtl.setAgentBankAccount(request.getParameter("agentBankAccount"));
			agentDtl.setAgentSchemeId(request.getParameter("agentSchemeId"));

			ResponseEntity<?> responseEntity = ouInternalRestTemplate.postForEntity(APIURL.ONBOARD_AGENT_URL,
					PortalUtils.getHttpHeader(agentDtl, userService), null);
			AgentDetail agentList = (AgentDetail) responseEntity.getBody();
			
			redirectAttributes.addFlashAttribute("success",
					"Agent " + agentDtl.getAgentName() + " saved successfully");
		} catch (Exception e) {
			LOG.error("In class AgentInstController >> saveAgent Exception : ", e);
			return "redirect:/agentInstitute/agentView";
		}
		return "redirect:/agentInstitute/agentView";
	}

	/*
	 * @RequestMapping(value = "/agentCheck", method = RequestMethod.GET) public
	 * ModelAndView agentCheck(@ModelAttribute("agent") AgentView agent, Model
	 * model) { String METHOD_NAME = "agentCheck"; if (LOG.isDebugEnabled()) {
	 * LOG.debug("Entering " + METHOD_NAME); } return new
	 * ModelAndView("agentCheck", "agent", agent); }
	 */

	/*
	 * @RequestMapping(value = "/agentPending", method = RequestMethod.POST)
	 * public @ResponseBody JsonResponse agentPendingList() { String METHOD_NAME
	 * = "agentPending"; if (LOG.isDebugEnabled()) { LOG.debug("Entering " +
	 * METHOD_NAME); } JsonResponse json = new JsonResponse(); uri =
	 * URI.create(APIURL.AGENT_PENDING_APPROVAL_URL); CommonAPIRequest
	 * commonAPIRequest = new CommonAPIRequest();
	 * commonAPIRequest.setAgentInstId(getPrincipal()); ResponseEntity<?>
	 * responseEntity = ouInternalRestTemplate.exchange(uri, HttpMethod.POST,
	 * PortalUtils.getHttpHeader(commonAPIRequest, userService),
	 * AgentList.class); AgentList agentList = (AgentList)
	 * responseEntity.getBody(); List<Agent> agentLists =
	 * agentList.getAgentLists(); if (LOG.isDebugEnabled()) { LOG.debug(
	 * "Received List From Api agentPending Length: " +
	 * agentList.getAgentLists().size()); } json.setResult(agentLists); return
	 * json; }
	 */

	@RequestMapping(value = "/agentApprove/{agentId}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse agentApprove(@PathVariable("agentId") String agentId,
			RedirectAttributes redirectAttributes) {
		String METHOD_NAME = "agentApprove";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();

		CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
		commonAPIRequest.setAgentId(agentId);
		ResponseEntity<?> ack = ouInternalRestTemplate.postForEntity(APIURL.AGENT_APPROVE_URL,
				PortalUtils.getHttpHeader(commonAPIRequest, userService), null);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api agentPending Length: " + ack);
		}
		json.setResult("SUCCESS");
		redirectAttributes.addFlashAttribute("success", agentId + " approved successfully");
		return json;
	}

	@RequestMapping(value = "/agentReject/{agentId}/{comment}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse agentReject(@PathVariable("agentId") String agentId, @PathVariable("comment") String comment) {
		String METHOD_NAME = "agentReject";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();

		CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
		commonAPIRequest.setAgentId(agentId);
		commonAPIRequest.setRejectComment(comment);
		ResponseEntity<Ack> ack = ouInternalRestTemplate.postForEntity(APIURL.AGENT_REJECT_URL,
				PortalUtils.getHttpHeader(commonAPIRequest, userService), null);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api agentPending Length: " + ack);
		}
		json.setResult("SUCCESS");
		return json;
	}

	@RequestMapping(value = "/cityList/{stateId}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse fetchCityList(@PathVariable("stateId") String stateId) {
		String METHOD_NAME = "cityList";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();

		StateList statelst = new StateList();
		statelst.setStateId(Long.parseLong(stateId));
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.CITY_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader(statelst, userService), CityList.class);
		CityList cityLists = (CityList) responseEntity.getBody();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api cityList Length: " + cityLists.getCities().size());
		}
		json.setResult(cityLists);
		return json;
	}

	@RequestMapping(value = "/postalList/{cityId}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse fetchPostalList(@PathVariable("cityId") String cityId) {
		String METHOD_NAME = "postalList";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();

		CityList cityLst = new CityList();
		cityLst.setCityId(Long.parseLong(cityId));
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.POSTALCODE_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader(cityLst, userService), PostalCodeList.class);
		PostalCodeList postalLists = (PostalCodeList) responseEntity.getBody();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api portalList Length: " + postalLists.getPostals().size());
		}
		json.setResult(postalLists);
		return json;
	}

	@RequestMapping(value = "/agentReconciliationReport", method = RequestMethod.GET)
	public ModelAndView agentReconciliationReport(@ModelAttribute("agent") AgentView agent, Model model) {
		String METHOD_NAME = "agentReconciliationReport";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("agentReconciliationReport", "agent", agent);
	}

	@RequestMapping(value = "/agentReconReport", method = RequestMethod.POST)
	public @ResponseBody JsonResponse reconReport(HttpServletRequest request, Model model, @RequestBody String str) {
		JSONObject jsonObject;
		JsonResponse json = new JsonResponse();
		ReconSummaryList reconLists = null;

		String LinkedAI = null;
		HttpSession httpSession = request.getSession(false);
		if(!httpSession.getId().isEmpty()){
			LinkedAI = httpSession.getAttribute("userRefID").toString();
		}
		try {
			jsonObject = new JSONObject(str);
			String stDate = jsonObject.getString("startDt");// request.getParameter("datepickerEffctvFrom");
			String endDate = jsonObject.getString("endDt"); // request.getParameter("datepickerEffctvTo");
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.RECON_REPORT_URL + LinkedAI + "?startDate=" + stDate + "&endDate=" + endDate), HttpMethod.POST,
					PortalUtils.getHttpEntity(userService), ReconSummaryList.class);
			reconLists = (ReconSummaryList) responseEntity.getBody();
		} catch (JSONException e) {
			LOG.error("In class AgentInstController >> reconReport JsonException : ", e);
			LOG.error("Error: ", e);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api Length: " + reconLists.getReconSummaryLists().size());
		}

		json.setResult(reconLists.getReconSummaryLists());

		return json;
	}

	@RequestMapping(value = "/agentCommissionReport", method = RequestMethod.GET)
	public ModelAndView agentCommissionReport(@ModelAttribute("agent") AgentView agent, Model model) {
		String METHOD_NAME = "agentCommissionReport";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("agentCommissionReport", "agent", agent);
	}

	@RequestMapping(value = "/agentView", method = RequestMethod.GET)
	public ModelAndView agentView(@ModelAttribute("agent") AgentView agent, Model model) {
		String METHOD_NAME = "agentView";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("agentView", "agent", agent);
	}

	@RequestMapping(value = "/agentList", method = RequestMethod.POST)
	public @ResponseBody JsonResponse agentAllList(HttpServletRequest request) {
		String METHOD_NAME = "agentAllList";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		String LinkedAI = null;
		HttpSession httpSession = request.getSession(false);
		if(!httpSession.getId().isEmpty()){
			LinkedAI = httpSession.getAttribute("userRefID").toString();
		}

		CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
		commonAPIRequest.setAgentInstId(LinkedAI);
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.View_AGENT_LIST_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader(commonAPIRequest, userService), AgentList.class);
		AgentList agentList = (AgentList) responseEntity.getBody();
		List<Agent> agentLists = agentList.getAgentLists();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api All agentList Length: " + agentList.getAgentLists().size());
		}
		json.setResult(agentLists);

		return json;
	}

	@RequestMapping(value = "/agentScheme", method = RequestMethod.POST)
	public @ResponseBody JsonResponse agentSchemeList() {
		String METHOD_NAME = "agentSchemeList";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}
		JsonResponse json = new JsonResponse();
		List<Scheme> schemeLists = Collections.EMPTY_LIST;

		CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
		commonAPIRequest.setAgentInstId(getPrincipal());
		ResponseEntity<?> responseEntityScheme = ouInternalRestTemplate.exchange(URI.create(APIURL.SCHEME_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader(commonAPIRequest, userService), AgentSchemeList.class);
		AgentSchemeList schemeList = (AgentSchemeList) responseEntityScheme.getBody();
		schemeLists = schemeList.getSchemes();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Received List From Api agent Scheme Length: " + schemeList.getSchemes().size());
		}
		json.setResult(schemeLists);

		return json;
	}

	@RequestMapping(value = "/agentDtlView/{agentID}", method = RequestMethod.POST)
	public ModelAndView fetchAgentDetails(@ModelAttribute("agent") AgentView agent, ModelMap model,
			@PathVariable("agentID") String agentID) {
		String METHOD_NAME = "fetchAgentDetails";
		if (LOG.isDebugEnabled()) {
			LOG.debug("Entering " + METHOD_NAME);
		}

		List<StateDetail> stateLists = Collections.EMPTY_LIST;
		List<ApplicationConfigDetail> paymentModes = Collections.EMPTY_LIST;
		List<Scheme> schemeLists = Collections.EMPTY_LIST;

		try {
			CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
			commonAPIRequest.setAgentInstId(agentID);
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.View_AGENT_DTL_URL), HttpMethod.POST,
					PortalUtils.getHttpHeader(commonAPIRequest, userService), AgentDetail.class);
			AgentDetail agentList = (AgentDetail) responseEntity.getBody();

			agent.setAgentId(agentList.getAgentID());
			agent.setAgentName(agentList.getAgentName());
			agent.setAgentAliasName(agentList.getAgentAliasName());
			agent.setAgentInst(agentList.getAgentInst());
			agent.setAgentMobileNo(agentList.getAgentMobile());
			agent.setAgentShopName(agentList.getAgentShopName());
			agent.setBusinessType(agentList.getAgentBusinessType());
			agent.setAgentRegisteredAdrline(agentList.getAgentAddr());
			ObjectMapper mapper = new ObjectMapper();

			if (agentList.getAgentPaymentModes() != null) {
				String agentPModes = agentList.getAgentPaymentModes().get(0).getPaymentMode();

				if (!agentPModes.equals("{}")) {
					List<PaymentModeLimit> paymentModeList = mapper.readValue(agentPModes,
							TypeFactory.defaultInstance().constructCollectionType(List.class, PaymentModeLimit.class));
					agent.setAgentPaymentModes(paymentModeList);
				}
			}

			if (agentList.getAgentPaymentChannels() != null) {
				String agentPChannels = agentList.getAgentPaymentChannels().get(0).getPaymentChannel();

				if (!agentPChannels.equals("{}")) {
					List<PaymentChannelLimit> paymentChannelList = mapper.readValue(agentPChannels, TypeFactory
							.defaultInstance().constructCollectionType(List.class, PaymentChannelLimit.class));
					agent.setAgentPaymentChannels(paymentChannelList);
				}
			}

			agent.setAgentGeoCode(agentList.getAgentGEOCode());
			agent.setAgentEffctvFrom(agentList.getAgentEffectiveFrom());
			agent.setAgentEffctvTo(agentList.getAgentEffectiveFrom());
			agent.setAgentBankAccount(agentList.getAgentBankAccount());
			agent.setAgentSchemeId(agentList.getAgentSchemeId());

			agent.setIsView("Y");
			agent.setIsEdit("N");

			populateFields(stateLists, paymentModes, schemeLists);
			model.addAttribute("stateList", stateLists);
			model.addAttribute("paymentModeViewList", paymentModes);
			model.addAttribute("schemeList", schemeLists);
			model.addAttribute("agent", agent);

		} catch (JsonParseException e) {
			LOG.error("In class AgentInstController >> fetchAgentDetails JsonParseException : ", e);
		} catch (JsonMappingException e) {
			LOG.error("In class AgentInstController >> fetchAgentDetails JsonMappingException : ", e);
		} catch (IOException e) {
			LOG.error("In class AgentInstController >> fetchAgentDetails IOException : ", e);
		}

		return new ModelAndView("agentOnBoard", "agent", agent);
		/*
		 * ModelAndView modelAndView = new ModelAndView();
		 * modelAndView.addObject("redirectUrl", "/agentBoarding"); return
		 * modelAndView;
		 */
	}

	private void populateFields(List<StateDetail> stateLists, List<ApplicationConfigDetail> paymentModes,
			List<Scheme> schemeLists) {
		try {
			StateList statelst = new StateList();
			statelst.setStateId(Long.parseLong("0"));
			ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.STATE_URL),
					HttpMethod.POST, PortalUtils.getHttpHeader(statelst, userService), StateList.class);
			StateList stateList = (StateList) responseEntity.getBody();
			stateLists = stateList.getStateLists();

			ResponseEntity<?> responseEntityPM = ouInternalRestTemplate.exchange(URI.create(APIURL.APP_CONF_URL),
					HttpMethod.POST, PortalUtils.getHttpHeader("Payment_Mode_Vs_Channel", userService),
					ApplicationConfigList.class);
			ApplicationConfigList pMode = (ApplicationConfigList) responseEntityPM.getBody();
			paymentModes = pMode.getApplicationConfigs();

			CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
			commonAPIRequest.setAgentInstId(getPrincipal());
			ResponseEntity<?> responseEntityScheme = ouInternalRestTemplate.exchange(URI.create(APIURL.SCHEME_URL),
					HttpMethod.POST, PortalUtils.getHttpHeader(commonAPIRequest, userService), AgentSchemeList.class);
			AgentSchemeList schemeList = (AgentSchemeList) responseEntityScheme.getBody();
			schemeLists = schemeList.getSchemes();

		} catch (Exception e) {
			LOG.error("In class AgentInstController >> populateFields Excp : ", e);
		}
	}

	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}

}