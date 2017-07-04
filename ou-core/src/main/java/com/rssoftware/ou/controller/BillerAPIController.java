package com.rssoftware.ou.controller;

import in.co.rssoftware.bbps.schema.InterchangeFeeConfigSubmitRequest;
import in.co.rssoftware.bbps.schema.InterchangeFeeSubmitRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.bbps.schema.Ack;
import org.bbps.schema.BillFetchResponse;
import org.bbps.schema.BillPaymentResponse;
import org.bbps.schema.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import reactor.bus.Event;
import reactor.bus.EventBus;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.common.MTI;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.domain.JsonResponse;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.domain.Response;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;
import com.rssoftware.ou.tenant.service.impl.BillerServiceImpl;
import com.rssoftware.ou.validator.Interchangefeevalidator;

@RestController
@Scope(value = WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/APIService/BillerOU") 
public class BillerAPIController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private String authLetter1 = "";
	private byte[] authLetterScanCopy1 = null;
	private String authLetter2 = "";
	private byte[] authLetterScanCopy2 = null;
	private String authLetter3 = "";
	private byte[] authLetterScanCopy3 = null;
	
	@Autowired
	private EventBus eventBus;

	@Autowired
	private BillerService billerService;
	
	@Autowired
	InterchangeFeeService interchangeFeeService;
	
	@Autowired
	InterchangeFeeConfService interchangeFeeConfService;
	
	@Autowired
	Interchangefeevalidator interchangefeevalidator;
	
	
	@RequestMapping(value = "/urn:tenantId:{tenantId}/getBillers", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getAllBillers(HttpServletRequest request, @PathVariable String tenantId	) {

		String METHOD_NAME = "getAllBillers";
		if (logger.isDebugEnabled()) {
			logger.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			
			List<BillerView> billerViews = billerService.getActiveBillers();
			if (billerViews != null) {
				response = new ResponseEntity(BillerServiceImpl.getBillerJaxb(billerViews), HttpStatus.OK);
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd("Biller_LIST_NOT_FOUND");
				errorMessage.setErrorDtl("Biller List could not be found!");
				response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	@RequestMapping(value = "/urn:tenantId:{tenantId}/getBillerDetails/{billerId}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getBillerDetails(HttpServletRequest request, @PathVariable String tenantId, @PathVariable String billerId	) {

		String METHOD_NAME = "getBillerDetails";
		if (logger.isDebugEnabled()) {
			logger.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		ResponseEntity<?> response = null;
		try {
			
			BillerView billerView = billerService.getBillerById(billerId);
			if (billerView != null) {
				response = new ResponseEntity(billerView, HttpStatus.OK);
			} else {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setErrorCd("Bille_NOT_FOUND");
				errorMessage.setErrorDtl("Biller  could not be found!");
				response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCd("EXCEPTION");
			errorMessage.setErrorDtl(e.getMessage());
			response = new ResponseEntity(errorMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/urn:tenantId:{tenantId}/process_bill_fetch_response", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)	
	public @ResponseBody Ack processBillFetchResponse(HttpServletRequest request, @RequestBody BillFetchResponse billFetchResponse, @PathVariable String tenantId, @RequestParam String referenceId) {
		
		logger.info("in process_bill_fetch_response");
				
		TransactionContext.putTenantId(tenantId);
		
		Ack ack = getAck(referenceId, Action.FETCH_RESPONSE);
		
		Response response = getResponse(request, tenantId, Action.FETCH_RESPONSE);
		response.setBillFetchResponse(billFetchResponse);
		eventBus.notify(CommonConstants.OU_BILL_FETCH_EVENT, Event.wrap(response));

		ack.setRefId(billFetchResponse.getHead().getRefId());
		ack.setMsgId(billFetchResponse.getTxn().getMsgId());
		ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		return ack;

	}
	
	@RequestMapping(value = "/urn:tenantId:{tenantId}/process_bill_pay_response", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)	
	public @ResponseBody Ack processBillPayResponse(HttpServletRequest request, @RequestBody BillPaymentResponse billPaymentResponse, @PathVariable String tenantId, @RequestParam String referenceId) {
		
		//logger.info("[BillerAPIController::processBillPayResponse]");
		//LogUtils.logReqRespMessage(billPaymentResponse, null, null);
		
		TransactionContext.putTenantId(tenantId);
		
		Ack ack = getAck(referenceId, Action.PAYMENT_RESPONSE);
		
		Response response = getResponse(request, tenantId, Action.PAYMENT_RESPONSE);
		response.setBillPaymentResponse(billPaymentResponse);
		eventBus.notify(CommonConstants.OU_BILL_FETCH_EVENT, Event.wrap(response));

		ack.setRefId(billPaymentResponse.getHead().getRefId());
		ack.setMsgId(billPaymentResponse.getTxn().getMsgId());
		ack.setRspCd(CommonConstants.RESP_SUCCESS_MSG);
		return ack;

	}
	
	private Ack getAck(String referenceId, Action action) {
		Ack ack = new Ack();
		ack.setApi(action.name());
		ack.setTs(CommonUtils.getFormattedCurrentTimestamp());
		ack.setRefId(referenceId);
		return ack;
	}
	
	private Response getResponse(HttpServletRequest request, String tenantId, Action action) {
		Response response = new Response();
		response.setNodeAddress(CommonUtils.getServerNameWithPort());
		response.setAction(action);
		response.setTenantId(tenantId);
		return response;
	}
	
	private void setAckWithErrorMessage(Ack ack, ValidationException ve) {
		ack.setRspCd(CommonConstants.RESP_FAILURE_MSG);
		ack.getErrorMessages().add(new ErrorMessage());
		ack.getErrorMessages().get(0).setErrorCd(ve.getCode());
		ack.getErrorMessages().get(0).setErrorDtl(ve.getDescription());
	}

	@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/interchangefee", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public InterchangeFeeSubmitRequest insertInterchangeFee(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestBody InterchangeFeeSubmitRequest interchangeFee) {
		logger.info("in insertInterchangeFee method");
		
		TransactionContext.putTenantId(tenantId);
		try{
		    interchangefeevalidator.validateInterchangeFee(interchangeFee);	
		    interchangeFeeService.save(maptoView(interchangeFee));
		    
		}
		catch(ValidationException ve)
		{
			//ve.printStackTrace();
			logger.error(ve.getMessage(), ve);
			logger.info("In Excp : " + ve.getMessage());
			interchangeFee=new InterchangeFeeSubmitRequest();
			if (ve.getAck() != null) {
				for (org.bbps.schema.ErrorMessage error : ve.getAck().getErrorMessages()) {
					interchangeFee.getErrors().add(mapTo(error));
				}
			}
		}
		
		catch (IOException e) {
			logger.error( e.getMessage(), e);
	         logger.info("In Excp : " + e.getMessage());
		}
		
		return interchangeFee;
	}
	private in.co.rssoftware.bbps.schema.ErrorMessage mapTo(ErrorMessage error) {
		in.co.rssoftware.bbps.schema.ErrorMessage msg=new in.co.rssoftware.bbps.schema.ErrorMessage();
		msg.setErrorCd(error.getErrorCd());
		msg.setErrorDtl(error.getErrorDtl());
		return msg;
	}
	private InterchangeFeeView maptoView(InterchangeFeeSubmitRequest interchangeFee) {
		InterchangeFeeView interchangeFeeView=interchangeFeeService.fetchByBillerIdFeeCode(interchangeFee.getBillerId(), interchangeFee.getFeeCode());
		if(interchangeFeeView==null){
			interchangeFeeView=new InterchangeFeeView();	
			interchangeFeeView.setBillerId(interchangeFee.getBillerId());
			interchangeFeeView.setFeeCode(interchangeFee.getFeeCode());
		}
		interchangeFeeView.setFeeDesc(interchangeFee.getFeeDescription());
		interchangeFeeView.setFeeDirection(InterchangeFeeDirectionType.valueOf(interchangeFee.getFeeDirection()));
		interchangeFeeView.setFeeDirection(InterchangeFeeDirectionType.valueOf(interchangeFee.getFeeDirection()));
		interchangeFeeView.setTranAmtRangeMin(Long.parseLong(interchangeFee.getTranAmountRangeMin(),10));
		interchangeFeeView.setTranAmtRangeMax(Long.parseLong(interchangeFee.getTranAmountRangeMax(),10));
		interchangeFeeView.setPercentFee(new BigDecimal(interchangeFee.getPercentFee()));
		interchangeFeeView.setFlatFee(new BigDecimal(interchangeFee.getFlatFee()));
		interchangeFeeView.setEffctvFrom(interchangeFee.getEffctvFrom().toString().replaceAll("-", ""));
		interchangeFeeView.setEffctvTo(interchangeFee.getEffctvTo().toString().replaceAll("-", ""));
		return interchangeFeeView;
	}
	
	@ResponseBody
	@RequestMapping(value = "/urn:tenantId:{tenantId}/interchangefeeConfig", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public InterchangeFeeConfigSubmitRequest insertInterchangeFeeConf(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @RequestBody InterchangeFeeConfigSubmitRequest interchangeFeeConf) {
		logger.info("in insertInterchangeFeeConf method");
		
		TransactionContext.putTenantId(tenantId);
		try{
			interchangeFeeConfService.save(maptoConfigView(interchangeFeeConf));
		return interchangeFeeConf;
		}
		catch(Exception e)
		{
			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());	
			InterchangeFeeSubmitRequest icf=new InterchangeFeeSubmitRequest();
			return null;
		}
	}
	private InterchangeFeeConfView maptoConfigView(InterchangeFeeConfigSubmitRequest req) {
		InterchangeFeeConfView view=new InterchangeFeeConfView();
		view.setBlrId(req.getBillerId());
		view.setMti(MTI.valueOf(req.getMti()));
		view.setPaymentMode(PaymentMode.valueOf(req.getPaymentMode()));
		view.setPaymentChannel(PaymentChannel.valueOf(req.getPaymentChannel()));
		view.setResponseCode(req.getResponseCode());
		view.setFees(req.getFees().getFees());
		view.setEffectiveFrom(req.getEffctvFrom().toString().replaceAll("-",""));
		view.setEffectiveTo(req.getEffctvTo().toString().replaceAll("-",""));
		view.setDefault(req.isIsDefault());
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value = "/multipleFileUpload", method = RequestMethod.POST)
	public String authFilesUpload(
			@RequestParam(value = "authLetter1", required = false) MultipartFile authLetter1Files,
			@RequestParam(value = "authLetter2", required = false) MultipartFile authLetter2Files,
			@RequestParam(value = "authLetter3", required = false) MultipartFile authLetter3Files, HttpServletRequest request) {

		String msg = "File(s) has been successfully uploaded";
		StringBuilder sb = new StringBuilder("");
		String [] uploadedFileName = new String[]{"authLetter1","authLetter2","authLetter3"};
		
		Enumeration<String> attributeNames = request.getAttributeNames();
        while(attributeNames!=null && attributeNames.hasMoreElements()){
            String elementAttr = attributeNames.nextElement();
            Object attribute = request.getAttribute(elementAttr);
            
            if(attribute instanceof FileUploadException){
            	sb.append(elementAttr +",");
            }
            else if(attribute instanceof FileItem){
                FileItem fi = (FileItem)attribute;
                String fieldName = fi.getFieldName();
                if(authLetter1Files == null && fieldName != null && fieldName.equals(uploadedFileName[0])){
                	authLetter1Files = new CommonsMultipartFile(fi);
                }else if(authLetter2Files == null && fieldName != null && fieldName.equals(uploadedFileName[1])){
                	authLetter2Files = new CommonsMultipartFile(fi);
                }else if(authLetter3Files == null && fieldName != null && fieldName.equals(uploadedFileName[2])){
                	authLetter3Files = new CommonsMultipartFile(fi);
                }
            }
        }
        
		try {
			if (authLetter1Files != null && authLetter1Files.getBytes().length > 0) {
				String fileName = authLetter1Files.getOriginalFilename();
				byte[] byes = authLetter1Files.getBytes();
				authLetter1 = fileName;
				authLetterScanCopy1 = byes;
			}
			if (authLetter2Files != null && authLetter2Files.getBytes().length > 0) {
				String fileName = authLetter2Files.getOriginalFilename();
				byte[] byes = authLetter2Files.getBytes();
				authLetter2 = fileName;
				authLetterScanCopy2 = byes;
			}
			if (authLetter3Files != null && authLetter3Files.getBytes().length > 0) {
				String fileName = authLetter3Files.getOriginalFilename();
				byte[] byes = authLetter3Files.getBytes();
				authLetter3 = fileName;
				authLetterScanCopy3 = byes;
			}

		} catch (IOException e) {
			logger.error(e.getMessage());
			msg = "Failed to upload files";
		}
		
		if(!sb.equals("") && sb.length()>0 && sb.indexOf("authLetter") != -1  ){
    		return sb.toString();
    	}
		return msg;
	}
	@RequestMapping(value = "/urn:tenantId:{tenantId}/submitBillerDetails", method = RequestMethod.POST, produces = "application/json", headers = "Accept=application/json")
	public @ResponseBody JsonResponse submitBillerDetails(@RequestBody BillerView billerView, @PathVariable String tenantId) {
		String METHOD_NAME = "submitBillerDetails";
		if (logger.isDebugEnabled()) {
			logger.debug("Entering " + METHOD_NAME);
		}
		TransactionContext.putTenantId(tenantId);
		JsonResponse response = new JsonResponse();
		BillerView view = null;
		boolean isParent = false;
		BillerView biller = new BillerView();
		BillerView blr = new BillerView();
		String parentBiller = null;
		String blrid = "";
		try {
			if (billerView.getEntityStatus() != null
					&& billerView.getEntityStatus().name() == "") {
				billerView.setBillerAuthLetter1Name(authLetter1);
				billerView.setBillerAuthLetter2Name(authLetter2);
				billerView.setBillerAuthLetter3Name(authLetter3);
				billerView.setBillerAuthLetter1(authLetterScanCopy1);
				billerView.setBillerAuthLetter2(authLetterScanCopy2);
				billerView.setBillerAuthLetter3(authLetterScanCopy3);

				List<PaymentModeLimit> paymdLimit = billerView
						.getBillerPaymentModes();
				List<PaymentModeLimit> paymdLimitEdit = new ArrayList<PaymentModeLimit>();
				for (PaymentModeLimit param : paymdLimit) {
					if (param.getPaymentMode() != null) {

						PaymentModeLimit pm = new PaymentModeLimit();
						pm.setPaymentMode(param.getPaymentMode());
						pm.setMaxLimit(param.getMaxLimit());
						paymdLimitEdit.add(pm);

					}

				}
				billerView.setBillerPaymentModes(paymdLimitEdit);

				List<PaymentChannelLimit> paychnlLimit = billerView
						.getBillerPaymentChannels();
				List<PaymentChannelLimit> paychnlLimitEdit = new ArrayList<PaymentChannelLimit>();
				for (PaymentChannelLimit param : paychnlLimit) {
					if (param.getPaymentChannel() != null) {
						PaymentChannelLimit pcl = new PaymentChannelLimit();

						pcl.setPaymentChannel(param.getPaymentChannel());
						pcl.setMaxLimit(param.getMaxLimit());
						paychnlLimitEdit.add(pcl);

					}

				}

				billerView.setBillerPaymentChannels(paychnlLimitEdit);
				if (CommonUtils.getLoggedInUser() == null) {
					isParent = true;
					billerView.setParentBlr(isParent);
					billerView.setParentBlrId(null);
				} else {
					if (billerView.getParentBlrId() != null) {
						blr = billerService
								.fetchFunctionalActiveParent(billerView
										.getParentBlrId());
						if (blr != null) {
							parentBiller = blr.getBlrName();
						}
					} else {
						parentBiller = null;
						billerView.setParentBlrId(null);
					}
				}

				if (isParent) {
					blrid = CommonUtils.generateBillerId(billerView
							.getBlrName().trim(), null, isParent);
				} else {
					if (parentBiller == null) {
						blrid = CommonUtils.generateBillerId(billerView
								.getBlrName().trim(), null, isParent);
					} else {
						blrid = CommonUtils.generateBillerId(parentBiller,
								billerView.getBlrName().trim(), isParent);
					}

				}
				billerView.setBlrId(blrid);
				String blrCat = billerView.getBlrCategoryName();
				if (blrCat != null && blrCat.contains(",")) {
					blrCat = blrCat.replace(",", "");
				}
				billerView.setBlrCategoryName(blrCat);

				String ownerShip = billerView.getBlrOwnerShp();
				if (ownerShip != null && ownerShip.contains(",")) {
					ownerShip = ownerShip.replace(",", "");
				}
				billerView.setBlrOwnerShp(ownerShip);

				String cvg = billerView.getBlrCoverage();
				if (cvg != null && cvg.contains(",")) {
					cvg = cvg.replace(",", "");
				}
				billerView.setBlrCoverage(cvg);
				String ValidFrom = billerView.getBlrEffctvFrom();
				String effectiveFrom = "";

				String ValidTo = billerView.getBlrEffctvTo();
				String effectiveTo = "";
				if (ValidTo != null && !ValidTo.equals("")) {

					effectiveTo = CommonUtils.validateEffectiveDates(ValidTo);
					billerView.setBlrEffctvTo(effectiveTo.trim());
				}
				if (ValidFrom != null && !ValidFrom.equals("")) {
					effectiveFrom = CommonUtils
							.validateEffectiveDates(ValidFrom);
				} else {
					effectiveFrom = CommonUtils
							.getFormattedDateyyyyMMdd(new Date());
				}
				billerView.setBlrEffctvFrom(effectiveFrom.trim());
				if (billerView.getSameAsReg() != null) {
					if (billerView.getSameAsReg().equals("Y")) {
						billerView.setBlrRegisteredAdrline(billerView
								.getBlrRegisteredAdrline());
						billerView.setBlrRegisteredState(billerView
								.getBlrRegisteredState());
						billerView.setBlrRegisteredCity(billerView
								.getBlrRegisteredCity());
						billerView.setBlrRegisteredPinCode(billerView
								.getBlrRegisteredPinCode());
						billerView.setBlrRegisteredCountry(billerView
								.getBlrRegisteredCountry());
					}
				}

				String regcity[] = billerView.getBlrRegisteredCity().split(
						"\\,");
				String comcity[] = billerView.getBlrCommumicationCity().split(
						"\\,");

				billerView.setBlrRegisteredCity(regcity[0]);
				billerView.setBlrCommumicationCity(comcity[0]);
				billerView.setBillerDefaultOuChangeVrsn(0);

				billerService.submit(billerView, isParent);
			} else {
				//billerView = commonUpdate(billerView);
				if (CommonUtils.getLoggedInUser() == null) {
					isParent = true;
				}
				view = billerService.submit(billerView, isParent);
			}
			response.setCode(1);
			response.setMessage("success");
		} catch (Exception e) {

			logger.error( e.getMessage(), e);
	        logger.info("In Excp : " + e.getMessage());	
			//log.error("Error " + e);
		
			// /model.addAttribute("errmsg", "ERROR...");
			// return "billerOnboarding";
            //e.printStackTrace();
	        //response.setCode(0);
			response.setMessage("failure");
			//response.setResult(result);
			
		}
		response.setResult(view);
		return response;
	}

}
