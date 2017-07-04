package com.rssoftware.ou.portal.web.service.impl;

import java.util.LinkedHashMap;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.awl.merchanttoolkit.dto.ReqMsgDTO;
import com.awl.merchanttoolkit.transaction.AWLMEAPI;
import com.rssoftware.ou.portal.web.modal.BillInfoDTO;

@Component
public class PayByIDBI extends PayByFormSubmission {

	private final static Logger logger = LoggerFactory.getLogger(PayByIDBI.class);
	public static String SUBMIT_URL;
	public static String MID;
	public static String ENCRIPTION_KEY;
	private ReqMsgDTO reqMsgDTO;

	@Value("${ou.tenantId}")
	private String tenantId;
	
	@Value("${ou.domain}")
	private String uri;
	
	@Override
	public Object pay(BillInfoDTO billInfoDTO) {
		logger.info("Paying through IDBI bank.....");

		// If payment context not present load this first.
		if (getPgParams() == null) {
			loadPGParams(tenantId, uri);
		}

		// Adding the values of pg_integration_fields those are not present in
		// the table.
		for (Object obj : getPgParams()) {
			LinkedHashMap map = (LinkedHashMap) obj;
			setValueToDTO(map);
		}

		// Setting Submit URL, MID, Encryption Key
		PayByIDBI.SUBMIT_URL = getPGParamValue("ME_PAY_REQUEST");
		PayByIDBI.MID = getPGParamValue("MID");
		PayByIDBI.ENCRIPTION_KEY = getPGParamValue("ENCKEY");

		// Transaction amount in paisa format
		String amount = billInfoDTO.getTotalAmtToPaid();
		reqMsgDTO.setTrnAmt(amount);

		// return getPgParams();
		//return generateRequestMessageForMerchant(reqMsgDTO);
		return reqMsgDTO;
	}

	/*
	 * @PostConstruct public void init() { logger.info(
	 * "Initilizing PayByIDBI bean."); loadContext(); logger.info(
	 * "Initilization complete."); }
	 */

	@PreDestroy
	public void destroy() {

	}

	public static String generateRequestMessageForMerchant(ReqMsgDTO obj) {

		String merchantRequest = null;
		try {
			// Initialise object to generate transaction request message
			AWLMEAPI transactMeAPI = new AWLMEAPI();
			ReqMsgDTO objReqMsgDTO = transactMeAPI.generateTrnReqMsg(obj);

			// Check status desciption for message generation
			if (objReqMsgDTO.getStatusDesc().equals("Success")) {
				merchantRequest = objReqMsgDTO.getReqMsg();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return merchantRequest;
	}

	private void setValueToDTO(LinkedHashMap map) {
		if (reqMsgDTO == null) {
			reqMsgDTO = new ReqMsgDTO();
		}

		// Merchant ID
		if ("MID".equals(map.get("paramName"))) {
			reqMsgDTO.setMid(PayByIDBI.MID);

		}

		// Merchant Encryption key
		if ("ENCKEY".equals(map.get("paramName"))) {
			reqMsgDTO.setEnckey(map.get("paramValue").toString());
		}

		// Merchant transaction currency
		if ("CRN".equals(map.get("paramName"))) {
			reqMsgDTO.setTrnCurrency(map.get("paramValue").toString());
		}

		// Transaction remarks
		reqMsgDTO.setTrnRemarks("Test Transaction.");

		// Merchant request type S/P/R [S=single, P=?, R=recurring]
		if ("MREQTYP".equals(map.get("paramName"))) {
			reqMsgDTO.setMeTransReqType(map.get("paramValue").toString());

		}

		// Recurring period (M/W))if merchant request type is R [M=monthly,
		// W=weekly]
		if ("RECPRD".equals(map.get("paramName")) && "R".equals(reqMsgDTO.getMeTransReqType())) {
			reqMsgDTO.setRecurrPeriod(map.get("paramValue").toString());

		}

		// Recurring day if merchant request type is R: Recurring Payment
		if ("RECDAY".equals(map.get("paramName")) && "R".equals(reqMsgDTO.getMeTransReqType())) {
			reqMsgDTO.setRecurrDay(map.get("paramValue").toString());

		}

		// No of recurring if merchant request type is R
		if ("RECNUM".equals(map.get("paramName")) && "R".equals(reqMsgDTO.getMeTransReqType())) {
			reqMsgDTO.setNoOfRecurring(map.get("paramValue").toString());

		}

		// Merchant response URL
		if ("RU".equals(map.get("paramName"))) {
			reqMsgDTO.setResponseUrl(map.get("paramValue").toString());
		}

		// Optional Addition fields for Merchant use
		// reqMsgDTO.setAddField1(request.getParameter("addField1"));
	}
	
	
	@Override
	public String populateModel(Model model) {
		return "idbi_payment_form";
	}
	
}
