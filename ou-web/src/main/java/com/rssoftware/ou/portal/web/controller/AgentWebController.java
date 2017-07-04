package com.rssoftware.ou.portal.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Image;
import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.utils.WebAPIURL;
import com.rssoftware.ou.portal.web.modal.BillFetchResponse;
import com.rssoftware.ou.portal.web.utils.CreatePDF;
import com.rssoftware.ou.portal.web.utils.UIUtils;
import com.rssoftware.ou.portal.web.utils.Utility;
import com.rssoftware.ou.tenant.service.SmsService;

import in.co.rssoftware.bbps.schema.AgentDetail;
import in.co.rssoftware.bbps.schema.AgentType;
import in.co.rssoftware.bbps.schema.BillDetailsType;
import in.co.rssoftware.bbps.schema.BillFetchCustomParams;
import in.co.rssoftware.bbps.schema.BillerCatagory;
import in.co.rssoftware.bbps.schema.BillerList;
import in.co.rssoftware.bbps.schema.BillerType;
import in.co.rssoftware.bbps.schema.CCF;
import in.co.rssoftware.bbps.schema.CCFRequest;
import in.co.rssoftware.bbps.schema.CustomerDtlsType;
import in.co.rssoftware.bbps.schema.CustomerParamsType;
import in.co.rssoftware.bbps.schema.DeviceTagNameType;
import in.co.rssoftware.bbps.schema.DeviceType;
import in.co.rssoftware.bbps.schema.FetchBill;
import in.co.rssoftware.bbps.schema.FetchRequest;
import in.co.rssoftware.bbps.schema.FetchResponse;
import in.co.rssoftware.bbps.schema.PaymentChannels;
import in.co.rssoftware.bbps.schema.PaymentInformation;
import in.co.rssoftware.bbps.schema.PaymentReceipt;
import in.co.rssoftware.bbps.schema.PmtMtdType;
import in.co.rssoftware.bbps.schema.QckPayType;
import in.co.rssoftware.bbps.schema.SpltPayType;
import in.co.rssoftware.bbps.schema.TxnDetailType;
import in.co.rssoftware.bbps.schema.TxnSearchRequest;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

@Controller
@RequestMapping("/AgentService")
public class AgentWebController {

	@Value("${ou.tenantId}")
	private String tenantId;

	@Value("${ou.domain}")
	private String uri;
	
	@Autowired
	private SmsService smsService;

	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();

	

	@RequestMapping("/agentLogin")
	public String agentLogin(Model model, HttpServletRequest request) {

		String url = "https://localhost:9090/APIService/biller-list/urn:tenantId:OU23?billerCategory=Telecom";
		return "agentLogin";
	}

