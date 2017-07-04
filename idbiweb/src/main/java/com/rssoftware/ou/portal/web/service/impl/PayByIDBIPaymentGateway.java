package com.rssoftware.ou.portal.web.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.awl.merchanttoolkit.dto.ReqMsgDTO;
import com.awl.merchanttoolkit.dto.ResMsgDTO;
import com.awl.merchanttoolkit.transaction.AWLMEAPI;
import com.rssoftware.ou.portal.web.dto.BillInfoDTO;


@Component
public class PayByIDBIPaymentGateway extends PayByFormSubmission {

	private final static Logger logger = LoggerFactory.getLogger(PayByIDBIPaymentGateway.class);
	public String SUBMIT_URL;
	public String MID;
	public String ENCRIPTION_KEY;
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
		this.SUBMIT_URL = getPGParamValue("IDBI_REQ_URL");
		this.MID = getPGParamValue("IDBI_MID");
		this.ENCRIPTION_KEY = getPGParamValue("IDBI_ENCKEY");

		// Transaction amount in paisa format
		String amount = billInfoDTO.getTotalAmtToPaid();
		
		int transactionAmount = Double.valueOf(amount).intValue();
		transactionAmount = transactionAmount * 100;
		
		int taxAmount = Double.valueOf(amount).intValue();
		int totalAmount = transactionAmount + taxAmount;
		
		reqMsgDTO.setTrnAmt(String.valueOf(transactionAmount));
		reqMsgDTO.setTaxAmount(billInfoDTO.getCcfTax());
		
		// Setting gross amount.
		reqMsgDTO.setGrossTrnAmt(String.valueOf(totalAmount));
		
		reqMsgDTO.setOrderId(billInfoDTO.getTxnRefId());
		
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

	private String generateRequestMessageForMerchant(ReqMsgDTO obj) {

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
		if ("IDBI_MID".equals(map.get("paramName"))) {
			reqMsgDTO.setMid(map.get("paramValue").toString());

		}

		// Merchant Encryption key
		if ("IDBI_ENCKEY".equals(map.get("paramName"))) {
			reqMsgDTO.setEnckey(map.get("paramValue").toString());
		}

		// Merchant transaction currency
		if ("IDBI_CRN".equals(map.get("paramName"))) {
			reqMsgDTO.setTrnCurrency(map.get("paramValue").toString());
		}

		// Transaction remarks
		reqMsgDTO.setTrnRemarks("Test Transaction.");

		// Merchant request type S/P/R [S=single, P=?, R=recurring]
		if ("IDBI_MREQTYP".equals(map.get("paramName"))) {
			reqMsgDTO.setMeTransReqType(map.get("paramValue").toString());

		}

		// Recurring period (M/W))if merchant request type is R [M=monthly,
		// W=weekly]
		if ("IDBI_RECPRD".equals(map.get("paramName")) && "R".equals(reqMsgDTO.getMeTransReqType())) {
			reqMsgDTO.setRecurrPeriod(map.get("paramValue").toString());

		}

		// Recurring day if merchant request type is R: Recurring Payment
		if ("IDBI_RECDAY".equals(map.get("paramName")) && "R".equals(reqMsgDTO.getMeTransReqType())) {
			reqMsgDTO.setRecurrDay(map.get("paramValue").toString());

		}

		// No of recurring if merchant request type is R
		if ("IDBI_RECNUM".equals(map.get("paramName")) && "R".equals(reqMsgDTO.getMeTransReqType())) {
			reqMsgDTO.setNoOfRecurring(map.get("paramValue").toString());

		}

		// Merchant response URL
		if ("IDBI_RU".equals(map.get("paramName"))) {
			reqMsgDTO.setResponseUrl(map.get("paramValue").toString());
		}

		// Optional Addition fields for Merchant use
		// reqMsgDTO.setAddField1(request.getParameter("addField1"));
	}
	
	
	@Override
	public String populateModel(Model model) {
		model.addAttribute("payment_page_heading", "This page will be autometically submitted to IDBI's payment page.");
		model.addAttribute("submit_url", this.SUBMIT_URL);
		model.addAttribute("MID", this.MID);
		model.addAttribute("merchantRequest", generateRequestMessageForMerchant(reqMsgDTO));
		return "idbi_payment_form";
	}
	
	@Override
	public Object digestResponseParams(Map<String, String> responseParams) {
		AWLMEAPI transactMeAPI = new AWLMEAPI();
		try {
			ResMsgDTO resMsgDTO = transactMeAPI.parseTrnResMsg(responseParams.get("merchantResponse"), this.ENCRIPTION_KEY);
			return resMsgDTO;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
}
