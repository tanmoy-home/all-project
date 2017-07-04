package com.rssoftware.ou.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillSummaryRequest;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.HeadType;
import org.bbps.schema.ReasonType;
import org.bouncycastle.crypto.generators.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.CommonAPIRequest;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.utils.BillSummary;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.ContactDetailsView;
import com.rssoftware.ou.model.tenant.ContactDetailsView.LinkedEntityType;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.model.tenant.UserView;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.ChannelParametersService;
import com.rssoftware.ou.tenant.service.CommonAgentService;
import com.rssoftware.ou.tenant.service.CommonService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.TenantParamService;
import com.rssoftware.ou.tenant.service.TransactionDataService;
import com.rssoftware.ou.tenant.service.UserService;
import com.rssoftware.ou.tenant.service.impl.AgentServiceImpl;
import com.rssoftware.ou.validator.UserValidator;

import in.co.rssoftware.bbps.schema.AccountBalance;
import in.co.rssoftware.bbps.schema.AgentDetail;
import in.co.rssoftware.bbps.schema.AgentRegistrationResponse;
import in.co.rssoftware.bbps.schema.AgentType;
import in.co.rssoftware.bbps.schema.DeviceTagNameType;
import in.co.rssoftware.bbps.schema.DeviceType;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.ErrorResponse;
import in.co.rssoftware.bbps.schema.FetchAgentTypeRequest;
import in.co.rssoftware.bbps.schema.FetchResponse;
import in.co.rssoftware.bbps.schema.PasswordResetRequest;
import in.co.rssoftware.bbps.schema.PaymentInformation;
import in.co.rssoftware.bbps.schema.PaymentReceipt;
import in.co.rssoftware.bbps.schema.RegisterLoginRequest;
import in.co.rssoftware.bbps.schema.RegisterLoginResponse;
import in.co.rssoftware.bbps.schema.Response;
import in.co.rssoftware.bbps.schema.TxnSearchRequest;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

@RestController

@RequestMapping(value = "/APIService/agents/urn:tenantId:{tenantId}")

public class AgentAPIController {

	@Autowired
	private AgentService			agentService;

	@Autowired
	private UserService				userService;

	@Autowired
	private CommonAgentService		commonAgentService;

	@Autowired
	private TransactionDataService	transactionDataService;

	@Autowired
	private CommonService			commonService;

	@Autowired
	private IDGeneratorService		iDGeneratorService;

	@Autowired
	private TenantParamService		tenantParamService;

	@Autowired
	private TenantDetailService		tenantDetailService;
	
	@Autowired
	private ChannelParametersService			channelParametersService;

