package com.rssoftware.ou.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bbps.schema.BillPaymentRequest;

import com.rssoftware.ou.common.utils.CommonUtils;
import com.rssoftware.ou.domain.PaymentChannel;
import com.rssoftware.ou.domain.PaymentModeBreakup;
import com.rssoftware.ou.model.tenant.InterchangeFeeConfView;
import com.rssoftware.ou.model.tenant.InterchangeFeeView;
import com.rssoftware.ou.model.tenant.ServiceTaxConfView;
import com.rssoftware.ou.model.tenant.ServiceTaxView;

public class FeeCalculationHelper {
	private static final BigDecimal HUNDRED = new BigDecimal("100");
	
	public static enum ApplicableInterchangeFeeDirection {
		B2C, C2B, BOTH;
	}
	
	public static ClearingFeeObj calculateCustomerConvenienceFee(BillPaymentRequest bpr, String successResponseCode, List<InterchangeFeeConfView> iFeeConfList, Map<String, List<InterchangeFeeView>> iFeeMap){
		MTI mti = MTI.PAYMENT;
		PaymentChannel paymentChannel = CommonUtils.getPaymentChannel(bpr);
		List<PaymentModeBreakup> paymentModeBreakups = Arrays.asList(CommonUtils.getPaymentModeBreakups(bpr));
		ClearingFeeObj clearingFeeObj = null;
		if (iFeeConfList != null && iFeeMap != null){
			Map<Integer, Set<String>> paymentModeIndexFees = new HashMap<Integer, Set<String>>();
			clearingFeeObj = new ClearingFeeObj();

			for (InterchangeFeeConfView iFeeConf:iFeeConfList){
				List<Integer> applicablePmtModeIndices = getApplicableInterchangeFeePaymentModeIndices(paymentModeBreakups, mti, paymentChannel, successResponseCode, iFeeConf);
				
				if (applicablePmtModeIndices != null && applicablePmtModeIndices.size() == 1 && iFeeConf.getFees() != null && iFeeConf.getFees().size() > 0){
					for (String feeCode:iFeeConf.getFees()){
						if (feeCode != null
								){
							long applicableAmt = 0;
							if (paymentModeBreakups != null 
									&& !paymentModeBreakups.isEmpty()){
								applicableAmt = paymentModeBreakups.get(applicablePmtModeIndices.get(0)).getAmount();
							}
							else {
								applicableAmt = Long.parseLong(bpr.getAmount().getAmt().getAmount());
							}
							
							applyInterchangeFee(paymentModeIndexFees, clearingFeeObj, feeCode, true, applicableAmt, iFeeMap, applicablePmtModeIndices.get(0), ApplicableInterchangeFeeDirection.C2B);		
						}
					}
				}
				else if (applicablePmtModeIndices != null && applicablePmtModeIndices.size() == 2 && iFeeConf.getFees() != null && iFeeConf.getFees().size() > 0){ // for splitpay
					for (String feeCode:iFeeConf.getFees()){
						if (feeCode != null 
								){
							long amt0 = paymentModeBreakups.get(0).getAmount();
							long amt1 = paymentModeBreakups.get(1).getAmount();
							
							applyInterchangeFee(paymentModeIndexFees, clearingFeeObj, feeCode, true, amt0, iFeeMap, 0, ApplicableInterchangeFeeDirection.C2B);		
							applyInterchangeFee(paymentModeIndexFees, clearingFeeObj, feeCode, true, amt1, iFeeMap, 1, ApplicableInterchangeFeeDirection.C2B);

						}
					}
				}

			}
		}
		return clearingFeeObj;
	}
	
