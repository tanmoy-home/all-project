package com.rssoftware.ou.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.AdditionalInfoType;
import org.bbps.schema.AnalyticsType;
import org.bbps.schema.BillFetchRequest;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillerResponseType;
import org.bbps.schema.HeadType;
import org.bbps.schema.ReasonType;
import org.bbps.schema.TxnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.businessprocessor.AsyncProcessor;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.ErrorCodeUtil;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.common.utils.RequestResponseGenerator;
import com.rssoftware.ou.domain.BillFetchRequestExt;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.gateway.BillFetchGateway;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CommonAgentService;
import com.rssoftware.ou.tenant.service.CommonBillerService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.TenantParamService;

import in.co.rssoftware.bbps.schema.AgentType;
import in.co.rssoftware.bbps.schema.BillDetailsType;
import in.co.rssoftware.bbps.schema.BillFetchCustomParams;
import in.co.rssoftware.bbps.schema.BillerCatagory;
import in.co.rssoftware.bbps.schema.BillerDetail;
import in.co.rssoftware.bbps.schema.BillerList;
import in.co.rssoftware.bbps.schema.BillerType;
import in.co.rssoftware.bbps.schema.CustomerDtlsType;
import in.co.rssoftware.bbps.schema.CustomerParamsType;
import in.co.rssoftware.bbps.schema.DeviceTagNameType;
import in.co.rssoftware.bbps.schema.DeviceType;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.FetchRequest;
import in.co.rssoftware.bbps.schema.FetchResponse;
import in.co.rssoftware.bbps.schema.PaymentInformation;
import in.co.rssoftware.bbps.schema.RiskScoresType;

@RestController
@RequestMapping(value = "/APIService")
public class APIController {

	private static Log logger = LogFactory.getLog(APIController.class);
	
	@Autowired
	private BillerService billerService;

	@Autowired
	private BillFetchGateway billFetchGateway;

	@Autowired
	private IDGeneratorService idGeneratorService;

	@Autowired
	private TenantDetailService tenantDetailService;

	@Autowired
	private TenantParamService tenantParamService;

	@Autowired
	private CommonAgentService commonAgentService;
	
	@Autowired
	private CommonBillerService commonBillerService;

	@Autowired
	private AsyncProcessor asyncProcessor;

	/*@RequestMapping(value = "/biller-fetch-custom-params/urn:tenantId:{tenantId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public BillFetchCustomParams getBillerFormElements(@PathVariable String tenantId,
			@RequestParam(value = "billerId") String billerId) throws IOException {
		TransactionContext.putTenantId(tenantId);

		List<ParamConfig> custParams = commonAgentService.getCustomerParamsbyBillerId(billerId);
		BillFetchCustomParams billerFetch = mapTo(custParams);

		return billerFetch;

	}*/
	/*edited by Somnath at 19/01/2017*/
	
	@RequestMapping(value = "/biller-fetch-custom-params/urn:tenantId:{tenantId}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },consumes = {
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public BillFetchCustomParams getBillerFormElements(@PathVariable String tenantId,
			@RequestBody PaymentInformation info) throws IOException {
		TransactionContext.putTenantId(tenantId);
		String billerId=info.getBillerId();
		List<ParamConfig> custParams = commonAgentService.getCustomerParamsbyBillerId(billerId);
		BillFetchCustomParams billerFetch = mapTo(custParams);

		return billerFetch;

	}

	/*@RequestMapping(value = "/biller-category-list/urn:tenantId:{tenantId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public BillerCatagory getBillerCategories(@PathVariable String tenantId,
			@RequestParam(value = "paymentChannels", required = false) String pcs,
			@RequestParam(value = "paymentModes", required = false) String pms) throws IOException {
		TransactionContext.putTenantId(tenantId);

		List<String> paymentChannels = CommonUtils.hasValue(pcs) ? Arrays.asList(pcs.trim().split(","))
				: new ArrayList<>();
		List<String> paymentModes = CommonUtils.hasValue(pms) ? Arrays.asList(pms.trim().split(","))
				: new ArrayList<>();
		BillerCatagory billerCatagory = new BillerCatagory();

		List<String> categories = null;
		try {
			categories = commonAgentService.getBillerCategories(paymentChannels, paymentModes);
			billerCatagory.getCatagories().addAll(categories);
		} catch (ValidationException ve) {
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					billerCatagory.getErrors().add(mapTo(error));
				}
			}
		}

		return billerCatagory;
	}*/

	@RequestMapping(value = "/biller-category-list/urn:tenantId:{tenantId}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },consumes = {
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public BillerCatagory getBillerCategories(@PathVariable String tenantId,
			@RequestBody PaymentInformation info) throws IOException {

		logger.info("info.getPaymentChannel() "+info.getPaymentChannel());	
		logger.info("info.getPaymentMode() "+info.getPaymentMode());
		String pcs=info.getPaymentChannel();
		String pms=info.getPaymentMode();
		TransactionContext.putTenantId(tenantId);

		List<String> paymentChannels = CommonUtils.hasValue(pcs) ? Arrays.asList(pcs.trim().split(","))
				: new ArrayList<>();
		List<String> paymentModes = CommonUtils.hasValue(pms) ? Arrays.asList(pms.trim().split(","))
				: new ArrayList<>();
		BillerCatagory billerCatagory = new BillerCatagory();
		
		List<String> categories = null;
		try {
			categories = commonAgentService.getBillerCategories(paymentChannels, paymentModes);
			billerCatagory.getCatagories().addAll(categories);
		} catch (ValidationException ve) {
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					billerCatagory.getErrors().add(mapTo(error));
				}
			}
		}

		return billerCatagory;
	}
	
