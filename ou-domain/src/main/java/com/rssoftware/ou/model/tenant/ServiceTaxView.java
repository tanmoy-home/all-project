package com.rssoftware.ou.model.tenant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.rssoftware.ou.common.EntityStatus;

public class ServiceTaxView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3507240107648890152L;

	private long serviceTaxId;
	private String code;
	private String description;
	private Long serviceAmtRangeMin;
	private Long serviceAmtRangeMax;
	private BigDecimal percentTax;
	private BigDecimal flatTax;
	private String effectiveFrom;
	private String effectiveTo;
	private Timestamp activatedTs;
	private String activatedBy;
	private Timestamp deactivatedTs;
	private String deactivatedBy;
	private Timestamp crtnTs;
	private String createdBy;
	private Timestamp updtTs;
	private String updatedBy;
	private EntityStatus entityStatus;

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

	public EntityStatus getEntityStatus() {
		return entityStatus;
	}

	public void setEntityStatus(EntityStatus entityStatus) {
		this.entityStatus = entityStatus;
	}
}