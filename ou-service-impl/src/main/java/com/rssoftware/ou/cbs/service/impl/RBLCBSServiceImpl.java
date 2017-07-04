package com.rssoftware.ou.cbs.service.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.finacle.fixml.AcctId;
import com.finacle.fixml.Amount;
import com.finacle.fixml.Body;
import com.finacle.fixml.FIXML;
import com.finacle.fixml.Header;
import com.finacle.fixml.MessageKeyType;
import com.finacle.fixml.PartTrnRec;
import com.finacle.fixml.PasswordTokenType;
import com.finacle.fixml.RBL;
import com.finacle.fixml.RequestHeaderType;
import com.finacle.fixml.RequestMessageInfoType;
import com.finacle.fixml.STATUS;
import com.finacle.fixml.SecurityType;
import com.finacle.fixml.TokenType;
import com.finacle.fixml.XferTrnAddRequest;
import com.finacle.fixml.XferTrnAddRq;
import com.finacle.fixml.XferTrnDetail;
import com.finacle.fixml.XferTrnHdr;
import com.rssoftware.ou.common.utils.JaxbUtil;
import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.cbs.CBSResponse;
import com.rssoftware.ou.model.cbs.TxnStatus;
import com.rssoftware.ou.model.cbs.TxnType;
import com.rssoftware.ou.schema.cbs.CbsApiNames;
import com.rssoftware.ou.service.CBSService;
import com.rssoftware.ou.tenant.service.ParamService;

@Service
public class RBLCBSServiceImpl implements CBSService {
	
	@Autowired
	private ParamService paramService;
//	private String CBS_RBL_URL = "http://localhost:9001/rblsimulator/rblCbs/{API}";
//	private String CBS_RBL_URL = "http://10.80.45.226:9000/rbl/{API}";
	private String CBS_RBL_URL = null;//paramService.retrieveStringParamByName("CBS_URL");
	
	private final Logger logf = LoggerFactory.getLogger(getClass());

	private Map<String, String> errorCodeMap = new HashMap<>();

	private String SERVICE_DEBIT = "performDebitCredit";
	private String SERVICE_CREDIT = "performDebitCredit";
	private String SERVICE_DEBIT_REVERSAL = "performDebitCreditReversal";
	private String SERVICE_CREDIT_REVERSAL = "performDebitCreditReversal";
	private String SERVICE_ACCOUNT_SUMMARY = "CustAccountSummary";

	// Used from multiple places, hence made it a constant;
	private final String SESSION_ID_KEY = "SessionId";

	@Override
	public void initialize() {
		// To do
	}

	private static RestTemplate restTemplate = new RestTemplate();

	@Override
	public CBSResponse doBalanceEnquiry(CBSRequest request) {

		CBSResponse response = new CBSResponse();
		Map<String, String> values = getDefaultMap(request);
		RBL rbl = listAccount(values);

		boolean found = false;
		for (int i = 0; (i < rbl.getResponses().size()) && (!found); i++) {
			if (rbl.getResponses().get(i).getAcctdetails().getAcctno().equals(request.getAccNo())) {
				found = true;
				response.setAccBalance(rbl.getResponses().get(i).getAcctdetails().getAcctbalance());
			}
		}
		
		response.setTxnType(TxnType.BALANCE_ENQ);
		if (!found) {
			response.setStatus(TxnStatus.FAILURE);
			response.setErrorCode("INVALID_ACCOUNT");
			response.setErrorMessage("Payer does not exist!");
		} else {
			response.setStatus(TxnStatus.SUCCESS);
		}
		return response;
	}
	
