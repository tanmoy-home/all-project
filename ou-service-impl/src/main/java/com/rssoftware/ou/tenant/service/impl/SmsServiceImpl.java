package com.rssoftware.ou.tenant.service.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.bbps.schema.Ack;
import org.bbps.schema.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.NotificationEventType;
import com.rssoftware.ou.common.NotificationType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.rest.SMSClientHttpRequestFactory;
import com.rssoftware.ou.common.rest.SMSSender;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.database.entity.tenant.DBSms;
import com.rssoftware.ou.database.entity.tenant.NotificationTemplate;
import com.rssoftware.ou.database.entity.tenant.SMSConfig;
import com.rssoftware.ou.database.entity.tenant.SMSDetails;
import com.rssoftware.ou.database.entity.tenant.SmsLogDetials;
import com.rssoftware.ou.model.tenant.SMSDetailsView;
import com.rssoftware.ou.model.tenant.SmsConfigView;
import com.rssoftware.ou.model.tenant.SmsMessageView;
import com.rssoftware.ou.tenant.dao.DBSmsDao;
import com.rssoftware.ou.tenant.dao.NotificationDao;
import com.rssoftware.ou.tenant.dao.SMSConfigDao;
import com.rssoftware.ou.tenant.dao.SMSDetailsDBDao;
import com.rssoftware.ou.tenant.dao.SmsLogDetialsDao;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.SmsService;

@Service
public class SmsServiceImpl implements SmsService {

	private static Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

	private static String TXN_ID = "##TransactionReferanceNo##";
	private static String TXN_AMOUNT = "##Amount##";
	private static String DATE = "##TransactionDate##";
	private static String BENEFICIARY_NAME = "##BeneficiaryName##";
	private static String TRANSACTION_STATE = "##TransactionState##";
	private static String BILLER_NAME = "##BillerName##";
	private static String CONSUMER_NO = "##ConsumerNo##";
	private static String PAYMENT_CHANNEL = "##PaymentChannel##";
	// private static String CONSUMER_NAME = "##CONSUMER_NAME##";
	// private static String VERIFICATION_CODE = "##VERIFICATION_CODE##";
	// private static String BANK_NAME = "##BANK_NAME##";
	// private static String BRANCH_NAME = "##BRANCH_NAME##";
	// private static String ACCOUNT_NUMBER = "##ACCOUNT_NUMBER##";
	// private static String IFSC_CODE = "##IFSC_CODE##";

	private static final String SMS_NOTIFICATION_TYPE_IS_DB = "DB";

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private IDGeneratorService idGeneratorService;

	@Autowired
	private SMSDetailsDBDao smsDetailsDBDao;

	@Autowired
	private ApplicationConfigService applicationConfigService;

	@Autowired
	private SMSConfigDao smsconfigDao;

	@Autowired
	private DBSmsDao dbSmsDao;

	@Autowired
	private SmsLogDetialsDao smsLogDetailsDao;

	public SmsConfigView getSmsConfig(String tenantId, String sendType) throws ValidationException {
		SMSConfig smsConfig = null;
		smsConfig = smsconfigDao.getSmsConfigByType(tenantId, sendType);
		return (mapToView(smsConfig));
	}

