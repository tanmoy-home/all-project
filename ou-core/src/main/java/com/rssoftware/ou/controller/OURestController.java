package com.rssoftware.ou.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.AgentType;
import org.bbps.schema.AmountType;
import org.bbps.schema.AmtType;
import org.bbps.schema.AnalyticsType;
import org.bbps.schema.BillDetailsType;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.BillerType;
import org.bbps.schema.CustomerDtlsType;
import org.bbps.schema.CustomerParamsType;
import org.bbps.schema.DeviceType;
import org.bbps.schema.HeadType;
import org.bbps.schema.PmtMtdType;
import org.bbps.schema.PymntInfType;
import org.bbps.schema.RiskScoresType;
import org.bbps.schema.RiskScoresType.Score;
import org.bbps.schema.SpltPayType;
import org.bbps.schema.TransactionType;
import org.bbps.schema.TxnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.ErrorCodeUtil;
import com.rssoftware.ou.common.PGIntegrationFieldType;
import com.rssoftware.ou.common.PGParam;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.Utility;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.FinTransactionData;
import com.rssoftware.ou.domain.BillPaymentRequestExt;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ChannelPartnerInfoDetails;
import com.rssoftware.ou.domain.FinTransactionDetails;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.PaymentModeBreakup;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.TransactionDataView;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CCFCalculatorService;
import com.rssoftware.ou.tenant.service.ChannelPartnerInfoService;
import com.rssoftware.ou.tenant.service.CommonAgentService;
import com.rssoftware.ou.tenant.service.CommonBillerService;
import com.rssoftware.ou.tenant.service.FinTransactionDataService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.PGService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

import in.co.rssoftware.bbps.schema.BillPaymentForm;
import in.co.rssoftware.bbps.schema.BillPaymentForm.Option;
import in.co.rssoftware.bbps.schema.BillPaymentForm.Option.Breakup;
import in.co.rssoftware.bbps.schema.CCFRequest;
import in.co.rssoftware.bbps.schema.DeviceType.Tag;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.FinTransactionDetailsType;
import in.co.rssoftware.bbps.schema.PayAmountType;
import in.co.rssoftware.bbps.schema.PaymentReceipt;
import in.co.rssoftware.bbps.schema.PaymentRequest;
import in.co.rssoftware.bbps.schema.PaymentSearchInformation;
import in.co.rssoftware.bbps.schema.QckPayType;

@RestController
@RequestMapping(value = "/APIService")
public class OURestController {

	private static Log logger = LogFactory.getLog(OURestController.class);

	@Autowired
	private BillerService billerService;

	@Autowired
	private ParamService paramService;

	@Autowired
	private TransactionDataService transactionDataService;

	@Autowired
	private IDGeneratorService idGeneratorService;

	@Autowired
	private TenantDetailService tenantDetailService;

	@Autowired
	private CommonAgentService commonAgentService;

	@Autowired
	private CCFCalculatorService ccfCalculatorService;

	@Autowired
	private PGService pgService;

	@Autowired
	private FinTransactionDataService finTransactionDataService;

	@Autowired
	private IDGeneratorService idGenetarorService;

	@Autowired
	private ChannelPartnerInfoService channelPartnerInfoService;

	@Autowired
	private CommonBillerService commonBillerService;

	/*
	 * @RequestMapping(value =
	 * "/bill-payment-form/urn:tenantId:{tenantId}/{searchParam}/{id}", method =
	 * RequestMethod.GET, consumes = { MediaType.APPLICATION_JSON_VALUE,
	 * MediaType.APPLICATION_XML_VALUE }, produces = {
	 * MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	 * public @ResponseBody BillPaymentForm
	 * getBillPaymentFormByBillerID(HttpServletRequest request,
	 * HttpServletResponse response, @PathVariable String
	 * tenantId, @PathVariable String searchParam,
	 * 
	 * @PathVariable String id) throws IOException {
	 * TransactionContext.putTenantId(tenantId); BillPaymentForm billPaymentForm
	 * = new BillPaymentForm(); if ("billerId".equals(searchParam)) { BillerView
	 * biller = billerService.getBillerById(id);
	 * 
	 * List<ParamConfig> list = biller.getBillerCustomerParams(); for
	 * (ParamConfig paramConfig : list) { BillPaymentForm.CustomParams
	 * customParams = new BillPaymentForm.CustomParams();
	 * customParams.setName(paramConfig.getParamName());
	 * customParams.setType(paramConfig.getDataType().name());
	 * customParams.setIsMandatory(!paramConfig.getOptional());
	 * billPaymentForm.getCustomParams().add(customParams); } } else if
	 * ("referenceId".equals(searchParam)) {
	 * billPaymentForm.getOptions().addAll(getBillPaymentOptions(id)); } return
	 * billPaymentForm; }
	 */

