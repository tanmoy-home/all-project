package com.rssoftware.ou.common.utils;

import java.util.List;

import org.bbps.schema.AgentType;
import org.bbps.schema.AnalyticsType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillerType;
import org.bbps.schema.CustomerDtlsType;
import org.bbps.schema.CustomerParamsType;
import org.bbps.schema.DeviceTagNameType;
import org.bbps.schema.DeviceType;
import org.bbps.schema.HeadType;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.PymntInfType;
import org.bbps.schema.QckPayType;
import org.bbps.schema.RiskScoresType;
import org.bbps.schema.SpltPayType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.RiskScoresType.Score;
import org.bbps.schema.TxnType;
import org.springframework.util.MultiValueMap;

import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;

public class RequestResponseGenerator {

	public static HeadType getHead(String ouName, String refId) {
		HeadType head = new HeadType();
		head.setVer("1.0");
		head.setTs(CommonUtils.getFormattedCurrentTimestamp());
		head.setOrigInst(ouName);
		head.setRefId(refId);		
		return head;
	}
	
	public static AnalyticsType getAnalytics() {
		AnalyticsType analytics = new AnalyticsType();
		AnalyticsType.Tag fetchRequestStartTag = new AnalyticsType.Tag();
		fetchRequestStartTag.setName("FETCHREQUESTSTART");
		fetchRequestStartTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(fetchRequestStartTag);

		AnalyticsType.Tag fetchRequestEndTag = new AnalyticsType.Tag();
		fetchRequestEndTag.setName("FETCHREQUESTEND");
		fetchRequestEndTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(fetchRequestEndTag);
		
		return analytics;
	}
	
	public static AnalyticsType getPaymentAnalytics() {
		AnalyticsType analytics = new AnalyticsType();
		AnalyticsType.Tag fetchRequestStartTag = new AnalyticsType.Tag();
		fetchRequestStartTag.setName("PAYREQUESTSTART");
		fetchRequestStartTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(fetchRequestStartTag);

		AnalyticsType.Tag fetchRequestEndTag = new AnalyticsType.Tag();
		fetchRequestEndTag.setName("PAYREQUESTEND");
		fetchRequestEndTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(fetchRequestEndTag);
		
		return analytics;
	}

	public static TxnType getTxn(String ouName, String msgId) {
		TxnType txn = new TxnType();
		txn.setTs(CommonUtils.getFormattedCurrentTimestamp());
		
		txn.setMsgId(msgId);//idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, ouName));
		RiskScoresType riskScore = new RiskScoresType();
		Score score = new Score();
		score.setProvider(ouName);
		score.setType("TXNRISK");
		score.setValue("000"); // populate a default score now
		riskScore.getScores().add(score);
		txn.setRiskScores(riskScore);
		return txn;
	}
	
	public static CustomerDtlsType getCustomer(MultiValueMap<String, Object> params) {
		CustomerDtlsType customer = new CustomerDtlsType();
		customer.setMobile(params.get("mobileNum").get(0).toString());//form

		if (params.get("emailId") != null && !params.get("emailId").isEmpty() && params.get("emailId").get(0) != null
				&& CommonUtils.hasValue(params.get("emailId").get(0).toString())){
			CustomerDtlsType.Tag emailTag = new CustomerDtlsType.Tag();
			emailTag.setName("EMAIL");
			emailTag.setValue(params.get("emailId").get(0).toString());// form
			customer.getTags().add(emailTag);
		}

		if (params.get("pan") != null && !params.get("pan").isEmpty() && params.get("pan").get(0) != null
				&& CommonUtils.hasValue(params.get("pan").get(0).toString())){
			CustomerDtlsType.Tag panTag = new CustomerDtlsType.Tag();
			panTag.setName("PAN");
			panTag.setValue(params.get("pan").get(0).toString());// form
			customer.getTags().add(panTag);
		}

		if (params.get("aadhaarNum") != null && !params.get("aadhaarNum").isEmpty() && params.get("aadhaarNum").get(0) != null
				&& CommonUtils.hasValue(params.get("aadhaarNum").get(0).toString())){
			CustomerDtlsType.Tag aadhaarTag = new CustomerDtlsType.Tag();
			aadhaarTag.setName("AADHAAR");
			aadhaarTag.setValue(params.get("aadhaarNum").get(0).toString());// form
			customer.getTags().add(aadhaarTag);
		}
		return customer;
	}
	