	@Override
	public String sendSms(String mobNumber, String message, String tenantId, String sendType,
			SmsMessageView.SMSType type, String baseUrl) throws ValidationException {
		if (message.length() > 160) {
			message = message.substring(1, 160);
		}

		SmsConfigView configView = getSmsConfig(tenantId, sendType);
		// validateParam(messageView,configView);
		String status = CommonConstants.SMS_NOT_SENT;
		if (configView != null) {
			if (sendType.equalsIgnoreCase(CommonConstants.SMS_BY_URL)) {

			} else if (sendType.equalsIgnoreCase(CommonConstants.SMS_TO_DB)) {
				DBSms dbSms = new DBSms();
				dbSms.setConfigId(configView.getConfigId());
				dbSms.setMobileNum(mobNumber);
				dbSms.setSmsId(idGeneratorService.getUniqueID(12, "111"));
				dbSms.setCreationDt(new Timestamp(System.currentTimeMillis()));
				dbSms.setMessage(getSmsMsg(mobNumber, message, configView));
				dbSms.setSmsType(type.name());
				dbSmsDao.create(dbSms);
			}

			if (sendType.equalsIgnoreCase(CommonConstants.SMS_BY_URL)
					|| sendType.equalsIgnoreCase(CommonConstants.SMS_TO_DB)) {
				SmsLogDetials logDetials = new SmsLogDetials();
				logDetials.setConfigId(configView.getConfigId());
				logDetials.setCreationDt(new Timestamp(System.currentTimeMillis()));
				logDetials.setMobileNum(mobNumber);
				logDetials.setSmsUrl(baseUrl);
				logDetials.setStatus(status);
				logDetials.setData(getSmsMsg(mobNumber, message, configView));
				logDetials.setSmsType(type.name());
				logDetials.setLogId(idGeneratorService.getUniqueID(12, "111"));
				smsLogDetailsDao.create(logDetials);
			}
		}
		return null;
	}

	private String getqueryString(String mob, String message, SmsConfigView configView) {
		String queryString = "";
		// boolean isFirst = true;
		// for (Map.Entry<String, String> param : params.entrySet()) {
		// if (isFirst) {
		// queryString = "?" + param.getKey() + "=" + param.getValue();
		// } else {
		// queryString = "&" + param.getKey() + "=" + param.getValue();
		// }
		// }

		for (int i = 0; i < configView.getParamlist().size(); i++) {
			switch (i) {
			case 0:
				queryString += "?" + configView.getParamlist().get(i) + "=" + mob;
				break;
			case 1:
				queryString += "&" + configView.getParamlist().get(i) + "=" + message;

				break;
			default:
				String constFields = configView.getParamlist().get(i);
				String[] values = constFields.split("~");
				queryString += "&" + values[0] + "=" + (values.length > 1 ? ("'" + values[1] + "'") : "");
				break;
			}
		}
		return queryString;

	}

	private String getSmsMsg(String mob, String messageg, SmsConfigView configView) {
		String msg = "";
		String msg2 = "";
		boolean isFirst = true;
		for (int i = 0; i < configView.getParamlist().size(); i++) {
			switch (i) {
			case 0:
				msg += "(" + configView.getParamlist().get(i);
				msg2 += "('" + mob + "'";
				break;
			case 1:
				msg += "," + configView.getParamlist().get(i);
				msg2 += ",'" + messageg + "'";
				break;
			default:
				String constFields = configView.getParamlist().get(i);
				String[] values = constFields.split("~");
				msg += "," + values[0];
				msg2 += "," + (values.length > 1 ? ("'" + values[1] + "'") : "NULL");
				break;
			}
		}

		// for(Map.Entry<String,String> param:params.entrySet())
		// {
		// if(isFirst)
		// {
		// msg+="('"+param.getKey()+"'";
		// msg2+="('"+param.getValue()+"'";
		// //queryString="?"+param.getKey()+"="+param.getValue();
		// isFirst=false;
		// }
		// else
		// {
		//
		// msg+=",'"+param.getKey()+"'";
		// msg2+=",'"+param.getValue()+"'";
		// }
		// }
		msg += ")";
		msg2 += ")";
		return "insert into " + configView.getTableName() + " " + msg + " values " + msg2;

	}

