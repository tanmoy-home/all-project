package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rssoftware.ou.common.ServiceTaxFeeTypes;

@Entity
@Table(name="SERVICE_TAX_CONFIG")
public class ServiceTaxConf implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8513332569685870218L;
	
	@Id
	@Column(name="service_tax_cfg_id")
	private long serviceTaxConfId;
	
	@Column(name="fee_type")
	@Enumerated(EnumType.STRING)
	private ServiceTaxFeeTypes feeType;
	
	@Column(name="fee_subtype")
	private String feeSubType;
	
	@Column(name="blr_category_name")
	private String billerCategory;
	
	@Column(name="service_tax_codes")
	private String codes; 
	
	@Column(name="effctv_from")
	private String effctvFrom;
	
	@Column(name="effctv_to")	
	private String effctvTo;
	
	public long getServiceTaxConfId() {
		return serviceTaxConfId;
	}
	public void setServiceTaxConfId(long serviceTaxConfId) {
		this.serviceTaxConfId = serviceTaxConfId;
	}
	public String getCodes() {
		return codes;
	}
	public void setCodes(String codes) {
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