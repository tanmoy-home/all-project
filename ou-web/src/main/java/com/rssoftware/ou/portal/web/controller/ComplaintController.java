package com.rssoftware.ou.portal.web.controller;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bbps.schema.Ack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.ExchangeId;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.rest.OUInternalRestTemplate;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.WebAPIURL;
import com.rssoftware.ou.domain.UserDetails;
import com.rssoftware.ou.model.tenant.SmsMessageView.SMSType;
import com.rssoftware.ou.portal.web.modal.ComplaintReceiptDTO;
import com.rssoftware.ou.portal.web.utils.UIUtils;
import com.rssoftware.ou.portal.web.utils.Utility;
import com.rssoftware.ou.tenant.service.SmsService;

import in.co.rssoftware.bbps.schema.AgentList;
import in.co.rssoftware.bbps.schema.BillerList;
import in.co.rssoftware.bbps.schema.ComplainRequest;
import in.co.rssoftware.bbps.schema.ComplainStatusRequest;
import in.co.rssoftware.bbps.schema.ComplaintRaisedResponse;
import in.co.rssoftware.bbps.schema.ComplaintType;
import in.co.rssoftware.bbps.schema.DispositionList;
import in.co.rssoftware.bbps.schema.ErrorMessage;
import in.co.rssoftware.bbps.schema.FetchResponse;
import in.co.rssoftware.bbps.schema.ParticipationType;
import in.co.rssoftware.bbps.schema.ServiceReasonList;
import in.co.rssoftware.bbps.schema.TransactionSearchRequest;
import in.co.rssoftware.bbps.schema.TxnSearchResponse;

@Controller

@RequestMapping("/ComplaintService")
public class ComplaintController {
	
	@Value("${ou.tenantId}")
	private String tenantId;

	@Value("${ou.domain}")
	private String uri;
	
	@Autowired
	private SmsService smsService;
	
	private static final Logger log = LoggerFactory.getLogger(ComplaintController.class);
	private static OUInternalRestTemplate internalRestTemplate = OUInternalRestTemplate.createInstance();
	
	
	
	@ModelAttribute("userDetails")
	public UserDetails getUserDetails(HttpServletRequest request) {
		UserDetails  userDetails= new UserDetails();
		userDetails.setTenantId(tenantId);
		return userDetails;
		//return new UserDetails();
	}
	
	@RequestMapping("/raiseComplaintreq")
	public String raiseComplaintRequestView(@ModelAttribute("userDetails") UserDetails userDetails, Model model,
			HttpServletRequest request) {
		log.info("In the method raiseComplaintRequestView of ComplaintController.");
		System.out.println("---------------------------------------------------"+request.getContextPath());
		model.addAttribute("userDetails", userDetails);
		return "raiseComplaintReq";
	}
	
	@RequestMapping("/checkComplaintStatus")
	public String checkComplaintStatusView(@ModelAttribute("userDetails") UserDetails userDetails, Model model,
			HttpServletRequest request) {
		log.info("In the method checkComplaintStatusView of ComplaintController.");
		
		model.addAttribute("userDetails", userDetails);
		
		return "complaintStatusCheck";
	}
	
	@ResponseBody
	@RequestMapping(value = "/{tenantId}/complaintstatus/{complaintType}/{complaintId}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
	public String checkComplaintStatus(HttpServletRequest request, HttpServletResponse response,@ModelAttribute("userDetails") UserDetails userDetails,
			@PathVariable String tenantId,@PathVariable String complaintType,@PathVariable String complaintId) throws ValidationException {
		
		log.info("In the method checkComplaintStatus of ComplaintController.");
		
		TransactionContext.putTenantId(tenantId);
//		String url = hostport+"/APIService/complaint/searchcomplaintstatus/urn:tenantId:"+tenantId;
		
		ComplainStatusRequest complainRequest=new ComplainStatusRequest();
		if(complaintType.equals(ComplaintType.TRANSACTION.name()))
			complainRequest.setComplaintType(ComplaintType.TRANSACTION);
		if(complaintType.equals(ComplaintType.SERVICE.name()))
		    complainRequest.setComplaintType(ComplaintType.SERVICE);
		complainRequest.setComplaintId(complaintId);
		
	    ComplaintRaisedResponse complaintRaisedResponse=null;
        try{
	        ResponseEntity responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.COMPLAINT_CHECK_STATUS_URL, HttpMethod.POST, Utility.getHttpEntityForPost(complainRequest), ComplaintRaisedResponse.class,tenantId);
	        complaintRaisedResponse =(ComplaintRaisedResponse)responseEntity.getBody();
        
	} catch(Exception e)
        {
        	log.error(e.getMessage());
        }
				
		return UIUtils.getComplaintStatus(complaintRaisedResponse);
	}

