package com.rssoftware.ou.consumer;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.Biller;
import org.bbps.schema.BillerFetchRequest;
import org.bbps.schema.BillerFetchResponse;
import org.bbps.schema.HeadType;
import org.bbps.schema.InterchangeFeeDetailsType;
import org.bbps.schema.MyBiller;
import org.bbps.schema.ReqDiagnostic;
import org.bbps.schema.ResDiagnostic;
import org.bbps.schema.SearchByTime;
import org.bbps.schema.SearchMyBiller;
import org.bbps.schema.SearchType;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Transaction;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.batch.job.BOUBlrReconJob;
import com.rssoftware.ou.batch.job.BillFetchJob;
import com.rssoftware.ou.batch.job.BillPaymentJob;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.BatchRequest;
import com.rssoftware.ou.common.BillerMode;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.EntityStatus;
import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.common.MTI;
import com.rssoftware.ou.common.TypeOfBatch;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.database.entity.global.TenantDetail;
import com.rssoftware.ou.database.entity.tenant.AgentDetail;
import com.rssoftware.ou.database.entity.tenant.BillerFetchRequestResponse;
import com.rssoftware.ou.domain.BillerResponseParams;
import com.rssoftware.ou.domain.ParamConfig;
import com.rssoftware.ou.domain.ParamConfig.DataType;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentChannelLimit;
import com.rssoftware.ou.domain.PaymentMode;
import com.rssoftware.ou.domain.PaymentModeLimit;
import com.rssoftware.ou.model.tenant.AgentView;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.model.tenant.MyBillerView;
import com.rssoftware.ou.service.TenantDetailService;
import com.rssoftware.ou.tenant.dao.DBSmsDao;
import com.rssoftware.ou.tenant.service.AgentService;
import com.rssoftware.ou.tenant.service.BillerFetchRequestResponseService;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.FinTransactionDataService;
import com.rssoftware.ou.tenant.service.IDGeneratorService;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;
import com.rssoftware.ou.tenant.service.MyBillerDetailService;
import com.rssoftware.ou.tenant.service.ParamService;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class BatchRequestProcessor implements Consumer<Event<BatchRequest>> {

	private static Log logger = LogFactory.getLog(BatchRequestProcessor.class);

	private static OURestTemplate restTemplate = OURestTemplate.createInstance(); 
	
	private static final String BLANK = "";
	
	private static ObjectMapper objectMapper = new ObjectMapper();
		
	@Autowired
	private TenantDetailService tenantDetailService; 
	
	@Autowired
	private IDGeneratorService idGeneratorService; 

	@Autowired
	private ParamService paramService; 
	
	@Autowired
	private BillerService billerService; 
	
	@Autowired
	private MyBillerDetailService myBillerService; 
	
	@Autowired
	private FinTransactionDataService finTransactionDataService; 
	
	@Autowired
	private InterchangeFeeService interchangeFeeService;
	
	@Autowired
	private InterchangeFeeConfService interchangeFeeConfService;
	
	@Autowired
	private BillFetchJob billFetchJob;
	
	@Autowired
	private BillPaymentJob billPaymentJob;
	
	@Autowired
	private BOUBlrReconJob bouBlrReconJob;
	
	@Autowired
	private BillerFetchRequestResponseService billerFetchRequestResponseService;
	
	@Autowired
	private AgentService agentService;
	
	@Value("${finReconSQL}")
	private String sql;
	
	@Override
	public void accept(Event<BatchRequest> event) {
		logger.info("Event received in Batch Request processor accept"+event);
		BatchRequest request = event.getData();
		logger.info("Event received in Batch Request processor accept Batch Request Type is "+request.getTypeOfBatch().name());
		logger.info("Event received in Batch Request processor accept Tenant Id is "+request.getTenantId());
		TransactionContext.putTenantId(request.getTenantId());
		TypeOfBatch typeOfBatch = request.getTypeOfBatch();
		
		//get the active billers
		List<BillerView> billerViews = null;
		List<MyBillerView> myBillerViews = null;

		try {
            billerViews = billerService.getActiveBillers();
            //myBillerViews = myBillerService.getActiveBillers();
        } catch (IOException e) {
            logger.error("Error getting active bilers: ", e);        
        }
		
		if (typeOfBatch == TypeOfBatch.BILLER_FETCH){
			triggerBillerFetch();
		}
		else if (typeOfBatch == TypeOfBatch.BILLER_FETCH_BY_PARAMETER){
			triggerBillerFetchByParameter(request.getRefId());
		}
		else if (typeOfBatch == TypeOfBatch.HEARTBEAT){
			triggerDiagnosticRequest();
		}
		else if (typeOfBatch == TypeOfBatch.BILLER_SAVE){
			// ((((---------(for /scheduleBillerFetch(GET method)
			BillerFetchRequestResponse req=billerFetchRequestResponseService.fetchByRefId(request.getBillerFetchResponse().getHead().getRefId());
			if(req==null){
			  boolean reqAvailable = false;
			  saveBillerFetch(request.getBillerFetchResponse(),reqAvailable);
			}
			else//-----------------------------------)))))))
			  saveBillerFetch(request.getBillerFetchResponse());//For /billerFetchRequestByParameter(POST Method)
		}
		else if (typeOfBatch == TypeOfBatch.FIN_RECON) { 
			triggerFinRecon();
		}
		else if (typeOfBatch == TypeOfBatch.BOU_BLR_RECON) { 
			try {
				myBillerViews = myBillerService.getActiveBillers();
			} catch (IOException e) {
				logger.error("Error getting mybiller active bilers: ", e);	
			}
			if(myBillerViews!=null){
				bouBlrReconJob.doBOUBlrRecon(request.getTenantId(), myBillerViews);
			}
			//triggerBOUBlrRecon();
		}
		//Biller related batch processing
		else if (typeOfBatch == TypeOfBatch.BILL_FETCH){
			billFetchJob.watchDirectory();
		}
		else if (typeOfBatch == TypeOfBatch.BILL_PAYMENT){
			if(billerViews!=null){
				billPaymentJob.billPayment(request.getTenantId(), billerViews);
			}
		}
		else if (typeOfBatch == TypeOfBatch.BILL_PAYMENT_REPORT){
			if(billerViews!=null)
				billPaymentJob.reportBillPayment(request.getTenantId(), billerViews);
		}
		else if (typeOfBatch == TypeOfBatch.TXN_REPORT){
			triggerTxnReport();
		}
		else if (typeOfBatch == TypeOfBatch.CU_AGENT_APPROVAL_REQ) {
			
			triggerCUAgtApprovalReqCsvGen();
			
		}
		else if (typeOfBatch == TypeOfBatch.ALL_CU_APPROVED_AGENTS_FETCH) {
			
			triggerAllCUApprovedAgentsFetch();
			
		}
		// TODO: write rest of batch handling here
	}

	private void saveBillerFetch(BillerFetchResponse bfr){
		//fetch the request by refID
		BillerFetchRequestResponse req=billerFetchRequestResponseService.fetchByRefId(bfr.getHead().getRefId());
		try {
				req.setResponseJson(objectMapper.writeValueAsBytes(bfr));
			} catch (Exception e) {
				e.printStackTrace();
		} 
		billerFetchRequestResponseService.save(req);//saving response json
		
		//If MyBiller=YES, save response to my_biller_details table
		if(req.getMyBiller()!=null && req.getMyBiller().equals(MyBiller.YES.name())){
			List<String> mybillerIdList=myBillerService.getAllCurrentBillerIds();
			if (bfr != null && !bfr.getBillers().isEmpty()){
				for (Biller blr:bfr.getBillers()){
					if (blr != null && blr.getBillerId() != null){
						MyBillerView bv = mapFromMyBillerView(blr);
						if(mybillerIdList.contains(bv.getBlrId())) {
							mybillerIdList.remove(bv.getBlrId());
						}
						myBillerService.save(bv);
					}
				}
			}
			for(String billerId : mybillerIdList) {
				myBillerService.delete(billerId);
			}
		}
		else{//save response to biller_details table
			List<String> billerIdList = billerService.getAllCurrentBillerIds();
			if (bfr != null && !bfr.getBillers().isEmpty()){
				for (Biller b:bfr.getBillers()){
					if (b != null && b.getBillerId() != null){
						BillerView bv = mapFrom(b);
						if(billerIdList.contains(bv.getBlrId())) {
							billerIdList.remove(bv.getBlrId());
						}
						billerService.save(bv);
					//	interchangeFeeConfService.deleteAllInterchangeFeeConfByBillerId(bv.getBlrId());
						for(InterchangeFeeConfView ifcv : bv.getInterchangeFeeConfView()) {
							interchangeFeeConfService.save(ifcv);	
						}
					//	interchangeFeeService.deleteAllInterchangeFeeByBillerId(bv.getBlrId());
						for(InterchangeFeeView ifv : bv.getInterchangeFeeView()) {
							interchangeFeeService.save(ifv);
						}					
					}
				}
			}
			/*for(String billerId : billerIdList) {
				billerService.delete(billerId);
			}
*/		}
	}
    
	private void saveBillerFetch(BillerFetchResponse bfr,boolean reqAvailable) {//(for /scheduleBillerFetch(GET method)
		if(!reqAvailable){
			List<String> billerIdList = billerService.getAllCurrentBillerIds();
			if (bfr != null && !bfr.getBillers().isEmpty()){
				for (Biller b:bfr.getBillers()){
					if (b != null && b.getBillerId() != null){
						BillerView bv = mapFrom(b);
						if(billerIdList.contains(bv.getBlrId())) {
							billerIdList.remove(bv.getBlrId());
						}
						billerService.save(bv);
						interchangeFeeConfService.deleteAllInterchangeFeeConfByBillerId(bv.getBlrId());
						for(InterchangeFeeConfView ifcv : bv.getInterchangeFeeConfView()) {
							interchangeFeeConfService.save(ifcv);	
						}
						interchangeFeeService.deleteAllInterchangeFeeByBillerId(bv.getBlrId());
						for(InterchangeFeeView ifv : bv.getInterchangeFeeView()) {
							interchangeFeeService.save(ifv);
						}					
					}
				}
			}
			for(String billerId : billerIdList) {
				billerService.delete(billerId);
			}
		}
		
	}
	
	private void triggerBillerFetch(){
		BillerFetchRequest bfr = new BillerFetchRequest();
		bfr.setHead(new HeadType());
		bfr.getHead().setOrigInst(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName());
		TenantDetail td = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId());
		bfr.getHead().setRefId(idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, td.getOuName()));
		bfr.getHead().setVer("1.0");
		bfr.getHead().setTs(CommonUtils.getFormattedCurrentTimestamp());
		SearchMyBiller searchMyBiller = new SearchMyBiller();
		searchMyBiller.setMybiller(MyBiller.NO);
		bfr.setSearchMyBiller(searchMyBiller);
		String url = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN) + CommonConstants.BILLER_FETCH_REQUEST_URl + bfr.getHead().getRefId();
		Ack ack = null;
		try {
			ack = restTemplate.postForObject(url, bfr, Ack.class);
			LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);	
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
				logger.error("Refresh Biller call failed, error:" + (!ack.getErrorMessages().isEmpty()?ack.getErrorMessages().get(0).getErrorCd():"") + ", " + (!ack.getErrorMessages().isEmpty()?ack.getErrorMessages().get(0).getErrorDtl():""));
			}

		}
		catch (Exception e){
			logger.error("Refresh Biller call failed", e); 
		}
	}
	
	private void triggerBillerFetchByParameter(String refId) {
		BillerFetchRequestResponse req=billerFetchRequestResponseService.fetchByRefId(refId);
		BillerFetchRequest bfr = new BillerFetchRequest();
		bfr.setHead(new HeadType());
		bfr.getHead().setOrigInst(tenantDetailService.fetchByTenantId(TransactionContext.getTenantId()).getOuName());
		bfr.getHead().setRefId(refId);
		bfr.getHead().setVer("1.0");
		bfr.getHead().setTs(CommonUtils.getFormattedCurrentTimestamp());
		if (req.getMyBiller()!=null && req.getMyBiller()!=""){
			SearchMyBiller searchMyBiller = new SearchMyBiller();
			searchMyBiller.setMybiller(MyBiller.valueOf(req.getMyBiller()));
			bfr.setSearchMyBiller(searchMyBiller);
		}
		
		if (req.getBillerId()!=null && !req.getBillerId().isEmpty()){
			SearchType searchType = new SearchType();
			List<String> billerIdList = Arrays.asList(req.getBillerId().split("\\s*,\\s*"));
			for(String billerId:billerIdList){
				searchType.getBillerIds().add(billerId);
			}
			bfr.setSearch(searchType);
		}
		if (req.getDateRange()!=null && req.getDateRange()!=""){
			SearchByTime searchByTime = new SearchByTime();
			searchByTime.setTime(req.getDateRange());
			bfr.setSearchByTime(searchByTime);
		}
        String url = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN) + CommonConstants.BILLER_FETCH_REQUEST_URl + bfr.getHead().getRefId();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		LogUtils.logReqRespMessage(bfr, bfr.getHead().getRefId(),Action.BILLER_LIST_REQUEST);
		Ack ack = null;
		try {
			ack = restTemplate.postForObject(url, bfr, Ack.class);
			LogUtils.logReqRespMessage(ack, ack.getRefId(), Action.ACK);	
			if (!CommonConstants.RESP_SUCCESS_MSG.equals(ack.getRspCd())){
				logger.error("Refresh Biller call failed, error:" + (!ack.getErrorMessages().isEmpty()?ack.getErrorMessages().get(0).getErrorCd():"") + ", " + (!ack.getErrorMessages().isEmpty()?ack.getErrorMessages().get(0).getErrorDtl():""));
			}
		}
		catch (Exception e){
			logger.error("Refresh Biller call failed", e); 
		}
	}
	
	private void triggerDiagnosticRequest() {

		ReqDiagnostic reqDiagnostic = new ReqDiagnostic();
		TenantDetail td = tenantDetailService.fetchByTenantId(TransactionContext.getTenantId());
		
		String headRefId = idGeneratorService.getUniqueID(CommonConstants.LENGTH_REF_ID, td.getOuName());

		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);
		
		HeadType head = new HeadType();
		head.setTs(CommonUtils.getFormattedCurrentTimestamp());
		head.setOrigInst(td.getOuName());
		head.setRefId(headRefId);
		head.setVer("1.0");
		reqDiagnostic.setHead(head);
		LogUtils.logReqRespMessage(reqDiagnostic, reqDiagnostic.getHead().getOrigInst(), Action.HEART_BEAT);
		String cuDiagnosticUrl = cuDomain + CommonConstants.DIAGNOSTIC_REQUEST_URL + headRefId;

		ResponseEntity<ResDiagnostic> response = restTemplate.postForEntity(cuDiagnosticUrl, reqDiagnostic,
				ResDiagnostic.class);
		ResDiagnostic diagnosticResponse = response.getBody();
		LogUtils.logReqRespMessage(diagnosticResponse, diagnosticResponse.getHead().getOrigInst(), Action.HEART_BEAT);
	}

	private static BillerView mapFrom(Biller billerDetail) {
		if (null == billerDetail) {
			return null;
		}
		BillerView billerView = new BillerView();
		billerView.setBlrId(billerDetail.getBillerId());
		billerView.setBlrMode(billerDetail.getBillerMode()!=null?BillerMode.valueOf(billerDetail.getBillerMode()):null);
		billerView.setFetchRequirement(billerDetail.getFetchRequirement()!=null?billerDetail.getFetchRequirement().name():null);
		billerView.setBlrName(billerDetail.getBillerName());
		billerView.setParentBlr(billerDetail.isParentBiller());
		billerView.setBlrAcceptsAdhoc(billerDetail.isBillerAcceptsAdhoc());
		billerView.setBlrCategoryName(billerDetail.getBillerCategoryName());
		billerView.setParentBlrId(billerDetail.getParentBillerId());
		billerView.setBillerPaymentExactness(billerDetail.getPaymentAmountExactness());
	    billerView.setBillerPaymentModes(mapPaymentModeLimitList(billerDetail.getBillerPaymentModes()));
	    billerView.setBillerPaymentChannels(mapPaymentChanelLimitList(billerDetail.getBillerPaymentChannels()));
	    billerView.setBillerCustomerParams(mapParamConfigList(billerDetail.getBillerCustomerParams()));
	    billerView.setBillerResponseParams(mapBillerResponseParams(billerDetail.getBillerResponseParams()));
	    billerView.setBillerAdditionalInfo(mapParamConfigList(billerDetail.getBillerAdditionalInfos()));
		billerView.setEntityStatus(EntityStatus.ACTIVE);
		//billerView.setBlrEffctvFrom(null);
		//billerView.setBlrEffctvTo(null);
		
		 billerView.setBlrEffctvFrom(billerDetail.getBillerEffctvFrom());
	        billerView.setBlrEffctvTo(billerDetail.getBillerEffctvTo());
	        billerView.setBlrOwnerShp(billerDetail.getBillerOwnerShp());
	        billerView.setBlrCoverage(billerDetail.getBillerCoverage());
	        
	        billerView.setInterchangeFeeView(mapInterchangeFees(billerDetail.getInterchangeFees(),billerDetail.getBillerId()));
	        billerView.setInterchangeFeeConfView(mapInterchangeFeeConf(billerDetail.getInterchangeFeeConves(),billerDetail.getBillerId()));
		return billerView;
	}

	private static List<PaymentModeLimit> mapPaymentModeLimitList(List<org.bbps.schema.PaymentModeLimit> input){
		List<PaymentModeLimit> list = new ArrayList<PaymentModeLimit>();
		if (input != null){
			for (org.bbps.schema.PaymentModeLimit p:input){
				if (p != null){
					PaymentModeLimit pml = new PaymentModeLimit();
					pml.setPaymentMode(PaymentMode.getFromExpandedForm(p.getPaymentMode()));
					pml.setMaxLimit(p.getMaxLimit());
					list.add(pml);
				}
			}
		}
		
		return list;
	}

	private static List<PaymentChannelLimit> mapPaymentChanelLimitList(List<org.bbps.schema.PaymentChannelLimit> input){
		List<PaymentChannelLimit> list = new ArrayList<PaymentChannelLimit>();
		if (input != null){
			for (org.bbps.schema.PaymentChannelLimit p:input){
				if (p != null){
					PaymentChannelLimit pcl = new PaymentChannelLimit();
					pcl.setPaymentChannel(PaymentChannel.getFromExpandedForm(p.getPaymentChannel()));
					pcl.setMaxLimit(p.getMaxLimit());
					list.add(pcl);
				}
			}
		}
		
		return list;
	}

	private static BillerResponseParams mapBillerResponseParams(org.bbps.schema.BillerResponseParams input){
		BillerResponseParams brp = new BillerResponseParams();
		if (input != null){
			brp.getParams().addAll(mapParamConfigList(input.getParams()));
			for (org.bbps.schema.AmountOption ao : input.getAmountOptions()){
				com.rssoftware.ou.domain.BillerResponseParams.AmountOption amountOption = new com.rssoftware.ou.domain.BillerResponseParams.AmountOption();
				amountOption.setAmountBreakups(ao.getAmountBreakupSets());
				brp.getAmountOptions().add(amountOption);
			}
		}
		
		return brp;
	}
	
	private static List<ParamConfig> mapParamConfigList(List<org.bbps.schema.ParamConfig> input){
		List<ParamConfig> list = new ArrayList<ParamConfig>();
		if (input != null){
			for (org.bbps.schema.ParamConfig p:input){
				if (p != null){
					ParamConfig pc = new ParamConfig();
					pc.setParamName(p.getParamName());
					pc.setOptional(p.isOptional());
					pc.setDataType(DataType.valueOf(p.getDataType().value()));
					list.add(pc);
				}
			}
		}
		return list;
	}
	
	private static List<InterchangeFeeConfView>mapInterchangeFeeConf(List<org.bbps.schema.InterchangeFeeConf> input,String billerId){
        
        List<InterchangeFeeConfView> list = new ArrayList<InterchangeFeeConfView>();
        if(input != null){
            for(org.bbps.schema.InterchangeFeeConf conf:input){
                
                if(conf != null){
                    
                    InterchangeFeeConfView feeconf= new InterchangeFeeConfView();
                    feeconf.setBlrId(billerId);
                    feeconf.setMti(MTI.getFromExpandedForm(conf.getMti()));
                    feeconf.setDefault(conf.isDefaultFee());
                    feeconf.setEffectiveFrom(conf.getEffctvFrom());
                    feeconf.setEffectiveTo(conf.getEffctvTo());
                    //feeconf.setFees(StringUtils.join(conf.getFees(),","));
                    feeconf.setFees(conf.getFees());
                    feeconf.setPaymentChannel(PaymentChannel.getFromExpandedForm(conf.getPaymentChannel()));
                    feeconf.setPaymentMode(PaymentMode.getFromExpandedForm(conf.getPaymentMode()));
                    feeconf.setResponseCode(conf.getResponseCode());
                    list.add(feeconf);
                }
                
            }
            
            
        }
        return list;
        
    }
 
	private static List<InterchangeFeeView> mapInterchangeFees(List<org.bbps.schema.InterchangeFee> input, String billerId){
	    List<InterchangeFeeView> feeViewlist = new ArrayList<InterchangeFeeView>();
	    if(input != null) {
	        for(org.bbps.schema.InterchangeFee fee : input) {
	            if(fee != null) {                
	                for(InterchangeFeeDetailsType iFeeDetailsType : fee.getInterchangeFeeDetails()) {
	                    InterchangeFeeView interfee = new InterchangeFeeView();
	                    interfee.setBillerId(billerId);
	                    interfee.setFeeCode(fee.getFeeCode());
	                    interfee.setFeeCode(fee.getFeeCode());
	                    interfee.setFeeDesc(fee.getFeeDesc());
	                    interfee.setFeeDirection(InterchangeFeeDirectionType.valueOf(fee.getFeeDirection().value()));
	                    interfee.setFlatFee(iFeeDetailsType.getFlatFee());
	                    interfee.setPercentFee(iFeeDetailsType.getPercentFee());
	                    interfee.setTranAmtRangeMin(iFeeDetailsType.getTranAmtRangeMin());
	                    interfee.setTranAmtRangeMax(iFeeDetailsType.getTranAmtRangeMax());
	                    interfee.setEffctvFrom(getFormattedDateyyyyMMdd(iFeeDetailsType.getEffctvFrom()));
	                    interfee.setEffctvTo(getFormattedDateyyyyMMdd(iFeeDetailsType.getEffctvTo()));
	                    feeViewlist.add(interfee);
	                }   
	            }            
	        }	        
	    }
	    return feeViewlist;
	}
	
	private static String getFormattedDateyyyyMMdd(String date) {
		if(date != null && !"".equals(date)) {
			return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).format(DateTimeFormatter.BASIC_ISO_DATE);
		} else {
			return BLANK;
		}
	}
	private Timestamp reconTimestamp() {
		return new Timestamp(new Date().getTime()-Integer.parseInt(paramService.retrieveStringParamByName("RECON_TIMEOUT"))*60*1000);
	}

	private void triggerFinRecon(){
		
		String[] springConfig = { "FinReconJob.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("finReconJob");
		//String outputFile = System.getProperty("reconFileLocation")+"/fin_recon_"+CommonUtils.getFormattedDateyyyyMMdd(new Date())+".csv";
		//String outputFile = System.getProperty("reconFileLocation")+"/recon_"+idGeneratorService.getUniqueID(12, CommonUtils.getFormattedDateyyyyMMdd(new Date())+"_")+".csv";
		String outputFile = retrieveFileName();
		//String outputFile = "file:/home/rsdpp/git/BBPS_OU/Fin_Recon/fin_recon_"+idGeneratorService.getUniqueID(15, TransactionContext.getTenantId())+".csv";
		
		try {
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			JobParameters param = new JobParametersBuilder()
					.addString("output_file", outputFile)
					.addString("sql", sql+paramService.retrieveStringParamByName("RECON_TIMEOUT")+"' minute")
					.toJobParameters();
			//ExecutionContext executionContext = new ExecutionContext();
			
			/*Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -2);
			executionContext.put("ts", new Timestamp(cal.getTime().getTime()));*/
			File file = new File(outputFile);
			if(file.exists())
				logger.info(file.getName() + " is deleted! "+file.delete());

			JobExecution execution = jobLauncher.run(job, param);
			
			logger.info("Exit Status : " + execution.getStatus());
			
			if(execution.getStatus().equals(BatchStatus.FAILED))
			{
				logger.info("Failure reason : " + execution.getAllFailureExceptions());
				execution.stop();
				execution.setExitStatus(ExitStatus.FAILED);
			}
		} 
		catch (Exception e) {
			logger.error("Fetching financial reconciliation data call failed", e); 
		}
		finally {
			logger.info("Financial Reconciliation Done!");
		}
	}


	private String retrieveFileName() {
		String path = System.getProperty("reconFileLocation");
		String extension = "_"+idGeneratorService.getUniqueID(5, "")+".csv";
		String fileName = paramService.retrieveStringParamByName("RECON_FILE_NAME");
		String dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+"/"+fileName+extension;
	}
	private String retrieveTxnReportFileName() {
		String path = System.getProperty("reconFileLocation");
		String extension = "_"+idGeneratorService.getUniqueID(5, "")+".csv";
		String fileName = paramService.retrieveStringParamByName("RECON_FILE_NAME");
		String dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+"/TxnReport/"+fileName+extension;
	}
	
	private String retrieveComplaintReportFileName() {
		String path = System.getProperty("reconFileLocation");
		String extension = "_"+idGeneratorService.getUniqueID(5, "")+".csv";
		String fileName = paramService.retrieveStringParamByName("RECON_FILE_NAME");
		String dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+"/ComplaintReport/"+fileName+extension;
	}
	
	private String retrieveSegmentReportFileName() {
		String path = System.getProperty("reconFileLocation");
		String extension = "_"+idGeneratorService.getUniqueID(5, "")+".csv";
		String fileName = paramService.retrieveStringParamByName("RECON_FILE_NAME");
		String dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+"/SegmentReport/"+fileName+extension;
	}
	
	private String retrieveCUAgentApprovalReqFileName() {
		//String path =System.getProperty("reconFileLocation");
		String path = System.getProperty("cuAgentApprovalReqLocation");
		String extension = ".csv";
		String fileName = "CU_AGENT_APPROVAL_REQ";//paramService.retrieveStringParamByName("RECON_FILE_NAME");
		//tring dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		//fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+fileName+System.currentTimeMillis()+extension;
	}
	
	private String retrieveAllCUApprovedAgentsFileLoc() {
		//String path =System.getProperty("reconFileLocation");
		String path = System.getProperty("allCUApprovedAgentsFileLoc");
		String extension = ".csv";
		String fileName = "ALL_CU_APPROVED_AGENTS_";//paramService.retrieveStringParamByName("RECON_FILE_NAME");
		//tring dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		//fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+fileName+date+extension;
	}
	
	private String retrieveTempContentLoc() {
		//String path =System.getProperty("reconFileLocation");
		String path = System.getProperty("allCUApprovedAgentsFileLoc");
		String extension = ".csv";
		String fileName = "TEMP_CONTENT_";//paramService.retrieveStringParamByName("RECON_FILE_NAME");
		//tring dateFormat = fileName.substring(fileName.indexOf('{')+1, fileName.indexOf('}'));
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		String date = formatter.format(new Date());
	    //String date = CommonUtils.getFormattedDateyyyyMMdd(new Date());
		//fileName = fileName.replaceAll("\\{"+dateFormat+"\\}", date);
	    //fileName = fileName.replaceAll(",", "_");
	    return path+fileName+System.currentTimeMillis()+extension;
	}
	private static MyBillerView mapFromMyBillerView(Biller billerDetail) {
		if (null == billerDetail) {
			return null;
		}
		MyBillerView billerView = new MyBillerView();
		billerView.setBlrId(billerDetail.getBillerId());
		billerView.setBlrMode(billerDetail.getBillerMode()!=null?BillerMode.valueOf(billerDetail.getBillerMode()):null);
		billerView.setFetchRequirement(billerDetail.getFetchRequirement()!=null?billerDetail.getFetchRequirement().name():null);
		billerView.setBlrName(billerDetail.getBillerName());
		billerView.setParentBlr(billerDetail.isParentBiller());
		billerView.setBlrAcceptsAdhoc(billerDetail.isBillerAcceptsAdhoc());
		billerView.setBlrCategoryName(billerDetail.getBillerCategoryName());
		billerView.setParentBlrId(billerDetail.getParentBillerId());
	    billerView.setBillerPaymentModes(mapPaymentModeLimitList(billerDetail.getBillerPaymentModes()));
	    billerView.setBillerPaymentChannels(mapPaymentChanelLimitList(billerDetail.getBillerPaymentChannels()));
	    billerView.setBillerCustomerParams(mapParamConfigList(billerDetail.getBillerCustomerParams()));
	    billerView.setBillerResponseParams(mapBillerResponseParams(billerDetail.getBillerResponseParams()));
	    billerView.setBillerAdditionalInfo(mapParamConfigList(billerDetail.getBillerAdditionalInfos()));
		billerView.setEntityStatus(EntityStatus.ACTIVE);
		//billerView.setBlrEffctvFrom(null);
		//billerView.setBlrEffctvTo(null);
		
		 billerView.setBlrEffctvFrom(billerDetail.getBillerEffctvFrom());
	        billerView.setBlrEffctvTo(billerDetail.getBillerEffctvTo());
	        billerView.setBlrOwnerShp(billerDetail.getBillerOwnerShp());
	        billerView.setBlrCoverage(billerDetail.getBillerCoverage());
	        
	        billerView.setInterchangeFeeView(mapInterchangeFees(billerDetail.getInterchangeFees(),billerDetail.getBillerId()));
	        billerView.setInterchangeFeeConfView(mapInterchangeFeeConf(billerDetail.getInterchangeFeeConves(),billerDetail.getBillerId()));
		return billerView;
	}
	
	private void triggerTxnReport() {
		String[] springConfig = { "TxnReportJob.xml" };
		String[] springConfigComplaint = { "ComplaintReportJob.xml" };
		String[] springConfigSegment = { "SegmentReportJob.xml" };

		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		ApplicationContext contextComplaint = new ClassPathXmlApplicationContext(springConfigComplaint);
		ApplicationContext contextSegment = new ClassPathXmlApplicationContext(springConfigSegment);

		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		JobLauncher jobLauncherComplaint = (JobLauncher) contextComplaint.getBean("jobLauncher");
		JobLauncher jobLauncherSegment = (JobLauncher) contextSegment.getBean("jobLauncher");

		Job job = (Job) context.getBean("txnReportJob");
		Job jobComplaint = (Job) contextComplaint.getBean("complaintReportJob");
		Job jobSegment = (Job) contextSegment.getBean("segmentReportJob");

		String outputFile = retrieveTxnReportFileName();
		String outputFileComplaint = retrieveComplaintReportFileName();
		String outputFileSegment = retrieveSegmentReportFileName();

		try {
			JobParameters param = new JobParametersBuilder()
					.addString("output_file", outputFile)
					.addString("OU_Name", paramService.retrieveStringParamByName("OU_NAME"))
					.toJobParameters();
			
			JobParameters paramComplaint = new JobParametersBuilder()
			.addString("output_file", outputFileComplaint)
			.addString("OU_Name", paramService.retrieveStringParamByName("OU_NAME"))
			.toJobParameters();
			
			
			JobParameters paramSegment = new JobParametersBuilder()
			.addString("output_file", outputFileSegment)
			.addString("OU_Name", paramService.retrieveStringParamByName("OU_NAME"))
			.toJobParameters();
			
			File file = new File(outputFile);
			if(file.exists())
				logger.info(file.getName() + " is deleted! "+file.delete());

			JobExecution execution = jobLauncher.run(job, param);
			JobExecution executionComplaint = jobLauncherComplaint.run(jobComplaint, paramComplaint);
			JobExecution executionSegment = jobLauncherSegment.run(jobSegment, paramSegment);

			logger.info("Exit Status : txnreport- " + execution.getStatus()+",complaintreport- "+ executionComplaint.getStatus()+",segmentreport- "+ executionSegment.getStatus());
			
			if(execution.getStatus().equals(BatchStatus.FAILED) || executionComplaint.getStatus().equals(BatchStatus.FAILED) || executionSegment.getStatus().equals(BatchStatus.FAILED))
			{
				logger.info("Failure reason txnreport- : " + execution.getAllFailureExceptions());
				logger.info("Failure reason complaintreport- : " + executionComplaint.getAllFailureExceptions());
				logger.info("Failure reason segmentreport- : " + executionSegment.getAllFailureExceptions());

				if(execution.getStatus().equals(BatchStatus.FAILED)) {
					execution.stop();
					execution.setExitStatus(ExitStatus.FAILED);
				}
				
				if(executionComplaint.getStatus().equals(BatchStatus.FAILED)) {
					executionComplaint.stop();
					executionComplaint.setExitStatus(ExitStatus.FAILED);
				}
				
				if(executionSegment.getStatus().equals(BatchStatus.FAILED)) {
					executionSegment.stop();
					executionSegment.setExitStatus(ExitStatus.FAILED);
				}
			}
		} 
		catch (Exception e) {
			logger.error("Fetching Txn Report data call failed", e); 
		}
		finally {
			logger.info("Txn Report Done!");
		}		
	}
	
	private void triggerCUAgtApprovalReqCsvGen(){
		String[] springConfig = { "CUAgentRegApprovalReqJob.xml" };

		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

		Job job = (Job) context.getBean("cuAgentApprovalReqJob");

		String outputFile = retrieveCUAgentApprovalReqFileName();
		try {
			JobParameters param = new JobParametersBuilder()
					.addString("output_file", outputFile)
					.toJobParameters();
			
			
			File file = new File(outputFile);

			JobExecution execution = jobLauncher.run(job, param);

			logger.info("Exit Status : cuAgentApprovalReqJob- " + execution.getStatus());
			
			if(execution.getStatus().equals(BatchStatus.FAILED))
			{
				logger.info("Failure reason cuAgentApprovalReqJob- : " + execution.getAllFailureExceptions());

				if(execution.getStatus().equals(BatchStatus.FAILED)) {
					execution.stop();
					execution.setExitStatus(ExitStatus.FAILED);
				}
			}
			else{
				// TODO update status in DB to CU_SENT
				List<String> forCUApprovalAgents = Collections.EMPTY_LIST;
				
				if (null != execution.getExecutionContext().get("forCUApprovalAgents")){
					forCUApprovalAgents = (List<String>)(execution.getExecutionContext().get("forCUApprovalAgents"));
					logger.info("size of approved agents"+forCUApprovalAgents.size());
					for(String agentId: forCUApprovalAgents){
						AgentView agentView = agentService.getAgentById(agentId);
						agentView.setEntityStatus(EntityStatus.CU_SENT);
						agentService.save(agentView);
					}
					
				}
			}
		} 
		catch (Exception e) {
			logger.error("Fetching Pending activation agent details call failed", e); 
		}
		finally {
			logger.info("Pending activation agent details list Fetched!");
		}		
	}
	
	private void triggerAllCUApprovedAgentsFetch(){

		String[] springConfig = { "AllCUApprovedAgentsFetchJob.xml" };

		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");

		Job job = (Job) context.getBean("allCUApprovedAgentsFetchJob");

		String inputFile = retrieveAllCUApprovedAgentsFileLoc();
		String tempContentLoc = retrieveTempContentLoc();
		try {
			JobParameters param = new JobParametersBuilder()
					.addString("all_cu_approved_agents_file_loc", inputFile)
					.addString("tempContentLoc", tempContentLoc)
					.toJobParameters();
			
			
			File file = new File(inputFile);

			JobExecution execution = jobLauncher.run(job, param);

			logger.info("Exit Status : allCUApprovedAgentsFetchJob- " + execution.getStatus());
			
			if(execution.getStatus().equals(BatchStatus.FAILED))
			{
				logger.info("Failure reason allCUApprovedAgentsFetchJob- : " + execution.getAllFailureExceptions());

				if(execution.getStatus().equals(BatchStatus.FAILED)) {
					execution.stop();
					execution.setExitStatus(ExitStatus.FAILED);
				}
			}
			else{
				List<AgentDetail> approvedAgents = Collections.EMPTY_LIST;
				if (null != execution.getExecutionContext().get("approvedAgents")){
					approvedAgents = (List<AgentDetail>)(execution.getExecutionContext().get("approvedAgents"));
					logger.info("size of approved agents"+approvedAgents.size());
					agentService.saveBulk(approvedAgents);
					/*for(AgentDetail cuApprovedAgentDtl: approvedAgents){
						AgentView agentView = agentService.getAgentById(cuApprovedAgentDtl.getAgentId());
						agentView.setAgentCUID(cuApprovedAgentDtl.getAgentCUID());
						agentView.setEntityStatus(EntityStatus.CU_APPROVED);
						agentService.save(agentView);
					}*/
				}
				
			}
		} 
		catch (Exception e) {
			logger.error("Fetching all cu approved agents call failed", e); 
		}
		finally {
	logger.info("Fetching all cu approved agents Fetched!");
		}		
	
		
	}
	
}
