package com.rssoftware.ou.utils;

import java.io.IOException;
import java.util.List;

import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.ErrorMessage;
import org.bbps.schema.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rssoftware.ou.common.ErrorCode;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.tenant.service.BillerOUtransactionDataService;
import com.rssoftware.ou.tenant.service.ErrorCodesService;
import com.rssoftware.ou.tenant.service.MyBillerDetailService;



public class BillerUtil {
	private static Logger logger = LoggerFactory.getLogger(BillerUtil.class);
	public static boolean validate(BillFetchRequest billFetchRequest, List<ErrorMessage> errors,
			ErrorCodesService errorCodesService,MyBillerDetailService myBillerDetailService,
			BillerOUtransactionDataService billerOUtransactionDataService) {
			
		if (billFetchRequest == null) {
			errors.add(errorCodesService.getErrorMessage(ErrorCode.REQUEST_INVALID_ACK));
		}
		else{
			if (billFetchRequest.getHead() == null) {
				errors.add(errorCodesService.getErrorMessage(ErrorCode.HEAD_REQUIRED_ACK));
			}
			else{
				try {
					if(billerOUtransactionDataService.getTransactionData(billFetchRequest.getHead().getRefId(), RequestType.FETCH) != null)
						errors.add(errorCodesService.getErrorMessage(ErrorCode.DUPLICATE_REFID_ACK));
				} catch (IOException e) {
						logger.error("Validate RefId of BillFetchRequest", e);
				}
			}
			if (billFetchRequest.getBillDetails() == null) {
				errors.add(errorCodesService.getErrorMessage(ErrorCode.BILLDETAILS_NULL_ACK));
			}
			if (billFetchRequest.getBillDetails() != null
					&& billFetchRequest.getBillDetails().getBiller() == null) {
				errors.add(errorCodesService.getErrorMessage(ErrorCode.BILLER_NULL_ACK));
			}
			if (billFetchRequest.getBillDetails() != null
					&& billFetchRequest.getBillDetails().getBiller() != null) {
	
				if (billFetchRequest.getBillDetails().getBiller().getId() == null || "".equals(billFetchRequest.getBillDetails().getBiller().getId())) {
					errors.add(errorCodesService.getErrorMessage(ErrorCode.BILLERID_NULL_ACK));
				}
				else{
					try {
						if(myBillerDetailService.getBillerById(billFetchRequest.getBillDetails().getBiller().getId())==null){
							errors.add(errorCodesService.getErrorMessage(ErrorCode.INVALID_BILLER_ACK));
						}
					} catch (IOException e) {
		    				logger.error("Validate BillerID of BillFetchRequest", e);
					}
				}
			}
		}
		return errors.size() == 0;
	}
	
	public static boolean validate(BillPaymentRequest billPaymentRequest, List<ErrorMessage> errors,
			ErrorCodesService errorCodesService,MyBillerDetailService myBillerDetailService,
			BillerOUtransactionDataService billerOUtransactionDataService) {
		
		if (billPaymentRequest == null) {
			errors.add(errorCodesService.getErrorMessage(ErrorCode.REQUEST_INVALID_ACK));
		}
		else{
			RequestType requestType=null;
			if(billPaymentRequest.getTxn().getType().equals(TransactionType.FORWARD_TYPE_REQUEST.value())){
				requestType=RequestType.PAYMENT;
			}
			else if(billPaymentRequest.getTxn().getType().equals(TransactionType.REVERSAL_TYPE_REQUEST.value())){
				requestType=RequestType.REVERSAL;
			}
			if (billPaymentRequest.getHead() == null) {
				errors.add(errorCodesService.getErrorMessage(ErrorCode.HEAD_REQUIRED_ACK));
			}
			else{
				try {
					if(billerOUtransactionDataService.getTransactionData(billPaymentRequest.getHead().getRefId(), requestType) != null)
						errors.add(errorCodesService.getErrorMessage(ErrorCode.DUPLICATE_REFID_ACK));
				} catch (IOException e) {
						logger.error("Validate RefId of BillPaymentRequest", e);
				}
			}
			if(requestType!=RequestType.REVERSAL){
					if (billPaymentRequest.getBillDetails() == null) {
						errors.add(errorCodesService.getErrorMessage(ErrorCode.BILLDETAILS_NULL_ACK));
					}
					if (billPaymentRequest.getBillDetails() != null
							&& billPaymentRequest.getBillDetails().getBiller() == null) {
						errors.add(errorCodesService.getErrorMessage(ErrorCode.BILLER_NULL_ACK));
					}
					if (billPaymentRequest.getBillDetails() != null
							&& billPaymentRequest.getBillDetails().getBiller() != null) {
			
						if (billPaymentRequest.getBillDetails().getBiller().getId() == null || "".equals(billPaymentRequest.getBillDetails().getBiller().getId())) {
							errors.add(errorCodesService.getErrorMessage(ErrorCode.BILLERID_NULL_ACK));
						}
						else{
							try {
								if(myBillerDetailService.getBillerById(billPaymentRequest.getBillDetails().getBiller().getId())==null){
									errors.add(errorCodesService.getErrorMessage(ErrorCode.INVALID_BILLER_ACK));
								}
							} catch (IOException e) {
				    				logger.error("Validate BillerID of BillPaymentRequest", e);
							}
						}
					}
			}
		}
		return errors.size() == 0;
	}
	/*public static boolean isOnline(BillerView biller) {
		return "ONLINE".equals(biller.getBlrMode());
	}
	
	public static boolean isOfflineA(BillerView biller) {
		return "OFFLINEA".equals(biller.getBlrMode());
	}
	
	public static boolean isOfflineB(BillerView biller) {
		return "OFFLINEB".equals(biller.getBlrMode());
	}*/
	
}