	@Override
	public CBSResponse doDebit(CBSRequest request) {
		CBSResponse response = new CBSResponse();

		FIXML cbsRequestXml = createFIXMLObject(request, TxnType.DEBIT, request.getCreditAccNo());
		FIXML fixmlResponse = postFIXMLRequest(cbsRequestXml, SERVICE_DEBIT);

		if (fixmlResponse == null)
			return new CBSResponse("ERR", "Cbs Timed Out!", TxnStatus.FAILURE, TxnType.DEBIT);

		response.setTxnType(TxnType.DEBIT);
		response.setTxnRefId(fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier().getTrnId());
		response.setStatus(fixmlResponse.getBody().getError() == null ? TxnStatus.SUCCESS : TxnStatus.FAILURE);
		if(fixmlResponse.getBody().getError() != null) {
			response.setErrorCode(fixmlResponse.getBody().getError().getFIBusinessException().getErrorDetails().get(0).getErrorDesc());
		}
		
		// Setting the transaction to cbsRefId field. This field will be used in the audit trail.
		if (fixmlResponse.getBody().getXferTrnAddResponse() != null && fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs() != null && fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier() != null)
			response.setCbsRefId(fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier().getTrnId());
		
		return response;
	}

	@Override
	public CBSResponse doCredit(CBSRequest request) {
		CbsApiNames apiName = request.getApiName();
		CBSResponse response = new CBSResponse();

		FIXML cbsRequestXml = createFIXMLObject(request, TxnType.CREDIT, request.getCreditAccNo());
		FIXML fixmlResponse = postFIXMLRequest(cbsRequestXml, SERVICE_CREDIT);

		if (fixmlResponse == null)
			return new CBSResponse("ERR", "Cbs Timed Out!", TxnStatus.FAILURE, TxnType.CREDIT);

		response.setTxnType(TxnType.CREDIT);
		response.setStatus(fixmlResponse.getBody().getError() == null ? TxnStatus.SUCCESS : TxnStatus.FAILURE);

		// Setting the transaction to cbsRefId field. This field will be used in the audit trail.
		if (fixmlResponse.getBody().getXferTrnAddResponse() != null && fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs() != null && fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier() != null)
			response.setCbsRefId(fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier().getTrnId());
		
		return response;
	}

	@Override
	public CBSResponse doDebitReversal(CBSRequest request) {
		CbsApiNames apiName = request.getApiName();
		CBSResponse response = new CBSResponse();
		response.setTxnType(TxnType.DEBIT_REVERSAL);

		return reverseDebitTransaction(request, response, request.getCreditAccNo());
	}

	@Override
	public CBSResponse doCreditReversal(CBSRequest request) {
		CbsApiNames apiName = request.getApiName();
		CBSResponse response = new CBSResponse();
		response.setTxnType(TxnType.CREDIT_REVERSAL);

		return reverseCreditTransaction(request, response, request.getCreditAccNo());
	}
	
	private Map<String, String> getDefaultMap(CBSRequest request) {
		Map<String, String> values = new HashMap<String, String>();
		/**
		 * TODO Hardcoded for now, has to come in request
		 */
		values.put(SESSION_ID_KEY, StringUtils.isBlank(request.getTxnRefId()) ? String.valueOf(System.currentTimeMillis()) : request.getTxnRefId());
		values.put("RQLoginUserId", request.getCustomerId());
		values.put("RQDeviceFormat", "Android"/* request.getDeviceFormat() */);
		values.put("RQOperationId", request.getOperationId());
		values.put("RQDeviceFamily", "Tablet"/* request.getDeviceFamily() */);
		values.put("RQTransSeq", "01"/* String.valueOf(request.getTransSeq()) */);
		values.put("RQClientAPIVer", "4.1"/* request.getClientApiVer() */);

		return values;
	}

	private CBSResponse reverseDebitTransaction(CBSRequest request, CBSResponse response, String creditAccountNo) {
		CbsApiNames apiName = request.getApiName();
		FIXML cbsRequestXml = createFIXMLObject(request, TxnType.DEBIT_REVERSAL, creditAccountNo);
		FIXML fixmlResponse = postFIXMLRequest(cbsRequestXml, SERVICE_DEBIT_REVERSAL);

		if (fixmlResponse == null)
			return new CBSResponse("ERR", "Cbs Timed Out!", TxnStatus.FAILURE, TxnType.DEBIT_REVERSAL);

		response.setStatus(fixmlResponse.getBody().getError() == null ? TxnStatus.SUCCESS : TxnStatus.FAILURE);

		if (fixmlResponse.getBody().getXferTrnAddResponse() != null
				&& fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs() != null
				&& fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier() != null)
			response.setCbsRefId(
					fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier().getTrnId());

		return response;
	}