	public static BillDetailsType getBillDetails(String billerId, MultiValueMap<String, Object> params, List<ParamConfig> paramConfigList) {
		BillDetailsType billDetails = new BillDetailsType();
		BillerType biller = new BillerType();
		biller.setId(billerId);
		billDetails.setBiller(biller);
		
		CustomerParamsType customerParams = new CustomerParamsType();

		if (paramConfigList != null){
			for (ParamConfig pc:paramConfigList){
				String paramName = CommonUtils.escapeSpaces(pc.getParamName());
				
				if (params.get(paramName) != null && !params.get(paramName).isEmpty() && params.get(paramName).get(0) != null
						&& CommonUtils.hasValue(params.get(paramName).get(0).toString())){
					CustomerParamsType.Tag customerParamTag = new CustomerParamsType.Tag();
					customerParamTag.setName(pc.getParamName());// form
					customerParamTag.setValue(params.get(paramName).get(0).toString().trim());// form
					customerParams.getTags().add(customerParamTag);
				}
			}
		} 
		
		billDetails.setCustomerParams(customerParams);
		return billDetails;
	}
	
	public static AgentType getAgent(String agentId) {
		AgentType agent = new AgentType();
		agent.setId(agentId);		
		return agent;
	}

	public static DeviceType getAgentDeviceForInternetBankingChannel(String ipAddress, String defaultMacAddress) {
		DeviceType device = new DeviceType();
		DeviceType.Tag initChannelTag = new DeviceType.Tag();
		initChannelTag.setName(DeviceTagNameType.INITIATING_CHANNEL);
		initChannelTag.setValue(PaymentChannel.Internet_Banking.getExpandedForm());
		device.getTags().add(initChannelTag);
		
		device.getTags().add(getIPTag(ipAddress));
		device.getTags().add(getMacTag(defaultMacAddress));
		return device;
	}
	
	public static DeviceType getAgentDeviceForMobileChannel(String ipAddress, String imei, String os, String app) {
		DeviceType device = new DeviceType();
		DeviceType.Tag initChannelTag = new DeviceType.Tag();
		initChannelTag.setName(DeviceTagNameType.INITIATING_CHANNEL);
		initChannelTag.setValue(PaymentChannel.Mobile.getExpandedForm());
		device.getTags().add(initChannelTag);

		device.getTags().add(getIPTag(ipAddress));	
		device.getTags().add(getImeiTag(imei));
		device.getTags().add(getOsTag(os));
		device.getTags().add(getAppTag(app));
		return device;
	}
	
	public static DeviceType getAgentDeviceForPosChannel(String terminalId, String mobile, String geoCode, String postalCode) {
		return getCommonDeviceTypeForPOSAndMPOS(PaymentChannel.POS, terminalId, mobile, geoCode, postalCode);
	}
	
	public static DeviceType getAgentDeviceForMPosChannel(String terminalId, String mobile, String geoCode, String postalCode) {
		return getCommonDeviceTypeForPOSAndMPOS(PaymentChannel.MPOS, terminalId, mobile, geoCode, postalCode);
	}
	
	public static DeviceType getAgentDeviceForATMChannel(String terminalId) {
		DeviceType device = new DeviceType();
		DeviceType.Tag initChannelTag = new DeviceType.Tag();
		initChannelTag.setName(DeviceTagNameType.INITIATING_CHANNEL);
		initChannelTag.setValue(PaymentChannel.ATM.getExpandedForm());
		device.getTags().add(initChannelTag);

		device.getTags().add(getTerminalIdTag(terminalId));
		return device;
	}
	
	public static DeviceType getAgentDeviceForBankBranchChannel(String ifsc, String mobile, String geoCode, String postalCode) {
		DeviceType device = new DeviceType();
		DeviceType.Tag initChannelTag = new DeviceType.Tag();
		initChannelTag.setName(DeviceTagNameType.INITIATING_CHANNEL);
		initChannelTag.setValue(PaymentChannel.Bank_Branch.getExpandedForm());
		device.getTags().add(initChannelTag);

		device.getTags().add(getIFSCTag(ifsc));
		device.getTags().add(getMobileTag(mobile));
		device.getTags().add(getGeoCodeTag(geoCode));
		device.getTags().add(getPostalCodeTag(postalCode));
		return device;
	}

	public static DeviceType getAgentDeviceForKioskChannel(String terminalId) {
		DeviceType device = new DeviceType();
		DeviceType.Tag initChannelTag = new DeviceType.Tag();
		initChannelTag.setName(DeviceTagNameType.INITIATING_CHANNEL);
		initChannelTag.setValue(PaymentChannel.Kiosk.getExpandedForm());
		device.getTags().add(initChannelTag);

		device.getTags().add(getTerminalIdTag(terminalId));
		return device;
	}
	
