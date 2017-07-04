package com.rssoftware.ou.tenant.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bbps.schema.AgentType;
import org.bbps.schema.AmountType;
import org.bbps.schema.AmtType;
import org.bbps.schema.BillPaymentRequest;
import org.bbps.schema.PmtMtdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rssoftware.ou.common.ClearingFeeObj;
import com.rssoftware.ou.common.CommonConstants;
import com.rssoftware.ou.common.FeeCalculationHelper;
import com.rssoftware.ou.common.InterchangeFeeDirectionType;
import com.rssoftware.ou.common.RequestType;
import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.common.utils.RequestResponseGenerator;
import com.rssoftware.ou.model.tenant.BillerView;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.model.tenant.ServiceTaxConfView;
import com.rssoftware.ou.model.tenant.ServiceTaxView;
import com.rssoftware.ou.tenant.service.BillerService;
import com.rssoftware.ou.tenant.service.CCFCalculatorService;
import com.rssoftware.ou.tenant.service.InterchangeFeeConfService;
import com.rssoftware.ou.tenant.service.InterchangeFeeService;
import com.rssoftware.ou.tenant.service.ParamService;
import com.rssoftware.ou.tenant.service.ResponseCodeService;
import com.rssoftware.ou.tenant.service.ServiceTaxConfService;
import com.rssoftware.ou.tenant.service.ServiceTaxService;

@Service
public class CCFCalculatorServiceImpl implements CCFCalculatorService {
	
	@Autowired
	private InterchangeFeeConfService interchangeFeeConfService;
	
	@Autowired
	private InterchangeFeeService interchangeFeeService;
		
	@Autowired
	private ServiceTaxConfService serviceTaxConfService;
	
	@Autowired
	private ServiceTaxService serviceTaxService;
	
	@Autowired
	private ResponseCodeService responseCodeService;
	
	@Autowired
	private BillerService billerService;
	
	@Autowired
	private ParamService paramService;
	@Override
	public String calculateCCF(String billerId, String billAmount, AgentType agent, PmtMtdType pmtMtdType) throws IOException{
		BillerView billerView = billerService.getBillerById(billerId);
		String billerCategory = billerView.getBlrCategoryName();
		if (billAmount == null || CommonConstants.EMPTY_STRING.equalsIgnoreCase(billAmount))
			return CommonConstants.EMPTY_STRING;
		//int billAmountInPaise =	new BigDecimal(billAmount).multiply(CommonConstants.HUNDRED).intValue();

		BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
		AmountType amount = new AmountType();
		AmtType amt = new AmtType();
		amt.setAmount(String.valueOf(billAmount));
		amount.setAmt(amt);
		billPaymentRequest.setAmount(amount);

		if (agent == null) {
			String defaultAgentId = paramService.retrieveStringParamByName(CommonConstants.DEFAULT_AGENT);
			String defaultMacAddress = paramService.retrieveStringParamByName(CommonConstants.DEFAULT_MAC_ADDRESS);
			agent = RequestResponseGenerator.getAgent(defaultAgentId);
			agent.setDevice(RequestResponseGenerator.getAgentDeviceForInternetBankingChannel(CommonUtils.getHostname(),
					defaultMacAddress));
		}
		billPaymentRequest.setAgent(agent);

		//PmtMtdType pmtMtdType = new PmtMtdType();
		//pmtMtdType.setPaymentMode(PaymentMode.Internet_Banking.getExpandedForm());
		//pmtMtdType.setSplitPay(SpltPayType.NO);
		//pmtMtdType.setQuickPay(QckPayType.NO);
		billPaymentRequest.setPaymentMethod(pmtMtdType);

		List<InterchangeFeeConfView> icFeeConfs = interchangeFeeConfService.fetchAllInterchangeFeeConfByBillerId(billerId);
		List<InterchangeFeeView> icFees = interchangeFeeService.fetchAllInterchangeFeeByBillerId(billerId);

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
		if(null == responseCodeService.getSuccessfulResponseCode(RequestType.PAYMENT, false)) {
			responseCodeService.refresh();
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


		return String.valueOf(cfo.getBOUTotalCCFPlusTax().longValue());
	}	
}