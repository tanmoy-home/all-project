package com.rssoftware.ou.common;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.ReasonType;
import org.bbps.schema.TxnType;
import org.springframework.core.env.Environment;

import com.rssoftware.framework.hibernate.dao.common.BeanLocator;
import com.rssoftware.ou.common.utils.RequestResponseGenerator;
import com.rssoftware.ou.database.entity.tenant.COUErrorCodes;
import com.rssoftware.ou.domain.ResponseCode;
import com.rssoftware.ou.tenant.service.ErrorCodesService;

import in.co.rssoftware.bbps.schema.ComplaintRaisedResponse;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.FetchResponse;
import in.co.rssoftware.bbps.schema.PaymentReceipt;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

public class ErrorCodeUtil {
	
	private static Log logger = LogFactory.getLog(ErrorCodeUtil.class);	

	public static String getErrorCode(String ERROR_CODE_CONSTANT) {
		Environment env = BeanLocator.getBean(Environment.class);
		return env.getProperty(ERROR_CODE_CONSTANT);
	}

	public static String getErrorMessage(String errorCode) {
		ErrorCodesService service = BeanLocator.getBean(ErrorCodesService.class);
		return service.getErrorMessage(errorCode);
	}

	public static COUErrorCodes getCOUErrorCodes(String errorCode) {
		ErrorCodesService service = BeanLocator.getBean(ErrorCodesService.class);
		return service.getCOUErrorCodes(errorCode);
	}

	public static String getCommaSeperatorErrorCodesFromAck(Ack ack) {
		String errorCodes = "";
		List<org.bbps.schema.ErrorMessage> errorMessages = (List<org.bbps.schema.ErrorMessage>) ack.getErrorMessages();
		if (!errorMessages.isEmpty()) {
			for (org.bbps.schema.ErrorMessage errorMessage : errorMessages) {
				errorCodes += errorMessage.getErrorCd() + ',';				
			}
		}
		return errorCodes.replaceAll(",$", "");
	}

	public static String getDelimitedErrorDetailsFromAck(Ack ack) {
		String errorCode = "", errorDetails = "", customErrorMessage = "";
		List<org.bbps.schema.ErrorMessage> errorMessages = (List<org.bbps.schema.ErrorMessage>) ack.getErrorMessages();
		if (!errorMessages.isEmpty()) {
			for (org.bbps.schema.ErrorMessage errorMessage : errorMessages) {
				errorCode = errorMessage.getErrorCd();
				customErrorMessage = ErrorCodeUtil.getErrorMessage(errorCode);
				if (customErrorMessage != null && !"".equals(customErrorMessage)) {
					logger.info("Showing our custom message from database.");
					errorDetails += customErrorMessage + '|';
				} else {
					logger.info("Showing message from ack.");
					errorDetails += errorMessage.getErrorDtl() + '|';
				}
			}
		}
		return errorDetails.replaceAll("\\|$", "");
	}
	
	/*
	 * public static BillFetchResponse
	 * populateErroneousResponseWithActualErrorMessage(BillFetchResponse
	 * billFetchResponse) { if (billFetchResponse != null &&
	 * billFetchResponse.getReason().getResponseCode() != "000") { String
	 * actualMsg = ErrorCodeUtil.getErrorMessage(billFetchResponse.getReason().
	 * getComplianceReason()); if (actualMsg != null) {
	 * billFetchResponse.getReason().setComplianceRespCd(billFetchResponse.
	 * getReason().getComplianceReason());
	 * billFetchResponse.getReason().setComplianceReason(actualMsg); } else {
	 * actualMsg = ErrorCodeUtil.getErrorMessage(billFetchResponse.getReason().
	 * getComplianceRespCd()); if (actualMsg != null) {
	 * billFetchResponse.getReason().setComplianceReason(actualMsg); } } }
	 * return billFetchResponse; }
	 */

