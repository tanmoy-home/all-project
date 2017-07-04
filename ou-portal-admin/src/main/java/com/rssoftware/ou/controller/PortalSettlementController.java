package com.rssoftware.ou.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rssoftware.ou.common.APIURL;
import com.rssoftware.ou.common.PortalUtils;
import com.rssoftware.ou.domain.JsonResponse;
import com.rssoftware.ou.model.tenant.ReconFileViews;
import com.rssoftware.ou.service.UserService;

@Controller
@RequestMapping(value = "/settlement")
public class PortalSettlementController {

	private static final Logger log = LoggerFactory.getLogger(PortalSettlementController.class);

	private static OUInternalRestTemplate ouInternalRestTemplate = OUInternalRestTemplate.createInstance();

	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/cuSettlementFiles", method = RequestMethod.GET)
	public ModelAndView cuSettlementFiles(Model model) {
		String METHOD_NAME = "cuSettlementFiles";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		return new ModelAndView("viewCuSettlementFiles");
	}

	@RequestMapping(value = "/viewCuSettlementFiles", method = RequestMethod.POST)
	public @ResponseBody JsonResponse viewCuSettlementFiles(Model model) {
		String METHOD_NAME = "viewCuSettlementFiles";
		JsonResponse json = new JsonResponse();
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		ResponseEntity<?> responseEntity = ouInternalRestTemplate.exchange(URI.create(APIURL.CU_SETLMNT_REPORT_URL),
				HttpMethod.POST, PortalUtils.getHttpEntity(userService), ReconFileViews.class);
		ReconFileViews reconFileViews = (ReconFileViews) responseEntity.getBody();
		if (log.isDebugEnabled()) {
			log.debug("Received List From Api Length: " + reconFileViews.getReconFileViews().size());
		}
		json.setResult(reconFileViews.getReconFileViews());
		return json;
	}

}