	private static List<Integer> getApplicableInterchangeFeePaymentModeIndices(List<PaymentModeBreakup> paymentModeBreakups, 
			MTI mtiInput, PaymentChannel paymentChannel, String responseCode, InterchangeFeeConfView iFeeConf){
		
		// what is the effective MTI?
		MTI mti = mtiInput;
		if (mtiInput == MTI.FORCE_PAYMENT 
				|| mtiInput == MTI.FP_PRE_ARB 
				|| mtiInput == MTI.FP_GF_FULL 
				|| mtiInput == MTI.FP_GF_PART 
				|| mtiInput == MTI.FP_ARB){
			mti = MTI.PAYMENT;
		}
		
		if (paymentModeBreakups != null && !paymentModeBreakups.isEmpty() && iFeeConf != null){ 
			List<Integer> indices = null;
			int cnt = 0;
			for (PaymentModeBreakup pmb:paymentModeBreakups){
				if (pmb != null){ // MTI		PAYMENT_MODE	PAYMENT_CHANNEL			RESPONSE_CODE
					if ((iFeeConf.getMti() == null || mti == iFeeConf.getMti())
							&& (iFeeConf.getPaymentMode() == null || pmb.getPaymentMode() == iFeeConf.getPaymentMode())
							&& (iFeeConf.getPaymentChannel() == null || paymentChannel == iFeeConf.getPaymentChannel())
							&& (iFeeConf.getResponseCode() == null || iFeeConf.getResponseCode().equals(responseCode))
							){
						if (indices == null){
							indices = new ArrayList<Integer>(2);
						}
						indices.add(cnt);
					}
				}
				cnt++;
			}
			return indices;
		}
		else if (iFeeConf != null && (paymentModeBreakups == null || paymentModeBreakups.isEmpty())){
			List<Integer> indices = null;
			if ((iFeeConf.getMti() == null || mti == iFeeConf.getMti())
					&& (iFeeConf.getPaymentMode() == null)
					&& (iFeeConf.getPaymentChannel() == null || paymentChannel == iFeeConf.getPaymentChannel())
					&& (iFeeConf.getResponseCode() == null || iFeeConf.getResponseCode().equals(responseCode))
					){
				if (indices == null){
					indices = new ArrayList<Integer>(2);
				}
				indices.add(0);
			}
			return indices;
		}
		return null;
	}