	public static FetchResponse generateErroneousFetchResponse(BillFetchResponse billFetchResponse) {
		FetchResponse fetchResponse = new FetchResponse();		

		if (billFetchResponse != null && billFetchResponse.getReason().getComplianceRespCd() == "BOU001") {			
			fetchResponse.getErrorMessages()
					.add(ErrorCodeUtil.generateErrorMessage(billFetchResponse.getReason().getComplianceRespCd(),
							billFetchResponse.getReason().getComplianceReason()));
		} else if (billFetchResponse != null && billFetchResponse.getReason().getComplianceRespCd() == "BOU002") {
			String errorCodesStr = billFetchResponse.getReason().getComplianceReason();
			String[] errorCodes = errorCodesStr.split(",");
			for (String errorCode : errorCodes) {
				fetchResponse.getErrorMessages().add(ErrorCodeUtil.generateErrorMessage(errorCode));
			}
		} else if (billFetchResponse != null
				&& (billFetchResponse.getReason().getComplianceRespCd().equals(ErrorCodeUtil.getErrorCode("COU_DUPLICATE_REQUEST"))
						|| billFetchResponse.getReason().getComplianceRespCd().equals(ErrorCodeUtil.getErrorCode("COU_REQUEST_TIMEOUT"))
						|| billFetchResponse.getReason().getComplianceRespCd().equals(ErrorCodeUtil.getErrorCode("COU_NETWORK_ERROR")))) {			
			fetchResponse.getErrorMessages()
					.add(ErrorCodeUtil.generateErrorMessage(billFetchResponse.getReason().getComplianceRespCd(),
							billFetchResponse.getReason().getComplianceReason()));
		} else {
			String errorCd = billFetchResponse.getReason().getComplianceRespCd();
			String complianceReason = ErrorCodeUtil.getErrorMessage(billFetchResponse.getReason().getComplianceReason());
			String errorMsg = complianceReason != null ? complianceReason : billFetchResponse.getReason().getComplianceReason();			
			fetchResponse.getErrorMessages().add(ErrorCodeUtil.generateErrorMessage(errorCd, errorMsg));
		}
		return fetchResponse;
	}

	public static FetchResponse generateFetchResponseForErroneousAck(BillFetchResponse billFetchResponse) {
		FetchResponse fetchResponse = new FetchResponse();
		String errorCodesStr = billFetchResponse.getReason().getComplianceRespCd();
		String errorDetailsStr = billFetchResponse.getReason().getComplianceReason();
		String[] errorCodes = errorCodesStr.split(",");
		String[] errorDetails = errorDetailsStr.split("\\|");
		int cntr = 0;
		for (String errorCode : errorCodes) {			
			fetchResponse.getErrorMessages().add(ErrorCodeUtil.generateErrorMessage(errorCode, errorDetails[cntr++]));
		}
		return fetchResponse;
	}

	/*
	 * public static BillPaymentResponse
	 * populateErroneousResponseWithActualErrorMessage(BillPaymentResponse
	 * billPaymentResponse) { if (billPaymentResponse != null &&
	 * billPaymentResponse.getReason().getResponseCode() != "000") { String
	 * actualMsg =
	 * ErrorCodeUtil.getErrorMessage(billPaymentResponse.getReason().
	 * getComplianceReason()); if (actualMsg != null) {
	 * billPaymentResponse.getReason().setComplianceRespCd(billPaymentResponse.
	 * getReason().getComplianceReason());
	 * billPaymentResponse.getReason().setComplianceReason(actualMsg); } else {
	 * actualMsg =
	 * ErrorCodeUtil.getErrorMessage(billPaymentResponse.getReason().
	 * getComplianceRespCd()); if (actualMsg != null) {
	 * billPaymentResponse.getReason().setComplianceReason(actualMsg); } } }
	 * return billPaymentResponse; }
	 */

	public static PaymentReceipt generateErroneousPaymentReceipt(BillPaymentResponse billPaymentResponse) {
		PaymentReceipt paymentReceipt = new PaymentReceipt();

		if (billPaymentResponse != null && billPaymentResponse.getReason().getComplianceRespCd() == "BOU001") {
			paymentReceipt.getErrorMessages()
					.add(ErrorCodeUtil.generateErrorMessage(billPaymentResponse.getReason().getComplianceRespCd(),
							billPaymentResponse.getReason().getComplianceReason()));
		} else if (billPaymentResponse != null && billPaymentResponse.getReason().getComplianceRespCd() == "BOU002") {
			String errorCodesStr = billPaymentResponse.getReason().getComplianceReason();
			String[] errorCodes = errorCodesStr.split(",");
			for (String errorCode : errorCodes) {
				paymentReceipt.getErrorMessages().add(ErrorCodeUtil.generateErrorMessage(errorCode));
			}
		} else if (billPaymentResponse != null
				&& (billPaymentResponse.getReason().getComplianceRespCd() == ErrorCodeUtil.getErrorCode("COU_DUPLICATE_REQUEST")
						|| billPaymentResponse.getReason().getComplianceRespCd() == ErrorCodeUtil.getErrorCode("COU_REQUEST_TIMEOUT")
						|| billPaymentResponse.getReason().getComplianceRespCd() == ErrorCodeUtil.getErrorCode("COU_NETWORK_ERROR"))) {			
			paymentReceipt.getErrorMessages()
					.add(ErrorCodeUtil.generateErrorMessage(billPaymentResponse.getReason().getComplianceRespCd(),
							billPaymentResponse.getReason().getComplianceReason()));
		}
		return paymentReceipt;
	}