	private CBSResponse reverseCreditTransaction(CBSRequest request, CBSResponse response, String creditAccountNo) {
		CbsApiNames apiName = request.getApiName();
		FIXML cbsRequestXml = createFIXMLObject(request, TxnType.CREDIT_REVERSAL, creditAccountNo);
		FIXML fixmlResponse = postFIXMLRequest(cbsRequestXml, SERVICE_CREDIT_REVERSAL);

		if (fixmlResponse == null)
			return new CBSResponse("ERR", "Cbs Timed Out!", TxnStatus.FAILURE, TxnType.CREDIT_REVERSAL);

		response.setStatus(fixmlResponse.getBody().getError() == null ? TxnStatus.SUCCESS : TxnStatus.FAILURE);

		if (fixmlResponse != null && fixmlResponse.getBody().getXferTrnAddResponse() != null
				&& fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs() != null
				&& fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier() != null) {

			response.setCbsRefId(
					fixmlResponse.getBody().getXferTrnAddResponse().getXferTrnAddRs().getTrnIdentifier().getTrnId());
		}

		if (logf.isDebugEnabled()) {
			logf.debug("Txn Status is RBL CBS Service: " + response.getStatus());
		}
		return response;
	}

	private String getStringBody(Map<String, String> values) {
		StringBuilder body = new StringBuilder();

		Iterator<Entry<String, String>> iter = values.entrySet().iterator();
		Entry<String, String> entry = null;
		while (iter.hasNext()) {
			entry = iter.next();
			body.append("&" + entry.getKey() + "=" + entry.getValue());
		}

		return body.substring(1);
	}

	private FIXML postFIXMLRequest(FIXML request, String api) {
		FIXML response = null;
		CBS_RBL_URL = paramService.retrieveStringParamByName("CBS_URL");
		try {
			if (logf.isDebugEnabled()) {
				logf.debug("Sent Request ===> " + JaxbUtil.stringifyJaxb(request));
				logf.debug("CBS URL: " + CBS_RBL_URL.replace("{API}", api));
			}
			String resp;
			resp = restTemplate.postForEntity(CBS_RBL_URL.replace("{API}", api), request, String.class).getBody();

			if (logf.isDebugEnabled()) {
				logf.debug("Received Request ===> " + resp);
			}
			response = JaxbUtil.jaxbifyString(resp, FIXML.class);
		} catch (Exception ex) {
			logf.error(ex.getMessage(), ex);
		}

		return response;
	}

