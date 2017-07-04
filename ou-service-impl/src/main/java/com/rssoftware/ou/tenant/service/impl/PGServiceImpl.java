package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bbps.schema.AgentType;
import org.bbps.schema.AmountType;
import org.bbps.schema.AmtType;
import org.bbps.schema.AnalyticsType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.BillerType;
import org.bbps.schema.CustomerDtlsType;
import org.bbps.schema.CustomerParamsType;
import org.bbps.schema.DeviceTagNameType;
import org.bbps.schema.DeviceType;
import org.bbps.schema.HeadType;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.PymntInfType;
import org.bbps.schema.QckPayType;
import org.bbps.schema.RiskScoresType;
import org.bbps.schema.RiskScoresType.Score;
import org.bbps.schema.SpltPayType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.TxnType;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.to.BillDetails;
import com.rssoftware.ou.cbs.service.factory.CBSServiceFactory;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.PGParamParser;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.Expression;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationField;
import com.rssoftware.ou.database.entity.tenant.PgIntegrationFieldPK;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.CustomUserDetails;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.expression.ExpressionParser;
import com.rssoftware.ou.gateway.BillPaymentGateway;
import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.cbs.CBSResponse;
import com.rssoftware.ou.model.cbs.CurrencyCodes;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.ChannelParametersView;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.service.CBSService;
import com.rssoftware.ou.tenant.dao.ExpressionDao;
import com.rssoftware.ou.tenant.dao.FinTransactionDataDao;
import com.rssoftware.ou.tenant.dao.PgIntegrationFieldDao;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.ChannelParametersService;
import com.rssoftware.ou.tenant.service.FinTransactionDataService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.PGService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

@Service
public class PGServiceImpl implements PGService {
	
