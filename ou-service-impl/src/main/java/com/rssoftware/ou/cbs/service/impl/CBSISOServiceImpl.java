//package com.rssoftware.ou.cbs.service.impl;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.rssoftware.ou.cbsiso.gateway.CbsIsoFromUPIGateway;
//import com.rssoftware.ou.common.MetaApiName;
//import com.rssoftware.ou.helper.CBSISOHelper;
//import com.rssoftware.ou.model.cbs.AccountDetails;
//import com.rssoftware.ou.model.cbs.AccountType;
//import com.rssoftware.ou.model.cbs.CBSRequest;
//import com.rssoftware.ou.model.cbs.CBSResponse;
//import com.rssoftware.ou.model.cbs.ProcessingMessage;
//import com.rssoftware.ou.model.cbs.TxnStatus;
//import com.rssoftware.ou.model.cbs.TxnType;
//import com.rssoftware.ou.schema.cbs.CBSISOConstants;
//import com.rssoftware.ou.schema.cbs.CbsApiNames;
//import com.rssoftware.ou.service.CBSService;
//import generated.CustAccountDetails;
//
//@Service
////@ConditionalOnProperty(value = "cbs.iso", havingValue = "true", matchIfMissing = false)
//public class CBSISOServiceImpl implements CBSService {
//	
//	private final Logger logf = LoggerFactory.getLogger(getClass());
//	
//	private Map<String,CBSRequest> requestMap = new ConcurrentHashMap<>();
//	
//	private Map<String,CBSResponse> responseMap = new ConcurrentHashMap<>();
//	
//	@Autowired
//	private CbsIsoFromUPIGateway cbsIsoFromUPIGateway;
//	
//	private final Logger log = LoggerFactory.getLogger(getClass());
//
//
//	@Override
//	public void initialize() {
//
//	}
//
//
//	@Override
//	public CBSResponse doDebit(CBSRequest request) {
//		CbsApiNames apiName = request.getApiName();
//		request.setTxnType(TxnType.DEBIT);
//		requestMap.put(request.getTxnRefId(), request);
//		
//		CBSRequest req = new CBSRequest();
//		req.setMobileNo(request.getMobileNo());
//		req.setAccNo(request.getAccNo());
//		req.setApiName(request.getApiName());
//		req.setTxnRefId(request.getTxnRefId());
//		
//		ProcessingMessage pmsg = new ProcessingMessage();
//		//pmsg.setMetaApiName(MetaApiName.DoDebit);
//		request.setTxnType(TxnType.DEBIT);
//		pmsg.setPayload(request);
//		pmsg.setTransactionId(request.getTxnRefId());
//				
//		cbsIsoFromUPIGateway.sendMessage(pmsg);
//		CBSResponse cbsResponse = waitForResponse(request, apiName);
//		cbsResponse.setTxnType(TxnType.DEBIT);
//		
//		if (cbsResponse.getStatus() == TxnStatus.FAILURE) {
//			cbsResponse.setCbsRefId(StringUtils.EMPTY);
//		}
//		return cbsResponse;
//	}
//	
//	private CBSResponse waitForSolResponse(String accNo,String mob, CbsApiNames apiName) {
//		
//		ProcessingMessage pmsg = new ProcessingMessage();
//		
//		//pmsg.setMetaApiName(MetaApiName.DoDebit);
//		CBSRequest request = new CBSRequest();
//		request.setMobileNo(mob);
//		request.setAccNo(accNo);
//		request.setProcessingCode(CBSISOConstants.ISO_SOL_PRCSNG_CODE);
//		request.setTxnRefId(request.getTxnRefId());
//		requestMap.put(request.getTxnRefId(), request);
//		pmsg.setPayload(request);
//		pmsg.setTransactionId(request.getTxnRefId());
//		cbsIsoFromUPIGateway.sendMessage(pmsg);
//		
//		boolean timeout = false;
//		try {
//			synchronized (request) {
//				request.wait(CBSISOConstants.ISO_REQUEST_TIMEOUT);
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			timeout = true;
//		}
//		if (log.isDebugEnabled()) log.debug("timeout: " + timeout + " / responseMap: " + responseMap);
//		if(timeout || responseMap.get(request.getTxnRefId()) == null){
//			
//			CBSResponse cbsResp = new CBSResponse("ERR-TIME-OUT", 
//					"ISO REQUEST TIMED OUT", TxnStatus.FAILURE);
//			cbsResp.setTxnType(request.getTxnType());
//			return cbsResp;
//		}
//		
//		CBSResponse cbsResponse = responseMap.get(request.getTxnRefId());
//		cbsResponse.setTxnType(request.getTxnType());
//		
//		requestMap.remove(request.getTxnRefId());
//		responseMap.remove(request.getTxnRefId());
//		return cbsResponse;
//	}
//	
//	
//	private CBSResponse waitForResponse(CBSRequest request, CbsApiNames apiName) {
//		boolean timeout = false;
//		try {
//			synchronized (request) {
//				request.wait(CBSISOConstants.ISO_REQUEST_TIMEOUT);
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			timeout = true;
//		}
//		if(timeout){
////			CBSResponse cbsResp = new CBSResponse(getTimeOutErrorCode(apiName), "ISO REQUEST TIMED OUT", TxnStatus.FAILURE);
//			CBSResponse cbsResp = new CBSResponse("ERR-TIME-OUT", "ISO REQUEST TIMED OUT", TxnStatus.FAILURE);
//			cbsResp.setTxnType(request.getTxnType());
//			return cbsResp;
//		}
//		
//		CBSResponse cbsResponse = null;
//		if(request.getTxnRefId()!=null )
//			cbsResponse  = responseMap.get(request.getTxnRefId());
//		if(cbsResponse == null && request.getOriginalTxnId()!=null){
//			cbsResponse = responseMap.get(request.getOriginalTxnId());
//		}
//		if(request.getOriginalTxnId() != null && requestMap.get(request.getOriginalTxnId())!=null){
//			requestMap.remove(request.getOriginalTxnId());
//			responseMap.remove(request.getOriginalTxnId());
//		}
//		if(request.getTxnRefId() != null && requestMap.get(request.getTxnRefId())!=null){
//			requestMap.remove(request.getTxnRefId());
//			responseMap.remove(request.getTxnRefId());
//		}
//		if (log.isDebugEnabled()) log.debug("cbsResponse in waitForResponse: " + cbsResponse);
//		if (cbsResponse == null) {
////			CBSResponse cbsResp = new CBSResponse(getTimeOutErrorCode(apiName), "ISO REQUEST TIMED OUT", TxnStatus.FAILURE);
//			CBSResponse cbsResp = new CBSResponse("ERR-TIME-OUT", "ISO REQUEST TIMED OUT", TxnStatus.FAILURE);
//			cbsResp.setTxnType(request.getTxnType());
//			if (log.isDebugEnabled()) log.debug("CBS is still null - returning from waitForResponse with TimedOut Error!");
//			return cbsResp;
//		}
//		cbsResponse.setTxnType(request.getTxnType());
//		return cbsResponse;
//	}
//
//	@Override
//	public CBSResponse doCredit(CBSRequest request) {
//		CbsApiNames apiName = request.getApiName();
//		request.setTxnType(TxnType.CREDIT);
//		
//		ProcessingMessage pmsg = new ProcessingMessage();
//		pmsg.setTransactionId(request.getTxnRefId());
//		//pmsg.setMetaApiName(MetaApiName.DoCredit);
//		request.setTxnType(TxnType.CREDIT);
//		pmsg.setPayload(request);
//		
//		cbsIsoFromUPIGateway.sendMessage(pmsg);
//		CBSResponse cbsResponse = waitForResponse(request, apiName);
//		cbsResponse.setTxnType(TxnType.CREDIT);
//		if (cbsResponse.getStatus() == TxnStatus.FAILURE) {
//			cbsResponse.setCbsRefId(StringUtils.EMPTY);
//		}
//		return cbsResponse;
//	}
//
//	@Override
//	public CBSResponse doBalanceEnquiry(CBSRequest request) {
//		CbsApiNames apiName = request.getApiName();
//		CBSResponse resp = doListAccount(request);
//		if (resp.getStatus() == TxnStatus.FAILURE) {
//			resp.setTxnType(TxnType.BALANCE_ENQ);
//			return resp;
//		}
//		
//		request.setMobileNo(request.getMobileNo().length() > 10 ? request.getMobileNo().substring(request.getMobileNo().length() - 10) : request.getMobileNo());
//
//		
//		if (log.isErrorEnabled()) log.debug("Got Account Details, fetching balance now");
//		requestMap.put(request.getTxnRefId(), request);
//		if (log.isErrorEnabled()) log.debug("requestMap: " + requestMap);
//		if (log.isErrorEnabled()) log.debug("Waiting for SolResponse...");
//		CBSResponse cbsResp = waitForSolResponse(request.getAccNo(),request.getMobileNo(), apiName);
//		if (log.isErrorEnabled()) log.debug("Received Sol Response, Error Code: " + cbsResp.getErrorCode());
//		if(cbsResp.getErrorCode()!=null){
//			cbsResp.setTxnType(TxnType.BALANCE_ENQ);
//			return cbsResp;
//		}
//		if (log.isErrorEnabled()) log.debug("Setting cust id: " + cbsResp.getCustId());
//		request.setCustId(cbsResp.getCustId());
//		request.setAccNo(CBSISOConstants.ISO_BANK_CODE+" "+cbsResp.getSol()+" "+request.getAccNo());
//		if (log.isErrorEnabled()) log.debug("Account Number: " + request.getAccNo());
//		ProcessingMessage pmsg = new ProcessingMessage();
//		pmsg.setTransactionId(request.getTxnRefId());
//		pmsg.setMetaApiName(MetaApiName.ReqBalEnq);
//		request.setTxnType(TxnType.BALANCE_ENQ);
//		pmsg.setPayload(request);
//		if (log.isErrorEnabled()) log.debug("Sending p_message: " + pmsg);
//		cbsIsoFromUPIGateway.sendMessage(pmsg);
//		if (log.isErrorEnabled()) log.debug("Waiting for response...");
//		CBSResponse cbsResponse = waitForResponse(request, apiName);
//		if (log.isErrorEnabled()) log.debug("Got Response: " + cbsResponse.getErrorCode());
//		cbsResponse.setTxnType(TxnType.BALANCE_ENQ);
//		return cbsResponse;
//	}
//
//	/**
//	 * Required - Mobile Number
//	 */
//	private CBSResponse doListAccount(CBSRequest request) {
//		CbsApiNames apiName = request.getApiName();
//		String mobileNo = request.getMobileNo().length() == 10 ? "91" + request.getMobileNo() : request.getMobileNo();
//		CustAccountDetails acctDetls = CBSISOHelper.listAcct(mobileNo, request.getAccNo());
//		if (log.isDebugEnabled()) {
//			log.debug("CBSISOServiceImpl - doListAccount response from listAcct soRf: " + acctDetls.getSORf());
//			log.debug("CBSISOServiceImpl - doListAccount response from listAcct msg: " + acctDetls.getMsg());
//		}
//		if (CBSISOHelper.FAILED.equals(acctDetls.getSORf())) {
//			
//			CBSResponse response = new CBSResponse("NO_ACCOUNT_FOUND_IN_CBS", acctDetls.getMsg(), TxnStatus.FAILURE, request.getTxnType()); // errorCodeMappingService.getErrorCode(UPIErrorKeys.NO_ACCOUNT_FOUND_IN_CBS, apiName)
//			return response;
//		}
//		CBSResponse response = new CBSResponse();
//		
//		for (CustAccountDetails.AccountDetails accountDetails : acctDetls.getAccountDetails()) {
//			if (log.isDebugEnabled()) log.debug("From List Account, Acc IFSC: " + accountDetails.getAcctIFSC());
//			response.setCustId(accountDetails.getCustId());
//			AccountDetails acctDetails = new AccountDetails();
//			//acctDetails.setAcctBalance(acctDetls..getAcctbalance());
//			acctDetails.setAcctNo(accountDetails.getAcctNo());
//			//acctDetails.setClearBalance(cad.getAcctdetails().getAcctbalance());
//			acctDetails.setCurrency("INR");
//			//acctDetails.setCustomerName(cad.getCustDetails().getCustName());
//			acctDetails.setIfsc(accountDetails.getAcctIFSC());
//			acctDetails.setAccountType("SBA".equals(accountDetails.getAcctType())?AccountType.SAVINGS:AccountType.CURRENT);
//			
//			// Set Meba if an Mpin is there for the mobile number
////			MpinMapperDetails mpinDetails = mpinService.getMPinDetails(mobileNo.length() > 10 ? mobileNo.substring(mobileNo.length() - 10) : mobileNo);
//			acctDetails.setAeba(true);
//			if (log.isDebugEnabled()) log.debug("Mobilebanking flag from CBS: " + acctDetls.getMobBanking());
////			acctDetails.setMeba(mpinDetails != null && acctDetls.getMobBanking().equals("Y"));
//			acctDetails.setCustomerName(accountDetails.getAcctName());
//			//acctDetails.setMmid("9013123"); //Hardcoded with prefix IDBI NBIN - 9013
//			
//			response.getAccountDetails().add(acctDetails);
//		}
//		
//		response.setStatus(TxnStatus.SUCCESS);
//		return response;
//	}
//
//	
//
//	@Override
//	public CBSResponse doDebitReversal(CBSRequest request) {
//		CbsApiNames apiName = request.getApiName();
//		CBSResponse response = new CBSResponse();
//		response.setTxnType(TxnType.DEBIT_REVERSAL);
//		
//		
//		ProcessingMessage pmsg = new ProcessingMessage();
//		pmsg.setTransactionId(request.getTxnRefId());
//		//pmsg.setMetaApiName(MetaApiName.DoDebitReversal);
//		request.setTxnType(TxnType.DEBIT_REVERSAL);
//		pmsg.setPayload(request);
//		cbsIsoFromUPIGateway.sendMessage(pmsg);
//		CBSResponse cbsResponse = waitForResponse(request, apiName);
//		cbsResponse.setTxnType(TxnType.DEBIT_REVERSAL);
//		if (cbsResponse.getStatus() == TxnStatus.FAILURE) {
//			cbsResponse.setCbsRefId(StringUtils.EMPTY);
//		}
//		if (this.log.isDebugEnabled()) this.log.debug("returning from doDebitReversal - Status: " + cbsResponse.getStatus());
//		return cbsResponse;
//	}
//
//	@Override
//	public CBSResponse doCreditReversal(CBSRequest request) {
//		CbsApiNames apiName = request.getApiName();
//		CBSResponse response = new CBSResponse();
//		response.setTxnType(TxnType.CREDIT_REVERSAL);
//
//		ProcessingMessage pmsg = new ProcessingMessage();
//		pmsg.setTransactionId(request.getTxnRefId());
//		//pmsg.setMetaApiName(MetaApiName.DoCreditReversal);
//		request.setTxnType(TxnType.CREDIT_REVERSAL);
//		pmsg.setPayload(request);
//		cbsIsoFromUPIGateway.sendMessage(pmsg);
//		CBSResponse cbsResponse = waitForResponse(request, apiName);
//		cbsResponse.setTxnType(TxnType.CREDIT_REVERSAL);
//		if (cbsResponse.getStatus() == TxnStatus.FAILURE) {
//			cbsResponse.setCbsRefId(StringUtils.EMPTY);
//		}
//		return cbsResponse;
//	}
//
//
//}