	private static void applyInterchangeFee(Map<Integer, Set<String>> paymentModeIndexFees, ClearingFeeObj clearingFeeObj, String feeCode, boolean applyFlatFee, long amount, Map<String, List<InterchangeFeeView>> iFeeMap, Integer paymentModeIndex, ApplicableInterchangeFeeDirection applicableFeeDirection){
		if (feeCode != null && iFeeMap.get(feeCode) != null && !iFeeMap.get(feeCode).isEmpty()
				&& (paymentModeIndexFees.get(paymentModeIndex) == null || !paymentModeIndexFees.get(paymentModeIndex).contains(feeCode)) 
				){ 
			if (paymentModeIndexFees.get(paymentModeIndex) == null){
				paymentModeIndexFees.put(paymentModeIndex, new HashSet<String>());
			}
			paymentModeIndexFees.get(paymentModeIndex).add(feeCode);
			
			for (InterchangeFeeView iFee:iFeeMap.get(feeCode)){				
				if (iFee != null && iFee.getFeeDirection() != null && (applicableFeeDirection == ApplicableInterchangeFeeDirection.BOTH || applicableFeeDirection.name().equalsIgnoreCase(iFee.getFeeDirection().name()))  
						&& (iFee.getTranAmtRangeMax() >= amount && amount >= iFee.getTranAmtRangeMin())){
					ClearingFeeObj.InterchangeFee calcFee = new ClearingFeeObj.InterchangeFee(iFee.getFeeDirection());  
					if (applyFlatFee && iFee.getFlatFee() != null){
						calcFee.setFlatFee(iFee.getFlatFee().setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP));
					}
					if (iFee.getPercentFee() != null){
						double perc = iFee.getPercentFee().doubleValue();
						double percFee = amount * (perc / 100);
						
						calcFee.setPercFee((new BigDecimal(percFee)).setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP));
					}
					
					ClearingFeeObj.InterchangeFee calcFeeOpp = new ClearingFeeObj.InterchangeFee(iFee.getFeeDirection());
					calcFeeOpp.setFlatFee(calcFee.getFlatFee() != null ? calcFee.getFlatFee().negate() : null);
					calcFeeOpp.setPercFee(calcFee.getPercFee() != null ? calcFee.getPercFee().negate() : null);
					
					if (clearingFeeObj.getCouInterchangeFees() == null){
						clearingFeeObj.setCouInterchangeFees(new HashMap<String, ClearingFeeObj.InterchangeFee>());
					}
					if (clearingFeeObj.getBouInterchangeFees() == null){
						clearingFeeObj.setBouInterchangeFees(new HashMap<String, ClearingFeeObj.InterchangeFee>());
					}
					
					if (iFee.getFeeDirection() == InterchangeFeeDirectionType.B2C){
						clearingFeeObj.getCouInterchangeFees().put(feeCode, addInterchangeFees(clearingFeeObj.getCouInterchangeFees().get(feeCode),calcFee));
						clearingFeeObj.getBouInterchangeFees().put(feeCode, addInterchangeFees(clearingFeeObj.getBouInterchangeFees().get(feeCode),calcFeeOpp));
					}
					else if (iFee.getFeeDirection() == InterchangeFeeDirectionType.C2B){
						clearingFeeObj.getCouInterchangeFees().put(feeCode, addInterchangeFees(clearingFeeObj.getCouInterchangeFees().get(feeCode),calcFeeOpp));
						clearingFeeObj.getBouInterchangeFees().put(feeCode, addInterchangeFees(clearingFeeObj.getBouInterchangeFees().get(feeCode),calcFee));
					}
					
					if (clearingFeeObj.getCouInterchangeFees().get(feeCode) != null){
						clearingFeeObj.getCouInterchangeFees().get(feeCode).setFeeDescription(iFee.getFeeDesc());
					}
					if (clearingFeeObj.getBouInterchangeFees().get(feeCode) != null){
						clearingFeeObj.getBouInterchangeFees().get(feeCode).setFeeDescription(iFee.getFeeDesc());
					}						
				}				
			}
		}
	}

	
	
	
	private static ClearingFeeObj.InterchangeFee addInterchangeFees(ClearingFeeObj.InterchangeFee fee1, ClearingFeeObj.InterchangeFee fee2){
		if (fee1 == null && fee2 == null){
			return null;
		}
		else if (fee1 != null && fee2 == null){
			return fee1;
		}
		else if (fee1 == null && fee2 != null){
			return fee2;
		}
		else { // both contain value
			if (fee1.getFlatFee() == null){
				fee1.setFlatFee(fee2.getFlatFee());
			}
			else if (fee2.getFlatFee() != null){ // implicitly fee1.flatfee is also != null
				fee1.setFlatFee(fee1.getFlatFee().add(fee2.getFlatFee()));
			}

			if (fee1.getPercFee() == null){
				fee1.setPercFee(fee2.getPercFee());
			}
			else if (fee2.getPercFee() != null){ // implicitly fee1.percfee is also != null
				fee1.setPercFee(fee1.getPercFee().add(fee2.getPercFee()));
			}

			
			return fee1;
		}
	}
	
	public static void calculateInterchangeFeeTaxes(ClearingFeeObj cfo, String billerCategory, InterchangeFeeDirectionType feeDirection, List<ServiceTaxConfView> serviceTaxConfs, Map<String, List<ServiceTaxView>> serviceTaxMap){
		if (cfo != null && serviceTaxConfs != null && serviceTaxMap != null && feeDirection != null){
			BigDecimal couTotalIC = cfo.getCOUTotalInterchangeRounded(feeDirection);
			BigDecimal bouTotalIC = cfo.getBOUTotalInterchangeRounded(feeDirection);
			
			List<ServiceTaxConfView> filteredTaxConfigList = new ArrayList<ServiceTaxConfView>(2);
			
			if (billerCategory != null){
				for (ServiceTaxConfView serviceTaxConf:serviceTaxConfs){
					if (serviceTaxConf != null){
						// check if this config is applicable
						if ((serviceTaxConf.getFeeType() == null || serviceTaxConf.getFeeType() == ServiceTaxFeeTypes.INTERCHANGE)
								&& (serviceTaxConf.getFeeSubType() == null || serviceTaxConf.getFeeSubType().equals(feeDirection.name()))
								// specific biller category check
								&& billerCategory.equals(serviceTaxConf.getBillerCategory())){
							// the config is applicable, add to list
							filteredTaxConfigList.add(serviceTaxConf);
						}
					}
				}
			}
			
			if (filteredTaxConfigList.isEmpty()){
				// biller category wise filter didnt match, check generic config with biller category == null
				for (ServiceTaxConfView serviceTaxConf:serviceTaxConfs){
					if (serviceTaxConf != null){
						// check if this config is applicable
						if ((serviceTaxConf.getFeeType() == null || serviceTaxConf.getFeeType() == ServiceTaxFeeTypes.INTERCHANGE)
								&& (serviceTaxConf.getFeeSubType() == null || serviceTaxConf.getFeeSubType().equals(feeDirection.name()))
								&& serviceTaxConf.getBillerCategory() == null){
							// the config is applicable, add to list
							filteredTaxConfigList.add(serviceTaxConf);
						}
					}
				}
			}
			
			for (ServiceTaxConfView serviceTaxConf:filteredTaxConfigList){
				// pull out the fee codes
				if (serviceTaxConf.getCodes() != null){
					for (String serviceTaxCode:serviceTaxConf.getCodes()){
						if (serviceTaxCode != null && serviceTaxMap.get(serviceTaxCode) != null && !serviceTaxMap.get(serviceTaxCode).isEmpty()){
							for (ServiceTaxView serviceTax:serviceTaxMap.get(serviceTaxCode)){
								if (serviceTax != null && (serviceTax.getFlatTax() != null || serviceTax.getPercentTax() != null)){
									long maxRange = serviceTax.getServiceAmtRangeMax();
									long minRange = serviceTax.getServiceAmtRangeMin();

									if (couTotalIC.abs().longValue() >= minRange && couTotalIC.abs().longValue() <= maxRange){
										BigDecimal couPercTax = calculatePercentageTaxAmount(serviceTax.getPercentTax(), couTotalIC);
										if (feeDirection == InterchangeFeeDirectionType.B2C){
											if (cfo.getCouBFTaxes() == null){
												cfo.setCouBFTaxes(new HashMap<String, ClearingFeeObj.ServiceTax>());
											}
										}
										else if (feeDirection == InterchangeFeeDirectionType.C2B){
											if (cfo.getCouCCFTaxes() == null){
												cfo.setCouCCFTaxes(new HashMap<String, ClearingFeeObj.ServiceTax>());
											}
										}
										ClearingFeeObj.ServiceTax stax = new ClearingFeeObj.ServiceTax();
										if (serviceTax.getFlatTax() != null){
											if (couTotalIC.compareTo(BigDecimal.ZERO) < 0){
												stax.setFlatTax(serviceTax.getFlatTax().negate());		
											}
											else {
												stax.setFlatTax(serviceTax.getFlatTax());
											}
										}
										stax.setPercTax(couPercTax);
										stax.setTaxDescription(serviceTax.getDescription());
										
										if (feeDirection == InterchangeFeeDirectionType.B2C){
											cfo.getCouBFTaxes().put(serviceTaxCode, addServiceTaxes(stax,cfo.getCouBFTaxes().get(serviceTaxCode)));
										}
										else if (feeDirection == InterchangeFeeDirectionType.C2B){
											cfo.getCouCCFTaxes().put(serviceTaxCode, addServiceTaxes(stax,cfo.getCouCCFTaxes().get(serviceTaxCode)));
										}
									}
									
									if (bouTotalIC.abs().longValue() >= minRange && bouTotalIC.abs().longValue() <= maxRange){
										BigDecimal bouPercTax = calculatePercentageTaxAmount(serviceTax.getPercentTax(), bouTotalIC);
										if (feeDirection == InterchangeFeeDirectionType.B2C){
											if (cfo.getBouBFTaxes() == null){
												cfo.setBouBFTaxes(new HashMap<String, ClearingFeeObj.ServiceTax>());
											}
										}
										else if (feeDirection == InterchangeFeeDirectionType.C2B){
											if (cfo.getBouCCFTaxes() == null){
												cfo.setBouCCFTaxes(new HashMap<String, ClearingFeeObj.ServiceTax>());
											}
										}

										ClearingFeeObj.ServiceTax stax = new ClearingFeeObj.ServiceTax();
										if (serviceTax.getFlatTax() != null){
											if (bouTotalIC.compareTo(BigDecimal.ZERO) < 0){
												stax.setFlatTax(serviceTax.getFlatTax().negate());		
											}
											else {
												stax.setFlatTax(serviceTax.getFlatTax());
											}
										}
										stax.setPercTax(bouPercTax);
										stax.setTaxDescription(serviceTax.getDescription());
										if (feeDirection == InterchangeFeeDirectionType.B2C){
											cfo.getBouBFTaxes().put(serviceTaxCode, addServiceTaxes(stax,cfo.getBouBFTaxes().get(serviceTaxCode)));
										}
										else if (feeDirection == InterchangeFeeDirectionType.C2B){
											cfo.getBouCCFTaxes().put(serviceTaxCode, addServiceTaxes(stax,cfo.getBouCCFTaxes().get(serviceTaxCode)));
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private static ClearingFeeObj.ServiceTax addServiceTaxes(ClearingFeeObj.ServiceTax tax1, ClearingFeeObj.ServiceTax tax2){
		if (tax1 == null && tax2 == null){
			return null;
		}
		else if (tax1 != null && tax2 == null){
			return tax1;
		}
		else if (tax1 == null && tax2 != null){
			return tax2;
		}
		else { // both contain value
			if (tax1.getFlatTax() == null){
				tax1.setFlatTax(tax2.getFlatTax());
			}
			else if (tax2.getFlatTax() != null){ // implicitly fee1.flatfee is also != null
				tax1.setFlatTax(tax1.getFlatTax().add(tax2.getFlatTax()));
			}

			if (tax1.getPercTax() == null){
				tax1.setPercTax(tax2.getPercTax());
			}
			else if (tax2.getPercTax() != null){ // implicitly fee1.percfee is also != null
				tax1.setPercTax(tax1.getPercTax().add(tax2.getPercTax()));
			}
			return tax1;
		}
	}

	private static BigDecimal calculatePercentageTaxAmount(BigDecimal percTax, BigDecimal feeAmt){
		if (percTax == null){
			return BigDecimal.ZERO;
		}
		if (feeAmt == null){
			feeAmt = BigDecimal.ZERO;
		}
		return feeAmt.multiply(percTax).divide(CommonConstants.HUNDRED).setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP);
	}
}