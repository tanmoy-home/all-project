
package com.rssoftware.ou.portal.web.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.rssoftware.ou.portal.web.modal.BillInfoDTO;

@Component
public class PayBySIB extends PayByFormSubmission {

	private final static Logger logger = LoggerFactory.getLogger(PayBySIB.class);
	public static String SUBMIT_URL;
	public static String MID;
	private SIBReqDTO reqMsgDTO;
	public static String INTERMEDIATE_PAGE = "sib_payment_form";

	@Value("${ou.tenantId}")
	private String tenantId;

	@Value("${ou.domain}")
	private String uri;

	@Override
	public Object pay(BillInfoDTO billInfoDTO) {
		logger.info("Paying throygh SIB bank.....");

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

		// Setting Submit URL
		PayBySIB.SUBMIT_URL = getPGParamValue("REQ_URL");

		// Transaction amount in paisa format
		String amount = billInfoDTO.getTotalAmtToPaid();

		// Bank code, Mandatory
		reqMsgDTO.setBankID(getPGParamValue("BANK_ID"));

		// MD Optional, Default value P
		reqMsgDTO.setMD("P");

		// Payee ID. Mandatory
		reqMsgDTO.setPID("200711");

		// Unique reference number, Setting bill number. Mandatory
		reqMsgDTO.setITC(billInfoDTO.getBillNumber());

		// Payee reference number, Mandatory
		reqMsgDTO.setPRN(getPGParamValue("PAYEE_REF_NO"));

		// Amount to be paid. Mandatory
		reqMsgDTO.setAMT(billInfoDTO.getTotalAmtToPaid());

		// Currency Code
		reqMsgDTO.setCRN("INR");

		// Return URL, Mandatory
		reqMsgDTO.setRU(getPGParamValue("RET_URL"));

		// Default values
		reqMsgDTO.setCG("Y");
		reqMsgDTO.setUserLangID("001");
		reqMsgDTO.setUserType("1");
		reqMsgDTO.setAppType("corporate");
		reqMsgDTO.setCS("");
		reqMsgDTO.setSTATFLG("H");

		// Merchant name, Mandatory
		reqMsgDTO.setMerchantName(getPGParamValue("MERCHANT_NAME"));

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

	public String populateModel(Model model) {
		model.addAttribute("payment_page_heading", "This page will be autometically submitted to SIB's payment page.");
		model.addAttribute("submit_url", PayBySIB.SUBMIT_URL);
		model.addAttribute("reqMsgDTO", reqMsgDTO);
		return "sib_payment_form";
	}

	private void setValueToDTO(LinkedHashMap map) {
		if (reqMsgDTO == null) {
			reqMsgDTO = new SIBReqDTO();
		}
	}

	public class SIBReqDTO {
		private String BankID;
		private String MD;
		private String PID;
		private String ITC;
		private String PRN;
		private String AMT;
		private String CRN;
		private String RU;
		private String CG;
		private String userLangID;
		private String UserType;
		private String AppType;
		private String CS;
		private String STATFLG;
		private String merchantName;

		public String getBankID() {
			return BankID;
		}

		public void setBankID(String bankID) {
			BankID = bankID;
		}

		public String getMD() {
			return MD;
		}

		public void setMD(String mD) {
			MD = mD;
		}

		public String getPID() {
			return PID;
		}

		public void setPID(String pID) {
			PID = pID;
		}

		public String getITC() {
			return ITC;
		}

		public void setITC(String iTC) {
			ITC = iTC;
		}

		public String getPRN() {
			return PRN;
		}

		public void setPRN(String pRN) {
			PRN = pRN;
		}

		public String getAMT() {
			return AMT;
		}

		public void setAMT(String aMT) {
			AMT = aMT;
		}

		public String getCRN() {
			return CRN;
		}

		public void setCRN(String cRN) {
			CRN = cRN;
		}

		public String getRU() {
			return RU;
		}

		public void setRU(String rU) {
			RU = rU;
		}

		public String getCG() {
			return CG;
		}

		public void setCG(String cG) {
			CG = cG;
		}

		public String getUserLangID() {
			return userLangID;
		}

		public void setUserLangID(String userLangID) {
			this.userLangID = userLangID;
		}

		public String getUserType() {
			return UserType;
		}

		public void setUserType(String userType) {
			UserType = userType;
		}

		public String getAppType() {
			return AppType;
		}

		public void setAppType(String appType) {
			AppType = appType;
		}

		public String getCS() {
			return CS;
		}

		public void setCS(String cS) {
			CS = cS;
		}

		public String getSTATFLG() {
			return STATFLG;
		}

		public void setSTATFLG(String sTATFLG) {
			STATFLG = sTATFLG;
		}

		public String getMerchantName() {
			return merchantName;
		}

		public void setMerchantName(String merchantName) {
			this.merchantName = merchantName;
		}

		@Override
		public String toString() {
			return "SIBReqDTO [BankID=" + BankID + ", MD=" + MD + ", PID=" + PID + ", ITC=" + ITC + ", PRN=" + PRN
					+ ", AMT=" + AMT + ", CRN=" + CRN + ", RU=" + RU + ", CG=" + CG + ", userLangID=" + userLangID
					+ ", UserType=" + UserType + ", AppType=" + AppType + ", CS=" + CS + ", STATFLG=" + STATFLG
					+ ", merchantName=" + merchantName + "]";
		}

	}

	public class SIBResDTO {
		private String BankId;
		private String MD;
		private String PID;
		private String CRN;
		private String CG;
		private String USER_LANG_ID;
		private String UserType;
		private String AppType;
		private String CS;
		private String PRN;
		private String ITC;
		private String AMT;
		private String BID;
		private String STATFLG;
		private String merchantname;
		private String PAID;
		private String RU;
		private String BTXN_AMT;
		private String BTXN_TIME;
		private String ErrorMsg;
		private String ext_key1;
		private String ext_key2;
		private String CUSTOMERROR;

		public String getBankId() {
			return BankId;
		}

		public void setBankId(String bankId) {
			BankId = bankId;
		}

		public String getMD() {
			return MD;
		}

		public void setMD(String mD) {
			MD = mD;
		}

		public String getPID() {
			return PID;
		}

		public void setPID(String pID) {
			PID = pID;
		}

		public String getCRN() {
			return CRN;
		}

		public void setCRN(String cRN) {
			CRN = cRN;
		}

		public String getCG() {
			return CG;
		}

		public void setCG(String cG) {
			CG = cG;
		}

		public String getUSER_LANG_ID() {
			return USER_LANG_ID;
		}

		public void setUSER_LANG_ID(String uSER_LANG_ID) {
			USER_LANG_ID = uSER_LANG_ID;
		}

		public String getUserType() {
			return UserType;
		}

		public void setUserType(String userType) {
			UserType = userType;
		}

		public String getAppType() {
			return AppType;
		}

		public void setAppType(String appType) {
			AppType = appType;
		}

		public String getCS() {
			return CS;
		}

		public void setCS(String cS) {
			CS = cS;
		}

		public String getPRN() {
			return PRN;
		}

		public void setPRN(String pRN) {
			PRN = pRN;
		}

		public String getITC() {
			return ITC;
		}

		public void setITC(String iTC) {
			ITC = iTC;
		}

		public String getAMT() {
			return AMT;
		}

		public void setAMT(String aMT) {
			AMT = aMT;
		}

		public String getBID() {
			return BID;
		}

		public void setBID(String bID) {
			BID = bID;
		}

		public String getSTATFLG() {
			return STATFLG;
		}

		public void setSTATFLG(String sTATFLG) {
			STATFLG = sTATFLG;
		}

		public String getMerchantname() {
			return merchantname;
		}

		public void setMerchantname(String merchantname) {
			this.merchantname = merchantname;
		}

		public String getPAID() {
			return PAID;
		}

		public void setPAID(String pAID) {
			PAID = pAID;
		}

		public String getRU() {
			return RU;
		}

		public void setRU(String rU) {
			RU = rU;
		}

		public String getBTXN_AMT() {
			return BTXN_AMT;
		}

		public void setBTXN_AMT(String bTXN_AMT) {
			BTXN_AMT = bTXN_AMT;
		}

		public String getBTXN_TIME() {
			return BTXN_TIME;
		}

		public void setBTXN_TIME(String bTXN_TIME) {
			BTXN_TIME = bTXN_TIME;
		}

		public String getErrorMsg() {
			return ErrorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			ErrorMsg = errorMsg;
		}

		public String getExt_key1() {
			return ext_key1;
		}

		public void setExt_key1(String ext_key1) {
			this.ext_key1 = ext_key1;
		}

		public String getExt_key2() {
			return ext_key2;
		}

		public void setExt_key2(String ext_key2) {
			this.ext_key2 = ext_key2;
		}

		public String getCUSTOMERROR() {
			return CUSTOMERROR;
		}

		public void setCUSTOMERROR(String cUSTOMERROR) {
			CUSTOMERROR = cUSTOMERROR;
		}

		public void digest(Map<String, String> requestParams) {
			// SIBResDTO resMsgDTO = new SIBResDTO();
			for (Map.Entry<String, String> entry : requestParams.entrySet()) {

				if (entry.getKey().equals("BankId")) {
					this.setBankId(entry.getValue());
				} else if (entry.getKey().equals("MD")) {
					this.setMD(entry.getValue());
				} else if (entry.getKey().equals("PID")) {
					this.setPID(entry.getValue());
				} else if (entry.getKey().equals("CRN")) {
					this.setCRN(entry.getValue());
				} else if (entry.getKey().equals("CG")) {
					this.setCG(entry.getValue());
				} else if (entry.getKey().equals("USER_LANG_ID")) {
					this.setUSER_LANG_ID(entry.getValue());
				} else if (entry.getKey().equals("UserType")) {
					this.setUserType(entry.getValue());
				} else if (entry.getKey().equals("AppType")) {
					this.setAppType(entry.getValue());
				} else if (entry.getKey().equals("CS")) {
					this.setCS(entry.getValue());
				} else if (entry.getKey().equals("PRN")) {
					this.setPRN(entry.getValue());
				} else if (entry.getKey().equals("ITC")) {
					this.setITC(entry.getValue());
				} else if (entry.getKey().equals("AMT")) {
					this.setAMT(entry.getValue());
				} else if (entry.getKey().equals("BID")) {
					this.setBID(entry.getValue());
				} else if (entry.getKey().equals("STATFLG")) {
					this.setSTATFLG(entry.getValue());
				} else if (entry.getKey().equals("merchantname")) {
					this.setMerchantname(entry.getValue());
				} else if (entry.getKey().equals("PAID")) {
					this.setPAID(entry.getValue());
				} else if (entry.getKey().equals("RU")) {
					this.setRU(entry.getValue());
				} else if (entry.getKey().equals("BTXN_AMT")) {
					this.setBTXN_AMT(entry.getValue());
				} else if (entry.getKey().equals("BTXN_TIME")) {
					this.setBTXN_TIME(entry.getValue());
				} else if (entry.getKey().equals("ErrorMsg")) {
					this.setErrorMsg(entry.getValue());
				} else if (entry.getKey().equals("ext_key1")) {
					this.setExt_key1(entry.getValue());
				} else if (entry.getKey().equals("ext_key2")) {
					this.setExt_key2(entry.getValue());
				} else if (entry.getKey().equals("CUSTOMERROR")) {
					this.setCUSTOMERROR(entry.getValue());
				}
			}
			// return resMsgDTO;
		}
	}

}
