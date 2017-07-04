package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PMT_CHANNEL_PMT_MODE_CONFIG")
public class PcPmMapping implements Serializable{
	
	private final static long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "SEQ_ID")
	protected Long seqId;
	
	@Column(name = "PC_ID")
	private Integer pcId;
	
	@Column(name = "PM_ID")
	private Integer pmId;
	
	@Column(name = "IS_SUPPORTED")
	private String isSupported;
	
	public Long getSeqId() {
		return seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public Integer getPcId() {
		return pcId;
	}

	public void setPcId(Integer pcId) {
		this.pcId = pcId;
	}

	public Integer getPmId() {
		return pmId;
	}

	public void setPmId(Integer pmId) {
		this.pmId = pmId;
	}

	public String getIsSupported() {
		return isSupported;
	}

	public void setIsSupported(String isSupported) {
		this.isSupported = isSupported;
	}

}
