package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SERVICE_TAX")
public class ServiceTax implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3507240107648890152L;

	@Id
	@Column(name = "service_tax_id")
	private long serviceTaxId;

	@Column(name = "tax_code")
	private String code;

	@Column(name = "tax_description")
	private String description;

	@Column(name = "service_amt_min")
	private Long serviceAmtRangeMin;

	@Column(name = "service_amt_max")
	private Long serviceAmtRangeMax;

	@Column(name = "percent_tax")
	private BigDecimal percentTax;

	@Column(name = "flat_tax")
	private BigDecimal flatTax;

	@Column(name = "effctv_from")
	private String effectiveFrom;

	@Column(name = "effctv_to")
	private String effectiveTo;

	@Column(name = "activated_ts")
	private Timestamp activatedTs;

	@Column(name = "activated_by_user_id")
	private String activatedBy;

	@Column(name = "deactivated_ts")
	private Timestamp deactivatedTs;

	@Column(name = "deactivated_by_user_id")
	private String deactivatedBy;

	@Column(name = "crtn_ts")
	private Timestamp crtnTs;

	@Column(name = "crtn_user_id")
	private String createdBy;

	@Column(name = "updt_ts")
	private Timestamp updtTs;

	@Column(name = "updt_user_id")
	private String updatedBy;

	@Column(name = "entity_status")
	private String entityStatus;

	public long getServiceTaxId() {
		return serviceTaxId;
	}

	public void setServiceTaxId(long serviceTaxId) {
		this.serviceTaxId = serviceTaxId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getServiceAmtRangeMin() {
		return serviceAmtRangeMin;
	}

	public void setServiceAmtRangeMin(Long serviceAmtRangeMin) {
		this.serviceAmtRangeMin = serviceAmtRangeMin;
	}

	public Long getServiceAmtRangeMax() {
		return serviceAmtRangeMax;
	}

	public void setServiceAmtRangeMax(Long serviceAmtRangeMax) {
		this.serviceAmtRangeMax = serviceAmtRangeMax;
	}

	public BigDecimal getPercentTax() {
		return percentTax;
	}

	public void setPercentTax(BigDecimal percentTax) {
		this.percentTax = percentTax;
	}

	public BigDecimal getFlatTax() {
		return flatTax;
	}

	public void setFlatTax(BigDecimal flatTax) {
		this.flatTax = flatTax;
	}

	public String getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(String effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(String effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public Timestamp getActivatedTs() {
		return activatedTs;
	}

	public void setActivatedTs(Timestamp activatedTs) {
		this.activatedTs = activatedTs;
	}

	public String getActivatedBy() {
		return activatedBy;
	}

	public void setActivatedBy(String activatedBy) {
		this.activatedBy = activatedBy;
	}

	public Timestamp getDeactivatedTs() {
		return deactivatedTs;
	}

	public void setDeactivatedTs(Timestamp deactivatedTs) {
		this.deactivatedTs = deactivatedTs;
	}

	public String getDeactivatedBy() {
		return deactivatedBy;
	}

	public void setDeactivatedBy(String deactivatedBy) {
		this.deactivatedBy = deactivatedBy;
	}

	public Timestamp getCrtnTs() {
		return crtnTs;
	}

	public void setCrtnTs(Timestamp crtnTs) {
		this.crtnTs = crtnTs;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getUpdtTs() {
		return updtTs;
	}

	public void setUpdtTs(Timestamp updtTs) {
		this.updtTs = updtTs;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}

}