	private FIXML createFIXMLObject(CBSRequest request, TxnType txnType, String creditAccountNo) {
		Calendar now = Calendar.getInstance();
		/************************ HEADER ************************/
		Header header = new Header();
		RequestHeaderType reqHeader = new RequestHeaderType();

		MessageKeyType messageKey = new MessageKeyType();
		messageKey.setChannelId("DMR");
		messageKey.setRequestUUID(request.getRequestId());
		messageKey.setServiceRequestId("XferTrnAdd");
		messageKey.setServiceRequestVersion("10.2");
		messageKey.setLanguageId("");
		reqHeader.setMessageKey(messageKey);

		RequestMessageInfoType messageInfo = new RequestMessageInfoType();
		messageInfo.setBankId("01");
		messageInfo.setTimeZone("");
		messageInfo.setMessageDateTime(now);
		messageInfo.setEntityId("");
		messageInfo.setEntityType("");
		messageInfo.setArmCorrelationId("");
		reqHeader.setRequestMessageInfo(messageInfo);

		SecurityType security = new SecurityType();
		TokenType token = new TokenType();
		PasswordTokenType passwordToken = new PasswordTokenType();
		passwordToken.setUserId("");
		passwordToken.setPassword("");
		token.setPasswordToken(passwordToken);
		security.setToken(token);
		security.setFICertToken("");
		security.setRealUser("");
		security.setRealUserLoginSessionId("");
		security.setRealUserPwd("");
		security.setSSOTransferToken("");
		reqHeader.setSecurity(security);

		header.setRequestHeader(reqHeader);

		/************************ BODY ************************/
		Body body = new Body();
		XferTrnAddRequest trnAddRequest = new XferTrnAddRequest();
		XferTrnAddRq trnAddRq = new XferTrnAddRq();

		XferTrnHdr trnHdr = new XferTrnHdr();
		trnHdr.setTrnType("T");
		/*
		 * trnHdr.setTrnSubType((txnType == TxnType.DEBIT_REVERSAL) ? "BI" :
		 * "CI");
		 */
		trnHdr.setTrnSubType("CI");
		trnAddRq.setXferTrnHdr(trnHdr);

		/***
		 * Amount should be same for both - Payer and Payee Hence, using one
		 * common object in both of them Assuming that Currency Code will also
		 * be the same.
		 */
		Amount amount = new Amount();
		amount.setAmountValue(request.getTxnAmount());
		amount.setCurrencyCode(request.getCurrencyCode().getValue());

		XferTrnDetail trnDetail = new XferTrnDetail();
		PartTrnRec debitor = new PartTrnRec();
		AcctId debitorCustAcctId = new AcctId();
		PartTrnRec creditor = new PartTrnRec();
		AcctId creditorCustAcctId = new AcctId();

		if (txnType == TxnType.DEBIT) {
			creditorCustAcctId.setAcctId(creditAccountNo);
			debitorCustAcctId.setAcctId(request.getAccNo());
		} else if (txnType == TxnType.CREDIT) {
			creditorCustAcctId.setAcctId(request.getAccNo());
			debitorCustAcctId.setAcctId(creditAccountNo);
		} else if (txnType == TxnType.CREDIT_REVERSAL) {
			creditorCustAcctId.setAcctId(creditAccountNo);
			debitorCustAcctId.setAcctId(request.getAccNo());
		} else if (txnType == TxnType.DEBIT_REVERSAL) {
			creditorCustAcctId.setAcctId(request.getAccNo());
			debitorCustAcctId.setAcctId(creditAccountNo);
		}
		debitor.setAcctId(debitorCustAcctId);
		creditor.setAcctId(creditorCustAcctId);

		debitor.setCreditDebitFlg(TxnType.DEBIT.getValue());
		debitor.setTrnAmt(amount);
		debitor.setTrnParticulars("UPI-" + request.getCustRefId());
		debitor.setValueDt(now);
		trnDetail.getPartTrnRecs().add(debitor);

		creditor.setCreditDebitFlg(TxnType.CREDIT.getValue());
		creditor.setTrnAmt(amount);
		creditor.setTrnParticulars("UPI-" + request.getCustRefId());
		creditor.setValueDt(now);
		trnDetail.getPartTrnRecs().add(creditor);

		trnAddRq.setXferTrnDetail(trnDetail);
		trnAddRequest.setXferTrnAddRq(trnAddRq);
		body.setXferTrnAddRequest(trnAddRequest);

		FIXML fixmlObject = new FIXML();
		fixmlObject.setHeader(header);
		fixmlObject.setBody(body);

		return fixmlObject;
	}
	
	private RBL listAccount(Map<String, String> values) {
		RBL response = postStringRequestForFinacle(getStringBody(values), SERVICE_ACCOUNT_SUMMARY, values.get(SESSION_ID_KEY), values.get(SESSION_ID_KEY), "LIST_ACCOUNT");
		return response;
	}
	
	private RBL postStringRequestForFinacle(String body, String api, String txnId, String upiTxnId, String event) {
		RBL response = null;
		CBS_RBL_URL = paramService.retrieveStringParamByName("CBS_URL");
		try {
			
			if (logf.isDebugEnabled()) {
				logf.debug("Sent Request for " + CBS_RBL_URL.replace("{API}", api) + "\n\t ===> " + body);
			}
			
			response = restTemplate.postForObject(CBS_RBL_URL.replace("{API}", api)+"?"+body, null, RBL.class);
			
			body = JaxbUtil.stringifyJaxb(response);
			if (logf.isDebugEnabled()) {
				logf.debug("Received Request for " + api + " ===> " + body);
			}
			
		} catch (Exception ex) {
			logf.error(ex.getMessage() , ex);
			STATUS status = new STATUS();
			status.setCODE(-1);
			status.setMESSAGE(ex.getMessage());
			status.setSEVERITY("ERROR");
			response = new RBL();
			response.setSTATUS(status);
		}

		return response;
	}

}