	private void validateParam(SmsMessageView messageView, SmsConfigView configView) throws ValidationException {

		boolean flag = false;
		if (messageView != null && messageView.getMessage() != null && configView.getParamlist() != null) {
			String[] ar1 = CommonUtils.getSmsParamsAsStrings(messageView);
			String[] ar2 = configView.getParamlist().toArray(new String[configView.getParamlist().size()]);
			Arrays.sort(ar1);
			Arrays.sort(ar2);
			if (Arrays.equals(ar1, ar2)) {
				flag = true;
			}
		}
		if (!flag) {
			Ack ack = new Ack();
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_PARAMS.name());
			error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_PARAMS.getDescription());
			ack.getErrorMessages().add(error);
			throw ValidationException.getInstance(ack);
		}
	}

	private SmsConfigView mapToView(SMSConfig smsConfig) throws ValidationException {
		SmsConfigView configView = null;
		if (smsConfig != null) {
			configView = new SmsConfigView();
			configView.setConfigId(smsConfig.getConfigId() + "");
			configView.setTenantId(smsConfig.getTenantId());
			configView.setSendType(smsConfig.getSendType());
			configView.setBaseUrl(smsConfig.getBaseUrl());
			configView.setTableName(smsConfig.getTableName());
			if (smsConfig.getParma1() != null && !smsConfig.getParma1().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma1());
			} else {
				Ack ack = new Ack();
				ErrorMessage error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_Param.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_Param.getDescription());
				ack.getErrorMessages().add(error);
				/* throw ValidationException.getInstance(ack); */
				LogUtils.logReqRespMessage(ack, null, Action.SMS_PARAM_ERROR);
			}
			if (smsConfig.getParma2() != null && !smsConfig.getParma2().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma2());
			}
			if (smsConfig.getParma3() != null && !smsConfig.getParma3().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma3());
			}
			if (smsConfig.getParma4() != null && !smsConfig.getParma4().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma4());
			}
			if (smsConfig.getParma5() != null && !smsConfig.getParma5().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma5());
			}
			if (smsConfig.getParma6() != null && !smsConfig.getParma6().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma6());
			}
			if (smsConfig.getParma7() != null && !smsConfig.getParma7().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma7());
			}
			if (smsConfig.getParma8() != null && !smsConfig.getParma8().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma8());
			}
			if (smsConfig.getParma9() != null && !smsConfig.getParma9().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma9());
			}
			if (smsConfig.getParma10() != null && !smsConfig.getParma10().isEmpty()) {
				configView.getParamlist().add(smsConfig.getParma10());
			}
		} else {
			Ack ack = new Ack();
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_SMS_CONFIG.name());
			error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_SMS_CONFIG.getDescription());
			ack.getErrorMessages().add(error);
			LogUtils.logReqRespMessage(ack, null, Action.SMS_CONFIG_NOT_FOUND_ERROR);
			logger.info(error.getErrorCd() + "----INFO-------- " + error.getErrorDtl());
		}
		return configView;

	}

	@Override
	public String createSms(SMSDetailsView smsDetailsView) {
		logger.info("Inside method createSms.....................");
		String response = "DB Insert ";
		try {
		smsDetailsView.setStatus('D');
		saveSms(smsDetailsView);
		response += "SUCCESS";
		logger.info("***************Save SMS response:" + response);
		
		if (null != System.getProperty("sms.cert")) {
			// send sms then save in db
			response = "Sending SMS to URL  ";
			sendsms(smsDetailsView.getSmsMessage(), smsDetailsView.getMobileNo());
			response += "SUCCESS";
			logger.info("***************Save SMS response:" + response);
		} else {

			logger.info(" ***************** SMS ERROR  : sms.cert not present");
		}
		} catch (Exception e) {
			e.printStackTrace();
			response += "FAILURE";
		}
		
		return response;
		
	}

	@Override
	public String saveSms(SMSDetailsView smsDetailsView) {
		logger.info("Inside method saveSms................");
		String response = "DB Insert ";
		try {
			smsDetailsDBDao.create(mapFrom(smsDetailsView));
			response += "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			response += "FAILURE";
		}
		logger.info("Save SMS response:" + response);
		return response;
	}

	private SMSDetails mapFrom(SMSDetailsView smsDetailsView) {
		SMSDetails details = new SMSDetails();

		// details.setRecordId(smsDetailsView.getRecordId());
		details.setRecordId(Long.valueOf(idGeneratorService.getUniqueID(12, "111")));
		details.setCreatedBy(smsDetailsView.getCreatedBy());
		details.setMobileNo(smsDetailsView.getMobileNo());
		details.setSmsMessage(smsDetailsView.getSmsMessage());
		details.setType(smsDetailsView.getType().name());
		details.setStatus(smsDetailsView.getStatus());
		/*
		 * details.setStatus(smsDetailsView.getStatus());
		 * details.setCreationDate(smsDetailsView.getCreationDate());
		 * details.setDeliveryDate(smsDetailsView.getDeliveryDate());
		 */

		return details;
	}
	// private SMSDetailsView mapTo(SMSDetails details) {
	// if (null == details) {
	// return null;
	// }
	// SMSDetailsView smsDetailsView = new SMSDetailsView();
	//
	// smsDetailsView.setRecordId(details.getRecordId());
	// smsDetailsView.setCreatedBy(details.getCreatedBy());
	// smsDetailsView.setMobileNo(details.getMobileNo());
	// smsDetailsView.setSmsMessage(details.getSmsMessage());
	// if(details.getType()!=null)
	// smsDetailsView.setType(SMSType.valueOf(details.getType()));
	// smsDetailsView.setStatus(details.getStatus());

	@Override
	public String getTxnSuccessTemplateByMsgType(String msgType) {
		NotificationTemplate txnSuccessTemplate = notificationDao.getTxnSuccessTemplateByMsgType(msgType);
		return txnSuccessTemplate.getMessage();
	}

	@Override
	public String getNotificationTemplateByMsgTypeAndEventType(String msgType, String eventType) {
		NotificationTemplate txnSuccessTemplate = notificationDao.getNotificationTemplateByMsgTypeAndEventType(msgType, eventType);
		return txnSuccessTemplate.getMessage();
	}
	
	
	private void sendsms(String message,String mobileNo){
		try{
		//https://www.google.co.in/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=suman
			
		/*System.setProperty("sms.cert", "/home/rsdpp/certificate/google.cer");
		System.setProperty("sms.url", "https://www.google.co.in/webhp");
		System.setProperty("sms.params", "dcode=INBD&subuid=sumandom&suman=suman&sender=RSIINB&pno=919830582701&msgtxt=TestMsg&msgtype=S&intflag=1");
		System.setProperty("sms.sendMethod", "POST");*/
			
		String url = System.getProperty("sms.url");
		String urlparams = System.getProperty("sms.params");
		String sendMethod = System.getProperty("sms.sendMethod");
		logger.info(" ***************** SMS TESTING url : "+url);
		logger.info(" ***************** SMS TESTING urlparams : "+urlparams);
		logger.info(" ***************** SMS TESTING sendMethod : "+sendMethod);

		MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
		if(null != urlparams){
			for (String retvalL1: urlparams.split("&")) {
				if(null != retvalL1){
					if(null != retvalL1.split("=")[0] && "msgtxt".equals(retvalL1.split("=")[0])){
						bodyMap.add("msgtxt", message);
					} else if(null != retvalL1.split("=")[0] && "pno".equals(retvalL1.split("=")[0])){
						bodyMap.add("pno", mobileNo);
					} else{	
						bodyMap.add(retvalL1.split("=")[0], retvalL1.split("=")[1]);
					}
				}
			}
		}
		SMSClientHttpRequestFactory factory = new SMSClientHttpRequestFactory();
		factory.setReadTimeout(100000);
		factory.setConnectTimeout(1000);
		factory.setBufferRequestBody(false);
		
		RestTemplate restTemplate = new RestTemplate(factory);
		String resp = null;
		if(null != sendMethod && sendMethod.equalsIgnoreCase("GET")){
			logger.info(" ***************** SMS TESTING body to be sent Using GET : "+bodyMap);
			resp = restTemplate.getForObject(url, String.class, bodyMap );
		} else if(null != sendMethod && sendMethod.equalsIgnoreCase("POST")){
			logger.info(" ***************** SMS TESTING body to be sent Using POST : "+bodyMap);
			resp = restTemplate.postForObject(url, bodyMap,String.class );
		}
		logger.info(" ***************** SMS TESTING Response from SMS Gateway "+resp);
		
		}catch(Throwable ex){
			logger.info(" ***************** SMS TESTING Exception during sending sms "+ex);
			ex.printStackTrace();
		}
		
	}
}