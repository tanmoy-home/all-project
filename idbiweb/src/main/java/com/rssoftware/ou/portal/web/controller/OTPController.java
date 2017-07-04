package com.rssoftware.ou.portal.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.NotificationEventType;
import com.rssoftware.ou.common.NotificationType;
import com.rssoftware.ou.common.OtpStatus;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.utils.WebAPIURL;
import com.rssoftware.ou.database.entity.tenant.OtpDetails;
import com.rssoftware.ou.model.tenant.SmsMessageView.SMSType;
import com.rssoftware.ou.portal.web.utils.Utility;
import com.rssoftware.ou.tenant.service.OtpService;

import in.co.rssoftware.bbps.schema.OtpResponse;
import in.co.rssoftware.bbps.schema.OtpType;
import in.co.rssoftware.bbps.schema.SmsType;

@Controller
@RequestMapping("/OTPService")
public class OTPController {
	
	@Value("${ou.tenantId}")
	private String tenantId;

	@Value("${ou.domain}")
	private String uri;
	
	@Autowired
	private OtpService otpService;
	
	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();

	@RequestMapping(value = "/generateOTP" , method = RequestMethod.POST)
	public @ResponseBody boolean generateOtp(Model model, HttpServletRequest request, @RequestBody String mobileNoWithType) {
		TransactionContext.putTenantId(tenantId);
		JSONObject jsonObject;
		OtpDetails otpDetails = null;
		
		try {
			jsonObject = new JSONObject(mobileNoWithType);
			String type = jsonObject.getString("comp_type");
			String mobileNo = jsonObject.getString("mobileNo");

//			otpDetails= new OtpDetails();
//			otpDetails.setMobileNo(mobileNo);
//			otpDetails= otpService.generateOtp(otpDetails);
			OtpType otpType= new OtpType();
			otpType.setMobileNumber(mobileNo);
			String notificationTemplate = null;
			if(type.equalsIgnoreCase(CommonConstants.TRAN_OTP))
				{
				notificationTemplate=getNotificationTemplate(NotificationEventType.TRAN_COMP_OTP.name(),NotificationType.SMS.name());
				}
			else if(type.equalsIgnoreCase(CommonConstants.SVC_OTP))
			{
				notificationTemplate=getNotificationTemplate(NotificationEventType.SVC_COMP_OTP.name(),NotificationType.SMS.name());
			}
			OtpResponse otpResponse=generateOtp(otpType);
			String notificationMsg = notificationTemplate
			.replace(CommonConstants.TEMPLATE_OTP, otpResponse.getOtpNumber());
			saveSms(mobileNo,notificationMsg, SMSType.COMPLAINT_OTP.name());
			
				return otpResponse.isStatus() ;	
			
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@RequestMapping(value = "/validateOTP" , method = RequestMethod.POST)
	public @ResponseBody boolean validateOtp(Model model, HttpServletRequest request, @RequestBody String inputData) {
		TransactionContext.putTenantId(tenantId);
		boolean validOtp = false;
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(inputData);
			String otpNumber = jsonObject.getString("otpNumber");
			String mobileNo = jsonObject.getString("mobileNo");
			OtpType otpType= new OtpType();
			otpType.setMobileNumber(mobileNo);
			otpType.setOtpNumber(otpNumber);
			return ValidateOtp(otpType);
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
//		validOtp = true;
		return validOtp;
	}
	
	
	private String getNotificationTemplate(String eventType,String notificationType)
	{
		String apiUrl = uri + WebAPIURL.GET_NOTIFICATION_TEMPLATE_URL;

		ResponseEntity<?> responseEntity = internalRestTemplate.exchange(apiUrl, HttpMethod.GET,
				Utility.getHttpEntityForGet(), String.class, tenantId, notificationType,eventType);

		String notificationTemplate = (String) responseEntity.getBody();
		return notificationTemplate;
	}
	private void saveSms(String mob,String notificationMsg,String type) {
		
		SmsType smsType = new SmsType();
		smsType.setMessage(notificationMsg);
		smsType.setMobileNo(mob);
		smsType.setType(type);
		smsType.setStatus("U");

		String apiUrl = uri + WebAPIURL.SAVE_SMS_URL;
		ResponseEntity<?> responseEntity  = internalRestTemplate.postForEntity(apiUrl, Utility.getHttpEntityForPost(smsType), String.class,
				tenantId);
		String status = (String)responseEntity.getBody();
		System.out.println("SMS " + status);

	}
	private OtpResponse generateOtp(OtpType otpType)
	{
		String apiUrl = uri + WebAPIURL.GENERATE_OTP;

		ResponseEntity<?> responseEntity = internalRestTemplate.exchange(apiUrl, HttpMethod.POST,
				Utility.getHttpEntityForPost(otpType), OtpResponse.class, tenantId);

		OtpResponse otpResponse = (OtpResponse) responseEntity.getBody();
		return otpResponse;
	}
	private boolean ValidateOtp(OtpType otpType)
	{
		String apiUrl = uri + WebAPIURL.VALIDATE_OTP;

		ResponseEntity<?> responseEntity = internalRestTemplate.exchange(apiUrl, HttpMethod.POST,
				Utility.getHttpEntityForPost(otpType), OtpResponse.class, tenantId);

		OtpResponse otpResponse = (OtpResponse) responseEntity.getBody();
		return otpResponse.isStatus();
	}
}