	private static DeviceType getCommonDeviceTypeForPOSAndMPOS(PaymentChannel paymentChannel, String terminalId, String mobile, String geoCode, String postalCode) {
		DeviceType device = new DeviceType();
		DeviceType.Tag initChannelTag = new DeviceType.Tag();
		initChannelTag.setName(DeviceTagNameType.INITIATING_CHANNEL);
		initChannelTag.setValue(paymentChannel.getExpandedForm());
		device.getTags().add(initChannelTag);

		device.getTags().add(getTerminalIdTag(terminalId));
		device.getTags().add(getMobileTag(mobile));
		device.getTags().add(getGeoCodeTag(geoCode));
		device.getTags().add(getPostalCodeTag(postalCode));
		return device;
	}

	
	public static TxnType getTxnForBillPayment(String ouName, String msgId, String txnRefId, TransactionType transactionType) {
			TxnType txn = new TxnType();
			txn.setTs(CommonUtils.getFormattedCurrentTimestamp());			
			txn.setMsgId(msgId);
			
			RiskScoresType riskScore = new RiskScoresType();
			Score score = new Score();
			score.setProvider(ouName);
			score.setType("TXNRISK");
			score.setValue("000"); // populate a default score now
			riskScore.getScores().add(score);
			txn.setRiskScores(riskScore);
			
			riskScore.getScores().add(score);
			txn.setRiskScores(riskScore);
			txn.setTxnReferenceId(txnRefId);
			txn.setType(transactionType.value());
			
			return txn;
	}	
	
