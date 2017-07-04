package com.rssoftware.ou.portal.web.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.awl.merchanttoolkit.dto.ReqMsgDTO;
import com.awl.merchanttoolkit.transaction.AWLMEAPI;
import com.rssoftware.ou.portal.web.modal.BillInfoDTO;
import com.rssoftware.ou.portal.web.utils.AESEncryptionUtils;

@Component
public class PayByPNB extends PayByFormSubmission {

	private final static Logger logger = LoggerFactory.getLogger(PayByPNB.class);
	public static String SUBMIT_URL;
	public static String MID;
	public static String ENCRIPTION_KEY;
	private PNBReqDTO reqMsgDTO;
	private Map<String, String> submitParams;
	public static String INTERMEDIATE_PAGE = "pnb_payment_form";

	@Value("${ou.tenantId}")
	private String tenantId;

	@Value("${ou.domain}")
	private String uri;

	@Override
	public Object pay(BillInfoDTO billInfoDTO) {
		logger.info("Paying throygh PNB bank.....");

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
		PayByPNB.SUBMIT_URL = getPGParamValue("REQ_URL");
		PayByPNB.ENCRIPTION_KEY = getPGParamValue("ENC_KEY");

		// Transaction amount in paisa format
		String amount = billInfoDTO.getTotalAmtToPaid();
		reqMsgDTO.setAmount(amount);
		// submitParams.put("amt", getKeyValuePairAsString("amt",amount));

		// Merchant date "dd/MM/yyyy hh:mm:ss"
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		Date merchantDate = new Date(System.currentTimeMillis());
		reqMsgDTO.setMerchantDate(dateFormat.format(merchantDate));

		reqMsgDTO.setName("Sanjib Pal");
		reqMsgDTO.setAddress("Pairagacha, Janai, Hooghly, 712304");
		reqMsgDTO.setEmail("sanjibpal.india@gmail.com");
		reqMsgDTO.setPhone("8159938535");
		reqMsgDTO.setRemark("Test transaction");
		reqMsgDTO.setCin(billInfoDTO.getBillNumber());

		// try {
		// Key key = AESEncryptionUtils.genRandumKey();
		//
		// System.out.println("Originated Text === " + reqMsgDTO.getReqMsg());
		// System.out.println(" Encrypted Text === " +
		// AESEncryptionUtils.encrypt(reqMsgDTO.getReqMsg(), key));
		// System.out.println(" Decrypted Text === " +
		// AESEncryptionUtils.decrypt(AESEncryptionUtils.encrypt(reqMsgDTO.getReqMsg(),
		// key), key));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

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
		model.addAttribute("payment_page_heading", "This page will be autometically submitted to PNB's payment page.");
		model.addAttribute("submit_url", PayByPNB.SUBMIT_URL);
		model.addAttribute("encdata", reqMsgDTO.getReqMsg(PayByPNB.ENCRIPTION_KEY));

		// For testing return success.
		PNBResDTO resMsgDTO = new PNBResDTO();
		resMsgDTO.setBankamount("1.00");
		resMsgDTO.setBankdate("06-01-2016");
		resMsgDTO.setBankstatus("S");
		resMsgDTO.setStatusdesc("SUCCESS");
		resMsgDTO.setBanktransid("25468578");
		resMsgDTO.setCin(reqMsgDTO.getCin());
		model.addAttribute("encdata", resMsgDTO.getResMsg(PayByPNB.ENCRIPTION_KEY));
		model.addAttribute("encdata", "");

		return "pnb_payment_form";
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

	// private String getKeyValuePairAsString(Object key, Object value) {
	// return key.toString() + "=" + value.toString();
	// }

	private void setValueToDTO(LinkedHashMap map) {
		if (reqMsgDTO == null) {
			reqMsgDTO = new PNBReqDTO();
		}

		if ("RET_URL".equals(map.get("paramName"))) {
			reqMsgDTO.setReturnURL(map.get("paramValue").toString());

		}
		// submitParams.put(map.get("paramName").toString(),
		// getKeyValuePairAsString(map.get("paramName"),map.get("paramValue")));
	}

	public class PNBReqDTO {
		private String name;
		private String address;
		private String email;
		private String phone;
		private String merchantDate;
		private String amount;
		private String remark;
		private String cin;
		private String returnURL;
		private String checksum;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getMerchantDate() {
			return merchantDate;
		}

		public void setMerchantDate(String merchantDate) {
			this.merchantDate = merchantDate;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getCin() {
			return cin;
		}

		public void setCin(String cin) {
			this.cin = cin;
		}

		public String getReturnURL() {
			return returnURL;
		}

		public void setReturnURL(String returnURL) {
			this.returnURL = returnURL;
		}

		public String getChecksum() {
			return checksum;
		}

		public void setChecksum(String checksum) {
			this.checksum = checksum;
		}

		public String getReqMsg(String encKey) {
			String originalMsg = "name=" + name + "|address=" + address + "|email=" + email + "|phone=" + phone
					+ "|merchantdate=" + merchantDate + "|amt=" + amount + "|remark=" + remark + "|cin=" + cin + "|RU="
					+ returnURL;

			try {
				String checksum = AESEncryptionUtils.generateMD5Checksum(originalMsg);
				this.setChecksum(checksum);
				return AESEncryptionUtils.encrypt(originalMsg + "|checksum=" + checksum, encKey.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String toString() {
			return "PNBReqDTO [name=" + name + ", address=" + address + ", email=" + email + ", phone=" + phone
					+ ", merchantDate=" + merchantDate + ", amount=" + amount + ", remark=" + remark + ", cin=" + cin
					+ ", returnURL=" + returnURL + "]";
		}

	}

	public class PNBResDTO {
		private String cin;
		private String banktransid;
		private String bankdate;
		private String bankamount;
		private String bankstatus;
		private String statusdesc;
		private String checksum;

		public String getCin() {
			return cin;
		}

		public void setCin(String cin) {
			this.cin = cin;
		}

		public String getBanktransid() {
			return banktransid;
		}

		public void setBanktransid(String banktransid) {
			this.banktransid = banktransid;
		}

		public String getBankdate() {
			return bankdate;
		}

		public void setBankdate(String bankdate) {
			this.bankdate = bankdate;
		}

		public String getBankamount() {
			return bankamount;
		}

		public void setBankamount(String bankamount) {
			this.bankamount = bankamount;
		}

		public String getBankstatus() {
			return bankstatus;
		}

		public void setBankstatus(String bankstatus) {
			this.bankstatus = bankstatus;
		}

		public String getStatusdesc() {
			return statusdesc;
		}

		public void setStatusdesc(String statusdesc) {
			this.statusdesc = statusdesc;
		}

		public String getChecksum() {
			return checksum;
		}

		public void setChecksum(String checksum) {
			this.checksum = checksum;
		}

		public PNBResDTO parseResMsg(String resMsg) {
			PNBResDTO resMsgDto = null;
			try {

				String decodedText = AESEncryptionUtils.decrypt(resMsg, PayByPNB.ENCRIPTION_KEY);
				String resChecksum = null;
				if (decodedText.indexOf('|') != -1) {
					resChecksum = AESEncryptionUtils
							.generateMD5Checksum(decodedText.substring(0, decodedText.lastIndexOf('|')));
				}

				String[] fieldValues = decodedText.split("\\|");
				if (fieldValues.length == 7) {
					resMsgDto = new PNBResDTO();
					for (String keyValue : fieldValues) {
						setResMsgDtoAttribute(resMsgDto, keyValue);
					}
					// Validating checksum.
					if (resChecksum != null && resChecksum.equals(resMsgDto.getChecksum())) {
						return resMsgDto;
					} else {
						logger.error("Checksum not validated.");
					}
				}

			} catch (Exception e) {
				logger.error("Not able to parse the response message.", e);
			}
			return resMsgDto;
		}

		public void digest(Map<String, String> requestParams) {

			String resMsg = requestParams.get("encdata");

			try {

				String decodedText = AESEncryptionUtils.decrypt(resMsg, PayByPNB.ENCRIPTION_KEY);
				String resChecksum = null;
				if (decodedText.indexOf('|') != -1) {
					resChecksum = AESEncryptionUtils
							.generateMD5Checksum(decodedText.substring(0, decodedText.lastIndexOf('|')));
				}

				String[] fieldValues = decodedText.split("\\|");
				String checksumString = fieldValues[fieldValues.length - 1];
				String checksum = checksumString.substring(checksumString.indexOf('=') + 1);

				if (fieldValues.length == 7 && checksum.equals(resChecksum)) {
					for (String keyValue : fieldValues) {
						setResMsgDtoAttribute(this, keyValue);
					}
				} else {
					logger.error("Checksum not validated.");
				}

			} catch (Exception e) {
				logger.error("Not able to parse the response message.", e);
			}
		}

		private void setResMsgDtoAttribute(PNBResDTO resMsgDto, String keyValue) {
			String key = null, value = null;
			if (keyValue.indexOf('=') != -1) {
				key = keyValue.substring(0, keyValue.indexOf('='));
				value = keyValue.substring(keyValue.indexOf('=') + 1);
			}

			if (key.equals("cin")) {
				resMsgDto.setCin(value);
			}
			if (key.equals("banktransid")) {
				resMsgDto.setBanktransid(value);
			}
			if (key.equals("bankamount")) {
				resMsgDto.setBankamount(value);
			}
			if (key.equals("bankdate")) {
				resMsgDto.setBankdate(value);
			}
			if (key.equals("bankstatus")) {
				resMsgDto.setBankstatus(value);
			}
			if (key.equals("statusdesc")) {
				resMsgDto.setStatusdesc(value);
			}
			if (key.equals("checksum")) {
				resMsgDto.setChecksum(value);
			}
		}

		public String getResMsg(String encKey) {
			String originalMsg = "cin=" + cin + "|banktransid=" + banktransid + "|bankdate=" + bankdate + "|bankamount="
					+ bankamount + "|bankstatus=" + bankstatus + "|statusdesc=" + statusdesc;

			try {
				return AESEncryptionUtils.encrypt(
						originalMsg + "|checksum=" + AESEncryptionUtils.generateMD5Checksum(originalMsg),
						encKey.getBytes("UTF-8"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String toString() {
			return "PNBResDTO [cin=" + cin + ", banktransid=" + banktransid + ", bankdate=" + bankdate + ", bankamount="
					+ bankamount + ", bankstatus=" + bankstatus + ", statusdesc=" + statusdesc + ", checksum="
					+ checksum + "]";
		}

	}
}
