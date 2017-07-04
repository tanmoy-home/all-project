package com.rssoftware.ou.common.exception;

import org.bbps.schema.Ack;

public class ValidationException extends Exception {
	public enum ValidationErrorReason {
		REQUEST_NOT_FOUND("Request not found"),
		ILLEGAL_OPERATION("This operation is not permitted at this time"),
		SIGNATURE_MISMATCH("Digital Signture not matching"),
		INVALID_XML("Invalid XML"),
		NETWORK_ERR("CU could not be reached due to network error"),
		TIMEOUT("Transaction timed out"),
		REVERSAL("Transaction has been reversed"),
		DECLINE("Transaction has been declined"),
		ISE("Internal Server Error"),
		PG_RESPONSE_NOT_VALID("Response not valid"),
		NULL("Value is null"),
		OU_NOT_AVAILABLE("Ou is not available"),
		INVALID_STATE("Invalid State"),
		AGENT_NOT_FOUND("No agent found for the given agent ID"),
		PAYMENT_CHANNEL_NOT_SUPPORTED("Agent provided does not support any payment channel"),
		PAYMENT_MODE_NOT_IN_AGENT_PROFILE("Some provided payment modes are not in agent profile"),
		PAYMENT_CHANNEL_NOT_IN_AGENT_PROFILE("Some provided payment channels are not in agent profile"),
		BLANK_FIELD("Field not cannot be null."),
		INVALID_MOBILE("only numbers starts with 7 or 8 or 9, Length must be 10"),
		INVALID_PINCODE("Length must be 6,Can not start with 0"),
		RECORD_EXIST("Record already exists"),
		LENGTH_OF_ID_MISMATCH("Length of id mismatch"),
		INVALID_COMPLAINT_TYPE("Complaint Type must be Transaction or Service"),
		INVALID_PARTICIPATION_TYPE("Participation Type must be AGENT or BILLER or SYSTEM"),
		IVALID_AGENT_STATUS("Agent is in Invalid State"),
		INSUFFICIENT_BALANCE("Sorry, Your account is having insufficient balance!"),
		DUPLICATE_REQUEST("Request is duplicate"),
		UNEXPECTED_ERROR("An Unexpected Error occured. Please try again"),
		INVALID_BILLER_MODE("Invalid Biller Mode."),
		INVALID_JSON("Invalid JSON String."),
		INVALID_BILLER("Biller does not exists."),
		INVALID_DIRECTION("Enter B2C or C2B."),
		INVALID_DATE("Effective From Date must be less than Effective to date"),
		INVALID_AMOUNT_RANGE("Min Amount Range must be less than Max Amount Range"),
		INVALID_BILL_DETAILS("Bill Details in BillerResponse doesn't match"),
		INVALID_SMS_CONFIG("No SMSConfig found for given tenenatId and sendType"),
		INVALID_GEOCODE("only 4 digit allow after decimal"),
		INVALID_PARAMS("Some params are mismatched or not found"),
		INVALID_Param("Param1 can't be null or blank"),
		USER_ID_NOT_DEFINED("User name not defined"),
		INVALID_USER_ID("Invalid user name"),
		INVALID_PASSWORD("Invalid password"),
		USER_PASS_NOT_DEFINED("User password not defined"),
		AGENT_ID_NOT_FOUND("No agent found for the given user name"),
		INVALID_REQUEST_PARAM("Invalid request param"),
		OLD_PASS_NOT_MATCHED("Old password does not match."),
		CONFIRM_PASS_NOT_MATCHED("Confirm password does not match."),
		DUE_DATE_EXPIRED("Bill due date has expired"), 
		NO_BILL_DUE("Bill already paid"),
		NO_FINANCIAL_TRANSACTION_DETAIL_FOUND("No financial transaction detail found"),
		NO_BILL_DATA("No bill data available in DB");
		
		private final String description;

		private ValidationErrorReason(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

	}

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1006526618317617268L;
	private ValidationErrorReason	errorReason;
	private String					customDescription;
	private Ack						ack;
	private String					refId;
	private String					txnRefId;

	private ValidationException(ValidationErrorReason errorReason, String customDescription) {
		super();
		this.errorReason = errorReason;
		this.customDescription = customDescription;
	}

	private ValidationException(Ack ack) {
		super();
		this.ack = ack;
	}

	public String getCode() {
		if (this.errorReason != null) {
			return this.errorReason.name();
		} else if (ack != null && !ack.getErrorMessages().isEmpty() && ack.getErrorMessages().get(0) != null) {
			return ack.getErrorMessages().get(0).getErrorCd();
		}
		return null;
	}

	public String getDescription() {
		if (this.customDescription != null) {
			return this.customDescription;
		} else if (this.errorReason != null) {
			return this.errorReason.getDescription();
		} else if (ack != null && !ack.getErrorMessages().isEmpty() && ack.getErrorMessages().get(0) != null) {
			return ack.getErrorMessages().get(0).getErrorDtl();
		}
		return null;
	}
	
	public boolean matchReason(ValidationErrorReason errorReason) {
		return this.errorReason == errorReason;
	}

	public static ValidationException getInstance(ValidationErrorReason reason) {
		return new ValidationException(reason, null);
	}

	public static ValidationException getInstance(ValidationErrorReason reason, String customDescription) {
		return new ValidationException(reason, customDescription);
	}

	public static ValidationException getInstance(Ack ack) {
		return new ValidationException(ack);
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getTxnRefId() {
		return txnRefId;
	}

	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}

	public Ack getAck() {
		return ack;
	}

	public void setAck(Ack ack) {
		this.ack = ack;
	}

}