	public static PmtMtdType getCashAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Cash.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}
	
	public static PmtMtdType getCreditCardAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Credit_Card.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}
	
	public static PmtMtdType getDebitCardAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Debit_Card.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}	

	public static PmtMtdType getIMPSAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.IMPS.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}	

	public static PmtMtdType getPrepaidCardAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Prepaid_Card.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}	

	public static PmtMtdType getNEFTAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.NEFT.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}	

	public static PmtMtdType getUPIAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.UPI.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}	

	public static PmtMtdType getWalletAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Wallet.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;		
	}	


	public static PmtMtdType getInternetBankingAsPaymentModeWithoutSplitAndQuickPay() {		
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		
		return pmtMtdType;
	}
	
	public static PmtMtdType getInternetBankingAsPaymentModeWithQuickPay() {		
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.YES);
		
		return pmtMtdType;
	}
	
	public static PmtMtdType getInternetBankingAsPaymentModeWithSplitPay() {		
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.YES);
		pmtMtdType.setQuickPay(QckPayType.NO);
		
		return pmtMtdType;
	}
	
		public static PmtMtdType getInternetBankingAsPaymentMode(boolean isQuickpay, boolean isSplitPay) {
		PmtMtdType pmtMtdType = new PmtMtdType();
		pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		pmtMtdType.setSplitPay(SpltPayType.NO);
		pmtMtdType.setQuickPay(QckPayType.NO);
		if(isSplitPay) pmtMtdType.setSplitPay(SpltPayType.YES);
		if(isQuickpay) pmtMtdType.setQuickPay(QckPayType.YES);		
		return pmtMtdType;
	}
	
	public static PymntInfType getPaymentInformation(PaymentMode paymentMode, String paymentInstrument) {
		if(paymentMode ==  PaymentMode.Cash) {
			return getPaymentInformationAsRemarks(paymentInstrument);
		} else if(paymentMode ==  PaymentMode.Credit_Card || paymentMode ==  PaymentMode.Debit_Card || paymentMode ==  PaymentMode.Prepaid_Card) {
			return getPaymentInformationAsCardNumberAndAuthCode(paymentInstrument);
		} else if(paymentMode ==  PaymentMode.IMPS) {
			return getPaymentInformationAsMMIDAndMobileNo(paymentInstrument);
		} else if(paymentMode ==  PaymentMode.Wallet) {
			return getPaymentInformationAsWalletNameAndMobileNumber(paymentInstrument);
		} else if(paymentMode ==  PaymentMode.NEFT) {
			return getPaymentInformationAsWalletNameAndMobileNumber(paymentInstrument);
		} else if(paymentMode ==  PaymentMode.UPI) {
			return getPaymentInformationAsVPA(paymentInstrument);
		}
		return getPaymentInformationAsIFSCAndAccountNumber(paymentInstrument); //default for internet banking and others
	}

	public static PymntInfType getPaymentInformationWithBlankIFSCAndAccount() {
		return getDefaultPaymentInformation(CommonConstants.PAYMENT_INFO_IFSC_AC, CommonConstants.EMPTY_STRING);
	}
	
	public static PymntInfType getPaymentInformationAsIFSCAndAccountNumber(String ifscAndAccountNumber) {
		return getDefaultPaymentInformation(CommonConstants.PAYMENT_INFO_IFSC_AC, ifscAndAccountNumber);
	}
	
	public static PymntInfType getPaymentInformationAsRemarks(String paymentRemarks) {
		return getDefaultPaymentInformation(CommonConstants.PAYMENT_INFO_REMARKS, paymentRemarks);
	}
	
	public static PymntInfType getPaymentInformationAsCardNumberAndAuthCode(String cardNumberAndAuthCode) {
		return getDefaultPaymentInformation(CommonConstants.PAYMENT_INFO_CARD_AUTHCODE, cardNumberAndAuthCode);
	}
	
	public static PymntInfType getPaymentInformationAsMMIDAndMobileNo(String mmidAndMobileNumber) {
		return getDefaultPaymentInformation(CommonConstants.PAYMENT_INFO_MMID_MOBNUM, mmidAndMobileNumber);
	}
	
	public static PymntInfType getPaymentInformationAsWalletNameAndMobileNumber(String walletNameAndMobileNumber) {
		return getDefaultPaymentInformation(CommonConstants.PAYMENT_INFO_WALLET_MOBNUM, walletNameAndMobileNumber);
	}
	public static PymntInfType getPaymentInformationAsVPA(String vpa) {
		return getDefaultPaymentInformation(CommonConstants.PAYMENT_INFO_VPA, vpa);
	}
	
	public static PymntInfType getDefaultPaymentInformation(String paymentInformationTagName, String paymentInformationTagValue) {
		PymntInfType pymntInfo = new PymntInfType();
		PymntInfType.Tag ptag = new PymntInfType.Tag();
		ptag.setName(paymentInformationTagName);
		ptag.setValue(paymentInformationTagValue);
		pymntInfo.getTags().add(ptag);
		return pymntInfo;
	}
	
	private static DeviceType.Tag getIPTag(String ipAddress) {
		DeviceType.Tag ipTag = new DeviceType.Tag();
		ipTag.setName(DeviceTagNameType.IP);
		ipTag.setValue(ipAddress);
		return ipTag;
	}
	
	private static DeviceType.Tag getMacTag(String defaultMacAddress) {
		String mac = CommonUtils.getMacAddress();
		if (mac == null || "".equals(mac.trim())){
			mac = defaultMacAddress;
		}
		DeviceType.Tag macTag = new DeviceType.Tag();
		macTag.setName(DeviceTagNameType.MAC);
		macTag.setValue(mac);
		return macTag;
	}
	
	private static DeviceType.Tag getImeiTag(String imei) {
		DeviceType.Tag imeiTag = new DeviceType.Tag();
		imeiTag.setName(DeviceTagNameType.IMEI);
		imeiTag.setValue(imei);
		return imeiTag;
	}

	private static DeviceType.Tag getOsTag(String os) {
		DeviceType.Tag osTag = new DeviceType.Tag();
		osTag.setName(DeviceTagNameType.OS);
		osTag.setValue(os);
		return osTag;
	}
	
	private static DeviceType.Tag getAppTag(String app) {
		DeviceType.Tag osTag = new DeviceType.Tag();
		osTag.setName(DeviceTagNameType.OS);
		osTag.setValue(app);
		return osTag;		
	}
	
	private static DeviceType.Tag getTerminalIdTag(String terminalId) {
		DeviceType.Tag terminalIdTag = new DeviceType.Tag();
		terminalIdTag.setName(DeviceTagNameType.TERMINAL_ID);
		terminalIdTag.setValue(terminalId);
		return terminalIdTag;
	}

	private static DeviceType.Tag getMobileTag(String mobile) {
		DeviceType.Tag mobileTag = new DeviceType.Tag();
		mobileTag.setName(DeviceTagNameType.MOBILE);
		mobileTag.setValue(mobile);
		return mobileTag;
	}

	private static DeviceType.Tag getGeoCodeTag(String geoCode) {
		DeviceType.Tag geoCodeTag = new DeviceType.Tag();
		geoCodeTag.setName(DeviceTagNameType.GEOCODE);
		geoCodeTag.setValue(geoCode);
		return geoCodeTag;
	}

	private static DeviceType.Tag getPostalCodeTag(String postalCode) {
		DeviceType.Tag postalCodeTag = new DeviceType.Tag();
		postalCodeTag.setName(DeviceTagNameType.POSTAL_CODE);
		postalCodeTag.setValue(postalCode);
		return postalCodeTag;
	}

	private static DeviceType.Tag getIFSCTag(String ifsc) {
		DeviceType.Tag postalCodeTag = new DeviceType.Tag();
		postalCodeTag.setName(DeviceTagNameType.IFSC);
		postalCodeTag.setValue(ifsc);
		return postalCodeTag;
	}
}