	@RequestMapping("/agentRecovery")
	public String agentRecovery(Model model, HttpServletRequest request) {

		TransactionContext.putTenantId(tenantId);
		return "agentRecoverpage";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/invokeBillFetchPage")
	public String invokeBillFetchPage(Model model, HttpServletRequest request) {
		List<String> billerCategoryLists = new ArrayList<String>();
		try {
			
			TransactionContext.putTenantId(tenantId);			
			
			/*edited by Somnath at 18/01/2017*/
			PaymentInformation info=new PaymentInformation();
			info.setPaymentChannel("Internet_Banking");
			ResponseEntity responseEntity = internalRestTemplate.exchange((uri + WebAPIURL.FETCH_DEFAULT_AGENT_URL), HttpMethod.POST, Utility.getHttpEntityForPost(info), AgentDetail.class, tenantId);
			AgentDetail defaultAgent = (AgentDetail) responseEntity.getBody();
			model.addAttribute("agentId", defaultAgent.getAgentID());
			String paymentmode = "";
			boolean isFirst = true;
			for (in.co.rssoftware.bbps.schema.PaymentModeLimit mode : defaultAgent.getAgentPaymentModes()) {
				if (mode != null) {

					if (isFirst) {
						isFirst = false;
						paymentmode += mode.getPaymentMode();
					} else {
						paymentmode += "," + mode.getPaymentMode();
					}

				}
			}

			System.out.println("Payment Modes---------" + paymentmode);
			 info=new PaymentInformation();
			info.setPaymentChannel("INTB");
			info.setPaymentMode(paymentmode);
			/*responseEntity = internalRestTemplate.exchange(
					uri + (WebAPIURL.BILLER_CATEGORY_URL ),
					HttpMethod.GET, Utility.getHttpEntityForGet(), BillerCatagory.class, tenantId,"","");*/
			
			/*edited by Somnath at 18/01/2017*/
			responseEntity = internalRestTemplate.exchange(
					uri + (WebAPIURL.BILLER_CATEGORY_URL ),
					HttpMethod.POST, Utility.getHttpEntityForPost(info), BillerCatagory.class, tenantId);
			BillerCatagory billerCatagory = (BillerCatagory) responseEntity.getBody();
			billerCategoryLists = billerCatagory.getCatagories();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		model.addAttribute("BillerCategoryLists", billerCategoryLists);

		return "billFetchPayment";
	}

	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchBillerList/{category}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String fetchBillerlist(Model model, HttpServletRequest request,
		 @PathVariable String category) {
		//System.out.println("category Is "+ category);
		
		BillerList billerList = null;
		try {
			// String url =
			// "https://localhost:9090/APIService/biller-list/urn:tenantId:OU23?billerCategory="+selectedcategory;
			TransactionContext.putTenantId(tenantId);
			String url = uri + WebAPIURL.FETCH_BILLER_URL ;
			ResponseEntity<BillerList> responseEntity = (ResponseEntity<BillerList>) internalRestTemplate.exchange(url,
					HttpMethod.GET, Utility.getHttpEntityForGet(), BillerList.class, tenantId,category,"","");
			billerList = (BillerList) responseEntity.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return UIUtils.getAllBillersDropdown(billerList);
	}*/
	
	
	
	
/*	edited by Somnath at 18/01/2017*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchBillerList", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String fetchBillerlist(Model model, HttpServletRequest request,
		 @RequestBody String category) {
		
		
		BillerList billerList = null;
		try {
			// String url =
			// "https://localhost:9090/APIService/biller-list/urn:tenantId:OU23?billerCategory="+selectedcategory;
			TransactionContext.putTenantId(tenantId);
			String url = uri + WebAPIURL.FETCH_BILLER_URL ;
			PaymentInformation paymentInformation = new PaymentInformation();
			paymentInformation.setCategory(category);
			ResponseEntity<BillerList> responseEntity = (ResponseEntity<BillerList>) internalRestTemplate.exchange(url,
					HttpMethod.POST, Utility.getHttpEntityForPost(paymentInformation), BillerList.class, tenantId);
			billerList = (BillerList) responseEntity.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return UIUtils.getAllBillersDropdown(billerList);
	}
	
	/*@RequestMapping(value = "/fetchSubBillerList/{billerId}", method = RequestMethod.POST,produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String getSubBillerList(Model model, HttpServletRequest request, @PathVariable String billerId)
			throws IOException {
		BillerList billerList = null;
		try {
			// String url =
			// "https://localhost:9090/APIService/sub-biller-list/urn:tenantId:OU09?billerId="+selectedbillerId;

			TransactionContext.putTenantId(tenantId);
			String url = uri + WebAPIURL.FETCH_SUB_BILLER_URL  ;
			ResponseEntity<BillerList> responseEntity = (ResponseEntity<BillerList>) internalRestTemplate.exchange(url,
					HttpMethod.GET, Utility.getHttpEntityForGet(), BillerList.class, tenantId,billerId);
			billerList = (BillerList) responseEntity.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return UIUtils.getAllBillersDropdown(billerList);
	}
	*/
	
	
	
	
	/*edited by Somnath at 18/01/2017*/
	@RequestMapping(value = "/fetchSubBillerList", method = RequestMethod.POST,produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody String getSubBillerList(Model model, HttpServletRequest request, @RequestBody String billerId)
			throws IOException {
		BillerList billerList = null;
		try {
			// String url =
			// "https://localhost:9090/APIService/sub-biller-list/urn:tenantId:OU09?billerId="+selectedbillerId;
			
			TransactionContext.putTenantId(tenantId);
			String url = uri + WebAPIURL.FETCH_SUB_BILLER_URL  ;
			PaymentInformation paymentInformation = new PaymentInformation();
			paymentInformation.setBillerId(billerId);
			ResponseEntity<BillerList> responseEntity = (ResponseEntity<BillerList>) internalRestTemplate.exchange(url,
					HttpMethod.POST, Utility.getHttpEntityForPost(paymentInformation), BillerList.class, tenantId);
			billerList = (BillerList) responseEntity.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return UIUtils.getAllBillersDropdown(billerList);
	}

	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchCustomParam/{billerId}", method = RequestMethod.POST)
	public @ResponseBody String fetchCustomParams(Model model, HttpServletRequest request,
			@PathVariable String billerId) {
		ObjectMapper mapper = new ObjectMapper();
		String res = "";
		ResponseEntity resp = null;
		try {
			resp = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			res = mapper.writeValueAsString(resp);
			// String url =
			// "https://localhost:9090/APIService/biller-list/urn:tenantId:OU23?billerCategory="+selectedcategory;
			TransactionContext.putTenantId(tenantId);
			String url = uri + WebAPIURL.FETCH_CUSTOM_PARAM_URL ;
			resp = internalRestTemplate.exchange(url, HttpMethod.GET, Utility.getHttpEntityForGet(),
					BillFetchCustomParams.class, tenantId,billerId);
			res = mapper.writeValueAsString(resp);
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return res;
	}*/
	
	
	/*edited by Somnath at 19/01/2017*/
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fetchCustomParam", method = RequestMethod.POST)
	public @ResponseBody String fetchCustomParams(Model model, HttpServletRequest request,
			@RequestBody String billerId) {
		ObjectMapper mapper = new ObjectMapper();
		String res = "";
		ResponseEntity resp = null;
		try {
			resp = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			res = mapper.writeValueAsString(resp);
			// String url =
			// "https://localhost:9090/APIService/biller-list/urn:tenantId:OU23?billerCategory="+selectedcategory;
			TransactionContext.putTenantId(tenantId);
			PaymentInformation info=new PaymentInformation();
			info.setBillerId(billerId);
			String url = uri + WebAPIURL.FETCH_CUSTOM_PARAM_URL ;
			resp = internalRestTemplate.exchange(url, HttpMethod.POST, Utility.getHttpEntityForPost(info),
					BillFetchCustomParams.class, tenantId);
			res = mapper.writeValueAsString(resp);
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return res;
	}

	/*@RequestMapping(value = "/calculateCCF/{billerId}/{amount}/{paymentmode}/{agentId}/{paymentChannel}",method = RequestMethod.POST)
	public @ResponseBody String calculateCCF(Model model, HttpServletRequest request, @PathVariable String billerId,
			@PathVariable String amount, @PathVariable String paymentmode, @PathVariable String agentId,
			@PathVariable String paymentChannel) {
		ObjectMapper mapper = new ObjectMapper();
		String res = "";
		ResponseEntity resp = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

		try {
			res = mapper.writeValueAsString(resp);
			CCFRequest ccfRequest = new CCFRequest();
			// to do logged in agent details Agent Details
			AgentType agentType = new AgentType();
			agentType.setId(agentId);
			DeviceType deviceType = new DeviceType();
			DeviceType.Tag deviceTypeTag1 = new DeviceType.Tag();
			deviceTypeTag1.setName(DeviceTagNameType.IP);
			deviceTypeTag1.setValue("124.170.23.28");
			deviceType.getTags().add(deviceTypeTag1);
			DeviceType.Tag deviceTypeTag2 = new DeviceType.Tag();
			deviceTypeTag2.setName(DeviceTagNameType.INITIATING_CHANNEL);
			deviceTypeTag2.setValue(paymentChannel);
			deviceType.getTags().add(deviceTypeTag2);
			DeviceType.Tag deviceTypeTag3 = new DeviceType.Tag();
			deviceTypeTag3.setName(DeviceTagNameType.MAC);
			deviceTypeTag3.setValue("01-23-45-67-89-ab");
			deviceType.getTags().add(deviceTypeTag3);
			agentType.setDevice(deviceType);
			ccfRequest.setAgent(agentType);
			ccfRequest.setBillAmount(amount);
			ccfRequest.setBillerId(billerId);
			PmtMtdType mode = new PmtMtdType();
			mode.setPaymentMode(paymentmode);
			mode.setQuickPay(QckPayType.NO);
			mode.setSplitPay(SpltPayType.NO);
			ccfRequest.setPaymentMethod(mode);

			// String url =
			// "https://localhost:9090/APIService/biller-list/urn:tenantId:OU23?billerCategory="+selectedcategory;
			TransactionContext.putTenantId(tenantId);
			String url = uri + WebAPIURL.CALCULATE_CCF_URL;
			resp = internalRestTemplate.exchange(url, HttpMethod.POST, Utility.getHttpEntityForPost(ccfRequest),
					String.class, tenantId);
			res = mapper.writeValueAsString(resp);
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return res;
	}
*/
	
	
	
	@RequestMapping(value = "/calculateCCF",method = RequestMethod.POST)
	public @ResponseBody String calculateCCF(Model model, HttpServletRequest request,@RequestBody CCF ccf) {
		ObjectMapper mapper = new ObjectMapper();
		String res = "";
		ResponseEntity resp = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		
		try {
			String billerId=ccf.getBillerId();
			String amount=ccf.getBillAmount();
			String paymentmode=ccf.getPaymentMode();
			String agentId=ccf.getAgentId();
			String paymentChannel=ccf.getPayMentChannel();
			res = mapper.writeValueAsString(resp);
			CCFRequest ccfRequest = new CCFRequest();
			// to do logged in agent details Agent Details
			AgentType agentType = new AgentType();
			agentType.setId(agentId);
			DeviceType deviceType = new DeviceType();
			DeviceType.Tag deviceTypeTag1 = new DeviceType.Tag();
			deviceTypeTag1.setName(DeviceTagNameType.IP);
			deviceTypeTag1.setValue("124.170.23.28");
			deviceType.getTags().add(deviceTypeTag1);
			DeviceType.Tag deviceTypeTag2 = new DeviceType.Tag();
			deviceTypeTag2.setName(DeviceTagNameType.INITIATING_CHANNEL);
			deviceTypeTag2.setValue(paymentChannel);
			deviceType.getTags().add(deviceTypeTag2);
			DeviceType.Tag deviceTypeTag3 = new DeviceType.Tag();
			deviceTypeTag3.setName(DeviceTagNameType.MAC);
			deviceTypeTag3.setValue("01-23-45-67-89-ab");
			deviceType.getTags().add(deviceTypeTag3);
			agentType.setDevice(deviceType);
			ccfRequest.setAgent(agentType);
			ccfRequest.setBillAmount(amount);
			ccfRequest.setBillerId(billerId);
			PmtMtdType mode = new PmtMtdType();
			mode.setPaymentMode(paymentmode);
			mode.setQuickPay(QckPayType.NO);
			mode.setSplitPay(SpltPayType.NO);
			ccfRequest.setPaymentMethod(mode);

			// String url =
			// "https://localhost:9090/APIService/biller-list/urn:tenantId:OU23?billerCategory="+selectedcategory;
			TransactionContext.putTenantId(tenantId);
			String url = uri + WebAPIURL.CALCULATE_CCF_URL;
			resp = internalRestTemplate.exchange(url, HttpMethod.POST, Utility.getHttpEntityForPost(ccfRequest),
					String.class, tenantId);
			res = mapper.writeValueAsString(resp);
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return res;
	}

	
	/*@SuppressWarnings("rawtypes")
	@RequestMapping("/fetchBill/{billerId}/{mobNo}/{custumparam}/{agentId}/{paymentChannel}")
	public @ResponseBody String fetchBill(Model model, HttpServletRequest request,
			@PathVariable String billerId, @PathVariable(value = "mobNo") String mobNo,
			@PathVariable String custumparam, @PathVariable String agentId,
			@PathVariable String paymentChannel) {

		paymentChannel = "INT";
		String resp = null;
		ObjectMapper mapper = new ObjectMapper();
		BillFetchResponse billFetchResponse = new BillFetchResponse();
		try {
			TransactionContext.putTenantId(tenantId);

			FetchRequest fetchRequest = new FetchRequest();
			fetchRequest.setOrigInst(tenantId);

			// Customer Details
			CustomerDtlsType customerDetailsType = new CustomerDtlsType();
			customerDetailsType.setMobile(mobNo);
			fetchRequest.setCustomer(customerDetailsType);

			// To Do logged in agent detail other wise fetch dummy agent details
			AgentType agentType = new AgentType();
			agentType.setId(agentId);
			DeviceType deviceType = new DeviceType();
			DeviceType.Tag deviceTypeTag1 = new DeviceType.Tag();
			deviceTypeTag1.setName(DeviceTagNameType.IP);
			deviceTypeTag1.setValue("172.17.18.196");
			deviceType.getTags().add(deviceTypeTag1);
			DeviceType.Tag deviceTypeTag2 = new DeviceType.Tag();
			deviceTypeTag2.setName(DeviceTagNameType.INITIATING_CHANNEL);
			deviceTypeTag2.setValue(paymentChannel);
			deviceType.getTags().add(deviceTypeTag2);
			DeviceType.Tag deviceTypeTag3 = new DeviceType.Tag();
			deviceTypeTag3.setName(DeviceTagNameType.MAC);
			deviceTypeTag3.setValue("01-23-45-67-89-ab");
			deviceType.getTags().add(deviceTypeTag3);
			agentType.setDevice(deviceType);
			fetchRequest.setAgent(agentType);

			// Bill Details
			BillDetailsType billDetailsType = new BillDetailsType();
			BillerType billerType = new BillerType();
			billerType.setId(billerId);
			billDetailsType.setBiller(billerType);
			String[] params = custumparam.split("@");
			CustomerParamsType customerParamsType = new CustomerParamsType();
			for (String singleparam : params) {
				String[] paramsvlaue = singleparam.split(":");
				if (!paramsvlaue[1].isEmpty()) {
					CustomerParamsType.Tag customerParamsTypeTag2 = new CustomerParamsType.Tag();
					customerParamsTypeTag2.setName(paramsvlaue[0]);
					customerParamsTypeTag2.setValue(paramsvlaue[1]);
					customerParamsType.getTags().add(customerParamsTypeTag2);
				}

			}
			
			billDetailsType.setCustomerParams(customerParamsType);
			fetchRequest.setBillDetails(billDetailsType);

			// String url =
			// "https://localhost:9090/APIService/bill-fetch-form-post/urn:tenantId:OU23";
			String url = uri + WebAPIURL.BILL_FETCH_URL;
			ResponseEntity responseEntity = internalRestTemplate.exchange(url, HttpMethod.POST,
					Utility.getHttpEntityForPost(fetchRequest), FetchResponse.class, tenantId);
			FetchResponse fetchResponse = (FetchResponse) responseEntity.getBody();
			if (fetchResponse != null && fetchResponse.getErrorMessages().size() == 0) {
				BigDecimal amount = new BigDecimal(fetchResponse.getBillerResponse().getAmount())
						.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
				fetchResponse.getBillerResponse().setAmount(amount + "");
			}
			billFetchResponse.setFetchResponse(fetchResponse);

//			responseEntity = internalRestTemplate.exchange((uri + WebAPIURL.FETCH_BILLER_DETAILS_URL),
//					HttpMethod.GET, Utility.getHttpEntityForGet(), BillerView.class, tenantId, billerId);
//			BillerView billerView = (BillerView) responseEntity.getBody();
			String amountOption = UIUtils.getBillerAmountOptions( fetchResponse);
			billFetchResponse.setAmountOption(amountOption);
			resp = mapper.writeValueAsString(billFetchResponse);
		} catch (Exception ex) {
			ex.printStackTrace();
		
		}
		return resp;
	}*/
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/fetchBill", method = RequestMethod.POST)
	public @ResponseBody String fetchBill(Model model, HttpServletRequest request,
			@RequestBody FetchBill fetch) {

		String paymentChannel = fetch.getPaymentChannel();
		String agentId=fetch.getAgentId();
		String custumparam=fetch.getCustumparam();
		String mobNo=fetch.getMobNo();
		String billerId=fetch.getBillerId();
		String resp = null;
		ObjectMapper mapper = new ObjectMapper();
		BillFetchResponse billFetchResponse = new BillFetchResponse();
		try {
			TransactionContext.putTenantId(tenantId);

			FetchRequest fetchRequest = new FetchRequest();
			fetchRequest.setOrigInst(tenantId);

			// Customer Details
			CustomerDtlsType customerDetailsType = new CustomerDtlsType();
			customerDetailsType.setMobile(mobNo);
			fetchRequest.setCustomer(customerDetailsType);

			// To Do logged in agent detail other wise fetch dummy agent details
			AgentType agentType = new AgentType();
			agentType.setId(agentId);
			DeviceType deviceType = new DeviceType();
			DeviceType.Tag deviceTypeTag1 = new DeviceType.Tag();
			deviceTypeTag1.setName(DeviceTagNameType.IP);
			deviceTypeTag1.setValue("172.17.18.196");
			deviceType.getTags().add(deviceTypeTag1);
			DeviceType.Tag deviceTypeTag2 = new DeviceType.Tag();
			deviceTypeTag2.setName(DeviceTagNameType.INITIATING_CHANNEL);
			deviceTypeTag2.setValue(paymentChannel);
			deviceType.getTags().add(deviceTypeTag2);
			DeviceType.Tag deviceTypeTag3 = new DeviceType.Tag();
			deviceTypeTag3.setName(DeviceTagNameType.MAC);
			deviceTypeTag3.setValue("01-23-45-67-89-ab");
			deviceType.getTags().add(deviceTypeTag3);
			agentType.setDevice(deviceType);
			fetchRequest.setAgent(agentType);

			// Bill Details
			BillDetailsType billDetailsType = new BillDetailsType();
			BillerType billerType = new BillerType();
			billerType.setId(billerId);
			billDetailsType.setBiller(billerType);
			String[] params = custumparam.split("@");
			CustomerParamsType customerParamsType = new CustomerParamsType();
			for (String singleparam : params) {
				String[] paramsvlaue = singleparam.split(":");
				if (!paramsvlaue[1].isEmpty()) {
					CustomerParamsType.Tag customerParamsTypeTag2 = new CustomerParamsType.Tag();
					customerParamsTypeTag2.setName(paramsvlaue[0]);
					customerParamsTypeTag2.setValue(paramsvlaue[1]);
					customerParamsType.getTags().add(customerParamsTypeTag2);
				}

			}
			
			billDetailsType.setCustomerParams(customerParamsType);
			fetchRequest.setBillDetails(billDetailsType);

			// String url =
			// "https://localhost:9090/APIService/bill-fetch-form-post/urn:tenantId:OU23";
			String url = uri + WebAPIURL.BILL_FETCH_URL;
			ResponseEntity responseEntity = internalRestTemplate.exchange(url, HttpMethod.POST,
					Utility.getHttpEntityForPost(fetchRequest), FetchResponse.class, tenantId);
			FetchResponse fetchResponse = (FetchResponse) responseEntity.getBody();
			if (fetchResponse != null && fetchResponse.getErrorMessages().size() == 0) {
				BigDecimal amount = new BigDecimal(fetchResponse.getBillerResponse().getAmount())
						.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
				fetchResponse.getBillerResponse().setAmount(amount + "");
			}
			billFetchResponse.setFetchResponse(fetchResponse);

//			responseEntity = internalRestTemplate.exchange((uri + WebAPIURL.FETCH_BILLER_DETAILS_URL),
//					HttpMethod.GET, Utility.getHttpEntityForGet(), BillerView.class, tenantId, billerId);
//			BillerView billerView = (BillerView) responseEntity.getBody();
			String amountOption = UIUtils.getBillerAmountOptions( fetchResponse);
			billFetchResponse.setAmountOption(amountOption);
			resp = mapper.writeValueAsString(billFetchResponse);
		} catch (Exception ex) {
			ex.printStackTrace();
		
		}
		return resp;
	}

	@RequestMapping(value = "/invokeAgentTransactionsByDate", method = RequestMethod.GET)
	public String invokeAgentTransactionsByDate(Model model, HttpServletRequest request) {
		return "agentTransactionListByDate";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/searchAgentTransactionsByDate", method = RequestMethod.POST)
	public String searchAgentTransactionsByDate(Model model, HttpServletRequest request) {

		try {
			String startDate = request.getParameter("fromDate");
			String endDate = request.getParameter("toDate");
			model.addAttribute("fromDate", startDate);
			model.addAttribute("toDate", endDate);
			startDate = (startDate != null && startDate.length() > 10)
					? convertDateFormat(startDate.substring(0, 10), "yyyy-MM-dd", "dd-MM-yyyy") : "";
			endDate = (endDate != null && endDate.length() > 10)
					? convertDateFormat(endDate.substring(0, 10), "yyyy-MM-dd", "dd-MM-yyyy") : "";
			String tenantId = System.getProperty("tenantId"), agentId = "OU23AB21000001123458";
			TransactionContext.putTenantId(tenantId);

			// String url =
			// "https://localhost:9090/APIService/agents/urn:tenantId:" +
			// tenantId + "/agentTxnSearch/"+ agentId;
			String url = uri + WebAPIURL.AGENT_TXN_SEARCH_BYDATE_URL + agentId;

			TxnSearchRequest txnSearchRequest = new TxnSearchRequest();
			txnSearchRequest.setTxnStartDate(startDate);
			txnSearchRequest.setTxnEndDate(endDate);
			ResponseEntity responseEntity = internalRestTemplate.exchange(url, HttpMethod.POST,
					Utility.getHttpEntityForPost(txnSearchRequest), TxnSearchResponse.class, tenantId);
			TxnSearchResponse txnSearchResponse = (TxnSearchResponse) responseEntity.getBody();
			List<TxnDetailType> txnDetails = txnSearchResponse.getTxnDetails();
			for (TxnDetailType txnDetail : txnDetails) {
				txnDetail.setTxnDate("2016-11-25 15:12:46.049");
			}
			model.addAttribute("txnDetails", txnDetails);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "agentTransactionListByDate";
	}

	@RequestMapping(value = "/invokeAgentTransactions", method = RequestMethod.GET)
	public String invokeAgentTransactions(Model model, HttpServletRequest request) {
		return "agentTransactionList";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/searchAgentTransactions", method = RequestMethod.POST)
	public String searchAgentTransactions(Model model, HttpServletRequest request) {
		try {
			String agentId = request.getParameter("inputAgentID");
//			String tenantId = tenantId;
			TransactionContext.putTenantId(tenantId);
			// String url =
			// "https://localhost:9090/APIService/agents/urn:tenantId:" +
			// tenantId + "/agentTxnHistory/"+ agentId;
			String url = uri + WebAPIURL.AGENT_TXN_SEARCH_BYID_URL + agentId;

			ResponseEntity responseEntity = internalRestTemplate.exchange(uri, HttpMethod.GET,
					Utility.getHttpEntityForGet(), TxnSearchResponse.class, tenantId);
			TxnSearchResponse txnSearchResponse = (TxnSearchResponse) responseEntity.getBody();
			List<TxnDetailType> txnDetails = txnSearchResponse.getTxnDetails();
			for (TxnDetailType txnDetail : txnDetails) {
				txnDetail.setTxnDate("2016-11-25 15:12:46.049");
			}
			model.addAttribute("txnDetails", txnDetails);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "txnSearchByMobile";
	}

	@RequestMapping(value = "/invokeTxnSearchByMobile", method = RequestMethod.GET)
	public String invokeTxnSearchByMobile(Model model, HttpServletRequest request) {
		return "txnSearchByMobile";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/searchTxnSearchByMobile", method = RequestMethod.POST)
	public String searchTxnSearchByMobile(Model model, HttpServletRequest request) {
		try {
			String mobileNo = request.getParameter("inputMobileNo");
//			String tenantId = tenantId;
			TransactionContext.putTenantId(tenantId);
			// String url =
			// "https://localhost:9090/APIService/agents/urn:tenantId:" +
			// tenantId + "/agentTxnByMobile/"+ mobileNo;
			String url = uri + WebAPIURL.AGENT_TXN_SEARCH_BYMOBNO_URL + mobileNo;

			ResponseEntity responseEntity = internalRestTemplate.exchange(url, HttpMethod.GET,
					Utility.getHttpEntityForGet(), TxnSearchResponse.class, tenantId);
			TxnSearchResponse txnSearchResponse = (TxnSearchResponse) responseEntity.getBody();
			List<TxnDetailType> txnDetails = txnSearchResponse.getTxnDetails();
			for (TxnDetailType txnDetail : txnDetails) {
				txnDetail.setTxnDate("2016-11-25 15:12:46.049");
			}
			model.addAttribute("txnDetails", txnDetails);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "txnSearchByMobile";
	}

	@RequestMapping(value = "/AgentService/invokeTxnSearchByRefId", method = RequestMethod.GET)
	public String invokeTxnSearchByRefId(Model model, HttpServletRequest request) {
		return "txnSearchByMobile";
	}

	@RequestMapping(value = "/invokeDuplicatePrintReceipt", method = RequestMethod.GET)
	public String invokeDuplicatePrintReceipt(Model model, HttpServletRequest request) {
		return "duplicatePrintReceipt";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/searchDuplicatePrintReceipt", method = RequestMethod.POST)
	public String searchDuplicatePrintReceipt(Model model, HttpServletRequest request) {
		try {
//			String tenantId = System.getProperty("tenantId");
			TransactionContext.putTenantId(tenantId);
			String txnRefID = request.getParameter("inputTxnID");
			// String url =
			// "https://localhost:9090/APIService/agents/urn:tenantId:" +
			// tenantId + "/agentTxnByMobile/"+ mobileNo;
			String url = uri + WebAPIURL.DUPLICATE_PRINT_RECEIPT_URL + txnRefID;

			ResponseEntity responseEntity = internalRestTemplate.exchange(url, HttpMethod.GET,
					Utility.getHttpEntityForGet(), PaymentReceipt.class, tenantId);
			PaymentReceipt paymentReceipt = (PaymentReceipt) responseEntity.getBody();
			model.addAttribute("paymentReceipt", paymentReceipt);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "duplicatePrintReceipt";
	}

	private String convertDateFormat(String dateStr, String origFmt, String reqFmt) {
		try {
			return new SimpleDateFormat(reqFmt).format(new SimpleDateFormat(origFmt).parse(dateStr));
		} catch (ParseException e) {
			return null;
		}
	}

	@RequestMapping(value = "/downloadReciept/{filename}/{title}/{header}", method = RequestMethod.POST)
	public void downloadPDF(HttpServletRequest request, HttpServletResponse response, @RequestParam MultiValueMap<String, Object> params,
			@PathVariable String filename, @PathVariable String title, @PathVariable String header) {

		// System.out.println("receipt_data ====== " +
		// request.getParameter("receipt_data"));
		TransactionContext.putTenantId(tenantId);
		
//		String receipt_data=receiptDTO.getCustomerName()+"~"
//				          +receiptDTO.getCustomerMobileNumber()+"~"
//				          +receiptDTO.getCustomerAccountNumber()+"~"
//				          +receiptDTO.getCustomerTransactionRefId()+"~"
//				       	  +receiptDTO.getCustomerPaymentChannel()+"~"
//		     			  +receiptDTO.getCustomerPaymentMode()+"~"
//						  +receiptDTO.getCustomerBillDate()+"~"
//				          +receiptDTO.getCustomerBillAmount()+"~"
//				          +receiptDTO.getCustomerConvFee()+"~"
//				          +receiptDTO.getCustomerTotalAmount()+"~"
//				          +receiptDTO.getCustomerDateTime()+"~"
//				          +receiptDTO.getCustomerAuthCode();
		String [] receiptLabel=new String[params.size()];
		String [] receiptdata=new String[params.size()];
		int i=0;
		for(Map.Entry<String , List<Object>> element :params.entrySet())
		{
			receiptLabel[i]=element.getKey();
			receiptdata[i]=element.getValue()==null?"":element.getValue().get(0).toString();
			i++;
		}
		
		final ServletContext servletContext = request.getSession().getServletContext();

		final File tempDirectory = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

		final String temperotyFilePath = tempDirectory.getAbsolutePath();

	

		// System.out.println("receipt_data ==== " + receipt_data);
		String fileName = filename+".pdf";

		response.setContentType("application/pdf");

		response.setHeader("Content-disposition", "attachment; filename=" + fileName);

		List<Image> images = new ArrayList<>();

		try {

			Image bbpouImage = Image.getInstance(getPNGPhoto("bbpou-recept-logo.png"));
			images.add(bbpouImage);
			Image bbpsImage = Image.getInstance(getPNGPhoto("BBPS-recept-logo.png"));
			images.add(bbpsImage);

			// System.out.println("real path ==== " + contextPath);

			CreatePDF.createPDF(temperotyFilePath + "\\" + fileName,receiptLabel, receiptdata, images,title,header);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			baos = convertPDFToByteArrayOutputStream(temperotyFilePath + "\\" + fileName);

			OutputStream os = response.getOutputStream();

			baos.writeTo(os);

			os.flush();

		} catch (Exception e1) {

			e1.printStackTrace();

		}

	}
	
	private byte[] getPNGPhoto(String photoName)
	{
		File file;
		byte[] images=null;
		try {
			file = new ClassPathResource("static/images/"+photoName).getFile();
			images= Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return images;
	}

	private ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName) {

		InputStream inputStream = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {

			inputStream = new FileInputStream(fileName);

			byte[] buffer = new byte[1024];

			baos = new ByteArrayOutputStream();

			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) != -1) {

				baos.write(buffer, 0, bytesRead);

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			if (inputStream != null) {

				try {

					inputStream.close();

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

		}

		return baos;

	}

}

