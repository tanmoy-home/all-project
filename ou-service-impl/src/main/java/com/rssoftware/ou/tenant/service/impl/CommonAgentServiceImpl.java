package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.AgentType;
import org.bbps.schema.AmountType;
import org.bbps.schema.AmtType;
import org.bbps.schema.AnalyticsType;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.DeviceTagNameType;
import org.bbps.schema.DeviceType;
import org.bbps.schema.HeadType;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.PymntInfType;
import org.bbps.schema.RiskScoresType;
import org.bbps.schema.RiskScoresType.Score;
import org.bbps.schema.SpltPayType;
import org.bbps.schema.TransactionType;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.businessprocessor.AgentBusinessProcessor;
import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.cbs.service.factory.CBSServiceFactory;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.PGParamParser;
import com.rssoftware.ou.common.RequestStatus;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.exception.ValidationException.ValidationErrorReason;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.Expression;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.CustomUserDetails;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.expression.ExpressionParser;
import com.rssoftware.ou.model.cbs.CBSRequest;
import com.rssoftware.ou.model.cbs.CBSResponse;
import com.rssoftware.ou.model.cbs.CurrencyCodes;
import com.rssoftware.ou.model.cbs.TxnStatus;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.service.CBSService;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.dao.ExpressionDao;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.ApplicationConfigService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CCFCalculatorService;
import com.rssoftware.ou.tenant.service.CUService;
import com.rssoftware.ou.tenant.service.ChannelParametersService;
import com.rssoftware.ou.tenant.service.CommonAgentService;
import com.rssoftware.ou.tenant.service.FinTransactionDataService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.PGService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.TransactionDataService;
import com.rssoftware.ou.validator.AgentValidator;

@Service
public class CommonAgentServiceImpl implements CommonAgentService {

	private static ObjectMapper mapper = new ObjectMapper();

	private static BillerService billerService;

	@Autowired
	private FinTransactionDataService finTranDataService;

	@Autowired
	private IDGeneratorService idGenetarorService;

	@Autowired
	private AgentValidator agentValidator;

	private static AgentService agentService;

	@Autowired
	private AgentBusinessProcessor processor;

	private static ParamService paramService;

	@Autowired
	private AsyncProcessor asyncProcessor;

	@Autowired
	private CUService cuService;

	private static TransactionDataService transactionDataService;

	private static TenantDetailService tenantDetailService;
	private static IDGeneratorService idGeneratorService;

	@Autowired
	private CCFCalculatorService ccfCalculatorService;

	@Autowired
	private PGService pgService;

	@Autowired
	private ExpressionDao expressionDao;

	@Autowired
	private ChannelParametersService channelParametersService;

	@Autowired
	private ApplicationConfigService applicationConfigService;

	@Autowired
	public CommonAgentServiceImpl(ParamService paramService, TransactionDataService transactionDataService,
			TenantDetailService tenantDetailService, AgentService agentService, BillerService billerService,
			IDGeneratorService idGeneratorService) {
		super();
		this.transactionDataService = transactionDataService;
		this.tenantDetailService = tenantDetailService;
		this.agentService = agentService;
		this.billerService = billerService;
		this.idGenetarorService = idGeneratorService;
		this.paramService = paramService;
	}

	private static Log logger = LogFactory.getLog(CommonAgentServiceImpl.class);

	@Override
	public List<String> getBillerCategories(List<String> paymentChannels, List<String> paymentModes)
			throws ValidationException, IOException {

		CustomUserDetails userDetails = CommonUtils.getLoggedInUserDetails();
		logger.info("userDetails.getRefId() "+userDetails.getRefId());
		AgentView agent = agentService.getAgentById(userDetails.getRefId());

		
		agentValidator.isValidPayChannelnMode(agent, paymentChannels, paymentModes);

		List<BillerView> activeBillers = billerService.getActiveBillers();
		
		
		Map<String, BillerView> filteredBillers = processor.filterBillerListOnIncomingPaymentChannelAndMode(
				paymentChannels, paymentModes, activeBillers, agent, false);

		logger.info("size of filteredBillers "+filteredBillers.size());
		
		Set<String> categorySet = new TreeSet<String>();
		for (String billerId : filteredBillers.keySet()) {
			categorySet.add(filteredBillers.get(billerId).getBlrCategoryName());
		}
		
		
		
		return new ArrayList<String>(categorySet);
	}