	private final Logger			log	= LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> login(@PathVariable String tenantId, @RequestBody RegisterLoginRequest loginRequest) {
		RegisterLoginResponse response = new RegisterLoginResponse();

		try {
			TransactionContext.putTenantId(tenantId);
			List<String> requestParam = Arrays.asList(new String(Base64.decodeBase64(loginRequest.getLoginHash())).split(":"));

			if (requestParam.size() != 2)
				throw new Exception(ValidationErrorReason.INVALID_REQUEST_PARAM.getDescription());

			String userName = requestParam.get(0);
			String userPass = requestParam.get(1);

			log.info("Login param: " + loginRequest.getLoginHash());
			log.info("Login Id: " + userName);
			log.info("Login password: " + userPass);

			if (StringUtils.isBlank(userName))
				throw new Exception(ValidationErrorReason.USER_ID_NOT_DEFINED.getDescription());
			else if (StringUtils.isBlank(userPass))
				throw new Exception(ValidationErrorReason.USER_PASS_NOT_DEFINED.getDescription());

			UserView userView = userService.getUserByUserName(userName);
			//new BCryptPasswordEncoder().encode(userPass)
			//new BCryptPasswordEncoder().matches(userPass,userView.getPassword()); 
			//!org.springframework.security.crypto.bcrypt.BCrypt.checkpw(userPass, userView.getPassword());
			//new BCryptPasswordEncoder().matches(userPass, "$2a$10$.a7VtrWqPo614/fJV42mvu0UDAHch9dXGlRVzd52wAD.i9d0tlr7u");
			AgentView agentView=agentService.getAgentById(userView.getUserRefId());
			List<PaymentChannelLimit> paymentChannel=agentView.getAgentPaymentChannels();
			List<String> channelName=new ArrayList<String>();
			for(PaymentChannelLimit p:paymentChannel){
				PaymentChannel channel=p.getPaymentChannel();
				channelName.add(channel.getExpandedForm());
				
			}
			
			if (userView == null || userView.getUserRefId() == null)
				throw new Exception(ValidationErrorReason.AGENT_ID_NOT_FOUND.getDescription());	
			else if(!org.springframework.security.crypto.bcrypt.BCrypt.checkpw(userPass, userView.getPassword()))
				throw new Exception(ValidationErrorReason.INVALID_PASSWORD.getDescription());
			/*else if (!aa.matches("password", userView.getPassword()))//(!userPass.equals(userView.getPassword()))
				throw new Exception(ValidationErrorReason.INVALID_PASSWORD.getDescription());*/

			/****** Response *****/
			response.setAgentId(userView.getUserRefId());
			response.setPasswordReset(!userView.getResetPasswordFlag());
			response.setAgentFisrtName(userView.getFirstName());
			response.setAgentLastName(userView.getLastName());
			response.setAgentEmailid(userView.getEmail());
			response.setAgentPhoneNumber(String.valueOf(userView.getMobile()));
			response.setAgentGeoCode(agentView.getAgentGeoCode());
			response.setAgentRegisteredAdrline(agentView.getAgentRegisteredAdrline());
			response.setAgentRegisteredCity(agentView.getAgentRegisteredCity());
			response.setAgentRegisteredCountry(agentView.getAgentRegisteredCountry());
			response.setAgentRegisteredState(agentView.getAgentRegisteredState());
			response.setAgentRegisteredPinCode(agentView.getAgentRegisteredPinCode());
			response.setAgentShopName(agentView.getAgentShopName());
			response.setLatitude(agentView.getLatitude());
			response.setLongitude(agentView.getLongitude());
			response.setBillerPaymentchannel(channelName.toString());
			return new ResponseEntity<RegisterLoginResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Login error: ",e);

			ErrorResponse errResponse = new ErrorResponse();
			ErrorMessage errorMessage = new ErrorMessage();

			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			errResponse.getErrors().add(errorMessage);

			return new ResponseEntity<ErrorResponse>(errResponse, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@RequestMapping(value = "/resetpassword", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> passwordReset(@PathVariable String tenantId,
			@RequestBody PasswordResetRequest passwordResetRequest) {
		try {
			TransactionContext.putTenantId(tenantId);
			UserView userView = userService.getUserByUserName(passwordResetRequest.getUserName());

			if (UserValidator.validatePassword(passwordResetRequest.getNewPass(), passwordResetRequest.getConfirmPass())) {
				if (UserValidator.validatePassword(passwordResetRequest.getOldPass(), userView.getPassword())) {
					userView.setPassword(passwordResetRequest.getNewPass());
					userView.setResetPasswordFlag(true);
					userService.save(userView);
					return new ResponseEntity<String>("Password reset successful", HttpStatus.OK);
				} else
					throw new Exception(ValidationErrorReason.OLD_PASS_NOT_MATCHED.getDescription());
			} else
				throw new Exception(ValidationErrorReason.CONFIRM_PASS_NOT_MATCHED.getDescription());
		} catch (Exception e) {
			log.error("Login error: " + e.getMessage());
			ErrorResponse errResponse = new ErrorResponse();
			ErrorMessage errorMessage = new ErrorMessage();

			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			errResponse.getErrors().add(errorMessage);

			return new ResponseEntity<ErrorResponse>(errResponse, HttpStatus.EXPECTATION_FAILED);
		}
	}

	/**
	 * to be used by agents to view their account balance via cbs
	 * 
	 * @param tenantId
	 * @return
	 */

	@RequestMapping(value = "/fetchBalance", method = RequestMethod.POST)
	public ResponseEntity<?> fetchBalance(@PathVariable String tenantId, @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "fetchBalance";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		AccountBalance accountBalance = new AccountBalance();

		try {
			accountBalance.setAmount(new BigDecimal(commonAgentService.checkCurrentBalance(commonAPIRequest.getAgentId())));
			response = new ResponseEntity(accountBalance, HttpStatus.OK);
		} catch (ValidationException ve) {
			log.error(ve.getMessage(), ve);
			log.info("In Excp : " + ve.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd(ve.getCode());
			errorMessage.setErrorDtl(ve.getDescription());
			accountBalance.getErrors().add(errorMessage);
			accountBalance.setAmount(null);
			response = new ResponseEntity(accountBalance, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/agentRegistration", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> agentRegistration(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestBody AgentDetail agentRegistrationRequest) throws ValidationException, IOException {

		TransactionContext.putTenantId(tenantId);
		AgentRegistrationResponse agentRegistrationResponse = new AgentRegistrationResponse();
		ResponseEntity<?> agentresponse = null;
		// validation
		if (isValidAgent(agentRegistrationResponse, mapAgent(agentRegistrationRequest, tenantId))) {
			// send OTP

			// Data save to DB
			try {
				agentresponse = new ResponseEntity(
						AgentServiceImpl.mapAgentToJaxb(agentService.save(mapAgent(agentRegistrationRequest, tenantId))), HttpStatus.OK);
			} catch (ValidationException e) {
				log.error(e.getMessage(), e);
				log.info("In Excp : " + e.getMessage());
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd(e.getCode());
				errorMessage.setErrorDtl(e.getDescription());
				agentresponse = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}

		}
		else{
			log.info("validation fail");
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorDtl("validation fail");
			agentresponse = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return agentresponse;
	}

	public AgentView mapAgent(AgentDetail agentRegistrationRequest, String tenantId) {
		AgentView agent = new AgentView();
		if (StringUtils.isBlank(agentRegistrationRequest.getAgentID())) {
			String agentID = iDGeneratorService.getUniqueID(20, tenantId);
			agent.setAgentId(agentID);
		} else {
			agent.setAgentId(agentRegistrationRequest.getAgentID());
		}
		agent.setAgentAliasName(agentRegistrationRequest.getAgentAliasName());
		agent.setAgentInst(agentRegistrationRequest.getAgentInst());
		agent.setAgentEffctvFrom(agentRegistrationRequest.getAgentEffectiveFrom());
		agent.setAgentEffctvTo(agentRegistrationRequest.getAgentEffectiveTo());
		agent.setAgentGeoCode(agentRegistrationRequest.getAgentGEOCode());
		agent.setAgentMobileNo(agentRegistrationRequest.getAgentMobile());
		agent.setAgentName(agentRegistrationRequest.getAgentName());
		/*
		 * List<String> list = new ArrayList<String>(
		 * agentRegistrationRequest.getAgentPaymentModes());
		 */
		List<String> list = null;
		if (agentRegistrationRequest.getAgentPaymentModes() != null) {
			/*
			 * if(agentRegistrationRequest.getAgentPaymentModes().get(0).
			 * contains(",")){ list = new ArrayList<String>(
			 * Arrays.asList(agentRegistrationRequest.getAgentPaymentModes().get
			 * (0).split(","))); } else{ list =
			 * agentRegistrationRequest.getAgentPaymentModes(); }
			 */

			List<PaymentModeLimit> paymentModeLimits = new ArrayList<PaymentModeLimit>();
			for (in.co.rssoftware.bbps.schema.PaymentModeLimit mode : agentRegistrationRequest.getAgentPaymentModes()) {
				if (mode != null && mode.getPaymentMode() != null) {
					PaymentModeLimit paymentMode = new PaymentModeLimit();
					paymentMode.setPaymentMode(PaymentMode.getExpPaymentMode(mode.getPaymentMode()));
					paymentMode.setMaxLimit(null);
					paymentModeLimits.add(paymentMode);
				}
			}
			/*
			 * for (String str : list) { String str1 = null;
			 * if(str.contains(":")){ str1 =
			 * str.split(":")[1].toString().substring(1,
			 * str.split(":")[1].toString().length()-2); } else{ str1 = str; }
			 * PaymentModeLimit paymentModeLimit = new PaymentModeLimit();
			 * paymentModeLimit.setPaymentMode(PaymentMode.getExpPaymentMode(
			 * str1)); paymentModeLimit.setMaxLimit(null);
			 * paymentModeLimits.add(paymentModeLimit); }
			 */
			agent.setAgentPaymentModes(paymentModeLimits);
		}

		agent.setAgentRegisteredAdrline(agentRegistrationRequest.getAgentAddr());
		agent.setAgentRegisteredCity(agentRegistrationRequest.getAgentCity());
		agent.setAgentRegisteredCountry(agentRegistrationRequest.getAgentCountry());
		agent.setAgentRegisteredPinCode(agentRegistrationRequest.getAgentPin());
		agent.setAgentRegisteredState(agentRegistrationRequest.getAgentState());
		agent.setAgentShopName(agentRegistrationRequest.getAgentShopName());
		agent.setBusinessType(agentRegistrationRequest.getAgentBusinessType());
		agent.setAgentBankAccount(agentRegistrationRequest.getAgentBankAccount());
		agent.setAgentSchemeId(agentRegistrationRequest.getAgentSchemeId());
		agent.setAgentGeoCode(agentRegistrationRequest.getAgentGEOCode());
		agent.setIsUpload(agentRegistrationRequest.isIsUpload() == null ? false : agentRegistrationRequest.isIsUpload());
		if (agentRegistrationRequest.getAgentEntityStatus() != null) {
			agent.setEntityStatus(EntityStatus.valueOf(agentRegistrationRequest.getAgentEntityStatus()));

		} else {
			agent.setEntityStatus(EntityStatus.DRAFT);

		}
		boolean dummy = agentRegistrationRequest.isDummyAgent() == null ? false : agentRegistrationRequest.isDummyAgent();
		agent.setDummyAgent(dummy);
		
		if (dummy) {
			if (agentRegistrationRequest.getAgentPaymentChannels() != null) {
				List<PaymentChannelLimit> paymentChannelLimits = new ArrayList<PaymentChannelLimit>();
				for (in.co.rssoftware.bbps.schema.PaymentChannelLimit chnl : agentRegistrationRequest.getAgentPaymentChannels()) {
					if (chnl != null && chnl.getPaymentChannel() != null) {
						PaymentChannelLimit paymentChannel = new PaymentChannelLimit();
						paymentChannel.setPaymentChannel(PaymentChannel.getExpPaymentChannel(chnl.getPaymentChannel()));
						paymentChannel.setMaxLimit(null);
						paymentChannelLimits.add(paymentChannel);
					}
				}
				agent.setAgentPaymentChannels(paymentChannelLimits);
				agent.setAgentType("");
			}
		}
		else{
			agent.setAgentPaymentChannels(null);
			agent.setAgentType(agentRegistrationRequest.getAgentType());			
		}
		ContactDetailsView cont1 = new ContactDetailsView();
		cont1.setMobileNo(agentRegistrationRequest.getAgentMobile());
		cont1.setLinkedEntityID(agentRegistrationRequest.getAgentID());
		cont1.setLinkedEntityType(LinkedEntityType.AGENT);
		agent.setContactDetailsView1stLevel(cont1);
		return agent;
	}

	private boolean isValidAgent(Response response, AgentView agent) {
		boolean validagent;
		if (agent == null) {
			validagent = false;
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.AGENT_NOT_FOUND.name());
			error.setErrorDtl(ValidationException.ValidationErrorReason.AGENT_NOT_FOUND.getDescription());
			response.getErrors().add(error);
		} else if (agent.getAgentMobileNo() == null || agent.getAgentInst() == null || agent.getAgentName() == null
				|| agent.getAgentAliasName() == null || agent.getAgentPaymentModes() == null || agent.getBusinessType() == null
				|| agent.getAgentRegisteredState() == null || agent.getAgentRegisteredPinCode() == null
				|| agent.getAgentBankAccount() == null || agent.getAgentSchemeId() == null) {
			validagent = false;
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.BLANK_FIELD.name());
			error.setErrorDtl(ValidationException.ValidationErrorReason.BLANK_FIELD.getDescription());
			response.getErrors().add(error);
		} else if (!(agent.getAgentMobileNo().startsWith("9") || agent.getAgentMobileNo().startsWith("8")
				|| agent.getAgentMobileNo().startsWith("7"))) {
			validagent = false;
			ErrorMessage error = new ErrorMessage();
			error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_MOBILE.name());
			error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_MOBILE.getDescription());
			response.getErrors().add(error);
		} else if (agent.getAgentGeoCode() != null) {
			if (!(agent.getAgentGeoCode().split("\\.")[1].length() == 4)) {
				validagent = false;
				ErrorMessage error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_GEOCODE.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_GEOCODE.getDescription());
				response.getErrors().add(error);
			} else {
				validagent = true;
			}
		}else if (agent.isDummyAgent()) {
			if (agent.getAgentPaymentChannels() == null || agent.getAgentPaymentChannels().isEmpty()) {
				validagent = false;
				ErrorMessage error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.BLANK_FIELD.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.BLANK_FIELD.getDescription());
				response.getErrors().add(error);
			} else {
				validagent = true;
			}
		}else if (!agent.isDummyAgent()) {
			if (agent.getAgentType() == null || agent.getAgentType().isEmpty()) {
				validagent = false;
				ErrorMessage error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.BLANK_FIELD.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.BLANK_FIELD.getDescription());
				response.getErrors().add(error);
			} else {
				validagent = true;
			}
		}else {
			validagent = true;
		}

		return validagent;
	}

	private String getRemoteIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	@RequestMapping(value = "/agentTxnHistory", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> fetchTxnHistory(HttpServletRequest request, @PathVariable String tenantId,
			@RequestBody TxnSearchRequest txnSearchRequest) {
		String METHOD_NAME = "fetchTxnHistory";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			List<TransactionDataView> txnView = transactionDataService.getFilteredTransactionView(txnSearchRequest.getAgentId());
			if (txnView != null) {
				response = new ResponseEntity<TxnSearchResponse>(transactionDataService.getTxnJaxb(txnView), HttpStatus.OK);
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorDtl("Transaction List could not be found!");
				response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/agentTxnSearch", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> fetchTxnHistoryByDate(HttpServletRequest request, @PathVariable String tenantId,
			@RequestBody TxnSearchRequest txnSearchRequest) {
		String METHOD_NAME = "fetchTxnHistoryByDate";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		String startDt = txnSearchRequest.getTxnStartDate();
		String endDt = txnSearchRequest.getTxnEndDate();
		try {
			if (startDt == null && endDt == null) {
				startDt = new Date(System.currentTimeMillis()).toString();
				endDt = new Date(System.currentTimeMillis()).toString();
			}
			if (startDt != null && endDt != null) {
				List<TransactionDataView> txnView = transactionDataService.getFilteredTransactionByDate(txnSearchRequest.getAgentId(), startDt, endDt);
				if (txnView != null) {
					response = new ResponseEntity<TxnSearchResponse>(transactionDataService.getTxnJaxb(txnView), HttpStatus.OK);
				} else {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorDtl("Transaction List could not be found!");
					response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/agentId:{agentId}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getAgentDetailsById( @PathVariable String tenantId,@PathVariable String agentId) {

		String METHOD_NAME = "getAgentDetails";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			AgentView agentView = agentService.getAgentById(agentId);
			if (agentView != null) {
				response = new ResponseEntity(AgentServiceImpl.mapAgentToJaxb(agentView), HttpStatus.OK);
			} 
			else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd("AGENT_NOT_FOUND");
				errorMessage.setErrorDtl("Agent could not be found!");
				response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/agentId", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> getAgentDetails(HttpServletRequest request, @PathVariable String tenantId,
			final @RequestBody CommonAPIRequest commonAPIRequest) {

		String METHOD_NAME = "getAgentDetails";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			AgentView agentView = agentService.getAgentById(commonAPIRequest.getAgentId());
			if (agentView != null) {
				response = new ResponseEntity(AgentServiceImpl.mapAgentToJaxb(agentView), HttpStatus.OK);
			} 
			else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd("AGENT_NOT_FOUND");
				errorMessage.setErrorDtl("Agent could not be found!");
				response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/defaultAgent", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> getDefaultAgentDetails(HttpServletRequest request, @PathVariable String tenantId,
			@RequestBody PaymentInformation info) {

		String METHOD_NAME = "getDefaultAgentDetails";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			AgentView agentView = agentService.getDefaultAgent(info.getPaymentChannel());
			if (agentView != null) {
				response = new ResponseEntity(AgentServiceImpl.mapAgentToJaxb(agentView), HttpStatus.OK);
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd("DEFAULT_AGENT_NOT_FOUND");
				errorMessage.setErrorDtl("Default Agent could not be found!");
				response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/agentBillCount", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> fetchBillCount(HttpServletRequest request, @PathVariable String tenantId,
			final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "fetchBillCount";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {

			TransactionContext.putTenantId(tenantId);
			BillSummary billSummary = new BillSummary();

			billSummary.setBillCount(transactionDataService.txnCountByType("FETCH", commonAPIRequest.getAgentId()));
			response = new ResponseEntity<BillSummary>(billSummary, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/agentPaidBillCount", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> fetchPaidBillCount(HttpServletRequest request, @PathVariable String tenantId,
			final @RequestBody BillSummaryRequest summaryRequest) {
		String METHOD_NAME = "fetchPaidBillCount";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			TransactionContext.putTenantId(tenantId);
			BillSummary billSummary = new BillSummary();

			billSummary.setPaidBillCount(transactionDataService.txnCountByType("PAYMENT", summaryRequest.getAgentId()));
			response = new ResponseEntity<BillSummary>(billSummary, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/agentTodayCollectAmt", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> fetchTodayCollectAmt(HttpServletRequest request, @PathVariable String tenantId,
			final @RequestBody BillSummaryRequest summaryRequest) {
		String METHOD_NAME = "fetchTodayCollectAmt";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}

		ResponseEntity<?> response = null;
		try {
			TransactionContext.putTenantId(tenantId);
			BillSummary billSummary = new BillSummary();

			billSummary.setTodayCollectAmmount(transactionDataService.totalTxnAmount(RequestType.PAYMENT.toString(), summaryRequest.getAgentId(), null, null));
			response = new ResponseEntity<BillSummary>(billSummary, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/PaymentTransactionDetail", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> getPaymentTransactionDataByTranRefId(HttpServletRequest request, @PathVariable String tenantId,
			final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "getPaymentTransactionDataByTranRefId";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;

		try {
			TransactionDataView transactionDataView = transactionDataService.getFilteredTransactionByID(commonAPIRequest.getTxnId());
			BillPaymentResponse billPaymentResponse = null;
			PaymentReceipt paymentReceipt = null;
			if (transactionDataView != null) {
				if (RequestStatus.RESPONSE_SUCCESS.equals(transactionDataView.getStatus())) {
					billPaymentResponse = transactionDataView.getBillPaymentResponse();
					if (billPaymentResponse != null) {
						paymentReceipt = createPaymentReceipt(billPaymentResponse);
						response = new ResponseEntity<PaymentReceipt>(paymentReceipt, HttpStatus.OK);
					} else {
						ErrorMessage errorMessage = new ErrorMessage();
						errorMessage.setErrorCd("RECEIPT_NOT_FOUND");
						errorMessage.setErrorDtl("Receipt could not be found!");
						response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
					}
				} else {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorCd("TRANSACTION_NOT_FOUND");
					errorMessage.setErrorDtl("Transaction could not be found!");
					response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
				}
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd("TRANSACTION_NOT_FOUND");
				errorMessage.setErrorDtl("Transaction could not be found!");
				response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}

		return response;
	}

	private PaymentReceipt createPaymentReceipt(BillPaymentResponse billPaymentResponse) {
		PaymentReceipt paymentReceipt = new PaymentReceipt();
		paymentReceipt.setCustomerName(billPaymentResponse.getBillerResponse().getCustomerName());
		try {
			paymentReceipt.setBillDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
			paymentReceipt.setTransactionDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		} catch (DatatypeConfigurationException e) {
			// logger.error("Problem in setting BillDate: " + e.getCause());
		}
		paymentReceipt.setTransactionRefId(billPaymentResponse.getTxn().getTxnReferenceId());
		// paymentReceipt.setTransactionDateTime(new GregorianCalendar());//
		// need
		paymentReceipt.setBillNumber(billPaymentResponse.getBillerResponse().getBillNumber());
		paymentReceipt.setBillPeriod(billPaymentResponse.getBillerResponse().getBillPeriod());
		paymentReceipt.setAmount(billPaymentResponse.getBillerResponse().getAmount());
		return paymentReceipt;
	}

	@RequestMapping(value = "/agentTxnByMobile", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> fetchTxnByMobile(HttpServletRequest request, @PathVariable String tenantId,
			final @RequestBody CommonAPIRequest commonAPIRequest) {
		String METHOD_NAME = "fetchTxnByMobile";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			List<TransactionDataView> txnView = transactionDataService.fetchAllTxnByMobile(commonAPIRequest.getMobNo());
			if (txnView != null) {
				response = new ResponseEntity(transactionDataService.getTxnJaxb(txnView), HttpStatus.OK);
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorDtl("Transaction List could not be found!");
				response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("In Excp : " + e.getMessage());
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}

	@RequestMapping(value = "/agentOtherBillFetch", method = RequestMethod.POST)
	public Callable<FetchResponse> fetchTxnByID(HttpServletRequest request, @PathVariable String tenantId,
			@RequestBody FetchAgentTypeRequest fetchAgentTypeRequest) throws ValidationException, IOException {
		String METHOD_NAME = "fetchTxnByID";
		if (log.isDebugEnabled()) {
			log.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);

		TransactionDataView txnView = transactionDataService.getFilteredTransactionByID(fetchAgentTypeRequest.getTxnRefId());
		BillFetchRequestExt billFetchRequestExt = new BillFetchRequestExt();
		BillFetchRequest billFetchRequest = null;

		if (txnView != null) {
			billFetchRequest = txnView.getBillFetchRequest();

			AgentType agentType = fetchAgentTypeRequest.getAgent();
			org.bbps.schema.AgentType agentType2 = new org.bbps.schema.AgentType();

			if (agentType.getId() != null) {
				agentType2.setId(agentType.getId());
			} else {
				String agentId = tenantParamService.retrieveStringParamByName("DEFAULT_AGENT");
				agentType2.setId(agentId);
			}

			DeviceType deviceType = agentType.getDevice();
			org.bbps.schema.DeviceType deviceType2 = new org.bbps.schema.DeviceType();

			List<in.co.rssoftware.bbps.schema.DeviceType.Tag> tags = deviceType.getTags();

			for (int i = 0; i < tags.size(); i++) {
				in.co.rssoftware.bbps.schema.DeviceType.Tag tag = tags.get(i);
				DeviceTagNameType deviceTagNameType = tag.getName();
				String val = deviceTagNameType.value();
				org.bbps.schema.DeviceType.Tag tmpTag = new org.bbps.schema.DeviceType.Tag();
				tmpTag.setName(org.bbps.schema.DeviceTagNameType.fromValue(val));
				tmpTag.setValue(tag.getValue());
				deviceType2.getTags().add(tmpTag);
			}
			agentType2.setDevice(deviceType2);
			billFetchRequest.setAgent(agentType2);

			HeadType head = new HeadType();
			head.setVer("1.0");
			head.setTs(CommonUtils.getFormattedCurrentTimestamp());
			head.setOrigInst(tenantDetailService.getOUName(TransactionContext.getTenantId()));
			head.setRefId(iDGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID,
					tenantDetailService.getOUName(TransactionContext.getTenantId())));
			billFetchRequest.setHead(head);

			billFetchRequest.getTxn().setMsgId(iDGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID,
					tenantDetailService.getOUName(TransactionContext.getTenantId())));

			billFetchRequestExt.setAgentChannelID(txnView.getAgentChannelID());

			billFetchRequestExt.setAgentChannelCustomerID(txnView.getAgentChannelCustID());

			billFetchRequestExt.setAgentChannelTransactionID(txnView.getAgentChannelTxnID());
		}

		billFetchRequestExt.setCurrentNodeAddress(CommonUtils.getServerNameWithPort());
		billFetchRequestExt.setBillFetchRequest(billFetchRequest);

		return new Callable<FetchResponse>() {
			@Override
			public FetchResponse call() {
				BillFetchResponse billFetchResponse = null;
				FetchResponse fetchResponse = null;
				try {
					TransactionContext.putTenantId(tenantId);
					billFetchResponse = commonAgentService.fetchBill(billFetchRequestExt);
					if (billFetchResponse != null) {
						fetchResponse = createFetchResponse(billFetchResponse);
					}
				} catch (ValidationException e) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorCd(e.getCode());
					errorMessage.setErrorDtl(e.getDescription());
					fetchResponse = new FetchResponse();
					fetchResponse.getErrorMessages().add(errorMessage);
				}

				catch (IOException e) {
					log.error(e.getMessage(), e);
					log.info("In Excp : " + e.getMessage());
				}

				return fetchResponse;
			}
		};
	}

	private FetchResponse createFetchResponse(BillFetchResponse billFetchResponse) {

		FetchResponse fetchResponse = new FetchResponse();

		fetchResponse.setRefId(billFetchResponse.getHead().getRefId());
		// fetchResponse.set
		ReasonType reasonType = billFetchResponse.getReason();
		in.co.rssoftware.bbps.schema.ReasonType reasonType2 = new in.co.rssoftware.bbps.schema.ReasonType();
		reasonType2.setApprovalRefNum(reasonType.getApprovalRefNum());
		reasonType2.setComplianceReason(reasonType.getComplianceReason());
		reasonType2.setComplianceRespCd(reasonType.getComplianceRespCd());
		reasonType2.setResponseCode(reasonType.getResponseCode());
		reasonType2.setResponseReason(reasonType.getResponseReason());
		reasonType2.setValue(reasonType.getValue());
		fetchResponse.setReason(reasonType2);

		BillerResponseType billerResponseType = billFetchResponse.getBillerResponse();
		in.co.rssoftware.bbps.schema.BillerResponseType billerResponseType2 = new in.co.rssoftware.bbps.schema.BillerResponseType();
		billerResponseType2.setAmount(billerResponseType.getAmount());
		billerResponseType2.setBillDate(billerResponseType.getBillDate());
		billerResponseType2.setBillNumber(billerResponseType.getBillNumber());
		billerResponseType2.setBillPeriod(billerResponseType.getBillPeriod());
		billerResponseType2.setCustConvDesc(billerResponseType.getCustConvDesc());
		billerResponseType2.setCustConvFee(billerResponseType.getCustConvFee());
		billerResponseType2.setCustomerName(billerResponseType.getCustomerName());
		billerResponseType2.setDueDate(billerResponseType.getDueDate());

		List<BillerResponseType.Tag> listTags = billerResponseType.getTags();
		for (int i = 0; i < listTags.size(); i++) {
			BillerResponseType.Tag temptag = listTags.get(i);
			in.co.rssoftware.bbps.schema.BillerResponseType.Tag tag2 = new in.co.rssoftware.bbps.schema.BillerResponseType.Tag();
			tag2.setName(temptag.getName());
			tag2.setValue(temptag.getValue());
			billerResponseType2.getTags().add(tag2);
		}

		fetchResponse.setBillerResponse(billerResponseType2);
		AdditionalInfoType additionalInfoType = billFetchResponse.getAdditionalInfo();
		in.co.rssoftware.bbps.schema.AdditionalInfoType additionalInfoType2 = new in.co.rssoftware.bbps.schema.AdditionalInfoType();
		List<AdditionalInfoType.Tag> listTags1 = additionalInfoType.getTags();
		for (int i = 0; i < listTags1.size(); i++) {
			AdditionalInfoType.Tag temptag1 = listTags1.get(i);
			in.co.rssoftware.bbps.schema.AdditionalInfoType.Tag tag3 = new in.co.rssoftware.bbps.schema.AdditionalInfoType.Tag();
			tag3.setName(temptag1.getName());
			tag3.setValue(temptag1.getValue());
			additionalInfoType2.getTags().add(tag3);
		}
		fetchResponse.setAdditionalInfo(additionalInfoType2);

		return fetchResponse;
	}

}