	private final static Logger logger = LoggerFactory.getLogger(PGServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();
	
	private static FinTransactionDataDao finTransactionDataDao;
	
	private static TransactionDataService transactionDataService;
		
	private static ParamService paramService;
	
	private static BillerService billerService;
	
	private static AgentService agentService;
	
	private static IDGeneratorService idGeneratorService;
	
	private static PgIntegrationFieldDao pgIntegrationFieldDao;
	
	private static ExpressionDao expressionDao;
	
	private static BillPaymentGateway billPaymentGateway;
	
	private static FinTransactionDataService finTranDataService;
	
	private static ChannelParametersService channelParametersService;
	
	private static ApplicationConfigService applicationConfigService;

	@Autowired
	public PGServiceImpl(FinTransactionDataDao finTransactionDataDao,
			TransactionDataService transactionDataService,
			ParamService paramService, BillerService billerService,
			AgentService agentService, IDGeneratorService idGeneratorService,
			PgIntegrationFieldDao pgIntegrationFieldDao,
			ExpressionDao expressionDao, BillPaymentGateway billPaymentGateway,
			FinTransactionDataService finTranDataService,
			ChannelParametersService channelParametersService,
			ApplicationConfigService applicationConfigService) {
		super(); 
		this.finTransactionDataDao = finTransactionDataDao;
		this.transactionDataService = transactionDataService;
		this.paramService = paramService;
		this.billerService = billerService;
		this.agentService = agentService;
		this.idGeneratorService = idGeneratorService;
		this.pgIntegrationFieldDao = pgIntegrationFieldDao;
		this.expressionDao = expressionDao;
		this.billPaymentGateway = billPaymentGateway;
		this.finTranDataService = finTranDataService;
		this.channelParametersService = channelParametersService;
		this.applicationConfigService = applicationConfigService;
	}


	@Override
	public BillPaymentResponse receivePGResponse(List<PGParam> paramList, TenantDetail td,String ipaddress) throws ValidationException, IOException {
		PGParamParser object = new PGParamParser(paramList);
		String successExpression = getExpression(CommonConstants.EXPRESSION_IS_PAYMENT_SUCCESS);
		String paymentIntrumentExpression = getExpression(CommonConstants.EXPRESSION_GET_PAYMENT_INSTRUMENT);
		String authCodeExpression = getExpression(CommonConstants.EXPRESSION_GET_AUTH_CODE);
		String txnRefIdExpression = getExpression(CommonConstants.EXPRESSION_GET_TRAN_REF_ID);
		String amountExpression = getExpression(CommonConstants.EXPRESSION_GET_TRAN_AMT);
		String orderIdExpression = getExpression(CommonConstants.EXPRESSION_GET_ORDER_ID);
		String ccfExpression = getExpression(CommonConstants.EXPRESSION_GET_CCF);

		boolean isSuccess = (boolean) ExpressionParser.executeExpression("o", object, successExpression); 
		String paymentInstrument = (String)ExpressionParser.executeExpression("o", object, paymentIntrumentExpression);
		String authCode = (String)ExpressionParser.executeExpression("o", object, authCodeExpression);
		String txnRefId = (String)ExpressionParser.executeExpression("o", object, txnRefIdExpression);
		String amount = (String)ExpressionParser.executeExpression("o", object, amountExpression);
		String orderId = (String)ExpressionParser.executeExpression("o", object, orderIdExpression);
		String ccf = (String)ExpressionParser.executeExpression("o", object, ccfExpression);
	
		String	isquickPay="false";
			
		TransactionDataView transactionDataView= transactionDataService.getTransactionData(orderId,RequestType.PAYMENT);
		if(transactionDataView!=null){
			if(transactionDataView.getBillPaymentRequest().getPaymentMethod().getQuickPay().compareTo(QckPayType.YES)==0){
				 isquickPay="true";
			 }
		}
	
		FinTransactionData finData=finTranDataService.get(txnRefId);
		
		if (!RequestStatus.SENT.name().equals(finData.getCurrentStatus())){
			throw new IOException("fin response incorrect state");
		}
		
		if (isSuccess){
			finData.setCurrentStatus(RequestStatus.RESPONSE_SUCCESS.name());
		}
		else {
			finData.setCurrentStatus(RequestStatus.RESPONSE_DECLINE.name());
		}
		
		finData.setAuthCode(authCode);
		try {
			finData.setResponseJson(mapper.writeValueAsBytes(paramList));
		} catch (IOException e) {
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}
		
		finData.setUpdtTs(new Timestamp(System.currentTimeMillis()));
		
		finTranDataService.createOrUpdate(finData);
		
		BillPaymentRequest bpr = createBillPaymentRequest(td, finData, paymentInstrument,isquickPay,ipaddress,amount,ccf);
		bpr.getAmount().getAmt().setCustConvFee((new BigDecimal(ccf)).multiply(new BigDecimal("100")).setScale(0,RoundingMode.HALF_UP).toPlainString());

		//bpr.getAmount().getAmt().setCustConvFee((Double.toString(Double.parseDouble(ccf)*100)));
	
		BillPaymentResponse response;
		try {
			
			if("true".equalsIgnoreCase(isquickPay)) {
				
				response=billPaymentGateway.processOUBillQuickPayPaymentRequest(bpr);
				
			}else {
				response = billPaymentGateway.processOUBillPaymentRequest(bpr);
				
			}
			
			
			
			
	
		} catch (ValidationException e) {
			CustomUserDetails user = CommonUtils.getLoggedInUserDetails();
			AgentView agent = agentService.getAgentById(user.getRefId());
			CBSRequest reqRev = new CBSRequest();
			reqRev.setRequestId(object.get("reqId"));
			reqRev.setAccNo(agent.getAgentBankAccount());
			reqRev.setMobileNo(agent.getAgentMobileNo());
			reqRev.setTxnAmount(Double.parseDouble(amount));
			reqRev.setCurrencyCode(CurrencyCodes.valueOf("INR"));
			reqRev.setCustomerId(agent.getAgentId());
			reqRev.setCreditAccNo(paramService.retrieveStringParamByName("CBS_ACCOUNT"));
			CBSService cbsService = CBSServiceFactory.getService(paramService.retrieveStringParamByName("CBS_CODE"));
			CBSResponse cbsResp = cbsService.doDebitReversal(reqRev);
			e.setRefId(finData.getRefId());
			e.setTxnRefId(finData.getTxnRefId());
			System.out.println(cbsResp.toString());
			throw e;
		}
			
		return response;
	}	

	
	@Override
	public List<PGParam> getPGIntegrationPage(String billerId, String refId,
			String paymentOption, TenantDetail td,String quickPayAmount,BillPaymentRequest billPaymentRequest,String totamount,String ccf) throws IOException{
		String txnRefId=null;
		FinTransactionData finData=null;
		String paymentAmount=null;
		//Added for New PG Classes
		List<PGParam>  pgParam = new ArrayList<PGParam>();
		
		if(quickPayAmount==null) {
			 txnRefId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_TXN_REF_ID, td.getOuName());
			  finData = new FinTransactionData();
				finData.setTxnRefId(txnRefId);
				finData.setRefId(refId);
				
				TransactionContext.putVariable(CommonConstants.TXN_REF_ID, txnRefId);
				TransactionContext.putVariable(CommonConstants.REF_ID, refId);
				PGParam param = new PGParam();
				param.setParamName(CommonConstants.TXN_REF_ID);
				param.setParamValue(txnRefId);
				pgParam.add(param);
				
				param = new PGParam();
				param.setParamName(CommonConstants.REF_ID);
				param.setParamValue(refId);
				pgParam.add(param);


				BillFetchResponse bfr = transactionDataService.getTransactionData(refId, RequestType.FETCH).getBillFetchResponse();
				
				Map<String, BigDecimal> incomingPaymentAmounts = new HashMap<String, BigDecimal>();
				for (BillerResponseType.Tag tag:bfr.getBillerResponse().getTags()){
					if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())){
						BigDecimal tagAmount = (new BigDecimal(tag.getValue())).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
						incomingPaymentAmounts.put(tag.getName(), tagAmount);
					}
				}
				incomingPaymentAmounts.put(BillerResponseParams.BASE_BILL_AMOUNT, (new BigDecimal(bfr.getBillerResponse().getAmount())).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
				
				String[] paymentOptionTokens = paymentOption.split("\\|");
				BigDecimal summation = BigDecimal.ZERO; 
				for (int i=1;i<paymentOptionTokens.length;i++){
					summation = summation.add(incomingPaymentAmounts.get(paymentOptionTokens[i]));
				}
				
				 paymentAmount = summation.setScale(2, RoundingMode.HALF_UP).toPlainString();
				if (!paymentAmount.equals((new BigDecimal(paymentOptionTokens[0])).setScale(2, RoundingMode.HALF_UP).toPlainString())){
					throw new IOException("amount mismatch");
				}
				
				TransactionContext.putVariable(CommonConstants.PAYMENT_AMOUNT, paymentAmount);
				param = new PGParam();
				param.setParamName(CommonConstants.PAYMENT_AMOUNT);
				//changed for CCF
				//param.setParamValue(paymentAmount);
				param.setParamValue(totamount);
				
				pgParam.add(param);
				param = new PGParam();
				param.setParamName(CommonConstants.QUICK_PAY);
				param.setParamValue("false");
				pgParam.add(param);
				
				param = new PGParam();
				param.setParamName(CommonConstants.CUSTOMER_CONVIENCE_FEE);
				param.setParamValue(ccf);
				pgParam.add(param);	
				
				
			
		}
		else {
			txnRefId=billPaymentRequest.getTxn().getTxnReferenceId();
			 finData = new FinTransactionData();
			finData.setTxnRefId(txnRefId);
			finData.setRefId(refId);
			
			TransactionContext.putVariable(CommonConstants.TXN_REF_ID, txnRefId);
			TransactionContext.putVariable(CommonConstants.REF_ID, refId);
			transactionDataService.insertQuickPay(refId, billPaymentRequest);
			TransactionContext.putVariable(CommonConstants.PAYMENT_AMOUNT, quickPayAmount);
			PGParam param = new PGParam();
			param.setParamName(CommonConstants.TXN_REF_ID);
			param.setParamValue(txnRefId);
			pgParam.add(param);
			
			param = new PGParam();
			param.setParamName(CommonConstants.REF_ID);
			param.setParamValue(refId);
			pgParam.add(param);
			param = new PGParam();
			param.setParamName(CommonConstants.PAYMENT_AMOUNT);
			param.setParamValue(quickPayAmount);
			pgParam.add(param);
			param = new PGParam();
			param.setParamName(CommonConstants.QUICK_PAY);
			param.setParamValue("true");
			pgParam.add(param);	
			param = new PGParam();
			param.setParamName(CommonConstants.CUSTOMER_CONVIENCE_FEE);
			param.setParamValue("0");
			pgParam.add(param);	
			
		}
	
		
		//Invoke PG Specific Class
		String PG_CLASS = paramService.retrieveStringParamByName("PG_NAME");
		String className = "com.rssoftware.ou.pg.impl.PG_" + PG_CLASS.toUpperCase();
		List<PGParam> pgFields = null;
		