	/*@RequestMapping(value = "/biller-list/urn:tenantId:{tenantId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public BillerList getBillerList(@PathVariable String tenantId,
			@RequestParam(value = "billerCategory") String billerCategory,
			@RequestParam(value = "paymentChannels", required = false) String pcs,
			@RequestParam(value = "paymentModes", required = false) String pms) throws IOException {
		TransactionContext.putTenantId(tenantId);

		List<String> paymentChannels = CommonUtils.hasValue(pcs) ? Arrays.asList(pcs.trim().split(","))
				: new ArrayList<>();
		List<String> paymentModes = CommonUtils.hasValue(pms) ? Arrays.asList(pms.trim().split(","))
				: new ArrayList<>();
		BillerList billerList = new BillerList();

		List<BillerView> billerViewList = null;
		try {
			billerViewList = commonAgentService.getBillersbyCategory(billerCategory, paymentChannels, paymentModes);
			for (BillerView view : billerViewList) {
				billerList.getBillers().add(mapToBillerDetail(view));
			}
		} catch (ValidationException ve) {
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					billerList.getErrors().add(mapTo(error));
				}
			}
		}

		return billerList;
	}*/
	
	
	/*edited by Somnath at 18/01/2017*/
	@RequestMapping(value = "/biller-list/urn:tenantId:{tenantId}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public BillerList getBillerList(@PathVariable String tenantId,
			@RequestBody PaymentInformation info) throws IOException {
		
		logger.info(" ---------------------- AgentService   biller-list ");
		
		String billerCategory=info.getCategory();
		String pcs=info.getPaymentChannel();
		String pms=info.getPaymentMode();
		TransactionContext.putTenantId(tenantId);

		List<String> paymentChannels = CommonUtils.hasValue(pcs) ? Arrays.asList(pcs.trim().split(","))
				: new ArrayList<>();
		List<String> paymentModes = CommonUtils.hasValue(pms) ? Arrays.asList(pms.trim().split(","))
				: new ArrayList<>();
		BillerList billerList = new BillerList();

		List<BillerView> billerViewList = null;
		try {
			billerViewList = commonAgentService.getBillersbyCategory(billerCategory, paymentChannels, paymentModes);
			for (BillerView view : billerViewList) {
				billerList.getBillers().add(mapToBillerDetail(view));
			}
		} catch (ValidationException ve) {
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					billerList.getErrors().add(mapTo(error));
				}
			}
		}

		return billerList;
	}

