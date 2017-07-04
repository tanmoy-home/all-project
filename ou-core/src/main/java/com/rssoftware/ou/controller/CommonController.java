package com.rssoftware.ou.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.bbps.schema.Biller;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.OtpStatus;
import com.rssoftware.ou.database.entity.tenant.ApplicationConfig;
import com.rssoftware.ou.database.entity.tenant.OtpDetails;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.CityView;
import com.rssoftware.ou.model.tenant.PostalCodeView;
import com.rssoftware.ou.model.tenant.SMSDetailsView;
import com.rssoftware.ou.model.tenant.SMSDetailsView.SMSType;
import com.rssoftware.ou.model.tenant.StateView;
import com.rssoftware.ou.tenant.dao.NotificationDao;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.LocationService;
import com.rssoftware.ou.tenant.service.OtpService;
import com.rssoftware.ou.tenant.service.PaymentChannelPaymentModeService;
import com.rssoftware.ou.tenant.service.SmsService;

import in.co.rssoftware.bbps.schema.ApplicationConfigDetail;
import in.co.rssoftware.bbps.schema.ApplicationConfigList;
import in.co.rssoftware.bbps.schema.CityDetail;
import in.co.rssoftware.bbps.schema.CityList;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.OtpResponse;
import in.co.rssoftware.bbps.schema.OtpType;
import in.co.rssoftware.bbps.schema.ParamValue;
import in.co.rssoftware.bbps.schema.PaymentModeFilterReq;
import in.co.rssoftware.bbps.schema.PaymentModeFilterResponse;
import in.co.rssoftware.bbps.schema.PostalCodeList;
import in.co.rssoftware.bbps.schema.PostalDetail;
import in.co.rssoftware.bbps.schema.SmsType;
import in.co.rssoftware.bbps.schema.StateDetail;
import in.co.rssoftware.bbps.schema.StateList;
import in.co.rssoftware.bbps.schema.StateName;