		Class<?> c;
		try {
			c = Class.forName(className);
			Object t = c.newInstance();
			Method m = c.getMethod("getPGFields", List.class);
			Object ret = m.invoke(t, pgParam); 
			if (ret != null){
				pgFields = (List<PGParam>) ret;
			}	
			finData.setSelectedPaymentOptions(paymentOption);
			
			// Block end
			finData.setRequestJson(mapper.writeValueAsBytes(pgFields));
			finData.setCurrentStatus(RequestStatus.SENT.name());
			finData.setCrtnTs(new Timestamp(System.currentTimeMillis()));
			finTransactionDataDao.createOrUpdate(finData);
			
		} 
		catch  (Exception e1) {
			logger.error( e1.getMessage(), e1);
	        logger.info("In Excp : " + e1.getMessage());
		}
		
		return pgFields;
	}
	
	private String substituteParamValues(String input) {
		if (input == null) {
			return null;
		}

		for (String param : CommonUtils.getParamsFromString(input)) {
			String paramName = CommonUtils.extractParamName(param);

			String paramValue = CommonUtils
					.extractResolvedParamValue(paramService.retrieveStringParamByName(paramName));
			input = StringUtils.replace(input, param, paramValue != null ? paramValue : "");
		}
		return CommonUtils.extractResolvedParamValue(input);
	}

	@Override
	public void savePGIntegrationField(String fieldName,
			PGIntegrationFieldType type, String value) {
		PgIntegrationField p = new PgIntegrationField();
		p.setId(new PgIntegrationFieldPK());
		p.getId().setFieldName(fieldName);
		p.getId().setFieldType(type.name());
		p.setFieldValue(value);
		pgIntegrationFieldDao.createOrUpdate(p);
		
	}

	@Override
	public String fetchPGIntegrationField(String fieldName,
			PGIntegrationFieldType type) {
		PgIntegrationFieldPK pk = new PgIntegrationFieldPK();
		pk.setFieldName(fieldName);
		pk.setFieldType(type.name());
		
		PgIntegrationField p = pgIntegrationFieldDao.get(pk);
		if (p != null){
			return p.getFieldValue();
		}
		return null;
	}

	@Override
	public void saveExpression(String name, String value) {
		Expression e = new Expression();
		e.setExpressionName(name);
		e.setExpressionValue(value);
		expressionDao.createOrUpdate(e);
	}

	@Override
	public String getExpression(String name) {
		Expression e = expressionDao.get(name);
		if (e != null){
			return e.getExpressionValue();
		}
		return null;
	}
	
	
	public static BillPaymentRequest createBillPaymentRequest(TenantDetail td, FinTransactionData finData, String paymentInstrument,String isquickPay,String ipaddress,String quickPayAmount,String ccf) throws IOException{
		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
		TransactionDataView tdv=null;
		
		ChannelParametersView channelParmeterView=channelParametersService.getChannelParametersById(finData.getRefId());
		/*String paymentChannel=channelParmeterView.getBiller_Payment_channel();
		String paymentMode=  channelParmeterView.getBiller_Payment_Mode();*/
		String paymentChannel="INT";
		String paymentMode=  "Internet Banking";
		
		String paymentinfo= applicationConfigService.getPaymentInfoMap().get(paymentMode);
		Set<String> paymenttag = applicationConfigService.getAgentDevMap().get(paymentChannel);
		
		String payinfo []=  paymentinfo.split("\\|");
		StringBuilder sb = new StringBuilder("");
		for(String pyinfo:payinfo){
			if(pyinfo.equalsIgnoreCase("IFSC")){
				sb.append(paramService.retrieveStringParamByName("DEFAULT_IFSC"));
				sb.append("|");
			}
			if(pyinfo.equalsIgnoreCase("AccountNo")){
				sb.append(channelParmeterView.getAccountNo());
				sb.append("|");
			}
			if(pyinfo.equalsIgnoreCase("CardNum")){
				sb.append(channelParmeterView.getIfsc());
				sb.append("|");
			}
			if(pyinfo.equalsIgnoreCase("AuthCode")){
				sb.append(channelParmeterView.getIfsc());
				sb.append("|");
			}
			
			if(pyinfo.equalsIgnoreCase("MobileNo")){
				sb.append(channelParmeterView.getIfsc());
				sb.append("|");
			}
			
			if(pyinfo.equalsIgnoreCase("WalletName")){
				sb.append(channelParmeterView.getIfsc());
				sb.append("|");
			}
			if(pyinfo.equalsIgnoreCase("MMID")){
				sb.append(channelParmeterView.getIfsc());
				sb.append("|");
			}
			if(pyinfo.equalsIgnoreCase("Remarks")){
				sb.append(channelParmeterView.getIfsc());
				sb.append("|");
			}
			
		}
		String paymentInfo =sb.substring(0,sb.length()-1);
	
		
		
	
		PmtMtdType pmtMethod = new PmtMtdType();		
		pmtMethod.setSplitPay(SpltPayType.NO);
		pmtMethod.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		
		
		AmountType amount=new AmountType();
		//for agent
		if("true".equalsIgnoreCase(isquickPay)) {
			TransactionDataView	trandv = transactionDataService.getTransactionData(finData.getRefId(), RequestType.PAYMENT);
			/*billPaymentRequest.setHead(trandv.getBillPaymentRequest().getHead());
			billPaymentRequest.setAnalytics(trandv.getBillPaymentRequest().getAnalytics());
			
			billPaymentRequest.setTxn(trandv.getBillPaymentRequest().getTxn());*/
			HeadType head = new HeadType();
			head.setVer("1.0");
			head.setTs(CommonUtils.getFormattedCurrentTimestamp());
			head.setOrigInst(td.getOuName());
			head.setRefId(finData.getRefId());
			billPaymentRequest.setHead(head);
			

			AnalyticsType analytics = new AnalyticsType();
			AnalyticsType.Tag paymentRequestStartTag = new AnalyticsType.Tag();
			paymentRequestStartTag.setName("PAYREQUESTSTART");
			paymentRequestStartTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
			analytics.getTags().add(paymentRequestStartTag);

			AnalyticsType.Tag paymentRequestEndTag = new AnalyticsType.Tag();
			paymentRequestEndTag.setName("PAYREQUESTEND");
			paymentRequestEndTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
			analytics.getTags().add(paymentRequestEndTag);
			billPaymentRequest.setAnalytics(analytics);
			
			TxnType txn = new TxnType();
			txn.setTs(CommonUtils.getFormattedCurrentTimestamp());
			txn.setMsgId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, td.getOuName()));
			RiskScoresType riskScore = new RiskScoresType();
			Score score = new Score();
			score.setProvider(td.getOuName());
			score.setType("TXNRISK");
			score.setValue("000"); // populate a default score now
			riskScore.getScores().add(score);
			txn.setRiskScores(riskScore);
			txn.setTxnReferenceId(finData.getTxnRefId());
			txn.setType(TransactionType.FORWARD_TYPE_REQUEST.value());
			billPaymentRequest.setTxn(txn);
			pmtMethod.setQuickPay(QckPayType.YES);
			billPaymentRequest.setPaymentMethod(pmtMethod);
			AgentType agent = new AgentType();
			//  commented for custom  agentId
			agent.setId(CommonUtils.getLoggedInUserDetails().getRefId());
			DeviceType device = new DeviceType();
			/*for(String paymnttag:paymenttag){
				DeviceType.Tag initChannelTag = new DeviceType.Tag();
				
				initChannelTag.setName(DeviceTagNameType.valueOf(paymnttag));
				initChannelTag.setValue(convertToValueDeviceTagNameType(channelParmeterView,paymnttag));
				device.getTags().add(initChannelTag);
				
			}*/
			
			DeviceType.Tag initChannelTag = new DeviceType.Tag();
			
			initChannelTag.setName(DeviceTagNameType.valueOf("INITIATING_CHANNEL"));
			initChannelTag.setValue("INT");
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("IP"));
			initChannelTag.setValue("124.170.23.28");
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("MAC"));
			initChannelTag.setValue("01-23-45-67-89-ab");
			device.getTags().add(initChannelTag);
			agent.setDevice(device);
			// my code
			//commented for adding  channel
			//billPaymentRequest.setAgent(tdv.getBillFetchRequest().getAgent());
			billPaymentRequest.setAgent(agent);
			
			//billPaymentRequest.setAgent(trandv.getBillPaymentRequest().getAgent());
			//to be billPaymentRequest.setBillerResponse(trandv.getBillFetchResponse().getBillerResponse());
			//to be billPaymentRequest.setAdditionalInfo(trandv.getBillFetchResponse().getAdditionalInfo());
			billPaymentRequest.setCustomer(trandv.getBillPaymentRequest().getCustomer());		
			billPaymentRequest.setBillDetails(trandv.getBillPaymentRequest().getBillDetails());
			