	@ResponseBody
	@RequestMapping(value = "/{tenantId}/servicereason/{particiationType}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getServicereasonList(@ModelAttribute("userDetails") UserDetails userDetails,HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @PathVariable String particiationType) throws ValidationException {
		
		log.info("In the method getServicereasonList of ComplaintController.");
		
		TransactionContext.putTenantId(tenantId);
//		String url = hostport+"/APIService/complaint/getAllServiceReason/urn:tenantId:"+tenantId+"/"+particiationType;
		 
	    ServiceReasonList serviceReasonList=null;
        try{
	        ResponseEntity responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.COMPLAINT_ALL_SERVICE_REASON_URL+"/{particiationType}", HttpMethod.GET, Utility.getHttpEntityForGet(), ServiceReasonList.class,tenantId,particiationType);
	        serviceReasonList =(ServiceReasonList)responseEntity.getBody();
        }catch(Exception e)
        {
        	log.error(e.getMessage());
        }
		return UIUtils.getServiceReasonListDropdown(serviceReasonList);
	}

	@ResponseBody
	@RequestMapping(value = "/{tenantId}/agentslist", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getAgentList(@ModelAttribute("userDetails") UserDetails userDetails,HttpServletRequest request, HttpServletResponse response, @PathVariable String tenantId)
			throws ValidationException {
		
		log.info("In the method getAgentList of ComplaintController.");
		
		TransactionContext.putTenantId(tenantId);
//		String url = hostport+"/APIService/agentInst/urn:tenantId:"+tenantId+"/getAllAgents";
	    AgentList agentList=null;
        try{
	        ResponseEntity responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.FETCH_ALL_AGENT_URL, HttpMethod.GET, Utility.getHttpEntityForGet(), AgentList.class,tenantId);
	        agentList =(AgentList)responseEntity.getBody();
        }catch(Exception e)
        {
        	log.error(e.getMessage());
        }
		return UIUtils.getAllAgentsDropdown(agentList);
	}

	@ResponseBody
	@RequestMapping(value = "/{tenantId}/billerslist", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getBillerList(@ModelAttribute("userDetails") UserDetails userDetails,HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId) throws ValidationException {
		
		log.info("In the method getBillerList of ComplaintController.");
		
		TransactionContext.putTenantId(tenantId);
//	    String url = hostport+"/APIService/BillerOU/urn:tenantId:"+tenantId+"/getBillers";
	    BillerList billerList=null;
        try{
	        ResponseEntity responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.FETCH_ALL_BILLER_URL, HttpMethod.GET, Utility.getHttpEntityForGet(), BillerList.class,tenantId);
	        billerList =(BillerList)responseEntity.getBody();
        }catch(Exception e)
        {
        	log.error(e.getMessage());
        }
		
		return UIUtils.getAllBillersDropdown(billerList);
	}