	public static PaymentReceipt generateErroneousPaymentReceipt(String errorCodeConstant) {
		PaymentReceipt paymentReceipt = new PaymentReceipt();		
		paymentReceipt.getErrorMessages().add(ErrorCodeUtil.generateErrorMessage(ErrorCodeUtil.getErrorCode(errorCodeConstant)));
		return paymentReceipt;
	}

	public static PaymentReceipt generatePaymentReceiptForErroneousAck(BillPaymentResponse billPaymentResponse) {
		PaymentReceipt paymentReceipt = new PaymentReceipt();
		String errorCodesStr = billPaymentResponse.getReason().getComplianceRespCd();
		String errorDetailsStr = billPaymentResponse.getReason().getComplianceReason();
		String[] errorCodes = errorCodesStr.split(",");
		String[] errorDetails = errorDetailsStr.split("\\|");
		int cntr = 0;
		for (String errorCode : errorCodes) {
			paymentReceipt.getErrorMessages().add(ErrorCodeUtil.generateErrorMessage(errorCode, errorDetails[cntr++]));
		}
		return paymentReceipt;
	}

	public static BillPaymentResponse billPaymentErrorResponseForCOU(BillPaymentRequest billPaymentRequest,
			String ouName, Ack ack) {
		/*
		 * COUErrorCodes errorDetails =
		 * ErrorCodeUtil.getCOUErrorCodes(ack.getErrorMessages().get(0).
		 * getErrorCd());
		 * 
		 * if (errorDetails == null) { errorDetails = new COUErrorCodes();
		 * errorDetails.setErrorCode(ack.getErrorMessages().get(0).getErrorCd())
		 * ; errorDetails.setErrorMessage(ack.getErrorMessages().get(0).
		 * getErrorDtl()); errorDetails.setResponseCode("300"); }
		 */

		BillPaymentResponse response = new BillPaymentResponse();
		response.setHead(RequestResponseGenerator.getHead(ouName, billPaymentRequest.getHead().getRefId()));
		// Creating Reason tag
		ReasonType reasonType = new ReasonType();
		/*
		 * reasonType.setResponseCode(errorDetails.getResponseCode());
		 * reasonType.setResponseReason(ResponseCode.Failure.name());
		 * reasonType.setComplianceRespCd(errorDetails.getErrorCode());
		 * reasonType.setComplianceReason(errorDetails.getErrorMessage());
		 */
		reasonType.setResponseCode("ACK");
		reasonType.setResponseReason(ResponseCode.Failure.name());
		reasonType.setComplianceRespCd(ErrorCodeUtil.getCommaSeperatorErrorCodesFromAck(ack));
		reasonType.setComplianceReason(ErrorCodeUtil.getDelimitedErrorDetailsFromAck(ack));
		response.setReason(reasonType);
		response.setTxn(null);
		// Creating transaction tag.
		TxnType txnType = billPaymentRequest.getTxn();
		txnType.setRiskScores(null);
		txnType.setType(null);
		response.setTxn(txnType);
		return response;
	}

	public static BillPaymentResponse billPaymentErrorResponseForCOU(BillPaymentRequest billPaymentRequest,
			String ouName, String KEY_COMPLIANCE_CODE) {		
		String complianceCode = ErrorCodeUtil.getErrorCode(KEY_COMPLIANCE_CODE);
		COUErrorCodes errorDetails = ErrorCodeUtil.getCOUErrorCodes(complianceCode);
		
		/*
		 * COUErrorCodes errorDetails = null; if (errorCode != null) {
		 * errorDetails = ErrorCodeUtil.getCOUErrorCodes(errorCode); } else {
		 * errorDetails = ErrorCodeUtil.getCOUErrorCodes(ERROR_CODE_CONSTANT); }
		 */

		BillPaymentResponse response = new BillPaymentResponse();
		response.setHead(RequestResponseGenerator.getHead(ouName, billPaymentRequest.getHead().getRefId()));
		// Creating Reason tag
		ReasonType reasonType = new ReasonType();
		/*
		 * reasonType.setResponseCode(errorDetails.getResponseCode());
		 * reasonType.setResponseReason(ResponseCode.Failure.name());
		 * reasonType.setComplianceRespCd(errorDetails.getErrorCode());
		 * reasonType.setComplianceReason(errorDetails.getErrorMessage());
		 */
		reasonType.setResponseCode(errorDetails.getResponseCode());
		reasonType.setResponseReason(ResponseCode.Failure.name());
		reasonType.setComplianceRespCd(complianceCode);
		reasonType.setComplianceReason(errorDetails.getErrorMessage());
		response.setReason(reasonType);
		response.setTxn(null);
		// Creating transaction tag.
		TxnType txnType = billPaymentRequest.getTxn();
		txnType.setRiskScores(null);
		txnType.setType(null);
		response.setTxn(txnType);
		return response;
	}

