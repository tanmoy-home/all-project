package com.rssoftware.ou.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bbps.schema.Ack;
import org.bbps.schema.ErrorMessage;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.model.tenant.UserView;

public class UserValidator {
	
	//static Pattern pinCodePattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
	//static Pattern pinCodePattern = Pattern.compile("^([1-9])([0-9]){5}$");
	//static Pattern pinCodePattern = Pattern.compile("^([0-9]){6}$");
	static Pattern pinCodePattern = Pattern.compile("^([1-9])([0-9]{5})$");
	public static Boolean validatePassword(String pw1,String pw2){
		if(pw1.equals(pw2)){
			return true;
		}
		else
		return false;
	}
	
	public static UserView validateRequest(UserView userView) throws ValidationException{
		List<org.bbps.schema.ErrorMessage> errorMessages = new ArrayList<org.bbps.schema.ErrorMessage>();
		org.bbps.schema.ErrorMessage error = null;
		if (userView == null) {
			error = new ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Request object cannot be null!");
			errorMessages.add(error);
		    }
		if (userView != null) {
			//validate  mobile no.
			if(userView.getMobile()!=null){
			if(userView.getMobile().toString().length()!=10){
				error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_MOBILE.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_MOBILE.getDescription());
				errorMessages.add(error);
			}
			if (!(userView.getMobile().toString().startsWith("9") || userView.getMobile().toString().startsWith("8")
					|| userView.getMobile().toString().startsWith("7"))) {
				error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_MOBILE.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_MOBILE.getDescription());
				errorMessages.add(error);
		        }
			}
			//validate  pin code
			if(userView.getPincode()!=null){
			Matcher matcher = pinCodePattern.matcher(userView.getPincode().toString());
			if(!matcher.matches()){
				error = new ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_PINCODE.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_PINCODE.getDescription());
				errorMessages.add(error);
			}
			}
	    }
		if (errorMessages.size()>0) {
			Ack ack = new Ack();
			ack.getErrorMessages().addAll(errorMessages);
			throw ValidationException.getInstance(ack);
		}
	return userView;
}
}