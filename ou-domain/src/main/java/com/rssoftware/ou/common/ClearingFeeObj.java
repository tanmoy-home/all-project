package com.rssoftware.ou.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;

@XmlRootElement
public class ClearingFeeObj implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2099618549626138445L;

	public static class InterchangeFee implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8232512715613503756L;

		public InterchangeFee(){}
		private BigDecimal flatFee;
		private BigDecimal percFee;
		private String feeDescription;
		private InterchangeFeeDirectionType feeDirection;
		
		public InterchangeFee(InterchangeFeeDirectionType feeDirection) {
			this.feeDirection = feeDirection;
		}
		
		public BigDecimal getFlatFee() {
			return flatFee;
		}
		public void setFlatFee(BigDecimal flatFee) {
			this.flatFee = flatFee;
		}
		public BigDecimal getPercFee() {
			return percFee;
		}
		public void setPercFee(BigDecimal percFee) {
			this.percFee = percFee;
		}
		public String getFeeDescription() {
			return feeDescription;
		}
		public void setFeeDescription(String feeDescription) {
			this.feeDescription = feeDescription;
		}
		public InterchangeFeeDirectionType getFeeDirection() {
			return feeDirection;
		}
		public void setFeeDirection(InterchangeFeeDirectionType feeDirection) {
			this.feeDirection = feeDirection;
		}
	}

	public static class ServiceTax implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1776911299128894752L;
		public ServiceTax() {}
		private BigDecimal flatTax;
		private BigDecimal percTax;
		private String taxDescription;
		public BigDecimal getFlatTax() {
			return flatTax;
		}
		public void setFlatTax(BigDecimal flatTax) {
			this.flatTax = flatTax;
		}
		public BigDecimal getPercTax() {
			return percTax;
		}
		public void setPercTax(BigDecimal percTax) {
			this.percTax = percTax;
		}
		public String getTaxDescription() {
			return taxDescription;
		}
		public void setTaxDescription(String taxDescription) {
			this.taxDescription = taxDescription;
		}
	}
	
	private BigDecimal grossCOUSwitchingFeeFlat;
	private BigDecimal grossCOUSwitchingFeePerc;
	private BigDecimal couSwitchingFeeDiscountPerc;
	private BigDecimal netCOUSwitchingFee;
	
	private BigDecimal grossBOUSwitchingFeeFlat;
	private BigDecimal grossBOUSwitchingFeePerc;
	private BigDecimal bouSwitchingFeeDiscountPerc;
	private BigDecimal netBOUSwitchingFee;

	private Map<String, ClearingFeeObj.ServiceTax> couSwitchingFeeTaxes = null;
	private Map<String, ClearingFeeObj.ServiceTax> bouSwitchingFeeTaxes = null;
	
	private Map<String, ClearingFeeObj.ServiceTax> couCCFTaxes = null;
	private Map<String, ClearingFeeObj.ServiceTax> bouCCFTaxes = null;

	private Map<String, ClearingFeeObj.ServiceTax> couBFTaxes = null;
	private Map<String, ClearingFeeObj.ServiceTax> bouBFTaxes = null;

	
	private Map<String, ClearingFeeObj.InterchangeFee> couInterchangeFees = null;
	private Map<String, ClearingFeeObj.InterchangeFee> bouInterchangeFees = null;

	private Map<String, ClearingFeeObj.ServiceTax> cuSwitchingFeeTaxes = null;
	
	@JsonIgnore
	public BigDecimal getCOUTotalSwitchingPlusSTax(){
		BigDecimal total = BigDecimal.ZERO;
		if (netCOUSwitchingFee != null){
			total = total.add(netCOUSwitchingFee);
		}
		
		if (couSwitchingFeeTaxes != null && couSwitchingFeeTaxes.keySet() != null && !couSwitchingFeeTaxes.keySet().isEmpty()){
			for (String feeCode:couSwitchingFeeTaxes.keySet()){
				if (couSwitchingFeeTaxes.get(feeCode) != null){
					if (couSwitchingFeeTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(couSwitchingFeeTaxes.get(feeCode).getFlatTax());
					}
					if (couSwitchingFeeTaxes.get(feeCode).getPercTax() != null){
						total = total.add(couSwitchingFeeTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		
		return total.setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getCOUTotalSwitchingSTax(){
		BigDecimal total = BigDecimal.ZERO;
		if (couSwitchingFeeTaxes != null && couSwitchingFeeTaxes.keySet() != null && !couSwitchingFeeTaxes.keySet().isEmpty()){
			for (String feeCode:couSwitchingFeeTaxes.keySet()){
				if (couSwitchingFeeTaxes.get(feeCode) != null){
					if (couSwitchingFeeTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(couSwitchingFeeTaxes.get(feeCode).getFlatTax());
					}
					if (couSwitchingFeeTaxes.get(feeCode).getPercTax() != null){
						total = total.add(couSwitchingFeeTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		
		return total.setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP);
	}

	
	@JsonIgnore
	public BigDecimal getBOUTotalSwitchingPlusSTax(){
		BigDecimal total = BigDecimal.ZERO;
		if (netBOUSwitchingFee != null){
			total = total.add(netBOUSwitchingFee);
		}
		
		if (bouSwitchingFeeTaxes != null && bouSwitchingFeeTaxes.keySet() != null && !bouSwitchingFeeTaxes.keySet().isEmpty()){
			for (String feeCode:bouSwitchingFeeTaxes.keySet()){
				if (bouSwitchingFeeTaxes.get(feeCode) != null){
					if (bouSwitchingFeeTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(bouSwitchingFeeTaxes.get(feeCode).getFlatTax());
					}
					if (bouSwitchingFeeTaxes.get(feeCode).getPercTax() != null){
						total = total.add(bouSwitchingFeeTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		
		return total.setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getBOUTotalSwitchingSTax(){
		BigDecimal total = BigDecimal.ZERO;
		if (bouSwitchingFeeTaxes != null && bouSwitchingFeeTaxes.keySet() != null && !bouSwitchingFeeTaxes.keySet().isEmpty()){
			for (String feeCode:bouSwitchingFeeTaxes.keySet()){
				if (bouSwitchingFeeTaxes.get(feeCode) != null){
					if (bouSwitchingFeeTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(bouSwitchingFeeTaxes.get(feeCode).getFlatTax());
					}
					if (bouSwitchingFeeTaxes.get(feeCode).getPercTax() != null){
						total = total.add(bouSwitchingFeeTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		
		return total.setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP);
	}

	
	@JsonIgnore
	public BigDecimal getCUTotalSwitchingPlusSTax(){
		BigDecimal total = BigDecimal.ZERO;
		if (netBOUSwitchingFee != null){
			total = total.add(netBOUSwitchingFee.negate());
		}
		if (netCOUSwitchingFee != null){
			total = total.add(netCOUSwitchingFee.negate());
		}
		
		if (cuSwitchingFeeTaxes != null && cuSwitchingFeeTaxes.keySet() != null && !cuSwitchingFeeTaxes.keySet().isEmpty()){
			for (String feeCode:bouSwitchingFeeTaxes.keySet()){
				if (cuSwitchingFeeTaxes.get(feeCode) != null){
					if (cuSwitchingFeeTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(cuSwitchingFeeTaxes.get(feeCode).getFlatTax());
					}
					if (cuSwitchingFeeTaxes.get(feeCode).getPercTax() != null){
						total = total.add(cuSwitchingFeeTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		
		return total.setScale(CommonConstants.FEE_FRACTION_PLACES, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getCOUTotalCCFPlusTax(){
		return getCOUTotalInterchangeRounded(InterchangeFeeDirectionType.C2B).add(getCOUTotalCCFTaxRounded());
	}

	@JsonIgnore
	public BigDecimal getBOUTotalCCFPlusTax(){
		return getBOUTotalInterchangeRounded(InterchangeFeeDirectionType.C2B).add(getBOUTotalCCFTaxRounded());
	}
	
	@JsonIgnore
	public BigDecimal getCOUTotalBFPlusTax(){
		return getCOUTotalInterchangeRounded(InterchangeFeeDirectionType.B2C).add(getCOUTotalBFTaxRounded());
	}

	@JsonIgnore
	public BigDecimal getBOUTotalBFPlusTax(){
		return getBOUTotalInterchangeRounded(InterchangeFeeDirectionType.B2C).add(getBOUTotalBFTaxRounded());
	}
	
	@JsonIgnore
	public BigDecimal getCOUTotalInterchangeRounded(InterchangeFeeDirectionType feeDirection){
		BigDecimal total = BigDecimal.ZERO;
		if (couInterchangeFees != null && couInterchangeFees.keySet() != null && !couInterchangeFees.keySet().isEmpty()){
			for (String icCode:couInterchangeFees.keySet()){
				if (couInterchangeFees.get(icCode) != null && couInterchangeFees.get(icCode).getFeeDirection() == feeDirection){
					if (couInterchangeFees.get(icCode).getFlatFee() != null){
						total = total.add(couInterchangeFees.get(icCode).getFlatFee());
					}
					if (couInterchangeFees.get(icCode).getPercFee() != null){
						total = total.add(couInterchangeFees.get(icCode).getPercFee());
					}
				}
			}
		}
		return total.setScale(0, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getBOUTotalInterchangeRounded(InterchangeFeeDirectionType feeDirection){
		BigDecimal total = BigDecimal.ZERO;
		if (bouInterchangeFees != null && bouInterchangeFees.keySet() != null && !bouInterchangeFees.keySet().isEmpty()){
			for (String icCode:bouInterchangeFees.keySet()){
				if (bouInterchangeFees.get(icCode) != null && bouInterchangeFees.get(icCode).getFeeDirection() == feeDirection){
					if (bouInterchangeFees.get(icCode).getFlatFee() != null){
						total = total.add(bouInterchangeFees.get(icCode).getFlatFee());
					}
					if (bouInterchangeFees.get(icCode).getPercFee() != null){
						total = total.add(bouInterchangeFees.get(icCode).getPercFee());
					}
				}
			}
		}
		return total.setScale(0, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getCOUTotalCCFTaxRounded(){
		BigDecimal total = BigDecimal.ZERO;
		if (couCCFTaxes != null && couCCFTaxes.keySet() != null && !couCCFTaxes.keySet().isEmpty()){
			for (String feeCode:couCCFTaxes.keySet()){
				if (couCCFTaxes.get(feeCode) != null){
					if (couCCFTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(couCCFTaxes.get(feeCode).getFlatTax());
					}
					if (couCCFTaxes.get(feeCode).getPercTax() != null){
						total = total.add(couCCFTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		return total.setScale(0, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getBOUTotalCCFTaxRounded(){
		BigDecimal total = BigDecimal.ZERO;
		if (bouCCFTaxes != null && bouCCFTaxes.keySet() != null && !bouCCFTaxes.keySet().isEmpty()){
			for (String feeCode:bouCCFTaxes.keySet()){
				if (bouCCFTaxes.get(feeCode) != null){
					if (bouCCFTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(bouCCFTaxes.get(feeCode).getFlatTax());
					}
					if (bouCCFTaxes.get(feeCode).getPercTax() != null){
						total = total.add(bouCCFTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		return total.setScale(0, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getCOUTotalBFTaxRounded(){
		BigDecimal total = BigDecimal.ZERO;
		if (couBFTaxes != null && couBFTaxes.keySet() != null && !couBFTaxes.keySet().isEmpty()){
			for (String feeCode:couBFTaxes.keySet()){
				if (couBFTaxes.get(feeCode) != null){
					if (couBFTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(couBFTaxes.get(feeCode).getFlatTax());
					}
					if (couBFTaxes.get(feeCode).getPercTax() != null){
						total = total.add(couBFTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		return total.setScale(0, RoundingMode.HALF_UP);
	}

	@JsonIgnore
	public BigDecimal getBOUTotalBFTaxRounded(){
		BigDecimal total = BigDecimal.ZERO;
		if (bouBFTaxes != null && bouBFTaxes.keySet() != null && !bouBFTaxes.keySet().isEmpty()){
			for (String feeCode:bouBFTaxes.keySet()){
				if (bouBFTaxes.get(feeCode) != null){
					if (bouBFTaxes.get(feeCode).getFlatTax() != null){
						total = total.add(bouBFTaxes.get(feeCode).getFlatTax());
					}
					if (bouBFTaxes.get(feeCode).getPercTax() != null){
						total = total.add(bouBFTaxes.get(feeCode).getPercTax());
					}
				}
			}
		}
		return total.setScale(0, RoundingMode.HALF_UP);
	}

	
	
	
	public BigDecimal getGrossCOUSwitchingFeeFlat() {
		return grossCOUSwitchingFeeFlat;
	}
	public void setGrossCOUSwitchingFeeFlat(BigDecimal grossCOUSwitchingFeeFlat) {
		this.grossCOUSwitchingFeeFlat = grossCOUSwitchingFeeFlat;
	}
	public BigDecimal getGrossCOUSwitchingFeePerc() {
		return grossCOUSwitchingFeePerc;
	}
	public void setGrossCOUSwitchingFeePerc(BigDecimal grossCOUSwitchingFeePerc) {
		this.grossCOUSwitchingFeePerc = grossCOUSwitchingFeePerc;
	}
	public BigDecimal getCouSwitchingFeeDiscountPerc() {
		return couSwitchingFeeDiscountPerc;
	}
	public void setCouSwitchingFeeDiscountPerc(
			BigDecimal couSwitchingFeeDiscountPerc) {
		this.couSwitchingFeeDiscountPerc = couSwitchingFeeDiscountPerc;
	}
	public BigDecimal getNetCOUSwitchingFee() {
		return netCOUSwitchingFee;
	}
	public void setNetCOUSwitchingFee(BigDecimal netCOUSwitchingFee) {
		this.netCOUSwitchingFee = netCOUSwitchingFee;
	}
	public BigDecimal getGrossBOUSwitchingFeeFlat() {
		return grossBOUSwitchingFeeFlat;
	}
	public void setGrossBOUSwitchingFeeFlat(BigDecimal grossBOUSwitchingFeeFlat) {
		this.grossBOUSwitchingFeeFlat = grossBOUSwitchingFeeFlat;
	}
	public BigDecimal getGrossBOUSwitchingFeePerc() {
		return grossBOUSwitchingFeePerc;
	}
	public void setGrossBOUSwitchingFeePerc(BigDecimal grossBOUSwitchingFeePerc) {
		this.grossBOUSwitchingFeePerc = grossBOUSwitchingFeePerc;
	}
	public BigDecimal getBouSwitchingFeeDiscountPerc() {
		return bouSwitchingFeeDiscountPerc;
	}
	public void setBouSwitchingFeeDiscountPerc(
			BigDecimal bouSwitchingFeeDiscountPerc) {
		this.bouSwitchingFeeDiscountPerc = bouSwitchingFeeDiscountPerc;
	}
	public BigDecimal getNetBOUSwitchingFee() {
		return netBOUSwitchingFee;
	}
	public void setNetBOUSwitchingFee(BigDecimal netBOUSwitchingFee) {
		this.netBOUSwitchingFee = netBOUSwitchingFee;
	}
	public Map<String, ClearingFeeObj.InterchangeFee> getCouInterchangeFees() {
		return couInterchangeFees;
	}
	public void setCouInterchangeFees(
			Map<String, ClearingFeeObj.InterchangeFee> couInterchangeFees) {
		this.couInterchangeFees = couInterchangeFees;
	}
	public Map<String, ClearingFeeObj.InterchangeFee> getBouInterchangeFees() {
		return bouInterchangeFees;
	}
	public void setBouInterchangeFees(
			Map<String, ClearingFeeObj.InterchangeFee> bouInterchangeFees) {
		this.bouInterchangeFees = bouInterchangeFees;
	}
	public Map<String, ClearingFeeObj.ServiceTax> getCuSwitchingFeeTaxes() {
		return cuSwitchingFeeTaxes;
	}
	public void setCuSwitchingFeeTaxes(Map<String, ClearingFeeObj.ServiceTax> cuTaxes) {
		this.cuSwitchingFeeTaxes = cuTaxes;
	}
	public Map<String, ClearingFeeObj.ServiceTax> getCouSwitchingFeeTaxes() {
		return couSwitchingFeeTaxes;
	}
	public void setCouSwitchingFeeTaxes(
			Map<String, ClearingFeeObj.ServiceTax> couSwitchingFeeTaxes) {
		this.couSwitchingFeeTaxes = couSwitchingFeeTaxes;
	}
	public Map<String, ClearingFeeObj.ServiceTax> getBouSwitchingFeeTaxes() {
		return bouSwitchingFeeTaxes;
	}
	public void setBouSwitchingFeeTaxes(
			Map<String, ClearingFeeObj.ServiceTax> bouSwitchingFeeTaxes) {
		this.bouSwitchingFeeTaxes = bouSwitchingFeeTaxes;
	}

	public Map<String, ClearingFeeObj.ServiceTax> getCouCCFTaxes() {
		return couCCFTaxes;
	}

	public void setCouCCFTaxes(Map<String, ClearingFeeObj.ServiceTax> couCCFTaxes) {
		this.couCCFTaxes = couCCFTaxes;
	}

	public Map<String, ClearingFeeObj.ServiceTax> getBouCCFTaxes() {
		return bouCCFTaxes;
	}

	public void setBouCCFTaxes(Map<String, ClearingFeeObj.ServiceTax> bouCCFTaxes) {
		this.bouCCFTaxes = bouCCFTaxes;
	}

	public Map<String, ClearingFeeObj.ServiceTax> getCouBFTaxes() {
		return couBFTaxes;
	}

	public void setCouBFTaxes(Map<String, ClearingFeeObj.ServiceTax> couBFTaxes) {
		this.couBFTaxes = couBFTaxes;
	}

	public Map<String, ClearingFeeObj.ServiceTax> getBouBFTaxes() {
		return bouBFTaxes;
	}

	public void setBouBFTaxes(Map<String, ClearingFeeObj.ServiceTax> bouBFTaxes) {
		this.bouBFTaxes = bouBFTaxes;
	}

}


