package com.rssoftware.ou.model.tenant;

import java.io.Serializable;
import java.util.List;

import com.rssoftware.ou.common.ServiceTaxFeeTypes;

public class ServiceTaxConfView  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3507240107648890152L;
	
	private long serviceTaxConfId;
	private ServiceTaxFeeTypes feeType;
	private String feeSubType;
	private String billerCategory;
	private List<String> codes; 
	private String effctvFrom;
	private String effctvTo;

	public long getServiceTaxConfId() {
		return serviceTaxConfId;
	}
	public void setServiceTaxConfId(long serviceTaxConfId) {
		this.serviceTaxConfId = serviceTaxConfId;
	}
	public List<String> getCodes() {
		return codes;
	}
	public void setCodes(List<String> codes) {
		this.codes = codes;
	}
	public String getEffctvFrom() {
		return effctvFrom;
	}
	public void setEffctvFrom(String effctvFrom) {
		this.effctvFrom = effctvFrom;
	}
	public String getEffctvTo() {
		return effctvTo;
	}
	public void setEffctvTo(String effctvTo) {
		this.effctvTo = effctvTo;
	}
	public ServiceTaxFeeTypes getFeeType() {
		return feeType;
	}
	public void setFeeType(ServiceTaxFeeTypes feeType) {
		this.feeType = feeType;
	}
	public String getFeeSubType() {
		return feeSubType;
	}
	public void setFeeSubType(String feeSubType) {
		this.feeSubType = feeSubType;
	}
	public String getBillerCategory() {
		return billerCategory;
	}
	public void setBillerCategory(String billerCategory) {
		this.billerCategory = billerCategory;
	}

}