	public static BillFetchResponse billFetchErrorRespForCOU(BillFetchRequest billFetchRequest, String ouName,
			Ack ack) {
		/*
		 * COUErrorCodes errorDetails =
		 * ErrorCodeUtil.getCOUErrorCodes(ack.getErrorMessages().get(0).
		 * getErrorCd()); if (errorDetails == null) { errorDetails = new
		 * COUErrorCodes();
		 * errorDetails.setErrorCode(ack.getErrorMessages().get(0).getErrorCd())
		 * ; errorDetails.setErrorMessage(ack.getErrorMessages().get(0).
		 * getErrorDtl()); errorDetails.setResponseCode("300"); }
		 */

		BillFetchResponse response = new BillFetchResponse();
		response.setHead(RequestResponseGenerator.getHead(ouName, billFetchRequest.getHead().getRefId()));
		// Creating Reason tag
		ReasonType reasonType = new ReasonType();
		reasonType.setResponseCode("ACK");
		reasonType.setResponseReason(ResponseCode.Failure.name());
		reasonType.setComplianceRespCd(ErrorCodeUtil.getCommaSeperatorErrorCodesFromAck(ack));
		reasonType.setComplianceReason(ErrorCodeUtil.getDelimitedErrorDetailsFromAck(ack));
		response.setReason(reasonType);
		response.setTxn(null);
		// Creating transaction tag.
		TxnType txnType = billFetchRequest.getTxn();
		txnType.setRiskScores(null);
		txnType.setType(null);
		response.setTxn(txnType);
		return response;
	}

	public static BillFetchResponse billFetchErrorRespForCOU(BillFetchRequest billFetchRequest, String ouName,
			String KEY_COMPLIANCE_CODE) {
		String complianceCode = ErrorCodeUtil.getErrorCode(KEY_COMPLIANCE_CODE);
		COUErrorCodes errorDetails = ErrorCodeUtil.getCOUErrorCodes(complianceCode);
		/*
		 * COUErrorCodes errorDetails = null; if (errorCode != null) {
		 * errorDetails = ErrorCodeUtil.getCOUErrorCodes(errorCode); } else {
		 * errorDetails = ErrorCodeUtil.getCOUErrorCodes(ERROR_CODE_CONSTANT); }
		 */

		BillFetchResponse response = new BillFetchResponse();
		response.setHead(RequestResponseGenerator.getHead(ouName, billFetchRequest.getHead().getRefId()));
		// Creating Reason tag
		ReasonType reasonType = new ReasonType();
		/*
		 * reasonType.setResponseCode(errorDetails.getResponseCode());
		 * reasonType.setResponseReason(ResponseCode.Failure.name());
		 * reasonType.setComplianceRespCd(errorDetails.getErrorCode());
		 * reasonType.setComplianceReason(errorDetails.getErrorMessage());
		 */
		reasonType.setResponseCode(errorDetails.getResponseCode());
		reasonType.setResponseReason(ResponseCode.Failure.name());
		reasonType.setComplianceRespCd(complianceCode);
		reasonType.setComplianceReason(errorDetails.getErrorMessage());
		response.setReason(reasonType);
		response.setTxn(null);
		// Creating transaction tag.
		TxnType txnType = billFetchRequest.getTxn();
		txnType.setRiskScores(null);
		txnType.setType(null);
		response.setTxn(txnType);
		return response;
	}
	
	public static ComplaintRaisedResponse complaintErrorResponseForCOU(String KEY_ERROR_CODE) {
		ComplaintRaisedResponse response = new ComplaintRaisedResponse();
		response.getErrors().add(ErrorCodeUtil.generateErrorMessage(ErrorCodeUtil.getErrorCode(KEY_ERROR_CODE)));
		return response;
	}
	
	public static TxnSearchResponse txnSearchErrorResponseForCOU(String KEY_ERROR_CODE) {
		TxnSearchResponse response = new TxnSearchResponse();
		response.getErrors().add(ErrorCodeUtil.generateErrorMessage(ErrorCodeUtil.getErrorCode(KEY_ERROR_CODE)));
		return response;
	}
	
	public static ErrorMessage generateErrorMessage(String errorCode) {
		return ErrorCodeUtil.generateErrorMessage(errorCode, ErrorCodeUtil.getErrorMessage(errorCode));
	}
	
	public static ErrorMessage generateErrorMessage(String errorCode, String errorMsg) {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setErrorCd(errorCode);
		errorMessage.setErrorDtl(errorMsg);
		return errorMessage;
	}

}