//	@ResponseBody
	@RequestMapping(value = "/{tenantId}/raiseservicecomplaint", method = RequestMethod.POST)
	public String raiseServiceComplaint(@ModelAttribute("userDetails") UserDetails userDetails,HttpServletRequest request, HttpServletResponse response, 
			@PathVariable String tenantId, @RequestParam String servReason, @RequestParam String participationType,
			@RequestParam String participationId, @RequestParam String description, @RequestParam String typeName,Model model) throws ValidationException {
		
		log.info("In the method raiseServiceComplaint of ComplaintController.");
		
		TransactionContext.putTenantId(tenantId);
		
		ComplaintReceiptDTO complaintReceiptDTO=  new ComplaintReceiptDTO();
		// String url = hostport+"/APIService/complaint/raisecomplaintreq/urn:tenantId:"+tenantId;
		ComplainRequest complainRequest = new ComplainRequest();
		complaintReceiptDTO.setResponseMsg("Success");
		complainRequest.setComplaintType(ComplaintType.SERVICE);
		complainRequest.setServReason(servReason);
		complainRequest.setParticipationType(ParticipationType.valueOf(participationType));
		if (participationType.equals(ParticipationType.AGENT.name()))
		{
			complaintReceiptDTO.setTransactionLabel("Agent Name");
			complainRequest.setAgentId(participationId);
		}
		else
		{
			complaintReceiptDTO.setTransactionLabel("Biller Name");
			complainRequest.setBillerId(participationId);
		}
		complainRequest.setDescription(description);
	    ComplaintRaisedResponse resp = null;
	    ResponseEntity responseEntity=null;
		try {
			responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.RAISE_SERVICE_COMPLAINT_URL, HttpMethod.POST, Utility.getHttpEntityForPost(complainRequest), ComplaintRaisedResponse.class,tenantId);
			resp=(ComplaintRaisedResponse)responseEntity.getBody();
			complaintReceiptDTO.setComplaintId(resp.getComplaintId());
			complaintReceiptDTO.setComplaintDate(CommonUtils.formatterdd_MM_yyyy_HH_mm_ss_Z(Calendar.getInstance()));
			complaintReceiptDTO.setIsDup("no");
			complaintReceiptDTO.setTransactionId(typeName);
			complaintReceiptDTO.setReason(servReason);
			complaintReceiptDTO.setAssignedTo(resp.getAssigned());
			complaintReceiptDTO.setStatus(resp.getComplaintStatus());
			if (resp.getOpenComplaint() != null) {
				if (resp.getOpenComplaint().equals("Y")) {
					complaintReceiptDTO.setIsDup("yes");
					complaintReceiptDTO.setResponseMsg("Complaint already raised for this Transaction with Complaint ID: " + resp.getComplaintId());
					
				} else {
					
					complaintReceiptDTO.setResponseMsg("Complaint Raised Successfully with Complaint ID: " + resp.getComplaintId());
					
				}
			} else {
				
				complaintReceiptDTO.setResponseMsg("Complaint Raised Successfully with Complaint ID: " + resp.getComplaintId());
				
			}
		} catch (Exception e) {
			complaintReceiptDTO.setResponseMsg("Failed");
			log.error(e.getMessage());
		}
		
		
		model.addAttribute("resp",complaintReceiptDTO);
//		return UIUtils.getComplaintMessage(resp);
		return "complaint_receipt";
	}
	
	@ResponseBody
	@RequestMapping(value = "/{tenantId}/transactionlist/{mobile}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getTransactionList(@ModelAttribute("userDetails") UserDetails userDetails,HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId, @PathVariable String mobile) throws ValidationException {
		
		log.info("In the method getTransactionList of ComplaintController.");
		
		TransactionContext.putTenantId(tenantId);
		
		 //String url = hostport+"/APIService/complaint/searchtransaction/urn:tenantId:"+tenantId;
		 
		TransactionSearchRequest req = new TransactionSearchRequest();
		req.setComplaintType(ComplaintType.TRANSACTION);
		req.setMobile(mobile);
		req.setXchangeId(ExchangeId.FOURZEROONE.name());

		String url=uri+WebAPIURL.FETCH_ALL_TRANSACTIONS_LIST_URL;
	    TxnSearchResponse resp = null;
		ResponseEntity responseEntity=null;
		try{
			responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.FETCH_ALL_TRANSACTIONS_LIST_URL, HttpMethod.POST, Utility.getHttpEntityForPost(req), TxnSearchResponse.class,tenantId);
			resp=(TxnSearchResponse)responseEntity.getBody();
		}catch (Exception e) {
			log.error(e.getMessage());
		}
		return UIUtils.getTransactionSaerchPage(resp);
	}
	@ResponseBody
	@RequestMapping(value = "/{tenantId}/dispositionlist", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String getDispositionList(@ModelAttribute("userDetails") UserDetails userDetails,HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId) throws ValidationException {
		
		log.info("In the method getDispositionList of ComplaintController.");
		
		TransactionContext.putTenantId(tenantId);
	    //String url = hostport+"/APIService/complaint/urn:tenantId:"+tenantId+"/dispositionlist";
	    DispositionList dispositionList =null;
        try{
	        ResponseEntity responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.FETCH_ALL_DISPOSITION_URL, HttpMethod.GET, Utility.getHttpEntityForGet(), DispositionList.class,tenantId);
	        dispositionList =(DispositionList)responseEntity.getBody();
        }catch(Exception e)
        {
        	log.error(e.getMessage());
        }
		return UIUtils.getDispositionListDropdown(dispositionList);
	}
	
