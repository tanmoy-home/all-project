package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SCHEME_COMMISSION_DETAILS")
@NamedQuery(name = "SchemeCommissionDetail.findAll", query = "SELECT a FROM SchemeCommissionDetail a")
public class SchemeCommissionDetail implements Serializable {

	private static final long serialVersionUID = 1995393066527586414L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "COMMISSION_ID")
	private Long commissionId;

	@Column(name = "SCHEME_UNIQUE_ID")
	private String schemeUniqueId;

	@Column(name = "TRAN_AMT_RANGE_MIN")
	private Long tranAmtRangeMin;

	@Column(name = "TRAN_AMT_RANGE_MAX")
	private Long tranAmtRangeMax;

	@Column(name = "TRAN_COUNT_RANGE_MAX")
	private Long tranCountRangeMax;

	@Column(name = "TRAN_COUNT_RANGE_MIN")
	private Long tranCountRangeMin;

	@Column(name = "AGENT_PERCENT_FEE")
	private BigDecimal agentPercentFee;

	@Column(name = "AGENT_FLAT_FEE")
	private BigDecimal agentFlatFee;

	@Column(name = "AI_PERCENT_FEE")
	private BigDecimal aiPercentFee;

	public Long getCommissionId() {
		return commissionId;
	}

	public void setCommissionId(Long commissionId) {
		this.commissionId = commissionId;
	}

	public BigDecimal getAgentPercentFee() {
		return agentPercentFee;
	}

	public void setAgentPercentFee(BigDecimal agentPercentFee) {
		this.agentPercentFee = agentPercentFee;
	}

	public BigDecimal getAgentFlatFee() {
		return agentFlatFee;
	}

	public void setAgentFlatFee(BigDecimal agentFlatFee) {
		this.agentFlatFee = agentFlatFee;
	}

	public BigDecimal getAiPercentFee() {
		return aiPercentFee;
	}

	public void setAiPercentFee(BigDecimal aiPercentFee) {
		this.aiPercentFee = aiPercentFee;
	}

	public String getSchemeUniqueId() {
		return schemeUniqueId;
	}

	public void setSchemeUniqueId(String schemeUniqueId) {
		this.schemeUniqueId = schemeUniqueId;
	}

	public Long getTranAmtRangeMin() {
		return tranAmtRangeMin;
	}

	public void setTranAmtRangeMin(Long tranAmtRangeMin) {
		this.tranAmtRangeMin = tranAmtRangeMin;
	}

	public Long getTranAmtRangeMax() {
		return tranAmtRangeMax;
	}

	public void setTranAmtRangeMax(Long tranAmtRangeMax) {
		this.tranAmtRangeMax = tranAmtRangeMax;
	}

	public Long getTranCountRangeMax() {
		return tranCountRangeMax;
	}

	public void setTranCountRangeMax(Long tranCountRangeMax) {
		this.tranCountRangeMax = tranCountRangeMax;
	}

	public Long getTranCountRangeMin() {
		return tranCountRangeMin;
	}

	public void setTranCountRangeMin(Long tranCountRangeMin) {
		this.tranCountRangeMin = tranCountRangeMin;
	}

}
