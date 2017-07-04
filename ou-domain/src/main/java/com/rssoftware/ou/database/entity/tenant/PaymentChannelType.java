package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAYMENT_CHANNELS")
public class PaymentChannelType implements Serializable{
	
	private final static long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "PC_ID")
	protected Long pcId;

	@Column(name = "PC_DESC")
	protected String pcDesc;

	@Column(name = "PC_TAGS")
	protected String pcTags;

	public Long getPcId() {
		return pcId;
	}

	public void setPcId(Long pcId) {
		this.pcId = pcId;
	}

	public String getPcDesc() {
		return pcDesc;
	}

	public void setPcDesc(String pcDesc) {
		this.pcDesc = pcDesc;
	}

	public String getPcTags() {
		return pcTags;
	}

	public void setPcTags(String pcTags) {
		this.pcTags = pcTags;
	}

}