	@Override
	public List<BillerView> getBillersbyCategory(String billerCategory, List<String> paymentChannels,
			List<String> paymentModes) throws ValidationException, IOException {

		CustomUserDetails userDetails = CommonUtils.getLoggedInUserDetails();
		AgentView agent = agentService.getAgentById(userDetails.getRefId());
		agentValidator.isValidPayChannelnMode(agent, paymentChannels, paymentModes);

		List<BillerView> activeBillers = billerService.getActiveBillers();
		logger.info("ACTIVE_BILLER_ COUNT----------" + (activeBillers == null ? "0" : (activeBillers.size() + "")));
		Map<String, BillerView> filteredBillerMap = processor.filterBillerListOnIncomingPaymentChannelAndMode(
				paymentChannels, paymentModes, activeBillers, agent, false);

		logger.info("ACTIVE_BILLER_ COUNT BASED ON PAYMENT CHANNEL AND PAYMENT_MODES----------"
				+ (filteredBillerMap == null ? "0" : (filteredBillerMap.size() + "")));
		Set<BillerView> billers = new TreeSet<>();

		for (String billerId : filteredBillerMap.keySet()) {
			BillerView bv = filteredBillerMap.get(billerId);
			if (CommonUtils.hasValue(billerCategory) && billerCategory.equals(bv.getBlrCategoryName())) {
				billers.add(bv);
			}
		}

		return new ArrayList<BillerView>(billers);
	}

	@Override
	public List<BillerView> getSubBillers(String parentBillerId, List<String> paymentChannels,
			List<String> paymentModes) throws ValidationException, IOException {
		CustomUserDetails userDetails = CommonUtils.getLoggedInUserDetails();
		AgentView agent = agentService.getAgentById(userDetails.getRefId());

		agentValidator.isValidPayChannelnMode(agent, paymentChannels, paymentModes);

		List<BillerView> subBillers = billerService.getSubBillersByParentBiller(parentBillerId);
		Map<String, BillerView> filteredBillerMap = processor.filterBillerListOnIncomingPaymentChannelAndMode(
				paymentChannels, paymentModes, subBillers, agent, true);
		return new ArrayList<>(filteredBillerMap.values());
	}

	@Override
	public List<ParamConfig> getCustomerParamsbyBillerId(String billerId) throws IOException {
		BillerView biller = billerService.getBillerById(billerId);
		if (biller == null) {
			logger.info("BILLER ID NOT FOUND IN METHOD(getCustomerParamsbyBillerId in CommonAgentServiceImpl)-------"
					+ billerId);
		}
		return biller.getBillerCustomerParams();
	}

	/*@Override
	public BillPaymentResponse payBill(BillPaymentRequestExt billPaymentReq) throws ValidationException, IOException {
		if (billPaymentReq.getFinTransactionDetails() != null) {
			finTranDataService.insert(billPaymentReq.getFinTransactionDetails(),
					billPaymentReq.getBillPaymentRequest().getHead().getRefId());
		} else
			if (finTranDataService.get(billPaymentReq.getBillPaymentRequest().getTxn().getTxnReferenceId()) == null) {
			throw ValidationException.getInstance(ValidationErrorReason.NO_FINANCIAL_TRANSACTION_DETAIL_FOUND);	
		}
		transactionDataService.insert(billPaymentReq);
		Ack ack = cuService.postBillPayRequest(billPaymentReq);
		return asyncProcessor.processAgentBillPaymentRequest(billPaymentReq, ack);
	}*/

