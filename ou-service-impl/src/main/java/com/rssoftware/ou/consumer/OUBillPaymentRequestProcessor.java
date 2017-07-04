package com.rssoftware.ou.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bbps.schema.Ack;
import org.bbps.schema.BillPaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rssoftware.framework.hibernate.dao.common.TransactionContext;
import com.rssoftware.ou.common.Action;
import com.rssoftware.ou.common.ClearingFeeObj;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.FeeCalculationHelper;
import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.exception.ValidationException;
import com.rssoftware.ou.common.rest.OURestTemplate;
import com.rssoftware.ou.common.utils.LogUtils;
import com.rssoftware.ou.domain.Request;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.model.tenant.ServiceTaxConfView;
import com.rssoftware.ou.model.tenant.ServiceTaxView;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.ServiceTaxConfService;
import com.rssoftware.ou.tenant.service.ServiceTaxService;
import com.rssoftware.ou.tenant.service.TransactionDataService;

import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class OUBillPaymentRequestProcessor implements Consumer<Event<Request>> {

	private static Log logger = LogFactory.getLog(OUBillPaymentRequestProcessor.class);
	private static OURestTemplate restTemplate = OURestTemplate.createInstance();

	@Autowired
	private ParamService paramService;
	
	@Autowired
	private TransactionDataService transactionDataService;
	
	@Autowired
	private InterchangeFeeConfService iFeeConfService;
	
	@Autowired
	private InterchangeFeeService iFeeService;
	
	@Autowired
	private ServiceTaxConfService serviceTaxConfService;
	
	@Autowired
	private ServiceTaxService serviceTaxService;
	
	@Autowired
	private BillerService billerService;
	
	@Autowired
	private ResponseCodeService responseCodeService;

	@Override
	public void accept(Event<Request> event){
		Request request = event.getData();
		TransactionContext.putTenantId(request.getTenantId());
		try {
		String cuDomain = paramService.retrieveStringParamByName(CommonConstants.CU_DOMAIN);		
		
		
		BillPaymentRequest billPaymentRequest = request.getBillPaymentRequest();
		if(billPaymentRequest != null && billPaymentRequest.getAmount() != null && billPaymentRequest.getAmount().getAmt() != null) {		
			billPaymentRequest.getAmount().getAmt().setCustConvFee(getCCF(billPaymentRequest).getBOUTotalCCFPlusTax().toPlainString());
		}				 
		logger.info("Processing BillPaymentRequest with RefId: " + billPaymentRequest.getHead().getRefId() + ".");
		// Forward request to CU
		logger.info("\nOU forwards Bill Payment Request to CU. RefId :" + billPaymentRequest.getHead().getRefId());
		LogUtils.logReqRespMessage(billPaymentRequest, billPaymentRequest.getHead().getRefId(), Action.PAYMENT_REQUEST);
		String cuBillPaymentRequestUrl = cuDomain + CommonConstants.BILL_PAYMENT_REQUEST_URl
				+ billPaymentRequest.getHead().getRefId();
		ResponseEntity<Ack> ack = restTemplate.postForEntity(cuBillPaymentRequestUrl, billPaymentRequest, Ack.class);
		transactionDataService.insert(billPaymentRequest.getHead().getRefId(), billPaymentRequest);
		LogUtils.logReqRespMessage(ack.getBody(), ack.getBody().getRefId(), Action.ACK);
		logger.info("\nOU receives Ack from CU. RefId :" + ack.getBody().getRefId());
	   }
		
		catch (IOException ve) {
			logger.error( ve.getMessage(), ve);
			logger.info("In Excp : " + ve.getMessage());
		}
}
	
	public ClearingFeeObj getCCF(BillPaymentRequest billPaymentRequest) throws IOException{
		String billerId = billPaymentRequest.getBillDetails().getBiller().getId();
		BillerView billerView = billerService.getBillerById(billerId);
		
		String billerCategory = billerView.getBlrCategoryName();
		
		List<InterchangeFeeConfView> icFeeConfs = iFeeConfService.fetchAllInterchangeFeeConfByBillerId(billerId);
		List<InterchangeFeeView> icFees = iFeeService.fetchAllInterchangeFeeByBillerId(billerId);
		
		List<InterchangeFeeConfView> nonDefaultICConfs = new ArrayList<>(2);
		List<InterchangeFeeConfView> defaultICConfs = new ArrayList<>(2);
		
		for (InterchangeFeeConfView icConf:icFeeConfs){
			if (icConf != null){
				if (icConf.isDefault()){
					defaultICConfs.add(icConf);
				}
				else {
					nonDefaultICConfs.add(icConf);
				}
			}
		}
		
		Map<String, List<InterchangeFeeView>> iFeeMap = new HashMap<String, List<InterchangeFeeView>>();
		for (InterchangeFeeView fee:icFees){
			if (fee != null && fee.getFeeCode() != null){
				if (iFeeMap.get(fee.getFeeCode()) == null){
					iFeeMap.put(fee.getFeeCode(), new ArrayList<InterchangeFeeView>(2));
				}
				iFeeMap.get(fee.getFeeCode()).add(fee);
			}
		}
		
		List<ServiceTaxConfView> taxConfs = serviceTaxConfService.fetchAllActiveList();
		List<ServiceTaxView> taxes = serviceTaxService.fetchAllActiveList();
		
		Map<String, List<ServiceTaxView>> serviceTaxMap = new HashMap<>();
		
		if (taxes != null){
			for (ServiceTaxView serviceTax:taxes){
				if (serviceTax != null && serviceTax.getCode() != null){
					if (serviceTaxMap.get(serviceTax.getCode()) == null){
						serviceTaxMap.put(serviceTax.getCode(), new ArrayList<ServiceTaxView>());
					}
					
					serviceTaxMap.get(serviceTax.getCode()).add(serviceTax);	
				}
			}
		}

		ClearingFeeObj cfo = FeeCalculationHelper.calculateCustomerConvenienceFee(
				billPaymentRequest, responseCodeService.getSuccessfulResponseCode(RequestType.PAYMENT, false), nonDefaultICConfs, iFeeMap);	
		
		if (cfo.getBouInterchangeFees() != null && !cfo.getBouInterchangeFees().isEmpty()
				&& cfo.getCouInterchangeFees() != null && !cfo.getCouInterchangeFees().isEmpty()){
			// nothing to do int fees are already calculated
		}
		else { // try with the default fees
			cfo = FeeCalculationHelper.calculateCustomerConvenienceFee(
					billPaymentRequest, responseCodeService.getSuccessfulResponseCode(RequestType.PAYMENT, false) , defaultICConfs, iFeeMap);
		}
		
		FeeCalculationHelper.calculateInterchangeFeeTaxes(cfo, billerCategory, InterchangeFeeDirectionType.C2B, taxConfs, serviceTaxMap);
		return cfo;
	}
}