//String[] amtBreakup = finData.getSelectedPaymentOptions().split("\\|");
			
			amount.setAmt(new AmtType());
			amount.getAmt().setAmount((new BigDecimal(quickPayAmount)).multiply(new BigDecimal("100")).setScale(0,RoundingMode.HALF_UP).toPlainString());
			amount.getAmt().setCurrency(CommonConstants.INR_CURENCY);
			//amount.getAmt().setCustConvFee(trandv.getBillFetchResponse().getBillerResponse().getCustConvFee());
			
			/*if (amtBreakup.length > 1){
				Map<String, String> incomingPaymentAmounts = new HashMap<>();
				for (BillerResponseType.Tag tag:trandv.getBillFetchResponse().getBillerResponse().getTags()){
					if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())){
						incomingPaymentAmounts.put(tag.getName(), tag.getValue());
					}
				}
				
				for (int i=1;i<amtBreakup.length;i++){
					String amountComponent = amtBreakup[i];
					if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountComponent)){
						continue;
					}
					if (incomingPaymentAmounts.get(amountComponent) != null){
						AmountType.Tag tag = new AmountType.Tag();
						tag.setName(amountComponent);
						tag.setValue(incomingPaymentAmounts.get(amountComponent));
						amount.getTags().add(tag);
					}
				}
			}
			*/

		billPaymentRequest.setAmount(amount);
		
		PymntInfType pymntInfo = new PymntInfType();
		PymntInfType.Tag ptag = new PymntInfType.Tag();
		//ptag.setName(CommonConstants.PAYMENT_INFO_IFSC_AC);
		ptag.setName(paymentinfo);
		ptag.setValue(paymentInfo);
		pymntInfo.getTags().add(ptag);
		
		
		billPaymentRequest.setPaymentInformation(pymntInfo);
		
		}
		else {
		
			HeadType head = new HeadType();
			head.setVer("1.0");
			head.setTs(CommonUtils.getFormattedCurrentTimestamp());
			head.setOrigInst(td.getOuName());
			head.setRefId(finData.getRefId());
			billPaymentRequest.setHead(head);
			

			AnalyticsType analytics = new AnalyticsType();
			AnalyticsType.Tag paymentRequestStartTag = new AnalyticsType.Tag();
			paymentRequestStartTag.setName("PAYREQUESTSTART");
			paymentRequestStartTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
			analytics.getTags().add(paymentRequestStartTag);

			AnalyticsType.Tag paymentRequestEndTag = new AnalyticsType.Tag();
			paymentRequestEndTag.setName("PAYREQUESTEND");
			paymentRequestEndTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
			analytics.getTags().add(paymentRequestEndTag);
			billPaymentRequest.setAnalytics(analytics);
			
			TxnType txn = new TxnType();
			txn.setTs(CommonUtils.getFormattedCurrentTimestamp());
			txn.setMsgId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, td.getOuName()));
			RiskScoresType riskScore = new RiskScoresType();
			Score score = new Score();
			score.setProvider(td.getOuName());
			score.setType("TXNRISK");
			score.setValue("000"); // populate a default score now
			riskScore.getScores().add(score);
			txn.setRiskScores(riskScore);
			txn.setTxnReferenceId(finData.getTxnRefId());
			txn.setType(TransactionType.FORWARD_TYPE_REQUEST.value());
			billPaymentRequest.setTxn(txn);
			tdv = transactionDataService.getTransactionData(finData.getRefId(), RequestType.FETCH);
			pmtMethod.setQuickPay(QckPayType.NO);
			billPaymentRequest.setPaymentMethod(pmtMethod);
			// have to create new agent for channel
			
			AgentType agent = new AgentType();
			//  commented for custom  agentId
			agent.setId(CommonUtils.getLoggedInUserDetails().getRefId());
			DeviceType device = new DeviceType();
			/*for(String paymnttag:paymenttag){
				DeviceType.Tag initChannelTag = new DeviceType.Tag();
				
				initChannelTag.setName(DeviceTagNameType.valueOf(paymnttag));
				initChannelTag.setValue(convertToValueDeviceTagNameType(channelParmeterView,paymnttag));
				device.getTags().add(initChannelTag);
				
			}*/
			
			DeviceType.Tag initChannelTag = new DeviceType.Tag();
			
			initChannelTag.setName(DeviceTagNameType.valueOf("INITIATING_CHANNEL"));
			initChannelTag.setValue("INT");
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("IP"));
			initChannelTag.setValue("124.170.23.28");
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("MAC"));
			initChannelTag.setValue("01-23-45-67-89-ab");
			device.getTags().add(initChannelTag);
			agent.setDevice(device);
			// my code
			//commented for adding  channel
			//billPaymentRequest.setAgent(tdv.getBillFetchRequest().getAgent());
			billPaymentRequest.setAgent(agent);
			
			billPaymentRequest.setBillerResponse(tdv.getBillFetchResponse().getBillerResponse());
			billPaymentRequest.setAdditionalInfo(tdv.getBillFetchResponse().getAdditionalInfo());
			billPaymentRequest.setCustomer(tdv.getBillFetchRequest().getCustomer());		
			billPaymentRequest.setBillDetails(tdv.getBillFetchRequest().getBillDetails());
			
			String[] amtBreakup = finData.getSelectedPaymentOptions().split("\\|");
			
			amount.setAmt(new AmtType());
			amount.getAmt().setAmount((new BigDecimal(amtBreakup[0])).multiply(new BigDecimal("100")).setScale(0,RoundingMode.HALF_UP).toPlainString());
			amount.getAmt().setCurrency(CommonConstants.INR_CURENCY);
			amount.getAmt().setCustConvFee(tdv.getBillFetchResponse().getBillerResponse().getCustConvFee());
			
			if (amtBreakup.length > 1){
				Map<String, String> incomingPaymentAmounts = new HashMap<>();
				for (BillerResponseType.Tag tag:tdv.getBillFetchResponse().getBillerResponse().getTags()){
					if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())){
						incomingPaymentAmounts.put(tag.getName(), tag.getValue());
					}
				}
				
				for (int i=1;i<amtBreakup.length;i++){
					String amountComponent = amtBreakup[i];
					if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountComponent)){
						continue;
					}
					if (incomingPaymentAmounts.get(amountComponent) != null){
						AmountType.Tag tag = new AmountType.Tag();
						tag.setName(amountComponent);
						tag.setValue(incomingPaymentAmounts.get(amountComponent));
						amount.getTags().add(tag);
					}
				}
			}
		}	

		billPaymentRequest.setAmount(amount);
		
		PymntInfType pymntInfo = new PymntInfType();
		PymntInfType.Tag ptag = new PymntInfType.Tag();
		//ptag.setName(CommonConstants.PAYMENT_INFO_IFSC_AC);
		ptag.setName(paymentinfo);
		ptag.setValue(paymentInfo);
		pymntInfo.getTags().add(ptag);
		
		
		billPaymentRequest.setPaymentInformation(pymntInfo);
	/*String billdate=tdv.getBillFetchResponse().getBillerResponse().getBillDate();
	String customer=tdv.getBillFetchResponse().getBillerResponse().getCustomerName();
	String billnum=tdv.getBillFetchResponse().getBillerResponse().getBillNumber();
	String billperiod=tdv.getBillFetchResponse().getBillerResponse().getBillPeriod();
	String agentId=tdv.getBillFetchRequest().getAgent().getId();
	String billerId=tdv.getBillFetchResponse().getBillDetails().getBiller().getId();*/
	
	//

		
		return billPaymentRequest;
	}
	/*private DeviceTagNameType convertToDeviceTagNameType(String tag){
		DeviceTagNameType deviceTagNameType = null;
		if(tag.equalsIgnoreCase(DeviceTagNameType.INITIATING_CHANNEL.toString())){
			
		 deviceTagNameType =  DeviceTagNameType.INITIATING_CHANNEL;
			
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.MOBILE.toString())){
			 deviceTagNameType =  DeviceTagNameType.MOBILE;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.GEOCODE.toString())){
			 deviceTagNameType =  DeviceTagNameType.GEOCODE;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.POSTAL_CODE.toString())){
			 deviceTagNameType =  DeviceTagNameType.POSTAL_CODE;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.IP.toString())){
			 deviceTagNameType =  DeviceTagNameType.IP;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.TERMINAL_ID.toString())){
			 deviceTagNameType =  DeviceTagNameType.TERMINAL_ID;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.IMEI.toString())){
			 deviceTagNameType =  DeviceTagNameType.IMEI;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.IFSC.toString())){
			 deviceTagNameType =  DeviceTagNameType.IFSC;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.MAC.toString())){
			 deviceTagNameType =  DeviceTagNameType.MAC;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.OS.toString())){
			 deviceTagNameType =  DeviceTagNameType.OS;
		}
		if(tag.equalsIgnoreCase(DeviceTagNameType.APP.toString())){
			 deviceTagNameType =  DeviceTagNameType.APP;
		}
		return deviceTagNameType;
		
	
	}*/
	
	String convertToValueDeviceTagNameType(ChannelParametersView channelParmeterView,String paymnttag){
		String value =null;
		if(paymnttag.equalsIgnoreCase("MOBILE")){
			value=channelParmeterView.getMobile();
			
		}
		if(paymnttag.equalsIgnoreCase("GEOCODE")){
			value=channelParmeterView.getGeoCode();
			
		}
		if(paymnttag.equalsIgnoreCase("POSTAL_CODE")){
			value=channelParmeterView.getPostal_code();
			
		}
		if(paymnttag.equalsIgnoreCase("IP")){
			value=channelParmeterView.getIp();
			
		}
		if(paymnttag.equalsIgnoreCase("TERMINAL_ID")){
			value=channelParmeterView.getTerminal_id();
			
		}if(paymnttag.equalsIgnoreCase("IMEI")){
			value=channelParmeterView.getImei();
			
		}if(paymnttag.equalsIgnoreCase("IFSC")){
			value=channelParmeterView.getMobile();
			
		}if(paymnttag.equalsIgnoreCase("MAC")){
			value=channelParmeterView.getMobile();
			
		}if(paymnttag.equalsIgnoreCase("OS")){
			value=channelParmeterView.getMobile();
			
		}if(paymnttag.equalsIgnoreCase("APP")){
			value=channelParmeterView.getMobile();
			
		}
		
		return value;
	}
	
	public static BillPaymentRequest createQuickPayBillPaymentRequest(MultiValueMap<String, Object> params, TenantDetail td,String refId,String txnAmount,String billerId) throws IOException{
		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
		TransactionDataView tdv=null;
		
		
		HeadType head = new HeadType();
		head.setVer("1.0");
		head.setTs(CommonUtils.getFormattedCurrentTimestamp());
		head.setOrigInst(td.getOuName());
		head.setRefId(refId);
		billPaymentRequest.setHead(head);

		AnalyticsType analytics = new AnalyticsType();
		AnalyticsType.Tag paymentRequestStartTag = new AnalyticsType.Tag();
		paymentRequestStartTag.setName("PAYREQUESTSTART");
		paymentRequestStartTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(paymentRequestStartTag);

		AnalyticsType.Tag paymentRequestEndTag = new AnalyticsType.Tag();
		paymentRequestEndTag.setName("PAYREQUESTEND");
		paymentRequestEndTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(paymentRequestEndTag);
		billPaymentRequest.setAnalytics(analytics);

		org.bbps.schema.TxnType txn = new org.bbps.schema.TxnType();
		txn.setTs(CommonUtils.getFormattedCurrentTimestamp());
		txn.setMsgId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, td.getOuName()));
		RiskScoresType riskScore = new RiskScoresType();
		Score score = new Score();
		score.setProvider(td.getOuName());
		score.setType("TXNRISK");
		score.setValue("000"); // populate a default score now
		riskScore.getScores().add(score);
		txn.setRiskScores(riskScore);
		txn.setTxnReferenceId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_TXN_REF_ID, td.getOuName()));
		txn.setType(TransactionType.FORWARD_TYPE_REQUEST.value());
		billPaymentRequest.setTxn(txn);
		
		//for agent
		
			AgentType agent = new AgentType();
			agent.setId(paramService.retrieveStringParamByName(CommonConstants.DEFAULT_AGENT));
			DeviceType device = new DeviceType();
			DeviceType.Tag initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.INITIATING_CHANNEL);
			initChannelTag.setValue(PaymentChannel.Internet_Banking.getExpandedForm());// constant
			device.getTags().add(initChannelTag);

			DeviceType.Tag ipTag = new DeviceType.Tag();
			ipTag.setName(DeviceTagNameType.IP);
			ipTag.setValue(CommonUtils.getIP());// get IP from request
			device.getTags().add(ipTag);

			String mac = CommonUtils.getMacAddress();
			if (mac == null || "".equals(mac.trim())){
				mac = paramService.retrieveStringParamByName(CommonConstants.DEFAULT_MAC_ADDRESS);
			}
			DeviceType.Tag macTag = new DeviceType.Tag();
			macTag.setName(DeviceTagNameType.MAC);
			macTag.setValue(mac);// get IP from request
			device.getTags().add(macTag);
			
			agent.setDevice(device);
			billPaymentRequest.setAgent(agent);
			
			CustomerDtlsType customer = new CustomerDtlsType();
			customer.setMobile(params.get("mobileNum").get(0).toString());//form

			if (params.get("emailId") != null && !params.get("emailId").isEmpty() && params.get("emailId").get(0) != null
					&& CommonUtils.hasValue(params.get("emailId").get(0).toString())){
				CustomerDtlsType.Tag emailTag = new CustomerDtlsType.Tag();
				emailTag.setName("EMAIL");
				emailTag.setValue(params.get("emailId").get(0).toString());// form
				customer.getTags().add(emailTag);
			}

			if (params.get("pan") != null && !params.get("pan").isEmpty() && params.get("pan").get(0) != null
					&& CommonUtils.hasValue(params.get("pan").get(0).toString())){
				CustomerDtlsType.Tag panTag = new CustomerDtlsType.Tag();
				panTag.setName("PAN");
				panTag.setValue(params.get("pan").get(0).toString());// form
				customer.getTags().add(panTag);
			}

			if (params.get("aadhaarNum") != null && !params.get("aadhaarNum").isEmpty() && params.get("aadhaarNum").get(0) != null
					&& CommonUtils.hasValue(params.get("aadhaarNum").get(0).toString())){
				CustomerDtlsType.Tag aadhaarTag = new CustomerDtlsType.Tag();
				aadhaarTag.setName("AADHAAR");
				aadhaarTag.setValue(params.get("aadhaarNum").get(0).toString());// form
				customer.getTags().add(aadhaarTag);
			}
		
			
			billPaymentRequest.setCustomer(customer);	
			
			BillDetailsType billDetails = new BillDetailsType();
			CustomerParamsType customerParams = new CustomerParamsType();

			// loop through customer params
			BillerView bv = billerService.getBillerById(billerId);
			if (bv.getBillerCustomerParams() != null){
				for (ParamConfig pc:bv.getBillerCustomerParams()){
					String paramName = pc.getParamName();
					
					if (params.get(paramName) != null && !params.get(paramName).isEmpty() && params.get(paramName).get(0) != null
							&& CommonUtils.hasValue(params.get(paramName).get(0).toString())){
						CustomerParamsType.Tag customerParamTag = new CustomerParamsType.Tag();
						customerParamTag.setName(pc.getParamName());// form
						customerParamTag.setValue(params.get(paramName).get(0).toString().trim());// form
						customerParams.getTags().add(customerParamTag);
					}
				}
			} 
			
			billDetails.setCustomerParams(customerParams);
			
			BillerType biller = new BillerType();
			biller.setId(billerId);
			billDetails.setBiller(biller);
			billPaymentRequest.setBillDetails(billDetails);
	

		PmtMtdType pmtMethod = new PmtMtdType();
		
			pmtMethod.setQuickPay(QckPayType.YES);
		
	
		pmtMethod.setSplitPay(SpltPayType.NO);
		pmtMethod.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		billPaymentRequest.setPaymentMethod(pmtMethod);
		
		AmountType amount=null;
		
		if(txnAmount!=null) {
			amount= new AmountType();
			AmountType.Tag tag = new AmountType.Tag();
			tag.setName("QUICKPAY");
			tag.setValue(txnAmount);
			amount.getTags().add(tag);
			
		} 
		billPaymentRequest.setAmount(amount);
		
		
		PymntInfType pymntInfo = new PymntInfType();
		PymntInfType.Tag ptag = new PymntInfType.Tag();
		ptag.setName(CommonConstants.PAYMENT_INFO_IFSC_AC);
		ptag.setValue("");
		pymntInfo.getTags().add(ptag);
		
		billPaymentRequest.setPaymentInformation(pymntInfo);
	/*String billdate=tdv.getBillFetchResponse().getBillerResponse().getBillDate();
	String customer=tdv.getBillFetchResponse().getBillerResponse().getCustomerName();
	String billnum=tdv.getBillFetchResponse().getBillerResponse().getBillNumber();
	String billperiod=tdv.getBillFetchResponse().getBillerResponse().getBillPeriod();
	String agentId=tdv.getBillFetchRequest().getAgent().getId();
	String billerId=tdv.getBillFetchResponse().getBillDetails().getBiller().getId();*/
	
	//

		
		return billPaymentRequest;
	}


	@Override
	public BillPaymentResponse getBillPaymentResponse(String billerId,
			String refId, String paymentOption, TenantDetail td,
			String quickPayAmount, BillPaymentRequest billPaymentRequest,
			String totamount, String ccf, String ipaddress,
			String selectedPaymentMode) throws ValidationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<PGParam> getPGIntegrationFields(String bankId, PGIntegrationFieldType type) {
		List<PGParam> pgFields = new ArrayList<PGParam>();
		List<PgIntegrationField> allFields = pgIntegrationFieldDao.getAll();
		
		if (allFields != null){
			for (PgIntegrationField field:allFields){
				if (type.name().equals(field.getId().getFieldType())){
					PGParam param = new PGParam();
					param.setParamName(field.getId().getFieldName());
					param.setParamValue(substituteParamValues(field.getFieldValue()));
					pgFields.add(param);
				}
			}
		}
		return pgFields;
	}
	
	@Override
	public BillPaymentResponse processPaymentRequest(BillPaymentRequest billPaymentRequest) {
		BillPaymentResponse response = null;
		try {
			response = billPaymentGateway.processOUBillPaymentRequest(billPaymentRequest);
		} catch (ValidationException e) {
			logger.error(e.getMessage());
		}
		return response;
	}
	
}
