package com.rssoftware.ou.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import org.bbps.schema.PasswordResetRequest;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.model.tenant.UserView;
import com.rssoftware.ou.tenant.service.UserService;
import com.rssoftware.ou.validator.UserValidator;

import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.ProfileUpdate;

@RestController
@RequestMapping(value = "/APIService/user")
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	UserService userService;

	@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/userdetail/{userName}", method = RequestMethod.GET)
	public UserView postBillerFormElements(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @PathVariable String userName) {
		TransactionContext.putTenantId(tenantId);
		return userService.getUserByUserName(userName);
	}

	/*@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/resetpassword", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public String passwordReset(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestBody PasswordResetRequest passwordResetRequest) {
		TransactionContext.putTenantId(tenantId);
		String msg=null;
		UserView userView=userService.getUserByUserName(passwordResetRequest.getUserName());
		if(UserValidator.validatePassword(passwordResetRequest.getNewPass(), passwordResetRequest.getConfirmPass())){
			if(UserValidator.validatePassword(passwordResetRequest.getOldPass(),userView.getPassword())){
				userView.setPassword(passwordResetRequest.getNewPass());
				userService.saveNewPassword(userView);
				msg="Password reset successful";
			}
			else{
				msg="Failed. Old password does not match..";
			}
		}
		else{
			msg="Confirm password does not match";
		}
		return msg;
    }*/
	
	@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/updateprofile", method = RequestMethod.POST)
	public ProfileUpdate profileUpdate(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestBody ProfileUpdate profileUpdateRequest) {
		TransactionContext.putTenantId(tenantId);
		//ResponseEntity<?> res = null;
		UserView userView=userService.getUserByUserName(profileUpdateRequest.getUserName());
		userView.setFirstName(profileUpdateRequest.getFirstName());
		userView.setLastName(profileUpdateRequest.getLastName());
		userView.setAddress(profileUpdateRequest.getAddress());
		userView.setState(profileUpdateRequest.getState());
		userView.setCountry(profileUpdateRequest.getCountry());
		userView.setPincode(profileUpdateRequest.getPincode() == null ? null : Long.parseLong(profileUpdateRequest.getPincode()));
		userView.setContactNumber(profileUpdateRequest.getPhone());
		userView.setMobile(profileUpdateRequest.getMobile() == null ? null : Long.parseLong(profileUpdateRequest.getMobile()));
		userView.setEmail(profileUpdateRequest.getEmail());
		userView.setPan(profileUpdateRequest.getPan());
		userView.setAadhar(profileUpdateRequest.getAadhar());
		try{
			userView=userService.updateProfile(userView);
			profileUpdateRequest=getJaxbFromView(userView);
		}
		catch(ValidationException ve)
		{
			//ve.printStackTrace();
			logger.error("Error: ", ve);
			profileUpdateRequest=new ProfileUpdate();
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					profileUpdateRequest.getErrors().add(mapTo(error));
				}
			}
		}
		return profileUpdateRequest;
		
		
    }

	private ErrorMessage mapTo(org.bbps.schema.ErrorMessage error) {
		ErrorMessage msg = new ErrorMessage();
		msg.setErrorCd(error.getErrorCd());
		msg.setErrorDtl(error.getErrorDtl());
		return msg;
	}

	private ProfileUpdate getJaxbFromView(UserView userView) {
		ProfileUpdate profileUpdate=new ProfileUpdate();
		profileUpdate.setUserName(userView.getUsername());
		profileUpdate.setFirstName(userView.getFirstName());
		profileUpdate.setLastName(userView.getLastName());
		profileUpdate.setAddress(userView.getAddress());
		profileUpdate.setState(userView.getState());
		profileUpdate.setPincode(userView.getPincode().toString());
		profileUpdate.setCountry(userView.getCountry());
		profileUpdate.setPhone(userView.getContactNumber());
		profileUpdate.setMobile(userView.getMobile().toString());
		profileUpdate.setPan(userView.getPan());
		profileUpdate.setAadhar(userView.getAadhar());
		return profileUpdate;
	}
	private UserView getViewFromJaxb(ProfileUpdate profileUpdateRequest) {
		// TODO Auto-generated method stub
		return null;
	}
}