	@Override
    public BillPaymentResponse payBill(BillPaymentRequestExt billPaymentReq) throws ValidationException, IOException {
           if (billPaymentReq.getFinTransactionDetails() != null) {
        	   logger.info("Insert RefId "+billPaymentReq.getBillPaymentRequest().getHead().getRefId() +" into FinTransactionDetails");
                  finTranDataService.insert(billPaymentReq.getFinTransactionDetails(),
                               billPaymentReq.getBillPaymentRequest().getHead().getRefId());
           } else
                  if (finTranDataService.get(billPaymentReq.getBillPaymentRequest().getTxn().getTxnReferenceId()) == null) {
                  throw ValidationException.getInstance(ValidationErrorReason.NO_FINANCIAL_TRANSACTION_DETAIL_FOUND);   
           }
           transactionDataService.insert(billPaymentReq);
           Ack ack = cuService.postBillPayRequest(billPaymentReq);
           return asyncProcessor.processAgentBillPaymentRequest(billPaymentReq, ack);
    }
	
	@Override
	public BillFetchResponse fetchBill(BillFetchRequestExt billFetchRequestExt)
			throws ValidationException, IOException {

		transactionDataService.insert(billFetchRequestExt);
		Ack ack = cuService.postBillFetchRequest(billFetchRequestExt);
		return asyncProcessor.processAgentBillFetchRequest(billFetchRequestExt, ack);
	}

