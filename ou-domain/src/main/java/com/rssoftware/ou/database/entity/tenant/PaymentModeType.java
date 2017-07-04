package com.rssoftware.ou.database.entity.tenant;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAYMENT_MODES")
public class PaymentModeType implements Serializable{

private final static long serialVersionUID = -1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "PM_ID")
	protected Long pmId;

	@Column(name = "PM_DESC")
	protected String pmDesc;

	@Column(name = "PM_INFO")
	protected String pmInfo;

	public Long getPmId() {
		return pmId;
	}

	public void setPmId(Long pmId) {
		this.pmId = pmId;
	}

	public String getPmDesc() {
		return pmDesc;
	}

	public void setPmDesc(String pmDesc) {
		this.pmDesc = pmDesc;
	}

	public String getPmInfo() {
		return pmInfo;
	}

	public void setPmInfo(String pmInfo) {
		this.pmInfo = pmInfo;
	}

}