	@RequestMapping(value = "/bill-payment-form/urn:tenantId:{tenantId}", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public @ResponseBody BillPaymentForm getBillPaymentFormByBillerID(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String tenantId,
			@RequestBody PaymentSearchInformation searchInfo) throws IOException {
		TransactionContext.putTenantId(tenantId);
		String searchParam = searchInfo.getSearchParam();
		String id = searchInfo.getParamId();
		BillPaymentForm billPaymentForm = new BillPaymentForm();
		if ("billerId".equals(searchParam)) {
			BillerView biller = billerService.getBillerById(id);

			List<ParamConfig> list = biller.getBillerCustomerParams();
			for (ParamConfig paramConfig : list) {
				BillPaymentForm.CustomParams customParams = new BillPaymentForm.CustomParams();
				customParams.setName(paramConfig.getParamName());
				customParams.setType(paramConfig.getDataType().name());
				customParams.setIsMandatory(!paramConfig.getOptional());
				billPaymentForm.getCustomParams().add(customParams);
			}
		} else if ("referenceId".equals(searchParam)) {
			billPaymentForm.getOptions().addAll(getBillPaymentOptions(id));
		}
		return billPaymentForm;
	}

	@RequestMapping(value = "/bill-payment-form-post/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public @ResponseBody Callable<PaymentReceipt> billPayment(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestBody PaymentRequest paymentRequest)
					throws ValidationException, IOException {
		TransactionContext.putTenantId(tenantId);
		BillPaymentRequest billPaymentRequest = createBillPaymentRequest(paymentRequest);
		// Changes for adding AgentChannel Details start
		// ..Modified by Samarjit..//
		BillPaymentRequestExt billPaymentRequestExt = new BillPaymentRequestExt();
		if (paymentRequest.getAgentChannelDetails() != null) {
			billPaymentRequestExt.setAgentChannelID(paymentRequest.getAgentChannelDetails().getAgentChannelID());
			billPaymentRequestExt
					.setAgentChannelCustomerID(paymentRequest.getAgentChannelDetails().getAgentChannelCustomerID());
			billPaymentRequestExt.setAgentChannelTransactionID(
					paymentRequest.getAgentChannelDetails().getAgentChannelTransactionID());
		}
		if (paymentRequest.getFinTxnDetails() != null) {
			billPaymentRequestExt.setFinTransactionDetails(getFinTransactionDetails(paymentRequest.getFinTxnDetails()));
		}
		billPaymentRequestExt.setBillPaymentRequest(billPaymentRequest);

		return new Callable<PaymentReceipt>() {
			@Override
			public PaymentReceipt call() {
				BillPaymentResponse billPaymentResponse = null;
				PaymentReceipt paymentReceipt = null;
				try {
					TransactionContext.putTenantId(tenantId);
					if (billPaymentRequestExt.getBillPaymentRequest() == null)
						return ErrorCodeUtil.generateErroneousPaymentReceipt("COU_INVALID_REF_ID");
					LogUtils.logReqRespMessage(paymentRequest,
							billPaymentRequestExt.getBillPaymentRequest().getHead().getRefId(), Action.PAYMENT_REQUEST);

					/*if (commonBillerService.checkIsOnUs(
							billPaymentRequestExt.getBillPaymentRequest().getBillDetails().getBiller().getId())) {
						billPaymentResponse = commonBillerService.payBillOnUs(billPaymentRequestExt);
<<<<<<< HEAD
					} else*/ {
						billPaymentResponse = commonAgentService.payBill(billPaymentRequestExt);
					}

					if (billPaymentResponse != null
							&& "000".equals(billPaymentResponse.getReason().getResponseCode())) {
						paymentReceipt = createPaymentReceipt(billPaymentResponse,
								billPaymentRequestExt.getBillPaymentRequest());
					} else if (billPaymentResponse != null
							&& "ACK".equals(billPaymentResponse.getReason().getResponseCode())) {
						paymentReceipt = ErrorCodeUtil.generatePaymentReceiptForErroneousAck(billPaymentResponse);
					} else {
						paymentReceipt = ErrorCodeUtil.generateErroneousPaymentReceipt(billPaymentResponse);
					}
				} catch (ValidationException e) {
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorCd(e.getCode());
					errorMessage.setErrorDtl(e.getDescription());
					paymentReceipt = new PaymentReceipt();
					paymentReceipt.getErrorMessages().add(errorMessage);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					logger.info("In Excp : " + e.getMessage());
				}
				LogUtils.logReqRespMessage(paymentReceipt,
						billPaymentRequestExt.getBillPaymentRequest().getHead().getRefId(), Action.PAYMENT_RESPONSE);
				return paymentReceipt;
			}
		};
	}

	@RequestMapping(value = "/calculate-ccf/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public String getCCF(@PathVariable String tenantId, @RequestBody CCFRequest ccfRequest) throws IOException {
		TransactionContext.putTenantId(tenantId);

		String billAmount = ccfRequest.getBillAmount();
		if (StringUtils.isEmpty(billAmount)) {
			return CommonConstants.EMPTY_STRING;
		}
		billAmount = new BigDecimal(billAmount).multiply(CommonConstants.HUNDRED).toPlainString();
		AgentType agent = getAgentDetails(ccfRequest.getAgent());

		PmtMtdType paymentMethodType = getPaymentMethod(ccfRequest.getPaymentMethod());

		String ccf = ccfCalculatorService.calculateCCF(ccfRequest.getBillerId(), billAmount, agent, paymentMethodType);
		return new BigDecimal(ccf).divide(CommonConstants.HUNDRED).toPlainString();
	}

	@RequestMapping(value = "/pgparams/urn:tenantId:{tenantId}", method = RequestMethod.GET)
	public List<PGParam> getAllPGParams(@PathVariable String tenantId) {
		TransactionContext.putTenantId(tenantId);
		return pgService.getPGIntegrationFields("IDBI", PGIntegrationFieldType.REQ);
	}

	@RequestMapping(value = "/process_pg_payment/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public BillPaymentResponse processPGPayment(@PathVariable String tenantId,
			@RequestBody BillPaymentRequest billPaymentRequest) {
		TransactionContext.putTenantId(tenantId);
		return pgService.processPaymentRequest(billPaymentRequest);
	}

	@RequestMapping(value = "/tenantparam/urn:tenantId:{tenantId}/{paramName}", method = RequestMethod.GET)
	public String getTenantParam(@PathVariable String tenantId, @PathVariable String paramName) {
		TransactionContext.putTenantId(tenantId);
		return paramService.retrieveStringParamByName(paramName);
	}

	@RequestMapping(value = "/getuniqueid/urn:tenantId:{tenantId}", method = RequestMethod.GET)
	public String getUniqueId(@PathVariable String tenantId) {
		TransactionContext.putTenantId(tenantId);
		return idGenetarorService.getUniqueID(CommonConstants.LENGTH_TXN_REF_ID, tenantId);
	}

	@RequestMapping(value = "/fintransaction_insert/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public String insertFinTransaction(@PathVariable String tenantId,
			@RequestBody FinTransactionDetails finTransactionDetails) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		return finTransactionDataService.insert(finTransactionDetails);
	}

	@RequestMapping(value = "/insertChannelPartnerInfo/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public String insertChannelPartnerInfo(@PathVariable String tenantId,
			@RequestBody ChannelPartnerInfoDetails channelPartnerInfoDetails) throws ValidationException {
		TransactionContext.putTenantId(tenantId);
		return channelPartnerInfoService.insert(channelPartnerInfoDetails);
	}

	@RequestMapping(value = "/fintransaction_update/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public String updateFinTransaction(@PathVariable String tenantId,
			@RequestBody FinTransactionData finTransactionData) throws ValidationException {

		TransactionContext.putTenantId(tenantId);
		finTransactionDataService.createOrUpdate(finTransactionData);
		return "Success";
		// try {
		//
		// } catch (JsonProcessingException jpe) {
		// throw
		// ValidationException.getInstance(ValidationErrorReason.INVALID_JSON);
		// }
	}

	@RequestMapping(value = "/fintransaction_get/urn:tenantId:{tenantId}", method = RequestMethod.POST)
	public FinTransactionData getFinTransaction(@PathVariable String tenantId, @RequestBody String txnRefId)
			throws ValidationException {

		TransactionContext.putTenantId(tenantId);
		FinTransactionData finTransactionData = finTransactionDataService.get(txnRefId);
		return finTransactionData;
		// try {
		// FinTransactionData finTransactionData =
		// finTransactionDataService.get(txnRefId);
		// return finTransactionData;
		//// FinTransactionDetailsType finTransactionDetailsType = new
		// FinTransactionDetailsType();
		//// finTransactionDetailsType.setAuthCode(finTransactionData.getAuthCode());
		//// finTransactionDetailsType.setPaymentDetails(finTransactionData.getP);
		//// finTransactionDetailsType.setPaymentStatus(finTransactionData.getCurrentStatus());
		//// finTransactionDetailsType.setRequestJsonAsString(finTransactionData.getRe);
		//// finTransactionDetailsType.setResponseJsonAsString();
		//// finTransactionDetailsType.setTxnRefId();
		//
		//
		//// return "Success";
		// } catch (JsonProcessingException jpe) {
		// throw
		// ValidationException.getInstance(ValidationErrorReason.INVALID_JSON);
		// }
	}

	/*
	 * @RequestMapping(value = "/bill-pay-form-post/urn:tenantId:{tenantId}",
	 * method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE,
	 * produces = MediaType.APPLICATION_XML_VALUE) public @ResponseBody
	 * PaymentReceipt oldBillPayment(HttpServletRequest request,
	 * HttpServletResponse response,
	 * 
	 * @PathVariable String tenantId, @RequestBody PaymentRequest
	 * paymentRequest) throws Exception {
	 * 
	 * TransactionContext.putTenantId(tenantId); BillPaymentRequest
	 * billPaymentRequest = createBillPaymentRequest(paymentRequest); // Changes
	 * for adding AgentChannel Details start // ..Modified by Samarjit..//
	 * BillPaymentRequestExt billPaymentRequestExt = new
	 * BillPaymentRequestExt();
	 * billPaymentRequestExt.setAgentChannelID(paymentRequest.
	 * getAgentChannelDetails().getAgentChannelID()); billPaymentRequestExt
	 * .setAgentChannelCustomerID(paymentRequest.getAgentChannelDetails().
	 * getAgentChannelCustomerID()); billPaymentRequestExt
	 * .setAgentChannelTransactionID(paymentRequest.getAgentChannelDetails().
	 * getAgentChannelTransactionID());
	 * billPaymentRequestExt.setFinTransactionDetails(getFinTransactionDetails(
	 * paymentRequest.getFinTxnDetails()));
	 * billPaymentRequestExt.setBillPaymentRequest(billPaymentRequest);
	 * BillPaymentResponse billPaymentResponse = billPaymentGateway
	 * .processOUBillPaymentRequestExt(billPaymentRequestExt);
	 * 
	 * // Changes for adding AgentChannel details end // BillPaymentResponse
	 * billPaymentResponse = //
	 * billPaymentGateway.processOUBillPaymentRequest(billPaymentRequest);
	 * PaymentReceipt paymentReceipt =
	 * createPaymentReceipt(billPaymentResponse); return paymentReceipt; }
	 */

	private List<Option> getBillPaymentOptions(String referenceId) throws IOException {
		TransactionDataView transactionData = transactionDataService.getTransactionData(referenceId, RequestType.FETCH);
		BillFetchResponse billFetchResponse = transactionData.getBillFetchResponse();
		List<Option> options = new ArrayList<>();

		BillerView biller = billerService
				.getBillerById(transactionData.getBillFetchRequest().getBillDetails().getBiller().getId());

		if (biller.getBillerResponseParams() != null
				&& !biller.getBillerResponseParams().getAmountOptions().isEmpty()) {
			Map<String, BigDecimal> incomingPaymentAmounts = new HashMap<String, BigDecimal>();

			if (!billFetchResponse.getBillerResponse().getTags().isEmpty()) {
				for (BillerResponseType.Tag tag : billFetchResponse.getBillerResponse().getTags()) {
					if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())) {
						BigDecimal tagAmount = (new BigDecimal(tag.getValue())).divide(new BigDecimal(100)).setScale(2,
								RoundingMode.HALF_UP);
						incomingPaymentAmounts.put(tag.getName(), tagAmount);
					}
				}
			}

			for (BillerResponseParams.AmountOption ao : biller.getBillerResponseParams().getAmountOptions()) {
				if (ao != null) {
					boolean allAmtsPresent = true;
					for (String amountBreakup : ao.getAmountBreakups()) {
						if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
							continue;
						}
						if (incomingPaymentAmounts.get(amountBreakup) == null) {
							allAmtsPresent = false;
							break;
						}
					}

					if (allAmtsPresent) {
						BillPaymentForm.Option option = new BillPaymentForm.Option();
						for (String amountBreakup : ao.getAmountBreakups()) {
							Breakup breakup = new Breakup();

							if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
								breakup.setName("Bill Amount");
								BigDecimal amount = (new BigDecimal(billFetchResponse.getBillerResponse().getAmount()))
										.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
								breakup.setAmount(amount.toPlainString());
							} else {
								breakup.setName(amountBreakup);
								breakup.setAmount(incomingPaymentAmounts.get(amountBreakup).toPlainString());
							}
							option.getBreakups().add(breakup);
						}
						options.add(option);
					}
				}
			}

		}
		return options;
	}

	private BillPaymentRequest createBillPaymentRequest(PaymentRequest paymentRequest) throws IOException {
		TenantDetail tenantDetail = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId());
		BillFetchRequest billFetchRequest = null;
		BillFetchResponse billFetchResponse = null;
		boolean isQuickPay = QckPayType.YES.equals(paymentRequest.getPaymentMethod().getQuickPay());
		if (!isQuickPay) {
			TransactionDataView td = transactionDataService.getTransactionData(paymentRequest.getRefId(),
					RequestType.FETCH);
			if (td == null)
				return null;
			billFetchRequest = td.getBillFetchRequest();
			billFetchResponse = td.getBillFetchResponse();
		}
		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();

		// Head
		HeadType head = getHead(paymentRequest, tenantDetail, isQuickPay);
		billPaymentRequest.setHead(head);
		// Analytics
		AnalyticsType analytics = getAnalytics();
		billPaymentRequest.setAnalytics(analytics);
		// Txn
		TxnType txn = getTxn(paymentRequest, tenantDetail);
		billPaymentRequest.setTxn(txn);
		// Customer details
		CustomerDtlsType customerDetails = getCustomerDetails(paymentRequest, isQuickPay, billFetchRequest);
		billPaymentRequest.setCustomer(customerDetails);
		// Agent
		AgentType agent = getAgentDetails(paymentRequest.getAgent());
		billPaymentRequest.setAgent(agent);
		// BillDetails
		BillDetailsType billDetails = getBillDetails(paymentRequest, isQuickPay, billFetchRequest);
		billPaymentRequest.setBillDetails(billDetails);
		// BillerResponse
		BillerResponseType billerResponse = getBillerResponse(isQuickPay, billFetchResponse);
		billPaymentRequest.setBillerResponse(billerResponse);
		// AdditionalInfo
		AdditionalInfoType additionalInfo = getAdditionalInfo(isQuickPay, billFetchResponse);
		billPaymentRequest.setAdditionalInfo(additionalInfo);
		// PaymentMethod
		PmtMtdType pmtMtd = getPaymentMethod(paymentRequest.getPaymentMethod());
		billPaymentRequest.setPaymentMethod(pmtMtd);
		// Amount
		AmountType amount = getAmount(paymentRequest, isQuickPay);
		billPaymentRequest.setAmount(amount);
		// PaymentInformation
		PymntInfType pymntInf = getPaymentInformation(paymentRequest);
		billPaymentRequest.setPaymentInformation(pymntInf);
		return billPaymentRequest;
	}

	private HeadType getHead(PaymentRequest paymentRequest, TenantDetail tenantDetail, boolean isQuickPay) {
		HeadType head = new HeadType();
		head.setVer("1.0");
		head.setTs(CommonUtils.getFormattedCurrentTimestamp());
		head.setOrigInst(tenantDetail.getOuName());
		if (!isQuickPay) {
			head.setRefId(paymentRequest.getRefId());
		} else {
			head.setRefId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, tenantDetail.getOuName()));
		}
		return head;
	}

	private AnalyticsType getAnalytics() {
		AnalyticsType analytics = new AnalyticsType();
		AnalyticsType.Tag payRequestStartTag = new AnalyticsType.Tag();
		payRequestStartTag.setName("PAYREQUESTSTART");
		payRequestStartTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(payRequestStartTag);

		AnalyticsType.Tag payRequestEndTag = new AnalyticsType.Tag();
		payRequestEndTag.setName("PAYREQUESTEND");
		payRequestEndTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(payRequestEndTag);
		return analytics;
	}

	private TxnType getTxn(PaymentRequest paymentRequest, TenantDetail tenantDetail) {
		TxnType txn = new TxnType();
		txn.setTs(CommonUtils.getFormattedCurrentTimestamp());
		txn.setMsgId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, tenantDetail.getOuName()));
		txn.setRiskScores(getRiskScores(paymentRequest, tenantDetail));
		if (paymentRequest.getTxnRefId() != null) {
			txn.setTxnReferenceId(paymentRequest.getTxnRefId());
		} else {
			txn.setTxnReferenceId(
					idGeneratorService.getUniqueID(CommonConstants.LENGTH_TXN_REF_ID, tenantDetail.getOuName()));
		}
		txn.setType(TransactionType.FORWARD_TYPE_REQUEST.value());
		return txn;
	}

	private RiskScoresType getRiskScores(PaymentRequest paymentRequest, TenantDetail tenantDetail) {
		RiskScoresType riskScores = new RiskScoresType();
		if (null != paymentRequest.getRiskScores() && paymentRequest.getRiskScores().getScores().size() > 0) {
			for (in.co.rssoftware.bbps.schema.RiskScoresType.Score riskScore : paymentRequest.getRiskScores()
					.getScores()) {
				Score score = new Score();
				score.setProvider(riskScore.getProvider());
				score.setType(riskScore.getType());
				score.setValue(riskScore.getValue());
				riskScores.getScores().add(score);
			}
		} else {
			Score score = new Score();
			score.setProvider(tenantDetail.getOuName());
			score.setType("TXNRISK");
			score.setValue("000"); // populate a default score now
			riskScores.getScores().add(score);
		}
		return riskScores;
	}

	private CustomerDtlsType getCustomerDetails(PaymentRequest paymentRequest, boolean isQuickPay,
			BillFetchRequest billFetchRequest) {
		CustomerDtlsType customerDetails = new CustomerDtlsType();
		if (!isQuickPay) {
			customerDetails.setMobile(billFetchRequest.getCustomer().getMobile());
			customerDetails.getTags().addAll(billFetchRequest.getCustomer().getTags());
		} else {
			customerDetails.setMobile(paymentRequest.getCustomer().getMobile());
			for (in.co.rssoftware.bbps.schema.CustomerDtlsType.Tag tag : paymentRequest.getCustomer().getTags()) {
				CustomerDtlsType.Tag newTag = new CustomerDtlsType.Tag();
				newTag.setName(tag.getName());
				newTag.setValue(tag.getValue());
				customerDetails.getTags().add(newTag);
			}
		}
		return customerDetails;
	}

	private AgentType getAgentDetails(in.co.rssoftware.bbps.schema.AgentType agentType) {
		AgentType agent = new AgentType();
		agent.setId(agentType.getId());
		DeviceType deviceType = new DeviceType();
		if (agentType.getDevice() != null) {
			for (Tag tag : agentType.getDevice().getTags()) {
				org.bbps.schema.DeviceType.Tag newTag = new org.bbps.schema.DeviceType.Tag();
				newTag.setName(org.bbps.schema.DeviceTagNameType.fromValue(tag.getName().value()));
				newTag.setValue(tag.getValue());
				deviceType.getTags().add(newTag);
			}
		}
		agent.setDevice(deviceType);
		return agent;
	}

	private BillDetailsType getBillDetails(PaymentRequest paymentRequest, boolean isQuickPay,
			BillFetchRequest billFetchRequest) {
		if (!isQuickPay) {
			return billFetchRequest.getBillDetails();
		}

		BillDetailsType billDetails = new BillDetailsType();
		BillerType biller = new BillerType();
		biller.setId(paymentRequest.getBillDetails().getBiller().getId());
		billDetails.setBiller(biller);
		CustomerParamsType customerParams = new CustomerParamsType();
		for (in.co.rssoftware.bbps.schema.CustomerParamsType.Tag tag : paymentRequest.getBillDetails()
				.getCustomerParams().getTags()) {
			CustomerParamsType.Tag newTag = new CustomerParamsType.Tag();
			newTag.setName(tag.getName());
			newTag.setValue(tag.getValue());
			customerParams.getTags().add(newTag);
		}
		billDetails.setCustomerParams(customerParams);
		return billDetails;
	}

	private BillerResponseType getBillerResponse(boolean isQuickPay, BillFetchResponse billFetchResponse) {
		if (!isQuickPay) {
			return billFetchResponse.getBillerResponse();
		}
		return null;
	}

	private AdditionalInfoType getAdditionalInfo(boolean isQuickPay, BillFetchResponse billFetchResponse) {
		if (!isQuickPay) {
			return billFetchResponse.getAdditionalInfo();
		}
		return null;
	}

	private PmtMtdType getPaymentMethod(in.co.rssoftware.bbps.schema.PmtMtdType paymentMethodInfo) {
		PmtMtdType pmtMtd = new PmtMtdType();
		pmtMtd.setPaymentMode(paymentMethodInfo.getPaymentMode());
		pmtMtd.setQuickPay(org.bbps.schema.QckPayType.fromValue(paymentMethodInfo.getQuickPay().value()));

		pmtMtd.setSplitPay(SpltPayType.fromValue(paymentMethodInfo.getSplitPay().value()));
		return pmtMtd;
	}

	private AmountType getAmount(PaymentRequest paymentRequest, boolean isQuickPay) throws IOException {
		AmountType amount = new AmountType();
		AmtType amt = new AmtType();
		amt.setAmount(paymentRequest.getAmount().getAmount());
		if (StringUtils.isBlank(paymentRequest.getAmount().getCustomerConvinienceFee())) {
			amt.setCustConvFee(ccfCalculatorService.calculateCCF(paymentRequest.getBillDetails().getBiller().getId(),
					paymentRequest.getAmount().getAmount(), getAgentDetails(paymentRequest.getAgent()),
					getPaymentMethod(paymentRequest.getPaymentMethod())));
		} else {
			amt.setCustConvFee(paymentRequest.getAmount().getCustomerConvinienceFee());
		}
		amt.setCurrency(CommonConstants.INR_CURENCY);
		amount.setAmt(amt);
		amount.setSplitPayAmount(paymentRequest.getAmount().getSplitPayAmount());
		for (PayAmountType.Tag tag : paymentRequest.getAmount().getTags()) {
			AmountType.Tag newTag = new AmountType.Tag();
			newTag.setName(tag.getName());
			newTag.setValue(tag.getValue());
			amount.getTags().add(newTag);
		}
		return amount;
	}

	private PymntInfType getPaymentInformation(PaymentRequest paymentRequest) {
		PymntInfType pymntInf = new PymntInfType();
		for (in.co.rssoftware.bbps.schema.PymntInfType.Tag tag : paymentRequest.getPaymentInformation().getTags()) {
			PymntInfType.Tag newTag = new PymntInfType.Tag();
			newTag.setName(tag.getName());
			newTag.setValue(tag.getValue());
			pymntInf.getTags().add(newTag);
		}
		return pymntInf;
	}

	private PaymentReceipt createPaymentReceipt(BillPaymentResponse billPaymentResponse,
			BillPaymentRequest billPayReq) {
		logger.info("******************** BillPaymentResponse: from CU to COU  ***************************");
		LogUtils.logReqRespMessage(billPaymentResponse, null, null);
		logger.info("******************** BillPaymentResponse ***************************");

		PaymentReceipt paymentReceipt = new PaymentReceipt();
		paymentReceipt.setCustomerName(billPaymentResponse.getBillerResponse().getCustomerName());
		try {
			GregorianCalendar calender = new GregorianCalendar();

			calender.setTime(Utility.convertDateStrToCuDate(billPaymentResponse.getBillerResponse().getBillDate(),
					Utility.CU_DATE_FFORMAT));

			// paymentReceipt.setBillDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new
			// GregorianCalendar()));
			paymentReceipt.setBillDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(calender));
			paymentReceipt.setTransactionDateTime(
					DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		} catch (DatatypeConfigurationException e) {
			logger.error("Problem in setting BillDate: " + e.getCause());
		}
		paymentReceipt.setTransactionRefId(billPaymentResponse.getTxn().getTxnReferenceId());
		paymentReceipt.setBillNumber(billPaymentResponse.getBillerResponse().getBillNumber());
		paymentReceipt.setBillPeriod(billPaymentResponse.getBillerResponse().getBillPeriod());
		paymentReceipt.setAmount(billPaymentResponse.getBillerResponse().getAmount());
		paymentReceipt.setCcf(billPaymentResponse.getBillerResponse().getCustConvFee());
		paymentReceipt.setPaymentChannel(CommonUtils.getPaymentChannel(billPayReq).name().replaceAll("_", " "));
		paymentReceipt.setCustomerMobile(billPayReq.getCustomer().getMobile());
		PaymentModeBreakup[] pm = CommonUtils.getPaymentModeBreakups(billPayReq);
		paymentReceipt.setPaymentMode(pm[0].getPaymentMode().getExpandedForm());

		String billerId = billPayReq.getBillDetails().getBiller().getId();
		paymentReceipt.setBillerId(billerId);
		BillerView billerView = billerService.getBillerById(billerId);
		paymentReceipt.setBillerName(billerView.getBlrName());
		return paymentReceipt;
	}

	private FinTransactionDetails getFinTransactionDetails(FinTransactionDetailsType finTransactionDetailsDto) {
		FinTransactionDetails finTransactionDetails = new FinTransactionDetails();
		finTransactionDetails.setAuthCode(finTransactionDetailsDto.getAuthCode());
		finTransactionDetails.setClientTxnRefId(finTransactionDetailsDto.getTxnRefId());
		finTransactionDetails.setPaymentAmountDetails(finTransactionDetailsDto.getPaymentDetails());
		finTransactionDetails.setStatus(finTransactionDetailsDto.getPaymentStatus());
		// finTransactionDetails.setRequestJson(finTransactionDetailsDto.getRequestJsonAsString().get);
		// finTransactionDetails.setResponseJson(finTransactionDetailsDto.getResponseJsonAsString());
		return finTransactionDetails;

	}

}