//	@ResponseBody
	@RequestMapping(value = "/{tenantId}/txncomplaint", method = RequestMethod.POST)
	public String raiseTxnComplaint(@ModelAttribute("userDetails") UserDetails userDetails,HttpServletRequest request, HttpServletResponse response,
			@PathVariable String tenantId,@RequestParam String txnRefId,@RequestParam String disposition,
			@RequestParam String description,@RequestParam String mob,Model model) throws ValidationException {
		
		log.info("In the method raiseTxnComplaint of ComplaintController.");
		
		
		
		TransactionContext.putTenantId(tenantId);
		ComplaintReceiptDTO complaintReceiptDTO=  new ComplaintReceiptDTO();
		complaintReceiptDTO.setResponseMsg("Success");
		
		ComplainRequest complainRequest=new ComplainRequest();
		complainRequest.setComplaintType(ComplaintType.TRANSACTION);
		complainRequest.setTxnReferenceId(txnRefId);
		complainRequest.setDisposition(disposition);
		complainRequest.setDescription(description);

		ComplaintRaisedResponse resp = null;
		ResponseEntity responseEntity=null;
			try {
				responseEntity = internalRestTemplate.exchange(uri+WebAPIURL.RAISE_TRANSACTION_COMPLAINT_URL, HttpMethod.POST, Utility.getHttpEntityForPost(complainRequest), ComplaintRaisedResponse.class,tenantId);
				resp=(ComplaintRaisedResponse)responseEntity.getBody();
				complaintReceiptDTO.setComplaintId(resp.getComplaintId());
				String dttm=CommonUtils.formatterdd_MM_yyyy_HH_mm_ss_Z(Calendar.getInstance());
				complaintReceiptDTO.setComplaintDate(dttm);
				complaintReceiptDTO.setTransactionLabel("Transaction Reference Id");
				complaintReceiptDTO.setTransactionId(txnRefId);
				complaintReceiptDTO.setIsDup("no");
				complaintReceiptDTO.setReason(disposition);
				complaintReceiptDTO.setAssignedTo(resp.getAssigned());
				complaintReceiptDTO.setStatus(resp.getComplaintStatus());
				String msg="Your complain raised successfully vide Complain no.<"+resp.getComplaintId()+"> agains Transaction ref-Id <"+txnRefId+"> on "+dttm+".";
				if (resp.getOpenComplaint() != null) {
					if (resp.getOpenComplaint().equals("Y")) {
						complaintReceiptDTO.setIsDup("yes");
						complaintReceiptDTO.setResponseMsg("Complaint already raised for this Transaction with Complaint ID: " + resp.getComplaintId());
						smsService.sendSms(mob, msg, tenantId,"",SMSType.COMPLAINT_RAISE, null);
					} else {
						
						complaintReceiptDTO.setResponseMsg("Complaint Raised Successfully with Complaint ID: " + resp.getComplaintId());
						
					}
				} else {
					
					complaintReceiptDTO.setResponseMsg("Complaint Raised Successfully with Complaint ID: " + resp.getComplaintId());
					smsService.sendSms(mob, msg, tenantId,"",SMSType.COMPLAINT_RAISE, null);
				}
			} catch (Exception e) {
				
				complaintReceiptDTO.setResponseMsg("Failed");
				log.error(e.getMessage());
			}
//		return UIUtils.getComplaintMessage(resp);
			
			model.addAttribute("resp",complaintReceiptDTO);
			//return UIUtils.getComplaintMessage(resp);
			return "complaint_receipt";
	}
	 
	 
	 private boolean isValidated(UserDetails userDetails)
	 {
		if(null == userDetails)
			return false;
		
		
		if(null == userDetails.getTenantId())
			return false;
		else if(userDetails.getTenantId().trim().equalsIgnoreCase(""))
			return false;

		if(null == userDetails.getTenantId())
			return false;
		else if(userDetails.getTenantId().trim().equalsIgnoreCase(""))
			return false;

		if(null == userDetails.getTenantId())
			return false;
		else if(userDetails.getTenantId().trim().equalsIgnoreCase(""))
			return false;
		String testURL = uri+"/APIService/testApi/urn:tenantId:"+userDetails.getTenantId();
	        
	        try{
	        	ResponseEntity<Ack> responseEntity = 
	        			internalRestTemplate.postForEntity(testURL, Utility.getHttpEntityForGet(), Ack.class);	
	        	if(!responseEntity.getStatusCode().equals(HttpStatus.OK))
	        		return false;
	        	
	        }catch(Exception e)
	        {
	        	System.out.println(e.getMessage());
	        	return false;
	        }

		 return true;
	 }

}
