package com.rssoftware.ou.validator;

import in.co.rssoftware.bbps.schema.InterchangeFeeSubmitRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bbps.schema.Ack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.tenant.service.BillerService;



@Component
public class Interchangefeevalidator {
	
	@Autowired
	private  BillerService billerService;
	
	public InterchangeFeeSubmitRequest validateInterchangeFee(InterchangeFeeSubmitRequest req)throws ValidationException, IOException{
		List<org.bbps.schema.ErrorMessage> errorMessages = new ArrayList<org.bbps.schema.ErrorMessage>();
		org.bbps.schema.ErrorMessage error = null;
		if (req == null) {
			error = new org.bbps.schema.ErrorMessage();
			error.setErrorCd(ValidationErrorReason.NULL.name());
			error.setErrorDtl("Request object cannot be null!");
			errorMessages.add(error);
		}
		else{
			//check for valid biller 
			if(billerService.getBillerById(req.getBillerId())==null){
				error = new org.bbps.schema.ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_BILLER.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_BILLER.getDescription());
				errorMessages.add(error);
			}
			//check for fee direction
			if(!(req.getFeeDirection().equals(InterchangeFeeDirectionType.B2C.name())||
					(req.getFeeDirection().equals(InterchangeFeeDirectionType.C2B.name())))){
				error = new org.bbps.schema.ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_DIRECTION.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_DIRECTION.getDescription());
				errorMessages.add(error);
			}
			// check if effectiveto date is less than effectivefrom date
			int efctvfrm=Integer.parseInt(req.getEffctvFrom().toString().replaceAll("-", ""));
			int efctvto=Integer.parseInt(req.getEffctvTo().toString().replaceAll("-", ""));
			if(efctvto<efctvfrm){
				error = new org.bbps.schema.ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_DATE.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_DATE.getDescription());
				errorMessages.add(error);	
			}
			// check if tranAmountRangeMax is less than tranAmountRangeMin
			int minRange=Integer.parseInt(req.getTranAmountRangeMin().replaceAll("-", ""));
			int maxRange=Integer.parseInt(req.getTranAmountRangeMax().replaceAll("-", ""));
			if(maxRange<minRange){
				error = new org.bbps.schema.ErrorMessage();
				error.setErrorCd(ValidationException.ValidationErrorReason.INVALID_AMOUNT_RANGE.name());
				error.setErrorDtl(ValidationException.ValidationErrorReason.INVALID_AMOUNT_RANGE.getDescription());
				errorMessages.add(error);	
			}
		}
		if (!errorMessages.isEmpty()) {
			Ack ack = new Ack();
			//ack.getErrorMessages().add(mapTo(error));
			ack.getErrorMessages().addAll(errorMessages);
			throw ValidationException.getInstance(ack);
		}
		
		return req;
		
	}

}