/*
	@RequestMapping(value = "/sub-biller-list/urn:tenantId:{tenantId}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public BillerList getSubBillerList(@PathVariable String tenantId, @RequestParam(value = "billerId") String billerId,
			@RequestParam(value = "paymentChannels", required = false) String pcs,
			@RequestParam(value = "paymentModes", required = false) String pms) throws IOException {
		TransactionContext.putTenantId(tenantId);

		List<String> paymentChannels = CommonUtils.hasValue(pcs) ? Arrays.asList(pcs.trim().split(","))
				: new ArrayList<>();
		List<String> paymentModes = CommonUtils.hasValue(pms) ? Arrays.asList(pms.trim().split(","))
				: new ArrayList<>();
		BillerList billerList = new BillerList();

		List<BillerView> billerViewList = null;
		try {
			billerViewList = commonAgentService.getSubBillers(billerId, paymentChannels, paymentModes);
			for (BillerView view : billerViewList) {
				billerList.getBillers().add(mapToBillerDetail(view));
			}
		} catch (ValidationException ve) {
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					billerList.getErrors().add(mapTo(error));
				}
			}
		}

		return billerList;
	}*/

	
	/*edited by Somnath at 18/01/2017*/
	@RequestMapping(value = "/sub-biller-list/urn:tenantId:{tenantId}", method = RequestMethod.POST, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },  consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public BillerList getSubBillerList(@PathVariable String tenantId,@RequestBody PaymentInformation info) throws IOException {
		
		logger.info(" ---------------------- AgentService   sub-biller-list ");
		
		String billerId=info.getBillerId();
		String pcs=info.getPaymentChannel();
		String pms=info.getPaymentMode();
		TransactionContext.putTenantId(tenantId);

		List<String> paymentChannels = CommonUtils.hasValue(pcs) ? Arrays.asList(pcs.trim().split(","))
				: new ArrayList<>();
		List<String> paymentModes = CommonUtils.hasValue(pms) ? Arrays.asList(pms.trim().split(","))
				: new ArrayList<>();
		BillerList billerList = new BillerList();

		List<BillerView> billerViewList = null;
		try {
			billerViewList = commonAgentService.getSubBillers(billerId, paymentChannels, paymentModes);
			for (BillerView view : billerViewList) {
				billerList.getBillers().add(mapToBillerDetail(view));
			}
		} catch (ValidationException ve) {
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					billerList.getErrors().add(mapTo(error));
				}
			}
		}

		return billerList;
	}

	
	@RequestMapping(value = "/bill-fetch-form-post/urn:tenantId:{tenantId}", method = RequestMethod.POST ,produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public Callable<FetchResponse> postBillerFormElements(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestBody FetchRequest params,@RequestParam(value="isWeb" ,required=false) Boolean isWeb) throws ValidationException, IOException {
		TransactionContext.putTenantId(tenantId);
		BillFetchRequest billFetchRequest = createBillFetchRequest(params);

		/* Modified */
		BillFetchRequestExt billFetchRequestExt = new BillFetchRequestExt();

		if (params.getAgentChannelDetails() != null) {
			billFetchRequestExt.setAgentChannelID(params.getAgentChannelDetails().getAgentChannelID());

			billFetchRequestExt.setAgentChannelCustomerID(params.getAgentChannelDetails().getAgentChannelCustomerID());

			billFetchRequestExt
					.setAgentChannelTransactionID(params.getAgentChannelDetails().getAgentChannelTransactionID());
		}

		billFetchRequestExt.setCurrentNodeAddress(CommonUtils.getServerNameWithPort());
		billFetchRequestExt.setBillFetchRequest(billFetchRequest);

		return new Callable<FetchResponse>() {
			@Override
			public FetchResponse call() {
				BillFetchResponse billFetchResponse = null;
				FetchResponse fetchResponse = null;
				try {
					TransactionContext.putTenantId(tenantId);
					/*if(commonBillerService.checkIsOnUs(billFetchRequestExt.getBillFetchRequest().getBillDetails().getBiller().getId())){
						billFetchResponse = commonBillerService.fetchBillOnUs(billFetchRequestExt);
					}else*/{
					billFetchResponse = commonAgentService.fetchBill(billFetchRequestExt);
					}
					if (billFetchResponse != null && "000".equals(billFetchResponse.getReason().getResponseCode())) {
						fetchResponse = createFetchResponse(billFetchResponse);
						/*BillerView billerView=billerService.getBillerById(params.getBillDetails().getBiller().getId());
						fetchResponse=includeAmountOption(fetchResponse,billerView,isWeb);*/
					} else if (billFetchResponse != null && "ACK".equals(billFetchResponse.getReason().getResponseCode())) {
						fetchResponse = ErrorCodeUtil.generateFetchResponseForErroneousAck(billFetchResponse);				
					} else {
						/*fetchResponse = new FetchResponse();
						ErrorMessage errorMessage = new ErrorMessage();
						errorMessage.setErrorCd(billFetchResponse.getReason().getComplianceRespCd());
						errorMessage.setErrorDtl(billFetchResponse.getReason().getComplianceReason());
						fetchResponse.getErrorMessages().add(errorMessage);*/
						fetchResponse = ErrorCodeUtil.generateErroneousFetchResponse(billFetchResponse);
					}
				} catch (ValidationException e) {
					logger.error(e.getDescription(), e);
					ErrorMessage errorMessage = new ErrorMessage();
					errorMessage.setErrorCd(e.getCode());
					errorMessage.setErrorDtl(e.getDescription());
					fetchResponse = new FetchResponse();
					fetchResponse.getErrorMessages().add(errorMessage);
				}

				catch (IOException e) {
					logger.error(e.getMessage(), e);
					logger.info("In Excp : " + e.getMessage());
				}

				return fetchResponse;
			}
		};
	}
	
	private  FetchResponse includeAmountOption(FetchResponse fetchresp,BillerView biller,Boolean isWeb)
	{
		
		if(isWeb==null)
		{
			isWeb=false;
		}
		List<in.co.rssoftware.bbps.schema.BillerResponseType.Tag> tags=new ArrayList<in.co.rssoftware.bbps.schema.BillerResponseType.Tag>();
		
//		StringBuilder select = new StringBuilder("");
//		select.append(
//				"<select name='billerAmountOptions' id='billerAmountOptions'  onChange='calculateCCF()'; style='width: 100%; padding: 5px 0px;'>");
		if(fetchresp!=null&&fetchresp.getErrorMessages().size()==0)
		{
			long amount=(long)Float.parseFloat(fetchresp.getBillerResponse().getAmount());
			
//		select.append("<option value=\"\">");
//		select.append("Please Select");
//		select.append("</option>");

		if (biller.getBillerResponseParams() != null
				&& !biller.getBillerResponseParams().getAmountOptions().isEmpty()) {
			Map<String, Object> incomingPaymentAmounts = new HashMap<String, Object>();

			if (!fetchresp.getBillerResponse().getTags().isEmpty()) {
				for (in.co.rssoftware.bbps.schema.BillerResponseType.Tag tag : fetchresp.getBillerResponse().getTags()) {
					if (tag != null && CommonUtils.hasValue(tag.getName()) && CommonUtils.hasValue(tag.getValue())) {
						if(isWeb)
						{
							BigDecimal tagAmount = (new BigDecimal(tag.getValue())).divide(new BigDecimal(100)).setScale(2,
									RoundingMode.HALF_UP);
							incomingPaymentAmounts.put(tag.getName(), tagAmount);
						}
						else
						{
							long tagAmount=Long.parseLong(tag.getValue())/100;
							incomingPaymentAmounts.put(tag.getName(), tagAmount);
						}
						
						
						
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

					if(!isWeb){
					if (allAmtsPresent) {
						long total = 0L;
						StringBuilder b = new StringBuilder("");
						StringBuilder breakup = new StringBuilder("");
						for (String amountBreakup : ao.getAmountBreakups()) {
							if (breakup.length() > 0) {
								breakup.append("|");
							}
							breakup.append(amountBreakup);
							if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
								b.append("Bill Amount (" + amount + ")+");
								total = total+amount;
							} else {
								b.append(amountBreakup + " ("
										+ incomingPaymentAmounts.get(amountBreakup).toString() + ")+");
								total +=(Long)incomingPaymentAmounts.get(amountBreakup);//total.add(incomingPaymentAmounts.get(amountBreakup));
							}
						}
						in.co.rssoftware.bbps.schema.BillerResponseType.Tag tag= new in.co.rssoftware.bbps.schema.BillerResponseType.Tag();
						tag.setValue(total + "|" + breakup);
						tag.setName(b.substring(0, b.length() - 1) + "=" + total);
						tags.add(tag);
//						
//						select.append("<option value=\"" + total.toPlainString() + "|" + breakup + "\">");
//						select.append(b.substring(0, b.length() - 1) + "=" + total.toPlainString());
//						select.append("</option>");

					}
					}
					else
					{
						if (allAmtsPresent) {
							BigDecimal amount1=(new BigDecimal(amount)).divide(new BigDecimal(100)).setScale(2,
									RoundingMode.HALF_UP);;
							BigDecimal total = BigDecimal.ZERO;
							StringBuilder b = new StringBuilder("");
							StringBuilder breakup = new StringBuilder("");
							for (String amountBreakup : ao.getAmountBreakups()) {
								if (breakup.length() > 0) {
									breakup.append("|");
								}
								breakup.append(amountBreakup);
								if (BillerResponseParams.BASE_BILL_AMOUNT.equals(amountBreakup)) {
									b.append("Bill Amount (" + amount1 + ")+");
									total.add(amount1);
								} else {
									b.append(amountBreakup + " ("
											+ incomingPaymentAmounts.get(amountBreakup).toString() + ")+");
									total.add((BigDecimal)incomingPaymentAmounts.get(amountBreakup));
								}
							}
							in.co.rssoftware.bbps.schema.BillerResponseType.Tag tag= new in.co.rssoftware.bbps.schema.BillerResponseType.Tag();
							tag.setValue(total.toPlainString() + "|" + breakup);
							tag.setName(b.substring(0, b.length() - 1) + "=" + total.toPlainString());
							tags.add(tag);
//							
//							select.append("<option value=\"" + total.toPlainString() + "|" + breakup + "\">");
//							select.append(b.substring(0, b.length() - 1) + "=" + total.toPlainString());
//							select.append("</option>");

						}
					}
				}
			}

		} else {
			
			in.co.rssoftware.bbps.schema.BillerResponseType.Tag tag= new in.co.rssoftware.bbps.schema.BillerResponseType.Tag();
			if(isWeb)
			{
				BigDecimal amnt=(new BigDecimal(tag.getValue())).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
				tag.setName("Bill Amount (" + amnt + ")");
				tag.setValue(amnt+"");
			}
			else
			{
				tag.setName("Bill Amount (" + amount + ")");
				tag.setValue(amount+"");
			}
			
			
			tags.add(tag);
		
		}

		fetchresp.getBillerResponse().getTags().clear();
		fetchresp.getBillerResponse().getTags().addAll(tags);
		}
		
		return fetchresp;
	}

	@RequestMapping(value = "/response/urn:tenantId:{tenantId}/{refId}", method = RequestMethod.GET)
	public Boolean notify(@PathVariable String tenantId, @PathVariable String refId) {
		return asyncProcessor.notify(refId);
	}

	BillFetchRequest createBillFetchRequest(FetchRequest fetchRequest) {
		BillFetchRequest request = new BillFetchRequest();
		AgentType agentType = fetchRequest.getAgent();
		org.bbps.schema.AgentType agentType2 = new org.bbps.schema.AgentType();

		if (agentType.getId() != null) {
			agentType2.setId(agentType.getId());
		} else {
			String agentId = tenantParamService.retrieveStringParamByName("DEFAULT_AGENT");
			agentType2.setId(agentId);
		}

		DeviceType deviceType = agentType.getDevice();
		org.bbps.schema.DeviceType deviceType2 = new org.bbps.schema.DeviceType();

		List<in.co.rssoftware.bbps.schema.DeviceType.Tag> tags = deviceType.getTags();

		for (int i = 0; i < tags.size(); i++) {
			in.co.rssoftware.bbps.schema.DeviceType.Tag tag = tags.get(i);
			DeviceTagNameType deviceTagNameType = tag.getName();
			if (deviceTagNameType != null) {
				String val = deviceTagNameType.value();
				org.bbps.schema.DeviceType.Tag tmpTag = new org.bbps.schema.DeviceType.Tag();
				tmpTag.setName(org.bbps.schema.DeviceTagNameType.fromValue(val));
				tmpTag.setValue(tag.getValue());
				deviceType2.getTags().add(tmpTag);
			}
		}
		agentType2.setDevice(deviceType2);
		request.setAgent(agentType2);

		org.bbps.schema.CustomerDtlsType billDtlsType = new org.bbps.schema.CustomerDtlsType();

		CustomerDtlsType customerDtlsType = fetchRequest.getCustomer();
		// billDtlsType.setCorporateId(customerDtlsType.getCorporateId());
		billDtlsType.setMobile(customerDtlsType.getMobile());
		List<CustomerDtlsType.Tag> list = customerDtlsType.getTags();
		for (int j = 0; j < list.size(); j++) {
			CustomerDtlsType.Tag tag3 = list.get(j);
			String name = tag3.getName();
			String value = tag3.getValue();
			org.bbps.schema.CustomerDtlsType.Tag tmpTag1 = new org.bbps.schema.CustomerDtlsType.Tag();
			tmpTag1.setName(name);
			tmpTag1.setValue(value);
			billDtlsType.getTags().add(tmpTag1);
		}
		request.setCustomer(billDtlsType);
		BillDetailsType billDetailsType = fetchRequest.getBillDetails();
		BillerType billerType = billDetailsType.getBiller();
		org.bbps.schema.BillDetailsType billDetailsType2 = new org.bbps.schema.BillDetailsType();
		org.bbps.schema.BillerType billerType2 = new org.bbps.schema.BillerType();
		billerType2.setId(billerType.getId());
		billDetailsType2.setBiller(billerType2);

		CustomerParamsType customerParamsType = billDetailsType.getCustomerParams();
		org.bbps.schema.CustomerParamsType customerParamsType2 = new org.bbps.schema.CustomerParamsType();
		List<CustomerParamsType.Tag> tags4list = customerParamsType.getTags();

		for (int k = 0; k < tags4list.size(); k++) {
			CustomerParamsType.Tag tag5 = tags4list.get(k);
			org.bbps.schema.CustomerParamsType.Tag tagtemp5 = new org.bbps.schema.CustomerParamsType.Tag();
			tagtemp5.setName(tag5.getName());
			tagtemp5.setValue(tag5.getValue());
			customerParamsType2.getTags().add(tagtemp5);
		}
		billDetailsType2.setCustomerParams(customerParamsType2);

		request.setBillDetails(billDetailsType2);

		RiskScoresType riskScoresType = fetchRequest.getRiskScores();
		TxnType txn = new TxnType();
		String msgId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_MSG_ID, fetchRequest.getOrigInst());
		if (riskScoresType != null) {
			List<RiskScoresType.Score> scoreList = riskScoresType.getScores();
			txn.setTs(CommonUtils.getFormattedCurrentTimestamp());

			org.bbps.schema.RiskScoresType riskScore = new org.bbps.schema.RiskScoresType();
			for (int m = 0; m < scoreList.size(); m++) {
				RiskScoresType.Score score = scoreList.get(m);

				txn.setMsgId(msgId);

				org.bbps.schema.RiskScoresType.Score score2 = new org.bbps.schema.RiskScoresType.Score();
				score2.setProvider(score.getProvider());
				score2.setType(score.getType());
				score2.setValue(score.getValue()); // populate a default score
													// now
				riskScore.getScores().add(score2);
				txn.setRiskScores(riskScore);
			}
		} else {
			txn  = RequestResponseGenerator.getTxn(fetchRequest.getOrigInst(), msgId);
		}

		request.setTxn(txn);

		HeadType head = new HeadType();
		head.setVer("1.0");
		head.setTs(CommonUtils.getFormattedCurrentTimestamp());
		head.setOrigInst(tenantDetailService.getOUName(TransactionContext.getTenantId()));
		head.setRefId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, fetchRequest.getOrigInst()));
		request.setHead(head);

		AnalyticsType analytics = new AnalyticsType();
		AnalyticsType.Tag fetchRequestStartTag = new AnalyticsType.Tag();
		fetchRequestStartTag.setName("FETCHREQUESTSTART");
		fetchRequestStartTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(fetchRequestStartTag);

		AnalyticsType.Tag fetchRequestEndTag = new AnalyticsType.Tag();
		fetchRequestEndTag.setName("FETCHREQUESTEND");
		fetchRequestEndTag.setValue(CommonUtils.getFormattedCurrentTimestamp());
		analytics.getTags().add(fetchRequestEndTag);
		request.setAnalytics(analytics);

		return request;

	}

	FetchResponse createFetchResponse(BillFetchResponse billFetchResponse) {
		logger.info("******************** BillFetchResponse: from CU to COU  ***************************");
		LogUtils.logReqRespMessage(billFetchResponse, null, null);
		logger.info("******************** BillFetchResponse ***************************");

		FetchResponse fetchResponse = new FetchResponse();

		fetchResponse.setRefId(billFetchResponse.getHead().getRefId());
		// fetchResponse.set
		ReasonType reasonType = billFetchResponse.getReason();
		in.co.rssoftware.bbps.schema.ReasonType reasonType2 = new in.co.rssoftware.bbps.schema.ReasonType();
		reasonType2.setApprovalRefNum(reasonType.getApprovalRefNum());
		reasonType2.setComplianceReason(reasonType.getComplianceReason());
		reasonType2.setComplianceRespCd(reasonType.getComplianceRespCd());
		reasonType2.setResponseCode(reasonType.getResponseCode());
		reasonType2.setResponseReason(reasonType.getResponseReason());
		reasonType2.setValue(reasonType.getValue());
		fetchResponse.setReason(reasonType2);

		BillerResponseType billerResponseType = billFetchResponse.getBillerResponse();
		in.co.rssoftware.bbps.schema.BillerResponseType billerResponseType2 = new in.co.rssoftware.bbps.schema.BillerResponseType();
		billerResponseType2.setAmount(billerResponseType.getAmount());
		billerResponseType2.setBillDate(billerResponseType.getBillDate());
		billerResponseType2.setBillNumber(billerResponseType.getBillNumber());
		billerResponseType2.setBillPeriod(billerResponseType.getBillPeriod());
		billerResponseType2.setCustConvDesc(billerResponseType.getCustConvDesc());
		billerResponseType2.setCustConvFee(billerResponseType.getCustConvFee());
		billerResponseType2.setCustomerName(billerResponseType.getCustomerName());
		billerResponseType2.setDueDate(billerResponseType.getDueDate());

		List<BillerResponseType.Tag> listTags = billerResponseType.getTags();
		for (int i = 0; i < listTags.size(); i++) {
			BillerResponseType.Tag temptag = listTags.get(i);
			in.co.rssoftware.bbps.schema.BillerResponseType.Tag tag2 = new in.co.rssoftware.bbps.schema.BillerResponseType.Tag();
			tag2.setName(temptag.getName());
			tag2.setValue(temptag.getValue());
			billerResponseType2.getTags().add(tag2);
		}

		fetchResponse.setBillerResponse(billerResponseType2);
		AdditionalInfoType additionalInfoType = billFetchResponse.getAdditionalInfo();
		if(additionalInfoType == null)
			additionalInfoType = new AdditionalInfoType();
		in.co.rssoftware.bbps.schema.AdditionalInfoType additionalInfoType2 = new in.co.rssoftware.bbps.schema.AdditionalInfoType();
		List<AdditionalInfoType.Tag> listTags1 = additionalInfoType.getTags();
		for (int i = 0; i < listTags1.size(); i++) {
			AdditionalInfoType.Tag temptag1 = listTags1.get(i);
			in.co.rssoftware.bbps.schema.AdditionalInfoType.Tag tag3 = new in.co.rssoftware.bbps.schema.AdditionalInfoType.Tag();
			tag3.setName(temptag1.getName());
			tag3.setValue(temptag1.getValue());
			additionalInfoType2.getTags().add(tag3);
		}
		fetchResponse.setAdditionalInfo(additionalInfoType2);

		return fetchResponse;
	}

	private BillerDetail mapToBillerDetail(BillerView view) {
		BillerDetail billerDetail = new BillerDetail();
		billerDetail.setName(view.getBlrName());
		billerDetail.setMode(view.getBlrMode()!=null?view.getBlrMode().name():null);
		billerDetail.setId(view.getBlrId());
		billerDetail.setAcceptsAdhoc(view.isBlrAcceptsAdhoc());
		billerDetail.setCategoryName(view.getBlrCategoryName());
		billerDetail.setParentBillerId(view.getParentBlrId());
		billerDetail.setIsParent(view.isParentBlr());
		billerDetail.setFetchRequirement(view.getFetchRequirement());
		return billerDetail;
	}

	private BillFetchCustomParams mapTo(List<ParamConfig> custParams) {
		BillFetchCustomParams billerFetch = new BillFetchCustomParams();

		List<BillFetchCustomParams.CustomParams> customParams = billerFetch.getCustomParams();
		for (ParamConfig pc : custParams) {
			BillFetchCustomParams.CustomParams params = new BillFetchCustomParams.CustomParams();
			if (pc.getOptional() != null) {
				params.setIsMandatory(!pc.getOptional());
			}
			params.setName(pc.getParamName());
			params.setType(pc.getDataType().toString());
			customParams.add(params);
		}

		return billerFetch;
	}

	private ErrorMessage mapTo(org.bbps.schema.ErrorMessage error) {
		ErrorMessage msg = new ErrorMessage();
		msg.setErrorCd(error.getErrorCd());
		msg.setErrorDtl(error.getErrorDtl());
		return msg;
	}
}
