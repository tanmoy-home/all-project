package com.rssoftware.ou.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rssoftware.ou.common.APIURL;
import com.rssoftware.ou.common.CommonAPIRequest;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.PortalUtils;
import com.rssoftware.ou.domain.JsonResponse;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.PaymentModeView;
import com.rssoftware.ou.service.UserService;

import in.co.rssoftware.bbps.schema.Agent;
import in.co.rssoftware.bbps.schema.AgentDetail;
import in.co.rssoftware.bbps.schema.AgentInst;
import in.co.rssoftware.bbps.schema.AgentInstList;
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
import in.co.rssoftware.bbps.schema.Scheme;
import in.co.rssoftware.bbps.schema.StateDetail;
import in.co.rssoftware.bbps.schema.StateList;

@Controller
@RequestMapping(value = "/COUAdmin")
public class PortalCOUController {
	private static final Logger Log = LoggerFactory.getLogger(PortalCOUController.class);

	private static OUInternalRestTemplate ouInternalRestTemplate = OUInternalRestTemplate.createInstance();
	
	@Autowired
	UserService userService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/agentBoarding/{type}/{agentID}", method = RequestMethod.GET)
	public ModelAndView agentRegistration(@ModelAttribute("agent") AgentView agent, Model model,
			@PathVariable("type") String type, @PathVariable("agentID") String agentID) {
		String METHOD_NAME = "agentBoarding";
		if (Log.isDebugEnabled()) {
			Log.debug("Entering " + METHOD_NAME);
		}
		List<StateDetail> stateLists = Collections.EMPTY_LIST;
		List<CityDetail> cityLists = Collections.EMPTY_LIST;
		List<PostalDetail> postalLists = Collections.EMPTY_LIST;

		List<Scheme> schemeLists = Collections.EMPTY_LIST;
		List<AgentInst> agentInstList = Collections.EMPTY_LIST;
		Map<String, Set<String>> payModeMap = Collections.EMPTY_MAP;
						
		/****************** Populate Dropdown Data **********************/
		StateList statelst = new StateList();
		statelst.setStateId(Long.parseLong("0"));
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.STATE_URL),
				HttpMethod.POST, PortalUtils.getHttpHeader(statelst, userService), StateList.class);
		StateList stateList = (StateList) responseEntity.getBody();
		stateLists = stateList.getStateLists();
		
		ResponseEntity<?> responseEntityPM = ouInternalRestTemplate.exchange(URI.create(APIURL.APP_CONF_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader("Payment_Mode_Vs_Channel", userService), ApplicationConfigList.class);
		ApplicationConfigList pMode = (ApplicationConfigList) responseEntityPM.getBody();
		payModeMap = new HashMap<>();
		Set<String> payModesSet = null;
		for (ApplicationConfigDetail applicationConfigDetail : pMode.getApplicationConfigs()) {
			List<ParamValue> configParamsLst = applicationConfigDetail.getConfigParamValues();
			for (ParamValue payChannel : configParamsLst) {
				if (null == payModeMap.get(payChannel.getParamElement())) {
					payModesSet = new HashSet<>();
				} else {
					payModesSet = payModeMap.get(payChannel.getParamElement());

				}
				payModesSet.add(applicationConfigDetail.getConfigParam());
				payModeMap.put(payChannel.getParamElement(), payModesSet);
			}

		}
		
		ResponseEntity<?> responseEntityAI = ouInternalRestTemplate.exchange(URI.create(APIURL.AI_LIST_URL), HttpMethod.POST,
				PortalUtils.getHttpEntity(userService), AgentInstList.class);
		AgentInstList AI = (AgentInstList) responseEntityAI.getBody();
		agentInstList = AI.getAgentInsts();

		CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
		commonAPIRequest.setAgentInstId(getPrincipal());
		ResponseEntity<?> responseEntityScheme = ouInternalRestTemplate.exchange(URI.create(APIURL.SCHEME_URL),
				HttpMethod.POST, PortalUtils.getHttpHeader(commonAPIRequest, userService), AgentSchemeList.class);
		AgentSchemeList schemeList = (AgentSchemeList) responseEntityScheme.getBody();
		schemeLists = schemeList.getSchemes();
		
		/**************************Role****************/
		
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Log.isDebugEnabled()) {
			Log.debug("loggedinuserrole :  " + authentication.getAuthorities().toString());
		}
		model.addAttribute("loggedinuserrole", authentication.getAuthorities().toString());
		/*****************
		 * Populate Dropdown Data
		 ******************************/
		
		model.addAttribute("stateList", stateLists);
		model.addAttribute("paymentChannelViewList", payModeMap);
		model.addAttribute("schemeList", schemeLists);
		model.addAttribute("agentInstList", agentInstList);

		if (type.equals("I")) {
			agent.setBusinessType("ABC Business Type");
			agent.setAgentRegisteredCountry("India");
			agent.setEntityStatus(EntityStatus.NEW);
			agent.setDummyAgent(true);
		} else {
			CommonAPIRequest commonAPIRequestAgt = new CommonAPIRequest();
			commonAPIRequestAgt.setAgentId(agentID);
			ResponseEntity<?> responseEntityAgt = ouInternalRestTemplate.exchange(URI.create(APIURL.View_AGENT_DTL_URL), HttpMethod.POST,
					PortalUtils.getHttpHeader(commonAPIRequestAgt, userService), AgentDetail.class);
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
			agent.setAgentType(agentList.getAgentType());
			agent.setDummyAgent(agentList.isDummyAgent());

			StateList statelst1 = new StateList();
			statelst1.setStateId(Long.parseLong(agentList.getAgentState()));
			ResponseEntity<?> responseEntityCity = ouInternalRestTemplate.exchange(URI.create(APIURL.CITY_URL), HttpMethod.POST,
					PortalUtils.getHttpHeader(statelst, userService), CityList.class);
			CityList cityList = (CityList) responseEntityCity.getBody();
			cityLists = cityList.getCities();

			CityList cityLst = new CityList();
			cityLst.setCityId(Long.parseLong(agentList.getAgentCity()));
			ResponseEntity<?> responseEntity2 = ouInternalRestTemplate.exchange(URI.create(APIURL.POSTALCODE_URL), HttpMethod.POST,
					PortalUtils.getHttpHeader(cityLst, userService), PostalCodeList.class);
			PostalCodeList postalList = (PostalCodeList) responseEntity2.getBody();
			postalLists = postalList.getPostals();

			model.addAttribute("cityList", cityLists);
			model.addAttribute("pinList", postalLists);
			
			if (agentList.getAgentPaymentModes() != null) {
				String agentPModes = "";
				for(in.co.rssoftware.bbps.schema.PaymentModeLimit pModeLmt:agentList.getAgentPaymentModes()){
					agentPModes = PaymentMode.getExpPaymentMode(pModeLmt.getPaymentMode()).getExpandedForm() + "," + agentPModes;
				}

				if (!agentPModes.equals("{}")) {
					agent.setAgentPaymentMode(agentPModes.substring(0, agentPModes.lastIndexOf(",")));
					//model.addAttribute("agentPModes", agentPModes.substring(0, agentPModes.lastIndexOf(",")));
				}
			}

			/*if (agentList.getAgentPaymentModes() != null) {
				String agentPModes = agentList.getAgentPaymentModes().get(0).getPaymentMode();

				if (!agentPModes.equals("{}")) {
					model.addAttribute("agentPModes", PaymentMode.getExpPaymentMode(agentPModes).getExpandedForm());
				}
			}*/

			if (agentList.getAgentPaymentChannels() != null) {
				if (!agentList.getAgentPaymentChannels().isEmpty()) {
					String agentPChannels = agentList.getAgentPaymentChannels().get(0).getPaymentChannel();

					if (!agentPChannels.equals("{}")) {
						model.addAttribute("agentPChannels",
								PaymentChannel.getExpPaymentChannel(agentPChannels).getExpandedForm());
					}
				}
			}

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

		return new ModelAndView("agentOnBoardCOU", "agent", agent);
	}

	@RequestMapping(value = "/saveAgent", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
	public String saveAgent(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		String METHOD_NAME = "saveAgent";
		if (Log.isDebugEnabled()) {
			Log.debug("Entering " + METHOD_NAME);
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
			agentDtl.setAgentInst(request.getParameter("agentInst"));
			agentDtl.setAgentMobile(request.getParameter("agentMobileNo"));
			agentDtl.setAgentShopName(request.getParameter("agentShopName"));
			agentDtl.setAgentBusinessType(request.getParameter("businessType"));
			agentDtl.setAgentAddr(request.getParameter("agentRegisteredAdrline"));
			agentDtl.setAgentState(request.getParameter("agentRegisteredState"));
			agentDtl.setAgentCity(request.getParameter("agentRegisteredCity"));
			agentDtl.setAgentPin(request.getParameter("agentRegisteredPinCode"));
			agentDtl.setAgentCountry(request.getParameter("agentRegisteredCountry"));
			agentDtl.setAgentType(request.getParameter("agentType"));
			agentDtl.setDummyAgent(request.getParameter("dummyAgent") != null ? true : false);
			// ObjectMapper mapper = new ObjectMapper();

			/*if (null != request.getParameter("agentPaymentModes")
					&& !request.getParameter("agentPaymentModes").isEmpty()) {
				in.co.rssoftware.bbps.schema.PaymentModeLimit paymentMode = new in.co.rssoftware.bbps.schema.PaymentModeLimit();
				paymentMode.setPaymentMode(
						PaymentMode.getFromExpandedForm(request.getParameter("agentPaymentModes")).name());
				paymentMode.setLimit(null);
				agentDtl.getAgentPaymentModes().add(paymentMode);

				
				 * agentDtl.getAgentPaymentModes().get(0)
				 * .setPaymentMode((mapper.writeValueAsString(request.
				 * getParameter("agentPaymentModes"))));
				 
			}*/
			
			if (null != request.getParameterValues("agentPaymentMode")) {
				for(String pMode : request.getParameterValues("agentPaymentMode")[0].split(",")){
					if(pMode != null){
						in.co.rssoftware.bbps.schema.PaymentModeLimit paymentMode = new in.co.rssoftware.bbps.schema.PaymentModeLimit();
						paymentMode.setPaymentMode(
								PaymentMode.getFromExpandedForm(pMode).name());
						paymentMode.setLimit(null);
						agentDtl.getAgentPaymentModes().add(paymentMode);
					}
				}
			}

			if (request.getParameter("agentPaymentChannels") != null
					&& !request.getParameter("agentPaymentChannels").isEmpty()) {
				in.co.rssoftware.bbps.schema.PaymentChannelLimit paymentChannel = new in.co.rssoftware.bbps.schema.PaymentChannelLimit();
				paymentChannel.setPaymentChannel(
						PaymentChannel.getFromExpandedForm(request.getParameter("agentPaymentChannels")).name());
				paymentChannel.setLimit(null);
				agentDtl.getAgentPaymentChannels().add(paymentChannel);
			}

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
			redirectAttributes.addFlashAttribute("success", "Agent " + agentDtl.getAgentName() + " saved successfully");
			 redirectAttributes.addFlashAttribute("agentInst", request.getParameter("agentInst"));
		} catch (Exception e) {
			Log.error("In class PortalCOUController >> saveAgent: ", e);
			return "redirect:/COUAdmin/agentViewCOU";
		}
		return "redirect:/COUAdmin/agentViewCOU";
	}

	@RequestMapping(value = "/agentTypeList", method = RequestMethod.POST)
	public @ResponseBody JsonResponse fetchAgentTypeList() {
		JsonResponse json = new JsonResponse();
		String METHOD_NAME = "fetchAgentTypeList";
		if (Log.isDebugEnabled()) {
			Log.debug("Entering " + METHOD_NAME);
		}
		
		List<ApplicationConfigDetail> agentTypeLists = Collections.EMPTY_LIST;
		
		ResponseEntity<?> responseEntityAgentType = ouInternalRestTemplate.exchange(URI.create(APIURL.APP_CONF_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader("AGENT_TYPE", userService), ApplicationConfigList.class);
		ApplicationConfigList aType = (ApplicationConfigList) responseEntityAgentType.getBody();
		agentTypeLists = aType.getApplicationConfigs();

		if (Log.isDebugEnabled()) {
			Log.debug("Received List From Api agentTypeList Length: " + aType.getApplicationConfigs().size());
		}
		json.setResult(agentTypeLists);
		return json;
	}

	@RequestMapping(value = "/agentViewCOU", method = RequestMethod.GET)
	public ModelAndView agentView(@ModelAttribute("agent") AgentView agent, Model model) {
		String METHOD_NAME = "agentView";
		if (Log.isDebugEnabled()) {
			Log.debug("Entering " + METHOD_NAME);
		}
		List<AgentInst> agentInstList = Collections.EMPTY_LIST;
		ResponseEntity<?> responseEntityAI = ouInternalRestTemplate.exchange(URI.create(APIURL.AI_LIST_URL), HttpMethod.POST,
				PortalUtils.getHttpEntity(userService), AgentInstList.class);
		AgentInstList AI = (AgentInstList) responseEntityAI.getBody();
		agentInstList = AI.getAgentInsts();
		model.addAttribute("agentInstList", agentInstList);
		model.addAttribute("agent", agent);
		return new ModelAndView("agentViewCOU", "agent", agent);
	}

	@RequestMapping(value = "/agentList/{agentInstID}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse agentAllList(@PathVariable("agentInstID") String agentInstID) {
		JsonResponse json = new JsonResponse();
		String METHOD_NAME = "agentAllList";
		if (Log.isDebugEnabled()) {
			Log.debug("Entering " + METHOD_NAME);
		}
		CommonAPIRequest commonAPIRequest = new CommonAPIRequest();
		commonAPIRequest.setAgentInstId(agentInstID);
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.View_AGENT_LIST_URL), HttpMethod.POST,
				PortalUtils.getHttpHeader(commonAPIRequest, userService), AgentList.class);
		AgentList agentList = (AgentList) responseEntity.getBody();
		List<Agent> agentLists = new ArrayList<Agent>();
		if (!agentList.getAgentLists().isEmpty()) {
			for (Agent agent : agentList.getAgentLists()) {
				if (!"AGT".equalsIgnoreCase(agent.getAgentType())) {
					agentLists.add(agent);
				}
			}
		}
		if (Log.isDebugEnabled()) {
			Log.debug("Received List From Api All agentList Length: " + agentList.getAgentLists().size());
		}
		json.setResult(agentLists);

		return json;
	}
	
	@RequestMapping(value = "/paymentMode/{pmode}", method = RequestMethod.POST)
	public @ResponseBody JsonResponse fetchPaymentChennal(@PathVariable("pmode") String pmode) {
		JsonResponse json = new JsonResponse();
		String METHOD_NAME = "fetchPaymentChennal";
		if (Log.isDebugEnabled()) {
			Log.debug("Entering " + METHOD_NAME);
		}

		ResponseEntity<?> responseEntityRoleSet = ouInternalRestTemplate.exchange(URI.create(APIURL.TENENT_PARAM_URL + "/" + "DEFAULT_PAY_CHANNEL"), HttpMethod.GET,
				PortalUtils.getHttpEntity(userService), String.class);
		String defaultPayChannel = (String) responseEntityRoleSet.getBody();
		
		// Map<String,Set<String>> payModeMap = new HashMap<>();
		Set<String> payModesSet = new HashSet<>();
		/*if (pmode.equals("PM")) {
			for (ApplicationConfigDetail applicationConfigDetail : pMode.getApplicationConfigs()) {
				List<ParamValue> configParamsLst = applicationConfigDetail.getConfigParamValues();
				// payModesSet =
				for (ParamValue payChannel : configParamsLst) {
					payModesSet.add(applicationConfigDetail.getConfigParam());
				}
			}
		} else {*/
			PaymentModeFilterReq paymentModeFilterReq = new PaymentModeFilterReq();
			paymentModeFilterReq.setDefaultPaymentChannel(defaultPayChannel);
			paymentModeFilterReq.setAgentPaymentChannel(pmode);
			
			ResponseEntity<?> responseEntityPM = ouInternalRestTemplate.exchange(URI.create(APIURL.PAYMENT_MODE_LOOKUP_URL), HttpMethod.POST,
					PortalUtils.getHttpHeader(paymentModeFilterReq, userService), PaymentModeFilterResponse.class);
			PaymentModeFilterResponse paymentModeFilterResponse = (PaymentModeFilterResponse) responseEntityPM.getBody();
			for (String paymentMode : paymentModeFilterResponse.getPaymentModes()) {
					payModesSet.add(PaymentMode.getExpPaymentMode(paymentMode).getExpandedForm());
			}
			
			
			
			/*for (ApplicationConfigDetail applicationConfigDetail : pMode.getApplicationConfigs()) {
				List<ParamValue> configParamsLst = applicationConfigDetail.getConfigParamValues();
				// payModesSet =
				for (ParamValue payChannel : configParamsLst) {
					if (payChannel.getParamElement().equals(pmode)) {
						payModesSet.add(applicationConfigDetail.getConfigParam());
					}
				}
			}*/
		/*}*/
		if (Log.isDebugEnabled()) {
			Log.debug("Received List From Api paymentChannel Length: " + payModesSet.size());
		}
		json.setResult(payModesSet);
		return json;
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