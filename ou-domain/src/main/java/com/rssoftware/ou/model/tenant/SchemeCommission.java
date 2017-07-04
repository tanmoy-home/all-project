package com.rssoftware.ou.model.tenant;

import java.io.Serializable;
import java.math.BigDecimal;

public class SchemeCommission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal agentPercentFee;

	private BigDecimal agentFlatFee;

	private BigDecimal aiPercentFee;

	private Long tranAmtRangeMin;

	private Long tranAmtRangeMax;

	private Long tranCountRangeMin;

	private Long tranCountRangeMax;

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

	public Long getTranCountRangeMin() {
		return tranCountRangeMin;
	}

	public void setTranCountRangeMin(Long tranCountRangeMin) {
		this.tranCountRangeMin = tranCountRangeMin;
	}

	public Long getTranCountRangeMax() {
		return tranCountRangeMax;
	}

	public void setTranCountRangeMax(Long tranCountRangeMax) {
		this.tranCountRangeMax = tranCountRangeMax;
	}

}