@RestController
@RequestMapping(value = "/APIService")
public class CommonController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private ApplicationConfigService applicationConfigService;

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private SmsService smsService;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private PaymentChannelPaymentModeService paymentChannelPaymentModeService;
	
	@Autowired
	private BillerService billerService;

	@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/fetch-city-detail", method = RequestMethod.POST)
	public CityList getCities(@PathVariable String tenantId, @RequestBody StateList stateList) {
		TransactionContext.putTenantId(tenantId);
		List<CityView> cityViewList = null;
		CityList cityList = new CityList();
		long stateid = 0;
		Long stateId = stateList.getStateId();
		try {
			if (stateId == null || stateId == 0) {
				cityViewList = locationService.getCityList();
			} else if (stateId != null || stateId != 0) {
				stateid = Long.valueOf(stateId).longValue();
				cityViewList = locationService.getCityByStateList(stateid);
			}
			if (cityViewList.size() > 0) {
				for (CityView view : cityViewList) {
					cityList.getCities().add(mapToCityDetail(view));
				}
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorDtl("No Data found For this State code");
				cityList.getErrors().add(errorMessage);
			}

		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(e.getMessage());
			errorMessage.setErrorDtl("Please enter valid state code");
			cityList.getErrors().add(errorMessage);
		}

		return cityList;
	}

	@SuppressWarnings("null")
	@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/fetch-state-detail", method = RequestMethod.POST)
	public StateList getStateDetail(@PathVariable String tenantId, @RequestBody StateList state_List) {
		TransactionContext.putTenantId(tenantId);
		List<StateView> stateViewList = new ArrayList<>();
		StateList stateList = new StateList();
		long stateid = 0;
		Long stateId = state_List.getStateId();
		String stateName = null;
		try {
			if (stateId == null || stateId == 0) {
				stateViewList = locationService.getAllState();
				if (stateViewList.size() > 0) {
					for (StateView view : stateViewList) {
						stateList.getStateLists().add(mapToStateDetail(view));
					}
				} else if (stateViewList.size() <= 0) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorDtl("No Data found For this code");
					stateList.getErrors().add(errorMessage);
				}

			} else if (stateId != null || stateId != 0) {
				stateid = Long.valueOf(stateId).longValue();
				try {
					stateName = locationService.retrieveState(stateid);

					StateName sName = mapToStateName(stateName);
					stateList.setStateName(sName);
				} catch (Exception e) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorDtl("No Data found For this State code");
					stateList.getErrors().add(errorMessage);
				}
			}
		} catch (Exception e) {
			Log.error(e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(e.getMessage());
			errorMessage.setErrorDtl("Please enter valid state code");
			stateList.getErrors().add(errorMessage);
		}
		return stateList;
	}

	@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/fetch-postal-detail", method = RequestMethod.POST)
	public PostalCodeList fetchPostalCode(@PathVariable String tenantId, @RequestBody CityList cityList) {

		PostalCodeList postalCodeList = new PostalCodeList();
		TransactionContext.putTenantId(tenantId);
		List<PostalCodeView> postalCodeViewlist = new ArrayList<>();
		Long stateId = cityList.getStateId();
		Long cityId = cityList.getCityId();
		try {
			if (stateId == null && cityId == null) {
				postalCodeViewlist = locationService.fetchAll();
			} else if (cityId != null && cityId != 0) {
				long cityIdLong = cityId;
				postalCodeViewlist = locationService.retirivePinByCityList(cityIdLong);
			} else if (stateId != null && stateId != 0) {
				long stateIdLong = stateId;
				postalCodeViewlist = locationService.retirivePinByStateList(stateIdLong);
			}
			if (postalCodeViewlist.size() > 0) {
				for (PostalCodeView view : postalCodeViewlist) {
					postalCodeList.getPostals().add(mapToPostalDetail(view));
				}
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorDtl("No data found");
				postalCodeList.getErrors().add(errorMessage);
			}
		} catch (Exception e) {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(e.getMessage());
			errorMessage.setErrorDtl("Enter the Correct Code");
			postalCodeList.getErrors().add(errorMessage);
		}

		return postalCodeList;
	}

	@SuppressWarnings({ "null", "unused" })
	@RequestMapping(value = "/urn:tenantId:{tenantId}/applicationConfigGroupName", method = RequestMethod.POST)
	public @ResponseBody ApplicationConfigList getValuesByGroup(HttpServletRequest request,
			@PathVariable String tenantId, @RequestBody String appConfigList) {
		TransactionContext.putTenantId(tenantId);
		List<ApplicationConfig> appView = new ArrayList<>();
		ApplicationConfigList applicationConfigList = new ApplicationConfigList();
		try {
			if (appConfigList != null || !appConfigList.isEmpty()) {
				appView = applicationConfigService.getValuesByGroup(appConfigList);
			}
			if (appView.size() > 0) {
				for (ApplicationConfig view : appView) {
					applicationConfigList.getApplicationConfigs().add(mapToApplicationConfigDetail(view));
				}
			} else if (appView.size() <= 0) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorDtl("No Data Found : ");
				applicationConfigList.getErrors().add(errorMessage);
			}
		} catch (Exception e) {
			Log.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("Please enter correct input");
			errorMessage.setErrorDtl(e.getMessage());
			applicationConfigList.getErrors().add(errorMessage);
		}
		return applicationConfigList;
	}

	@RequestMapping(value = "/urn:tenantId:{tenantId}/notificationtemplate", method = RequestMethod.GET)
	public @ResponseBody String getNotificationTemplate(@PathVariable String tenantId, @RequestParam String msgType,
			@RequestParam(value = "eventType", required = false) String eventType) {
		TransactionContext.putTenantId(tenantId);
		String txnSuccessTemplate = null;

		if (eventType == null || eventType.isEmpty()) {
			txnSuccessTemplate = smsService.getTxnSuccessTemplateByMsgType(msgType);
		} else {
			txnSuccessTemplate = smsService.getNotificationTemplateByMsgTypeAndEventType(msgType, eventType);
		}

		return txnSuccessTemplate;
	}

	@RequestMapping(value = "/urn:tenantId:{tenantId}/sms", method = RequestMethod.POST)
	public String saveSms(@PathVariable String tenantId, @RequestBody SmsType sms) {
		TransactionContext.putTenantId(tenantId);
		SMSDetailsView smsView = mapTo(sms);
		return smsService.saveSms(smsView);
	}
	
	@RequestMapping(value = "/urn:tenantId:{tenantId}/sendsms", method = RequestMethod.POST)
	public String sendAndSaveSms(@PathVariable String tenantId, @RequestBody SmsType sms) {
		TransactionContext.putTenantId(tenantId);
		SMSDetailsView smsView = mapTo(sms);
		return smsService.createSms(smsView);
	}
	
	@RequestMapping(value = "/urn:tenantId:{tenantId}/generateOtp", method = RequestMethod.POST)
	public OtpResponse generateOtp(@PathVariable String tenantId, @RequestBody OtpType otp) {
		TransactionContext.putTenantId(tenantId);
		OtpDetails  otpDetails= new OtpDetails();
		otpDetails.setMobileNo(otp.getMobileNumber());
		otpDetails= otpService.generateOtp(otpDetails);
		OtpResponse otpResponse= new OtpResponse();
		otpResponse.setStatus(false);
		if (otpDetails != null) 
		{
			otpResponse.setStatus(true);	
			otpResponse.setOtpNumber(otpDetails.getOtpNo());
		}
		return otpResponse;
	}
	@RequestMapping(value = "/urn:tenantId:{tenantId}/validateOtp", method = RequestMethod.POST)
	public OtpResponse validateOtp(@PathVariable String tenantId, @RequestBody OtpType otp) {
		TransactionContext.putTenantId(tenantId);
		
		OtpDetails  otpDetails= new OtpDetails();
		otpDetails.setMobileNo(otp.getMobileNumber());
		OtpResponse otpResponse= new OtpResponse();
		otpResponse.setStatus(false);
		
		OtpStatus otpStatus = otpService.validateOtp(otpDetails,otp.getOtpNumber());
		if(OtpStatus.VALID_OTP.equals(otpStatus)){
			otpResponse.setStatus(true);	
		}
		return otpResponse;
	}
	
	@RequestMapping(value = "/urn:tenantId:{tenantId}/lookupPaymentMode", method = RequestMethod.POST)
	public PaymentModeFilterResponse loopupPaymentMode(@PathVariable String tenantId, @RequestBody PaymentModeFilterReq paymentModeFilterReq) {
		TransactionContext.putTenantId(tenantId);
		BillerView biller = null;
		if (paymentModeFilterReq.getBillerId() != null && !paymentModeFilterReq.getBillerId().isEmpty()) {
			biller = billerService.getBillerById(paymentModeFilterReq.getBillerId());
		}		
		Set<String> paymentModes = paymentChannelPaymentModeService.fetchPaymentModes(paymentModeFilterReq.getDefaultPaymentChannel(), paymentModeFilterReq.getAgentPaymentChannel(), biller);
		PaymentModeFilterResponse paymentModeFilterResponse = new PaymentModeFilterResponse();
		if (paymentModes != null && !paymentModes.isEmpty()) {
			for (String paymentMode : paymentModes) {
				paymentModeFilterResponse.getPaymentModes().add(paymentMode);
			}
		}
		return paymentModeFilterResponse;
	}

	private SMSDetailsView mapTo(SmsType sms) {
		SMSDetailsView smsDetails = new SMSDetailsView();
		smsDetails.setSmsMessage(sms.getMessage());
		smsDetails.setMobileNo(sms.getMobileNo());
		smsDetails.setType(SMSType.valueOf(sms.getType()));
		if (sms.getStatus() != null && sms.getStatus().toCharArray().length > 0) {
			char[] status = sms.getStatus().toCharArray();
			smsDetails.setStatus(status[0]);
		}
		return smsDetails;
	}

	private ApplicationConfigDetail mapToApplicationConfigDetail(ApplicationConfig view) {
		ApplicationConfigDetail applicationConfigDetail = new ApplicationConfigDetail();

		applicationConfigDetail.setConfigParam(view.getConfigParam());
		applicationConfigDetail.setConfigParamDesc(view.getConfigParamDesc());
		applicationConfigDetail.setConfigParamGroup(view.getConfigParamGroup());
		if (view.getConfigParamValue() != null || !view.getConfigParamValue().isEmpty()) {
			ParamValue paramValue = null;
			String[] element = view.getConfigParamValue().split("~");
			for (int i = 0; i < element.length; i++) {
				paramValue = new ParamValue();
				paramValue.setParamElement(element[i]);
				applicationConfigDetail.getConfigParamValues().add(paramValue);
			}
		}
		return applicationConfigDetail;
	}

	private PostalDetail mapToPostalDetail(PostalCodeView view) {
		PostalDetail postalDetail = new PostalDetail();

		postalDetail.setPostalCode(view.getPinCode());
		postalDetail.setCityId(view.getCityId());
		postalDetail.setPostalLocation(view.getPinLocation());
		postalDetail.setStateId(view.getStateId());

		return postalDetail;
	}

	private CityDetail mapToCityDetail(CityView view) {
		CityDetail cityDetail = new CityDetail();
		cityDetail.setCityId(view.getCityId());
		cityDetail.setCityName(view.getCityName());
		cityDetail.setStateId(view.getStateId());

		return cityDetail;
	}

	private StateDetail mapToStateDetail(StateView view) {
		StateDetail stateDetail = new StateDetail();
		stateDetail.setStateId(view.getStateId());
		stateDetail.setStateName(view.getStateName());

		return stateDetail;
	}

	private StateName mapToStateName(String name) {
		StateName stateName = new StateName();
		stateName.setStateName(name);
		return stateName;

	}
}