	@Override
	public List<TransactionDataView> getTransactions(String agentId, String customerId, String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal checkCommisionForAgent(String agentId, String startDate, String endDate) {

		return null;
	}

	@Override
	public double checkCurrentBalance(String agentId) throws ValidationException {
		double balance = 0;
		// CustomUserDetails userDetails = CommonUtils.getLoggedInUserDetails();
		AgentView agent = agentService.getAgentById(agentId);
		CBSRequest req = new CBSRequest();
		req.setRequestId(CommonUtils.generateId());
		req.setAccNo(agent.getAgentBankAccount());
		req.setMobileNo(agent.getAgentMobileNo());
		req.setCustomerId(agent.getAgentId());
		CBSService cbsService = CBSServiceFactory.getService(paramService.retrieveStringParamByName("CBS_CODE"));
		CBSResponse cbsResp = cbsService.doBalanceEnquiry(req);
		if (cbsResp.getErrorCode() != null) {
			throw ValidationException.getInstance(ValidationErrorReason.DECLINE, cbsResp.getErrorMessage());
		}
		if (cbsResp.getAccBalance() != null) {
			balance = cbsResp.getAccBalance();
		}
		return balance;
	}

	@Override
	public BillPaymentResponse updateAgentBalance(List<PGParam> inputParams, BillPaymentRequest billPaymentRequest)
			throws ValidationException {
		BillPaymentResponse bpr = null;
		CBSResponse cbsResp = null;
		List<PGParam> requestParams = null;

		PGParamParser object = new PGParamParser(inputParams);
		try {
			double txnAmount = Double.parseDouble(object.get("totamount"));
			CBSRequest req = formCBSRequest(txnAmount);
			requestParams = populateCUAndFinData(object, req, billPaymentRequest);

			CBSService cbsService = CBSServiceFactory.getService(paramService.retrieveStringParamByName("CBS_CODE"));
			cbsResp = cbsService.doDebit(req);
			// cbsResp.setErrorCode("E01");//CBS FAILURE TESTING

			if (cbsResp.getErrorCode() != null) {
				throw ValidationException.getInstance(ValidationErrorReason.DECLINE, cbsResp.getErrorCode());
			} else {
				if (cbsResp.getStatus().equals(TxnStatus.SUCCESS)) {

					PGParam param = new PGParam();
					param.setParamName(CommonConstants.STATUS);
					param.setParamValue(TxnStatus.SUCCESS.name());
					requestParams.add(param);

					param = new PGParam();
					param.setParamName(CommonConstants.ACCOUNT_NO);
					param.setParamValue(req.getAccNo());
					requestParams.add(param);

					param = new PGParam();
					param.setParamName(CommonConstants.AUTH_CODE);
					param.setParamValue(cbsResp.getCbsRefId());
					requestParams.add(param);

					param = new PGParam();
					param.setParamName(CommonConstants.REQ_ID);
					param.setParamValue(req.getRequestId());
					requestParams.add(param);
					BillPaymentRequest bpreq = updateCUAndFinData(cbsResp, requestParams);
					// bpr = pgService.receivePGResponse(requestParams,
					// tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()),
					// null);
				} else if (cbsResp.getStatus().equals(TxnStatus.FAILURE)) {
					throw ValidationException.getInstance(ValidationErrorReason.DECLINE, cbsResp.getErrorCode());
				}
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return bpr;
	}

	public String getExpression(String name) {
		Expression e = expressionDao.get(name);
		if (e != null) {
			return e.getExpressionValue();
		}
		return null;
	}

	private BillPaymentRequest updateCUAndFinData(CBSResponse cbsResp, List<PGParam> paramList) throws IOException {
		PGParamParser object = new PGParamParser(paramList);
		String successExpression = getExpression(CommonConstants.EXPRESSION_IS_PAYMENT_SUCCESS);
		String paymentIntrumentExpression = getExpression(CommonConstants.EXPRESSION_GET_PAYMENT_INSTRUMENT);
		String authCodeExpression = getExpression(CommonConstants.EXPRESSION_GET_AUTH_CODE);
		String txnRefIdExpression = getExpression(CommonConstants.EXPRESSION_GET_TRAN_REF_ID);
		String amountExpression = getExpression(CommonConstants.EXPRESSION_GET_TRAN_AMT);
		String orderIdExpression = getExpression(CommonConstants.EXPRESSION_GET_ORDER_ID);
		String ccfExpression = getExpression(CommonConstants.EXPRESSION_GET_CCF);

		boolean isSuccess = (boolean) ExpressionParser.executeExpression("o", object, successExpression);
		String paymentInstrument = (String) ExpressionParser.executeExpression("o", object, paymentIntrumentExpression);
		String authCode = (String) ExpressionParser.executeExpression("o", object, authCodeExpression);
		String txnRefId = (String) ExpressionParser.executeExpression("o", object, txnRefIdExpression);
		String amount = (String) ExpressionParser.executeExpression("o", object, amountExpression);
		String orderId = (String) ExpressionParser.executeExpression("o", object, orderIdExpression);
		String ccf = (String) ExpressionParser.executeExpression("o", object, ccfExpression);

		String isquickPay = "false";

		TransactionDataView transactionDataView = transactionDataService.getTransactionData(orderId,
				RequestType.PAYMENT);
		if (transactionDataView != null) {
			if (transactionDataView.getBillPaymentRequest().getPaymentMethod().getQuickPay()
					.compareTo(org.bbps.schema.QckPayType.YES) == 0) {
				isquickPay = "true";
			}
		}

		FinTransactionData finData = finTranDataService.get(txnRefId);

		if (!RequestStatus.SENT.name().equals(finData.getCurrentStatus())) {
			throw new IOException("fin response incorrect state");
		}

		if (isSuccess) {
			finData.setCurrentStatus(RequestStatus.RESPONSE_SUCCESS.name());
		} else {
			finData.setCurrentStatus(RequestStatus.RESPONSE_DECLINE.name());
		}

		finData.setAuthCode(authCode);
		try {
			finData.setResponseJson(mapper.writeValueAsBytes(paramList));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			logger.info("In Excp : " + e.getMessage());
			throw new IOException(e);
		}

		finData.setUpdtTs(new Timestamp(System.currentTimeMillis()));

		finTranDataService.createOrUpdate(finData);
		BillPaymentRequest bpr = createBillPaymentRequest(finData, paymentInstrument, isquickPay, CommonUtils.getIP(),
				amount, ccf);
		bpr.getAmount().getAmt().setCustConvFee((new BigDecimal(ccf)).multiply(new BigDecimal("100"))
				.setScale(0, RoundingMode.HALF_UP).toPlainString());

		return bpr;
	}

	private CBSRequest formCBSRequest(double txnAmount) {
		CustomUserDetails user = CommonUtils.getLoggedInUserDetails();
		AgentView agent = agentService.getAgentById(user.getRefId());
		CBSRequest req = new CBSRequest();
		req.setRequestId(CommonUtils.generateId());
		req.setAccNo(agent.getAgentBankAccount());
		req.setMobileNo(agent.getAgentMobileNo());
		req.setTxnAmount(txnAmount);
		req.setCurrencyCode(CurrencyCodes.valueOf("INR"));
		req.setCustomerId(agent.getAgentId());
		req.setCreditAccNo(paramService.retrieveStringParamByName("CBS_ACCOUNT"));
		return req;
	}

	private List<PGParam> populateCUAndFinData(PGParamParser object, CBSRequest cbsReq,
			BillPaymentRequest billPaymentRequest) throws IOException {

		TenantDetail td = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId());

		String refId = object.get("refId");
		String paymentOption = object.get("billerAmountOptions");
		String txnAmount = object.get("totamount");
		String ccf = object.get("ccf");
		String quickPayAmount = object.get("quickPayAmount");

		String txnRefId = null;
		FinTransactionData finData = new FinTransactionData();

		String paymentAmount = null;
		List<PGParam> pgParam = new ArrayList<PGParam>();
		PGParam param = new PGParam();
		String authCode = Long.toString(System.currentTimeMillis());

		if (quickPayAmount == null) {
			txnRefId = idGenetarorService.getUniqueID(CommonConstants.LENGTH_TXN_REF_ID, td.getOuName());

			BillFetchResponse bfr = transactionDataService.getTransactionData(refId, RequestType.FETCH)
					.getBillFetchResponse();

			Map<String, BigDecimal> incomingPaymentAmounts = new HashMap<String, BigDecimal>();
			for (BillerResponseType.Tag tag : bfr.getBillerResponse().getTags()) {
				if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())) {
					BigDecimal tagAmount = (new BigDecimal(tag.getValue())).divide(new BigDecimal(100)).setScale(2,
							RoundingMode.HALF_UP);
					incomingPaymentAmounts.put(tag.getName(), tagAmount);
				}
			}
			incomingPaymentAmounts.put(BillerResponseParams.BASE_BILL_AMOUNT,
					(new BigDecimal(bfr.getBillerResponse().getAmount())).divide(new BigDecimal(100)).setScale(2,
							RoundingMode.HALF_UP));

			String[] paymentOptionTokens = paymentOption.split("\\|");
			BigDecimal summation = BigDecimal.ZERO;
			for (int i = 1; i < paymentOptionTokens.length; i++) {
				summation = summation.add(incomingPaymentAmounts.get(paymentOptionTokens[i]));
			}

			paymentAmount = summation.setScale(2, RoundingMode.HALF_UP).toPlainString();
			if (!paymentAmount.equals(
					(new BigDecimal(paymentOptionTokens[0])).setScale(2, RoundingMode.HALF_UP).toPlainString())) {
				throw new IOException("amount mismatch");
			}

			pgParam.add(param);
			param = new PGParam();
			param.setParamName(CommonConstants.QUICK_PAY);
			param.setParamValue("false");
			pgParam.add(param);
		} else {
			txnRefId = billPaymentRequest.getTxn().getTxnReferenceId();
			transactionDataService.insertQuickPay(refId, billPaymentRequest);

			param = new PGParam();
			param.setParamName(CommonConstants.QUICK_PAY);
			param.setParamValue("true");
			pgParam.add(param);
		}
		try {
			finData.setTxnRefId(txnRefId);
			finData.setRefId(refId);
			TransactionContext.putVariable(CommonConstants.TXN_REF_ID, txnRefId);
			TransactionContext.putVariable(CommonConstants.REF_ID, refId);
			TransactionContext.putVariable(CommonConstants.PAYMENT_AMOUNT, txnAmount);

			param.setParamName(CommonConstants.TXN_REF_ID);
			param.setParamValue(txnRefId);
			pgParam.add(param);
			param = new PGParam();
			param.setParamName(CommonConstants.REF_ID);
			param.setParamValue(refId);
			pgParam.add(param);
			param = new PGParam();
			param.setParamName(CommonConstants.PAYMENT_AMOUNT);
			param.setParamValue(txnAmount);
			pgParam.add(param);
			param = new PGParam();
			param.setParamName(CommonConstants.CUSTOMER_CONVIENCE_FEE);
			param.setParamValue(ccf);
			pgParam.add(param);

			finData.setRequestJson(mapper.writeValueAsBytes(cbsReq));
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		finData.setSelectedPaymentOptions(paymentOption);
		finData.setCurrentStatus(RequestStatus.SENT.name());
		finData.setCrtnTs(new Timestamp(System.currentTimeMillis()));
		finTranDataService.createOrUpdate(finData);
		return pgParam;
	}

	public BillPaymentRequest createBillPaymentRequest(FinTransactionData finData, String paymentInstrument,
			String isquickPay, String ipaddress, String quickPayAmount, String ccf) throws IOException {
		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
		TransactionDataView tdv = null;
		String billerPaymentMode = "Cash";
		String paymentinfo = applicationConfigService.getPaymentInfoMap().get(billerPaymentMode);
		String paymentInfo = paramService.retrieveStringParamByName("DEFAULT_IFSC");

		PmtMtdType pmtMethod = new PmtMtdType();
		pmtMethod.setSplitPay(SpltPayType.NO);
		pmtMethod.setPaymentMode(PaymentMode.Cash.getExpandedForm());

		AmountType amount = new AmountType();
		if ("true".equalsIgnoreCase(isquickPay)) {
			TransactionDataView trandv = transactionDataService.getTransactionData(finData.getRefId(),
					RequestType.PAYMENT);

			HeadType head = new HeadType();
			head.setVer("1.0");
			head.setTs(CommonUtils.getFormattedCurrentTimestamp());
			head.setOrigInst(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName());
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

			org.bbps.schema.TxnType txn = new org.bbps.schema.TxnType();
			txn.setTs(CommonUtils.getFormattedCurrentTimestamp());
			txn.setMsgId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID,
					tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName()));
			RiskScoresType riskScore = new RiskScoresType();
			Score score = new Score();
			score.setProvider(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName());
			score.setType("TXNRISK");
			score.setValue("000"); // populate a default score now
			riskScore.getScores().add(score);
			txn.setRiskScores(riskScore);
			txn.setTxnReferenceId(finData.getTxnRefId());
			txn.setType(TransactionType.FORWARD_TYPE_REQUEST.value());
			billPaymentRequest.setTxn(txn);
			pmtMethod.setQuickPay(org.bbps.schema.QckPayType.YES);
			billPaymentRequest.setPaymentMethod(pmtMethod);
			AgentType agent = new AgentType();
			agent.setId(CommonUtils.getLoggedInUserDetails().getRefId());
			DeviceType device = new DeviceType();
			DeviceType.Tag initChannelTag = new DeviceType.Tag();

			initChannelTag.setName(DeviceTagNameType.valueOf("INITIATING_CHANNEL"));
			initChannelTag.setValue("BNKBRNCH");
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("IP"));
			initChannelTag.setValue(CommonUtils.getIP());
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("MAC"));
			initChannelTag.setValue(CommonUtils.getMacAddress());
			device.getTags().add(initChannelTag);
			agent.setDevice(device);
			billPaymentRequest.setAgent(agent);
			billPaymentRequest.setCustomer(trandv.getBillPaymentRequest().getCustomer());
			billPaymentRequest.setBillDetails(trandv.getBillPaymentRequest().getBillDetails());

			amount.setAmt(new AmtType());
			amount.getAmt().setAmount((new BigDecimal(quickPayAmount)).multiply(new BigDecimal("100"))
					.setScale(0, RoundingMode.HALF_UP).toPlainString());
			amount.getAmt().setCurrency(CommonConstants.INR_CURENCY);

			billPaymentRequest.setAmount(amount);

			PymntInfType pymntInfo = new PymntInfType();
			PymntInfType.Tag ptag = new PymntInfType.Tag();
			ptag.setName(paymentinfo);
			ptag.setValue(paramService.retrieveStringParamByName("DEFAULT_IFSC"));
			pymntInfo.getTags().add(ptag);

			billPaymentRequest.setPaymentInformation(pymntInfo);

		} else {

			HeadType head = new HeadType();
			head.setVer("1.0");
			head.setTs(CommonUtils.getFormattedCurrentTimestamp());
			head.setOrigInst(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName());
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

			org.bbps.schema.TxnType txn = new org.bbps.schema.TxnType();
			txn.setTs(CommonUtils.getFormattedCurrentTimestamp());
			txn.setMsgId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID,
					tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName()));
			RiskScoresType riskScore = new RiskScoresType();
			Score score = new Score();
			score.setProvider(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName());
			score.setType("TXNRISK");
			score.setValue("000"); // populate a default score now
			riskScore.getScores().add(score);
			txn.setRiskScores(riskScore);
			txn.setTxnReferenceId(finData.getTxnRefId());
			txn.setType(TransactionType.FORWARD_TYPE_REQUEST.value());
			billPaymentRequest.setTxn(txn);
			tdv = transactionDataService.getTransactionData(finData.getRefId(), RequestType.FETCH);
			pmtMethod.setQuickPay(org.bbps.schema.QckPayType.NO);
			billPaymentRequest.setPaymentMethod(pmtMethod);
			// have to create new agent for channel

			AgentType agent = new AgentType();
			// commented for custom agentId
			agent.setId(CommonUtils.getLoggedInUserDetails().getRefId());
			DeviceType device = new DeviceType();
			/*
			 * for(String paymnttag:paymenttag){ DeviceType.Tag initChannelTag =
			 * new DeviceType.Tag();
			 * 
			 * initChannelTag.setName(DeviceTagNameType.valueOf(paymnttag));
			 * initChannelTag.setValue(convertToValueDeviceTagNameType(
			 * channelParmeterView,paymnttag));
			 * device.getTags().add(initChannelTag);
			 * 
			 * }
			 */

			DeviceType.Tag initChannelTag = new DeviceType.Tag();

			initChannelTag.setName(DeviceTagNameType.valueOf("INITIATING_CHANNEL"));
			initChannelTag.setValue("BNKBRNCH");
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("IP"));
			initChannelTag.setValue(CommonUtils.getIP());
			device.getTags().add(initChannelTag);
			initChannelTag = new DeviceType.Tag();
			initChannelTag.setName(DeviceTagNameType.valueOf("MAC"));
			initChannelTag.setValue(CommonUtils.getMacAddress());
			device.getTags().add(initChannelTag);
			agent.setDevice(device);
			billPaymentRequest.setAgent(agent);

			billPaymentRequest.setBillerResponse(tdv.getBillFetchResponse().getBillerResponse());
			billPaymentRequest.setAdditionalInfo(tdv.getBillFetchResponse().getAdditionalInfo());
			billPaymentRequest.setCustomer(tdv.getBillFetchRequest().getCustomer());
			billPaymentRequest.setBillDetails(tdv.getBillFetchRequest().getBillDetails());

			String[] amtBreakup = finData.getSelectedPaymentOptions().split("\\|");

			amount.setAmt(new AmtType());
			amount.getAmt().setAmount((new BigDecimal(amtBreakup[0])).multiply(new BigDecimal("100"))
					.setScale(0, RoundingMode.HALF_UP).toPlainString());
			amount.getAmt().setCurrency(CommonConstants.INR_CURENCY);
			amount.getAmt().setCustConvFee(tdv.getBillFetchResponse().getBillerResponse().getCustConvFee());

			if (amtBreakup.length > 1) {
				Map<String, String> incomingPaymentAmounts = new HashMap<>();
				for (BillerResponseType.Tag tag : tdv.getBillFetchResponse().getBillerResponse().getTags()) {
					if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())) {
						incomingPaymentAmounts.put(tag.getName(), tag.getValue());
					}
				}

				for (int i = 1; i < amtBreakup.length; i++) {
					String amountComponent = amtBreakup[i];
					if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountComponent)) {
						continue;
					}
					if (incomingPaymentAmounts.get(amountComponent) != null) {
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
		// ptag.setName(CommonConstants.PAYMENT_INFO_IFSC_AC);
		ptag.setName(paymentinfo);
		ptag.setValue(paymentInfo);
		pymntInfo.getTags().add(ptag);
		billPaymentRequest.setPaymentInformation(pymntInfo);
		return billPaymentRequest;
	}
}
